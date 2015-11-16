package com.cpioli.hybridharmony.playfield;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.cpioli.hybridharmony.statistics.Vial;

/**
 * this class acts as a mirror of the Selection class in Hybrid Harmony, but
 * it's adapted to handle the scene2d ui resources instead of my bare-bones
 * stuff.
 * 
 * This contains an array of MeshActors, and in addition contains meta-data
 * 
 * Once we add color to the game, this data structure will become useful
 * @author cpioli
 *
 *
 *UPDATE: 12/27/12:
 *
 *1) Implement the Subject interface
 *2) Add color variables
 *3) Take GridElements instead of GridCoordinates into the Array
 *4) Remove the Selection Visualizer
 *
 *GridElement and Subject will not yet be connected. Right now we'll work on
 *updating the Selection for color. Then the mesh actor.
 */
public class Selection extends Array<Cell> implements SelectionSubject {
	
	public static enum removalCondition {
		SUBMISSION, //to be passed to Selection if we're submitting selection
		DESELECTION //to be passed to Selection if we're removing one or more cells
	}
	
	String output;
	
	private float cumulativeColor;
	private float value255;
	private static Color currentColor;
	private ArrayList<SelectionObserver> observers;
	
	public Selection() {
		super(true, 12, Cell.class); //changed from 6 to 12 on 7/11/12
		cumulativeColor = 0.0f;
		value255 = 0.0f;
		output = new String();
		currentColor = new Color(Color.GRAY);
		observers = new ArrayList<SelectionObserver>();
	}
	
	@Override
	public void add(Cell cell){
		if(!this.contains(cell, false)){
			super.add(cell);

			if(cell.getColorValue() == 8) {
				cumulativeColor += 255.0f;
			} else {
				cumulativeColor += (cell.getColorValue() * 32.0f);
			}
			
			value255 = cumulativeColor / ((float)super.size);
			updateColor(value255);
			cell.notifyOfAdditionToSelection();
		}
		notifyObservers();
	}
	

	public void removeFromSelection(Cell cell, removalCondition condition) {
		if(contains(cell, true)){
			super.removeValue(cell, true);
			
			if(cell.getColorValue() == 8) {
				cumulativeColor -= 255.0f;
			} else {
				cumulativeColor -= (cell.getColorValue() * 32.0f);
			}
			
			if(super.size > 0) {
				value255 = cumulativeColor / ((float)super.size);
			} else value255 = 0;
			
			if(condition == removalCondition.DESELECTION) {
				cell.notifyOfRemoval();
				updateColor(value255);
				notifyObservers();
			}
		}
		//selViz.updateSelectionVisualizer(this.items, this.size);

	}
	
	public Cell pop(removalCondition condition) {
		Cell popped = super.pop();
		
		if(popped.getColorValue() == 8) {
			cumulativeColor -= 255.0f;
		} else {
			cumulativeColor -= (popped.getColorValue() * 32.0f);
		}
		
		if(super.size > 0) {
			value255 = cumulativeColor / ((float)super.size);
		} else {
			value255 = 0;
		}
		
		if(condition == removalCondition.DESELECTION) {
			popped.notifyOfRemoval();
			updateColor(value255);
			notifyObservers();
		}
		//selViz.updateSelectionVisualizer(this.items, this.size);
		return popped;
	}
	/*
	 * Issue: I cannot call "removeFromSelection" from the clear method
	 * because that is changing each individual cell's color at the time of
	 * submission. Fortunately we're specifying SUBMISSION as opposed to 
	 * DESELECTION, which has fixed the outstanding color change issue
	 * But this outstanding issue should be fixed.
	 */
	public void clear(removalCondition condition) {
		for(int i = super.size - 1; i >= 0; i--) {
			this.removeFromSelection(super.get(i), condition);
		}
		cumulativeColor = 0.0f;
		value255 = 0.0f;
		updateColor(value255);
		notifyObservers();
	}
	
	/**
	 * Pythonic implementation of MeshActor
	 */
	@Override
	public Cell get(int index){
		int trueIndex = 0;
		if(index < 0) trueIndex = super.size + index; //pythonic enhancement
		else trueIndex = index;
		return super.get(trueIndex);
	}
	
	private void updateColor(float value255) {
		currentColor.r = value255/255.0f;
		currentColor.g = currentColor.r;
		currentColor.b = currentColor.r; //for black/white only. These will all
		//change once I get the spectrums built.
		currentColor.a = 1.0f;
		//System.out.println("updateColor()'s current Selection Color is " + currentColor.toString().substring(2));
		Cell cell;
		for(int i = 0; i < super.size; i++) {
			cell = super.get(i);
			cell.notifyOfColorChange();
		}
	}
	
	@Override
	public String toString() {
		output = "";
		output += "Cumulative Color: " + this.cumulativeColor;
		output += "\nCurrent Color: " + Float.toString(this.currentColor.r * 255.0f);
		output += "\n\t";
		for(int i = 0; i < super.size; i++) {
			output += get(i).toString();
			output += "\n\t";
		}
		output += "\n";
		return output;
	}
	
	@SuppressWarnings("static-access")
	public Color getColor() {
		//System.out.println("The color being sent from Selection.getColor() is " + this.currentColor.toString().substring(2));
		return this.currentColor;
	}
	
	public Vial generateVial() {
		return new Vial(super.size, cumulativeColor);
	}
	
	//NEEDED: a method that returns a list of every cell's x/y location
	public Array<GridCoordinate> getSelectionArea() {
		Array<GridCoordinate> selectionArea = new Array<GridCoordinate>();
		for(int i = 0; i < super.size; i++) {
			selectionArea.add(super.get(i).getGC());
		}
		
		return selectionArea;
	}

	public void registerObserver(SelectionObserver so) {
		observers.add(so);
		
	}

	public void removeObserver(SelectionObserver so) {
		int i = observers.indexOf(so);
		if(i >= 0) {
			observers.remove(i);
		}
	}
	
	public void notifyObservers() {
		for(int i = 0; i < observers.size(); i++) {
			SelectionObserver so = observers.get(i);
			so.update(super.size, cumulativeColor);
		}
	}
	
	public void reset() {
		super.clear();
		cumulativeColor = 0.0f;
		currentColor = Color.GRAY;
		value255 = 0.0f;
	}
	
	public float getValue255() {
		return this.value255;
	}
}