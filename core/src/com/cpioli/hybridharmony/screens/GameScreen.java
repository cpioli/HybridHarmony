package com.cpioli.hybridharmony.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cpioli.hybridharmony.Assets;
import com.cpioli.hybridharmony.GameService;
import com.cpioli.hybridharmony.hybridharmony;
import com.cpioli.hybridharmony.playfield.Playfield;
import com.cpioli.hybridharmony.playfield.Selection;
import com.cpioli.hybridharmony.playfield.SelectionVisualizer;
import com.cpioli.hybridharmony.spectrum.GrayscaleSpectrum;
import com.cpioli.hybridharmony.spectrum.Spectrum;
import com.cpioli.hybridharmony.statistics.Beaker;
import com.cpioli.hybridharmony.statistics.ContentsQualityGauge;
import com.cpioli.hybridharmony.statistics.GameOverObserver;
import com.cpioli.hybridharmony.statistics.Statistics;
import com.cpioli.hybridharmony.utilities.Astar;

public class GameScreen implements ReusableScreen, GameOverObserver {

	hybridharmony game;
	Stage stage;
	Table root;
	Overlay pauseOverlay;
	Overlay gameOverOverlay;
	public boolean isInitialized;
	public long frames;
	Playfield playfield;
	SelectionVisualizer selectionViz;
	Selection selection;
	Statistics statistics;
	Image beakerImg;
	Beaker beaker;
	ContentsQualityGauge cqg;
	public boolean gameOver;
	public boolean paused;
	private Image background;
	
	public GameScreen(hybridharmony game){
		this.game = game;
		isInitialized = false;
		frames = 0;
	}
	
	public void show() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Assets.initBigGameScreenItems();
		Assets.initBigOverlayItems();
		createOverlays();
		Gdx.input.setInputProcessor(stage);
		gameOver = false;
		paused = false;
		Gdx.input.setCatchMenuKey(true);
		GameService.INSTANCE.initialize();
		//selectionViz = new SelectionVisualizer();
		background = Assets.bigBackground;
		background.setSize(500.0f, 500.0f);
		background.setPosition(15.0f, 15.0f);
		selection = new Selection();

		playfield = new Playfield(selection);
		playfield.setTouchable(Touchable.enabled);
		playfield.registerObserver(GameService.INSTANCE);
		
		statistics = new Statistics(selection, playfield);
		statistics.setBounds(540.0f, 10.0f, 410.0f, 520.0f);
		statistics.initializeGameOverCondition(this);
		
		GameService.INSTANCE.addPlayfield(playfield);
		GameService.INSTANCE.addStatistics(statistics);
		

		root = new Table();
		stage.addActor(root);
		stage.addActor(background);
		stage.addActor(statistics);
		stage.addActor(playfield);
		stage.addActor(pauseOverlay);
		stage.addActor(gameOverOverlay);
		
		root.setClip(true);
		root.size(960, 540);
		root.setX(0.0f); 
		root.setY(0.0f); 
		
		root.debug();
		root.pack();
		
		final GameScreen currentScreen = this;
		stage.getRoot().addCaptureListener(new InputListener(){
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if(keycode == Input.Keys.P  || keycode == Input.Keys.MENU) {
					if(!currentScreen.paused) {
						currentScreen.pause();
					} else {
						currentScreen.resumeGame();
					}
				}
				return true;
			}
		});
		playfield.displayCellsOnField(0.5f, 0.0f);
		isInitialized = true;
	}
	
	public void dispose() {
		stage.dispose();
		Assets.disposeBigOverlayItems();
		Assets.disposeBigGameScreenItems();
	}

	public void hide() {
		root.setVisible(false);
		Gdx.input.setInputProcessor(null);
	}

	public void pause() {
		pauseGame();
	}

	public void render(float arg0) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
		frames++;
		
		//System.out.println("Frame " + frames + ":");
	}

	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * This method commands the stage and all its Groups/Actors to pause
	 * It will also display the pause Overlay once that is completed.
	 */
	public void pauseGame() {
		playfield.pauseGame();
		statistics.pauseGame();
		pauseOverlay.display();
		paused = true;

		System.out.println("PAUSING GAME!");
	}
	
	public void resume() {
		// TODO Auto-generated method stub
		root.setVisible(true);
		Gdx.input.setInputProcessor(this.stage);
		resumeGame();
	}

	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return isInitialized;
	}

	public void declareGameOver() {
		this.gameOver = true;
		playfield.pauseGame();
		statistics.pauseGame();
		gameOverOverlay.display();
		
	}
	
	/**
	 * Restarts all elements in the game
	 */
	public void reset() {
		playfield.resetGame();
		statistics.resetGame();
		playfield.resumeGame();
		statistics.resumeGame();
	}

	
	/**
	 * This method will command the pause overlay to depart, and focus returns on the game
	 */
	public void resumeGame() {
		pauseOverlay.sendAway(this);
		playfield.resumeGame();
		statistics.resumeGame();
		paused = false;
		System.out.println("UNPAUSING GAME!");
	}
	
	public void createOverlays() {
		pauseOverlay = new Overlay(250.0f, 200.0f, 460.0f, 250.0f, "Paused", Color.BLACK);
		final GameScreen screen = this;
		ImageButton pauseMainMenuButton = Assets.bigPauseMainMenuButton;
		pauseMainMenuButton.setPosition(100.0f, 10.0f);
		pauseMainMenuButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("MainMenu button clicked at location " + x + ", " + y);
				screen.pauseOverlay.sendAway();
				screen.game.setScreen(screen.game.mainMenuScreen);
				screen.paused = false;
			}
		});
		
		ImageButton resumeButton = Assets.bigResumeButton;
		resumeButton.setPosition(100.0f, 80.0f);
		resumeButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Resume Button clicked at location " + x + ", " + y);
				screen.resumeGame();
			}
		});
		pauseOverlay.addActor(pauseMainMenuButton);
		pauseOverlay.addActor(resumeButton);
		
		gameOverOverlay = new Overlay(250.0f, 200.0f, 460.0f, 250.0f, "Game Over", Color.BLACK);
		ImageButton gameOverMainMenuButton = Assets.bigGameOverMainMenuButton;
		gameOverMainMenuButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.gameOverOverlay.sendAway();
				screen.game.setScreen(screen.game.mainMenuScreen);
				screen.reset();
			}
		});
		gameOverOverlay.addActor(gameOverMainMenuButton);
		ImageButton restartButton = Assets.bigRestartButton;
		restartButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.gameOverOverlay.sendAway();
				screen.reset();
			}
		});
		restartButton.setPosition(100.0f, 10.0f);
		gameOverMainMenuButton.setPosition(100.0f, 80.0f);
		gameOverOverlay.addActor(gameOverMainMenuButton);
		gameOverOverlay.addActor(restartButton);
	}
}
