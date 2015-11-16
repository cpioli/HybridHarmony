package com.cpioli.hybridharmony.performance;

/**
 * Holds information on one instance of the Selection Phase.
 * A list of these objects keep track of player performance for the purpose
 * of determining which colors should be allocated to the playing field.
 * @author cpioli
 *
 */
public class SelectionPhaseStatistic {
	private float selectionPhaseDuration;
	private int vialSize;
	private float qualityValue;
	private float cpm;
	private float bonusValue;
	private int currentLevel;
	private int turnNumber;

	static private int turnCounter = 0;
	
	public SelectionPhaseStatistic() {
		this(0L, 0, 0.0f, 0.0f, 0.0f, 0);
	}
	
	public SelectionPhaseStatistic(long duration, int size, float qualityValue, float cpm, float bonusValue, int currentLevel) {
		this.selectionPhaseDuration = duration;
		this.vialSize = size;
		this.qualityValue = qualityValue;
		this.cpm = cpm;
		this.bonusValue = bonusValue;
		this.currentLevel = currentLevel;
		turnCounter++;
		this.turnNumber = turnCounter;
	}

	public float getSelectionPhaseDuration() {
		return selectionPhaseDuration;
	}

	public void setSelectionPhaseDuration(float selectionPhaseDuration) {
		this.selectionPhaseDuration = selectionPhaseDuration;
	}

	public int getVialSize() {
		return vialSize;
	}

	public void setVialSize(int vialSize) {
		this.vialSize = vialSize;
	}

	public float getQualityValue() {
		return qualityValue;
	}

	public void setQualityValue(float qualityValue) {
		this.qualityValue = qualityValue;
	}

	public float getCpm() {
		return cpm;
	}

	public void setCpm(float cpm) {
		this.cpm = cpm;
	}

	public float getBonusValue() {
		return bonusValue;
	}

	public void setBonusValue(float bonusValue) {
		this.bonusValue = bonusValue;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	
	public int getTurnNumber() {
		return turnNumber;
	}
}