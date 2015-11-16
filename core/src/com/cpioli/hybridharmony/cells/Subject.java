package com.cpioli.hybridharmony.cells;

import com.cpioli.hybridharmony.playfield.MeshActor;

public interface Subject {
	/**
	 * registers an actor to receive status updates on this Cell. It will be 
	 * notified of state, color and selection changes so it can make the 
	 * appropriate changes to itself.
	 * 
	 * @param meshActor
	 */
	public void registerObserver(MeshActor meshActor);
	
	/**
	 * This is unlikely to be used, but it might be useful down the line...
	 * removes a MeshActor from this cell's broadcast.
	 * @param meshActor
	 */
	public void removeObserver(MeshActor meshActor);
	
	/**
	 * notify the observers that a change has been made, and that they need to
	 * make an update and know what to do about the changes to the Cell.
	 */
	public void notifyObservers();
}


