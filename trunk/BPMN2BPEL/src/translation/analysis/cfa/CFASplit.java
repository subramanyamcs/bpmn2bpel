// Copyright (C) 2008 Luciano Garcia-Banuelos <lgbanuelos@gmail.com>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package translation.analysis.cfa;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.stp.bpmn.SequenceEdge;
import org.eclipse.stp.bpmn.Vertex;


public class CFASplit extends CFANode {
	public BitSet postdominatingJoins = new BitSet();
	public BitSet visibleJoins = new BitSet();
	private boolean traversed;
	public CFASplit(Vertex w) {
		super(w);
		traversed = false;
		postdominatingJoins.set(0, postdominatingJoins.size());
	}

	public boolean performCFA(Map<Vertex, CFANode> map, SequenceEdge entry, SequenceEdge exit) {
        BitSet tracep = (BitSet)trace.clone();
        for (SequenceEdge e : wrapped.getIncomingEdges()) {
            CFANode pred = map.get(e.getSource());
            if (pred != null && e != entry && e != exit) {
            	trace.or(pred.trace);
	            splitTrace.or(pred.splitTrace);
            }
        }
        trace.set(cfanum);
        splitTrace.set(cfanum);
        tracep.xor(trace);
        return !tracep.isEmpty();
    }
	
	public boolean performBCFA(HashMap<Vertex, CFANode> map,
			SequenceEdge entry, SequenceEdge exit) {
		traversed = true;
		BitSet shadow = (BitSet)postdominatingJoins.clone();
        for (SequenceEdge e : wrapped.getOutgoingEdges()) {
            CFANode succ = map.get(e.getTarget());
            if (succ != null && e != entry && e != exit) {
            	postdominatingJoins.and(succ.joinRTrace);
            	visibleJoins.or(succ.joinRTrace);
            }
        }
        return !postdominatingJoins.equals(shadow);
	}
}
