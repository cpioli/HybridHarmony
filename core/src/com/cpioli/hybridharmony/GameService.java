package com.cpioli.hybridharmony;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeTo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.cpioli.hybridharmony.performance.SelectionPhaseStatistic;
import com.cpioli.hybridharmony.performance.SelectionPhaseStatisticArray;
import com.cpioli.hybridharmony.playfield.Cell;
import com.cpioli.hybridharmony.playfield.MeshActor;
import com.cpioli.hybridharmony.playfield.Playfield;
import com.cpioli.hybridharmony.playfield.Playfield.PlayfieldUpdateType;
import com.cpioli.hybridharmony.playfield.PlayfieldObserver;
import com.cpioli.hybridharmony.playfield.Selection;
import com.cpioli.hybridharmony.spectrum.GrayscaleSpectrum;
import com.cpioli.hybridharmony.spectrum.Spectrum;
import com.cpioli.hybridharmony.statistics.PolarityObserver;
import com.cpioli.hybridharmony.statistics.Statistics;
import com.cpioli.hybridharmony.statistics.Vial;

/**
 * Centralization of lots of data occurs here. Responsible for handling only
 * the levelup, for now. It will handle more responsibilities as the game
 * is developed.
 * @author cpioli
 *
 *
 *Update 9/
 */

public enum GameService implements PlayfieldObserver, PolarityObserver {
	
	INSTANCE;

	private int level;
	private boolean levelUp;
	private int vialsSubmitted;
	private int levelRequirements[] = {10, 20, 30, 45, 60, 75, 95, 115, 135, 155 };
	private boolean initialized;
	private boolean poleIsLeft;
	private Playfield playfield; //having difficulty accessing playfield/statistics objects
	private Statistics statistics; //maybe this will work better
	private static Spectrum currentSpectrum;
	private static Spectrum previousSpectrum;
	private static GrayscaleSpectrum grayscaleSpectrum;
	
	private static enum Phase {
		selection,
		submission
	};
	
	private Phase phase;
	
	GameService() {
		initialized = false;
		poleIsLeft = true;
	}
	
	public void initialize() {
		if(!initialized) {
			//this.playfield = playfield;
			//playfield.registerObserver(INSTANCE);
			GameService.INSTANCE.grayscaleSpectrum = new GrayscaleSpectrum();
			GameService.INSTANCE.currentSpectrum = grayscaleSpectrum;
			GameService.INSTANCE.previousSpectrum = null;
			GameService.INSTANCE.level = 1;
			GameService.INSTANCE.vialsSubmitted = 0;
			GameService.INSTANCE.initialized = true;
			GameService.INSTANCE.phase = Phase.selection;
			GameService.INSTANCE.levelUp = false;
		}
	}
	
	public void addPlayfield(Playfield playfield) {
		if(GameService.INSTANCE.playfield == null) {
			GameService.INSTANCE.playfield = playfield;
		}
	}
	
	public void addStatistics(Statistics statistics) {
		if(GameService.INSTANCE.statistics == null) {
			GameService.INSTANCE.statistics = statistics;
		}
	}
	
	public void levelUp() {
		level++;
		System.out.println("Moving onto level " + level);
		/*this block of conditions is to be saved for later
		 * if(currentSpectrum instanceof GrayscaleSpectrum) {
		 
			currentSpectrum = redBlueSpectrum;
			previousSpectrum = grayscaleSpectrum;
		} else if(currentSpectrum instanceof RedBlueSpectrum) {
			currentSpectrum = grayscaleSpectrum;
			previousSpectrum = redBlueSpectrum;
		}*/
		playfield.levelUp(level, currentSpectrum);
		statistics.levelUp();
		//statistics.levelUp();
	}

	@Override
	public void update(PlayfieldUpdateType updateType, Object object) {
		// TODO Auto-generated method stub
		if(updateType == PlayfieldUpdateType.SUBMISSION) {
			vialsSubmitted++;
			if(vialsSubmitted == levelRequirements[level - 1]) {
				levelUp();
			}
		}
	}
	
	//============
	//Placing all spectrum based methods HERE
	//============
	
	
	public Color getSpectrumColor(int index) {
		return currentSpectrum.getSpectrumColor(index);
	}
	
	public Color getLeftColor() {
		return currentSpectrum.getLeftColor();
	}
	
	public Color getRightColor() {
		return currentSpectrum.getRightColor();
	}
	
	//added 5-25-13
	//use this to calculate the destination color value of the tweening Actor
	//public Color calculateSpectrumColor(float value255) {
	//	return currentSpectrum.calculateSpectrumColor(value255);
	//}
	
	//added 5-25-13
	//Use this to calculate the current color value of the tweening Actor during
	//spectrum swap
	//public Color calculateSpectrumColor(Color color) {
		
	//}
	//============
	//End placement of spectrum based methods
	//============
	
	public int randomGen() {
		int output = 4;
		while(output == 4) {
			output = (int)Math.floor(Math.random()*8);
		}
		return output;
	}
	
	/**
	 * Runs a probability equation to generate an int 0-8 that determines the new
	 * cell being added into the playfield
	 * 
	 * NOTE: to be used later (as of 5/24/2013)
	 * @return
	 */
	public int generateNewCell() {
		//double colorDeterminer = Math.random() * 11.167;
		double colorDeterminer = Math.random() * 14.375;
		System.out.print("Number generated: ");
		//Implementing new series of probability listed in an excel document.
		
		if(!poleIsLeft) {
			if(colorDeterminer <= 3.5) {
				System.out.println("0");
				return 0;
			} else if(colorDeterminer > 3.5 && colorDeterminer <= 7.5) {
				System.out.println("1");
				return 1;
			} else if(colorDeterminer > 7.5 && colorDeterminer <= 10.5) {
				System.out.println("2");
				return 2;
			} else if(colorDeterminer > 10.5 && colorDeterminer <= 12.5) {
				System.out.println("3");
				return 3;
			} else if(colorDeterminer > 12.5 && colorDeterminer <= 13.5) {
				System.out.println("4");
				return 4;
			} else if(colorDeterminer > 13.5 && colorDeterminer <= 14.0) {
				System.out.println("5");
				return 5;
			} else if(colorDeterminer > 14.0 && colorDeterminer <= 14.25) {
				System.out.println("6");
				return 6;
			} else if(colorDeterminer > 14.25 && colorDeterminer <= 14.375) {
				System.out.println("7");
				return 7;
			}
		} else {
			if(colorDeterminer <= 3.5) {
				System.out.println("7");
				return 7;
			} else if(colorDeterminer > 3.5 && colorDeterminer <= 7.5) {
				System.out.println("6");
				return 6;
			} else if(colorDeterminer > 7.5 && colorDeterminer <= 10.5) {
				System.out.println("5");
				return 5;
			} else if(colorDeterminer > 10.5 && colorDeterminer <= 12.5) {
				System.out.println("4");
				return 4;
			} else if(colorDeterminer > 12.5 && colorDeterminer <= 13.5) {
				System.out.println("3");
				return 3;
			} else if(colorDeterminer > 13.5 && colorDeterminer <= 14.0) {
				System.out.println("2");
				return 2;
			} else if(colorDeterminer > 14.0 && colorDeterminer <= 14.25) {
				System.out.println("1");
				return 1;
			} else if(colorDeterminer > 14.25 && colorDeterminer <= 14.375) {
				System.out.println("0");
				return 0;
			}
		}
		
		
		return 0;
	}
	
	public int getLevel() {
		return level;
	}

	@Override
	public void update(boolean polarity) {
		this.poleIsLeft = polarity;
		
	}
	
	public void changeToSubmissionPhase(Vial vial, float time) {
		if(GameService.INSTANCE.phase == Phase.selection) { //this is permitted
			System.out.println("Changing to the submission phase.");
			GameService.INSTANCE.vialsSubmitted++;
			if(vialsSubmitted == levelRequirements[level - 1]) {
				GameService.INSTANCE.levelUp = true;
			}
			//sps = new SelectionPhaseStatistic()
			GameService.INSTANCE.statistics.pauseTimers();
			GameService.INSTANCE.playfield.setTouchable(Touchable.disabled);
			GameService.INSTANCE.playfield.removeEndpoints();
			GameService.INSTANCE.phase = Phase.submission;
			GameService.INSTANCE.statistics.animateStatisticsUpdate(vial);

		}
	}
	
	public void changeToSelectionPhase() {
		if(GameService.INSTANCE.phase == Phase.submission) {
			System.out.println("Changing to the selection phase.");
			if(GameService.INSTANCE.levelUp) {
				GameService.INSTANCE.levelUp(); //this will eventually become an aniamtion action
				GameService.INSTANCE.levelUp = false;
			}
			GameService.INSTANCE.playfield.assignEndpoints();
			GameService.INSTANCE.playfield.setTouchable(Touchable.enabled);
			GameService.INSTANCE.statistics.resumeTimers();
			GameService.INSTANCE.phase = Phase.selection;
		}
	}
	
	private final float VIAL_DROPPING_SPEED = 0.5f;
	/**
	 * NOTES on the submissions:
	 * The submission phase takes X seconds in Level 1, and that time is
	 * reduced each level.
	 * 
	 * This method manages time between Playfield and Statistics for the Submission
	 * Phase.
	 * 
	 * The goal is to have this called at the end of changeToSubmissionPhase,
	 * then at the end of choreographSubmission we'll call changeToSelectionPhase
	 * 
	 */
	private void choreographSubmission(float timeInSeconds, final Vial vial) {
		
		Selection selection = playfield.getSelection();
		int size = selection.size;
		float submissionDuration = timeInSeconds / 2;
		float cellTime = submissionDuration / size;
		int counter = 0;
		
		//PHASE 1: animate all cells entering the SubmittedVial widget
		for(Cell cell: selection) {
			cell.setTouchable(Touchable.disabled);
			cell.addAction(delay(
							(0.15f * counter), 
							(Action)sequence(
										parallel(
											moveTo(600.0f, 400.0f, cellTime),
											sizeTo(15.0f, 15.0f, cellTime)
												), 
										delay(submissionDuration),
										new Action() {
											public boolean act(float delta) {
												Cell cell = (Cell)this.getActor();
												cell.setSize(18.0f, 18.0f);
												//cell.setOrigin(MeshActor.transformOrigin.CENTER);
												cell.setOrigin(Align.center);
												float xloc = (float)(96*cell.getGC().getX());
												float yloc = (float)(96*cell.getGC().getY());
												//cell.setXY(xloc, yloc); //move cells to the lower-left hand side of their location on the Playfield
												cell.setOrigin(xloc, yloc);
												cell.setColorValue(GameService.INSTANCE.generateNewCell());
												cell.setColor(Color.WHITE); //same as color of cells in queue
												cell.addAction(sequence(
																	moveBy(35.0f, 35.0f, VIAL_DROPPING_SPEED, Interpolation.exp5Out),
																	parallel(
																			color(((Playfield)cell.getParent()).getSpectrumColor(cell.getColorValue()), VIAL_DROPPING_SPEED, Interpolation.exp5Out),
																			sizeTo(cell.getOriginalDimension(), cell.getOriginalDimension(), VIAL_DROPPING_SPEED, Interpolation.exp5Out)
																			), //end parallel action
																	new Action() {
																		public boolean act(float delta) {
																			Cell cell = (Cell)this.getActor();
																			cell.setTouchable(Touchable.enabled);
																			cell.setPosition(96.0f * cell.getGC().getX(), 96.0f * cell.getGC().getY());
																			return true;
																		}
																	} //end creation of new action
																) //end sequence action
															); //end addAction command
												return true;
											} //end new Action method act()
										} //end new Action
									) //end sequence Action
									) //end delay Action
								); //end cell.addAction() command
		}//end loop to animate cells in selection
		
		//PHASE 2: animate the chain of objects being altered in the statistics group
		//right now, four elements are being altered in the submission phase
		//The submitted vial, the cqg, the speedometer, and the score
		//We're to grab each of the objects from the Statistics object and call them
		//to make their updates, injecting them with our own time delays.
		//this requires modification of the objects' tweening methods to handle external
		//time parameters.
		
		float statChangeDuration = submissionDuration;
		float widgetDuration = statChangeDuration/4;
		float widgetDelay = widgetDuration / 4;
		float widgetTweenDuration = 3 * widgetDelay;

		/*statistics.addAction(sequence(
				delay(widgetDelay,
						new Action() {
							public boolean act(float delta) {
								SubmittedVial submittedVial = statistics.getSubmittedVial();
								submittedVial.addVial(vial);
								return true;
							}
						}
				),
				delay((widgetDelay + widgetTweenDuration),
						new Action() {
							public boolean act(float delta) {
								
							}
				})
				)
			)*/
	}
}