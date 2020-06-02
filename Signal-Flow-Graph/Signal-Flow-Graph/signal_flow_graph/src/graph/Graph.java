package graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

class Node {
	private double weight;
	private int numOfNode;

	Node(double weight, int numOfNode) {
		this.weight = weight;
		this.numOfNode = numOfNode;
	}

	public double getWeight() {
		return weight;
	}

	public int getNumOfNode() {
		return numOfNode;
	}

}

public class Graph {
	private int v;
	private int target;
	private LinkedList<Node> adjList[];
	private ArrayList<String> forwardPaths;
	private ArrayList<String> loops;
	private ArrayList<ArrayList<String>> nonTouched;
	private double Delta;
	private double[] deltaForward;

	@SuppressWarnings("unchecked")
	public Graph(int v, int target) {
		this.v = v;
		this.target = target;
		adjList = new LinkedList[v];
		for (int i = 0; i < v; i++) {
			adjList[i] = new LinkedList<>();
		}
		forwardPaths = new ArrayList<String>();
		loops = new ArrayList<String>();
		nonTouched = new ArrayList<ArrayList<String>>();
	}

	public void addEdge(Graph graph, int src, int dest, double weight) throws Exception {
		Node n = new Node(weight, dest);
		for (int i = 0; i < graph.adjList[src].size(); i++) {
			if (graph.adjList[src].get(i).getNumOfNode() == dest) {
				throw new Exception();
			}
		}
		graph.adjList[src].add(n);
	}

	public int size() {
		return v;
	}

	public void printGraph(Graph graph) {
		for (int i = 0; i < graph.size(); i++) {
			System.out.println("Adjacency list of vertex " + i);
			System.out.print("head");
			for (Node j : graph.adjList[i]) {
				System.out.print(" -> (" + j.getNumOfNode() + "," + j.getWeight() + ")");
			}
			System.out.println("\n");
		}
	}

	public void DFS(int vertix) {
		boolean[] visited = new boolean[v];
		String st = "";
		DFSUtil(vertix, visited, st);
	}

	private void DFSUtil(int vertix, boolean[] visited, String st) {
		visited[vertix] = true;
		st = st + vertix + "-";
		Iterator<Node> itr = adjList[vertix].listIterator();
		if (vertix == target) {
			st = st.substring(0, st.length() - 1);
			forwardPaths.add(st);
			return;
		}
		while (itr.hasNext()) {
			Node n = itr.next();
			if (!visited[n.getNumOfNode()]) {
				DFSUtil(n.getNumOfNode(), visited, st);
				visited[n.getNumOfNode()] = false;
			}
		}
	}

	public void GrapghLoops(Graph graph) {
		boolean[] visited = new boolean[v];
		for (int i = 0; i < graph.size(); i++) {
			loopDFS(i, i, visited);
			visited[i] = true;
		}
	}

	private void loopDFS(int vertix, int target, boolean[] visited) {
		boolean[] visitedNodes = new boolean[v];
		String st = "";
		findingLoops(vertix, target, visited, visitedNodes, st);
	}

	private void findingLoops(int vertix, int target, boolean[] visited, boolean[] visitedNodes, String st) {
		visitedNodes[vertix] = true;
		st = st + vertix + "-";
		Iterator<Node> itr = adjList[vertix].listIterator();
		if (vertix == target && st.length() > 2) {
			st = st.substring(0, st.length() - 1);
			loops.add(st);
			return;
		}
		while (itr.hasNext()) {
			Node n = itr.next();
			if ((!visitedNodes[n.getNumOfNode()] || n.getNumOfNode() == target) && !visited[n.getNumOfNode()]) {
				findingLoops(n.getNumOfNode(), target, visited, visitedNodes, st);
				visitedNodes[n.getNumOfNode()] = false;
			}
		}
	}

	public double pathGain(Graph graph, String path) {
		String[] pathSplited = path.split("-");
		double gain = 1;
		for (int i = 0; i < pathSplited.length - 1; i++) {
			int j = Integer.parseInt(pathSplited[i]);
			for (Node n : graph.adjList[j]) {
				if (n.getNumOfNode() == Integer.parseInt(pathSplited[i + 1])) {
					gain = gain * n.getWeight();
				}
			}
		}
		return gain;
	}

	public ArrayList<String> getForwardPaths() {
		return forwardPaths;
	}

	public ArrayList<String> getloops() {
		return loops;
	}

	@SuppressWarnings("unchecked")
	private void twoNonTouched(Graph graph) {
		for (int i = 0; i < graph.loops.size(); i++) {
			for (int j = i + 1; j < graph.loops.size(); j++) {
				if (checkNonTouched(graph.loops.get(i), graph.loops.get(j))) {
					ArrayList<String> arr = new ArrayList<String>();
					arr.add(graph.loops.get(i));
					arr.add(graph.loops.get(j));
					nonTouched.add((ArrayList<String>) arr.clone());
				}
			}
		}
	}

	private boolean checkNonTouched(String loop1, String loop2) {
		String[] l1 = loop1.split("-");
		for (int i = 0; i < l1.length; i++) {
			if (loop2.contains(l1[i])) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public void nonTouchedLoops(Graph graph) {
		twoNonTouched(graph);
		if (graph.nonTouched.isEmpty()) {
			return;
		}
		for (int i = 0; i < graph.nonTouched.size(); i++) {
			ArrayList<String> ar = graph.nonTouched.get(i);
			for (int j = i + 1; j < graph.nonTouched.size(); j++) {
				ArrayList<String> arr = graph.nonTouched.get(j);
				for (int x = 0; x < arr.size(); x++) {
					int counter = -1;
					for (int y = 0; y < ar.size(); y++) {
						if (checkNonTouched(arr.get(x), ar.get(y))) {
							counter++;
						}
					}
					if (counter == ar.size() - 1) {
						ArrayList<String> newUntouched = (ArrayList<String>) ar.clone();
						newUntouched.add(arr.get(x));
						if (!checkSameUntouched(newUntouched)) {
							nonTouched.add((ArrayList<String>) newUntouched.clone());
						}
					}
				}
			}
		}
	}

	private boolean checkSameUntouched(ArrayList<String> unTouched) {
		int counter = 0;
		for (int i = 0; i < nonTouched.size(); i++) {
			ArrayList<String> ar = nonTouched.get(i);
			if (ar.size() == unTouched.size()) {
				for (int x = 0; x < unTouched.size(); x++) {
					for (int y = 0; y < unTouched.size(); y++) {
						if (unTouched.get(x).equals(ar.get(y))) {
							counter++;
							break;
						}
					}
				}
			}
		}
		return counter == unTouched.size();
	}

	public ArrayList<ArrayList<String>> getUntouchedLoops() {
		return nonTouched;
	}

	public double delta(Graph graph) {
		double Delta = 1;
		if (loops.size() == 0) {
			return 1;
		}
		double sum = 0;
		for (int i = 0; i < graph.loops.size(); i++) {
			sum = sum + graph.pathGain(graph, graph.loops.get(i));
		}
		Delta = Delta - sum;
		for (int i = 0; i < graph.nonTouched.size(); i++) {
			ArrayList<String> nonTouchedLoops = graph.nonTouched.get(i);
			double loopGain = 1;
			if (nonTouchedLoops.size() % 2 == 0) {
				for (int j = 0; j < nonTouchedLoops.size(); j++) {
					loopGain = loopGain * pathGain(graph, nonTouchedLoops.get(j));
				}
			} else {
				for (int j = 0; j < nonTouchedLoops.size(); j++) {
					loopGain = loopGain * pathGain(graph, nonTouchedLoops.get(j));
				}
				loopGain = loopGain * -1;
			}
			Delta = Delta + loopGain;
		}
		return Delta;
	}

	private boolean check(String path[], String loop) {
		for (int i = 0; i < path.length; i++) {
			if (loop.contains(path[i])) {
				return false;
			}
		}
		return true;
	}

	public double deltaForward(Graph graph, String forwardPath) {
		String[] path = forwardPath.split("-");
		double Delta = 1;
		if (loops.size() == 0) {
			return 1;
		}
		double sum = 0;
		for (int i = 0; i < graph.loops.size(); i++) {
			if (check(path, graph.loops.get(i))) {
				sum = sum + graph.pathGain(graph, graph.loops.get(i));
			}
		}
		Delta = Delta - sum;
		for (int i = 0; i < graph.nonTouched.size(); i++) {
			ArrayList<String> nonTouchedLoops = graph.nonTouched.get(i);
			double loopGain = 1;
			for (int j = 0; j < nonTouchedLoops.size(); j++) {
				for (int x = 0; x < path.length; x++) {
					if (nonTouchedLoops.get(j).contains(path[x])) {
						loopGain = 0;
						break;
					}
				}
				if (loopGain == 0) {
					break;
				}
			}
			if (loopGain == 0) {
				continue;
			} else {
				if (nonTouchedLoops.size() % 2 == 0) {
					for (int j = 0; j < nonTouchedLoops.size(); j++) {
						loopGain = loopGain * pathGain(graph, nonTouchedLoops.get(i));
					}
				} else {
					for (int j = 0; j < nonTouchedLoops.size(); j++) {
						loopGain = loopGain * pathGain(graph, nonTouchedLoops.get(i));
					}
					loopGain = loopGain * -1;
				}
			}
			Delta = Delta + loopGain;
		}
		return Delta;
	}

	public double overallGain(Graph graph) {
		double overall;
		double delta = graph.delta(graph);
		Delta = delta;
		deltaForward = new double[forwardPaths.size()];
		double deltaForward;
		double forwardGain = 0;
		for (int i = 0; i < graph.forwardPaths.size(); i++) {
			deltaForward = graph.deltaForward(graph, graph.forwardPaths.get(i));
			this.deltaForward[i] = deltaForward;
			forwardGain = forwardGain + graph.pathGain(graph, graph.forwardPaths.get(i)) * deltaForward;
		}
		overall = forwardGain / delta;
		return overall;
	}

	public double getDelta() {
		return Delta;
	}

	public double[] getDeltai() {
		return deltaForward;
	}
}
