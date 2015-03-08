/*
 *   Copyright 2013 oddlydrawn
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.tumblr.oddlydrawn.nahwc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/** @author oddlydrawn */
public class Controller {
	final private int STRAIGHT = 1; // 1
	final private int RIGHT = 2; // 2
	final private int LEFT = 3; // 3
	Worm worm;
	int dir = STRAIGHT;
	int inputX;
	int screenWidthPx;
	boolean letGo;

	public Controller (Worm w) {
		worm = w;
		screenWidthPx = Gdx.graphics.getWidth();
		letGo = true;
	}

	public void update () {
		if (dir == LEFT) worm.turnLeft();
		if (dir == RIGHT) worm.turnRight();
		dir = STRAIGHT;
	}

	public void processInput () {
		inputX = Gdx.input.getX();

		// If half of the screen is touched or right key, same for left.
		if (Gdx.input.isKeyPressed(Keys.RIGHT) || (rightTouched())) {
			if (letGo) dir = RIGHT;
			letGo = false;
		} else if (Gdx.input.isKeyPressed(Keys.LEFT) || (leftTouched())) {
			if (letGo) dir = LEFT;
			letGo = false;
		} else {
			letGo = true;
		}
	}

	public boolean rightTouched () {
		if (Gdx.input.isTouched() && inputX > screenWidthPx / 2) return true;
		return false;
	}

	public boolean leftTouched () {
		if (Gdx.input.isTouched() && inputX <= screenWidthPx / 2) return true;
		return false;
	}
}
