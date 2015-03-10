package com.tumblr.oddlydrawn.nahwc.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tumblr.oddlydrawn.stupidworm.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "NAHWC";
		config.width = 480;
		config.height = 320;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
