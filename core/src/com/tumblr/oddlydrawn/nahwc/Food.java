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

import java.util.ArrayList;
import com.badlogic.gdx.math.Rectangle;

public class Food {
	ArrayList<Rectangle> allFood = new ArrayList<Rectangle>();
	Rectangle food;
	int eatenIndex;

	public Food () {
		allFood = new ArrayList<Rectangle>();
	}

	public void createOne (float x, float y, float SIZE) {
		allFood.get(eatenIndex).setX(x);
		allFood.get(eatenIndex).setY(y);
		allFood.get(eatenIndex).setWidth(SIZE);
		allFood.get(eatenIndex).setHeight(SIZE);
	}

	public void createInitial (float x, float y, float SIZE) {
		food = new Rectangle(x, y, SIZE, SIZE);
		allFood.add(food);
	}

	public ArrayList<Rectangle> getFood () {
		return allFood;
	}

	public void removeOne (int index) {
		allFood.get(index).setX(-50);
		allFood.get(index).setY(0);
		eatenIndex = index;
	}

	public ArrayList<Rectangle> getRectangles () {
		return allFood;
	}

	public void update () {
		// TODO Create different colored fruits which disappear after a certain
		// amount of time then turn into regular fruits. Random chance involved

		// calculate distance, in updates, from food to fruit + random wiggle time room thing
	}
}
