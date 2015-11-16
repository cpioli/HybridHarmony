package com.cpioli.hybridharmony.utilities;

import java.util.Iterator;
import java.util.TreeMap;

import com.badlogic.gdx.utils.Array;
import com.cpioli.hybridharmony.playfield.GridCoordinate;

/**
 * Storage receptacle for Astar's results.
 * @author cpioli
 *
 */

public enum CellAnimationPath {
	INSTANCE;
	
	TreeMap<String, Array<GridCoordinate>> choreography;
	private static final int MAX_SIZE = 6;
	private static boolean initialized = false;
	
	
	CellAnimationPath() {
		
	}
	
	public void initialize() {
		if(!initialized) {
			choreography = new TreeMap<String, Array<GridCoordinate>>();
		}
		initialized = true;
	}
	
	/**
	 * Takes an itinerary (containing the list of cell coordinates to traverse)
	 * and the starting point, turning them into one ArrayList<GridCoordinate>.
	 * a reference to the object is stored. we do not make a copy.
	 * @param itinerary
	 */
	public void addResults(Array<GridCoordinate> itinerary, GridCoordinate origin) {
		String location = origin.toString();
		itinerary.insert(0, origin);
		choreography.put(location, itinerary);
	}
	
	public Array<GridCoordinate> getItinerary(GridCoordinate cell) {
		return choreography.get(cell.toString());
	}
	
	public void clearResults() {
		choreography.clear();
	}
	
	@Override
	public String toString() {
		Iterator iter = choreography.navigableKeySet().iterator();
		String output = "";
		//because the iterator treats choreography's keys like a set, this may
		//not behave the way I expect it to.
		while(iter.hasNext()) {
			String key = (String)iter.next();
			output += "Cell at " + key + ":\n\t";
			output += choreography.get(key).toString() + "\n";
		}
		
		return output;
	}
}