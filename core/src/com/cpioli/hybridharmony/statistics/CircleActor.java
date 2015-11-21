package com.cpioli.hybridharmony.statistics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Align;

public class CircleActor extends Actor implements Disposable {

	
	private float centerX;
	private float centerY;
	private float radius;
	
	private int segments; //number of fan triangles to use
	private float theta, c, s, t; //angle of each triangle fan
	protected Mesh mesh;
	protected Mesh borderMesh;
	protected Color borderColor;
	private float thickness;
	private boolean hasBorder;
	
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
	
	public CircleActor(float x, float y, int alignment, String name) {
		super.setOrigin(alignment);
		super.setName(name);
		super.setX(x);
		super.setY(y);
	}
	
	/*@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.end();
		float tempColor = Color.toFloatBits(super.getColor().r, super.getColor().g, super.getColor().b, super.getColor().a * parentAlpha);
		float borderTempColor = 0.0f;
		float[] vertices = new float[segments * 4 + 8]; //the first additional 4 are for the center, the second additional four connect point 2 to point (segment)
		float[] borderVertices = new float[segments * 8 + 8];
		float x = radius;
		float y = 0;
		float xb = 0;
		float yb = 0;
		float tb = 0;
		
		vertices[0] = super.getX();
		vertices[1] = super.getY();
		vertices[2] = 0.0f;
		vertices[3] = tempColor;
		
		if(hasBorder) {
			xb = x + thickness;
			yb = y;
			borderTempColor = Color.toFloatBits(borderColor.r, borderColor.g, borderColor.b, borderColor.a * parentAlpha);
		}
		
		for(int i = 1; i <= segments; i++) {
			vertices[i * 4] = x + super.getX();
			vertices[i * 4 + 1] = y + super.getY();
			vertices[i * 4 + 2] = 0.0f;
			vertices[i * 4 + 3] = tempColor;
			
			t = x;
			x = c * x - s * y;
			y = s * t + c * y;
		}
		vertices[(segments+1)*4] = vertices[4];
		vertices[(segments+1)*4 + 1] = vertices[5];
		vertices[(segments+1)*4 + 2] = vertices[6];
		vertices[(segments+1)*4 + 3] = vertices[7];
		if(hasBorder) {
			
			borderVertices[0] = vertices[4]; //REMEMBER: the first vertex in the Circle is the center of the circle
			borderVertices[1] = vertices[5];//           the second vertex is the first vertex on the circle's edge
			borderVertices[2] = 0.0f; //this is 0.0f
			borderVertices[3] = borderTempColor;
			
			borderVertices[4] = super.getX() + xb;
			borderVertices[5] = super.getY() + yb;
			borderVertices[6]= 0.0f;
			borderVertices[7] = borderTempColor;
			
			borderVertices[8] = vertices[8];
			borderVertices[9] = vertices[9];
			borderVertices[10] = 0.0f;
			borderVertices[11] = borderTempColor;
			
			tb = xb;
			xb = c * xb - s * yb;
			yb = s * tb + c * yb;
			
			borderVertices[12] = super.getX() + xb;
			borderVertices[13] = super.getY() + yb;
			borderVertices[14] = 0.0f;
			borderVertices[15] = borderTempColor;
			


			for(int i = 0; i < segments; i++) {
				int k = i * 8;
				int v = (i + 1) * 4; //original vertices array's index
				borderVertices[k+0] = vertices[v]; //x position of circle vertex
				borderVertices[k+1] = vertices[v+1]; //y position of circle vertex
				borderVertices[k+2] = 0.0f;
				borderVertices[k+3] = borderTempColor;
				
				borderVertices[k+4] = super.getX() + xb; //x position of external side of border
				borderVertices[k+5] = super.getY() + yb; //y position of external side of border
				borderVertices[k+6] = 0.0f;
				borderVertices[k+7] = borderTempColor;
				
				tb = xb;
				xb = c * xb - s * yb; //calculating new external x position for next part of circle border
				yb = s * tb + c * yb; //calculating new external y position for next part of circle border
			}
			
			
			//for(int i = 0; i < 8; i++) {
			//	borderVertices[segments * 8 + i] = borderVertices[i];
			//}
		}
		//System.out.println("This will display if we are able to draw the circle properly.");

		mesh.setVertices(vertices);
		mesh.render(GL10.GL_TRIANGLE_FAN, 0, segments+2);
		if(hasBorder) {
			borderMesh.setVertices(borderVertices);
			borderMesh.render(GL10.GL_TRIANGLE_STRIP, 0, segments * 2);
		}
		//drawing calculations go here (it might get ugly...)
		batch.begin();
		
	}*/

	public void render(ShapeRenderer renderer) {
		if(renderer.getCurrentType() != ShapeRenderer.ShapeType.Filled) {
			renderer.end();
			renderer.begin(ShapeType.Filled);
		}
		renderer.setColor(super.getColor());
		renderer.circle(super.getX(), super.getY(), radius);
	}

	public void setRadius(float radius) {
		this.radius = radius;
		float width = radius * 2;
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
		} else {
			super.setWidth(width);
		}
		float height = radius * 2;
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
		} else {
			super.setHeight(height);
		}
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		mesh.dispose();
	}
	
	public void addBorder(float thickness, Color color) {
		hasBorder = true;
		this.thickness = thickness;
		this.borderColor = color;
	}
}