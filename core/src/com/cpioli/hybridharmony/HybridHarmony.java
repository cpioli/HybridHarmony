package com.cpioli.hybridharmony;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.cpioli.hybridharmony.screens.GameScreen;
import com.cpioli.hybridharmony.screens.MainMenuScreen;
import com.cpioli.hybridharmony.screens.ReusableScreen;

public class HybridHarmony extends Game {
	public MainMenuScreen mainMenuScreen;
	InputProcessor input;
	public GameScreen gameScreen;
	private ReusableScreen screen;
	private float accum;
	
	@Override
	public void create () {
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);
		//submissionTweenScreen = new SubmissionTweenScreen(this);
		//Gdx.input.setInputProcessor(input);
		Assets.loadBig();
		Assets.initMainMenuItems();
		this.screen = mainMenuScreen;
		screen.show();
		accum = 0.0f;
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.screen.render(Gdx.graphics.getDeltaTime());
	}

	public void setScreen(ReusableScreen newScreen) {
		if(newScreen != null) {
			screen.hide();
			screen = newScreen;
			if(!screen.isInitialized()) {
				screen.show();
			} else {
				screen.resume();
				System.out.println("Resuming!");
			}
		} else {
			System.out.println("The screen has not been allocated yet.");
		}
	}
}