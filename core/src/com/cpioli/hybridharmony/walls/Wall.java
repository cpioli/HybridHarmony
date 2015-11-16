package com.cpioli.hybridharmony.walls;


/**
 * A simple data object storing a 9x9 array of booleans, indicating which
 * walls in the 9x9 playfield grid will be occupied by a wall. Walls are
 * interchangeable.
 * @author cpioli
 *
 */

public class Wall {
	
	private boolean wallGrid[][];
	private boolean used; //determines if we've already used this object in a game session
	
	public Wall(boolean wallGrid[][]){
		
		this.wallGrid = new boolean[9][9];
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				this.wallGrid[x][y] = wallGrid[y][x]; //I don't know why, but this fixes a WEIRD bug in the game that rotates the walls by 90 degrees
			}
		}
		used = false;
		
		
	}
	
	public boolean[][] getGrid() {
		return wallGrid;
	}
	/**
	 * indicates we're using this in the game
	 */
	public void markForUsage() {
		used = true;
	}
	/**
	 * indicates we have not yet used this in game
	 * this is the default setting in the constructor, it's only used when
	 * resetting the game
	 */
	public void resetUsage() {
		used = false;
	}
	
	public boolean coordinateIsWall(int x, int y) {
		return wallGrid[x][y];
	}
	
	public boolean alreadyUsed() {
		return used;
	}
}