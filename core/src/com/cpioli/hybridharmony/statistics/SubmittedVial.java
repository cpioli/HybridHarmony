package com.cpioli.hybridharmony.statistics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

public class SubmittedVial extends Group implements SubmittedVialSubject {
	
	private int size;
	private float cumulativeColor;
	private float value255;
	
	private CircleActor vial;
	
	public ArrayList<SubmittedVialObserver> observers;
	
	public SubmittedVial(float x, float y, float radius) {
		vial = new CircleActor(x, y, Align.bottomLeft, "submittedVial");
		vial.addBorder(5.0f, Color.YELLOW);
		this.addActor(vial);
		size = 0;
		cumulativeColor = 0.0f;
		value255 = 0.0f;
		
		observers = new ArrayList<SubmittedVialObserver>();
	}
	
	public void addVial(Vial vial) {
		this.size = vial.getSize();
		this.cumulativeColor = vial.getCumulativeColor();
		this.value255 = cumulativeColor/size;
		float cumulativeToFloat = value255 / 255.0f;
		this.vial.setColor(cumulativeToFloat, cumulativeToFloat, cumulativeToFloat, 1.0f);
		//notifyObservers(); taking this out for the Submission phase animations
	}
	
	public void reset() {
		size = 0;
		this.vial.setColor(Color.CLEAR);
		this.cumulativeColor = 0.0f;
		this.value255 = 0.0f;
	}

	public void registerObserver(SubmittedVialObserver bo) {
		observers.add(bo);
		
	}

	public void removeObserver(SubmittedVialObserver bo) {
		int i = observers.indexOf(bo);
		if(i >= 0) {
			observers.remove(i);
		}
	}

	public void notifyObservers() {
		for(int i = 0; i < observers.size(); i++) {
			SubmittedVialObserver bo = observers.get(i);
			bo.update(getValue255());
		}
	}

	public float getValue255() {
		return value255;
	}
	
}