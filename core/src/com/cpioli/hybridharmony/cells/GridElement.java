package com.cpioli.hybridharmony.cells;

import com.cpioli.hybridharmony.playfield.GridCoordinate;
import com.cpioli.hybridharmony.playfield.MeshActor;
import com.cpioli.hybridharmony.playfield.Selection;
import com.cpioli.hybridharmony.spectrum.Spectrum;

public class GridElement implements Subject, Observer {
	
	public static final int UNSELECTED_STATE = 0;
	public static final int SELECTED_STATE = 1;
	public static final int EMPTY_STATE = 2;
	
	CellState cellState;
	SelectedState selectedState;
	EmptyState emptyState;
	UnselectedState unselectedState;
	
	@SuppressWarnings("unused")
	private MeshActor subscribedMeshActor;
	private GridCoordinate gc;
	@SuppressWarnings("unused")
	private Spectrum sColor;
	@SuppressWarnings("unused")
	private int colorValue; //a value between 0 and 9, NOT 4
	@SuppressWarnings("unused")
	private int state;
	private Selection selection;
	
	
	public GridElement(GridCoordinate gridCoordinate, Spectrum sColor, int colorValue, MeshActor meshActor) {
		this.subscribedMeshActor = meshActor;
		this.gc = gridCoordinate;
		this.sColor = sColor;
		this.colorValue = colorValue;
		this.unselectedState = new UnselectedState(this);
		this.selectedState = new SelectedState(this);
		this.emptyState =  new EmptyState(this);
		this.cellState = unselectedState;
		this.registerObserver(meshActor);
		this.selection = null;
		this.state = UNSELECTED_STATE;
		
	}
	
	public GridElement(GridCoordinate gridCoordinate){
		this(gridCoordinate, null, 0, null);
	}
	
	public GridElement(GridCoordinate gridCoordinate, Spectrum sColor, int colorValue) {
		this(gridCoordinate, sColor, 0, null);
	}

	public GridCoordinate getCoordinates() {
		return this.gc;
	}

	public Spectrum getColor() {
		return cellState.getColor();
	}

	public Selection getSelection() {
		return selection;
	}
	
	public boolean addToSelection() {
		cellState.addToSelection(selection);
		return false;
	}

	public void removeFromSelection() {
		cellState.removeFromSelection();
	}

	public long countdownTicker() {
		return cellState.countdownTicker();
	}
	
	/*--------------------------------------------
	 * Methods for the States to modify the GridElement
	 *--------------------------------------------
	 */
	
	/**
	 * sets the state of the cell. Only the CellStates can control this.
	 * @param cellState
	 */
	protected void setState(CellState cellState) {
		this.cellState = cellState;
		if(this.cellState instanceof UnselectedState)
			this.state = UNSELECTED_STATE;
		else if(this.cellState instanceof SelectedState)
			this.state = SELECTED_STATE;
		else if(this.cellState instanceof EmptyState)
			this.state = EMPTY_STATE;
	}
	
	/**
	 * This is run on the gridElement whenever the EmptyState needs to transfer
	 * to the UnselectedState.
	 * @param sColor
	 */
	protected void setBaseColor(Spectrum sColor) {
		
	}
	
	protected boolean unsetSelection() {
		if(this.selection == null)
			return false;
		else {
			this.selection = null;
			return true;
		}
	}
	
	protected void setSelection(Selection selection){
		this.selection = selection;
	}
	
	/**
	 * called by the Selection when this GridElement needs to update
	 */
	public void update() {

	}
	
	public void registerObserver(MeshActor meshActor) {
		this.subscribedMeshActor = meshActor;
	}

	public void removeObserver(MeshActor meshActor) {
		this.subscribedMeshActor = null;
	}

	public void notifyObservers() {
		//subscribedMeshActor.update();
	}
	
}