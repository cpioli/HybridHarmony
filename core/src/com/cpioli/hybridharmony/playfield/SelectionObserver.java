package com.cpioli.hybridharmony.playfield;

public interface SelectionObserver {
	public void update(int numberOfCells, float cumulativeColor);
}