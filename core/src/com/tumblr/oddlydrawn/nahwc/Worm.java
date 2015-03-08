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

import com.badlogic.gdx.math.Rectangle;

public class Worm {
	private final int UP = 1;
	private final int RIGHT = 2;
	private final int DOWN = 3;
	private final int LEFT = 4;
	private final int WORM_MAX_SIZE = 2000;
	private final float SIZE;
	private Vector2Marked[] allBody = new Vector2Marked[WORM_MAX_SIZE];
	private float tmpX;
	private float tmpY;
	private int dir;
	private int tileWidth;
	private int tileHeight;
	private int bodyLength;
	private int score;
	private int originalLength;

	public Worm (Rectangle bounds, int wormLength) {
		SIZE = bounds.width;
		dir = UP;
		tmpX = bounds.x;
		tmpY = bounds.y;
		for (int i = 0; i < WORM_MAX_SIZE; i++) {
			allBody[i] = new Vector2Marked(tmpX, tmpY);
		}
		bodyLength = wormLength;
		updateBody();
		originalLength = wormLength;
		tileWidth = Level.TILES_WIDTH;
		tileHeight = Level.TILES_HEIGHT;
	}

	public void update () {
		score = bodyLength - originalLength;
		// Moves the body.
		updateBody();

		// Moves the head. [0] is the head.
		tmpX = allBody[0].x;
		tmpY = allBody[0].y;
		if (dir == UP) {
			allBody[0].y = tmpY + SIZE;
			// If it's out of bounds, wrap around.
			if (allBody[0].y + SIZE > tileHeight * SIZE - SIZE * 2) {
				tmpY = 0;
				allBody[0].y = tmpY;
			}
		} else if (dir == RIGHT) {
			allBody[0].x = tmpX + SIZE;
			// If it's out of bounds, wrap around.
			if (allBody[0].x + SIZE > tileWidth * SIZE) {
				tmpX = 0;
				allBody[0].x = tmpX;
			}
		} else if (dir == DOWN) {
			allBody[0].y = tmpY - SIZE;
			// If it's out of bounds, wrap around.
			if (allBody[0].y < 0) {
				tmpY = tileHeight * SIZE - SIZE * 3;
				allBody[0].y = tmpY;
			}
		} else {
			allBody[0].x = tmpX - SIZE;
			// If it's out of bounds, wrap around.
			if (allBody[0].x < 0) {
				tmpX = tileWidth * SIZE - SIZE;
				allBody[0].x = tmpX;
			}
		}
	}

	// Works from the tail to the head, gives last tail bit second-to-the-last's
	// position, and gives second-to-the-last tail bit third-to-the-last's
	// position and pulls itself up like so forever,
	// until it reaches this.update(), which contains the update for the head
	private void updateBody () {
		for (int i = bodyLength; i > 0; i--) {
			tmpX = allBody[i - 1].x;
			tmpY = allBody[i - 1].y;
			// Does the same thing with marked positions, so the animation/grow
			// moves down the entire length of the body.
			if (allBody[i - 1].getMarked() == true) {
				allBody[i].setMarked();
				allBody[i - 1].removeMarked();
			}
			allBody[i].x = tmpX;
			allBody[i].y = tmpY;
		}
	}

	public void turnRight () {
		dir++;
		if (dir > LEFT) dir = UP;
	}

	public void turnLeft () {
		dir--;
		if (dir < UP) dir = LEFT;
	}

	public void setPos (Vector2Marked pos) {
		tmpX = pos.x;
		tmpY = pos.y;
		allBody[0].set(tmpX, tmpY);
	}

	public void bodyPlusPlus () {
		allBody[bodyLength + 1].x = allBody[bodyLength].x;
		allBody[bodyLength + 1].y = allBody[bodyLength].y;
		// Since marked position moves down the entire body, it moves it down to
		// one square larger than the worm to remove it, this removes that or
		// there will be animation when the next body bit appears
		allBody[bodyLength].removeMarked();
		bodyLength++;
	}

	public void markHead () {
		// Marks the head for animation - growing/shrinking bit
		allBody[0].setMarked();
	}

	public Vector2Marked[] getAllBody () {
		return allBody;
	}

	public Vector2Marked getBodySegment (int segment) {
		return allBody[segment];
	}

	public Vector2Marked getHead () {
		return allBody[0];
	}

	public int getHeadIntX () {
		return (int)allBody[0].x;
	}

	public int getHeadIntY () {
		return (int)allBody[0].y;
	}

	public int getBodyLength () {
		return bodyLength;
	}

	public int getScore () {
		return score;
	}
}
