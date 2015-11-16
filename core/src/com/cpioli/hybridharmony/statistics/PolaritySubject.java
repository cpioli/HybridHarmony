package com.cpioli.hybridharmony.statistics;

public interface PolaritySubject {
	public void registerObserver(PolarityObserver po);
	public void removeObserver(PolarityObserver po);
	public void notifyObservers();
}