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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

/** @author oddlydrawn */
public class NahwcGame {
	private final String SCORES_STRING = "scores";
	private final String NO_FAST_STRING = "noFast";
	private final String FILE_EXT = ".txt";
	private final float UPDATE_SPEED_DECREASE_ZERO = 0.0003125f; // 0.0003125f
	private final float UPDATE_SPEED_DECREASE_ONE = 0.000625f; // 0.000625f
	private final float UPDATE_SPEED_DECREASE_TWO = 0.00125f; // 0.00125f
	private final float UPDATE_SPEED_DECREASE_THREE = 0.0025f; // 0.0025f
	private final float UPDATE_SPEED_DECREASE_FOUR = 0.005f; // 0.005f
	private final float UPDATE_SPEED_DECREASE_FIVE = 0.01f; // 0.01f
	private final float SCREEN_WIDTH_PX = 480;
	private final float SCREEN_HEIGHT_PX = 320;
	private final float WORM_SIZE_PX = 80;
	private final float START_TIME = 0.85f;
	private final int WORM_LENGTH = 10; // 10
	private final int NUMBER_OF_FOOD = 5;
	private final int COLOR_MULTIPLE = 10;
	private final int OUTLINE_MULTIPLE = 20;
	private final int SCREEN_WIDTH_TILES = 59;
	private final int SCREEN_HEIGHT_TILES = 39;
	private final int TEXT_PADDING = 2;
	private float timeToUpdate = 0.2f; // 0.2f
	private OrthographicCamera cam;
	private Renderer renderer;
	private Rectangle bounds;
	private Worm worm;
	private Food food;
	private CheckCollision collision;
	private Random rnd;
	private Rectangle testRect;
	private Level level;
	private MyMusic musicPlayer;
	private String scoreString;
	private Controller controller;
	private String scoresFile;
	private float timeSinceLastUpdate;
	private float delta;
	private float startX;
	private float startY;
	private float animSize;
	private float decreaseSpeed;
	private float timeToStartGame;
	private float halfUpdate;
	private int counter;
	private int score;
	private int hiScore;
	private int tmpScore;
	private int levelNumber;
	private int fasterSpeed;
	private boolean isFaster;
	private boolean isColor;
	private boolean isAnimate;
	private boolean isSound;
	private boolean gameOver;
	private boolean isOutline;
	private boolean isPermOutline;
	private boolean startGame = false;

	public NahwcGame () {
		loadPreferences();
		loadScore();
		loadLevel();
		createObjects();
	}

	public void runGame () {
		Gdx.gl.glClearColor(0, 0, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			saveScore();
			Gdx.app.exit();
		}

		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
		timeSinceLastUpdate += delta;

		renderer.update(animSize);
		cam.update();

		// Gets current score (worm size - original worm size).
		score = worm.getScore();
		if (score > hiScore) hiScore = score;

		renderer.setHiScore(hiScore);

		if (startGame) {
			// Pauses time between worm movement.
			if (timeSinceLastUpdate > timeToUpdate) {
				controller.update();
				worm.update();
				timeSinceLastUpdate = 0;
				if (counter <= 2) counter++;
				// Checking for collisions has to happen after worm head is off body
				if (counter > 2) {
					if (collision.wormAndWall() || collision.wormAndWorm()) {
						Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
						saveScore();
						gameOver = true;
					}
				}
			}

			calculateAnimationSize();
			controller.processInput();
			checkWormAndFoodCollision();
		} else {
			timeToStartGame += delta;
			if (timeToStartGame > START_TIME) startGame = true;
		}
	}

	private void calculateAnimationSize () {
		halfUpdate = timeToUpdate / 2;
		if (timeSinceLastUpdate < halfUpdate) {
			// A formula for scale in relation to time (8 to 16 for first half
			// of a pause between worm timeToUpdate updates).
			animSize = WORM_SIZE_PX * timeSinceLastUpdate + Level.SIZE;
		} else if (timeSinceLastUpdate > halfUpdate) {
			// This shrinks the scale for the second half of the worm update.
			animSize = -WORM_SIZE_PX * timeSinceLastUpdate + Level.SIZE * 3;
		} else {
			animSize = 0;
		}
	}

	private void checkWormAndFoodCollision () {
		if (collision.wormAndFood()) {
			if (isSound) musicPlayer.playPickup();
			if (isAnimate) worm.markHead();
			worm.bodyPlusPlus();
			makeNewFood();
			score = worm.getScore();
			// XXX Score is 1 less than actual score.
			tmpScore = score + 1;
			if (isFaster) timeToUpdate -= decreaseSpeed;
			if (isColor) {
				if (tmpScore % COLOR_MULTIPLE == 0) renderer.changeColor();
			}
			if (isOutline && !isPermOutline) {
				if (tmpScore % OUTLINE_MULTIPLE == 0) renderer.changeOutline();
			}
		}
	}

	private void loadPreferences () {
		// Loads the preferences saved from the MainMenuScreen.
		String prefString;
		FileHandle prefHandle;
		if (Gdx.files.local("nahwc-prefs.txt").exists()) {
			prefHandle = Gdx.files.local("nahwc-prefs.txt");
		} else {
			prefHandle = Gdx.files.local("nahwc-prefs.txt");
			prefString = "0000";
			prefHandle.writeString(prefString, false);
		}
		prefString = prefHandle.readString();

		char one = '1';
		// True if character at string index is '1', false if '0'
		isFaster = (one == prefString.charAt(0));
		isColor = (one == prefString.charAt(1));
		isAnimate = (one == prefString.charAt(2));
		isSound = (one == prefString.charAt(3));
		isOutline = (one == prefString.charAt(4));
		isPermOutline = (one == prefString.charAt(5));
		levelNumber = Character.getNumericValue(prefString.charAt(6));
		fasterSpeed = Character.getNumericValue(prefString.charAt(7));

		if (fasterSpeed == 0) {
			decreaseSpeed = UPDATE_SPEED_DECREASE_ZERO;
		} else if (fasterSpeed == 1) {
			decreaseSpeed = UPDATE_SPEED_DECREASE_ONE;
		} else if (fasterSpeed == 2) {
			decreaseSpeed = UPDATE_SPEED_DECREASE_TWO;
		} else if (fasterSpeed == 3) {
			decreaseSpeed = UPDATE_SPEED_DECREASE_THREE;
		} else if (fasterSpeed == 4) {
			decreaseSpeed = UPDATE_SPEED_DECREASE_FOUR;
		} else if (fasterSpeed == 5) {
			decreaseSpeed = UPDATE_SPEED_DECREASE_FIVE;
		}

		// Initital speed up
		timeToUpdate -= 5 * decreaseSpeed;
	}

	private void loadScore () {
		// Creates correct filename for scores file
		FileHandle handle;
		scoresFile = SCORES_STRING;
		scoresFile += String.valueOf(levelNumber);
		if (isFaster) {
			scoresFile += String.valueOf(fasterSpeed);
		} else {
			scoresFile += NO_FAST_STRING;
		}
		scoresFile += FILE_EXT;

		// Checks if scores file exists, creates one if not
		if (Gdx.files.local(scoresFile).exists()) {
			handle = Gdx.files.local(scoresFile);
		} else {
			handle = Gdx.files.local(scoresFile);
			scoreString = Integer.toString(0);
			handle.writeString(scoreString, false);
		}

		scoreString = handle.readString();
		try {
			hiScore = Integer.parseInt(scoreString);
		} catch (NumberFormatException e) {
			hiScore = 0;
		}
	}

	private void loadLevel () {
		// Loads level based on selected level
		level = new Level(levelNumber);
		level.loadLevel();
		startX = level.getStartCoords().x;
		startY = level.getStartCoords().y;
	}

	private void createObjects () {
		bounds = new Rectangle(startX, startY, Level.SIZE, Level.SIZE);
		worm = new Worm(bounds, WORM_LENGTH);
		cam = new OrthographicCamera();
		cam.setToOrtho(false, SCREEN_WIDTH_PX, SCREEN_HEIGHT_PX);
		food = new Food();
		renderer = new Renderer(cam, worm, food, level);
		testRect = new Rectangle(0, 0, Level.SIZE, Level.SIZE);
		rnd = new Random();

		collision = new CheckCollision(food, worm, level);
		musicPlayer = new MyMusic();
		for (int i = 0; i < NUMBER_OF_FOOD; i++) {
			makeInitialFood();
		}
		renderer.init();
		controller = new Controller(worm);
		if (isPermOutline) {
			renderer.changeOutline();
		}
	}

	public void makeInitialFood () {
		do {
			testRect.x = rnd.nextInt(SCREEN_WIDTH_TILES) * Level.SIZE;
			testRect.y = rnd.nextInt(SCREEN_HEIGHT_TILES - TEXT_PADDING) * Level.SIZE;
		} while (collision.thisAndAll(testRect));
		food.createInitial(testRect.x, testRect.y, Level.SIZE);
	}

	public void makeNewFood () {
		do {
			testRect.x = rnd.nextInt(SCREEN_WIDTH_TILES) * Level.SIZE;
			testRect.y = rnd.nextInt(SCREEN_HEIGHT_TILES - TEXT_PADDING) * Level.SIZE;
		} while (collision.thisAndAll(testRect));
		food.createOne(testRect.x, testRect.y, Level.SIZE);
	}

	public void saveScore () {
		FileHandle handle = Gdx.files.local(scoresFile);
		scoreString = Integer.toString(hiScore);
		handle.writeString(scoreString, false);
	}

	public boolean getIfGameOver () {
		return gameOver;
	}

	public void dispose () {
		renderer.dispose();
		musicPlayer.dispose();
	}
}
