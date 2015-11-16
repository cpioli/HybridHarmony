package com.cpioli.hybridharmony.statistics;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * A beaker contains the 3 vials. It's responsibility is to display both the
 * beaker png image and restrain the Contents object.
 * Beakers contain 3 objects: the beaker itself, and two Mesh actors
 * The display has 3 layers: the beaker image is at top, the contents is at
 * bottom, and in-between are two triangles which hide the remnants of the
 * contents MeshActor.
 * 
 * This class has exclusive access to the Contents object. The game is supposed
 * to interact with the Contents object by adding and removing vials from the
 * beaker class.
 * @author cpioli
 *
 */
public class Beaker extends Group implements SubmittedVialSubject {
	
	private Contents contents;
	private Image beaker;
	//private MeshActor droplet;
	
	public ArrayList<SubmittedVialObserver> observers;
	
	boolean dropIsOccurring;
	
	public Beaker(Image beaker, float beakerX, float beakerY) {
		this.beaker = beaker;
		beaker.setX(beakerX);
		beaker.setY(beakerY);
		this.contents = new Contents(beaker.getX()+8.0f, beaker.getY()+4.0f, beaker.getWidth()-8.0f);
		super.addActor(this.contents);
		super.addActor(this.beaker);
		super.setTouchable(Touchable.enabled);
		observers = new ArrayList<SubmittedVialObserver>();
	}
	
	/**
	 * a call to request the Contents object decrease itself by the amount of
	 * the vial added least previously. Returns false if unsuccessful.
	 * @return
	 */
	//public boolean ejectVial() {
	//	
	//}
	
	/**
	 * This method is called when a new vial is to be dropped into the Contents
	 * The droplet is tweened from some point all the way to the bottom of the
	 * beaker.
	 * Only one droplet can be displayed at a time. If a droplet is dropping,
	 * this method returns false indicating a new vial cannot be dropped in yet
	 * 
	 * UPDATE: This method is delayed. I will not use for the next couple of weeks.
	 * A simpler version will substitute that does not use tweens.
	 * @return
	 */
	public boolean addDroplet(Vial vial) {
		if(!dropIsOccurring) {
			return !dropIsOccurring; //false
		} else {
			//replace this comment with the commands to start the drop
			dropIsOccurring = true;
			return true;
		}
	}
	
	public void addVial(Vial vial) {
		contents.addVial(vial);
		//System.out.println("Contents of Beaker: \n" + contents.toString() + "\nend of contents");
		notifyObservers();
	}
	
	public float getBeakerQuality() {
		return contents.getContentsQuality();
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
			bo.update(getBeakerQuality());
		}
	}
	
	public void reset() {
		contents.reset();
	}
}

