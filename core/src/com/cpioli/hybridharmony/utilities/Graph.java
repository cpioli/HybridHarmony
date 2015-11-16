package com.cpioli.hybridharmony.utilities;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Array;
import com.cpioli.hybridharmony.playfield.GridCoordinate;
import com.cpioli.hybridharmony.walls.Wall;

/**
 * Used in A* logic
 * Needs: (1) Priority Queue
 *        (2) Nodes for direction
 * @author cpioli
 *
 */
public enum Graph {
	
	INSTANCE;
	
	private Node graph[][];
	private boolean initialized;
	
	Graph() {
		
	}
	
	public void initialize() {
		initialized = true;
		graph = new Node[5][5]; //true/false indicating whether a space is open for movement (true) or whether a wall is blocking the path (false)
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				graph[i][j] = new Node(i, j);
			}
		}
	}
	
	public void updateLevel(Wall wall) {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				graph[i][j].resetWalls();
			}
		}
		
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(wall.coordinateIsWall(i, j) && !(j%2 != 0 && i%2 !=0)) { //if it's a wall and not a little square thingy
					if(i%2 == 0) { //this indicates a long wall blocking two on the y level
						graph[i/2][j/2].travelUp = false;
						graph[i/2][j/2+1].travelDown = false;
					} else if (j%2 == 0) { //indicating a tall wall blocking two on the x level
						graph[i/2][j/2].travelRight = false;
						graph[i/2+1][j/2].travelLeft = false;
					}
				}
			}
		}
	}
	
	public void updateTravelRange(Array<GridCoordinate> selection) {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				graph[i][j].available = false;
			}
		} //reset all nodes
		for(GridCoordinate gCoord : selection) {
			graph[gCoord.getX()][gCoord.getY()].available = true;
			System.out.print("Making available " + gCoord.getX() + ", " + gCoord.getY() + "\n");
		} //set only the available nodes for traversal
	}
	
	public Node getNode(GridCoordinate gc) {
		return graph[gc.getX()][gc.getY()];
	}
	
	public Node getNode(int x, int y) {
		return graph[x][y];
	}
	
	public ArrayList<Node> getNeighbors(int x, int y) {
		ArrayList<Node> four = new ArrayList<Node>(4);
		if(x > 0) {
			if(graph[x-1][y].available) {
				four.add(graph[x-1][y]);
			}
		}
		if(x < 4) {
			if(graph[x+1][y].available) {
				four.add(graph[x+1][y]);
			}
		}
		if(y > 0) {
			if(graph[x][y-1].available) { 
				four.add(graph[x][y-1]);
			}
		}
		if(y < 4) {
			if(graph[x][y+1].available) {
				four.add(graph[x][y+1]);
			}
		}
		return four;
	}
	
	public int manhattan(GridCoordinate gcStart, GridCoordinate gcEnd) {
		int d1 = Math.abs(gcEnd.getX() - gcStart.getX());
		int d2 = Math.abs(gcEnd.getY() - gcStart.getY());
		return d1 + d2;
	}
	
	public int manhattan(int x1, int y1, int x2, int y2) {
		int d1 = Math.abs(x2 - x1);
		int d2 = Math.abs(y2 - y1);
		return d1 + d2;
	}
	
	@Override
	public String toString() {
		String output = "";
		for(int y = 4; y >= 0; y--) {
			for(int x = 0; x < 5; x++) {
				if(graph[x][y].visited) {
					output += "V";
				} else if (graph[x][y].available){
					output += "O";
				} else {
					output += "+";
				}
			}
			output += "\n";
		}
		return output;
	}
	
	public void resetNodes() {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				graph[i][j].resetTraversal();
			}
		}
	}
}