package com.cpioli.hybridharmony.utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * A texture whose sole purpose is to be clipped to represent a speedometer
 * @author cpioli
 *
 */
public class ClippedTexture extends Image {

	private Texture texture;
	private Vector2 position;
	
	float targetWidth;
	 
	//Src Dimensions of Image
	 
	int atlasRegionSrcWidth;
	int atlasRegionSrcHeight;
	int atlasRegionSrcX;
	int atlasRegionSrcY;
	 
	//Ratio of dimension of target and source
	 
	float srcTargetRatioX;
	float srcTargetRatioY;
	
	float current_ImageWidth, current_ImageHeight;
	 
	int clipSrcWidth;
	int clipSrcHeight;
	
	
	public ClippedTexture(TextureRegion tr, float x, float y) {
		super(tr.getTexture());
		texture = tr.getTexture();
		super.setX(x);
		super.setY(y);
		atlasRegionSrcX = tr.getRegionX();
		atlasRegionSrcY = tr.getRegionY();
		atlasRegionSrcHeight = tr.getRegionHeight();
		atlasRegionSrcWidth = tr.getRegionWidth();
		
		current_ImageWidth = (float)atlasRegionSrcWidth;
	}
	
	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		validate();
		
		float x = super.getX();
		float y = super.getY();
		clipSrcWidth = (int)targetWidth;
		clipSrcHeight = atlasRegionSrcHeight;
		batch.draw(texture, 
				x - current_ImageWidth / 2,
				y - current_ImageHeight / 2,
				super.getOriginX() - super.getImageX(),
				super.getOriginY() - super.getImageY(),
				current_ImageWidth,
				current_ImageHeight,
				1.0f,
				1.0f,
				super.getRotation(),
				atlasRegionSrcX,
				atlasRegionSrcY,
				atlasRegionSrcWidth,
				atlasRegionSrcHeight,
				false,
				false
				);
	}
	
}