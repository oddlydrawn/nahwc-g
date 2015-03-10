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

package com.tumblr.oddlydrawn.stupidworm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/** @author oddlydrawn */
public class MyMusic {
	private float volume = 1.0f;
	private final String SOUND_PICKUP_LOC = "data/sound/pickup.wav";
	Sound soundPickup;

	public MyMusic () {
		soundPickup = Gdx.audio.newSound(Gdx.files.internal(SOUND_PICKUP_LOC));
	}

	public void playPickup () {
		soundPickup.play(volume);
	}

	public void dispose () {
		soundPickup.dispose();
	}
}
