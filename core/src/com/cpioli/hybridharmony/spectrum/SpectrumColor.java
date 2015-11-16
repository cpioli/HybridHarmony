package com.cpioli.hybridharmony.spectrum;

import com.badlogic.gdx.graphics.Color;

public abstract class SpectrumColor extends Color {
	
	private float value255;
	
	private void truncate255(float tempValue255) {
		if(tempValue255 > 255.0f) {
			tempValue255 = 255.0f;
		} else if (tempValue255 < 0.0f) {
			tempValue255 = 0.0f;
		}
	}
	
	public abstract float calculateValue255ByColor(SpectrumColor color);
	public abstract void setValue255(float value255);
}