/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import util.GraphLoader;

/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {
	
	private HashMap<Integer, HashSet<Integer>> verticies;
	private HashSet<Edge> edges;
	private int numVertex;
	private int numEdges;

	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	
	public CapGraph() {
		verticies = new HashMap<Integer, HashSet<Integer>>();
		edges = new HashSet<Edge>();
		numVertex = 0;
		numEdges = 0;
	}
	
	
	@Override
	public void addVertex(int num) {
		
		if(!verticies.containsKey(num)) {
			verticies.put(num, new HashSet<Integer>());
			numVertex++;
		}
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		
		if(!verticies.containsKey(from) || !verticies.containsKey(to)) {
			return;
		}
		else {
			verticies.get(from).add(to);
			Edge e = new Edge(from,to);
			edges.add(e);
			numEdges++;
		}
		

		// TODO Auto-generated method stub

	}
	
	private Set<Integer> getVerticies(){
		return verticies.keySet();
	}
	
	private HashSet<Edge> getEdges(){
		return edges;
	}
	
	private HashSet<Integer> getNeighbors(int i){
		return verticies.get(i);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		CapGraph egoNet = new CapGraph();
		HashSet<Integer> neighbors = verticies.get(center);
		egoNet.addVertex(center);
		for(Integer i : neighbors) {
			egoNet.addVertex(i);
		}
		for(Edge e : edges) {
			if(neighbors.contains(e.getFrom()) && neighbors.contains(e.getTo())) {
				egoNet.addEdge(e.getFrom(), e.getTo());
			}
		}
		
		// TODO Auto-generated method stub
		return egoNet;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	
	private CapGraph transposeGraph(CapGraph g) {
		CapGraph reverse = new CapGraph();
		for(Integer i : g.getVerticies()) {
			reverse.addVertex(i);
		}
		for(Edge e : g.getEdges()) {
			reverse.addEdge(e.getTo(), e.getFrom());
		}
		return reverse;
	}
	
	public Stack<Integer> dfs(CapGraph g, Stack<Integer> vertexStack){
		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();

		while(!vertexStack.isEmpty()) {
			Integer v = vertexStack.pop();
			if(!visited.contains(v)) {
				dfsVisit(g, v, visited, finished);
			}
		}
		return finished;
	}
	
	public void dfsVisit (CapGraph g, int v, HashSet<Integer> visited, Stack<Integer> finished) {
		visited.add(v);
		for(Integer i : getNeighbors(v)) {
			if(!visited.contains(i)) {
				dfsVisit(g, i, visited, finished);
			}
		}
		finished.push(v);
		return;
	}
	
	public List<Graph> dfsSCC(CapGraph g, Stack<Integer> vertexStack){
		List<Graph> sccList = new ArrayList<Graph>();
		HashSet<Integer> visited = new HashSet<Integer>();
		while(!vertexStack.isEmpty()) {
			Integer v = vertexStack.pop();
			if(!visited.contains(v)) {
				CapGraph scc = new CapGraph();
				scc.addVertex(v);
				dfsVisitSCC(g, v, visited, scc);
				sccList.add(scc);
			}
		}
		return sccList;
	}
	
	public void dfsVisitSCC(CapGraph g, Integer v, HashSet<Integer> visited, CapGraph scc) {
		visited.add(v);
		for(Integer i : g.getNeighbors(v)) {
			if(!visited.contains(i)) {
				dfsVisitSCC(g,i,visited,scc);
			}
		}
		scc.addVertex(v);
		return;
	}
	
	
	@Override
	public List<Graph> getSCCs() {
		Stack<Integer> vertexStack = new Stack<Integer>();
		for(Integer i : verticies.keySet()) {
			vertexStack.add(i);
		}
		
		Stack<Integer> reverse = new Stack<Integer>();
		reverse = dfs(this, vertexStack);
		
		CapGraph transpose = transposeGraph(this);
			
		
		// TODO Auto-generated method stub
		return dfsSCC(transpose, reverse);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		// TODO Auto-generated method stub
		return verticies;
	}
	
	//First the algorithm looks at all neighbors 
	//Then all neighbors of neighbors
	//All of these are stored into a HashSet
	//Then the List of all Edges is referenced 
	//If an edge already exists between the input user and a user in the HashSet of potential friends
	//this friend is removed from the set
	//the remainder is returned as a set of potential friends that could be added by the input user
	public HashSet<Integer> recommendFriends(Integer vertex) {
		HashSet<Integer> potentialFriends = new HashSet<Integer>();
		for(Integer i : getNeighbors(vertex)) {
			potentialFriends.addAll(getNeighbors(i));
		}
		for(Edge e: edges) {
			if(e.getFrom() == vertex && potentialFriends.contains(e.getTo())){
				potentialFriends.remove(e.getTo());
			}
		}
		potentialFriends.remove(vertex);
		return potentialFriends;
		
	}
	

	//helper method that loops through a List of uncovered verticies and finds the one with the
	//highest number of neighbors
	
	public Integer findVertex(List<Integer> uncovered) {
		int largest = 0;
		int result = 0;
		for(Integer i : uncovered) {
			if(getNeighbors(i).size() > largest) {
				largest= getNeighbors(i).size();
				result = i;
			}
		}
		return result;
	}
	
	// The algorithm first creates a List of vertexes and adds into it all the vertexes in the graph 
	//A while loop runs as long as the list idn't empty
	//Every iteration of the loop calls a helper method to find the vertex with the most neighbors
	//Then it removes that vertex and its neighbors from the original list
	//The vertex is added to the dominant set
	//The set is returned as a result
	
	public HashSet<Integer> dominantSet(){
		List<Integer> uncovered = new LinkedList<Integer>();
		uncovered.addAll(verticies.keySet());
		HashSet<Integer> dominant = new HashSet<Integer>();
		while(!uncovered.isEmpty()) {
			Integer largest = findVertex(uncovered);
			uncovered.remove(largest);
			uncovered.removeAll(getNeighbors(largest));
			dominant.add(largest);
		}
		return dominant;
	}
	
	public static void main (String[] args) {
		
		CapGraph test = new CapGraph();
		GraphLoader.loadGraph(test, "data/facebook_1000.txt");
		System.out.println(test.recommendFriends(255));
		System.out.println(test.dominantSet());
		System.out.println(test.numVertex);
		System.out.println(test.dominantSet().size());

		System.out.println("      ");
		
		CapGraph test1 = new CapGraph();
		GraphLoader.loadGraph(test1, "data/facebook_2000.txt");
		System.out.println(test1.recommendFriends(255));
		System.out.println(test1.dominantSet());
		System.out.println(test1.numVertex);
		System.out.println(test1.dominantSet().size());

	}

}


