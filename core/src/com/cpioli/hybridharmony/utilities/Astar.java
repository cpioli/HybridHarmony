package com.cpioli.hybridharmony.utilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import com.badlogic.gdx.utils.Array;
import com.cpioli.hybridharmony.playfield.GridCoordinate;
import com.cpioli.hybridharmony.walls.Wall;

public enum Astar {
	INSTANCE;
	private static Comparator<Node> nodeComparator;
	private PriorityQueue<Node> nodeHeap;
	private boolean initialized;
	
	Astar() {
		initialized = false;
	}
	
	public void initialize(Wall wall) {
		if(!initialized) {
			nodeComparator = new NodeComparator();
			Graph.INSTANCE.initialize();
			Graph.INSTANCE.updateLevel(wall);
			CellAnimationPath.INSTANCE.initialize();
			nodeHeap = new PriorityQueue<Node>(15, nodeComparator);
			initialized = true;
		}
	}
	
	public void updateLevel(Wall wall) {
		Graph.INSTANCE.updateLevel(wall);
	}
	/**
	 * Given a list of GridCoordinates, return an array of
	 * @param selection
	 * @return
	 */
	public void search(Array<GridCoordinate> selection) {
		//initialize fields and reset the nodeHeap
		for(GridCoordinate gc: selection) {
			System.out.print("(" + gc.getX() + ", " + gc.getY() + ")");
		}
		//System.out.print("\n");
		GridCoordinate start;
		GridCoordinate end = selection.get(selection.size - 1);
		//System.out.print("Start: (" + start.getX() + ", " + start.getY() + ")\n");
		//System.out.print("End: (" + end.getX() + ", " + end.getY() + ")\n");

		//reset the availability of Graph's nodes
		Graph.INSTANCE.updateTravelRange(selection);
		CellAnimationPath.INSTANCE.clearResults();
		//do the algorithm on one single Cell

		//System.out.println(nodeHeap.element().toString());
		//System.out.println(Graph.INSTANCE.toString());
		int i = 0;
		while(i < selection.size) {
			Array<GridCoordinate> results = new Array<GridCoordinate>();
			start = selection.get(i);
			System.out.println(i + ":");
			nodeHeap.clear();
			nodeHeap.add(Graph.INSTANCE.getNode(start));
			Graph.INSTANCE.resetNodes();
			while(nodeHeap.size() > 0) {
				//System.out.println("Current queue setup: " + nodeHeap.toString());			
				Node currentNode = nodeHeap.remove();
				
				if(currentNode.x == end.getX() && currentNode.y == end.getY()) { //end case
					Node curr = currentNode;
					System.out.println("About to complete.");
					String output = "Completed! ";
					while(curr.parent != null) {
						output += "(" + curr.x + ", " + curr.y + ") ";
						results.add(new GridCoordinate(curr.x, curr.y));
						curr = curr.parent;
					}
					System.out.println(output);
					results.reverse();
					CellAnimationPath.INSTANCE.addResults(results, start);
				}
			
				currentNode.closed = true;
				
				ArrayList<Node> neighbors = Graph.INSTANCE.getNeighbors(currentNode.x, currentNode.y);
				//System.out.println("neighbors: " + neighbors.toString());
				for(int j = 0; j < neighbors.size(); j++) {
					Node neighbor = neighbors.get(j);
					int dx = neighbor.x - currentNode.x;
					int dy = neighbor.y - currentNode.y;
					
					if(neighbor.closed) {
						continue;
					}	
					if(dx > 0 && !currentNode.travelRight) {
						continue;
					}
					if(dx < 0 && !currentNode.travelLeft){
						continue;
					}
					if(dy > 0 && !currentNode.travelUp){
						continue;
					}	
					if(dy < 0 && !currentNode.travelDown) {
						continue;
					}
				
					int gScore = currentNode.g + neighbor.cost;
					boolean beenVisited = neighbor.visited;
				
					if(!beenVisited || gScore < neighbor.g){ //this is a node we will traverse b/c it's cheaper!
						neighbor.visited = true;
						neighbor.parent = currentNode;
						neighbor.h = Graph.INSTANCE.manhattan(neighbor.x, neighbor.y, end.getX(), end.getY());
						neighbor.g = gScore;
						neighbor.f = neighbor.g + neighbor.h;
						nodeHeap.add(neighbor);
					}	
				
				}
			
			}
			i++;
		}
	}
}

class NodeComparator implements Comparator<Node> {

	@Override
	public int compare(Node node0, Node node1) {
		return node0.f - node1.f;
	}
	
}