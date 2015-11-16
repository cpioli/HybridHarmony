package com.cpioli.hybridharmony.performance;

import java.util.Iterator;

import com.badlogic.gdx.utils.Array;
/**
 * This class contains a series of performance readings from the game.
 * These readings are used to gauge what colors should be assigned to replace
 * the empty spaces on the board.
 * 
 * The first element in the Array should be the most recent. The game should
 * never have to worry about removing elements from this object, only adding
 * elements. Removal is automated.
 * @author cpioli
 *
 */
public class SelectionPhaseStatisticArray extends Array<SelectionPhaseStatistic> {
	private String output;
	
	public SelectionPhaseStatisticArray() {
		this(10);
	}
	
	public SelectionPhaseStatisticArray(int length) {
		super(true, length, SelectionPhaseStatistic.class);
		output = "";
	}
	
	@Override
	public void add(SelectionPhaseStatistic sps) {
		SelectionPhaseStatistic[] items = this.items;
		if(this.size == items.length){ //if our array is full
			this.pop(); //pop out the end value
		}
		System.arraycopy(items, 0, items, 1, items.length - 1);
		this.insert(0, sps);
	}
	
	@Override
	public String toString() {
		output = "";
		Iterator<SelectionPhaseStatistic> iterator = this.iterator();
		SelectionPhaseStatistic sps = null;
		while(iterator.hasNext()) {
			sps = iterator.next();
			output += "Turn " + sps.getTurnNumber();
			output += ": Level " + sps.getCurrentLevel();
			output += " " + sps.getSelectionPhaseDuration() + " seconds, ";
			output += sps.getVialSize() + " cells, ";
			output += sps.getQualityValue() + " quality, ";
			output += sps.getCpm() + " cpm, ";
			output += sps.getBonusValue() + "x bonus\n";
		}
		
		return output;
	}
}