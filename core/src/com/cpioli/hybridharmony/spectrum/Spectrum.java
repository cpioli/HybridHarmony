package com.cpioli.hybridharmony.spectrum;

import com.badlogic.gdx.graphics.Color;

/**
 * Important note: color gradients' (I should call them gradients now that I
 * know there exists an appropriate term for it) values are not as easily
 * distinguishable in the RGB color space as in the CIELab color space. It
 * might be best to obtain equations for gradients in the CIELab color spectrum
 * then converting them to RGB.
 * 
 * The other issue is that each piece of hardware will display colors
 * differently, even before taking into account users' options to adjust their
 * displays (tint, contrast, brightness, hue, etc.).
 * @author cpioli
 *
 */

public abstract class Spectrum {
	
	protected Color[] spectralColor;
	
	public abstract float r(int colorValue);
	
	public abstract float g(int colorValue);
	
	public abstract float b(int colorValue);
	
	public abstract float getValue255(Color color);
	
	public Color getLeftColor() {
		return spectralColor[0];
	}
	
	public Color getRightColor() {
		return spectralColor[8];
	}
	
	public Color getSpectrumColor(int index) {
		return spectralColor[index];
	}

}