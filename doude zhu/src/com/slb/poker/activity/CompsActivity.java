package com.slb.poker.activity;

import java.util.HashMap;
import java.util.Map;

import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

import com.slb.poker.R;
import com.slb.poker.listener.CallViewListener;
import com.slb.poker.utils.Property;
import com.slb.poker.utils.Util;

public abstract class CompsActivity extends NavigationAbstractActivity{
	private int ddz_title_msgId = 10001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comps);
		ViewShowListen(R.id.ddz_title_id, ddz_title_msgId);
	}
	@Override
	protected void ViewAfterShow(int viewID, int width, int height) {
		if(viewID == R.id.ddz_title_id){
			prework();
			setCompsTitle(getCompsTitle());
			dobody();
			finishingWork();
		}
	}
	protected void setCompsTitle(String title){
		if("…Ë÷√".equals(title)){
			addLeftCharacterView(createCharacterView(R.drawable.title_font_0_shezhi_she));
			addRightCharacterView(createCharacterView(R.drawable.title_font_0_shezhi_zhi));
		}else if("≥‰÷µ".equals(title)){
			addLeftCharacterView(createCharacterView(R.drawable.title_font_0_chong));
			addRightCharacterView(createCharacterView(R.drawable.title_font_0_zhi));
		}else if("µµ∞∏".equals(title)){
			addLeftCharacterView(createCharacterView(R.drawable.title_font_dangan_dang));
			addRightCharacterView(createCharacterView(R.drawable.title_font_dangan_an));
		}
	}
	protected void prework(){
		addToClickScaleView(findViewById(R.id.imageView1, ImageView.class),createMap(Property.EXIT_IDENTITY,null));
	}
	protected void addRightCharacterView(ImageView imageView){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.ddz_title_id);
		params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.imageView2);
		params.rightMargin = Util.dpToPx(15);
		params.bottomMargin = Util.dpToPx(4);
		addView(imageView, params);
	}
	protected ImageView createCharacterView(int id){
		Options options = new Options();
		options.inScaled = true;
		ImageView view = buildImageView(id, options);
		view.setScaleType(ScaleType.FIT_XY);
		return view;
	}
	protected void addLeftCharacterView(ImageView imageView){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.ddz_title_id);
		params.addRule(RelativeLayout.ALIGN_LEFT, R.id.imageView2);
		params.leftMargin = Util.dpToPx(15);
		params.bottomMargin = Util.dpToPx(4);
		addView(imageView, params);
	}
	public abstract String getCompsTitle();
	public abstract void dobody();
	protected void addView(View view,RelativeLayout.LayoutParams params){
		addViewToViewGroup(findViewById(R.id.mall_id, RelativeLayout.class), view, params);
	}
	protected void finishingWork(){
		addScaleAnimationOn(CallViewImp.class,CompsActivity.class);
	}
	protected void addViewToContainer(View view,RelativeLayout.LayoutParams params){
		addViewToViewGroup(findViewById(R.id.container, RelativeLayout.class), view, params);
	}
	protected void removeViewFromContainer(View view){
		findViewById(R.id.container, RelativeLayout.class).removeView(view);
	}
	protected void removeView(View view){
		removeViewFromViewGroup(findViewById(R.id.mall_id, RelativeLayout.class), view);
	}
	protected class CallViewImp implements CallViewListener{
		public CallViewImp(){}
		@Override
		public void call(View v,Map<?,?> map) {
			send(v,map);
		}
	}
	protected void send(View v,Map<?,?> map){
		if(map.get("type").equals(Property.EXIT_IDENTITY)){
			exit();
		}
		clickedBackcall(v,map);
	}
	protected abstract void clickedBackcall(View v,Map<?,?> map);
	protected void exit(){
		this.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);  
	}
	protected Map<String,String> createMap(String type,String[][] arraies){
		Map<String,String> maps = new HashMap<String, String>();
		maps.put(Property.TYPE_IDENTITY, type);
		if(arraies!=null)
			for(String[] strs : arraies){
				maps.put(strs[0], strs[1]);
			}
		return maps;
	}
}
