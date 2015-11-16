package com.cpioli.hybridharmony.statistics;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.cpioli.hybridharmony.Assets;
import com.cpioli.hybridharmony.GameService;
import com.cpioli.hybridharmony.playfield.MeshActor;
import com.cpioli.hybridharmony.playfield.Playfield;
import com.cpioli.hybridharmony.playfield.Playfield.PlayfieldUpdateType;
import com.cpioli.hybridharmony.playfield.PlayfieldObserver;
import com.cpioli.hybridharmony.playfield.Selection;
import com.cpioli.hybridharmony.playfield.SelectionObserver;

public class Statistics extends Group implements PlayfieldObserver, SelectionObserver {
	
	protected ContentsQualityGauge contentsQualityGauge;
	protected CellQueue queue;
	protected Speedometer speedometer;
	protected Score score;
	protected Polarity polarity;
	private boolean isPaused;
	private SubmittedVial submittedVial;
	
	private Label levelLabel;
	private StringBuffer levelString;
	private int level;
	
	private int animationStep;
	private ArrayList<String> groupNames;
	
	
	public Statistics(Selection selection, Playfield playfield) {
		groupNames = new ArrayList<String>();
		this.addActor(new MeshActor(0.0f, 0.0f,410.0f, 520.0f, Color.DARK_GRAY, "statsbackground"));
		playfield.registerObserver(this);
		selection.registerObserver(this);
		contentsQualityGauge = new ContentsQualityGauge(selection);
		contentsQualityGauge.setBounds(48.5f, 289.0f, 311.0f, 50.0f);
		queue = new CellQueue();
		queue.setBounds(10.0f, 0.0f, 32.0f, 520.0f);
		speedometer = new Speedometer(queue);
		speedometer.setBounds(52.0f, 115.0f, 60.0f, 60.0f);
		score = new Score();
		score.setBounds(52.0f, 10.0f, 90.0f, 120.0f);
		polarity = new Polarity(GameService.INSTANCE.getLeftColor(), GameService.INSTANCE.getRightColor());
		polarity.setBounds(52.0f, 165.0f, 60.0f, 60.0f);
		polarity.registerObserver(GameService.INSTANCE);
		
		level = 1;
		levelString = new StringBuffer();
		levelString.delete(0, levelString.length());
		levelString.append("LEVEL ").append(Integer.toString(level));
		levelLabel = Assets.bigLevelTitleAndValue;
		levelLabel.setAlignment(Align.left);
		levelLabel.setText(levelString);
		levelLabel.setPosition(180.0f, 450.0f);

		setSubmittedVial(new SubmittedVial(140.0f, 460.0f, 40.0f));
		getSubmittedVial().registerObserver(speedometer);
		getSubmittedVial().registerObserver(contentsQualityGauge);
		
		
		
		
		contentsQualityGauge.setName("QualityGauge");
		this.addActor(contentsQualityGauge);
		groupNames.add("QualityGauge");
		
		getSubmittedVial().setName("Vial");
		this.addActor(getSubmittedVial());
		groupNames.add("Vial");
		
		queue.setName("Queue");
		this.addActor(queue);
		groupNames.add("Queue");
		
		speedometer.setName("Speedometer");
		this.addActor(speedometer);
		groupNames.add("Speedometer");
		
		score.setName("Score");
		this.addActor(score);
		groupNames.add("Score");
		
		//to be removed, eventually
		this.addActor(polarity);
		
		this.addActor(levelLabel);

		isPaused = false;
		animationStep = 0;
	}

	/**
	 * This method choreographs the changes to be made to the statistics after
	 * a selection is submitted.
	 */
	public void animateStatisticsUpdate(final Vial vial) {
		this.addAction(sequence(
			delay(2.0f),
			new Action() {
				public boolean act(float delta) {
					SubmittedVial submittedVial = ((Statistics)this.getActor()).getSubmittedVial();
					submittedVial.addVial(vial);
					return true;
				}
			},
			delay(1.0f), 
			new Action() {
				public boolean act(float delta) {
					ContentsQualityGauge cqg = ((Statistics)this.getActor()).contentsQualityGauge;
					cqg.update(vial.getCumulativeColor() / vial.getSize());
					return true;
				}
			},
			delay(1.0f),
			new Action() {
				public boolean act(float delta) {
					Speedometer speedometer = ((Statistics)this.getActor()).speedometer;
					speedometer.update(vial.getCumulativeColor() / vial.getSize());
					return true;
				}
			},
			delay(1.0f),
			new Action() {
				public boolean act(float delta) {
					CellQueue cellQueue = ((Statistics)this.getActor()).queue;
					cellQueue.update(vial.getSize());
					return true;
				}
			},
			delay(1.0f),
			new Action() {
				public boolean act(float delta) {
					Score score = ((Statistics)this.getActor()).score;
					score.update(vial.getSize());
					return true;
				}
			},
			delay(1.0f),
			new Action() {
				public boolean act(float delta) {
					GameService.INSTANCE.changeToSelectionPhase();
					return true;
				}
			}
			));
	}
	
	public void update(PlayfieldUpdateType updateType, Object object) {
		// TODO Auto-generated method stub
		switch(updateType) {
		case SUBMISSION:
			//Beaker beaker = (Beaker)this.findActor("beaker");
			//beaker.addVial((Vial)object); //to update the contents of the beaker
			getSubmittedVial().addVial((Vial)object);
			queue.update(((Vial)object).getSize()); //to tell the queue how many cells must be removed
			score.update(((Vial)object).getSize()); //to update the score
			break;
		}
	}

	//this method notifies several statistics Widgets of changes
	//including the bonus detector, spedometer, score, and contents quality.
	public void update(int numberOfCells, float cumulativeColor) {
		//right now, all that needs to be updated is the Contents Quality Gauge
		contentsQualityGauge.update(numberOfCells, cumulativeColor);
	}
	
	public void initializeGameOverCondition(GameOverObserver goo) {
		queue.registerObserver(goo);
	}
	
	@Override
	public void act(float delta) {
		if(!isPaused) {
			super.act(delta);
		}
	}
	
	public void pauseGame() {
		isPaused = true;
	}
	
	public void resumeGame() {
		isPaused = false;
	}
	
	public void resetGame() {
		contentsQualityGauge.reset();
		getSubmittedVial().reset();
		queue.reset();
		speedometer.reset();
		score.reset();
		polarity.reset();
		level = 1;
		levelString.delete(0, levelString.length());
		levelString.append("LEVEL ").append(Integer.toString(level));
		levelLabel.setText(levelString);
	}
	
	public void pauseTimers() {
		score.pauseBonus();
		queue.pauseTimer();
		polarity.pauseTimer();
	}
	
	public void resumeTimers() {
		score.resumeBonus();
		queue.resumeTimer();
		polarity.resumeTimer();
	}

	public SubmittedVial getSubmittedVial() {
		return submittedVial;
	}

	public void setSubmittedVial(SubmittedVial submittedVial) {
		this.submittedVial = submittedVial;
	}
	
	public void levelUp() {
		level++;
		levelString = new StringBuffer();
		levelString.delete(0, levelString.length());
		levelString.append("LEVEL ").append(Integer.toString(level));
		levelLabel.setText(levelString);
	}
	
}