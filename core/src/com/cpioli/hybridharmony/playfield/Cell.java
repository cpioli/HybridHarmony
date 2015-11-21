package com.cpioli.hybridharmony.playfield;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeTo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.cpioli.hybridharmony.GameService;

public class Cell extends Actor {
	private final float TWEEN_DURATION = 0.25f;
	
	public GridCoordinate gc;
	private Selection selection;
	private int colorValue; //int between 0 and 9 (but NOT 4)
	private float originalDimension; //to return to original dimensions
	private boolean isEndpoint;
	
	private final static Interpolation STRETCH_INTERPOLATION = Interpolation.exp10Out;
	
	public static enum stretchDirection {
		UP,
		DOWN,
		LEFT,
		RIGHT
	};
	
	
	//two enumerated states involving the cell color, shape, and selection state
	//not all of these will be used, but I'd like them all available so I know which
	//will be used and which can be deprecated.
	
	public static enum cellTweeningState {
		STATIC,
		TWEENING_INTO_SELECTION,
		TWEENING_OUTOF_SELECTION,
		TWEENING_INTO_SUBMISSION,
		TWEENING_REEMERGENCE
	};
	protected cellTweeningState cellTweeningState;
	
	public static enum colorTweeningState {
		STATIC,
		TWEENING
	};
	protected colorTweeningState colorTweeningState;
	
	public static enum cellState {
		UNSELECTED,
		SELECTED,
		EMPTY
	};
	protected cellState cellState;
	protected cellState previousCellState;
	
	/**
	 * This is only called when the cell in question is placed into the selection
	 */
	
	public Cell(float x, float y, float dimension, int colorValue, Color color, int gridX, int gridY, Selection selection, String name){
		//super(x, y, dimension, dimension, color, name);
		super.setPosition(x, y);
		super.setWidth(dimension);
		super.setHeight(dimension);
		super.setColor(color);
		super.setName(name);
		this.setColorValue(colorValue);
		this.selection = selection;
		this.gc = new GridCoordinate(gridX, gridY);
		this.setOriginalDimension(dimension);
		this.cellTweeningState = cellTweeningState.STATIC;
		//this.cellTweeningState = cellTweeningState.STATIC;
		//this.colorTweeningState = colorTweeningState.STATIC;
		//this.cellState = cellState.UNSELECTED;
		this.previousCellState = null;
		
		this.isEndpoint = false;
		super.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
				Cell cell = (Cell) event.getListenerActor();
				Playfield playfield = (Playfield) cell.getParent();
				Selection selection = playfield.getSelection();
				if (playfield.getIsDragged()) {
					if (selection.contains(cell, true)) {//if this cell is already in the selection
						if (selection.indexOf(cell, true) == (selection.size - 2)) { //if the index is the second-to-last cell added
							//The following block of code is to be the equivalent of
							//version 1's Moderator.RemoveFromSelection method
							Cell popped = selection.pop(Selection.removalCondition.DESELECTION);
							System.out.println("Removed " + popped.toString() + " from the selection");
							System.out.println(selection.toString());
							//selection.pop(Selection.removalCondition.DESELECTION);
							//System.out.println("Selection contents: " + selection.toString());
						}
					} else if (selection.size == 0 ||
							(cell.uldrNeighbor(selection, cell) && selection.size < 12)) { //UPDATED 7/11/13: changed max selection size from 6 to 12
						//This block of code is the equivalent to version 1's Moderator.addToSelection() method
						System.out.println("Added " + cell.getName() + " to the selection, with color " + cell.getColor().toString().substring(0, 6));
						selection.add(cell);
						System.out.println(selection.toString());
						//cellState = cellState.SELECTED;
						//System.out.println("Selection contents: " + selection.toString());
						//TODO: add action to adjust this cell's width or height
						//check which direction we need to go
					}
				}
			}

			//works with enter/exit, but only the first time after entering. For it to work again
			//you have to exit and re-enter a meshactor
			/*public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("Touch up overrides clicked, but our event is type " + event.getType());
			}*/
		});
	}
	
	//public void draw(SpriteBatch batch, float parentAlpha) {
	//	super.draw(batch, parentAlpha);
	//}

    public void renderShape(ShapeRenderer renderer) {
		System.out.println("Did we reach the render portion for the Cell object?");
		if(renderer.isDrawing()) {
            renderer.setColor(super.getColor());
            renderer.rect(super.getY(), super.getY(), super.getWidth(), super.getHeight());
        }
		//super.render(renderer);

    }

	protected void notifyOfAdditionToSelection() {
		//cellState = cellState.SELECTED;
		cellTweeningState = cellTweeningState.TWEENING_INTO_SELECTION;
		float newWidth = 0.0f;
		float newHeight = 0.0f;
		if(selection.size > 1) {
			super.addAction(color(selection.getColor(), TWEEN_DURATION));
			Cell stretchingCell = selection.get(-2);
			if (leftOfCell(stretchingCell)) {
				newWidth = 96.0f;
				newHeight = this.getHeight();
			//	stretchingCell.origin = MeshActor.transformOrigin.LOWER_RIGHT;
				stretchingCell.setOrigin(Align.bottomRight);
			}
			if (rightOfCell(stretchingCell)) {
				newWidth = 96.0f;
				newHeight = this.getHeight();
				//stretchingCell.origin = MeshActor.transformOrigin.LOWER_LEFT;
				stretchingCell.setOrigin(Align.bottomLeft);
			}
			if (belowCell(stretchingCell)) {
				newWidth = this.getWidth();
				newHeight = 96.0f;
			//	stretchingCell.origin = MeshActor.transformOrigin.UPPER_LEFT;
				stretchingCell.setOrigin(Align.topLeft);
			}
			if (aboveCell(stretchingCell)) {
				newWidth = this.getWidth();
				newHeight = 96.0f;
			//	stretchingCell.origin = MeshActor.transformOrigin.LOWER_LEFT;
				stretchingCell.setOrigin(Align.bottomLeft);
			}
			stretchingCell.addAction(sequence(sizeTo(newWidth, newHeight, TWEEN_DURATION, STRETCH_INTERPOLATION), new Action() {
				public boolean act(float delta) {
					Cell cell = (Cell)this.getActor();
					//cell.setOrigin(MeshActor.transformOrigin.LOWER_LEFT);
					cell.setOrigin(Align.bottomLeft);
					cell.cellTweeningState = cellTweeningState.STATIC; //returning to non-tweening state
					return true;
				}}));
		}
	}
	
	protected void notifyOfRemoval() {
		//cellState = cellState.UNSELECTED;
		//System.out.println("Cell " + this.getGC().x + "," + this.getGC().y + " has been removed");
		cellTweeningState = cellTweeningState.TWEENING_OUTOF_SELECTION;
		super.addAction(color(((Playfield)this.getParent()).getSpectrumColor(this.getColorValue()), TWEEN_DURATION));
		System.out.println("We're returning the cell " + this.getGridX() + "," + this.getGridY() + " to its original color");
		SequenceAction sAction = null;
		if(selection.size > 0) {
			Cell stretchingCell = selection.get(-1);
			sAction = (SequenceAction)stillStretching(stretchingCell);
		
			if(sAction != null) {
				sAction.reset();
				//stretchingCell.setOrigin(MeshActor.transformOrigin.LOWER_LEFT);
				stretchingCell.setOrigin(Align.bottomLeft);
			}
			if (leftOfCell(stretchingCell)) {
			//	stretchingCell.origin = MeshActor.transformOrigin.LOWER_RIGHT;
				stretchingCell.setOrigin(Align.bottomRight);
			}
			if (rightOfCell(stretchingCell)) {
			//	stretchingCell.origin = MeshActor.transformOrigin.LOWER_LEFT;
				stretchingCell.setOrigin(Align.bottomLeft);
			}
			if (belowCell(stretchingCell)) {
			//	stretchingCell.origin = MeshActor.transformOrigin.UPPER_LEFT;
				stretchingCell.setOrigin(Align.topLeft);
			}
			if (aboveCell(stretchingCell)) {
			//	stretchingCell.origin = MeshActor.transformOrigin.LOWER_LEFT;
				stretchingCell.setOrigin(Align.bottomLeft);
			}
			stretchingCell.addAction(sequence(sizeTo(88.0f, 88.0f, TWEEN_DURATION, STRETCH_INTERPOLATION), new Action() {
				public boolean act(float delta) {
					Cell cell = (Cell)this.getActor();
					//cell.setOrigin(MeshActor.transformOrigin.LOWER_LEFT);
					cell.cellTweeningState = cellTweeningState.STATIC; //returning to non-tweening state
					return true;
				}}));
		}
	}
	/**
	 * This method will call the animation actions.
	 */
	private final float VIAL_SUBMISSION_SPEED = .75f;
	private final float VIAL_ADDITION_SPEED = 0.5f;
	//private final float DROP_SPEED = 0.25f;
	
	protected void notifyOfSubmission(int location) { //location is a value between 0 and 5
		this.setColor(selection.getColor());
		super.setTouchable(Touchable.disabled);
		super.addAction(delay((0.15f * (float)location), (Action)sequence(
				parallel(moveTo(600.0f, 440.0f, VIAL_SUBMISSION_SPEED), 
						 sizeTo(15.0f, 15.0f, VIAL_SUBMISSION_SPEED)),
						 delay(5.0f),
				new Action() {
					public boolean act(float delta) {
						Cell cell = (Cell)this.getActor();
						cell.setSize(18.0f, 18.0f);
						//cell.setOrigin(MeshActor.transformOrigin.CENTER);
						cell.setOrigin(Align.center);
						float xloc = (float)(8*(cell.getGC().x) + 88*cell.getGC().x);
						float yloc = (float)(96*cell.getGC().y);
						//cell.setXY(xloc, yloc);
						cell.setOrigin(xloc, yloc);
						cell.setColorValue(GameService.INSTANCE.generateNewCell());
						//cell.setCellOriginalColor();
						cell.setColor(Color.WHITE);
						cell.addAction(
                                sequence(
										Actions.moveBy(35.0f, 35.0f, VIAL_ADDITION_SPEED, Interpolation.exp5Out),
										parallel(
												color(
														((Playfield)cell.getParent()).getSpectrumColor(cell.getColorValue())
														, VIAL_ADDITION_SPEED, Interpolation.exp5Out),
												sizeTo(cell.getOriginalDimension(), cell.getOriginalDimension(), VIAL_ADDITION_SPEED, Interpolation.exp5Out)),
											new Action() {
												public boolean act(float delta) {
													Cell cell = (Cell)this.getActor();
													//cell.cellState = cellState.UNSELECTED;
													cell.setTouchable(Touchable.enabled);
													cell.setPosition(96.0f * cell.getGC().x, 96.0f * cell.getGC().y);
													//System.out.println("Cell " + cell.getName() + " is now the selection's color " + cell.getColor().toString().substring(2));
													return true;
												}
											}));
										//cell.cellState = cellState.UNSELECTED;
						return true;
					}
				}
		)));
		//System.out.println("We've added the new Action to the submitted vial");
				
	}

	protected void notifyOfColorChange() {
		//update the color
		ColorAction cAction = stillChangingColor(this);
		if(cAction == null) { //if in fact the color is no longer changing
			//System.out.println("Color change in Cell.notifyOfColorChange().\nThis cell had a color of " + this.toString());
			super.addAction(color(selection.getColor(), TWEEN_DURATION));
		} else {
			cAction.reset();
			//System.out.println("Color change in Cell.notifyOfColorChange().\nThis cell had a color of " + this.toString());
			super.addAction(color(selection.getColor(), TWEEN_DURATION));
		}
	}
	
	/**
	 * Changing to include addition of wall.
	 * @param selection
	 * @param cell
	 * @return
	 */
	private boolean uldrNeighbor(Selection selection, Cell cell) {
		GridCoordinate lastCell = selection.get(-1).getGC();
		GridCoordinate currentCell = cell.getGC();
		
		Playfield playfield = (Playfield)this.getParent();
		if(currentCell.getX() == lastCell.getX()) { //if it could be an upper or lower neighbor
			int dy = currentCell.y - lastCell.y;
			int increment = 0;
			int wall_locx = 0;
			int wall_locy = 0;
			if(dy < 0) {
				increment = -1;
			} else if (dy > 0) {
				increment = 1;
			}
			int counter = 0;
			int end = Math.abs(dy);
			for(int i = lastCell.y + increment; counter < end; i += increment) {
				Cell examine = (Cell)playfield.findActor("" + currentCell.x + i);
				wall_locx = currentCell.x * 2;
				wall_locy = i * 2 - increment;
				if(!selection.contains(examine, false)) {
					if(!examine.equals(cell)) {
						return false;
					}
				}
				if(playfield.currentWall.coordinateIsWall(wall_locx, wall_locy)) {
					return false;
				}
				counter++;
			}
			
			return true;
		} else if(currentCell.getY() == lastCell.getY()) {
			int dx = currentCell.x - lastCell.getX();
			int increment = 0;
			int wall_locx = 0;
			int wall_locy = 0;
			if(dx < 0) {
				increment = -1;
			} else if(dx > 0) {
				increment = 1;
			}
			int counter = 0;
			int end = Math.abs(dx);
			for(int i = lastCell.x + increment; counter < end; i+= increment) {
				Cell examine = (Cell)playfield.findActor("" + i + currentCell.y);
				wall_locx = i * 2 - increment;
				wall_locy = currentCell.getY() * 2;
				if(!selection.contains(examine, false)) {
					if(!examine.equals(cell)) {
						return false;
					}
				}
				if(playfield.currentWall.coordinateIsWall(wall_locx, wall_locy)) {
					return false;
				}
				counter++;
			}
			
			return true;
		}
		return false; //in all other cases we return false
	}
		
	/**
	 * returns an action that is making the MeshActor stretch
	 * Returns null if no such Action is in use
	 * @param cell
	 * @return
	 */
		
	private static Action stillStretching(Cell cell) {
		Array<Action> actionArray = cell.getActions();
		for(int i = 0; i < actionArray.size; i++) {
			Action a = actionArray.get(i);
			if(a instanceof SequenceAction) {
				return a;
			}
		}
		
		return null;
	}
	
	private static ColorAction stillChangingColor(Cell cell) {
		Array<Action> actionArray = cell.getActions();
		for(int i = 0; i < actionArray.size; i++) {
			Action a = actionArray.get(i);
			if(a instanceof ColorAction) {
				return (ColorAction)a;
			}
		}
		
		return null;
	}
	
	//to ensure the fill is completed when selecting the next cell, this
	//method completes the stretch incase the player touched so fast the
	//program did not detect it should stretch to MAX_SIZE
	/*public void fillRemainderStretch(Cell.stretchDirection direction){
		switch(direction) {
		case LEFT:
			this.setOrigin(transformOrigin.LOWER_RIGHT);
			this.setWidth(MAX_SIZE);
			this.setOrigin(transformOrigin.LOWER_LEFT);
		break;
		
		case RIGHT:
			this.setOrigin(transformOrigin.LOWER_LEFT);
			this.setWidth(MAX_SIZE);
			//LOWER_LEFT is the default origin, so we're not going to run this
			//command a second time.
		break;
		
		case UP:
			System.out.println("NOT YET IMPLEMENTED");
		break;
		
		case DOWN:
			System.out.println("NOT YET IMPLEMENTED");
		break;
		}
	}*/
	
	public int getGridX() {
		return gc.getX();
	}

	public int getGridY() {
		return gc.getY();
	}
	
	public GridCoordinate getGC() {
		return this.gc;
	}

	public int getColorValue() {
		return colorValue;
	}
	
	//finds out if this cell is below the cell passed as a parameter
	public boolean belowCell(Cell cell) {
		return (this.getGridY() - cell.getGridY() < 0);
	}
	
	//finds out if this cell is above the cell passed as a parameter
	public boolean aboveCell(Cell cell) {
		return (this.getGridY() - cell.getGridY() > 0);
	}
	
	//finds out if this cell is to the left of the cell passed as a parameter
	public boolean leftOfCell(Cell cell) {
		return (this.getGridX() - cell.getGridX() < 0);
	}
	
	//finds out if this cell is to the right of the cell passed as a parameter
	public boolean rightOfCell(Cell cell) {
		return (this.getGridX() - cell.getGridX() > 0);
	}
	
	@Override
	public String toString() {
		String output = "";
		output += "Cell " + this.getName() + ", " + this.getColor().toString().substring(0,6);
		return output;
	}
	
	public boolean isEndpoint() {
		return isEndpoint;
	}

	public void setEndpoint(boolean isEndpoint) {
		this.isEndpoint = isEndpoint;
	}
	
	protected void reset() {
		//this.cellState = cellState.UNSELECTED;
		this.clearActions();
		this.setPosition(this.getGC().getX() * 96.0f, this.getGC().getY() * 96.0f);
		this.setSize(88.0f, 88.0f);
		this.setColorValue(GameService.INSTANCE.generateNewCell());
		this.setColor(((Playfield)this.getParent()).getSpectrumColor(this.getColorValue()));
		this.isEndpoint = false;
	}
	
	public boolean equals(Object cellObject) {
		if(this == cellObject) {
			return true;
		}
		
		if(!(cellObject instanceof Cell)) {
			return false;
		}
		GridCoordinate gc1 = this.getGC();
		GridCoordinate gc2 = ((Cell)cellObject).getGC();
		return (gc1.equals(gc2));
	}

	public void setColorValue(int colorValue) {
		this.colorValue = colorValue;
	}

	public float getOriginalDimension() {
		return originalDimension;
	}

	public void setOriginalDimension(float originalDimension) {
		this.originalDimension = originalDimension;
	}
	
}