package com.cpioli.hybridharmony.walls;

public class WallBuilder {
	public WallCache repository;
	
	
	public WallBuilder() {
		repository = new WallCache();
	}
	
	public WallCache addWallsToCache() {
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false,  true, false,  true, false, false, false},
					{false, false, false,  true, false,  true, false, false, false},
					{false, false, false, false, false, false, false, false, false},
					{ true,  true, false, false, false, false, false,  true,  true},
					{false, false, false, false, false, false, false, false, false},
					{ true,  true, false, false, false, false, false,  true,  true},
					{false, false, false, false, false, false, false, false, false},
					{false, false, false,  true, false,  true, false, false, false},
					{false, false, false,  true, false,  true, false, false, false}
				}), 1);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false, false, false,  true, false, false, false},
					{false, false, false,  true,  true,  true, false, false, false},
					{false, false, false, false, false, false, false, false, false},
					{false,  true, false, false, false, false, false,  true,  true},
					{false,  true, false, false, false, false, false,  true, false},
					{ true,  true, false, false, false, false, false,  true, false},
					{false, false, false, false, false, false, false, false, false},
					{false, false, false,  true,  true,  true, false, false, false},
					{false, false, false,  true, false, false, false, false, false},
				}), 1);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false, false, false, false, false, false, false},
					{false, false, false,  true, false,  true, false, false, false},
					{false, false, false,  true, false,  true, false, false, false},
					{false, false, false,  true, false,  true, false, false, false},
					{false, false, false, false, false, false, false, false, false},
					{false,  true,  true,  true, false,  true,  true,  true, false},
					{false, false, false, false, false, false, false, false, false},
					{false, false, false, false, false, false, false, false, false},
					{false, false, false, false, false, false, false, false, false}
				}), 1);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false, false, false, false, false, false, false},
					{false,  true,  true, false, false, false,  true,  true, false},
					{false,  true, false, false, false, false, false,  true, false},
					{false, false, false, false, false, false, false, false, false},
					{false, false, false, false, false, false, false, false, false},
					{false, false, false, false, false, false, false, false, false},
					{false,  true, false, false, false, false, false,  true, false},
					{false,  true,  true, false, false, false,  true,  true, false},
					{false, false, false, false, false, false, false, false, false}
				}), 1);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false, false, false, false, false, false, false},
					{false, false, false,  true,  true,  true, false, false, false},
					{false, false, false, false, false, false, false, false, false},
					{false,  true, false, false, false, false, false,  true, false},
					{false,  true, false, false, false, false, false,  true, false},
					{false,  true, false, false, false, false, false,  true, false},
					{false, false, false, false, false, false, false, false, false},
					{false, false, false,  true,  true,  true, false, false, false},
					{false, false, false, false, false, false, false, false, false}
				}), 1);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false, false, false, false, false, false, false},
					{false, false, false,  true,  true,  true, false, false, false},
					{false, false, false,  true, false, false, false, false, false},
					{false,  true,  true,  true, false, false, false,  true,  true},
					{false, false, false, false, false, false, false, false, false},
					{ true,  true, false, false, false,  true,  true,  true, false},
					{false, false, false, false, false,  true, false, false, false},
					{false, false, false,  true,  true,  true, false, false, false},
					{false, false, false, false, false, false, false, false, false}
				}), 2);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false,  true, false, false, false, false, false, false, false},
					{false,  true,  true,  true, false, false, false, false, false},
					{false, false, false,  true, false, false, false, false, false},
					{false, false, false,  true, false, false, false, false, false},
					{false, false, false,  true, false,  true, false, false, false},
					{false, false, false, false, false,  true, false, false, false},
					{false, false, false, false, false,  true, false, false, false},
					{false, false, false, false, false,  true,  true,  true, false},
					{false, false, false, false, false, false, false,  true, false}
				}), 2);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false, false, false, false, false, false, false},
					{false, false, false,  true, false,  true, false, false, false},
					{false, false, false,  true, false,  true, false, false, false},
					{false,  true,  true,  true, false,  true,  true,  true, false},
					{false, false, false, false, false, false, false, false, false},
					{false,  true,  true,  true, false,  true,  true,  true, false},
					{false, false, false,  true, false,  true, false, false, false},
					{false, false, false,  true, false,  true, false, false, false},
					{false, false, false, false, false, false, false, false, false}
				}), 2);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false,  true, false, false, false, false, false},
					{false, false, false,  true, false, false, false, false, false},
					{false, false, false,  true, false, false, false, false, false},
					{false, false, false,  true, false, false, false, false, false},
					{false, false, false, false, false, false, false, false, false},
					{ true,  true, false, false, false,  true,  true,  true,  true},
					{false, false, false, false, false, false, false, false, false},
					{false, false, false,  true, false, false, false, false, false},
					{false, false, false,  true, false, false, false, false, false}
				}), 2);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false,  true, false,  true, false, false, false},
					{false,  true,  true,  true, false,  true,  true,  true, false},
					{false,  true, false, false, false, false, false,  true, false},
					{false,  true, false, false, false, false, false,  true, false},
					{false, false, false, false, false, false, false, false, false},
					{false,  true, false, false, false, false, false,  true, false},
					{false,  true, false, false, false, false, false,  true, false},
					{false,  true,  true,  true, false,  true,  true,  true, false},
					{false, false, false, false, false, false, false, false, false}
				}), 2);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false, false, false,  true, false, false, false},
					{false, false, false,  true,  true,  true, false, false, false},
					{false, false, false, false, false,  true, false, false, false},
					{false,  true,  true,  true, false,  true, false,  true,  true},
					{false,  true, false, false, false, false, false,  true, false},
					{ true,  true, false,  true, false,  true,  true,  true, false},
					{false, false, false,  true, false, false, false, false, false},
					{false, false, false,  true,  true,  true, false, false, false},
					{false, false, false,  true, false, false, false, false, false}
				}), 3);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false,  true, false, false, false,  true, false, false, false},
					{false,  true, false,  true, false,  true, false,  true, false},
					{false, false, false,  true, false, false, false,  true, false},
					{false,  true, false,  true, false,  true, false,  true, false},
					{false,  true, false, false, false,  true, false, false, false},
					{false,  true, false,  true, false,  true, false,  true, false},
					{false, false, false,  true, false, false, false,  true, false},
					{false,  true, false,  true, false,  true, false,  true, false},
					{false,  true, false, false, false,  true, false, false, false}
				}), 3);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false,  true, false, false, false, false, false, false, false},
					{false,  true, false,  true,  true,  true,  true,  true,  true},
					{false,  true, false, false, false, false, false, false, false},
					{false,  true, false,  true, false,  true,  true,  true, false},
					{false,  true, false,  true, false,  true, false,  true, false},
					{false,  true,  true,  true, false,  true, false,  true, false},
					{false, false, false, false, false, false, false,  true, false},
					{ true,  true,  true,  true,  true,  true, false,  true, false},
					{false, false, false, false, false, false, false,  true, false}
				}), 3);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false, false, false, false, false, false, false},
					{ true,  true, false,  true,  true,  true, false,  true,  true},
					{false, false, false, false, false, false, false, false, false},
					{false,  true,  true,  true, false,  true,  true,  true, false},
					{false, false, false, false, false, false, false, false, false},
					{ true,  true, false,  true,  true,  true, false,  true,  true},
					{false, false, false, false, false, false, false, false, false},
					{false,  true,  true,  true, false,  true,  true,  true, false},
					{false, false, false, false, false, false, false, false, false}
				}), 3);
		
		repository.addWall(
				new Wall(new boolean[][]{
					{false, false, false, false, false,  true, false, false, false},
					{false, false, false, false, false,  true, false, false, false},
					{false, false, false, false, false,  true, false, false, false},
					{ true,  true,  true, false, false, false, false, false, false},
					{false, false, false, false, false, false, false, false, false},
					{false, false, false, false, false, false,  true,  true,  true},
					{false, false, false,  true, false, false, false, false, false},
					{false, false, false,  true, false, false, false, false, false},
					{false, false, false,  true, false, false, false, false, false}
				}), 3);
		
		return repository;
	}
}