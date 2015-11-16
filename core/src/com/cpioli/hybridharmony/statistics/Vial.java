package com.cpioli.hybridharmony.statistics;

/**
 * A Vial is not visibly shown. It is the contents of a selection that will 
 * undergo the submission process. It must be sent to the Beaker so the Beaker
 * will know how to adjust the Contents meshActor for color.
 * 
 * Instances of this class might be useful when Playfield needs to update the
 * Beaker Quality Gauge, the score, and the bonus
 * 
 * Also note that this object might become visualized so I can display it 
 * moving from the playfield and into the beaker.
 * 
 * @author cpioli
 *
 */
public class Vial {
	
	private int size;
	private float cumulativeColor;
	
	
	public Vial(int size, float cumulativeColor) {
		this.size = size;
		this.cumulativeColor = cumulativeColor;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public float getCumulativeColor() {
		return this.cumulativeColor;
	}
	
	@Override
	public String toString() {
		String output = "Size: " + this.size + " cells, color: " + this.cumulativeColor;
		return output;
	}
}