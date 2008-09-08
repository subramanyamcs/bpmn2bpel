package translation.analysis.sese;

import java.util.LinkedList;

import org.eclipse.stp.bpmn.Graph;
import org.eclipse.stp.bpmn.Vertex;

public class Util {
	public static Vertex getRootElement(Graph graph) {
		Vertex root = null;
		for (Vertex vertex : graph.getVertices()) {
			if (vertex.getIncomingEdges().size() == 0)
				root = vertex;
		}
		return root;
	}
	public static Vertex[] getLeafElements(Graph graph) {
		LinkedList<Vertex> list = new LinkedList<Vertex>();
		for (Vertex vertex : graph.getVertices()) {
			if (vertex.getOutgoingEdges().size() == 0)
				list.add(vertex);
		}
		return list.toArray(new Vertex[0]);
	}
}
