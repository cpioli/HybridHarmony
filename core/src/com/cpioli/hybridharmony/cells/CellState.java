package com.cpioli.hybridharmony.cells;

import com.cpioli.hybridharmony.playfield.Selection;
import com.cpioli.hybridharmony.spectrum.Spectrum;

/**
 * Several states are going to be available
 * 1) Cell: the state of an unselected cell
 * 2) SelectedCell: the state of a cell in a selection
 * 3) Vacancy: an empty cell waiting to be filled
 * @author cpioli
 *
 */
public interface CellState {
	
	/**
	 * returns the color of the cell
	 * @return
	 */
	public Spectrum getColor();
	
	/**
	 * sets the color of the cell. Can only be called in particular situations.
	 * Documentation for each state will include instructions on when the
	 * method can and should be called. Calling this method at any other time
	 * will introduce bugs into the game.
	 */
	
	public boolean setColor(Spectrum sColor);
	
	/**
	 * returns the Selection object containing this cell
	 * @return
	 */
	public Selection getSelection();
	
	/**
	 * A command to change this cell's state from unselected to selected
	 * Adds this cell to a selection, then 
	 * @return
	 */
	public boolean addToSelection(Selection selection);
	
	/**
	 * a command to remove this cell from the Selection, and reverts this selected cell
	 * back to its Cell state
	 */
	public void removeFromSelection();
	
	/**
	 * A command to submit a cell from a selection. Only the SelectedState uses this
	 * state, and it changes the state from the SelectedState to the EmptyState.
	 */
	public void submission();
	
	/**
	 * A countdown timer started when a selection is submitted. It is called by
	 * its MeshActor every frame. When the timer reaches a particular number of
	 * seconds, it returns to the Cell state and is given its color.
	 * The corresponding MeshActor is responsible for notifying the Vacancy it
	 * has to revert back to the Cell state.
	 * @return
	 */
	public long countdownTicker();
	
}