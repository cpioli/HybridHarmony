package com.cpioli.hybridharmony.playfield;

public class GridCoordinate {
	
	int x;
	int y;
	
	public GridCoordinate() {
		this(0, 0);
	}
	
	public GridCoordinate(String gridCoordinate) {
		this.x = Integer.parseInt((String)gridCoordinate.subSequence(0, 1));
		this.y = Integer.parseInt((String)gridCoordinate.subSequence(1, 2));
	}
	
	public GridCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public static float difference(GridCoordinate gc1, GridCoordinate gc2) {
		int vectorx = Math.abs(gc1.x - gc2.x);
		int vectory = Math.abs(gc1.y - gc2.y);
		return (float)Math.sqrt(vectorx * vectorx + vectory * vectory);
	}
	
	@Override
	public String toString() {
		return "" + x + y;
	}
}