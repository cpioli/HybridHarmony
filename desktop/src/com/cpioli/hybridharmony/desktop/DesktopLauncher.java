package com.cpioli.hybridharmony.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cpioli.hybridharmony.HybridHarmony;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "cpioliGame";
		config.width = 960;
		config.height = 540;
		new LwjglApplication(new HybridHarmony(), config);
	}
}
