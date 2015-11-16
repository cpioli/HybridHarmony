package com.cpioli.hybridharmony.playfield;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SelectionVisualizer extends Actor {
	
	private Quad selectionMarker[][];
	private Quad selectionDirection[];
	private int selectedCells;
	private Color coconut;
	
	public SelectionVisualizer() {
		
		selectionMarker = new Quad[6][4];
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 4; j++) {
				selectionMarker[i][j] = new Quad();
			}
		}
		selectionDirection = new Quad[6];
		for(int i = 0; i < 6; i++) {
			selectionDirection[i] = new Quad();
		}
		selectedCells = 0;
		coconut = new Color(1.0f, 0.988f, 0.811f, 1.0f);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.end();
		for(int i = 0; i < selectedCells;i++){
			selectionMarker[i][0].render();
			selectionMarker[i][1].render();
			selectionMarker[i][2].render();
			selectionMarker[i][3].render();
			if(i < selectedCells - 1) selectionDirection[i].render();
		}
		batch.begin();
		//System.out.println("Visualizer DRAWN");
	}
	
	/**
	 * every time a cell is added or removed, this method is called and the
	 * list of gridcoordinates representing the cells that are in the selection
	 * is passed, along with its size.
	 * 
	 * It's a pain in the ass to think about, so don't
	 */
	public void updateSelectionVisualizer(GridCoordinate gc[], int gcSize){
		//remember spaces are 5, sidelength is 48
		System.out.println(gcSize + ": " + gc.toString());
		
		selectedCells = gcSize;
		//@SuppressWarnings("unused")
		GridCoordinate currentGC;
		if(selectedCells > 1) {//perform all the rendering of these lines
			System.out.println("We'll render the selection lines now");
			GridCoordinate before = new GridCoordinate();
			GridCoordinate after = new GridCoordinate();
			int width = 0;
			int height = 0;
			int llx = 0; //lower left x position
			int lly = 0; //lower-left y position
			int xChange = 0;
			int yChange = 0;
			for(int i = 1; i < selectedCells; i++) {
				before = gc[i - 1];
				after = gc[i];
				xChange = after.x - before.x;
				yChange = after.y - before.y;
				if(xChange != 0 && yChange == 0) { //if we're going in the x direction
					width = 57;
					height = 4;
				} else if (xChange == 0 && yChange != 0) { //if we're going in the y direction
					width = 4;
					height = 57;
				}
				
				if(xChange > 0 || yChange > 0) { //if we're making a rectangle in the positive direction
					llx = 22 + 43*(after.x);
					lly = 22 + 43*(after.y);
				} else if (xChange < 0 || yChange < 0) { //if we're making a rectangle in the negative direction
					llx = 22 + 43*(before.x);
					lly = 22 + 43*(before.y);
				}
				
				selectionDirection[i].setDimensions(llx, lly, width, height);
				selectionDirection[i].setColor(Color.RED);
			}
		}
		
		int t = 2;
		int llx;
		int lly;
		//batch.begin();
		for(int k = 0; k < gcSize;k++){
				currentGC = gc[k];
				llx = 5 + 53*(currentGC.getX());
				lly = 53*(currentGC.getY());
				selectionMarker[k][0].setDimensions(llx, lly, t, 48);
				selectionMarker[k][0].setColor(coconut);
				selectionMarker[k][0].render();
				selectionMarker[k][1].setDimensions(llx, lly, 48, t);
				selectionMarker[k][1].setColor(coconut);
				selectionMarker[k][1].render();
				selectionMarker[k][2].setDimensions(llx, lly + 48 - t, 48, t);
				selectionMarker[k][2].setColor(coconut);
				selectionMarker[k][2].render();
				selectionMarker[k][3].setDimensions(llx + 48 - t, lly, t, 48);
				selectionMarker[k][3].setColor(coconut);
				selectionMarker[k][3].render();
				//font.draw(batch, ""+vacancy.getTimeRemaining(), llx+24, lly+24);
		}
	}
}