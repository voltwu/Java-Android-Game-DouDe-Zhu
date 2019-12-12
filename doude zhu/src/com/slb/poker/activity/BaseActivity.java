package com.slb.poker.activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.slb.poker.R;
import com.slb.poker.listener.CallViewListener;
import com.slb.poker.model.ScaledViewContainer;
import com.slb.poker.model.user.Player;
import com.slb.poker.utils.MusicPlayer;
import com.slb.poker.utils.Property;
import com.slb.poker.utils.SoundEffectPlayer;
import com.slb.poker.utils.Util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BaseActivity extends Activity{
	List<ScaledViewContainer> clickScaleViews = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SoundEffectPlayer.getInstance().load(this);
		MusicPlayer.getInstance().load(this);
		clickScaleViews = new ArrayList<ScaledViewContainer>();
	}
	protected void addToClickScaleView(View view,Map<String,String> map){
		clickScaleViews.add(new ScaledViewContainer(view,map));
	}
	protected void addScaleAnimationOn(Class<? extends CallViewListener> clazz,Class<?> innerClass){
		for(ScaledViewContainer svc : clickScaleViews){
			try {
				CallViewListener obj = null;
				if(innerClass != null){
					Constructor<? extends CallViewListener> ctor = clazz.getDeclaredConstructor(innerClass);
					obj = ctor.newInstance(this);
				}else{
					obj = clazz.newInstance();
				}
				svc.getView().setOnClickListener(new OnClickScaleImp(obj,svc.getMap()));
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	protected abstract void ViewAfterShow(int viewID,int width,int height);
	
	protected  void ViewShowListen(final int viewID,final int msgID){
		if(Looper.getMainLooper() == Looper.myLooper())
			runTimeSchedule(viewID,msgID);
		else
			new Handler(getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					runTimeSchedule(viewID,msgID);
				}
			});
		
	}
	protected void runTimeSchedule(final int viewID,final int msgID){
        final int f_viewID = viewID;
        final int f_msgID = msgID;
        final Timer f_timer = new Timer();
        final Handler f_handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == f_msgID){
                    final View v = (View)findViewById(f_viewID);
                    if(v.getWidth() != 0 && v.getHeight() != 0){
                        f_timer.cancel();
		                ViewAfterShow(viewID,v.getWidth(),v.getHeight());
                    }
                }
            }
        };
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = f_msgID;
                f_handler.sendMessage(message);
            }
        };
        f_timer.schedule(task,10,500);			
	}
	protected void Log(String message){
		Log.i("slb", message);
	}
	@SuppressWarnings("unchecked")
	public<T> T findViewById(int id,Class<T> clazz){
		return (T)findViewById(id);
	}
	public void addViewToViewGroup(final ViewGroup target,final View view,final ViewGroup.LayoutParams params){
		if(getMainLooper() == Looper.myLooper())
			target.addView(view, params);
		else
			target.post(new Runnable() {
				@Override
				public void run() {
					target.addView(view, params);
				}
			});
	}
	public void removeViewFromViewGroup(final ViewGroup target,final View view){
		if(getMainLooper() == Looper.myLooper())
			target.removeView(view);
		else
			target.post(new Runnable() {
				@Override
				public void run() {
					target.removeView(view);
				}
			});
	}
	protected int getScreenWidth(){
		Point point = new Point();
		getWindowManager().getDefaultDisplay().getSize(point);
		return point.x;
	}
	protected int getScreenHeight(){
		Point point = new Point();
		getWindowManager().getDefaultDisplay().getSize(point);
		return point.y;
	}
	protected Options createUnScaledOptions(){
		Options opts = new Options();
		opts.inScaled = false;
		return opts;
	}
	protected Bitmap getBitmapFromImageView(ImageView view){
		return ((BitmapDrawable)view.getDrawable()).getBitmap();
	}
	protected ImageView buildImageView(int id,Options options){
		ImageView imageView = new ImageView(this);
		if(options!=null)
			imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), id,options));
		else
			imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), id));
		return imageView;
	}
	protected ImageView buildImageView(Bitmap bitmap){
		ImageView imageView = new ImageView(this);
		imageView.setImageBitmap(bitmap);
		return imageView;
	}
	protected ImageView buildImageView(int id){
		return buildImageView(id, null);
	}
	protected void setData(String name,String value){
		Util.saveData(this, name, value);
	}
	protected String getData(String name,String defaultStr){
		return Util.retrieveData(this, name, defaultStr);
	}
	protected String getData(String name){
		return getData(name, "");
	}
	protected void saveMusicVolume(float percent){
		setData(Property.MUSIC_VOLUME_NAME, percent+"");
	}
	protected float getMusicVolume(){
		return Float.parseFloat(getData(Property.MUSIC_VOLUME_NAME,"0.5"));
	}
	protected void saveSoundEffectVolume(float percent){
		setData(Property.MUSIC_SOUND_EFFECT_NAME, percent+"");
	}
	protected float getSoundEffectVolume(){
		return Float.parseFloat(getData(Property.MUSIC_SOUND_EFFECT_NAME,"0.5"));
	}
	protected void savePlayer(Player player){
		setData(Property.PLAYER_SHARED_REFERENCE_NAME,
				String.format(Property.PLAYER_FORMAT_PATTERN_SHARED_REFERENCE_NAME,
						player.getName(),
						player.getCoins(),
						player.getHead_img(),
						player.getGender()));
	}
	protected Player getPlayer(){
		String playerstr = getData(Property.PLAYER_SHARED_REFERENCE_NAME,
				String.format(Property.PLAYER_FORMAT_PATTERN_SHARED_REFERENCE_NAME,
						Property.PLAYER_DEFAULT_NAME_SHARED_REFERENCE_NAME,
						Property.PLAYER_DEFAULT_COIN_SHARED_REFERENCE_NAME,
						Property.PLAYER_DEFAULT_HEAD_IMG_SHARED_REFERENCE_NAME,
						Property.PLAYER_DEFAULT_GENDER_SHARED_REFERENCE_NAME));
		Player player = new Player();
		Class<? extends Player> pclazz = player.getClass();
		for(String fstr : playerstr.split("\t")){
			int index = fstr.indexOf(":");
			String name = fstr.substring(0,index);
			String value = fstr.substring(index+1);
			Field field = null;
			try {
				Field [] fields = pclazz.getDeclaredFields();
				for(Field f : fields){
					if(f.getName().equals(name)){
						field = f;
						break;
					}
				}
				if(null == field){
					field = pclazz.getSuperclass().getDeclaredField(name);
				}
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
	        field.setAccessible(true);
	        try {
	        	if(field.getType().equals(String.class)){
	        		field.set(player, value.toString());
	        	}
	        	else if(field.getType().equals(int.class)){
	        		field.set(player,Integer.parseInt(value.toString()));
	        	}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return player;
	}
	protected String ConvertFrom(Object obj){
		if(null == obj)
			return null;
		else
			return obj.toString();
	}
	protected class OnClickScaleImp implements OnClickListener{
		CallViewListener listener;
		View v = null;
		Map<?,?> map = null;
		public OnClickScaleImp(CallViewListener listener,Map<?,?> map){
			this.listener = listener;
			this.map = map;
		}
		@Override
		public void onClick(View v) {
			SoundEffectPlayer.getInstance().playClick();
			Animation animation = AnimationUtils.loadAnimation(
                    BaseActivity.this, R.anim.single_scale);
			animation.setAnimationListener(new AnimationImp());
			this.v = v;
			v.startAnimation(animation);
		}
		public class AnimationImp implements AnimationListener{
			@Override
			public void onAnimationEnd(Animation animation) {
				listener.call(v,map);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationStart(Animation animation) {
			}
		}
	}
	protected void startToAnotherActivity(@SuppressWarnings("rawtypes") Class clazz){
		startToAnotherActivity(clazz,android.R.anim.fade_in, android.R.anim.fade_out);
	}
	protected void startToAnotherActivity(Intent[] intents){
		startToAnotherActivity(intents,android.R.anim.fade_in, android.R.anim.fade_out);
	}
	protected void startToAnotherActivity(Intent[] intents,int animin,int animout){
		startActivities(intents);
		oppendStyle(animin, animout);
	}
	protected void startToAnotherActivity(@SuppressWarnings("rawtypes") Class clazz,int animin,int animout){
		Intent intent = new Intent(this,clazz);
		startActivity(intent);
		oppendStyle(animin, animout);
	}
	protected void startToAnotherActivityThroughOptions(@SuppressWarnings("rawtypes") Class clazz,int animin,int animout){
		Intent intent = new Intent(this,clazz);
	    ActivityOptions options = ActivityOptions.makeCustomAnimation(this,animin,animout);
	    startActivity(intent,options.toBundle());
	}
	protected void oppendStyle(int in,int out){
		overridePendingTransition(in, out); 
	}
	protected void finishedActivity(){
		this.finish();
		oppendStyle(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	protected void showMessage(String message){
		final ImageView msgBarView = createShowMsgBarView();
		ValueAnimator valueAnimator = addAnimation(msgBarView);
		addShowMsgBarView(msgBarView);
		final TextView textView = createTextMsgView(message);
		valueAnimator.start();
		valueAnimator.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				addTextMsgView(textView);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						removeView(textView);
						removeView(msgBarView);
					}
				}).start();
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
	}
	private ImageView createShowMsgBarView(){
		Options options = new Options();
		options.inScaled = true;
		return buildImageView(R.drawable.des_ddz_style_msg_bar, options);
	}
	private void addShowMsgBarView(ImageView barView){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(barView, params);
	}
	protected void addTextMsgView(TextView tv){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(tv, params);
	}
	protected TextView createTextMsgView(String message){
		String text = message;
		TextView tv = new TextView(this);
		tv.setText(text);
		tv.setTextColor(Color.WHITE);
		tv.setTextSize(16);
		return tv;
	}
	protected ValueAnimator addAnimation(final ImageView imageView){
		ValueAnimator valueAnimator = ValueAnimator.ofInt(0, getScreenWidth());
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
            	if(imageView.getLayoutParams()!=null){
	                int currentValue = (Integer) animator.getAnimatedValue();
	                imageView.getLayoutParams().width=currentValue;
	                imageView.requestLayout();
                }
            }
        });
        return valueAnimator;
	}
	protected abstract void addView(View view,RelativeLayout.LayoutParams params);
	protected abstract void removeView(View view);
	protected void addCoins(int coins){
		Player player = getPlayer();
		player.addCoins(coins);
		savePlayer(player);
	}
	protected void updateTextView(final TextView textView,final String text){
		if(Looper.getMainLooper() == Looper.myLooper())
			textView.setText(text);
		else
			textView.post(new Runnable() {
				@Override
				public void run() {
					textView.setText(text);
				}
			});
	}
	protected boolean isAppOnForeground(Context context) {
	    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
	    if (appProcesses == null) {
	      return false;
	    }
	    final String packageName = context.getPackageName();
	    for (RunningAppProcessInfo appProcess : appProcesses) {
	      if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
	        return true;
	      }
	    }
	    return false;
  }
}
