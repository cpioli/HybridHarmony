package com.cpioli.hybridharmony.playfield;

public interface SelectionSubject {
	public void registerObserver(SelectionObserver so);
	public void removeObserver(SelectionObserver so);
	public void notifyObservers();
}