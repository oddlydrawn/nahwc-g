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
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.tumblr.oddlydrawn.nahwc.SavedStuff;

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
	private final String FONT_LOC = "data/font/dfont.fnt";
	Preferences prefs;
	OrthographicCamera cam;
	SpriteBatch batch;
	Game game;
	Sprite titleSprite;
	TextureRegion levelOnePreviewRegion;
	TextureRegion levelTwoPreviewRegion;
	TextureRegion levelThreePreviewRegion;
	TextureRegion levelFourPreviewRegion;
	TextureRegion levelFivePreviewRegion;
	Stage stage;
	Skin skin;
	TextureAtlas atlas;
	private SavedStuff savedStuff;
	boolean isFaster = false;
	boolean isColor = false;
	boolean isSound = false;
	boolean isAnimate = false;
	boolean isOutline = false;
	boolean isPermOutline = false;
	int levelNumber = 0;
	int fasterSpeed = 0;
	private final String HI_SCORE_STRING = "HiScore: ";
	final String TEXTURE_ATLAS_LOC = "data/pack.atlas";
	final String CHECKED_REGION_STRING = "checked";
	final String UNCHECKED_REGION_STRING = "unchecked";
	final String BACKGROUN_REGION_STRING = "background";
	final String KNOB_REGION_STRING = "knob";
	final String TITLE_REGION_STRING = "nahwc_title";
	final String PATCH_BOX_REGION_STRING = "box";
	final String LEVEL_ONE_REGION_STRING = "level1";
	final String LEVEL_TWO_REGION_STRING = "level2";
	final String LEVEL_THREE_REGION_STRING = "level3";
	final String LEVEL_FOUR_REGION_STRING = "level4";
	final String LEVEL_FIVE_REGION_STRING = "level5";
	final float TITLE_SPRITE_POS_X = -128;
	final float TITLE_SPRITE_POS_Y = 80;
	private int[][] allScores;
	private BitmapFont font;
	TextureRegion levelPreviewRegion;
	StringBuilder hiScoreBuilder;
	String highScoreString;

	public MainMenuScreen (Game g) {
		hiScoreBuilder = new StringBuilder();
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		game = g;
		batch = new SpriteBatch();
		titleSprite = new Sprite();
		stage = new Stage();
		skin = new Skin();
		atlas = new TextureAtlas(Gdx.files.internal(TEXTURE_ATLAS_LOC));
		
		AtlasRegion checked = atlas.findRegion(CHECKED_REGION_STRING);
		AtlasRegion unchecked = atlas.findRegion(UNCHECKED_REGION_STRING);
		AtlasRegion background = atlas.findRegion(BACKGROUN_REGION_STRING);
		AtlasRegion knob = atlas.findRegion(KNOB_REGION_STRING);
		
		titleSprite = atlas.createSprite(TITLE_REGION_STRING);
		titleSprite.setX(TITLE_SPRITE_POS_X);
		titleSprite.setY(TITLE_SPRITE_POS_Y);
		
		levelOnePreviewRegion = new TextureRegion(atlas.findRegion(LEVEL_ONE_REGION_STRING));
		levelTwoPreviewRegion = new TextureRegion(atlas.findRegion(LEVEL_TWO_REGION_STRING));
		levelThreePreviewRegion = new TextureRegion(atlas.findRegion(LEVEL_THREE_REGION_STRING));
		levelFourPreviewRegion = new TextureRegion(atlas.findRegion(LEVEL_FOUR_REGION_STRING));
		levelFivePreviewRegion = new TextureRegion(atlas.findRegion(LEVEL_FIVE_REGION_STRING));
		
		NinePatch patchBox;
		patchBox = new NinePatch(atlas.createPatch(PATCH_BOX_REGION_STRING));

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
		font = new BitmapFont(Gdx.files.internal(FONT_LOC));

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

		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = skin.getFont("default");
		textButtonStyle.up = new NinePatchDrawable(patchBox);
		skin.add("default", textButtonStyle);

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
		TextButton start = new TextButton("Start", skin);
		start.setPosition(210, 36);
		stage.addActor(start);

		TextButton license = new TextButton("License", skin);
		license.setPosition(205, 2);
		stage.addActor(license);

		fasterLabel.setY(215);
		fasterSlider.setY(200);

		levelLabel.setY(185);
		levelSlider.setY(170);
		table.setPosition(210, -40);

		loadSavedStuff();

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

	int hiScore;
	
	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// 286, 134
		batch.begin();
		titleSprite.draw(batch);
		drawLevelPreview();
		
		hiScoreBuilder.setLength(0);
		hiScoreBuilder.append(HI_SCORE_STRING);
		if (isFaster) {
//			System.out.println(allScores[levelNumber][fasterSpeed]);	
			hiScore = allScores[levelNumber][fasterSpeed];
		} else {
//			System.out.println(allScores[SavedStuff.NUMBER_OF_LEVELS - 1][fasterSpeed]);	
			hiScore = allScores[SavedStuff.NUMBER_OF_LEVELS - 1][fasterSpeed];	
		}
		hiScoreBuilder.append(hiScore);
		highScoreString = hiScoreBuilder.toString();
		font.draw(batch, highScoreString, 50, 73);
		
		batch.end();
		stage.act(delta);
		stage.draw();

	}

	public void drawLevelPreview () {
		switch (levelNumber) {
		case 1:
			batch.draw(levelOnePreviewRegion, 50, -27);
			break;
		case 2:
			batch.draw(levelTwoPreviewRegion, 50, -27);
			break;
		case 3:
			batch.draw(levelThreePreviewRegion, 50, -27);
			break;
		case 4:
			batch.draw(levelFourPreviewRegion, 50, -27);
			break;
		case 5:
			batch.draw(levelFivePreviewRegion, 50, -27);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show () {
	}
	
	public void loadSavedStuff () {
		savedStuff = new SavedStuff();
		savedStuff.loadPreferencesAndScore();
		
		levelNumber = savedStuff.getLevelNumber();
		isFaster = savedStuff.isFaster();
		isColor = savedStuff.isColor();
		isAnimate = savedStuff.isAnimate();
		isSound = savedStuff.isSound();
		isOutline = savedStuff.isOutline();
		isPermOutline = savedStuff.isPermOutline();
		levelNumber = savedStuff.getLevelNumber();
		
		savedStuff.loadAllScoresIntoArray();
		allScores = savedStuff.getAllScores();
	}

	@Override
	public void hide () {
	}
	
	public void setPreferences() {
		savedStuff.setFaster(isFaster);
		savedStuff.setColor(isColor);
		savedStuff.setAnimate(isAnimate);
		savedStuff.setSound(isSound);
		savedStuff.setOutline(isOutline);
		savedStuff.setPermOutline(isPermOutline);
		savedStuff.setFasterSpeed(fasterSpeed);
		savedStuff.setLevelNumber(levelNumber);
	}
	
	public void savePreferences() {
		savedStuff.savePreferences();
	}

	@Override
	public void pause () {
		setPreferences();
		savePreferences();
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
		font.dispose();
	}
}
