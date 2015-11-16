package com.cpioli.hybridharmony.statistics;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.IntAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.cpioli.hybridharmony.Assets;

public class Score extends Group {
	
	private final float BONUS_TIME_LIMIT = 9.0f;
	private final float MAX_BONUS = 5.0f;
	private float timePassed;
	private int score;
	private float bonus;
	
	private IntAction scoreTween;
	private boolean isTweening;
	private int updatedScore;
	
	private int[] baseScore = {0, 0, 10, 20, 40, 70, 110, 160, 220, 290, 360, 440};
	
	private Label scoreLabel;
	private Label scoreValue;
	private Label bonusLabel;
	private Label bonusValue;
	
	private boolean bonusActivated;
	private boolean bonusPaused;
	
	public Score() {
		timePassed = 0.0f;
		bonus = 1.0f;
		score = 0;
		this.scoreLabel = Assets.bigScoreTitle;
		this.scoreLabel.setPosition(0.0f, 0.0f);
		this.scoreValue = Assets.bigScoreNumber;
		this.scoreValue.setAlignment(Align.left);
		this.scoreValue.setPosition(130.0f, 0.0f);
		this.addActor(scoreLabel);
		this.addActor(scoreValue);
		this.bonusLabel = Assets.bigBonusTitle;
		this.bonusLabel.setPosition(0.0f, 50.0f);
		this.bonusValue = Assets.bigBonusNumber;
		this.bonusValue.setPosition(164.0f, 80.0f);
		this.bonusValue.setText("1.0");
		this.addActor(bonusLabel);
		this.addActor(bonusValue);
		this.bonusActivated = false;
		this.bonusPaused = false;
		
		isTweening = false;
		scoreTween = new IntAction();
		updatedScore = 0;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if(!bonusPaused) {
			timePassed += delta;
		}
		if(isTweening) {
			scoreTween.act(delta);
			score = scoreTween.getValue();
		}
		
		if(isTweening && score == updatedScore) {
			isTweening = false;
		}
		scoreValue.setText("" + Integer.toString(score));
	}
	
	public void update(int sizeOfSubmission) {
		if(bonusActivated) {
			float bonusIncrement = (BONUS_TIME_LIMIT - timePassed) / 2.0f;
			if(bonusIncrement > 0.0f) {
				bonus += bonusIncrement;
				if(bonus > MAX_BONUS) {
					bonus = MAX_BONUS;
				}
				System.out.println("bonusIncrement: " + bonusIncrement);
				long temp = (long)Math.floor(bonus * 10.0f); //truncation to the 1st decimal place
				bonus = temp / 10.0f;
				System.out.println("New Bonus: " + bonus);
			} else {
				System.out.println("Bonus reset!");
				bonus = 1.0f;
			}
			timePassed = 0.0f;
		}
		if(!bonusActivated) {
			bonusActivated = true;
		}
		updatedScore += baseScore[sizeOfSubmission - 1] * bonus;
		if(isTweening) {
			score = scoreTween.getValue();
			scoreTween.finish();
		} else {
			isTweening = true;
		}
		scoreTween.setStart(score);
		scoreTween.setEnd(updatedScore);
		scoreTween.setInterpolation(Interpolation.linear); //for now use linear, switch to something else later
		scoreTween.setDuration(1.0f);
		scoreTween.reset();
		bonusValue.setText(Float.toString(bonus));
		System.out.println("points added: " + baseScore[sizeOfSubmission - 1] * bonus);
		System.out.println("total score: " + score);
	}
	
	protected void reset() {
		timePassed = 0.0f;
		bonus = 1.0f;
		bonusValue.setText(Float.toString(bonus));
		score = 0;
		scoreValue.setText(Long.toString(score));
		scoreTween.finish();
		updatedScore = 0;
		bonusPaused = false;
	}
	
	protected void pauseBonus() {
		this.bonusPaused = true;
	}
	
	protected void resumeBonus() {
		this.bonusPaused = false;
	}
}