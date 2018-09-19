/**
 * 
 */
package com.app.base.service.android;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


/**
 * 
 * @author tianyu912@yeah.net
 */
public class SoundService extends AbstractAndroidService {

	private SoundPool sp;
	private int id;

	public SoundService(Activity activity) {
		super(activity);
	}

	public void initSound(Integer soundId) {
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		id = sp.load(this.activity, soundId, 1);
	}

	
	public void playSound(int number) {
		AudioManager am = (AudioManager) this.activity
				.getSystemService(Context.AUDIO_SERVICE);
		float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		float volumnRatio = volumnCurrent / audioMaxVolumn;

		sp.play(id, volumnRatio, volumnRatio, 1, number, 1f);
	}
	
	public void playSound() {
		this.playSound(1);
	}

}
