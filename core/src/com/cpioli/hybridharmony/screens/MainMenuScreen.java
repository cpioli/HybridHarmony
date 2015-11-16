package com.cpioli.hybridharmony.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cpioli.hybridharmony.Assets;
import com.cpioli.hybridharmony.Scene2DUI;

public class MainMenuScreen implements ReusableScreen {
	
	Scene2DUI game;
	Skin skin;
	Stage stage;
	Table root;
	public boolean isInitialized;
	
	ImageButton startGameButton;
	ImageButton optionsButton;
	ImageButton tutorialButton;
	ImageButton creditsButton;
	
	public MainMenuScreen(Scene2DUI game){
		this.game = game;
		isInitialized = false;
	}
	
	public void show() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		System.out.println(Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight());
		Gdx.input.setInputProcessor(stage);
		
		startGameButton = Assets.startGameButton;
		optionsButton = Assets.optionsButton;
		tutorialButton = Assets.tutorialButton;
		creditsButton = Assets.creditsButton;		
		
		root = new Table();
		stage.addActor(root);

		root.setClip(true);
		root.size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		root.setX(0.0f);
		root.setY(0.0f);
		
		root.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Button clicked at location " + x + ", " + y);
			}
		});
		
		startGameButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Starting the game...");
				game.setScreen((ReusableScreen)game.gameScreen);
			}
		});
		
		tutorialButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				//game.setScreen((ReusableScreen)game.submissionTweenScreen);
			}
		});
		
		optionsButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Options Button");
			}
		});
		
		creditsButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Credits Button");
			}
		});
		
		root.debug();
		root.row();
		root.add(startGameButton).pad(12.8f, 112.0f, 12.8f, 0.0f);
		root.row();
		root.add(creditsButton).expand().pad(0.0f, 112.0f, 12.8f, 0.0f);
		root.row();
		root.add(tutorialButton).expand().pad(0.0f, 112.0f, 12.8f, 0.0f);
		root.row();
		root.add(optionsButton).expand().pad(0.0f, 112.0f, 12.8f, 0.0f);
		root.pack();
		isInitialized = true;
	}
	
	public void dispose() {
		stage.dispose();
		
	}

	public void hide() {
		//use this to dispose of the visual objects for the screen, such as the spriteBatches
		//but you will not dispose of anything else - data which is not displayed.
		//only run in the game object
		root.setVisible(false);
		Gdx.input.setInputProcessor(null);
	}

	public void pause() {
		
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); //without act, all images stay in place
		stage.draw(); //without draw, no images get rendered and no actions can be taken on input
		Table.drawDebug(stage);
	}

	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	public void resume() {
		root.setVisible(true);
		Gdx.input.setInputProcessor(this.stage);
		
	}

	public boolean isInitialized() {
		return isInitialized;
	}
}