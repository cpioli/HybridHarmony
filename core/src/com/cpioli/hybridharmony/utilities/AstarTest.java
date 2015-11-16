package com.cpioli.hybridharmony.utilities;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Array;
import com.cpioli.hybridharmony.playfield.GridCoordinate;
import com.cpioli.hybridharmony.walls.Wall;
import com.cpioli.hybridharmony.walls.WallBuilder;
import com.cpioli.hybridharmony.walls.WallCache;

public class AstarTest {
	public static Astar astar;
	protected static Wall currentWall;
	private static WallCache wallCache;
	
	public static void main(String arg0[]) {
		WallBuilder wallBuilder = new WallBuilder();
		wallCache = wallBuilder.addWallsToCache();
		currentWall = wallCache.getWall(0);
		Astar.INSTANCE.initialize(currentWall);
		
		boolean grid[][] = currentWall.getGrid();
		
		for(int i = 8; i >= 0; i--) {
			String ass = "" + (i+1) + " |  ";
			for(int j = 0; j < 9; j++) {
				if(i%2 == 0 && j%2 == 0) {
					ass += "C";
				} else if(i%2 == 1 && j%2 == 1) {
					ass += " ";
				} else if(i%2 == 1) {
					if(grid[j][i] == false) {
						ass += " ";
					} else {
						ass += "-";
					}
				} else if(i%2 == 0) {
					if(grid[j][i] == false) {
						ass += " ";
					} else {
						ass += "|";
					}
				}
			}
			System.out.println(ass);
		}
		
		//Now, to create a series of GC lists
		test1();
		test2();
		test3();
	}
	
	public static void test1() {
		Array<GridCoordinate> selection = new Array<GridCoordinate>();
		selection.add(new GridCoordinate(0, 4));
		selection.add(new GridCoordinate(1, 4));
		selection.add(new GridCoordinate(2, 4));
		selection.add(new GridCoordinate(2, 3));
		selection.add(new GridCoordinate(1, 3));
		selection.add(new GridCoordinate(0, 3));
		Astar.INSTANCE.search(selection);
		System.out.println(CellAnimationPath.INSTANCE.toString());
	}
	
	public static void test2() {
		Array<GridCoordinate> selection = new Array<GridCoordinate>();
		selection.add(new GridCoordinate(1, 2));
		selection.add(new GridCoordinate(2, 2));
		selection.add(new GridCoordinate(2, 1));
		selection.add(new GridCoordinate(2, 0));
		selection.add(new GridCoordinate(3, 0));
		selection.add(new GridCoordinate(3, 1));
		Astar.INSTANCE.search(selection);
		System.out.println(CellAnimationPath.INSTANCE.toString());
	}
	
	public static void test3() {
		Array<GridCoordinate> selection = new Array<GridCoordinate>();
		selection.add(new GridCoordinate(4, 2));
		selection.add(new GridCoordinate(4, 1));
		selection.add(new GridCoordinate(4, 0));
		Astar.INSTANCE.search(selection);
		System.out.println(CellAnimationPath.INSTANCE.toString());
	}
	
	public void test4() {
		
	}
}