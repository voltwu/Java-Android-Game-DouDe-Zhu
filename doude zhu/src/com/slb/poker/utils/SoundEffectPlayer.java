package com.slb.poker.utils;

import com.slb.poker.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundEffectPlayer {
	private static SoundEffectPlayer soundEffectPlayer = new SoundEffectPlayer();
	SoundPool soundPool=null;
	float volume = 0.5f;
	float max_volumn = 1.0f;
	float min_volumn = 0.0f;
	boolean alarmLoaded = false;
	boolean clickLoaded = false;
	boolean yiFenNanLoaded = false;
	boolean yiFenNvLoaded = false;
	boolean erFenNanLoaded = false;
	boolean erFenNvLoaded = false;
	boolean sanFenNanLoaded = false;
	boolean sanFenNvLoaded = false;
	boolean buJiaoNanLoaded = false;
	boolean buJiaoNvLoaded = false;
	boolean sendCardsLoaded = false;
	
	int alarmId = 0;
	int clickId = 0;
	int yiFenNanId = 0;
	int yiFenNvId = 0;
	int erFenNanId = 0;
	int erFenNvId = 0;
	int sanFenNanId = 0;
	int sanFenNvId = 0;
	int buJiaoNanId = 0;
	int buJiaoNvId = 0;
	int sendCardsId = 0;
	
	public static SoundEffectPlayer getInstance(){
		return soundEffectPlayer;
	}
	private SoundEffectPlayer(){
		soundPool = new  SoundPool(100,AudioManager.STREAM_MUSIC,1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(final SoundPool paramSoundPool, final int sampleId,
					int status) {
				play(sampleId);
			}
		});
	}
	Context context = null;
	public void load(Context context){
		if(this.context==null){//check weather load before.
			this.context = context;
		}
	}
	public void playRocketEffect(){
		play(soundPool.load(context,R.raw.anim_zhadan,1));
	}
	public void playBombEffect(){
		play(soundPool.load(context,R.raw.anim_huojian,1));
	}
	public void playFeijiEffect(){
		play(soundPool.load(context,R.raw.anim_feiji,1));
	}
	public void playShunZiEffect(){
		play(soundPool.load(context,R.raw.anim_shunzi,1));
	}
	public void playLianDuiEffect(){
		play(soundPool.load(context,R.raw.anim_liandui,1));
	}
	public void playSendCards(){
		loadOrPlaySendCards();
	}
	public void playAlarm(){
		loadOrPlayAlarm();
	}
	public void playClick(){
		loadOrPlayClick();
	}
	public void playYiFenNan(){
		loadOrPlayYiFenNan();
	}
	public void playYiFenNv(){
		loadOrPlayYiFenNv();
	}
	public void playErFenNan(){
		loadOrPlayErFenNan();
	}
	public void playErFenNv(){
		loadOrPlayErFenNv();
	}
	public void playSanFenNan(){
		loadOrPlaySanFenNan();
	}
	public void playSanFenNv(){
		loadOrPlaySanFenNv();
	}
	public void playBuJiaoNan(){
		loadOrPlayBuJiaoNan();
	}
	public void playBuJiaoNv(){
		loadOrPlayBuJiaoNv();
	}
	public void playGuoEngNan() {
		loadOrPlayGuoEngNan();
	}
	public void playGuoEngNv() {
		loadOrPlayGuoEngNv();
	}	
	public float getSoundEffectVolumePercent() {
		return (volume-min_volumn)/max_volumn;
	}
	public void setSoundEffectVolumePercent(float volume){
		this.volume = volume;
		Util.saveData(context, Property.MUSIC_SOUND_EFFECT_NAME, volume+"");
	}
	public void playBuYaoNan() {
		loadOrPlayBuYaoNan();
	}
	public void playBuYaoNv() {
		loadOrPlayBuYaoNv();
	}
	public void playYaoBuQiNan() {
		loadOrPlayYaoBuQiNan();
	}
	public void playYaoBuQiNv() {
		loadOrPlayYaoBuQiNv();
	}	
	private boolean yaoBuQiNanLoaded = false;
	private int yaoBuQiId = 0;
	public void loadOrPlayYaoBuQiNan(){
		if(!yaoBuQiNanLoaded){
			yaoBuQiNanLoaded = true;
			yaoBuQiId = soundPool.load(context,R.raw.nan_yaobuqi,1);
		}else{
			play(yaoBuQiId);
		}
	}
	private boolean yaoBuQiNvLoaded = false;
	private int yaoBuQiNvId = 0;
	public void loadOrPlayYaoBuQiNv(){
		if(!yaoBuQiNvLoaded){
			yaoBuQiNvLoaded = true;
			yaoBuQiNvId = soundPool.load(context,R.raw.nv_yaobuqi,1);
		}else{
			play(yaoBuQiNvId);
		}
	}
	
	private boolean buyaoNanLoaded = false;
	private int buyaoId = 0;
	public void loadOrPlayBuYaoNan(){
		if(!buyaoNanLoaded){
			buyaoNanLoaded = true;
			buyaoId = soundPool.load(context,R.raw.nan_buyao,1);
		}else{
			play(buyaoId);
		}
	}
	private boolean buyaoNvLoaded = false;
	private int buyaoNvId = 0;
	public void loadOrPlayBuYaoNv(){
		if(!buyaoNvLoaded){
			buyaoNvLoaded = true;
			buyaoNvId = soundPool.load(context,R.raw.nv_buyao,1);
		}else{
			play(buyaoNvId);
		}
	}
	
	private boolean guoEngNanLoaded = false;
	private int guoEngNanId = 0;
	public void loadOrPlayGuoEngNan(){
		if(!guoEngNanLoaded){
			guoEngNanLoaded = true;
			guoEngNanId = soundPool.load(context,R.raw.nan_guo,1);
		}else{
			play(guoEngNanId);
		}
	}
	private boolean guoEngNvLoaded = false;
	private int guoEngNvId = 0;
	public void loadOrPlayGuoEngNv(){
		if(!guoEngNvLoaded){
			guoEngNvLoaded = true;
			guoEngNvId = soundPool.load(context,R.raw.nv_guo,1);
		}else{
			play(guoEngNvId);
		}
	}	
	
	public void loadOrPlaySendCards(){
		if(!sendCardsLoaded){
			sendCardsLoaded = true;
			sendCardsId = soundPool.load(context,R.raw.deal,1);
		}else{
			play(sendCardsId);
		}
	}
	public void loadOrPlayBuJiaoNv(){
		if(!buJiaoNvLoaded){
			buJiaoNvLoaded = true;
			buJiaoNvId = soundPool.load(context,R.raw.nv_bujiao,1);
		}else{
			play(buJiaoNvId);
		}
	}
	public void loadOrPlayBuJiaoNan(){
		if(!buJiaoNanLoaded){
			buJiaoNanLoaded = true;
			buJiaoNanId = soundPool.load(context,R.raw.nan_bujiao,1);
		}else{
			play(buJiaoNanId);
		}
	}
	
	public void loadOrPlaySanFenNv(){
		if(!sanFenNvLoaded){
			sanFenNvLoaded = true;
			sanFenNvId = soundPool.load(context,R.raw.nv_threepoint,1);
		}else{
			play(sanFenNvId);
		}
	}
	public void loadOrPlaySanFenNan(){
		if(!sanFenNanLoaded){
			sanFenNanLoaded = true;
			sanFenNanId = soundPool.load(context,R.raw.nan_threepoint,1);
		}else{
			play(sanFenNanId);
		}
	}
	public void loadOrPlayErFenNv(){
		if(!erFenNvLoaded){
			erFenNvLoaded = true;
			erFenNvId = soundPool.load(context,R.raw.nv_twopoint,1);
		}else{
			play(erFenNvId);
		}
	}
	public void loadOrPlayErFenNan(){
		if(!erFenNanLoaded){
			erFenNanLoaded = true;
			erFenNanId = soundPool.load(context,R.raw.nan_twopoint,1);
		}else{
			play(erFenNanId);
		}
	}
	public void loadOrPlayYiFenNv(){
		if(!yiFenNvLoaded){
			yiFenNvLoaded = true;
			yiFenNvId = soundPool.load(context,R.raw.nv_onepoint,1);
		}else{
			play(yiFenNvId);
		}
	}
	public void loadOrPlayYiFenNan(){
		if(!yiFenNanLoaded){
			yiFenNanLoaded = true;
			yiFenNanId = soundPool.load(context,R.raw.nan_onepoint,1);
		}else{
			play(yiFenNanId);
		}
	}
	public void loadOrPlayClick(){
		if(!clickLoaded){
			clickLoaded = true;
			clickId = soundPool.load(context,R.raw.click,1);
		}else{
			play(clickId);
		}
	}
	public void loadOrPlayAlarm(){
		if(!alarmLoaded){
			alarmLoaded = true;
			alarmId = soundPool.load(context,R.raw.alarm,1);
		}else{
			play(alarmId);
		}
	}
	public void playResource(int id){
		play(soundPool.load(context,id,1));
	}
	public void play(final int soundId){
		int id = playSoundId(soundId);
		if(id == 0){//failed in play
			playSoundId(soundId);
		}
	}
	/**
	 * @param soundId
	 * @return 0 if play failed. here always exists a certain rate in fail. 
	 * the resource died is one of most reason.
	 * but don't worry, this method doesn't throw any exceptions.
	 */
	public int playSoundId(int soundId){
		return soundPool.play(soundId,volume,volume,1,0,1);
	}
}
