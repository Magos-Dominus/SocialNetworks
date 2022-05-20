package graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	private int vertex;
	private List<Edge> edges;
	private boolean covered;
	
	public Node(int vert) {
		this.vertex = vert;
		this.edges = new ArrayList<Edge>();
		this.covered = false;
	}

	public int getVertex() {
		return vertex;
	}

	public void setVertex(int vertex) {
		this.vertex = vertex;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	
	public void addEdge(Edge e) {
		edges.add(e);
	}
	
	public void setCovered(boolean cov) {
		this.covered = cov;
	}
	
	public int getNumNeighbors() {
		return edges.size();
	}
	

}
