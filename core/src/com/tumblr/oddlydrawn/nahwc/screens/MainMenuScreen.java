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

package com.tumblr.oddlydrawn.nahwc.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/** @author oddlydrawn */
public class MainMenuScreen implements Screen {
	// FIXME I should play with windows instead of tables.
	// Until then, spaces fix table alignment
	final int WIDTH = 480;
	final int HEIGHT = 320;
	final String LABEL_FASTER = "Faster?";
	final String LABEL_COLOR = "Color?";
	final String LABEL_SOUND = "Sound?";
	final String LABEL_ANIMATE = "Animate?";
	final String LABEL_OUTLINE = "Outline?";
	final String LABEL_PERM_OUTLINE = "Perm Outline?";
	final String LABEL_LEVEL_SELECT = "Level:";
	final String LABEL_FASTER_SELECT = "Faster Speed:";
	final String FINE_PRINT = "data/font/fine_print.fnt";
	Preferences prefs;
	OrthographicCamera cam;
	SpriteBatch batch;
	Game game;
	Sprite titleSprite;
	Stage stage;
	Skin skin;
	TextureAtlas atlas;
	boolean isFaster = false;
	boolean isColor = false;
	boolean isSound = false;
	boolean isAnimate = false;
	boolean isOutline = false;
	boolean isPermOutline = false;
	int levelNumber = 0;
	int fasterSpeed = 0;

	public MainMenuScreen (Game g) {
		cam = new OrthographicCamera(480, 320);
		game = g;
		batch = new SpriteBatch();
		titleSprite = new Sprite();
		stage = new Stage();
		skin = new Skin();
		atlas = new TextureAtlas(Gdx.files.internal("data/pack.atlas"));
		AtlasRegion checked = atlas.findRegion("checked");
		AtlasRegion unchecked = atlas.findRegion("unchecked");
		AtlasRegion background = atlas.findRegion("background");
		AtlasRegion knob = atlas.findRegion("knob");
		titleSprite = atlas.createSprite("nahwc_title");
		titleSprite.setX(-128);
		titleSprite.setY(80);
		batch.setProjectionMatrix(cam.combined);

		Gdx.input.setInputProcessor(stage);
		stage.setViewport(new StretchViewport(WIDTH, HEIGHT));

		Table table = new Table();
		table.setFillParent(true);
		table.align(Align.left);
		stage.addActor(table);

		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.LIGHT_GRAY);
		pixmap.fill();
		skin.add("grey", new Texture(pixmap));
		skin.add("default", new BitmapFont(Gdx.files.internal(FINE_PRINT)));

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("default");
		skin.add("default", labelStyle);

		CheckBoxStyle checkBoxStyle = new CheckBoxStyle();
		checkBoxStyle.checkboxOff = skin.newDrawable("grey", Color.LIGHT_GRAY);
		checkBoxStyle.checkboxOn = skin.newDrawable("grey", Color.LIGHT_GRAY);
		checkBoxStyle.font = skin.getFont("default");
		checkBoxStyle.checkboxOff = new TextureRegionDrawable(unchecked);
		checkBoxStyle.checkboxOn = new TextureRegionDrawable(checked);
		skin.add("default", checkBoxStyle);

		SliderStyle sliderStyle = new SliderStyle();
		sliderStyle.background = new TextureRegionDrawable(background);
		sliderStyle.knob = new TextureRegionDrawable(knob);
		skin.add("default-horizontal", sliderStyle);

		ButtonStyle buttonStyle = new ButtonStyle();
		skin.add("default", buttonStyle);

		final CheckBox faster = new CheckBox(LABEL_FASTER, skin);
		table.add(faster).align(Align.left);

		table.row();
		final CheckBox color = new CheckBox(LABEL_COLOR, skin);
		table.add(color).align(Align.left);

		table.row();
		final CheckBox animate = new CheckBox(LABEL_ANIMATE, skin);
		table.add(animate).align(Align.left);

		table.row();
		final CheckBox sound = new CheckBox(LABEL_SOUND, skin);
		table.add(sound).align(Align.left);
		table.setPosition(210, 30);

		table.row();
		final CheckBox outline = new CheckBox(LABEL_OUTLINE, skin);
		table.add(outline).align(Align.left);

		table.row();
		final CheckBox permOutline = new CheckBox(LABEL_PERM_OUTLINE, skin);
		table.add(permOutline).align(Align.left);

		Label fasterLabel = new Label(LABEL_FASTER_SELECT, skin);
		fasterLabel.setPosition(240 - fasterLabel.getWidth() / 2, 115);
		stage.addActor(fasterLabel);

		final Slider fasterSlider = new Slider(0, 5, 1, false, skin);
		fasterSlider.setWidth(outline.getWidth());
		fasterSlider.setPosition(240 - fasterSlider.getWidth() / 2, 100);
		stage.addActor(fasterSlider);

		Label levelLabel = new Label(LABEL_LEVEL_SELECT, skin);
		levelLabel.setPosition(240 - levelLabel.getWidth() / 2, 85);
		stage.addActor(levelLabel);

		final Slider levelSlider = new Slider(0, 5, 1, false, skin);
		levelSlider.setWidth(outline.getWidth());
		levelSlider.setPosition(240 - levelSlider.getWidth() / 2, 70);
		stage.addActor(levelSlider);

		table.row();
		Button start = new Button(skin);
		start.add("Start");
		start.setPosition(240, 50);
		stage.addActor(start);

		Button license = new Button(skin);
		license.add("License");
		license.setPosition(240, 20);
		stage.addActor(license);

		fasterLabel.setY(215);
		fasterSlider.setY(200);

		levelLabel.setY(185);
		levelSlider.setY(170);
		table.setPosition(210, -40);

		// Loads prefString, if they exist, or creates it
		String prefString;
		FileHandle handle;
		if (Gdx.files.local("nahwc-prefs.txt").exists()) {
			handle = Gdx.files.local("nahwc-prefs.txt");
		} else {
			handle = Gdx.files.local("nahwc-prefs.txt");
			prefString = "00000000";
			handle.writeString(prefString, false);
		}
		prefString = handle.readString();

		// This is only useful for people that had an old version of the game
		if (prefString.length() < 8) {
			prefString = "00000000";
		}

		char one = '1';
		isFaster = (one == prefString.charAt(0));
		isColor = (one == prefString.charAt(1));
		isAnimate = (one == prefString.charAt(2));
		isSound = (one == prefString.charAt(3));
		isOutline = (one == prefString.charAt(4));
		isPermOutline = (one == prefString.charAt(5));
		levelNumber = Character.getNumericValue(prefString.charAt(6));
		fasterSpeed = Character.getNumericValue(prefString.charAt(7));

		// If preferences were set, this ticks the checkboxes and sets the sliders
		// to what they were saved
		faster.setChecked(isFaster);
		color.setChecked(isColor);
		animate.setChecked(isAnimate);
		sound.setChecked(isSound);
		outline.setChecked(isOutline);
		permOutline.setChecked(isPermOutline);
		permOutline.setVisible(isOutline);
		levelSlider.setValue(levelNumber);
		fasterSlider.setValue(fasterSpeed);

		faster.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				isFaster = faster.isChecked();
			}
		});
		color.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				isColor = color.isChecked();
			}
		});
		animate.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				isAnimate = animate.isChecked();
			}
		});
		sound.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				isSound = sound.isChecked();
			}
		});
		outline.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				isOutline = outline.isChecked();

				// Hides the permanent outline option if they don't want outlines.
				permOutline.setVisible(isOutline);
				if (isOutline == false) {
					isPermOutline = false;
					permOutline.setChecked(false);
				}
			}
		});
		permOutline.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				isPermOutline = permOutline.isChecked();
			}
		});
		start.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				dispose();
				game.setScreen(new LoadingScreen(game));
			}
		});
		license.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				dispose();
				game.setScreen(new LicenseScreen(game));
			}
		});
		levelSlider.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				levelNumber = (int)levelSlider.getValue();
			}
		});
		fasterSlider.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				fasterSpeed = (int)fasterSlider.getValue();
			}
		});
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		titleSprite.draw(batch);
		batch.end();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize (int width, int height) {
	    stage.getViewport().update(width, height, true);
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
		// Creates a string full of preferences.
		String prefString = "";
		FileHandle handle = Gdx.files.local("nahwc-prefs.txt");
		if (isFaster) {
			prefString += "1";
		} else {
			prefString += "0";
		}
		if (isColor) {
			prefString += "1";
		} else {
			prefString += "0";
		}
		if (isAnimate) {
			prefString += "1";
		} else {
			prefString += "0";
		}
		if (isSound) {
			prefString += "1";
		} else {
			prefString += "0";
		}
		if (isOutline) {
			prefString += "1";
		} else {
			prefString += "0";
		}
		if (isPermOutline) {
			prefString += "1";
		} else {
			prefString += "0";
		}

		String s;
		s = String.valueOf(levelNumber);
		prefString += s;
		s = String.valueOf(fasterSpeed);
		prefString += s;

		// Saves said preference string.
		handle.writeString(prefString, false);
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
		atlas.dispose();
		skin.dispose();
	}
}
