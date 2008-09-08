// Copyright (C) 2008 Luciano Garcia-Banuelos <lgbanuelos@gmail.com>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package translation;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.stp.bpmn.Activity;
import org.eclipse.stp.bpmn.ActivityType;
import org.eclipse.stp.bpmn.Association;
import org.eclipse.stp.bpmn.BpmnFactory;
import org.eclipse.stp.bpmn.Graph;
import org.eclipse.stp.bpmn.NamedBpmnObject;
import org.eclipse.stp.bpmn.Pool;
import org.eclipse.stp.bpmn.SequenceEdge;
import org.eclipse.stp.bpmn.SubProcess;
import org.eclipse.stp.bpmn.TextAnnotation;
import org.eclipse.stp.bpmn.Vertex;

import translation.analysis.cfa.CFAJoin;
import translation.analysis.cfa.CFANode;
import translation.analysis.cfa.CFASplit;
import translation.analysis.cfa.ControlFlowAnalyzer;
import translation.analysis.sese.SESE;
import translation.analysis.sese.SESE.Region;

/**
 * This class builds a Process Structure Tree of a BPMN model using the Johnson's SESE regions
 * and performs a control flow analysis to identify patterns that can be translated straightforwardly
 * to BPEL code.
 * 
 * It can be thought as a preprocessor. It takes a BPMN "flat" model and rewrites it into a new model
 * which uses subprocess to enclose every SESE region along with some information to drive the translation
 * into BPEL. Note that the resulting model reflects a hierarchy similar to the Process Structure Tree
 * if we look at the containment of suprocesses.
 *
 * For details on the underlying algorithms, take a look at:
 * 
 * 		Garcia-Banuelos, L. Pattern Identification and Classification in the
 * 		translation from BPMN to BPEL. In Proc. OTM 2008, Springer-Verlag, 2008
 * 
 * @author Luciano Garcia-Banuelos (email: lgbanuelos@gmail.com
 */
public class Rewriter {
	static BpmnFactory factory = BpmnFactory.eINSTANCE;
	static EcoreFactory efactory = EcoreFactory.eINSTANCE;
	HashMap<Vertex, CFANode> vertexMap = new HashMap<Vertex, CFANode>();
	HashSet<SequenceEdge> edgeSet = new HashSet<SequenceEdge>();
	LinkedList<Vertex> lvertices = new LinkedList<Vertex>();
	LinkedList<SequenceEdge> ledges = new LinkedList<SequenceEdge>();

	public void rewrite(Pool pool) {
		SESE sese = new SESE(pool);
		process(pool, sese.topLevel);
	}

	/**
	 * Performs a post-order traversal to the Process Structure Tree and controls the
	 * control flow analysis and the pattern identification.
	 * 
	 * @param graph
	 * @param region
	 */
	private void process(Graph graph, SESE.Region region) {
		if (!region.isTopLevel()
				&& ((SequenceEdge) region.entry).getTarget() == ((SequenceEdge) region.exit)
						.getSource()) {
			return; // Trivial SESE region
		}
		for (Object child : region.children())
			process(graph, (SESE.Region) child);

		Activity entry, exit;
		if (region.isTopLevel()) {
			entry = (Activity) region.nodes().toArray()[0];
			exit = (Activity) region.nodes().toArray()[region.nodes().size() - 1];
		} else {
			entry = (Activity) ((SequenceEdge) region.entry).getTarget();
			exit = (Activity) ((SequenceEdge) region.exit).getSource();
		}

		ControlFlowAnalyzer.cfa(region, entry, vertexMap, edgeSet);

		map(region, graph, entry, exit);
	}

	
	/**
	 * Copies the subgraph in a SESE region into a BPMN subprocess. The subprocess is then
	 * annotated with the pattern identified (e.g. "Sequence", "Structured cycle", etc.)
	 * 
	 * @param region	SESE region containing the subgraph to be factored-out
	 * @param graph		Parent graph (needed to insert there the newly created subprocess and
	 * 					all required edges)
	 * @param entry		Entry node for the SESE region
	 * @param exit		Exit node for the SESE region
	 * @param type		String containing the type of pattern identified within the SESE region
	 * @param vertices	Set of vertices composing the subgraph (and that should be factored-out)
	 * @param edges		Set of edges composing the the subgraph (and that should be factored-out)
	 * @return	BPMN subprocesses containing the SESE region
	 */
	private SubProcess factorOutRegion(SESE.Region region, Graph graph,
			Activity entry, Activity exit, String type,
			Collection<Vertex> vertices, Collection<SequenceEdge> edges) {
		SubProcess sp = factory.createSubProcess();
		sp.setActivityType(ActivityType.SUB_PROCESS_LITERAL);

		// -----------------------------------------
		// The following lines create a TextAnnotation which will hold a string
		// encoding the type of pattern identified within the SESE region
		EAnnotation annotation = efactory.createEAnnotation();
		sp.setName(entry.getName() + "-" + exit.getName());
		annotation.setSource(type);
		sp.getEAnnotations().add(annotation);
		TextAnnotation textAnnotation = BpmnFactory.eINSTANCE
				.createTextAnnotation();
		textAnnotation.setName(type);
		Association association = BpmnFactory.eINSTANCE.createAssociation();
		association.setSource(textAnnotation);
		association.setTarget(sp);
		// -----------------------------------------

		// Adjust incident edges (to the entry/from the exit)
		entry.getIncomingEdges().remove(region.entry);
		exit.getOutgoingEdges().remove(region.exit);
		if (!region.isTopLevel()) {
			((SequenceEdge) region.entry).setTarget(sp);
			((SequenceEdge) region.exit).setSource(sp);
			sp.getIncomingEdges().add(((SequenceEdge) region.entry));
			sp.getOutgoingEdges().add(((SequenceEdge) region.exit));
		}

		// Move Vertices and Edges to the right place
		sp.getVertices().addAll(vertices);
		sp.getSequenceEdges().addAll(edges);
		graph.getVertices().removeAll(sp.getVertices());
		graph.getSequenceEdges().removeAll(sp.getSequenceEdges());
		return sp;
	}

	
	/**
	 * Prepends a start event to a recently factored-out subgraph. Consider the following scenario:
	 * A structured cycle has been factored out. The entry node is XOR join gateway, but it only has
	 * a single outgoing edge and a single incoming edge (the incident edge has been removed, and it
	 * now refers to the subprocess holding the subgraph). There is no way to know if this gateway
	 * is playing the role of a split or of a join. That is why a start event is added, along with an
	 * edge from this start event to the XOR join gateway to disambiguate its role.
	 * 
	 * Note that this start event is annotated as "DUMMY". This allows us to avoid its translation.
	 * 
	 * @param graph	Parent graph, i.e. the subprocess holding the SESE region
	 * @param entry	Current entry node to the SESE region
	 * @return The start event that has been added
	 */
	private Activity prependStartEvent(Graph graph, Activity entry) {
		Activity result = entry;
		
		// The start event is not added if the entry node to the SESE region
		// is already a start event
		if (entry.getActivityType() != ActivityType.EVENT_START_EMPTY_LITERAL
				&& entry.getActivityType() != ActivityType.EVENT_START_MESSAGE_LITERAL) {
			result = BpmnFactory.eINSTANCE.createActivity();
			result.setActivityType(ActivityType.EVENT_START_EMPTY_LITERAL);

			SequenceEdge edge = BpmnFactory.eINSTANCE.createSequenceEdge();
			edge.setSource(result);
			edge.setTarget(entry);
			result.getOutgoingEdges().add(edge);
			entry.getIncomingEdges().add(edge);

			graph.getVertices().add(result);
			graph.getSequenceEdges().add(edge);

			EAnnotation annotation = efactory.createEAnnotation();
			annotation.setSource("DUMMY");
			result.getEAnnotations().add(annotation);
		}
		return result;
	}

	/**
	 * Apends an end event to a recently factored-out subgraph. Consider the following scenario:
	 * A structured cycle has been factored out. The exit node is XOR split gateway, but it only has
	 * a single outgoing edge and a single incoming edge (the outgoing edge has been removed, and it
	 * now is outgoing form the subprocess holding the subgraph). There is no way to know if this gateway
	 * is playing the role of a split or of a join. That is why an end event is added, along with an
	 * edge from the XOR split gateway to this end event to disambiguate the role of the split.
	 * 
	 * Note that this end event is annotated as "DUMMY". This allows us to avoid its translation.
	 * 
	 * @param graph	Parent graph, i.e. the subprocess holding the SESE region
	 * @param exit	Current exit node to the SESE region
	 * @return The event event that has been added
	 */
	private Activity appendEndEvent(Graph graph, Activity exit) {
		Activity result = exit;
		
		// No exit event is added if there is already and exit activity
		if (exit.getActivityType() != ActivityType.EVENT_END_EMPTY_LITERAL
				&& exit.getActivityType() != ActivityType.EVENT_END_MESSAGE_LITERAL
				&& exit.getActivityType() != ActivityType.EVENT_END_ERROR_LITERAL
				&& exit.getActivityType() != ActivityType.EVENT_END_TERMINATE_LITERAL
				&& exit.getActivityType() != ActivityType.EVENT_END_COMPENSATION_LITERAL) {
			result = BpmnFactory.eINSTANCE.createActivity();
			result.setActivityType(ActivityType.EVENT_END_EMPTY_LITERAL);

			SequenceEdge edge = BpmnFactory.eINSTANCE.createSequenceEdge();
			edge.setSource(exit);
			edge.setTarget(result);
			exit.getOutgoingEdges().add(edge);
			result.getIncomingEdges().add(edge);

			graph.getVertices().add(result);
			graph.getSequenceEdges().add(edge);

			EAnnotation annotation = efactory.createEAnnotation();
			annotation.setSource("DUMMY");
			result.getEAnnotations().add(annotation);
		}
		return result;
	}

	
	/**
	 * Based on the control flow analysis information, this method identifies patterns
	 * within a SESE region. It factors-out the region and annotates the holding subprocess
	 * with the information to be used during the translation to BPEL.
	 * 
	 * -- The identification of quasi-structured regions and unstructured cyclic regions
	 * MUST BE REFACTORED, URGENTLY !!!
	 * 
	 * @param region	SESE region being analyzed
	 * @param graph		Parent graph (e.g. Pool)
	 * @param entry		Entry node of the SESE region
	 * @param exit		Exit node of the SESE region
	 */
	private void map(SESE.Region region, Graph graph, Activity entry, Activity exit) {
		Collection<Vertex> vertices = new LinkedList<Vertex>(vertexMap.keySet());
		Collection<SequenceEdge> edges = edgeSet;

		// CASE 1: Sequence
		BitSet aux = (BitSet) vertexMap.get(exit).trace.clone();
		aux.xor((BitSet) vertexMap.get(exit).subtrace);
		if (aux.isEmpty()) {
			SubProcess sp = factorOutRegion(region, graph, entry, exit, "Sequence",
					vertices, edges);
			graph.getVertices().add(sp);
			prependStartEvent(sp, entry);
			appendEndEvent(sp, exit);
			return;
		}

		// CASE 2: Cycle
		aux = (BitSet) vertexMap.get(exit).trace.clone();
		aux.xor(vertexMap.get(entry).trace);
		if (aux.isEmpty()) {
			extractInnerSequencesCycle(region, graph, entry, exit, vertices, edges);
			SubProcess sp = factorOutRegion(region, graph, entry, exit,
					"StructuredCycle", vertices, edges);
			prependStartEvent(sp, entry);
			appendEndEvent(sp, exit);
			graph.getVertices().add(sp);
			return;
		}

		if (vertexMap.get(exit) instanceof CFAJoin) {
			CFAJoin cfaJoin = (CFAJoin) vertexMap.get(exit);
			// CASE 3: FLOW/SWITCH/PICK
			if (cfaJoin.visibleSplits.equals(cfaJoin.dominatingSplits)) {
				extractInnerSequencesSBlock(region, graph, entry, exit, vertices,
						edges);
				SubProcess sp = factorOutRegion(region, graph, entry, exit,
						"StructuredBlock", vertices, edges);
				graph.getVertices().add(sp);
				prependStartEvent(sp, entry);
				appendEndEvent(sp, exit);
				return;
			}
		}
		// All the other cases
		// --------------------
		// We must decide if the SESE region follows:
		//  - A quasi-structured pattern
		//	- An unstructured acyclic pattern
		//  - An unstructured cyclic pattern
		//
		// First, we look for cycles inside the region. If there is at least
		// one cycle (there might be more), we conclude that there is an
		// unstructured cyclic pattern.
		
		Map<Integer, List<Vertex>> cycleNodes = new HashMap<Integer, List<Vertex>>();
		List<List<Vertex>> cycles = new LinkedList<List<Vertex>>();
		
		classifyNodes(entry, cycleNodes, cycles, new HashSet<Vertex>());
		if (cycles.size() == 0) {
			
			// If there is no cycles, we perform a backward control flow analysis
			// to identify eventual quasi-structured patterns
			ControlFlowAnalyzer.bcfa(region, exit, vertexMap);
			LinkedList<Vertex> candidates = new LinkedList<Vertex>();
			for (Vertex vertex : vertexMap.keySet()) {
				String name = ((NamedBpmnObject) vertex).getName();
				CFANode node = vertexMap.get(vertex);
				if (node instanceof CFAJoin
						&& ((CFAJoin) node).visibleSplits.cardinality() == 1
						&& ((CFAJoin) node).visibleSplits
								.equals(((CFAJoin) node).dominatingSplits))
					candidates.add(vertex);
				else if (node instanceof CFASplit
						&& ((CFASplit) node).visibleJoins.cardinality() == 1
						&& ((CFASplit) node).visibleJoins
								.equals(((CFASplit) node).postdominatingJoins))
					candidates.add(vertex);
			}
			if (candidates.size() > 0) {
				// CASE 4a: QUASI-STRUCTURED
				
				// -------------------------------------------------------------------------
				// --------------------- This should be refactored !!!!
				// >>>>>>>>>>>>>>>>>>>>>>
				for (Vertex cand : candidates) {
					if (vertexMap.get(cand) instanceof CFAJoin) {
						CFAJoin node = (CFAJoin) vertexMap.get(cand);
						Activity counterpart = null;
						for (Vertex v : vertexMap.keySet())
							if (node.dominatingSplits.nextSetBit(0) == vertexMap
									.get(v).cfanum) {
								counterpart = (Activity) v;
								break;
							}
						Activity split = BpmnFactory.eINSTANCE.createActivity();
						split.setActivityType(counterpart.getActivityType());
						split.setName("_" + counterpart.getName() + "_");
						System.out.printf("%s :: %s", ((NamedBpmnObject) cand)
								.getName(), ((NamedBpmnObject) counterpart)
								.getName());
						LinkedList<SequenceEdge> toRemove = new LinkedList<SequenceEdge>();
						for (SequenceEdge e : counterpart.getOutgoingEdges()) {
							CFANode n = (CFANode) vertexMap.get(e.getTarget());
							if (n.joinRTrace.get(node.cfanum))
								toRemove.add(e);
						}
						SequenceEdge edge = BpmnFactory.eINSTANCE
								.createSequenceEdge();
						edge.setSource(counterpart);
						edge.setTarget(split);
						counterpart.getOutgoingEdges().add(edge);
						split.getIncomingEdges().add(edge);
						vertices.add(split);
						edges.add(edge);
						counterpart.getOutgoingEdges().removeAll(toRemove);
						split.getOutgoingEdges().addAll(toRemove);
						for (SequenceEdge e : toRemove)
							e.setSource(split);
					} else {
						CFASplit node = (CFASplit) vertexMap.get(cand);
						Activity counterpart = null;
						for (Vertex v : vertexMap.keySet())
							if (node.postdominatingJoins.nextSetBit(0) == vertexMap
									.get(v).cfanum) {
								counterpart = (Activity) v;
								break;
							}
						Activity join = BpmnFactory.eINSTANCE.createActivity();
						join.setActivityType(counterpart.getActivityType());
						join.setName("_" + counterpart.getName() + "_");
						System.out.printf("%s :: %s", ((NamedBpmnObject) cand)
								.getName(), ((NamedBpmnObject) counterpart)
								.getName());
						LinkedList<SequenceEdge> toRemove = new LinkedList<SequenceEdge>();
						for (SequenceEdge e : counterpart.getIncomingEdges()) {
							CFANode n = (CFANode) vertexMap.get(e.getSource());
							if (n.splitTrace.get(node.cfanum))
								toRemove.add(e);
						}
						SequenceEdge edge = BpmnFactory.eINSTANCE
								.createSequenceEdge();
						edge.setTarget(counterpart);
						edge.setSource(join);
						counterpart.getIncomingEdges().add(edge);
						join.getOutgoingEdges().add(edge);
						vertices.add(join);
						edges.add(edge);
						counterpart.getIncomingEdges().removeAll(toRemove);
						join.getIncomingEdges().addAll(toRemove);
						for (SequenceEdge e : toRemove)
							e.setTarget(join);
					}
				}
				SubProcess outersp = factorOutRegion(region, graph, entry, exit,
						"StructuredBlock", vertices, edges);

				entry = prependStartEvent(outersp, entry);
				exit = appendEndEvent(outersp, exit);
				SESE innersese = new SESE(outersp);
				process(outersp, innersese.topLevel);

				((SequenceEdge) region.entry)
						.setTarget(outersp.getVertices().get(0));
				((SequenceEdge) region.exit).setSource(outersp.getVertices().get(0));
				graph.getVertices().add(outersp.getVertices().get(0));
				return;
				// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			}
			// CASE 4b: UNSTRUCTURED BLOCK
			SubProcess sp = factorOutRegion(region, graph, entry, exit,
					"UnstructuredBlock", vertices, edges);

			prependStartEvent(sp, entry);
			appendEndEvent(sp, exit);
			graph.getVertices().add(sp);
			return;
		} else {
			// CASE 5: ARBITRARY CYCLE
			
			// -------------------------------------------------------------------------
			// --------------------- This should be refactored !!!!
			// >>>>>>>>>>>>>>>>>>>>>>
			LinkedList<Vertex> innerCycles = new LinkedList<Vertex>();
			for (List<Vertex> cycleElements : cycles) {
				LinkedList<Vertex> borderElements = new LinkedList<Vertex>();
				LinkedList<SequenceEdge> externals = new LinkedList<SequenceEdge>();
				LinkedList<SequenceEdge> internals = new LinkedList<SequenceEdge>();
				LinkedList<SequenceEdge> added = new LinkedList<SequenceEdge>();
				HashSet<Vertex> joins = new HashSet<Vertex>();
				HashSet<Vertex> splits = new HashSet<Vertex>();

				Activity cycleEntry = factory.createActivity();
				cycleEntry
						.setActivityType(ActivityType.GATEWAY_DATA_BASED_EXCLUSIVE_LITERAL);
				cycleEntry.setName("cycleEntry");
				Activity cycleExit = factory.createActivity();
				cycleExit
						.setActivityType(ActivityType.GATEWAY_EVENT_BASED_EXCLUSIVE_LITERAL);
				cycleExit.setName("cycleExit");
				for (Vertex elem : cycleElements) {
					System.out.printf(" %s ", ((Activity) elem).getName());
					internals.addAll(elem.getIncomingEdges());
					internals.addAll(elem.getOutgoingEdges());
					externals.clear();
					if (isBorderElement(elem, externals)) {
						System.out.print("{border}");
						borderElements.add(elem);
						if (elem.getIncomingEdges().size() > 1)
							joins.add(elem);
						else if (elem.getOutgoingEdges().size() > 1)
							splits.add(elem);
						unbindGateway(elem, externals, added, cycleEntry,
								cycleExit, vertices, edges);
					}
					internals.removeAll(externals);
				}

				if (cycleEntry.getIncomingEdges().size() == 1) {
					SequenceEdge toDelete = cycleEntry.getIncomingEdges()
							.get(0);
					vertices.remove(cycleEntry);
					edges.remove(toDelete);
					cycleEntry = (Activity) toDelete.getSource();
					cycleEntry.getOutgoingEdges().remove(toDelete);
					if (joins.contains(entry))
						entry = cycleEntry;
				}

				if (cycleExit.getOutgoingEdges().size() == 1) {
					SequenceEdge toDelete = cycleExit.getOutgoingEdges().get(0);
					vertices.remove(cycleExit);
					edges.remove(toDelete);
					cycleExit = (Activity) toDelete.getTarget();
					cycleExit.getIncomingEdges().remove(toDelete);
					if (splits.contains(exit))
						exit = cycleExit;
				}

				SequenceEdge edge = factory.createSequenceEdge();
				edge.setSource(cycleEntry);
				edge.setTarget(cycleExit);
				cycleEntry.getOutgoingEdges().add(edge);
				cycleExit.getIncomingEdges().add(edge);

				// FACTORING OUT the cycle
				SubProcess sp = factory.createSubProcess();
				sp.setActivityType(ActivityType.SUB_PROCESS_LITERAL);
				EAnnotation annotation = efactory.createEAnnotation();

				sp.setName("UnstructuredCycle");
				annotation.setSource("UnstructuredCycle");
				sp.getEAnnotations().add(annotation);

				Activity gw = BpmnFactory.eINSTANCE.createActivity();
				gw
						.setActivityType(ActivityType.GATEWAY_EVENT_BASED_EXCLUSIVE_LITERAL);
				sp.getVertices().add(gw);
				LinkedList<SequenceEdge> cedges = new LinkedList<SequenceEdge>();
				LinkedList<Vertex> cvertices = new LinkedList<Vertex>();

				for (Vertex v : cycleElements) {
					Activity cact = (Activity) v;
					Activity event = BpmnFactory.eINSTANCE.createActivity();
					event
							.setActivityType(ActivityType.EVENT_INTERMEDIATE_MESSAGE_LITERAL);
					event.setName("Receive " + cact.getName());
					cvertices.add(event);
					SequenceEdge se = BpmnFactory.eINSTANCE
							.createSequenceEdge();
					se.setSource(gw);
					se.setTarget(event);
					gw.getOutgoingEdges().add(se);
					event.getIncomingEdges().add(se);
					cedges.add(se);
					if (cact.getActivityType() == ActivityType.TASK_LITERAL
							|| cact.getActivityType() == ActivityType.SUB_PROCESS_LITERAL
							|| cact.getActivityType() == ActivityType.EVENT_INTERMEDIATE_MESSAGE_LITERAL) {
						SubProcess asub = BpmnFactory.eINSTANCE
								.createSubProcess();
						asub.setActivityType(ActivityType.SUB_PROCESS_LITERAL);
						asub.setName("Event4" + cact.getName());
						cvertices.add(asub);
						Activity following = (Activity) v.getOutgoingEdges()
								.get(0).getTarget();
						v.getIncomingEdges().clear();
						v.getOutgoingEdges().clear();
						SequenceEdge ae = BpmnFactory.eINSTANCE
								.createSequenceEdge();
						ae.setSource(event);
						ae.setTarget(asub);
						event.getOutgoingEdges().add(ae);
						asub.getIncomingEdges().add(ae);
						cedges.add(ae);
						annotation = efactory.createEAnnotation();
						annotation.setSource("Sequence");
						asub.getEAnnotations().add(annotation);
						asub.getVertices().add(v);

						if (following != null) {
							Activity invokeFollowing = BpmnFactory.eINSTANCE
									.createActivity();
							invokeFollowing.setName("invoke "
									+ following.getName());
							asub.getVertices().add(invokeFollowing);
							ae = BpmnFactory.eINSTANCE.createSequenceEdge();
							ae.setSource(event);
							ae.setTarget(invokeFollowing);
							v.getOutgoingEdges().add(ae);
							invokeFollowing.getIncomingEdges().add(ae);
							asub.getSequenceEdges().add(ae);
						}
					} else if (joins.contains(v)) {
						Activity following = (Activity) v.getOutgoingEdges()
								.get(0).getTarget();
						if (following != null) {
							Activity invokeFollowing = BpmnFactory.eINSTANCE
									.createActivity();
							invokeFollowing.setName("invoke "
									+ following.getName());
							cvertices.add(invokeFollowing);
							SequenceEdge ae = BpmnFactory.eINSTANCE
									.createSequenceEdge();
							ae.setSource(event);
							ae.setTarget(invokeFollowing);
							event.getOutgoingEdges().add(ae);
							invokeFollowing.getIncomingEdges().add(ae);
							cedges.add(ae);
						}
					} else {
						SubProcess asub = BpmnFactory.eINSTANCE
								.createSubProcess();
						asub.setActivityType(ActivityType.SUB_PROCESS_LITERAL);
						asub.setName("Event4" + cact.getName());
						asub.getVertices().add(v);
						cvertices.add(asub);

						LinkedList<SequenceEdge> followings = new LinkedList<SequenceEdge>();
						followings.addAll(v.getOutgoingEdges());
						v.getIncomingEdges().clear();
						v.getOutgoingEdges().clear();
						SequenceEdge ae = BpmnFactory.eINSTANCE
								.createSequenceEdge();
						ae.setSource(event);
						ae.setTarget(asub);
						event.getOutgoingEdges().add(ae);
						asub.getIncomingEdges().add(ae);
						cedges.add(ae);
						annotation = efactory.createEAnnotation();
						annotation.setSource("StructuredBlock");
						asub.getEAnnotations().add(annotation);
						for (SequenceEdge e : followings) {
							Activity following = (Activity) e.getTarget();
							Activity invokeFollowing = BpmnFactory.eINSTANCE
									.createActivity();
							invokeFollowing.setName("invoke "
									+ following.getName());
							asub.getVertices().add(invokeFollowing);
							ae = BpmnFactory.eINSTANCE.createSequenceEdge();
							ae.setSource(v);
							ae.setTarget(invokeFollowing);
							v.getOutgoingEdges().add(ae);
							invokeFollowing.getIncomingEdges().add(ae);
							asub.getSequenceEdges().add(ae);
						}
						Activity invokeFollowing = BpmnFactory.eINSTANCE
								.createActivity();
						invokeFollowing
								.setName("invoke exit_" + cact.getName());
						asub.getVertices().add(invokeFollowing);
						ae = BpmnFactory.eINSTANCE.createSequenceEdge();
						ae.setSource(v);
						ae.setTarget(invokeFollowing);
						v.getOutgoingEdges().add(ae);
						invokeFollowing.getIncomingEdges().add(ae);
						asub.getSequenceEdges().add(ae);
						prependStartEvent(asub, (Activity) v);
					}
				}

				sp.getVertices().addAll(cvertices);
				sp.getSequenceEdges().addAll(cedges);

				graph.getVertices().removeAll(cycleElements);
				graph.getSequenceEdges().removeAll(internals);

				// TODO: Explain why to use another list
				innerCycles.add(sp);

				// Adjust the enclosing region composition
				vertices.removeAll(cycleElements);
				vertices.add(cycleEntry);
				vertices.add(cycleExit);
				edges.removeAll(internals);
				edges.add(edge);
			}

			// Isolate the region
			entry.getIncomingEdges().remove(region.entry);
			exit.getOutgoingEdges().remove(region.exit);
			
			// Outer SP
			SubProcess outersp = factory.createSubProcess();
			outersp.setActivityType(ActivityType.SUB_PROCESS_LITERAL);
			EAnnotation annotation = efactory.createEAnnotation();

			outersp.setName("UnstructuredCyclicRegion");
			annotation.setSource("UnstructuredCyclicBlock");
			outersp.getEAnnotations().add(annotation);
			outersp.getVertices().addAll(vertices);
			outersp.getSequenceEdges().addAll(edges);

			prependStartEvent(outersp, entry);
			appendEndEvent(outersp, exit);

			SESE innersese = new SESE(outersp);
			process(outersp, innersese.topLevel);

			outersp.getVertices().addAll(innerCycles);
			((SequenceEdge) region.entry).setTarget(outersp);
			((SequenceEdge) region.exit).setSource(outersp);
			// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		}

	}

	
	/**
	 * When an unstructured cyclic region is found, this method is used for unbinding
	 * every border gateway (i.e. entry/exit). The incident edge is replaced with a
	 * local interaction using an event-oriented programming style (invoke -> onEvent).
	 * See paper for details.
	 * 
	 * @param elem
	 * @param externals
	 * @param added
	 * @param cycleEntry
	 * @param cycleExit
	 * @param vertices
	 * @param edges
	 */
	private void unbindGateway(Vertex elem, LinkedList<SequenceEdge> externals,
			LinkedList<SequenceEdge> added, Activity cycleEntry,
			Activity cycleExit, Collection<Vertex> vertices,
			Collection<SequenceEdge> edges) {
		// SPLIT case
		if (elem.getOutgoingEdges().size() > 1) {
			for (SequenceEdge e : externals) {
				Activity receive = factory.createActivity();
				receive
						.setActivityType(ActivityType.EVENT_INTERMEDIATE_MESSAGE_LITERAL);
				receive.setName(String.format("receive exit_%s",
						((Activity) elem).getName()));
				vertices.add(receive);
				// Now the SequenceEdges
				elem.getOutgoingEdges().remove(e);
				receive.getOutgoingEdges().add(e);
				e.setSource(receive);
				SequenceEdge newEdge = factory.createSequenceEdge();
				cycleExit.getOutgoingEdges().add(newEdge);
				newEdge.setSource(cycleExit);
				receive.getIncomingEdges().add(newEdge);
				newEdge.setTarget(receive);
				edges.add(newEdge);

				Activity invoke = factory.createActivity();
				invoke.setActivityType(ActivityType.TASK_LITERAL);
				invoke.setName(String.format("invoke %s", ((Activity) elem)
						.getName()));
				EAnnotation annotation = efactory.createEAnnotation();
				annotation.setSource("invoke");
				invoke.getEAnnotations().add(annotation);

				SequenceEdge toAdd = factory.createSequenceEdge();
				invoke.getIncomingEdges().add(toAdd);
				toAdd.setTarget(invoke);
				added.add(toAdd);

			}

		} else { // JOIN case
			for (SequenceEdge e : externals) {
				Activity invoke = factory.createActivity();
				invoke.setActivityType(ActivityType.TASK_LITERAL);
				invoke.setName(String.format("invoke %s", ((Activity) elem)
						.getName()));

				EAnnotation annotation = efactory.createEAnnotation();
				annotation.setSource("invoke");
				invoke.getEAnnotations().add(annotation);

				vertices.add(invoke);
				// Redirecting the SequenceEdge
				elem.getIncomingEdges().remove(e);
				invoke.getIncomingEdges().add(e);
				e.setTarget(invoke);
				SequenceEdge newEdge = factory.createSequenceEdge();
				cycleEntry.getIncomingEdges().add(newEdge);
				newEdge.setTarget(cycleEntry);
				invoke.getOutgoingEdges().add(newEdge);
				newEdge.setSource(invoke);
				edges.add(newEdge);
			}
		}
	}

	/**
	 * This function is used to test if a vertex is either an entry/exit gateway in an (unstructured) cycle or not
	 * 
	 * @param elem
	 * @param externals
	 * @return
	 */
	private boolean isBorderElement(Vertex elem, List<SequenceEdge> externals) {
		boolean result = false;
		BitSet elemTrace = vertexMap.get(elem).trace;
		if (elem.getOutgoingEdges().size() > 1) {
			for (SequenceEdge edge : elem.getOutgoingEdges())
				if (!vertexMap.containsKey(edge.getTarget())
						|| !vertexMap.get(edge.getTarget()).trace
								.equals(elemTrace)) {
					externals.add(edge);
					result = true;
				}
		} else if (elem.getIncomingEdges().size() > 1) {
			for (SequenceEdge edgep : elem.getIncomingEdges())
				if (!vertexMap.containsKey(edgep.getSource())
						|| !vertexMap.get(edgep.getSource()).trace
								.equals(elemTrace)) {
					externals.add(edgep);
					result = true;
				}
		}
		return result;
	}

	
	// TODO: Redesign this method to a non-recursive one
	/**
	 * Traverses the graph in a SESE region, identifies cycles inside the
	 * region and, if found, collects the set of vertices of every cycle
	 *
	 * @param n
	 * @param cycleNodes
	 * @param cycles
	 * @param mark
	 */
	private void classifyNodes(Vertex n, Map<Integer, List<Vertex>> cycleNodes,
			List<List<Vertex>> cycles, Set<Vertex> mark) {

		List<Vertex> list = cycleNodes.get(vertexMap.get(n).trace.hashCode());
		if (list == null) {
			list = new LinkedList<Vertex>();
			cycleNodes.put(vertexMap.get(n).trace.hashCode(), list);
		}
		list.add(n);
		if (list.size() == 2)
			cycles.add(list);
		mark.add(n);
		for (SequenceEdge e : n.getOutgoingEdges()) {
			Vertex succ = e.getTarget();
			if (!mark.contains(succ) && vertexMap.containsKey(succ))
				classifyNodes(succ, cycleNodes, cycles, mark);
		}
	}

	private void extractInnerSequence(Region r, Graph pool, SequenceEdge e,
			Collection<Vertex> vertices, Collection<SequenceEdge> edges) {
		Vertex pred = e.getSource();
		CFANode cfaPred = vertexMap.get(pred);
		if (cfaPred != null && cfaPred.subtrace.cardinality() > 1) {
			BitSet aux = (BitSet) cfaPred.subtrace.clone();
			System.out.printf("\t\t--> Factoring out a sequence: %s\n",
					cfaPred.subtrace);
			lvertices.clear();
			ledges.clear();
			Vertex current = pred;
			aux.clear(vertexMap.get(current).cfanum);
			lvertices.add(pred);
			while (!aux.isEmpty()) {
				SequenceEdge innerEdge = current.getIncomingEdges().get(0);
				current = innerEdge.getSource();
				lvertices.add(current);
				ledges.add(innerEdge);
				aux.clear(vertexMap.get(current).cfanum);
			}

			// CREATE a new Region
			SESE.Region lr = new SESE.Region(current.getIncomingEdges().get(0),
					e); // TODO -- what about PICK ?
			lr.nodes().add(current);
			lr.nodes().add(pred);
			SESE.Region.link(r, lr);

			SubProcess sp = factorOutRegion(lr, pool, (Activity) current,
					(Activity) pred, "Sequence", lvertices, ledges);
			vertices.add(sp);
			prependStartEvent(sp, (Activity) current);
			appendEndEvent(sp, (Activity) pred); 	// Update entry and exit
													// references within the
													// SESE Region ?
			vertices.removeAll(lvertices);
			edges.removeAll(ledges);
		}
	}

	private void extractInnerSequencesCycle(Region r, Graph pool,
			Activity join, Activity split, Collection<Vertex> vertices,
			Collection<SequenceEdge> edges) {
		for (SequenceEdge e : join.getIncomingEdges())
			extractInnerSequence(r, pool, e, vertices, edges);
		for (SequenceEdge e : split.getIncomingEdges())
			extractInnerSequence(r, pool, e, vertices, edges);
	}

	private void extractInnerSequencesSBlock(Region r, Graph pool,
			Activity split, Activity join, Collection<Vertex> vertices,
			Collection<SequenceEdge> edges) {
		for (SequenceEdge e : join.getIncomingEdges())
			extractInnerSequence(r, pool, e, vertices, edges);
	}
}
