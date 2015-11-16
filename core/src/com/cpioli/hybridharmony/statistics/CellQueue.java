package com.cpioli.hybridharmony.statistics;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.cpioli.hybridharmony.Assets;
import com.cpioli.hybridharmony.playfield.MeshActor;

/**
 * must implement the game over subject in this class
 * Update (June 6, 2013): Implement the animations for this class.
 * @author cpioli
 *
 */
public class CellQueue extends Group implements GameOverSubject {
	
	private ArrayBlockingQueue<MeshActor> onScreenCells;
	private ArrayBlockingQueue<MeshActor> offScreenCells;
	
	Image queueVisual;
	MeshActor vizCellsInQueue[];
	int amtCellsInQueue;
	private final float cellDimension = 18.0f;
	private final Color cellColor = Color.WHITE;
	
	private float rateOfCellIncrease;
	private float currentTimeElapsed;
	
	private boolean timerPaused;
	private boolean gameOver;
	//private final float x_location = 337.0f;
	
	private ArrayList<GameOverObserver> observers;
	
	public CellQueue() {
		queueVisual = Assets.bigQueueContainer;
		queueVisual.setX(0.0f);
		queueVisual.setY(0.0f);
		
		vizCellsInQueue = new MeshActor[25];
		
		rateOfCellIncrease = 2.0f;
		currentTimeElapsed = 0.0f;
		observers = new ArrayList<GameOverObserver>();
		//initialization of static meshActors
		for(int i = 0; i < 25; i++) {
			vizCellsInQueue[i] = new MeshActor(337.0f, 6.0f + 20.0f*i, cellDimension, cellDimension, cellColor, "");
			this.addActor(vizCellsInQueue[i]);
			vizCellsInQueue[i].setVisible(false);
		}
		
		//initialization of animated mesh actors
		onScreenCells = new ArrayBlockingQueue<MeshActor>(25);
		offScreenCells = new ArrayBlockingQueue<MeshActor>(25);
		
		for(int i = 0; i < 25; i++) {
			MeshActor mActor = new MeshActor(this.getX() + 7.0f, this.getY() + 540.0f, cellDimension, cellDimension, cellColor, "");
			this.addActor(mActor);
			offScreenCells.add(mActor);
		}
		this.addActor(queueVisual);
		amtCellsInQueue = 0;
		timerPaused = false;
		gameOver = false;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(timerPaused) {
			//System.out.println("CurrentTimeElapsed: " + currentTimeElapsed);
		} else {
			currentTimeElapsed += delta;
		}
		

		
		if(currentTimeElapsed >= rateOfCellIncrease) { //if enough time has elpased
			
			if(amtCellsInQueue == 24) { //we're about to add the 25th, so it's game over
				//notifyObservers(); notification will occur once the final block has fallen into the queue
				System.out.println("It'll be game over soon!");
				gameOver = true;
			}
			currentTimeElapsed -= rateOfCellIncrease;
			addCellToQueue(0.75f);
			//amtCellsInQueue++;							//increase the number of cells in the queue
			//vizCellsInQueue[ amtCellsInQueue - 1].setVisible(true); //visually show the new cell in the queue
		}

	}
	
	//if a submission occurs, this class receives a note about it and removes the appropriate number of cells.
	public void update(int size) {
		amtCellsInQueue = amtCellsInQueue > size? amtCellsInQueue - size : 0;
		if(amtCellsInQueue == 0) {
			//for(int i = 0; i < 25; i++) {
			//	vizCellsInQueue[i].setVisible(false);
			//}
			popCellsFromQueue(onScreenCells.size());
		} else { //if we've removed a number of cells
			//for(int i = 24; i > amtCellsInQueue - 1; i--) {
			//	vizCellsInQueue[i].setVisible(false);
			//}
			popCellsFromQueue(size);
		}
	}

	/*
	 * Location of animation coordinating methods
	 */
	
	/*
	 * SpeedFactor is a multiplier indicating how fast we should speed the drop
	 * relative to gravity. For example, a multiplier of 1.0f is 9.8m/s^2
	 * 
	 * Every action added here needs to be a sequence incase we need to add further actions afterwards.
	 */
	private void addCellToQueue(float speedFactor) {
		MeshActor cellToAdd = offScreenCells.remove();
		cellToAdd.setVisible(true);
		float y_destination = onScreenCells.size() * 20.0f + 6.0f;
		double time = Math.sqrt(2.0f * ((cellToAdd.getY() - y_destination)/(810 * speedFactor)) / 9.8f);
		cellToAdd.addAction(sequence(moveTo(cellToAdd.getX(), y_destination, (float)time, Interpolation.pow2In)));
		onScreenCells.add(cellToAdd);
		
		if(gameOver) { //contains the value of true iff the amtofcellsinqueue is 25 or more
			System.out.println("It's a game over, we're about to add another action...");
			SequenceAction action = (SequenceAction)cellToAdd.getActions().get(0);
			action.addAction(new Action() {
				public boolean act(float delta) {
					CellQueue cellQueue = (CellQueue)this.getActor().getParent();
					cellQueue.notifyObservers();
					System.out.println("We're adding the game over action to notify our observers!");
					return true;
				}
			});
		}
		amtCellsInQueue++;
	}
	
	private void popCellsFromQueue(int amount) { //this is going to pop a number of cells
//TODO: compute time traveled using velocity and distance traveled
		float delay = .30f;
		float time = 0.5f;
		float velocity =540.0f; //540 pixels per second
		float distance;
		int i = 0;
		for(MeshActor cell : onScreenCells) {
			if(cell.getActions().size == 0) { //if it's inactive
				if(i < amount) { //this cell is going to disappear
					distance = cell.getY() + cell.getHeight() * 2.0f;
					time = distance / velocity;
					cell.addAction(sequence(moveTo(cell.getX(), 0.0f - cell.getHeight() * 2.0f, time, Interpolation.linear), new Action() {
						public boolean act(float delta) {
							MeshActor mActor = (MeshActor)this.getActor();
							mActor.setVisible(false);
							mActor.setY(545.0f);
							return true;
						}
					}));
					System.out.println("Cell " + i + " time: " + time);
				} else {
					distance = 20.0f * amount;
					time = distance / velocity;
					cell.addAction(sequence(moveTo(cell.getX(), cell.getY() - 20.0f * amount, 0.5f, Interpolation.exp10In)));
				}
			} else if (cell.getActions().size == 1) {//otherwise, if the cell is falling into the queue
				if(cell.getActions().get(0) instanceof MoveToAction) { //moveTo actions are no longer used, and this block of code is never used either
					MoveToAction mtAction = (MoveToAction)cell.getActions().get(0);
					if(i < amount) {
						mtAction.setY(0.0f - cell.getHeight() - 5.0f);
					} else {
						mtAction.setY(mtAction.getY() - 20.0f * amount); //update the destination of the cell
					}
				} else if (cell.getActions().get(0) instanceof SequenceAction) {
					SequenceAction sAction = (SequenceAction)cell.getActions().get(0);
					if(i < amount) { //we'll be adding a command to move this element back to the top
						distance = cell.getY() + cell.getHeight() * 2.0f;
						time = distance / velocity;
						sAction.addAction(moveTo(cell.getX(), 0.0f - cell.getHeight() * 2.0f, time, Interpolation.linear));
						sAction.addAction(
							new Action() {
							public boolean act(float delta) {
								MeshActor mActor = (MeshActor)this.getActor();
								mActor.setVisible(false);
								mActor.setY(545.0f);
								return true;
							}
						});
					} else {
						Array<Action> cellsActions = sAction.getActions();
						MoveToAction mtAction = (MoveToAction)cellsActions.get(0);
						float y = mtAction.getY();
						if(y > (onScreenCells.size() - amount) * 20.0f + 6.0f) { //if this needs to be pulled down farther
							mtAction.setY(y - amount * 20.0f);
						}
					}
				}
			}
			i++;
		}
		
		for(int j = 0; j < amount; j++) {
			MeshActor mActor = onScreenCells.remove();

			//mActor.clearActions();
			try {
				offScreenCells.put(mActor);
			} catch (InterruptedException e) {
				System.out.println("Moving cell " + j + " from the queue into reserves failed");
				e.printStackTrace();
			}
		}
		/* This will be an advanced algorithm to use later
		 * MeshActor cellToRemove = onScreenCells.remove();
		 * float y_destination = 0.0f - cellToRemove.getHeight() - 5.0f;
		 * cellToRemove.addAction(moveTo(this.getX() + 7.0f, y_destination, 0.15f, Interpolation.exp5));
		 * for(int i = amount; i >= 0; i--) {
			cellToRemove = onScreenCells.peek();
			y_destination = 0.0f - cellToRemove.getHeight() - 5.0f;
			cellToRemove.addAction(sequence(delay(0.3f), moveTo(this.getX() + 7.0f, y_destination, 0.15f, Interpolation.exp5)));
			onScreenCells.remove();
			for(MeshActor cell : onScreenCells) { //for the remaining cells on screen, create or modify sequence/moveTo Actions
				if(cell.getActions().size == 1) {//there should only be 1 action in the array or no action at all
					if(cell.getActions().get(0) instanceof SequenceAction) {
						SequenceAction sAction = (SequenceAction)cell.getActions().get(0);
						sAction.addAction(delay(0.3f));
						sAction.addAction(moveTo(this.getX() + 7.0f, cell.getY() - 20.0f, 0.15f, Interpolation.exp5));
					} else if (cell.getActions().get(0) instanceof MoveToAction) {
						MoveToAction action = (MoveToAction)cell.getActions().get(0);
						float new_y = action.getY() - 20.0f;
						action.setY(new_y); //adjusts currently falling cell to continue falling for an additional 20.0f points
					}
				} else { //this meshActor isn't currently dropping, so we'll just have him drop again
					cell.addAction(sequence(delay(0.3f), moveTo(this.getX() + 7.0f, cell.getY() - 20.0f, 0.25f, Interpolation.exp5)));
				}
			}
			offScreenCells.add(cellToRemove);
			amtCellsInQueue--;
		}
		*/
		
		
		
		
	}
	
	public void registerObserver(GameOverObserver goo) {
		observers.add(goo);
		
	}

	public void removeObserver(GameOverObserver goo) {
		int i = observers.indexOf(goo);
		if(i >= 0) {
			observers.remove(i);
		}
		
	}

	public void notifyObservers() {
		for(int i = 0; i < observers.size(); i++) {
			GameOverObserver goo = observers.get(i);
			goo.declareGameOver();
		}
	}
	
	protected void updateSpeed(float speed) {
		rateOfCellIncrease = speed;
		if(currentTimeElapsed >= rateOfCellIncrease) {
			currentTimeElapsed = currentTimeElapsed % rateOfCellIncrease; //this way, they have a bit of time before the next cell is added
			/* Do NOT add any cells in the queue if this is the case. A player
			 * shouldn't see a new cell added simply because he submitted a poor
			 * vial.
			 * amtCellsInQueue++;
			 * vizCellsInQueue[ amtCellsInQueue - 1 ].setVisible(true);
			*/
		}
		if(amtCellsInQueue == 25) { //game over homeboy!
			notifyObservers();
		}
	}
	
	protected void reset() {
		amtCellsInQueue = 0;
		for(int i = 0; i < 25; i++) {
			vizCellsInQueue[i].setVisible(false);
		}
		
		int amount = onScreenCells.size();
		for(int i = 0; i < amount; i++) {
			MeshActor mActor = onScreenCells.remove();
			mActor.setY(545.0f);
			try {
				offScreenCells.put(mActor);
			} catch (InterruptedException e) {
				System.out.println("Moving cell " + i + " from the queue into reserves failed");
				e.printStackTrace();
			}
		}
		currentTimeElapsed = 0.0f;
		rateOfCellIncrease = 2.0f;
		timerPaused = false;
		gameOver = false;
	}
	
	protected void pauseTimer() {
		timerPaused = true;
	}
	
	protected void resumeTimer() {
		timerPaused = false;
	}
}