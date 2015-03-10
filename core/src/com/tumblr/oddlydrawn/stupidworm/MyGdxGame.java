
package com.tumblr.oddlydrawn.stupidworm;

import com.badlogic.gdx.Game;
import com.tumblr.oddlydrawn.stupidworm.screens.MainMenuScreen;

public class MyGdxGame extends Game {

	@Override
	public void create () {
		setScreen(new MainMenuScreen(this));
	}
}
