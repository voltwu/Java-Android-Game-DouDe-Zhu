package com.slb.poker.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings.SettingNotFoundException;

public class ScreenBrightnessController {
	public int maxBrightnessLevel = 255;
	private static ScreenBrightnessController screenBrightnessController = new ScreenBrightnessController();
	
	private ScreenBrightnessController(){
	}
	public static ScreenBrightnessController getInstance(){
		return screenBrightnessController;
	}
	Context context = null;
	public void load(Context context){
			this.context = context;
	}
	public float getBrightnessPercent(){
		try{
			if(isAuto(getmode()))
				stopAutoBrightness();
			return (float)((getCurrentBrightnessLevel()*1.0)/maxBrightnessLevel);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	public int getCurrentBrightnessLevel() throws SettingNotFoundException{
		return android.provider.Settings.System.getInt(context.getContentResolver(), 
				android.provider.Settings.System.SCREEN_BRIGHTNESS);		
	}
	public int getmode() throws SettingNotFoundException{
		return android.provider.Settings.System.getInt(context.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE);		
	}
	public boolean isAuto(int mode){
		return mode==1?true:false;
	}
	public void stopAutoBrightness() {
		android.provider.Settings.System.putInt(context.getContentResolver(),
			  android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
			  android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }
	/**
	 * @param brightness 0 is the most brightest, 255 is the most darkest.
	 */
	public void setBrightness(int brightness){
        ContentResolver cResolver = context.getApplicationContext().getContentResolver();
        android.provider.Settings.System.putInt(cResolver, 
        		android.provider.Settings.System.SCREEN_BRIGHTNESS, 
        		brightness);
	}
	public void setBrightnessPercent(float percent){
		setBrightness((int)(maxBrightnessLevel*(percent)));
	}
}
