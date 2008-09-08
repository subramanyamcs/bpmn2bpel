// Copyright (C) 2008 Luciano Garcia-Banuelos <lgbanuelos@gmail.com>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package translation.analysis.cfa;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.stp.bpmn.Activity;
import org.eclipse.stp.bpmn.ActivityType;
import org.eclipse.stp.bpmn.SequenceEdge;
import org.eclipse.stp.bpmn.Vertex;


public class CFANode {
	public int cfanum;
	public BitSet trace;
	public BitSet subtrace;
	public BitSet splitTrace;
	public BitSet joinRTrace;
	Vertex wrapped;
	
	public CFANode(Vertex w) {
		wrapped = w;
		trace = new BitSet();
		subtrace = new BitSet();
		splitTrace = new BitSet();
		joinRTrace = new BitSet();
	}
	
    public boolean performCFA(Map<Vertex, CFANode> map, SequenceEdge entry, SequenceEdge exit) {
        BitSet tracep = (BitSet)trace.clone();
        for (SequenceEdge e : wrapped.getIncomingEdges()) {
            CFANode pred = map.get(e.getSource());
            if (pred != null && e != entry && e != exit) {
            	trace.or(pred.trace);
            	if (!isEventIntermediate(wrapped))
            		subtrace.or(pred.subtrace);
	            splitTrace.or(pred.splitTrace);
            }
        }
        trace.set(cfanum);
    	if (!isEventIntermediate(wrapped))
        	subtrace.set(cfanum);
        tracep.xor(trace);
        return !tracep.isEmpty();
    }

	private boolean isEventIntermediate(Vertex wrapped2) {
		boolean result = false;
		
		Activity act = (Activity) wrapped2;
		switch (act.getActivityType().getValue()) {
		case ActivityType.EVENT_INTERMEDIATE_MESSAGE:
		case ActivityType.EVENT_INTERMEDIATE_TIMER:
			result = true;
		}
		return result;
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
        return !joinRTrace.equals(shadow);
	}
}
