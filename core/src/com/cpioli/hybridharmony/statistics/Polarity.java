package com.cpioli.hybridharmony.statistics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.cpioli.hybridharmony.Assets;
import com.cpioli.hybridharmony.GameService;
import com.cpioli.hybridharmony.playfield.MeshActor;

public class Polarity extends Group implements SubmittedVialObserver, PolaritySubject {
	
	private Color leftPolarity;
	private Color rightPolarity;

	private float rateOfPolaritySwitch;
	private float currentTimeElapsed;
	private boolean polarityIsRight;
	
	
	private boolean timerPaused;
	
	private Border border;
	private MeshActor mActor;
	private Label polarityTitle;
	
	private ArrayList<PolarityObserver> observers;
	
	public Polarity(Color left, Color right) {
		this.leftPolarity = left;
		this.rightPolarity = right;
		
		rateOfPolaritySwitch = 2.5f; //to start with...
		currentTimeElapsed = 0.0f;
		polarityIsRight = false;
		border = new Border(4.0f, 160.0f, 0.0f, 50.0f, 50.0f, Color.BLUE);
		mActor = new MeshActor(160.0f, 0.0f, 50.0f, 50.0f, leftPolarity, "polarity");
		polarityTitle = Assets.bigPolarityTitle;
		polarityTitle.setPosition(0.0f, 0.0f);
		this.addActor(mActor);
		this.addActor(border);
		this.addActor(polarityTitle);
		observers = new ArrayList<PolarityObserver>();
		boolean timerPaused = false;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(timerPaused) {
			
		} else {
			currentTimeElapsed += delta;			
		}
		if(currentTimeElapsed >= rateOfPolaritySwitch) {
			currentTimeElapsed -= rateOfPolaritySwitch;
			invertPolarity();
		}
	}
	
	//2 + x^2 / 100, 6 * 1.75 * log(abs(x)) - 7.6

	@Override
	public void update(float contentsQuality) {
		// TODO Auto-generated method stub
		float floatQuality = contentsQuality - 128.0f;
		if(-20.0f < floatQuality && floatQuality < 20.0f) {
			rateOfPolaritySwitch = 1.0f + (float)GameService.INSTANCE.getLevel() + (float)Math.pow(floatQuality,2)*.01f;
		} else {
			rateOfPolaritySwitch = 10.5f * (float)Math.log(Math.abs(floatQuality)) - 6.6f + (float)GameService.INSTANCE.getLevel();
		}
		
		if(currentTimeElapsed >= rateOfPolaritySwitch) {
			currentTimeElapsed -= rateOfPolaritySwitch;
			invertPolarity();
		}
		
	}	
	
	
	private void invertPolarity() {
		polarityIsRight = !polarityIsRight;
		if(polarityIsRight) {
			mActor.setColor(rightPolarity);
		} else {
			mActor.setColor(leftPolarity);
		}
		notifyObservers();
	}

	@Override
	public void registerObserver(PolarityObserver po) {
		observers.add(po);		
	}

	@Override
	public void removeObserver(PolarityObserver po) {
		int i = observers.indexOf(po);
		if(i >= 0) {
			observers.remove(i);
		}
	}

	@Override
	public void notifyObservers() {
		for(int i = 0; i < observers.size();i++) {
			PolarityObserver po = observers.get(i);
			po.update(this.polarityIsRight);
		}
	}
	
	public void reset() {
		rateOfPolaritySwitch = 2.5f; //to start with...
		currentTimeElapsed = 0.0f;
		polarityIsRight = true;
		timerPaused = false;
	}
	
	public void pauseTimer() {
		timerPaused = true;
	}
	
	public void resumeTimer() {
		timerPaused = false;
	}
}
