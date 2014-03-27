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

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Renderer {
	private final String FONT_LOC = "data/font/dfont.fnt";
	private final String SCORE = "Score: ";
	private final String HI_SCORE = "HiScore: ";
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera cam;
	private Random random;
	private Rectangle oneFood;
	private Color color;
	private Worm worm;
	private Food food;
	private Level level;
	private Rectangle rect;
	private Vector2Marked[] wholeWorm;
	private SpriteBatch batch;
	private BitmapFont font;
	private String tmpString;
	private float drawOffset;
	private float r = 1;
	private float g = 255;
	private float b = 255;
	private int[][] levelArray;
	private int score;
	private int hiScore;
	private int scoreHeight;
	private int hiScoreWidth;
	private int scoreNumberWidth;
	private int scoreHiNumberWidth;
	private boolean filled = true;

	public Renderer (OrthographicCamera cam, Worm worm, Food food, Level level) {
		font = new BitmapFont(Gdx.files.internal(FONT_LOC));
		this.cam = cam;
		this.worm = worm;
		wholeWorm = worm.getAllBody();
		this.food = food;
		this.level = level;
		levelArray = level.getLevelArray();
		shapeRenderer = new ShapeRenderer();
		random = new Random();
		color = new Color();
		color.r = Color.WHITE.r;
		color.g = Color.WHITE.g;
		color.b = Color.WHITE.b;
		color.a = Color.WHITE.a;
		rect = new Rectangle();
		batch = new SpriteBatch();
	}

	public void update (float animSize) {
		cam.update();
		shapeRenderer.setProjectionMatrix(cam.combined);
		if (filled) {
			shapeRenderer.begin(ShapeType.Filled);
		} else {
			shapeRenderer.begin(ShapeType.Line);
		}
		renderWorld();
		renderWorm(animSize);
		renderFood();
		shapeRenderer.end();

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		renderTextUI();
		batch.end();
	}

	private void renderWorld () {
		// walls
		shapeRenderer.setColor(Color.GRAY);
		for (int y = level.TILES_HEIGHT - 1; y >= 0; y--) {
			for (int x = 0; x < level.TILES_WIDTH; x++) {
				if (levelArray[x][y] == level.WALL) {
					rect.x = x * level.SIZE;
					rect.y = y * level.SIZE;
					rect.width = level.SIZE;
					rect.height = level.SIZE;
					shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
				}
			}
		}
	}

	private void renderFood () {
		// food
		shapeRenderer.setColor(Color.WHITE);
		for (int i = 0; i < food.getRectangles().size(); i++) {
			oneFood = food.getRectangles().get(i);
			shapeRenderer.rect(oneFood.x, oneFood.y, oneFood.width, oneFood.height);
		}
	}

	private void renderWorm (float animSize) {
		shapeRenderer.setColor(color);
		// XXX I set up the problem wrong, though, and I subtract 8 to fix it
		drawOffset = 0.5f * animSize + 4f;
		drawOffset -= 8;
		for (int i = 0; i < worm.getBodyLength(); i++) {
			if (wholeWorm[i].getMarked() == true) {
				rect.x = wholeWorm[i].x - drawOffset;
				rect.y = wholeWorm[i].y - drawOffset;
				rect.height = animSize;
				rect.width = animSize;
			} else {
				rect.x = wholeWorm[i].x;
				rect.y = wholeWorm[i].y;
				rect.height = 8;
				rect.width = 8;
			}
			shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		}
	}

	private void renderTextUI () {
		// Draws regular Score.
		score = worm.getScore();
		tmpString = Integer.toString(score);
		font.draw(batch, SCORE, 0, scoreHeight);
		font.draw(batch, tmpString, scoreNumberWidth, scoreHeight);

		// Draws HiScore.
		tmpString = Integer.toString(hiScore);
		font.draw(batch, HI_SCORE, hiScoreWidth, scoreHeight);
		font.draw(batch, tmpString, scoreHiNumberWidth, scoreHeight);
	}

	public void changeColor () {
		r = random.nextFloat();
		g = random.nextFloat();
		b = random.nextFloat();
		// It might be too dark, this might fix that.
		if ((r < 0.5 && g < 0.5) && (g < 0.5 && b < 0.5)) {
			r = 0.8f;
		}
		color.r = r;
		color.g = g;
		color.b = b;
	}

	public void changeOutline () {
		if (filled) {
			filled = false;
		} else {
			filled = true;
		}
	}

	public OrthographicCamera getCam () {
		return cam;
	}

	public void setHiScore (int hi) {
		hiScore = hi;
	}

	public void init () {
		scoreHeight = 40 * level.SIZE + 2;
		scoreNumberWidth = 10 * level.SIZE;
		hiScoreWidth = 36 * level.SIZE;
		scoreHiNumberWidth = 49 * level.SIZE;
	}

	public void dispose () {
		font.dispose();
		shapeRenderer.dispose();
		batch.dispose();
	}
}
