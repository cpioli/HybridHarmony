package com.cpioli.hybridharmony.spectrum;

import com.badlogic.gdx.graphics.Color;

public class GrayscaleSpectrum extends Spectrum {
	
	public GrayscaleSpectrum() {
		spectralColor = new Color[9];
		int val255;
		for(int i = 0; i < 8; i++) {
			val255 = i * 32;
			spectralColor[i] = new Color();
			spectralColor[i].r = r(val255);
			spectralColor[i].g = g(val255);
			spectralColor[i].b = b(val255);
			spectralColor[i].a = 1.0f;
		}
			spectralColor[8] = new Color();
			spectralColor[8].set(Color.WHITE);
	}

	@Override
	public float r(int colorValue) {
		return ((float)colorValue)/255.0f;
	}

	@Override
	public float g(int colorValue) {
		
		return ((float)colorValue)/255.0f;
	}

	@Override
	public float b(int colorValue) {
		return ((float)colorValue)/255.0f;
	}
	
	public Color getSpectrumColor(int index) {
		return spectralColor[index];
	}

	@Override
	public float getValue255(Color color) {
		return (color.r * 255.0f);
	}
}