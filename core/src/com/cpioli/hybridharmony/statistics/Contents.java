package com.cpioli.hybridharmony.statistics;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeTo;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.cpioli.hybridharmony.playfield.MeshActor;

/**
 * This is a mesh which changes shape based on how many cells' worth of vials
 * are contained in the beaker.
 * 
 * NOTE: need to implement a size 3 Array<Vial> to determine how to tween this
 * object.
 * @author cpioli
 *
 */
public class Contents extends MeshActor {
	//private float beakerFill; //if the dropping MeshActor passes this point
					  //then the Contents will tween its height
	ContentsArray vials;
	private static final float CONTENTS_CHANGE_DURATION = 0.4f;
	private float width;
	
	Contents(float originX, float originY, float width) {
		super(originX + 2.0f, originY, 108.0f, 0.0f, new Color(0.0f, 
				0.0f, 0.0f, 1.0f), "Contents");
		super.setTouchable(Touchable.disabled);
		this.width = 108.0f;
		vials = new ContentsArray();
		//beakerFill = 0.0f;
		//super.setVisible(false);
		
	}
	
	//takes the vial submitted from the Playfield
	//Contents takes the vial and adds it to the 
	public void contact(Vial vial) {
		
	}
	
	public void addVial(Vial vial) {
		@SuppressWarnings("unused")
		Vial poppedVial = vials.push(vial);
		float currentColorValue = vials.getCumulativeColor() / (255.0f * (float)vials.getCellCount());
		if(vials.size == 1) {
			super.setColor(new Color(currentColorValue, currentColorValue, currentColorValue, 1.0f));
			super.addAction(sequence(
					sizeTo(width, 3.0f * vials.getCellCount(), CONTENTS_CHANGE_DURATION),
					new Action() {
						@Override
						public boolean act(float delta) {
							Contents contents = (Contents)this.getActor();
							((Beaker)contents.getParent()).notifyObservers();
							return true;
						}
						
			}));
		} else {
			super.addAction(sequence(parallel(
					color(new Color(currentColorValue, currentColorValue, currentColorValue, 1.0f), CONTENTS_CHANGE_DURATION),
					sizeTo(width, 6.0f * vials.getCellCount(), CONTENTS_CHANGE_DURATION)
					), new Action() {

						@Override
						public boolean act(float delta) {
							Contents contents = (Contents)this.getActor();
							((Beaker)contents.getParent()).notifyObservers();
							return true;
						}
						
			}));
		}
	}
	
	public float getContentsQuality() { //returns a number between 0.0f and 255.0f
		return vials.getCumulativeColor() / (float)vials.getCellCount();
	}
	
	public void reset() {
		vials.clear();
		super.setHeight(0.0f);
		super.setColor(Color.BLACK);
	}
}

/**
 * This class acts as a queue. no actual popping should occur.
 * @author cpioli
 *
 */
class ContentsArray extends Array<Vial> {
	
	public ContentsArray() {
		super(true, 3, Vial.class);
	}
	
	public Vial push(Vial vial) {
		Vial returnVial = null;
		if(super.size == 3) {
			returnVial = super.removeIndex(0);
		}
		super.add(vial);
		return returnVial;
	}
	
	public int getCellCount() {
		Iterator<Vial> vialIterator = super.iterator();
		Vial vial = null;
		int cellCount = 0;
		while(vialIterator.hasNext()) {
			vial = vialIterator.next();
			cellCount += vial.getSize();
		}
		return cellCount;
	}
	
	public float getCumulativeColor() {
		Iterator<Vial> vialIterator = super.iterator();
		Vial vial = null;
		float colorValuesCount = 0.0f;
		while(vialIterator.hasNext()) {
			vial = vialIterator.next();
			colorValuesCount += vial.getCumulativeColor();
		}
		
		return colorValuesCount;
	}
	
	@Override
	public String toString() {
		String output = "";
		float colorValuesCount = 0.0f;
		int cellCount = 0;
		Iterator<Vial> vialIterator = super.iterator();
		Vial vial = null;
		while(vialIterator.hasNext()) {
			vial = vialIterator.next();
			output += "Vial" + super.indexOf(vial, true) + " " + vial.toString() + "\n";
			cellCount += vial.getSize();
			colorValuesCount += vial.getCumulativeColor();
		}
		output += "Total cells in Vial: " + cellCount + "\n" + "Total color value: " + colorValuesCount + "\n";
		
		return output;
	}
}