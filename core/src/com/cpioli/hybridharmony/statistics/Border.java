package com.cpioli.hybridharmony.statistics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.cpioli.hybridharmony.playfield.MeshActor;

public class Border extends Group{
	
	public float thickness;
	public float x;
	public float y;
	public float width;
	public float height;
	public Color color;
	private MeshActor[] lines;
	
	public Border(float thickness, float x, float y, float width, float height, Color color) {
		this.thickness = thickness;
		
		lines = new MeshActor[4];
		lines[0] = new MeshActor(                    x,                      y,     width,               thickness, color, "south");//lower _
		lines[1] = new MeshActor(x + width - thickness,          y + thickness, thickness, height - 2.0f*thickness, color, "east");//right |
		lines[2] = new MeshActor(                    x, y + height - thickness,     width,               thickness, color, "north"); //upper _
		lines[3] = new MeshActor(                    x,          y + thickness, thickness, height - 2.0f*thickness, color, "west"); //left |
		for(int i = 0; i < 4; i++) {
			this.addActor(lines[i]);
		}
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
}