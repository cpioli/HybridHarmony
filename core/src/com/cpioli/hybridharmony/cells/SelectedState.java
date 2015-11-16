package com.cpioli.hybridharmony.cells;

import com.cpioli.hybridharmony.playfield.Selection;
import com.cpioli.hybridharmony.spectrum.Spectrum;

public class SelectedState /*implements CellState*/ {
	
	GridElement gridElement;
	
	public SelectedState(GridElement gridElement) {
		this.gridElement = gridElement;
	}
	
	//public Spectrum getColor() {
		//return gridElement.getSelection().getColor();
	//}

	/**
	 * This cell is already in the selection. Will return "false" to indicate
	 * this method is not able to do anything.
	 */
	public boolean addToSelection(Selection selection) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Important note: the Selection class does not yet have color modifying
	 * methods. This will not yet work.
	 */
	public Selection getSelection() {
		return gridElement.getSelection();
	}
	
	
	public void removeFromSelection() {
		//gridElement.getSelection().removeFromSelection(gridElement.getCoordinates());
		gridElement.setState(gridElement.unselectedState);
	}
	
	/**
	 * This is unused in this class. Will return -1 to indicate the GridElement
	 * cannot use a countdownTicker()
	 */
	public long countdownTicker() {
		return -1;
	}

	public boolean setColor(Spectrum sColor) {
		return false;
	}
	
	/**
	 * Called when a submission occurs. There is no
	 * Sets the gridElement to empty
	 * 
	 */
	public void submission() {
		
		
		gridElement.setState(gridElement.emptyState);
		
	}
	
}