package com.cpioli.hybridharmony.playfield;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeTo;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.cpioli.hybridharmony.Assets;
import com.cpioli.hybridharmony.GameService;
import com.cpioli.hybridharmony.spectrum.Spectrum;
import com.cpioli.hybridharmony.statistics.Vial;
import com.cpioli.hybridharmony.utilities.CellAnimationPath;
import com.cpioli.hybridharmony.walls.Wall;
import com.cpioli.hybridharmony.walls.WallBuilder;
import com.cpioli.hybridharmony.walls.WallCache;


public class Playfield extends Group implements PlayfieldSubject {
	private static enum direction {
		NORTH,
		SOUTH,
		EAST,
		WEST
	};
	private static final float TIME_OF_TRAVERSAL = 5.0f;
	private boolean isDragged;
	private boolean isPaused;
	Cell currentCell;
	Cell cellGrid[][];
	MeshActor beakerDroplet;
	private Selection selection;
	private Image endpointImg1;
	private Image endpointImg2;
	
	//testing this idea I have
	private Array<GridCoordinate> endpoints;
	
	public ArrayList<PlayfieldObserver> observers;
	
	//implementation of Wall grid settings begins now
	protected Wall currentWall;
	private WallCache wallCache;
	private HashMap<String, MeshActor> walls; //special MeshActors that represent walls!
	
	//addition of performance tracking variables begins here
	private float selectionPhaseDuration;
	
	//Objects subscribing to the playfield will react differently to different kinds of updates
	public static enum PlayfieldUpdateType {
		SUBMISSION;
	}
	
	public Playfield(Selection selection) {
		this.selection = selection;
		this.setBounds(30.0f, 30.0f, 480.0f, 480.0f);
		observers = new ArrayList<PlayfieldObserver>();
		isPaused = false;
		System.out.println("Assigning the endpoints");


		generateCells();
		generateWalls();
		generateEndpoints();
		assignEndpoints();
		
		


		//Astar.INSTANCE.initialize(currentWall);

		super.addListener(new ClickListener() {
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Playfield playfield = (Playfield)event.getListenerActor();
				playfield.isDragging();
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Playfield playfield = (Playfield)event.getListenerActor();
				System.out.println("We triggered the touchUp event for the playfield object.");
				//System.out.println(x + " " + y);
				//System.out.println("x: " + playfield.getX() + " - " + playfield.getRight());
				//System.out.println("y: " + playfield.getY() + " - " + playfield.getTop());
				Selection selection = playfield.getSelection();
				playfield.isNotDragging();
				if((playfield.getX() > x || playfield.getRight() < x)
						|| (playfield.getY() > y || playfield.getTop() < y)) {
					System.out.println("We're outside of the playfield area, so we're deselecting the cells.");
					System.out.println("(" + x + ", " + y + ")");
					selection.clear(Selection.removalCondition.DESELECTION);
				} else {
					if(playfield.getTouchable() == Touchable.enabled
						&& selection.size > 0){
						Cell endpoint1 = selection.get(0);
						Cell endpoint2 = selection.get(selection.size - 1);
						System.out.println("Endpoint 1, 2: " + endpoint1.isEndpoint() + ", " + endpoint2.isEndpoint());
						if(endpoint1.isEndpoint() && endpoint2.isEndpoint() && !endpoint1.equals(endpoint2)) {
							System.out.println("We're going to submit this!" + selection.toString());
							performSubmission();
							//TODO: replace "performSubmission()" with "GameService.INSTANCE.changeToSubmissionPhase()"
						} else {
							System.out.println("The selection isn't the correct size, we won't submit it!");
							System.out.println(selection.toString());
							selection.clear(Selection.removalCondition.DESELECTION);
						}
						System.out.println("\n");
					}
				}
			}});
		
		this.selectionPhaseDuration = 0.0f;
	}

	private void generateCells() {
		Cell cell;
		float xloc, yloc;
		int randomGen;
		cellGrid = new Cell[5][5];
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 5; y++) {
				randomGen = 4;
				xloc = (float)(96*x);
				yloc = (float)(96*y);
				randomGen = GameService.INSTANCE.randomGen();
				cell = new Cell(xloc, yloc, 88.0f, randomGen, this.getSpectrumColor(randomGen), x, y, selection, "" + x + y);
				this.addActor(cell);
				cell.setWidth(0.0f);
				cell.setHeight(0.0f);
				cell.setX(cell.getX() + 44.0f);
				cell.setY(cell.getY() + 44.0f);
				//cell.setOrigin(MeshActor.transformOrigin.CENTER);
				cell.setOrigin(Align.center);
				cell.setTouchable(Touchable.disabled);
				cellGrid[x][y] = cell;
			}
		}
	}

	private void generateWalls() {
		WallBuilder wallBuilder = new WallBuilder();
		wallCache = wallBuilder.addWallsToCache();
		currentWall = wallCache.retrieveWall(1); //only going to use one of the first three walls to start with
		walls = new HashMap<String, MeshActor>();
		GridCoordinate gc = new GridCoordinate(0, 0);
		for(int i = 8; i >= 0; i--) {
			for(int j = 0; j < 9; j++) {
				//walls are only there if the i or j values are odd (or both)
				//the meshactor's location will be (i
				MeshActor actor;
				if(i%2 == 1 || j%2 == 1) {
					float x, y;
					gc.x = i;
					gc.y = j;
					if(i%2 == 0 && j%2 == 1) {//in this case we have a long mesh actor
						x = (float)(96 * (i/2));
						y = (float)(88 + 96 * (j/2));
						actor = new MeshActor(x, y, 88.0f, 8.0f, Color.RED, null);
						this.addActor(actor);
						walls.put(gc.toString(), actor);
						//System.out.println("New wall at (" + i + ", " + j + ") is (" + x + ", " + y + ") whose dimensions are 48wx5h");
					} else if (i%2 == 1 && j%2 == 0) {
						x = (float)(88 + 96 * (i/2));
						y = (float)(96 * (j/2));
						actor = new MeshActor(x, y, 8.0f, 88.0f, Color.RED, null);
						this.addActor(actor);
						walls.put(gc.toString(), actor);
						//System.out.println("New wall at (" + i + ", " + j + ") is (" + x + ", " + y + ") whose dimensions are 5wx48h");
					} else if (i%2 == 1 && j%2 == 1) {
						x = (float)(88 + 96 * (i/2));
						y = (float)(88 + 96 * (j/2));
						actor = new MeshActor(x, y, 8.0f, 8.0f, Color.RED, null);
						this.addActor(actor);
						walls.put(gc.toString(), actor);
						//System.out.println("New wall at (" + i + ", " + j + ") is (" + x + ", " + y + ") whose dimensions are 5wx5h");
					}

					if(currentWall.coordinateIsWall(i, j)) {
						walls.get(gc.toString()).setVisible(true);
					} else {
						walls.get(gc.toString()).setVisible(false);
					}
				}
			}
		}
	}

	public void generateEndpoints() {
		endpointImg1 = Assets.bigEncapsulator1;
		endpointImg1.setPosition(0.0f, 0.0f);
		endpointImg1.setVisible(false);
		endpointImg1.setTouchable(Touchable.disabled);
		endpointImg2 = Assets.bigEncapsulator2;
		endpointImg2.setVisible(false);
		endpointImg2.setPosition(0.0f, 0.0f);
		endpointImg2.setTouchable(Touchable.disabled);
		System.out.println("completed endpoint assignment");

		this.addActor(endpointImg1);
		this.addActor(endpointImg2);
		System.out.println("Added actors to the playfield");

		endpoints = new Array<GridCoordinate>(2);
		endpoints.add(new GridCoordinate(0, 0));
		endpoints.add(new GridCoordinate(0, 0));
		System.out.println("created digital endpoints");
	}

	public void assignEndpoints() {
		//TODO: complete
		genRandomEndpoint(endpoints.get(0));
		while( (endpoints.get(1).getX() == 0 || endpoints.get(1).getY() == 0)
				|| (GridCoordinate.difference(endpoints.get(0), endpoints.get(1)) < 3)) {
			genRandomEndpoint(endpoints.get(0));
			genRandomEndpoint(endpoints.get(1));
		}

		Cell endpoint1 = ((Cell)(this.findActor(endpoints.get(0).toString())));
		if(endpoint1.getColorValue() > 4) { //the cell is white
			System.out.println("Endpoint 1: " + endpoint1.getColorValue() + ", " + endpoint1.getColor().g);
			endpointImg1.setColor(Color.BLACK);
		} else if (endpoint1.getColorValue() < 4) {
			endpointImg1.setColor(Color.WHITE);
		}
		endpoint1.setEndpoint(true);
		endpointImg1.setPosition(endpoints.get(0).x * 96.0f + 25.0f, endpoints.get(0).y * 96.0f + 25.0f);
		endpointImg1.setVisible(true);

		Cell endpoint2 = ((Cell)(this.findActor(endpoints.get(1).toString())));
		if(endpoint2.getColorValue() > 4) {//if cell is white
			System.out.println("Endpoint 2: " + endpoint2.getColorValue() + ", " + endpoint2.getColor().g);
			endpointImg2.setColor(Color.BLACK);
		} else if (endpoint2.getColorValue() < 4) {
			endpointImg2.setColor(Color.WHITE);
		}
		endpoint2.setEndpoint(true);
		endpointImg2.setPosition(endpoints.get(1).x * 96.0f + 25.0f, endpoints.get(1).y * 96.0f + 25.0f); //although I had to subtract 20.0f from each endpoint's x/y position,
		//since these Cells are now relative to the Playfield so much their images.
		System.out.println("Endpoint 1's color: " + endpointImg1.getColor().toString());
		System.out.println("Endpoint 1: " + endpoints.get(0).toString());
		System.out.println("Endpoint 1's xy coordinates: (" + endpoint1.getX() + ", " + endpoint1.getY() + ")");

		System.out.println("Endpoint 2's color: " + endpointImg2.getColor().toString());
		System.out.println("Endpoint 2: " + endpoints.get(1).toString());
		System.out.println("Endpoint 2's xy coordinates: (" + endpoint2.getY() + ", " + endpoint2.getY() + ")");
		endpointImg2.setVisible(true);
	}

	public void render(ShapeRenderer renderer) {
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 5; y++) {
				cellGrid[x][y].renderShape(renderer);
			}
		}
		//TODO: render the walls
	}

	//At the start, an introduction of the cells will be given onstage
	public void displayCellsOnField(float tweenDuration, float tweenDelay) {
		String key = "";
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 5; y++) {
				key = "";
				key += Integer.toString(x);
				key += Integer.toString(y);
				Cell cell = (Cell)super.findActor(key);
				cell.addAction(sequence(sizeTo(88.0f, 88.0f, tweenDuration, Interpolation.linear), new Action() {
					public boolean act(float deltaTime) {
						Cell cell = (Cell)this.getActor();
						//cell.setOrigin(MeshActor.transformOrigin.LOWER_LEFT);
						cell.setOrigin(Align.bottomLeft);
						cell.setTouchable(Touchable.enabled);
						return true;
					}
				}));
			}
		}
	}
	
	public void isDragging() {
		isDragged = true;
	}
	
	public void isNotDragging() {
		isDragged = false;
	}
	
	public boolean getIsDragged() {
		return isDragged;
	}
	
	protected void setCurrentCell(Cell cell) {
		this.currentCell = cell;
	}
	
	public Cell getCurrentMeshActor() {
		return this.currentCell;
	}
	
	public Selection getSelection() {
		return selection;
	}
	
	/**
	 * float durationInSeconds: the amount of time the GameService has granted the
	 * selection to perform its submission Action
	 */
	private void performSubmission() {
		Cell cell;
		Vial vial = selection.generateVial();
		//List<SequenceAction> cellAnimations = retrieveActions(selection.getSelectionArea());
		for(int i = 0; i < selection.size; i++) {
			cell = selection.get(i);
			cell.notifyOfSubmission(i);
			if(i == (selection.size - 1)) {
				//notifyObservers(PlayfieldUpdateType.SUBMISSION, vial); this is made redundant by the Phase change

			}
		}
		GameService.INSTANCE.changeToSubmissionPhase(vial, selectionPhaseDuration);
		//choreographSubmission(); SAVING FOR LATER
		selection.clear(Selection.removalCondition.SUBMISSION);
		//removeEndpoints();
		//assignEndpoints();
	}

	@SuppressWarnings("unused")
	private void choreographSubmission(){
		
		//TreeMap<String, SequenceAction> choreography = new TreeMap<String, SequenceAction>();
		
		Array<GridCoordinate> gcs = new Array<GridCoordinate>();
		gcs = selection.getSelectionArea();
	//	Astar.INSTANCE.search(gcs);
		//for all cells in selection (except the last) calculate the best traversal
		//then use that to create a series of actions to tween the each cell
		//from its origin to the location of the selection
		
		for(int i = 0; i < gcs.size; i++) { //for each cell in the selection but the last one
			Array<GridCoordinate> itinerary = CellAnimationPath.INSTANCE.getItinerary(gcs.get(i));
			SequenceAction cellsAction = sequence();
			int k = 0;
			int m = 0; //used to track the number of times we go in one direction
			direction previousDirection = null; //if this direction does not equal the other direction below
			direction currentDirection = null; //that means we're turning and a new Action must be added
			float timePerStep = TIME_OF_TRAVERSAL / (float)itinerary.size;
			while(k < itinerary.size - 1) {
				GridCoordinate gc1 = itinerary.get(k);
				GridCoordinate gc2 = itinerary.get(k+1);
				if(gc2.x - gc1.x == 0) { //going either north or south
					if(gc2.y - gc1.y == 1) { //indicates we're going north
						currentDirection = direction.NORTH;
					} else {
						currentDirection = direction.SOUTH;
					}
				} else if (gc2.x - gc1.x == 1) { //going east
					currentDirection = direction.EAST;
				} else { //lastly, heading west
					currentDirection = direction.WEST;
				}
				
				if(previousDirection == null && itinerary.size > 2) { //if this is the first iteration of the loop
					m = 1;
				} else if (previousDirection == currentDirection || itinerary.size == 2) {
					if(k == itinerary.size - 2) { //if this is the last section
						//run the shit!
						GridCoordinate start = itinerary.get(k - m);
						GridCoordinate end = itinerary.get(k+1);
						float startX = (float)96*start.getX();
						float startY = (float)96*start.getY();
						if(m == 0) {
							m += 1;
						}
						switch(currentDirection) {
						case NORTH:
						case SOUTH:
							System.out.println("K = " + k + " Going NORTH/SOUTH: startX: " + startX + " endY: " + ((float)(96*end.getY())));
							cellsAction.addAction(moveTo(startX, (float)(96*end.getY()), timePerStep * m));
							m = 0;
						break;
						
						case EAST:
						case WEST:
							cellsAction.addAction(moveTo((float)(96*end.getX()), startY, timePerStep * m));
							m = 0;
						break;
						}
						
					} else {
						m += 1;
					}
				} else if (previousDirection != currentDirection) { //ok, we have to add an action to cellsAction
					GridCoordinate start = itinerary.get(k - m);
					GridCoordinate end = itinerary.get(k+1);
					float startX = (float)(96 * start.getX());
					float startY = (float)(96 * start.getY());
					switch(previousDirection) {
					case NORTH:
					case SOUTH:
						cellsAction.addAction(moveTo(startX, (float)(96*end.getY()), timePerStep * m));
						m = 0;
					break;
					
					case EAST:
					case WEST:
						cellsAction.addAction(moveTo((float)(96*end.getX()), startY, timePerStep * m));
						m = 0;
					break;
					}
				}
				previousDirection = currentDirection;
				k++;
			}
			cellsAction.addAction(new Action() {
				public boolean act(float delta) {
					
					return true;
				}
			});
			this.findActor(gcs.get(i).toString()).addAction(parallel(cellsAction, 
					sizeTo(88.0f, 88.0f, timePerStep * m)));
		}
	}

	public void registerObserver(PlayfieldObserver po) {
		observers.add(po);
		
	}

	public void removeObserver(PlayfieldObserver po) {
		// TODO Auto-generated method stub
		int i = observers.indexOf(po);
		if (i >= 0) {
			observers.remove(i);
		}
	}

	public void notifyObservers(Playfield.PlayfieldUpdateType updateType, Object object) {
		// TODO Auto-generated method stub
		for (int i = 0; i < observers.size(); i++) {
			PlayfieldObserver po = observers.get(i);
			po.update(updateType, object);
		}
	}
	
	public void pauseGame() {
		this.setTouchable(Touchable.disabled);
		isPaused = true;
	}
	
	public void resumeGame() {
		this.setTouchable(Touchable.enabled);
		isPaused = false;
		//UPDATE: if a selection has currently been made, we need to reset it
		if(selection.size > 0) {
			selection.clear(Selection.removalCondition.DESELECTION);
		}
	}
	
	@Override
	public void act(float delta) {
		if(!isPaused) {
			super.act(delta);
			this.selectionPhaseDuration += delta;
		}
	}
	
	public void resetGame() {
		//wallCache.resetWalls();
		//pickANewWall();
		selection.reset();
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 5; y++) {
				Cell cell = (Cell)this.findActor("" + x + y);
				cell.reset();
			}
		}
		removeEndpoints();
		assignEndpoints();

	}
	
	public void levelUp(int level, Spectrum newSpectrum) {
		currentWall = wallCache.retrieveWall(level);
		GridCoordinate gc = new GridCoordinate();
		for(int i = 8; i >= 0; i--) {
			for(int j = 0; j < 9; j++) {
				gc.x = i;
				gc.y = j;
				if(i%2 == 1 || j%2 == 1) {
					if(currentWall.coordinateIsWall(i, j)) {
						walls.get(gc.toString()).setVisible(true);
					} else {
						walls.get(gc.toString()).setVisible(false);
					}
				}
			}
		}
		for(String key : walls.keySet()) {
			MeshActor mActor = walls.get(key);
			mActor.setColor(Color.RED);
		}
		//Now, change all Cells
	}
	
	public Color getSpectrumColor(int index) {
		return GameService.INSTANCE.getSpectrumColor(index);
	}
	
	private void genRandomEndpoint(GridCoordinate gc) {
		gc.x = (int)(Math.random() * 4);
		gc.y = (int)(Math.random() * 4);
	}
	

	
	public void removeEndpoints() {
		Cell endpoint1 = ((Cell)(this.findActor(endpoints.get(0).toString())));
		endpoint1.setEndpoint(false);
		endpointImg1.setColor(Color.WHITE); //I cannot determine if this is the default color for an Image object...
		endpointImg1.setVisible(false);
		
		Cell endpoint2 = ((Cell)(this.findActor(endpoints.get(1).toString())));
		endpoint2.setEndpoint(false);
		endpointImg2.setColor(Color.WHITE); //if it is not, I will make the appropriate changes.
		endpointImg2.setVisible(false);
	}
	
}