package com.cpioli.hybridharmony.statistics;


public interface SubmittedVialSubject {
	public void registerObserver(SubmittedVialObserver bo);
	public void removeObserver(SubmittedVialObserver bo);
	public void notifyObservers();
}