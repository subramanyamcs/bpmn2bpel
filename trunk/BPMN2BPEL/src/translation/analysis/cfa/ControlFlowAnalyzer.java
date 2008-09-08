// Copyright (C) 2008 Luciano Garcia-Banuelos <lgbanuelos@gmail.com>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package translation.analysis.cfa;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.eclipse.stp.bpmn.Activity;
import org.eclipse.stp.bpmn.SequenceEdge;
import org.eclipse.stp.bpmn.Vertex;

import translation.analysis.sese.SESE.Region;

/**
 * This class provides methods to perform a control flow analysis
 * based on dataflow equations as described in:
 * 
 * Garcia-Banuelos, L. Pattern Identification and Classification in the
 * translation from BPMN to BPEL. In Proc. OTM 2008, Springer-Verlag, 2008
 * 
 * @author Luciano Garcia-Banuelos (lgbanuelos@gmail.com)
 */
public class ControlFlowAnalyzer {
	
	
	/**
	 * Performs the forward control flow analysis
	 * 
	 * @param region	SESE region being analyzed
	 * @param entry		Entry node to the SESE region
	 * @param vertexMap	Map correlating every BPMN Vertex with its control flow information
	 * @param edgeSet	Set of edges inside the region (this set is filled inside this method)
	 */
	public static void cfa(Region region, Vertex entry,
					 Map<Vertex, CFANode> vertexMap,
					 Set<SequenceEdge> edgeSet) {
        vertexMap.clear();
        edgeSet.clear();
        LinkedList<Vertex> workingList = new LinkedList<Vertex>();
        CFANode info;
        workingList.add(entry);
        while (workingList.size() > 0) {
            Vertex n = workingList.removeLast();
        	info = vertexMap.get(n);
            if (info == null) {
            	if (n.getOutgoingEdges().size() > 1)
            		info = new CFASplit(n);
            	else if (n.getIncomingEdges().size() > 1)
            		info = new CFAJoin(n);
            	else
            		info = new CFANode(n);
            	info.cfanum = vertexMap.size();
                vertexMap.put(n, info);
            }
            boolean changed = info.performCFA(vertexMap, (SequenceEdge)region.entry, (SequenceEdge)region.exit);
            if (changed) {
            	for (SequenceEdge edge : n.getOutgoingEdges())
            		if (edge != region.entry && edge != region.exit) {
            			workingList.add(edge.getTarget());
            			edgeSet.add(edge);
            		}
            }
        }
    }

	
	/**
	 * Performs the backward control flow analysis
	 * 
	 * @param region	SESE region being analyzed
	 * @param exit		Exit vertex in the region
	 * @param vertexMap	Map correlating every BPMN node to its control flow information
	 */
	public static void bcfa(Region region, Activity exit,
			HashMap<Vertex, CFANode> vertexMap) {
        LinkedList<Vertex> workingList = new LinkedList<Vertex>();
        CFANode info;
        workingList.add(exit);
        while (workingList.size() > 0) {
            Vertex n = workingList.removeFirst();
        	info = vertexMap.get(n);
            boolean changed = info.performBCFA(vertexMap, (SequenceEdge)region.entry, (SequenceEdge)region.exit);
            if (changed) {
            	for (SequenceEdge edge : n.getIncomingEdges())
            		if (edge != region.entry && edge != region.exit)
            			workingList.add(edge.getSource());
            }
        }		
	}
}
