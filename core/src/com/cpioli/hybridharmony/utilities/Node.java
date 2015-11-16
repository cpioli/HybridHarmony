package com.cpioli.hybridharmony.utilities;

class Node implements Comparable<Node> {
	boolean travelUp, travelDown, travelLeft, travelRight; //if walls are blocking these, they register as false. specialty booleans
	boolean visited; //already been touched in a previous traversal
	boolean closed; //already been used and traversed
	boolean available; //specialty boolean: is it one of the GC's available for traversal?
	Node parent;
	int x, y;
	int f; //g + h
	int g; //distance between this node and start
	int h; //distance between this node and the nextdoor node, weighed by a heuristic
	final int cost = 1;
	
	public Node(int x, int y) {
		travelUp = true;
		travelDown = true;
		travelLeft = true;
		travelRight = true;
		visited = false;
		closed = false;
		available = true;
		parent = null;
		this.x = x;
		this.y = y;
	}
	
	public void resetWalls() {
		travelUp = true;
		travelDown = true;
		travelLeft = true;
		travelRight = true;
		if(this.x == 0) { //if it's a left element
			travelLeft = false;
		} else if (this.x == 4) {
			travelRight = false;
		}
		if(this.y == 0) { //if it's a bottom element
			travelDown = false;
		} else if (this.y == 4) {
			travelUp = false;
		}
	}
	
	public void resetTraversal() {
		f = 0;
		g = 0;
		h = 0;
		parent = null;
		visited = false;
		closed = false;
	}

	@Override
	public int compareTo(Node node) {
		return this.f - node.f;
	}
	
	@Override
	public String toString() {
		String output = "";
		output += "(" + this.x + ", " + this.y + ") f=" + this.f;
		return output;
	}
}