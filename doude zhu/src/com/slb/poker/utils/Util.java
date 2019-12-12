package com.slb.poker.utils;

import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;

public class Util {
	private static int step = 190000;
	public static int getUniqueViewId(){
		return step++;
	}
	public static int pxToDp(int px) {
	    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
	}
	public static int dpToPx(int dp) {
		float density = Resources.getSystem().getDisplayMetrics().density;
	    return (int) (dp * density);
	}
	private static Random random = new Random(System.currentTimeMillis());
	public static int getRandom(int begin,int cap){
		return random.nextInt(cap-begin)+begin;
	}
	public static String getStringBy(int num,int minLen){
		String act = num+"";
		int actLen = act.length();
		for(int i = 0; i<(minLen-actLen); i++){
			act = "0"+act;
		}
		return act;
	}
	public static void runInMainThread(Runnable runnable){
		if(Looper.myLooper() == Looper.getMainLooper())
			runnable.run();
		else{
			Handler mainHandler = new Handler(Looper.getMainLooper());
			mainHandler.post(runnable);
		}
	}
	public static void saveData(Context context,String name,String value){
		 SharedPreferences.Editor editor = context.getSharedPreferences(Property.MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
		 editor.putString(name, value);
		 editor.apply();
	}
	public static String retrieveData(Context context,String name,String defaultStr){
		return context.getSharedPreferences(Property.MY_PREFS_NAME, Context.MODE_PRIVATE).getString(name,defaultStr);
	}
}
