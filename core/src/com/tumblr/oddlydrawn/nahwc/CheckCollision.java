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

/** @author oddlydrawn */
public class CheckCollision {
	final float SIZE = 8;
	Worm worm;
	Food food;
	Rectangle head;
	Level level;
	Rectangle tmpRect;
	Rectangle body;
	int[][] levelArray;
	int tmpX;
	int tmpY;

	public CheckCollision (Food food, Worm worm, Level level) {
		this.food = food;
		this.worm = worm;
		this.level = level;
		tmpRect = new Rectangle();
		setWorm(worm);
		setLevel(level);
	}

	public boolean wormAndWorm () {
		updateHead();
		for (int i = 1; i < worm.getBodyLength(); i++) {
			body.x = worm.getAllBody()[i].x;
			body.y = worm.getAllBody()[i].y;
			body.width = SIZE;
			body.height = SIZE;
			if (head.overlaps(body)) return true;
		}
		return false;
	}

	public boolean wormAndWall () {
		tmpX = (int)worm.getAllBody()[0].x;
		tmpY = (int)worm.getAllBody()[0].y;
		if (level.isWallAt(tmpX, tmpY)) return true;
		return false;
	}

	public boolean wormAndFood () {
		updateHead();
		for (int i = 0; i < food.getRectangles().size(); i++) {
			if (food.getRectangles().get(i).overlaps(head)) {
				food.removeOne(i);
				return true;
			}
		}
		return false;
	}

	public boolean thisAndAll (Rectangle testRect) {
		for (int i = 0; i < food.getRectangles().size(); i++) {
			if (food.getRectangles().get(i).overlaps(testRect)) return true;
		}
		tmpX = (int)testRect.x;
		tmpY = (int)testRect.y;
		tmpX /= level.SIZE;
		tmpY /= level.SIZE;
		if (levelArray[tmpX][tmpY] == level.WALL) return true;

		for (int i = 0; i < worm.getBodyLength(); i++) {
			tmpRect.x = worm.getAllBody()[i].x;
			tmpRect.y = worm.getAllBody()[i].y;
			tmpRect.width = level.SIZE;
			tmpRect.height = level.SIZE;
			if (testRect.overlaps(tmpRect)) return true;
		}
		return false;
	}

	private void updateHead () {
		head.set(worm.getAllBody()[0].x, worm.getAllBody()[0].y, SIZE, SIZE);
	}

	public void setWorm (Worm w) {
		head = new Rectangle(w.getAllBody()[0].x, w.getAllBody()[0].y, SIZE, SIZE);
		body = new Rectangle();
	}

	public void setLevel (Level l) {
		levelArray = l.getLevelArray();
	}
}
