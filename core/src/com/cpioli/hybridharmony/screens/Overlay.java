package com.cpioli.hybridharmony.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.cpioli.hybridharmony.Assets;
import com.cpioli.hybridharmony.playfield.MeshActor;
import com.cpioli.hybridharmony.statistics.Border;

public class Overlay extends Group {
	private Label title;
	private MeshActor background;
	private Border whiteBorder1;
	private Border whiteBorder2;
	private static float TWEEN_DURATION = 0.5f;
	private static Interpolation TWEEN_INTERPOLATION = Interpolation.exp10Out;
	private float screenX, screenY;
	
	public Overlay(float x, float y, float width, float height, String title, Color backgroundColor) {
		screenX = x;
		screenY = y;
		this.setBounds(961.0f, y, width, height);
		background = new MeshActor(0.0f, 0.0f, width, height, backgroundColor, title );
		whiteBorder1 = new Border(2.0f, 0.0f, 0.0f, width, height, Color.WHITE);
		whiteBorder2 = new Border(2.0f, 4.0f, 4.0f, width - 8.0f, height - 8.0f, Color.WHITE);
		this.title = new Label(title, new LabelStyle(Assets.myriadPro48, Color.WHITE));
		this.title.setPosition(100.0f, 160.0f);
		super.addActor(background);
		super.addActor(whiteBorder1);
		super.addActor(whiteBorder2);
		super.addActor(this.title);
	}
	
	public void display() {
		this.addAction(moveTo(screenX, screenY, TWEEN_DURATION, TWEEN_INTERPOLATION));
	}
	
	public void sendAway(GameScreen screen) {
		final GameScreen myScreen = screen;
		this.addAction(sequence(moveTo(961.0f, screenY, TWEEN_DURATION, TWEEN_INTERPOLATION),
				new Action() {
					@Override
					public boolean act(float delta) {
						myScreen.playfield.resumeGame();
						myScreen.statistics.resumeGame();
						return true;
					}
		}));
	}
	
	/**
	 * Immediately moves the screen back to its original location, without a tween.
	 * This is used when the "Main Menu" button is selected.
	 */
	public void sendAway() {
		this.setX(961.0f);
	}
}