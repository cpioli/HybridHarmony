package com.cpioli.hybridharmony;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.cpioli.hybridharmony.screens.GameScreen;
import com.cpioli.hybridharmony.screens.MainMenuScreen;
import com.cpioli.hybridharmony.screens.ReusableScreen;

public class hybridharmony extends Game {

	public MainMenuScreen mainMenuScreen;
	InputProcessor input;
	public GameScreen gameScreen;
//	public SubmissionTweenScreen submissionTweenScreen;
	//CreditsScreen creditsScreen;
	ScreenType currentScreen;
	private ReusableScreen screen;
	private float accum;
	
	
	public void create() {
		// TODO Auto-generated method stub
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
	
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		accum += Gdx.graphics.getDeltaTime();
		this.screen.render(accum);
		
	}
}