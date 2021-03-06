//SESE.java, created Mon Mar  1 23:52:29 1999 by cananian
// Copyright (C) 1999 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package translation.analysis.sese;

import org.eclipse.stp.bpmn.Activity;
import org.eclipse.stp.bpmn.Graph;
import org.eclipse.stp.bpmn.SequenceEdge;
import org.eclipse.stp.bpmn.Vertex;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * <code>SESE</code> computes nested single-entry single-exit regions
 * from a cycle-equivalency set.<p>
 * See Johnson, Pearson, and Pingali, <A
 * HREF="http://cs-tr.cs.cornell.edu:80/Dienst/UI/1.0/Display/ncstrl.cornell/TR93-1365"
 * >"Finding regions fast: Single entry single exit and control regions in
 * linear time"</A> (Technical Report TR 93-1365, Cornell University,
 * July 1993).  Their PLDI'94 paper "The program structure tree: Computing
 * control regions in linear time" might also provide a useful reference.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id: SESE.java,v 1.7 2004/02/08 03:19:12 cananian Exp $
 */
public class SESE  {
	/** Root of <code>Region</code> tree. */
	public final Region topLevel=new Region();
	/** Mapping from Nodes/Edges to smallest enclosing canonical SESE
	 *  <code>Region</code>. */
	public final Map smallestSESE;

	/** Creates a <code>SESE</code> using a <code>CycleEq</code>. */
	public SESE(Graph hc) {
		// compute cycle equivalence.
		CycleEq ceq = new CycleEq(hc);
		// use cycle equivalence classes to determine canonical sese regions.
		Map entryRegion= new HashMap();
		Map exitRegion = new HashMap();
		for (Object lO : ceq.cdClasses()) {
			List l = (List) lO;
			Object prev=null;
			for (Iterator lit=l.listIterator(); lit.hasNext(); ) {
				Object o = lit.next();
				if (prev!=null) {
					Region r = new Region(prev, o);
					entryRegion.put(prev, r);
					exitRegion.put(o, r);
				}
				prev = o;
			}
		}
		// now do DFS traversal of CFG to determine nesting of regions.
		Map workSmallSESE = new HashMap();
		// state for DFS
		Set visited = new HashSet();  visited.add(Util.getRootElement(hc));
		Stack nodeS = new Stack();    nodeS.push(Util.getRootElement(hc));
		Stack regionS=new Stack();    regionS.push(topLevel);

		while (!nodeS.isEmpty()) {
			assert nodeS.size()==regionS.size();
			Object o = nodeS.pop(); // either an Vertex or an SequenceEdge
			Region cR= (Region) regionS.pop(); // currentRegion.
			// deal with region entry/exit.
			if (true==(o instanceof Vertex)) {
				// cR is smallest enclosing canonical SESE of o.
				workSmallSESE.put(o, cR);
				cR.nodes.add(o);
			} else { // update current region.
				// if this is found in the cycle-equivalency stuff, push/pop.
				Region r1 = (Region) entryRegion.get(o);
				Region r2 = (Region) exitRegion.get(o);
				// pop up one level if we are leaving the region.
				if (cR.equals(r1)) { cR=cR.parent; r1=null; }
				if (cR.equals(r2)) { cR=cR.parent; r2=null; }
				// push a level if we are entering the region.
				if (r1!=null) { Region.link(cR, r1); cR=r1; }
				if (r2!=null) { Region.link(cR, r2); cR=r2; }
			}
			// continue DFS traversal.
			Object[] succ=null;
			if (o instanceof Vertex) // Node.  Push edges.
				succ = ((Vertex)o).getOutgoingEdges().toArray();
			if (o instanceof SequenceEdge) // Edge.  Push node.
				succ = new Vertex[] { ((SequenceEdge)o).getTarget() };
			for (int i=succ.length-1; i>=0; i--)
				if (!visited.contains(succ[i])) {
					nodeS.push(succ[i]); regionS.push(cR);
					visited.add(succ[i]);
				}
		} // END while.
		assert nodeS.isEmpty() && regionS.isEmpty();
		// make smallestSESE map unmodifiable.
		smallestSESE = Collections.unmodifiableMap(workSmallSESE);
		// make region lists unmodifiable
		for (Iterator it = topDown(); it.hasNext(); ) {
			Region r = (Region) it.next();
			r.nodes = Collections.unmodifiableCollection(r.nodes);
		}
	}

	/** Iterate through SESE regions, top-down.  All top-level regions
	 *  are visited first, then all children of top-level regions, then
	 *  all grandchildren, then all great-grandchildren, etc. */
	public Iterator topDown() { return iterator(true); }
	/** Iterate through SESE regions, depth-first. */
	public Iterator depthFirst() { return iterator(false); }

	private Iterator iterator(final boolean topdown) {
		return new UnmodifiableIterator() {
			LinkedList ll = new LinkedList();
			{ ll.add(topLevel); }
			public boolean hasNext() { return !ll.isEmpty(); }
			public Object next() {
				Region r = (Region) (topdown?ll.removeFirst():ll.removeLast());
				ll.addAll(r.children());
				return r;
			}
		};
	}

	public void print(java.io.PrintWriter pw) {
		for (Iterator it=depthFirst(); it.hasNext(); ) {
			Region r = (Region) it.next();
			for (int i=0; i<r.level; i++)
				pw.print(' ');
			pw.print(r.toString()); // print region.
			pw.print(" : ");
			pw.println(r.nodes.toString()); // print members
		}
	}
	
	public void print() {
		for (Iterator it=depthFirst(); it.hasNext(); ) {
			Region r = (Region) it.next();
			for (int i=0; i<r.level; i++)
				System.out.print(' ');
			System.out.print(r.toString()); // print region.
			System.out.print(" : [");
			for (Object o : r.nodes) {
				Activity a = (Activity) o;
				System.out.print(a.getName() + " "); // print members
			}
			System.out.println("]");
		}
	}
	
	/** <code>SESE.Region</code> represents a single-entry single-exit 
	 *  (SESE) Region, as computed by the <code>SESE</code> object. */
	public static class Region {
		/** entry edge of the region. */
		public final Object entry;
		/** exit edge of the region. */
		public final Object exit;
		// tree info.
		int level=0;
		Region parent=null;
		RegionList children=null;
		// list of nodes in this region
		Collection nodes = new ArrayList();
			Region() { // create top-level region.
			this.entry = this.exit = null;
			assert isTopLevel();
		}
		public Region(Object entry, Object exit) {
			this.entry = entry; this.exit = exit;
			assert !isTopLevel();
		}
		
		/** Parent region of this one (<code>null</code> for top-level
		 *  region). */
		public Region parent() { return this.parent; }
		/** Child regions of this one. */
		public Set children() { return RegionList.asSet(this.children); }
		/** Nodes in this region (and not contained in any child regions) */
		public Collection nodes() { return this.nodes; }

		public boolean isTopLevel() {
			return (this.entry==null) && (this.exit==null);
		}
		/** Compare two regions for equality. */
		public boolean equals(Object o) {
			Region r;
			if (this==o) return true;
			if (o==null) return false;
			try { r=(Region)o; }
			catch (ClassCastException e) { return false; }
			if (isTopLevel()) return r.isTopLevel();
			else return
			this.entry.equals(r.entry) &&
			this.exit.equals(r.exit);
		}
		public int hashCode() {
			if (isTopLevel()) return 0;
			else return (entry.hashCode()<<7) ^ exit.hashCode();
		}
		public String toString() {
			if (isTopLevel()) return "[-toplevel-]";
			else {
				Activity _entry = (Activity)((SequenceEdge)entry).getTarget();
				Activity _exit = (Activity)((SequenceEdge)exit).getSource();
				return "["+_entry.getName()+"->"+_exit.getName()+"]";
			}
		}
        public static void link(Region parent, Region child) {
        	child.level = 1 + parent.level;
        	child.parent = parent;
        	parent.children = new RegionList(child, parent.children);
        }
	}
	static class RegionList {
		final Region region;
		final RegionList next;
		final int size;
		RegionList(Region region, RegionList next) {
			this.region = region; this.next = next; 
			this.size = 1+((next==null)?0:next.size);
		}
		static Iterator elements(final RegionList rl) {
			return new UnmodifiableIterator() {
				RegionList rlp = rl;
				public boolean hasNext() { return rlp!=null; }
				public Object next() {
					Region r = rlp.region; rlp=rlp.next; return r;
				}
			};
		}
		static Set asSet(final RegionList rl) {
			return new AbstractSet() {
				public boolean isEmpty() { return rl==null; }
				public int size() { return (rl==null)?0:rl.size; }
				public Iterator iterator() { return elements(rl); }
			};
		}
	}
}
