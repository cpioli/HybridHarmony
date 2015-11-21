package com.cpioli.hybridharmony.playfield;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

/**
 * Short for "Quadrilateral", a four sided object
 * Used to contain both a visual Rectangle/square (view)
 * 
 * UPDATE 9/29: Added a feature to include layering on the z-axis.
 *
 * Update 11/18/2015: GL10 is not used any longer
 * need to adjust game to use GL20 shapeRenderer
 * This entire object might be deprecated.
 */

public class Quad {
	protected Mesh mesh;
	protected float x;
	protected float y;
//	private float z; //this is the layer of the current quad
	protected float width;
	protected float height;
	private Color color;
	
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
	
	public Quad() {
		this.x = 0;
		this.y = 0;
//		this.z = 0;
		this.width = 0;
		this.height = 0;
		this.color = new Color();
		mesh = new Mesh(true, 4, 4,
				new VertexAttribute(Usage.Position, 3, "a_position"),
				new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
		origin = transformOrigin.LOWER_LEFT;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public Quad(float x, float y, float width, float height, Color color) {
		this.x = x;
		this.y = y;
//		this.z = layer;
		this.width = width;
		this.height = height;
		this.color = color;
		
		
		mesh = new Mesh(true, 4, 4,
				new VertexAttribute(Usage.Position, 3, "a_position"),
				new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
		
		origin = transformOrigin.LOWER_LEFT;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public Quad(float x, float y, float width, float height, float r, float g, float b, float a) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = new Color(r, g, b, a);
		
		mesh = new Mesh(true, 4, 4,
				new VertexAttribute(Usage.Position, 3, "a_position"),
				new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
	}
	
	public void setDimensions(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
//		this.z = layer;
		this.width = width;
		this.height = height;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setX(int x) {
		this.x = (float)x;
	}
	
	public float getX() {
		return x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setY(int y) {
		this.y = (float)y;
	}
	
	public float getY() {
		return y;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void transparencyOn() {
		color.a = 0.0f;
	}
	
	public void transparencyOff() {
		color.a = 1.0f;
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		if (origin == transformOrigin.CENTER ||
				origin == transformOrigin.LOWER_CENTER ||
				origin == transformOrigin.UPPER_CENTER) {
			float difference = this.width - width;
			this.x += difference/2;
		} else if (origin == transformOrigin.RIGHT ||
				origin == transformOrigin.LOWER_RIGHT ||
				origin == transformOrigin.UPPER_RIGHT) {
			float difference = this.width - width;
			this.x += difference;
		}
		this.width = width;
	}
	
	public float getHeight() {
		return this.height;
	}
	
	public void setHeight(float height) {
		if (origin == transformOrigin.LEFT ||
			origin == transformOrigin.RIGHT ||
			origin == transformOrigin.CENTER) {
			float difference = this.height - height;
			this.y += difference/2;
		} else if(origin == transformOrigin.UPPER_LEFT ||
				origin == transformOrigin.UPPER_CENTER ||
				origin == transformOrigin.UPPER_RIGHT) {
			float difference = this.height - height;
			this.y += difference;
		}
		this.height = height;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public float getAlpha() {
		return this.color.a;
	}
	
	public void setAlpha(float alpha) {
		this.color.a = alpha;
	}
	
	public void setOrigin(transformOrigin origin) {
		this.origin = origin;
	}
	
	public transformOrigin getOrigin() {
		return origin;
	}
	
	public void render() {
		float tempColor = color.toFloatBits();
		mesh.setVertices(new float[] {
			      x,        y, 0.0f, tempColor,
			x+width,        y, 0.0f, tempColor,
			      x, y+height, 0.0f, tempColor,
			x+width, y+height, 0.0f, tempColor
		});
		//mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}

	public void render(Color color) {
		float tempColor = color.toFloatBits();
		mesh.setVertices(new float[] {
			      x,        y, 0.0f, tempColor,
			x+width,        y, 0.0f, tempColor,
			      x, y+height, 0.0f, tempColor,
			x+width, y+height, 0.0f, tempColor
		});
		//mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
}