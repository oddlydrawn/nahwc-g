
package com.tumblr.oddlydrawn.nahwc;

import com.badlogic.gdx.Game;
import com.tumblr.oddlydrawn.nahwc.screens.MainMenuScreen;

public class MyGdxGame extends Game {

	@Override
	public void create () {
		setScreen(new MainMenuScreen(this));
	}
}
