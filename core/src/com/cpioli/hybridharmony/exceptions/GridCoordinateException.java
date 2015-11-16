package com.cpioli.hybridharmony.exceptions;

public class GridCoordinateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GridCoordinateException() {
		super();
	}
	
	public GridCoordinateException(String message) {
		super(message);
	}
	
	public GridCoordinateException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public GridCoordinateException(Throwable cause) {
		super(cause);
	}
}