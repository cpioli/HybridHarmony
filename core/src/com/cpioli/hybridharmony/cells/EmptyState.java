package com.cpioli.hybridharmony.cells;

import com.cpioli.hybridharmony.playfield.Selection;
import com.cpioli.hybridharmony.spectrum.Spectrum;

public class EmptyState implements CellState {
	
	GridElement gridElement;
	
	public EmptyState(GridElement gridElement) {
		this.gridElement = gridElement;
	}
	
	public Spectrum getColor() {
		return null;
	}

	public Selection getSelection() {
		return null;
	}

	public void removeFromSelection() {
		// nothing occurs
		
	}

	/**
	 * This function returns the long value of the gridElement's countdown
	 * timer. Insert bad Europe reference here.
	 * 
	 * I have not finalized how this will work. It all depends on how I can 
	 * implement the countdown timer.
	 */
	public long countdownTicker() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean setColor(Spectrum sColor) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addToSelection(Selection selection) {
		// TODO Auto-generated method stub
		return false;
	}

	public void submission() {
		// TODO Auto-generated method stub
		
	}
	
}