package com.cpioli.hybridharmony.playfield;


public interface PlayfieldObserver {
	public void update(Playfield.PlayfieldUpdateType updateType, Object object);
}