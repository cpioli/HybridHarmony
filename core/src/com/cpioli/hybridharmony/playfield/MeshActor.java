package com.cpioli.hybridharmony.playfield;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

/*
 * TODO: call the MeshActor's fillRemainderStretch when entering or exiting a particular cell
 */

public class MeshActor extends Actor implements Disposable{
	
	//Initialization of primitive fields
	private float absoluteX;
	private float absoluteY;
	private float absoluteWidth;
	private float absoluteHeight;
	
	public static final float MAX_SIZE = 70.0f;
	public static final float MIN_SIZE = 50.0f;
	
	//Initialization of Object fields
	protected Mesh mesh;


	//Initialization of Enumerations and enumerated values
	public static enum transformOrigin {
		LOWER_LEFT,
		LOWER_RIGHT,
		LOWER_CENTER,
		RIGHT,
		LEFT,
		CENTER,
		UPPER_LEFT,
		UPPER_RIGHT,
		UPPER_CENTER
	};
	protected transformOrigin origin;
	
	//constructors and copy constructors
	public MeshActor() {
		this(0.0f, 0.0f, 0.0f, 0.0f, new Color(), null);
	}
	
	public MeshActor(float x, float y, float width, float height, float r, float g, float b, float a, int gridX, int gridY, int colorValue) {
		this(x, y, width, height, new Color(r, g, b, a), null);
	}
	
	public MeshActor(float x, float y, float width, float height, Color color, String name) {
		//setting all super values
		super.setX(x);
		super.setY(y);
		super.setWidth(width);
		super.setHeight(height);
		super.setColor(color);
		super.setName(name);
	
		this.mesh = new Mesh(true, 4, 4,
				new VertexAttribute(Usage.Position, 3, "a_position"),
				new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
		this.origin = transformOrigin.LOWER_LEFT;
	}

	
	/**
	 * this draw method is called by Group and Stage objects.
	 * Although we'll never really need a batcher, the parentAlpha
	 * is important.
	 * 
	 * This takes the place of the render function. the SpriteBatch will not
	 * be used
	 *
	 * 11/14/2015: To be deprecated
	 */
	/*@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.end();

		float tempColor = Color.toFloatBits(super.getColor().r, super.getColor().g, super.getColor().b, super.getColor().a * parentAlpha);
		mesh.setVertices(new float[] {
			            super.getX(),              super.getY(), 0.0f, tempColor,
			super.getX()+super.getWidth(),              super.getY(), 0.0f, tempColor,
			            super.getX(), super.getY()+super.getHeight(), 0.0f, tempColor,
			super.getX()+super.getWidth(), super.getY()+super.getHeight(), 0.0f, tempColor
		});
		mesh.render(GL20.GL_TRIANGLE_STRIP, 0, 4);
		batch.begin();
	}*/
	
	public void dispose() {
		mesh.dispose();
	}
	

		

	/**
	 * returns true if the point specified by the two floating point parameters
	 * lies within the range of the actor.
	 * returns false otherwise
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean contains(MeshActor cell, float x, float y) {
		return (cell.getX() < x && cell.getRight() > x && cell.getY() < y && cell.getTop() > y);
	}
		
	/**
	 * Determines if we have a cell that can be connected to the Vial
	 * As of now, this will only check for the immediate neighbor, not an overlapping neighbor
	 * like the current algorithm in HybridHarmony
	 * @param cell
	 * @return
	 */

	/*public void render(ShapeRenderer renderer) {
		if(renderer.isDrawing()) {
			renderer.setColor(super.getColor());
			renderer.rect(super.getY(), super.getY(), super.getWidth(), super.getHeight());
		}

	}*/
	
	/*-----------------------
	 * Getter and Setter methods
	 ------------------------*/
	
	@Override
	public void setWidth(float width) {
		if (origin == transformOrigin.CENTER ||
				origin == transformOrigin.LOWER_CENTER ||
				origin == transformOrigin.UPPER_CENTER) {
			float difference = super.getWidth() - width;
			super.setX(super.getX() + difference/2);
		} else if (origin == transformOrigin.RIGHT ||
				origin == transformOrigin.LOWER_RIGHT ||
				origin == transformOrigin.UPPER_RIGHT) {
			float difference = super.getWidth() - width;
			super.setX(super.getX() + difference);
		}
		super.setWidth(width);
	}
	
	@Override
	public void setHeight(float height) {
		if (origin == transformOrigin.LEFT ||
			origin == transformOrigin.RIGHT ||
			origin == transformOrigin.CENTER) {
			float difference = super.getHeight() - height;
			super.setY(super.getY() + difference/2);
		} else if(origin == transformOrigin.UPPER_LEFT ||
				origin == transformOrigin.UPPER_CENTER ||
				origin == transformOrigin.UPPER_RIGHT) {
			float difference = super.getHeight() - height;
			super.setY(super.getY() + difference);
		}
		super.setHeight(height);
	}
	
	@Override
	public float getWidth(){
		return super.getWidth();
	}
	
	@Override
	public float getHeight() {
		return super.getHeight();
	}
	
	public void setOrigin(transformOrigin origin) {
		this.origin = origin;
	}
	
	public transformOrigin getOrigin() {
		return origin;
	}
	
	public Color getColor() {
		return super.getColor();
	}
	
	public void setX(float x) {
		super.setX(x);
	}
	
	public void setY(float y) {
		super.setY(y);
	}
	
	public void setXY(float x, float y) {
		super.setX(x);
		super.setY(y);
	}


	//on occasion, the input moves so fast that the InputProcessor cannot track
	//it as finely as when dragging slower. This causes the MeshActors to
	//behave strangely, and can make the MeshActor stretch or shrink
	//this tries to solve the issue
	public void resetX() {
		if(super.getX() != absoluteX)
			super.setX(absoluteX);
	}
	
	public void resetWidth() {
		if(super.getWidth() != absoluteWidth) {
			super.setWidth(absoluteWidth);
		}
	}
	
	public void resetY() {
		if(super.getY() != absoluteY)
			super.setY(absoluteY);
	}
	
	public void resetHeight() {
		if(super.getHeight() != absoluteHeight) {
			super.setHeight(absoluteHeight);
		}
	}
	

}