package com.slb.poker.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.slb.poker.R;
import com.slb.poker.listener.CallViewListener;
import com.slb.poker.utils.Property;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class UpcomingActivity extends NavigationAbstractActivity{
	int time = 0;
	Timer timer = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upcoming);
		ViewShowListen(R.id.giveupgameclickid, 1001);
	}
	@Override
	protected void ViewAfterShow(int viewID, int width, int height) {
		if(viewID == R.id.giveupgameclickid){
			time = 5;
			Map<String,String> map = new HashMap<String,String>();
			map.put(Property.TYPE_IDENTITY, "giveup");
			addToClickScaleView(findViewById(R.id.giveupgameclickid, ImageView.class), 
					map);
			final TextView titleView = findViewById(R.id.title_text_id,TextView.class);
			titleView.post(new Runnable() {
				@Override
				public void run() {
					titleView.setText(getTitleText());
				}
			});
			final TextView tview = findViewById(R.id.full_ani_text_startgame, TextView.class);
			tview.post(new Runnable() {
				@Override
				public void run() {
					tview.setTextColor(Color.GREEN);
				}
			});
			addScaleAnimationOn(ScaledImp.class,UpcomingActivity.class);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(timer!=null)
			stopTimer();
		timer = new Timer();
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				time--;
				if(time<0){
					stopTimer();
					if(hasWindowFocus()){
						startToAnotherActivityThroughOptions(PlayActivity.class, R.anim.quickly_fade_in,  R.anim.custom_fade_out);
					}
				}else{
					setText(findViewById(R.id.full_ani_text_startgame, TextView.class),"还剩"+time+"秒开赛，请稍后");
				}
			}};
		timer.schedule(task, 500, 1000);
	}
	@Override
	protected void onPause() {
		stopTimer();
		super.onPause();
	}
	private void setText(final TextView textView,final String text){
		textView.post(new Runnable() {
			@Override
			public void run() {
				textView.setText(text);
			}
		});
	}
	private String getTitleText(){
		return "向左滑动屏幕智能选牌左移动";
	}
	@Override
	protected void addView(View view, LayoutParams params) {
	}
	@Override
	protected void removeView(View view) {
	}
	public class ScaledImp implements CallViewListener{
		@Override
		public void call(View v, Map<?, ?> map) {
			if(map.get(Property.TYPE_IDENTITY).equals("giveup")){
				stopTimer();
				finishedActivity();
			}
		}
	}
	@Override
	public void onBackPressed() {
		stopTimer();
		super.onBackPressed();
	}
	private void stopTimer(){
		if(timer!=null){
			timer.cancel();
			timer.purge();
			timer=null;
		}
	}
}
