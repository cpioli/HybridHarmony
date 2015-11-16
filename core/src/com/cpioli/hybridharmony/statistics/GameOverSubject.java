package com.cpioli.hybridharmony.statistics;

public interface GameOverSubject {
	public void registerObserver(GameOverObserver goo);
	public void removeObserver(GameOverObserver goo);
	public void notifyObservers();
}