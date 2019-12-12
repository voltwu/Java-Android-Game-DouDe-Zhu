package com.slb.poker.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.slb.poker.R;
import com.slb.poker.model.SettingItem;
import com.slb.poker.setting.MusicVolumeDragger;
import com.slb.poker.setting.ScreenBrightnessDragger;
import com.slb.poker.setting.SoundEffectVolumeDragger;
import com.slb.poker.utils.MusicPlayer;
import com.slb.poker.utils.ScreenBrightnessController;
import com.slb.poker.utils.SoundEffectPlayer;
import com.slb.poker.utils.Util;

import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends CompsActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public String getCompsTitle() {
		return "…Ë÷√";
	}
	@Override
	public void dobody() {
		MusicPlayer.getInstance().load(this);
		SoundEffectPlayer.getInstance().load(this);
		ScreenBrightnessController.getInstance().load(this);
		
		float mv = getCurrentMusicVolumnPercent();
		float lrv = getCurrentSoundEffectVolumePercent();
		float sb = getCurrentScreenBrightnessPercent();
		List<SettingItem> items = new ArrayList<SettingItem>();
		items.add(new SettingItem("±≥æ∞“Ù¿÷", mv,new MusicVolumeDragger()));
		items.add(new SettingItem("”Œœ∑“Ù–ß", lrv,new SoundEffectVolumeDragger()));
		items.add(new SettingItem("∆¡ƒª¡¡∂»", sb,new ScreenBrightnessDragger()));
		addItems(items);
	}
	private void addItems(List<SettingItem> items){
		int previousId = -1;
		for(SettingItem item : items){
			TextView textView = createNameView(item.getName());
			addNameView(textView,previousId);
			item.setSlidderId(Util.getUniqueViewId());
			ImageView slidderTrackView = createSlidderBgPlateView(new SlidderTouchListener(item),item.getSlidderId());
			int trackHeight = (int)(getBitmapFromImageView(slidderTrackView).getHeight()*(6.0/4));
			addSliddersBgPlateView(slidderTrackView, textView.getId(),trackHeight);

			item.setCoinViewId(Util.getUniqueViewId());
			ImageView coinSlidderView = createCoinSlidderView(item.getCoinViewId());
			item.setTrackLen(getBitmapFromImageView(slidderTrackView).getWidth()-getBitmapFromImageView(coinSlidderView).getWidth());
			
			//green slidder view
			item.setLeftHalCircleViewId(Util.getUniqueViewId());
			ImageView leftHalfCircleView = createGreenSlidderView(item.getLeftHalCircleViewId());
			addGreenSlidderHalfCircleView(leftHalfCircleView,
					slidderTrackView.getId(),
					trackHeight,
					trackHeight,
					0);
			
			//left half view 
			item.setBrickId(Util.getUniqueViewId());
			final ImageView lrfullView = createLFFullView(item.getBrickId());
			addLFFullView(lrfullView,
					leftHalfCircleView.getId(),
					item.getGreenTrackLen(),
					trackHeight);
			//coin
			addcoinSlidderView(coinSlidderView,
					lrfullView.getId(),
					trackHeight,
					trackHeight);
			previousId = textView.getId();
		}
	}
	private void addLFFullView(ImageView view,int leftId,int len,int height){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,height);
		params.width = len;
		params.addRule(RelativeLayout.RIGHT_OF, leftId);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, leftId);
		params.leftMargin = -height/2;
		addViewToContainer(view, params);
	}
	private ImageView createLFFullView(int id){
		Options options = new Options();
		options.inScaled = true;
		ImageView iv = buildImageView(R.drawable.setting_slidder_green_brick,options);
		iv.setId(id);
		iv.setScaleType(ScaleType.FIT_XY);
		return iv;
	}
	private void addGreenSlidderHalfCircleView(ImageView slidderGreenView,int slidderTrackerId,int width,int height,int leftMargin){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
		params.addRule(RelativeLayout.ALIGN_LEFT, slidderTrackerId);
		params.addRule(RelativeLayout.ALIGN_BOTTOM,slidderTrackerId);
		params.leftMargin = leftMargin;
		addViewToContainer(slidderGreenView, params);
	}
	private void addcoinSlidderView(ImageView coinSlidderView, int trackViewId,
			int width, int height) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
		params.addRule(RelativeLayout.ALIGN_BOTTOM,trackViewId);
		params.addRule(RelativeLayout.ALIGN_RIGHT, trackViewId);
		params.rightMargin = -height/2;
		addViewToContainer(coinSlidderView, params);
	}
	private ImageView createGreenSlidderView(int id){
		Options options = new Options();
		options.inScaled = true;
		ImageView imageView = buildImageView(R.drawable.setting_slidder_green, options);
		imageView.setId(id);
		imageView.setScaleType(ScaleType.FIT_XY);
		return imageView;
	}
	private ImageView createCoinSlidderView(int id){
		Options options = new Options();
		options.inScaled = true;
		ImageView view = buildImageView(R.drawable.setting_slidder_coin, options);
		view.setId(id);
		view.setScaleType(ScaleType.FIT_XY);
		return view;
	}
	private ImageView createSlidderBgPlateView(OnTouchListener listener,int id){
		Options options = new Options();
		options.inScaled = true;
		ImageView imageView = buildImageView(R.drawable.setting_sladder, options);
		imageView.setId(id);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setOnTouchListener(listener);
		return imageView;
	}
	private void addSliddersBgPlateView(ImageView imageView,int textId,int height){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.height = height;
		params.addRule(RelativeLayout.ALIGN_BOTTOM,textId);
		params.addRule(RelativeLayout.RIGHT_OF, textId);
		params.leftMargin = 70;
		params.bottomMargin = 5;
		addViewToContainer(imageView, params);
	}
	private TextView createNameView(String name){
		TextView tv = new TextView(this);
		tv.setText(name);
		tv.setTextColor(Color.WHITE);
		tv.setTextSize(26);
		tv.setTypeface(Typeface.MONOSPACE);
		tv.setId(Util.getUniqueViewId());
		return tv;
	}
	private void addNameView(TextView tv,int previousId){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.leftMargin = getScreenWidth()/8;
		params.topMargin = getScreenHeight()/8;
		if(previousId<0)
			params.topMargin = getScreenHeight()/5;
		else
			params.addRule(RelativeLayout.BELOW, previousId);
		
		addViewToContainer(tv, params);
	}
	@Override
	protected void clickedBackcall(View v, Map<?, ?> map) {
	}
	protected float getCurrentSoundEffectVolumePercent(){
		return SoundEffectPlayer.getInstance().getSoundEffectVolumePercent();
	}
	protected float getCurrentMusicVolumnPercent(){
		return MusicPlayer.getInstance().getStreamPercent();
	}
	private float getCurrentScreenBrightnessPercent(){
		return ScreenBrightnessController.getInstance().getBrightnessPercent();
	}
	public class SlidderTouchListener implements OnTouchListener{
		SettingItem item = null;
		public SlidderTouchListener(SettingItem item){
			this.item = item;
		}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN || 
					event.getAction() == MotionEvent.ACTION_MOVE){
				int clickedX = (int)event.getX();
				
				int distance = clickedX;
				int totalWidth = item.getTrackLen();
				if(distance<0)
					distance = 0;
				else if(distance>totalWidth)
					distance = totalWidth;
				
				final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)findViewById(item.getBrickId()).getLayoutParams();
				params.width = (int)distance;
				findViewById(item.getBrickId()).post(new Runnable() {
					@Override
					public void run() {
						findViewById(item.getBrickId()).setLayoutParams(params);
						findViewById(item.getBrickId()).invalidate();
					}
				});
				item.getListener().invoke(distance*1.0/totalWidth);
				return true;
			}
			return false;
		}
	}
}
