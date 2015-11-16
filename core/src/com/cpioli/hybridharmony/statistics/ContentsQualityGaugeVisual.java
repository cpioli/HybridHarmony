package com.cpioli.hybridharmony.statistics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cpioli.hybridharmony.playfield.MeshActor;
import com.cpioli.hybridharmony.spectrum.Spectrum;

//I must override the draw and construction methods of MeshActor
public class ContentsQualityGaugeVisual extends MeshActor {
	
	private Color leftSide;
	private Color rightSide;
	
	public ContentsQualityGaugeVisual(Color leftColor, Color rightColor) {
		super(0.0f, 0.0f, 311.0f, 50.0f, Color.BLACK, "QualityGauge");
		this.leftSide = leftColor;
		this.rightSide = rightColor;
		System.out.println("Left Color: " + leftSide.toString());
		System.out.println("Right Color: " + rightSide.toString());
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.end();

		float tempLeftColor = Color.toFloatBits(this.leftSide.r, this.leftSide.g, this.leftSide.b, this.leftSide.a * parentAlpha);
		float tempRightColor = Color.toFloatBits(this.rightSide.r, this.rightSide.g, this.rightSide.b, this.rightSide.a * parentAlpha);
		mesh.setVertices(new float[] {
			            super.getX(),                   super.getY(), 0.0f, tempLeftColor,
	   super.getX()+super.getWidth(),                   super.getY(), 0.0f, tempRightColor,
			            super.getX(), super.getY()+super.getHeight(), 0.0f, tempLeftColor,
       super.getX()+super.getWidth(), super.getY()+super.getHeight(), 0.0f, tempRightColor
		});
		mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		batch.begin();
	}
	
	//Eventually, we will want to tween the Visual's own special Color values,
	//but that will not happen until later.
	public void swapSpectrum(Color leftColor, Color rightColor) {
		this.leftSide = leftColor;
		this.rightSide = rightColor;
	}
	
}