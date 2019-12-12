package com.slb.poker.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.slb.poker.R;
import com.slb.poker.listener.CallViewListener;
import com.slb.poker.model.user.Player;
import com.slb.poker.utils.MusicPlayer;
import com.slb.poker.utils.Property;
import com.slb.poker.utils.SoundEffectPlayer;
import com.slb.poker.utils.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BoardActivity extends NavigationAbstractActivity{
	private int hallMsgId = 10001;
	private int bottomBackboardMsgId = 10002;
	private int bottomBackboardViewid = 10001;
	private int moreViewid = 10002;
	private int storeViewid = 10003;
	private int settingViewid = 10004;
	private int startViewid = 10005;
	private int voiceViewid = 10006;
	private int topBackboardViewId = 10007;
	private int headImageViewId = 10008;
	private int headNameViewId = 10009;
	private int hallMessageViewId = 10010;
	private int hallHeadCoinTextViewId = 10011;
	private int hallHeadGenderViewId = 10012;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		ViewShowListen(R.id.hall, hallMsgId);
	}
	@Override
	public void ViewAfterShow(int viewid, int width, int height) {
		if(R.id.hall == viewid){
			addBotttomBackboardView(createBottomBackboardView());
			addTopBackboardView(createTopBackboardView());
			adoptAudioVolume();
			ViewShowListen(bottomBackboardViewid, bottomBackboardMsgId);
		}else if(bottomBackboardViewid == viewid){
			decorateComps();
			addAnimator();
		}
	}
	private void adoptAudioVolume(){
		SoundEffectPlayer.getInstance().setSoundEffectVolumePercent(getSoundEffectVolume());
		MusicPlayer.getInstance().setStreamVolume(getMusicVolume());
	}
	private void decorateComps(){
		//add more button
		addMoreButtomView(createMoreButtomView());
		//add components of functions
		List<Map<String,Object>> components = new ArrayList<Map<String,Object>>();
		Map<String,Object> shop = new HashMap<String,Object>();
		shop.put("viewid", storeViewid);
		shop.put("text", "商场");
		shop.put("viewdrawableid", R.drawable.hall_btn_recharge);
		components.add(shop);
		Map<String,Object> setting = new HashMap<String, Object>();
		setting.put("viewid", settingViewid);
		setting.put("text","设置");
		setting.put("viewdrawableid", R.drawable.hall_btn_set);
		components.add(setting);
		decorateComponents(components);
		//add start button
		addStartQuickButtonBackView(createStartQuickButtonBackView());
		//add voice button
		addVoiceButtonView(createVoiceButtonView());
		//add Hall message
		addHallMessageView(createHallMessageView());
		//add head information
		addHeadPicInfo();		
	}
	private void addAnimator(){
		addScaleAnimationOn(BoardCallViewImp.class,BoardActivity.class);
	}
	private void addHeadPicInfo(){
		Player player = getPlayer();
		int topBoardHeight = findTopBackboardView().getHeight();
		ImageView gameRoomRoomHeadBgImg = createGameRoomRoomHeadBgView();
		int topMargin =  (topBoardHeight-getBitmapFromImageView(gameRoomRoomHeadBgImg).getHeight())/2;
		int leftMargin = 20;
		int width = ViewGroup.LayoutParams.WRAP_CONTENT,height = width;
		//add head background image
		addGameRoomRoomHeadView(topBoardHeight,gameRoomRoomHeadBgImg,topMargin,leftMargin,width,height);
		//add head picture image
		width = height = getBitmapFromImageView(gameRoomRoomHeadBgImg).getWidth();
		addGameRoomRoomHeadView(topBoardHeight,createHeadImage(player.getHead_img()),topMargin,leftMargin,width,height);
		//add head board image
		addGameRoomRoomHeadView(topBoardHeight,createGameRoomRoomHeadFrameImage(),topMargin,leftMargin,width,height);
		
		TextView playerNameView = createPlayerNameTextView(player.getName());
		addPlayerNameTextView(playerNameView,gameRoomRoomHeadBgImg.getId());
		ImageView playerScoreBgView = createPlayerScoreBg();
		addPlayerScoreBg(playerScoreBgView,gameRoomRoomHeadBgImg.getId());
		ImageView scoreMoneyLogView = createScoreMoneyLogView();
		addScoreMoneyLogView(scoreMoneyLogView,playerScoreBgView,gameRoomRoomHeadBgImg.getId());
		addScoreView(createScoreView(player.getCoins(),12),
				scoreMoneyLogView.getId());
		
		//add gender tag
		addGenderHumanView(createGenderHumanView(player.getGender()),playerNameView.getId(),playerNameView.getTextSize());
	}
	private ImageView createGenderHumanView(int gender){
		Options options = new Options();
		options.inScaled = true;
		ImageView view = buildImageView(gender == Property.BOY?
				R.drawable.friendscene_friend_icon_man:
				R.drawable.friendscene_friend_icon_woman, options);
		view.setId(hallHeadGenderViewId);
		return view;
	}
	private void addGenderHumanView(ImageView imageView,int previousId,float size){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = (int)size;
		params.addRule(RelativeLayout.RIGHT_OF, previousId);
		params.addRule(RelativeLayout.ALIGN_BOTTOM,previousId);
		params.leftMargin = 10;
		addView(imageView, params);
	}
	private void addScoreView(TextView textView,int moneyLogId){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.RIGHT_OF, moneyLogId);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, moneyLogId);
		params.leftMargin = 2;
		addView(textView, params);
	}
	private TextView createScoreView(int coins,int size){
		TextView textView = new TextView(this);
		textView.setText(coins+"");
		textView.setTextSize(size);
		textView.setTextColor(Color.YELLOW);
		textView.setId(hallHeadCoinTextViewId);
		return textView;
	}
	private void addScoreMoneyLogView(ImageView imageView,ImageView playerScoreBgView,int gameroombgid){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = getBitmapFromImageView(playerScoreBgView).getHeight();
		params.addRule(RelativeLayout.RIGHT_OF,gameroombgid);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, gameroombgid);
		params.leftMargin = 10;
		addView(imageView, params);
	}
	private ImageView createScoreMoneyLogView(){
		Options options = new Options();
		options.inScaled = true;
		ImageView imageView = buildImageView(R.drawable.gameroom_hall_money_up, options);
		imageView.setId(Util.getUniqueViewId());
		return imageView;
	}
	private void addPlayerScoreBg(ImageView imageView,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.RIGHT_OF,id);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, id);
		params.leftMargin = 5;
		addView(imageView, params);
	}
	private ImageView createPlayerScoreBg(){
		Options options = new Options();
		options.inScaled = true;
		ImageView imageView = buildImageView(R.drawable.gameroom_hall_money_bg, options);
		return imageView;
	}
	private TextView createPlayerNameTextView(String name){
		TextView tview = new TextView(this);
		tview.setText(name);
		tview.setTextSize(12);
		tview.setTextColor(Color.WHITE);
		tview.setId(headNameViewId);
		addToClickScaleView(tview, createMap(headNameViewId));
		return tview;
	}
	private void addPlayerNameTextView(TextView textView,int frameBoardViewId){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.RIGHT_OF, frameBoardViewId);
		params.addRule(RelativeLayout.ALIGN_TOP, frameBoardViewId);
		params.leftMargin = 5;
		addView(textView, params);
	}
	private void addGameRoomRoomHeadView(int topBoardHight,ImageView view,int topMargin,int leftMargin,int width,int height){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.topMargin = topMargin;
		params.leftMargin = leftMargin;
		addView(view, params);
	}
	private ImageView createGameRoomRoomHeadBgView(){
		Options options = new Options();
		options.inScaled = true;
		ImageView headimgbg = buildImageView(R.drawable.gameroom_room_head_bg,options);
		headimgbg.setId(Util.getUniqueViewId());
		return headimgbg;
	}
	private ImageView createGameRoomRoomHeadFrameImage(){
		Options options = new Options();
		options.inScaled = true;
		return buildImageView(R.drawable.gameroom_room_head_frame,options);
	}
	private ImageView createHeadImage(int headImageDrawableId){
		Options options = new Options();
		options.inScaled = true;
		ImageView imageView = buildImageView(headImageDrawableId, options);
		imageView.setId(headImageViewId);
		addToClickScaleView(imageView, createMap(headImageViewId+""));
		return imageView;
	}
	private ImageView createHallMessageView(){
		Options options = new Options();
		options.inScaled = true;
		ImageView imageView = buildImageView(R.drawable.hall_message, options);
		imageView.setId(hallMessageViewId);
		addToClickScaleView(imageView, createMap(hallMessageViewId));
		return imageView;
	}
	private void addHallMessageView(ImageView view){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.BELOW, topBackboardViewId);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.leftMargin = 20;
		params.topMargin = 30;
		addView(view, params);
	}
	private ImageView createVoiceButtonView(){
		Options options = new Options();
		options.inScaled = true;
		ImageView view = buildImageView(R.drawable.hall_voice1, options);
		view.setId(voiceViewid);
		addToClickScaleView(view, createMap(voiceViewid));
		return view;
	}
	private void addVoiceButtonView(ImageView view){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.BELOW, topBackboardViewId);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.rightMargin = 20;
		params.topMargin = 30;
		addView(view, params);
	}
	private ImageView[] createStartQuickButtonBackView(){
		Options options = new Options();
		options.inScaled = true;
		ImageView back = buildImageView(R.drawable.hall_btn_start_n, options);
		back.setId(Util.getUniqueViewId());
		ImageView view = buildImageView(R.drawable.hall_btn_start, options);
		view.setId(startViewid);
		addToClickScaleView(view, createMap(startViewid+""));
		return new ImageView[]{
				back,
				view
		};
	}
	private void addStartQuickButtonBackView(ImageView[] views){
		RelativeLayout.LayoutParams bck_params = new RelativeLayout.LayoutParams(0, 0);
		bck_params.width = bck_params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		bck_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		bck_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		bck_params.rightMargin = 80;
		addView(views[0], bck_params);
		
		RelativeLayout.LayoutParams btn_params = new RelativeLayout.LayoutParams(0, 0);
		btn_params.width = btn_params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		btn_params.addRule(RelativeLayout.ALIGN_LEFT,views[0].getId());
		btn_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		btn_params.leftMargin = (getBitmapFromImageView(views[0]).getWidth()-getBitmapFromImageView(views[1]).getWidth())/2;
		addView(views[1],btn_params);
	}
	private void decorateComponents(List<Map<String,Object>> components){
		int previousViewId = moreViewid;
		int bottomBackboardViewHeight = findBottomBackboardView().getHeight();
		for(Map<String,Object> comp : components){
			//add sign image
			ImageView view = createHallBottomIconImageView(Integer.parseInt(comp.get("viewdrawableid").toString()),
					Integer.parseInt(comp.get("viewid").toString()));
			addToClickScaleView(view, createMap(comp.get("viewid").toString()));
			previousViewId = addHallBottomIconView(
					view,
					previousViewId,
					20,
					(int)(bottomBackboardViewHeight*0.05),
					(int)(bottomBackboardViewHeight*1.1),
					(int)(bottomBackboardViewHeight*1.1));
			
			//add text
			previousViewId = addHallBottomIconView(
					createHallBottomIconTextView(Util.getUniqueViewId(), comp.get("text").toString()), 
					previousViewId, 
					0,
					(int)(bottomBackboardViewHeight*0.1),
					ViewGroup.LayoutParams.WRAP_CONTENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT);
			
			//add down line
			view = createHallBottomIconImageView(R.drawable.hall_line_down,Util.getUniqueViewId());
			previousViewId = addHallBottomIconView(
					view,
					previousViewId,
					20,
					(bottomBackboardViewHeight-getBitmapFromImageView(view).getHeight())/2,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
	}
	private Map<String,String> createMap(int id){
		return createMap(id+"");
	}
	private Map<String, String> createMap(String str) {
		Map<String,String> maps = new HashMap<String, String>();
		maps.put(Property.TYPE_IDENTITY, str);
		return maps;
	}
	private TextView createHallBottomIconTextView(int viewid,String text){
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(12);
		textView.setId(viewid);
		return textView;
	}
	private ImageView createHallBottomIconImageView(int drawableId,int viewid){
		Options options = new Options();
		options.inScaled = true;
		ImageView view = buildImageView(drawableId, options);
		view.setId(viewid);
		return view;
	}
	private int addHallBottomIconView(View view,int RightOfId,int leftMargin,int bottomMargin,int width,int height){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = width;
		params.height = height;
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.RIGHT_OF,RightOfId);
		params.leftMargin = leftMargin;
		params.bottomMargin = bottomMargin;
		addView(view, params);
		return view.getId();
	}
	private ImageView createMoreButtomView(){
		Options options = new Options();
		options.inScaled = true;
		ImageView imageView = buildImageView(R.drawable.hall_btn_more,options);
		imageView.setId(moreViewid);
		addToClickScaleView(imageView, createMap(moreViewid));
		return imageView;
	}
	private void addMoreButtomView(ImageView view){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		int bottomBackboardViewHeight = findBottomBackboardView().getHeight();
		params.height = params.width = (int)(bottomBackboardViewHeight*0.7);
		params.leftMargin =(int)(bottomBackboardViewHeight*0.3);
		params.bottomMargin = (int)(bottomBackboardViewHeight*0.15);
		addView(view, params);
	}
	private ImageView createTopBackboardView(){
		Options options = new Options();
		options.inScaled = true;
		options.inMutable = true;
		ImageView view = buildImageView(R.drawable.hall_ddz_title,options);
		view.setId(topBackboardViewId);
		return view;
	}
	private void addTopBackboardView(ImageView view){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = getScreenWidth();
		//note: the increase of with and height are by a certain percent.
		//my test result exclaim: the percent is 10. the height increase by 1, then width increase by 10.
		int rate = (getScreenWidth()-getBitmapFromImageView(view).getWidth())/10;
		params.height = getBitmapFromImageView(view).getHeight()+rate;
		
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		addView(view,params);
	}
	private ImageView createBottomBackboardView(){
		Options options = new Options();
		options.inScaled = true;
		options.inMutable = true;
		ImageView imageView = buildImageView(R.drawable.hall_bottom, options);
		imageView.setId(bottomBackboardViewid);
		return imageView;
	}
	private void addBotttomBackboardView(ImageView view){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = getScreenWidth();
		//note: the increase of with and height are by a certain percent.
		//my test result exclaim: the percent is 10. the height increase by 1, then width increase by 10.
		int rate = (getScreenWidth()-getBitmapFromImageView(view).getWidth())/10;
		params.height = getBitmapFromImageView(view).getHeight()+rate;
		
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		addView(view,params);
	}
	@Override
	protected void addView(View view,RelativeLayout.LayoutParams params){
		addViewToViewGroup(findHallView(),view,params);
	}
	private RelativeLayout findHallView(){
		return findViewById(R.id.hall, RelativeLayout.class);
	}
	private ImageView findBottomBackboardView(){
		return findViewById(bottomBackboardViewid, ImageView.class);
	}
	private ImageView findTopBackboardView(){
		return findViewById(topBackboardViewId, ImageView.class);
	}
	public class BoardCallViewImp implements CallViewListener{
		@Override
		public void call(View v, Map<?, ?> map) {
			int val = Integer.parseInt(map.get(Property.TYPE_IDENTITY).toString());
			//store
			if(val == storeViewid)
				startToAnotherActivity(MallActivity.class);
			else if(val == settingViewid)
				startToAnotherActivity(SettingActivity.class);
			else if(val == headImageViewId || val == headNameViewId)
				startToAnotherActivity(ProfileActivity.class);
			else if(val == startViewid)
				startToAnotherActivity(UpcomingActivity.class);
			else if(val == hallMessageViewId)
				showMessage("消息功能还未开通");
			else if(val == moreViewid)
				showMessage("更多功能还未开通");
			else if(val == voiceViewid)
				triggerVoice();
		}
	}
	protected void triggerVoice(){
		//this method trigger voice view button
	}
	@Override
	protected void removeView(View view) {
		removeViewFromViewGroup(findHallView(), view);
	}
	@Override
	protected void onStart() {
		super.onStart();
		updateHeadInfo();
	}
	private void updateHeadInfo(){
		Player player = getPlayer();
		updateHeadInfo_headimg(player.getHead_img());
		updateHeadInfo_name(player.getName());
		updateHeadInfo_coins(player.getCoins());
		updateHeadInfo_gender(player.getGender());
	}
	private void updateHeadInfo_gender(int gender){
		if(findViewById(hallHeadGenderViewId, ImageView.class)!=null)
			findViewById(hallHeadGenderViewId, ImageView.class).setImageBitmap(
					buildBitmap(gender == Property.BOY?
							R.drawable.friendscene_friend_icon_man:
							R.drawable.friendscene_friend_icon_woman));
	}
	private void updateHeadInfo_coins(int coins){
		if(findViewById(hallHeadCoinTextViewId, TextView.class)!=null)
			findViewById(hallHeadCoinTextViewId, TextView.class).setText(coins+"");
	}
	private void updateHeadInfo_name(String name){
		if(findViewById(headNameViewId, TextView.class)!=null)
			findViewById(headNameViewId, TextView.class).setText(name);
	}
	private void updateHeadInfo_headimg(int head_img_drawable_id){
		if(findViewById(headImageViewId, ImageView.class)!=null)
			findViewById(headImageViewId, ImageView.class).setImageBitmap(buildBitmap(head_img_drawable_id));
	}
	private Bitmap buildBitmap(int drawabled_id){
		return BitmapFactory.decodeResource(getResources(), drawabled_id);
	}
}
