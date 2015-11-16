package com.cpioli.hybridharmony.statistics;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.cpioli.hybridharmony.Assets;
import com.cpioli.hybridharmony.GameService;
import com.cpioli.hybridharmony.playfield.Selection;

/**
 * This is a group object that contains the gauge visual and the arrows.
 * It'll subscribe to the beaker to update the quality gauge.
 * @author cpioli
 *
 */
public class ContentsQualityGauge extends Group implements SubmittedVialObserver {

	private Image contentsQualityArrow;
	private Image selectionQualityArrow;
	private ContentsQualityGaugeVisual contentsQualityGaugeVisual;
	private float arrowLocationRatio;
	private Border border;
	private final static float SELECTION_ARROW_Y = 60.0f;
	private final static float CONTENTS_ARROW_Y = -30.0f;
	
	
	public ContentsQualityGauge(Selection selection) {
		
		contentsQualityGaugeVisual = new ContentsQualityGaugeVisual(GameService.INSTANCE.getLeftColor(), GameService.INSTANCE.getRightColor());
		this.addActor(contentsQualityGaugeVisual);
		
		contentsQualityArrow = Assets.bigContentsArrow;
		selectionQualityArrow = Assets.bigSelectionArrow;
		contentsQualityArrow.setX(156.0f);
		contentsQualityArrow.setY(CONTENTS_ARROW_Y);
		selectionQualityArrow.setX(156.0f);
		selectionQualityArrow.setY(SELECTION_ARROW_Y);
		selectionQualityArrow.rotateBy(180.0f);
		
		contentsQualityArrow.setVisible(false);
		selectionQualityArrow.setVisible(false);
		arrowLocationRatio = 311.0f / 255.0f;
		

		border = new Border(4.0f, 0.0f, 0.0f, 311.0f, 50.0f, new Color(0.0f, 1.0f, 0.0f, 1.0f));
		this.addActor(border);
		this.addActor(contentsQualityArrow);
		this.addActor(selectionQualityArrow);

	}
	
	//when this method is called, the larger contentsQualityArrow is updated
	public void update(float contentsQuality) {
		if(!contentsQualityArrow.isVisible()) {
			contentsQualityArrow.setVisible(true);
			System.out.println("Setting Contents Quality Gauge to Visible");
		}
		System.out.println("CQG Reading: contents Quality: " + contentsQuality);
		float location = contentsQuality * arrowLocationRatio - contentsQualityArrow.getWidth()/2.0f;
		
		Action currentAction = stillMoving(contentsQualityArrow);
		if(currentAction == null) {
			contentsQualityArrow.addAction(moveTo(location, CONTENTS_ARROW_Y, 0.5f, Interpolation.exp10Out));
		} else {
			currentAction.reset();
			contentsQualityArrow.addAction(moveTo(location, CONTENTS_ARROW_Y, 0.5f, Interpolation.exp10Out));
		}
	}
	
	//for this next update, I do not believe the selection quality arrow should be used. Makes things "interesting"
	public void update(int cellsInSelection, float cumulativeColor) {
		if(cellsInSelection > 0) {
			if(!selectionQualityArrow.isVisible()) {
				selectionQualityArrow.setVisible(true);
				System.out.println("Setting to visible");
			}
			float quality = cumulativeColor / (float)cellsInSelection;
			//the ratio is quality:location::255.0:155.5
			//so it is quality * 155.5 / 255.0, or 
			float location = quality * arrowLocationRatio + 15.0f; //that last piece of addition is to compensate for my issues with the Bounds
			//System.out.println("X value of Location arrow is: " + location);
			
			Action currentAction = stillMoving(selectionQualityArrow);
			if(currentAction == null) {
				selectionQualityArrow.addAction(moveTo(location, SELECTION_ARROW_Y, 0.5f, Interpolation.exp10Out));
			} else {
				currentAction.reset();
				selectionQualityArrow.addAction(moveTo(location, SELECTION_ARROW_Y, 0.5f, Interpolation.exp10Out));
			}
		} else { //indicating the selection has been emptied
			selectionQualityArrow.setVisible(false);
			selectionQualityArrow.setPosition(78.0f, SELECTION_ARROW_Y);
		}
	}
	

	
	private static Action stillMoving(Image img) {
		Array<Action> actionArray = img.getActions();
		for(int i = 0; i < actionArray.size; i++) {
			Action a = actionArray.get(i);
			if(a instanceof MoveToAction) {
				return a;
			}
		}
		
		return null;
	}
	
	public void reset() {
		selectionQualityArrow.clearActions();
		selectionQualityArrow.setX(156.0f);
		selectionQualityArrow.setVisible(false);
		contentsQualityArrow.clearActions();
		contentsQualityArrow.setX(156.0f);
		contentsQualityArrow.setVisible(false);
		
	}

	
}