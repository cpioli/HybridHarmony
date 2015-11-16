package com.cpioli.hybridharmony.cells;

import com.cpioli.hybridharmony.playfield.Selection;
import com.cpioli.hybridharmony.spectrum.Spectrum;

public class UnselectedState implements CellState {
	GridElement gridElement;
	
	public UnselectedState(GridElement gridElement) {
		this.gridElement = gridElement;
	}
	
	public Spectrum getColor() {
		return gridElement.getColor();
	}
	
	/**
	 * cannot set the color here, return false
	 */
	public boolean setColor(Spectrum sColor) {
		return false;
	}
	
	/**
	 * There is no selection to return, so simply return null
	 */
	public Selection getSelection() {
		return null;
	}

	/**
	 * Currently, adds the gridCoordinate to the selection object and returns
	 * true. When the Selection class is updated, we need to handle an array
	 * exception and possibly return false. As of right now, this method is
	 * VOLATILE
	 */
	public boolean addToSelection(Selection selection) {
		//selection.add(gridElement.getCoordinates());
		gridElement.setSelection(selection);
		//gridElement.setState(gridElement.selectedState);
		return true;
	}

	/**
	 * This method is unused in this class.
	 */
	public void removeFromSelection() {
		
	}
	/**
	 * returns -1 to indicate this ticker is not in use.
	 */
	public long countdownTicker() {
		return -1L;
	}
	
	
	public void submission() {
		// unused
	}
	
}