package com.cpioli.hybridharmony.walls;

/**
 * Stores arrays of grids, categorized by their difficulty level
 * @author cpioli
 *
 */

public class WallCache {
	private Wall[][] wallRepository;
	private int index1;
	private int index2;
	private int index3;
	//private int[][] wallsUsed; //UNUSED at this stage in the project (2-21-12)
	
	
	public WallCache() {
		wallRepository = new Wall[3][5]; //the difficulty level/limit of walls that can be added
		index1 = 0; //the number of walls we've added to the easy difficulty level
		index2 = 0; //the number of walls we've added to the medium difficulty level
		index3 = 0; //the number of walls we've added to the hard difficulty level
		//wallsUsed = new int[3][5];
	}
	
	public WallCache(Wall wall, int difficulty) {
		if(difficulty == 1){
			wallRepository[difficulty-1][0] = wall;
			index1++;
		} else if(difficulty == 2) {
			wallRepository[difficulty-1][0] = wall;
			index2++;
		} else if(difficulty == 3) {
			wallRepository[difficulty-1][0] = wall;
			index3++;
		}
	}
	
	/**
	 * returns true if successful in adding the wall
	 * returns false otherwise
	 * In later updates, an outOfBounds Exception should be caught
	 * @param wall
	 * @param difficulty
	 * @return
	 */
	public boolean addWall(Wall wall, int difficulty){
		if(difficulty == 1){
			if(index1 < 5){
				wallRepository[difficulty-1][index1] = wall;
				index1++;
				return true;
			}
		} else if(difficulty == 2) {
			if(index2 < 5){
				wallRepository[difficulty-1][index2] = wall;
				index2++;
				return true;
			}
		} else if(difficulty == 3) {
			if(index3 < 5) {
				wallRepository[difficulty-1][index3] = wall;
				index3++;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * This function retrieves a wall design that meets the following requirements:
	 * 1) is appropriate for the level the player has reached
	 * 2) has not yet been used in this play session
	 * 
	 * @param level
	 * @return
	 */
	public Wall retrieveWall(int level) {
		boolean complete = false;
		Wall selection = null;
		int random = 0;
		while(!complete){
			random = (int)(Math.random() * 4.0f);
			if(level > 0 && level < 4) {
				if(random < index1){
					if(!wallRepository[0][random].alreadyUsed()){
						selection = wallRepository[0][random];
						selection.markForUsage();
						complete = true;
					} else
						continue;
				} else
					continue;
			} if(level >= 4 && level < 7) {
				if(random < index2) { //if we've already used this wall
					if(!wallRepository[1][random].alreadyUsed()){
						selection = wallRepository[1][random];
						selection.markForUsage();
						complete = true;
					} else
						continue;
				} else
					continue;
			} if(level >= 7 && level <= 10) {
				if(random < index3) {
					if(!wallRepository[0][random].alreadyUsed()) {
						selection = wallRepository[2][random];
						selection.markForUsage();
						complete = true;
					} else
						continue;
				} else
					continue;
			}
		}
		return selection;
	}
	
	public Wall getWall(int i) {
		return wallRepository[i][2];
	}
	
/*	private boolean contains(int[] wallsUsed, int index, int selection) {
		for(int i = 0; i < index; i++) {
			if(selection == wallsUsed[i])
				return true;
		}
		
		return false;
	}
*/	
	/**
	 * resets the walls' selection flags so we can use them again.
	 * Runs the resetUsage method from the wall objects if the walls
	 * have been used
	 * 
	 * TODO: Complete
	 */
	public void resetWalls() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 5; j++) {
				wallRepository[i][j].resetUsage();
			}
		}
		//index1 = 0;
		//index2 = 0;
		//index3 = 0;
		
		System.out.println("Reset walls method complete");
	}
}