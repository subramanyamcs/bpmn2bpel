// Copyright (C) 2008 Luciano Garcia-Banuelos <lgbanuelos@gmail.com>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package translation.analysis.cfa;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.stp.bpmn.SequenceEdge;
import org.eclipse.stp.bpmn.Vertex;


public class CFAJoin extends CFANode {
	public BitSet dominatingSplits = new BitSet();
	public BitSet visibleSplits = new BitSet();
	public CFAJoin(Vertex w) {
		super(w);
		dominatingSplits.set(0, dominatingSplits.size());
	}

	public boolean performCFA(Map<Vertex, CFANode> map, SequenceEdge entry, SequenceEdge exit) {
        BitSet tracep = (BitSet)trace.clone();
        for (SequenceEdge e : wrapped.getIncomingEdges()) {
            CFANode pred = map.get(e.getSource());
            if (pred != null && e != entry && e != exit) {
            	trace.or(pred.trace);
            	dominatingSplits.and(pred.splitTrace);
            	visibleSplits.or(pred.splitTrace);
            }
        }
        trace.set(cfanum);
        tracep.xor(trace);
        return !tracep.isEmpty();
    }
	public boolean performBCFA(HashMap<Vertex, CFANode> map,
			SequenceEdge entry, SequenceEdge exit) {
		BitSet shadow = (BitSet)joinRTrace.clone();
        for (SequenceEdge e : wrapped.getOutgoingEdges()) {
            CFANode succ = map.get(e.getTarget());
            if (succ != null && e != entry && e != exit) {
            	joinRTrace.or(succ.joinRTrace);
            }
        }
        joinRTrace.set(cfanum);
        return !joinRTrace.equals(shadow);
	}
}
