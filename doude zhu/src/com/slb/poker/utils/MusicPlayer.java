package com.slb.poker.utils;


import com.slb.poker.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class MusicPlayer {
	private static MusicPlayer musicPlayer = new MusicPlayer();
	SoundPool soundPool=null;
	private MusicPlayer() {
		soundPool = new  SoundPool(100,AudioManager.STREAM_MUSIC,1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(final SoundPool paramSoundPool, final int sampleId,
					int status) {
				backgroundStreamId = soundPool.play(backgroundId,volume,volume,1,-1,1);
			}
		});
	}
	float volume = 0.5f;
	public static MusicPlayer getInstance(){
		return musicPlayer;
	}
	Context context = null;
	public MusicPlayer load(Context context){
		if(this.context==null){//check weather load before.
			this.context = context;
		}
		return this;
	}
	boolean backgroundLoaded = false;
	int backgroundId = 0;
	int backgroundStreamId = -1;
	boolean pausedAlreadyRunning = true;
	boolean startAlreadRunning =false;
	public MusicPlayer start(){
		if(!startAlreadRunning){
			startAlreadRunning = true;
			pausedAlreadyRunning = false;
			if(backgroundId!=-1){
				backgroundId = soundPool.load(context,R.raw.background,1);
			}else
				soundPool.resume(backgroundStreamId);
		}
		return this;
	}
	public MusicPlayer pause(){
		if(!pausedAlreadyRunning){
			pausedAlreadyRunning = true;
			startAlreadRunning = false;
			soundPool.pause(backgroundStreamId);
		}
		return this;
	}
	float max_volumn = 1.0f;
	float min_volumn = 0.0f;
	/**
	 * get the stream percent,(0.0-1.0)
	 * @return
	 */
	public float getStreamPercent(){
		return (volume-min_volumn)/max_volumn;
	}
	/**
	 * @param percent float the percent of volume.the value is [0.0,1.0]
	 */
	public void setStreamVolume(float volume){
		this.volume = volume;
		Util.saveData(context, Property.MUSIC_VOLUME_NAME, volume+"");
	}
}
