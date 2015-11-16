package com.cpioli.hybridharmony.playfield;

public interface PlayfieldSubject {
	
	public void registerObserver(PlayfieldObserver po);
	public void removeObserver(PlayfieldObserver po);
	public void notifyObservers(Playfield.PlayfieldUpdateType udpateType, Object object);
}