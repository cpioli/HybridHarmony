package com.cpioli.hybridharmony;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A class of static resources which can be accessed by any class in the
 * program. Credit goes to libgdx forum user Sako.
 * 
 * To be implemented in the future. For now, I'm just going to pass images alongst a tree.
 * @author cpioli
 *
 */
public class Assets {
	
	public static boolean mainMenuAssetsLoaded = false;
	public static boolean gameScreenAssetsLoaded = false;
	public static boolean overlayAssetsLoaded = false;
	
	/*Asset manager*/
	public static AssetManager manager;
	
	//270 x 480 resources
	private static TextureAtlas mainMenuAtlas;
	private static TextureAtlas gameScreenAtlas;
	@SuppressWarnings("unused")
	private static TextureAtlas overlayAtlas;
	private static Skin mainMenuSkin;
	private static Skin overlaySkin;
	// loads stuff via assetmanager
	
	public static ImageButton startGameButton;
	public static ImageButton optionsButton;
	public static ImageButton tutorialButton;
	public static ImageButton creditsButton;
	
	public static Image selectionArrow;
	public static Image contentsArrow;
	public static Image queueContainer;
	public static Image beaker;
	
	public static Label speedometerTitle;
	public static Label speedometerValue;
	public static Label scoreTitle;
	public static Label scoreNumber;
	public static Label cqgTitle;
	public static Label bonusTitle;
	public static Label bonusNumber;
	public static Label polarityTitle;

	
	public static ImageButton restartButton;
	public static ImageButton resumeButton;
	public static ImageButton gameOverMainMenuButton;
	public static ImageButton pauseMainMenuButton;
	
	public static BitmapFont andaleMono24;
	public static BitmapFont myriadPro24;
	
	private static LabelStyle labelStyle;
	
	//540 x 960 resources
	private static boolean bigGameScreenAssetsLoaded = false;
	private static boolean bigOverlayAssetsLoaded = false;
	private static TextureAtlas bigGameScreenAtlas;
	
	@SuppressWarnings("unused")
	private static TextureAtlas bigOverlayAtlas;
	private static Skin bigOverlaySkin;
	
	public static Image bigSelectionArrow;
	public static Image bigContentsArrow;
	public static Image bigQueueContainer;
	public static Image bigBeaker;
	public static Image bigBackground;
	public static Image bigEmptySpeedometer;
	public static Image bigClipSpeedometer;
	public static Image bigEncapsulator1;
	public static Image bigEncapsulator2;
	
	public static Label bigSpeedometerTitle;
	public static Label bigSpeedometerValue;
	public static Label bigScoreTitle;
	public static Label bigScoreNumber;
	public static Label bigCqgTitle;
	public static Label bigBonusTitle;
	public static Label bigBonusNumber;
	public static Label bigPolarityTitle;
	public static Label bigLevelTitleAndValue;

	
	public static ImageButton bigRestartButton;
	public static ImageButton bigResumeButton;
	public static ImageButton bigGameOverMainMenuButton;
	public static ImageButton bigPauseMainMenuButton;
	
	public static NinePatch background;
	
	public static BitmapFont myriadPro48;
	
	private static LabelStyle bigLabelStyle;
	
	private static void loadCommon() {
		if(manager == null) {
			manager = new AssetManager();
		}
		if(!manager.containsAsset("data/mainMenu.json")) {
			manager.load("data/mainMenu.json", Skin.class);
			System.out.println("loaded mainMenu.json");
		}
		if(!manager.containsAsset("data/overlay.json")) {
			manager.load("data/overlay.json", Skin.class);
			System.out.println("loaded overlay.json");
		}
	}
	
	public static void loadBig() {
		if(manager == null) {
			manager = new AssetManager();
		}
		
		System.out.println("We are running the loadCommon() method");
		loadCommon();
		System.out.println("We have gotten out of the loadCommon() method");
		manager.load("data/bigGameScreen.atlas", TextureAtlas.class);
		manager.load("data/bigOverlay.atlas", TextureAtlas.class);
		manager.load("data/mainMenu.atlas", TextureAtlas.class);
		manager.load("data/myriadPro48.fnt", BitmapFont.class);
		manager.load("data/myriadPro24.fnt", BitmapFont.class);
		manager.load("data/bigOverlay.json", Skin.class);
		manager.finishLoading();
		System.out.println("Finished loading the .atlas, .fnt, and .json files");
		
		myriadPro24 = manager.get("data/myriadPro24.fnt");
		myriadPro48 = manager.get("data/myriadPro48.fnt");
		bigLabelStyle = new LabelStyle(myriadPro48, Color.WHITE);
		labelStyle = new LabelStyle(myriadPro24, Color.WHITE);
		System.out.println("The program has completed the loadBig() method.");
	}
	
	public static void initBigGameScreenItems() {
		if(!bigGameScreenAssetsLoaded) {
			bigGameScreenAtlas = manager.get("data/bigGameScreen.atlas");
			bigContentsArrow = new Image(bigGameScreenAtlas.findRegion("bigArrow"));
			bigSelectionArrow = new Image(bigGameScreenAtlas.findRegion("bigSelectionArrow"));
			bigQueueContainer = new Image(bigGameScreenAtlas.findRegion("bigQueueContainer"));
			bigBeaker = new Image(bigGameScreenAtlas.findRegion("bigBeaker"));
			bigSpeedometerValue = new Label(" cpm", bigLabelStyle);
			bigSpeedometerTitle = new Label("SPEED", bigLabelStyle);
			bigScoreTitle = new Label("Score: ", bigLabelStyle);
			bigScoreNumber = new Label("0", bigLabelStyle);
			bigCqgTitle = new Label("Contents Quality:", bigLabelStyle);
			bigBonusTitle = new Label("BONUS:", bigLabelStyle);
			bigBonusNumber = new Label("", bigLabelStyle);
			bigEmptySpeedometer = new Image(bigGameScreenAtlas.findRegion("emptySpeedometer"));
			bigClipSpeedometer = new Image(bigGameScreenAtlas.findRegion("clipSpeedometer"));
			bigPolarityTitle = new Label("Polarity", bigLabelStyle);
			background = bigGameScreenAtlas.createPatch("background_default");
			bigBackground = new Image(background);
			bigEncapsulator1 = new Image(bigGameScreenAtlas.findRegion("bigEncapsulator1"));
			bigEncapsulator2 = new Image(bigGameScreenAtlas.findRegion("bigEncapsulator2"));
			bigLevelTitleAndValue = new Label("Level ", bigLabelStyle);
			bigGameScreenAssetsLoaded = true;
		}
	}
	
	public static void disposeBigGameScreenItems() {
		if(bigGameScreenAssetsLoaded) {
			bigGameScreenAtlas.dispose();
			bigGameScreenAssetsLoaded = false;
		}
	}
	
	public static void initBigOverlayItems() {
		if(!bigOverlayAssetsLoaded) {
			bigOverlayAtlas = manager.get("data/overlay.atlas");
			bigOverlaySkin = manager.get("data/bigOverlay.json");
			bigRestartButton = new ImageButton(bigOverlaySkin.get("overlayRestart", ImageButtonStyle.class));
			bigRestartButton.setName("restartbutton");
			bigResumeButton = new ImageButton(bigOverlaySkin.get("overlayResume", ImageButtonStyle.class));
			bigResumeButton.setName("resumebutton");
			bigPauseMainMenuButton = new ImageButton(bigOverlaySkin.get("overlayMainMenu", ImageButtonStyle.class));
			bigPauseMainMenuButton.setName("pausemainmenubutton");
			bigGameOverMainMenuButton = new ImageButton(bigOverlaySkin.get("overlayMainMenu", ImageButtonStyle.class));
			bigGameOverMainMenuButton.setName("gameovermainmenubutton");
			bigOverlayAssetsLoaded = true;
		}
	}
	
	public static void disposeBigOverlayItems() {
		if(bigOverlayAssetsLoaded) {
			bigOverlayAtlas.dispose();
			bigOverlaySkin.dispose();
		}
	}

	// optional: called when the manager has fully loaded; creates static references to frequently used assets
	public static void initMainMenuItems() {
		if(!mainMenuAssetsLoaded){
			mainMenuAtlas = manager.get("data/mainMenu.atlas", TextureAtlas.class);
			mainMenuSkin = manager.get("data/mainMenu.json", Skin.class);
			startGameButton = new ImageButton(mainMenuSkin.get("frMenuStartGame", ImageButtonStyle.class));
			optionsButton = new ImageButton(mainMenuSkin.get("frMenuOptions", ImageButtonStyle.class));
			tutorialButton = new ImageButton(mainMenuSkin.get("frMenuTutorial", ImageButtonStyle.class));
			creditsButton = new ImageButton(mainMenuSkin.get("frMenuCredits", ImageButtonStyle.class));
	
			mainMenuAssetsLoaded = true;
		}
	}
	
	public static void disposeMainMenuItems() {
		if(mainMenuAssetsLoaded) {
			mainMenuAtlas.dispose();
			mainMenuSkin.dispose();
			mainMenuAssetsLoaded = false;
		}
	}
	

	//OverlayItems currently for both big and regular size resolutions
	public static void initOverlayItems() {
		if(!overlayAssetsLoaded) {
			overlayAtlas = manager.get("data/overlay.atlas");
			overlaySkin = manager.get("data/overlay.json");
			restartButton = new ImageButton(overlaySkin.get("overlayRestart", ImageButtonStyle.class));
			restartButton.setName("restartbutton");
			resumeButton = new ImageButton(overlaySkin.get("overlayResume", ImageButtonStyle.class));
			resumeButton.setName("resumebutton");
			pauseMainMenuButton = new ImageButton(overlaySkin.get("overlayMainMenu", ImageButtonStyle.class));
			pauseMainMenuButton.setName("pausemainmenubutton");
			gameOverMainMenuButton = new ImageButton(overlaySkin.get("overlayMainMenu", ImageButtonStyle.class));
			gameOverMainMenuButton.setName("gameovermainmenubutton");
			overlayAssetsLoaded = true;
		}
	}
	
	public static void disposeOverlayItems() {
		if(overlayAssetsLoaded) {
			overlayAtlas.dispose();
			overlaySkin.dispose();
			overlayAssetsLoaded = false;
		}
	}
	
	public static void loadRegular() {
		
		if(manager == null) {
			manager = new AssetManager();
		}
		
		loadCommon();
		manager.load("data/mainMenu.atlas", TextureAtlas.class);
		manager.load("data/gameScreen.atlas", TextureAtlas.class);
		manager.load("data/overlay.atlas", TextureAtlas.class);
		//manager.load("data/mainMenu.json", Skin.class);
		//manager.load("data/overlay.json", Skin.class);
		manager.load("data/AndaleMonoWhite24.fnt", BitmapFont.class);
		manager.load("data/myriadPro24.fnt", BitmapFont.class);
		manager.finishLoading();
		
		andaleMono24 = manager.get("data/AndaleMonoWhite24.fnt");
		myriadPro24 = manager.get("data/myriadPro24.fnt");
		
		labelStyle = new LabelStyle(myriadPro24, Color.WHITE);
	}
	
	
	public static void initGameScreenItems() {
		if(!gameScreenAssetsLoaded) {
			gameScreenAtlas = manager.get("data/gameScreen.atlas");
			contentsArrow = new Image(gameScreenAtlas.findRegion("newArrowLarge"));
			selectionArrow = new Image(gameScreenAtlas.findRegion("newArrow"));
			queueContainer = new Image(gameScreenAtlas.findRegion("queueContainer"));
			beaker = new Image(gameScreenAtlas.findRegion("beaker"));
			speedometerValue = new Label(" cpm", labelStyle);
			speedometerTitle = new Label("SPEED", labelStyle);
			scoreTitle = new Label("Score: ", labelStyle);
			scoreNumber = new Label("0", labelStyle);
			cqgTitle = new Label("Contents Quality:", labelStyle);
			bonusTitle = new Label("BONUS:", labelStyle);
			bonusNumber = new Label("", labelStyle);
			gameScreenAssetsLoaded = true;
		}
	}
	
	public static void disposeGameScreenItems() {
		
	}
}