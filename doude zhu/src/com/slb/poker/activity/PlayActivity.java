package com.slb.poker.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.ai.poker.AI;
import com.ai.poker.DIZHU;
import com.ai.poker.P;
import com.ai.poker.Poker;
import com.ai.poker.PokerCounts;
import com.ai.poker.PokerGroupTypeEnum;
import com.ai.poker.PokerTypeEnum;
import com.slb.poker.R;
import com.slb.poker.R.string;
import com.slb.poker.listener.GameDialogStopListener;
import com.slb.poker.model.AnimationEnum;
import com.slb.poker.model.CardsDiPaiUnit;
import com.slb.poker.model.CardsNumUnit;
import com.slb.poker.model.GamerMessageEnum;
import com.slb.poker.model.ImageViewItem;
import com.slb.poker.model.InvocationOrder;
import com.slb.poker.model.InvocationRecorder;
import com.slb.poker.model.NumberOfChuPaiRecorder;
import com.slb.poker.model.OptionalCompEnum;
import com.slb.poker.model.OptionalCompViewMap;
import com.slb.poker.model.POSITION;
import com.slb.poker.model.Point;
import com.slb.poker.model.PokerPackageViewRecorder;
import com.slb.poker.model.RealPlayerCardUnit;
import com.slb.poker.model.StateEnum;
import com.slb.poker.model.TouchLocRecorder;
import com.slb.poker.model.user.Gamer;
import com.slb.poker.model.user.Player;
import com.slb.poker.utils.CardsNumUnitUtil;
import com.slb.poker.utils.MusicPlayer;
import com.slb.poker.utils.PokerUtil;
import com.slb.poker.utils.Property;
import com.slb.poker.utils.RobotFactory;
import com.slb.poker.utils.ScoinNumUnitUtil;
import com.slb.poker.utils.SoundEffectPlayer;
import com.slb.poker.utils.Util;
import com.slb.poker.views.AirplaneAnimationView;
import com.slb.poker.views.BombAnimationView;
import com.slb.poker.views.GameOverMessageView;
import com.slb.poker.views.PokerPackageView;
import com.slb.poker.views.ShunZiAnimationView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class PlayActivity extends NavigationMainActivity {
	private int playPanelMsgId = 10001;
	private int HEADNAMEVIEWID = 10002;
	private int BLOCKVIEWID = 10003;
	private int BLOCKVIEWMSGID = 10004;
	private int LEFTTOPROBOTBLOCKVIEWMSGID = 10005;
	private int RIGHTTOPBLOCKVIEWMSGID = 10006;
	private int MIDTIPBGMSGID = 10007;
	private boolean isTimeTickRunning = false;
	List<Gamer> gamers = null;
	List<CardsDiPaiUnit> diPaiUnits = null;
	CardsNumUnit[] cardsNumViews = null;
	private List<ImageView> realPlayerScoreViews = null;//this is scores
	List<RealPlayerCardUnit> realPlayerCardsViews = null; //this is cards
	private OptionalCompViewMap optionalCompViewMaps = null;
	private StateEnum stateEnum = null;
	private static boolean isBackFromBackground = false;
	private int zhaDanCount = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		isBackFromBackground = false;
		if(isScreenOn())
			ViewShowListen(R.id.play_panel_id, playPanelMsgId);
	}
	@Override
	protected void ViewAfterShow(int viewID, int width, int height) {
		if(viewID == R.id.play_panel_id){
			startNewGame();
		}else if(viewID == BLOCKVIEWID){
			updateTimeTextView();
		}else if(viewID == getGamer(POSITION.RIGHTTOP).getBlockTextViewId()){
			updateTimeTextView(getGamer(POSITION.RIGHTTOP));
		}else if(viewID == getGamer(POSITION.LEFTTOP).getBlockTextViewId()){
			updateTimeTextView(getGamer(POSITION.LEFTTOP));
		}else if(viewID == R.id.mid_tip_bg_id){
			if(isGamerOver())
				GameOver();
			else
				GamerSendCardPokers();
		}
	}
	private void startNewGame(){
		stateEnum = StateEnum.QIANGDIZHU;
		isTimeTickRunning = false;
		gamers = new ArrayList<Gamer>();
		diPaiUnits = new ArrayList<CardsDiPaiUnit>();
		realPlayerScoreViews = new ArrayList<ImageView>();
		realPlayerCardsViews = new ArrayList<RealPlayerCardUnit>();
		cardsNumViews = new CardsNumUnit[]{
				new CardsNumUnit(POSITION.LEFTTOP),
				new CardsNumUnit(POSITION.RIGHTTOP)
		};
		zhaDanCount = 0;
		InvocationOrder.instance().clear();
		InvocationRecorder.instance().clear();
		PokerPackageViewRecorder.instance().clear();
		NumberOfChuPaiRecorder.getInstance().clear();
		startGame();
	}
	private void GameOver(){
		MusicPlayer.getInstance().pause();
		removeAllRealPlayerCards();
		for(Gamer gamer : gamers){
			if(gamer.getPokersCount()!=0)
				showCards(gamer.getPokers(), gamer.getPosition());
		}
		final int realPlayerScore = getBottomCoins();
		boolean win = isWin();
		if(isWin()){
			SoundEffectPlayer.getInstance().playResource(R.raw.win);
		}else{
			SoundEffectPlayer.getInstance().playResource(R.raw.fail);
		}
		
		final GameOverMessageView view = new GameOverMessageView(this,win, getLeftCoins(),
				realPlayerScore,
				getRightCoins(),
				getChuntian(),
				getZhadan(),
				getBieShu(),
				getDiShu());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(view, params);
		view.setGameDialogStopListener(new GameDialogStopListener() {
			@Override
			public void call() {
				//score
				int newscore;
				if(isWin()){
					newscore = getGamer(POSITION.BOTTOM).getCoins()+Math.abs(realPlayerScore);
				}else{
					newscore = getGamer(POSITION.BOTTOM).getCoins()-Math.abs(realPlayerScore);
				}
				if(newscore<0) 
					newscore = 0;
				setRealViewScore(newscore);
				Player player = getPlayer();
				player.setCoins(newscore);
				savePlayer(player);
				//remove itself
				removeView(view);
				//here needs a restart
				startNewGame();
			}
		});
	}
	private int getDiShu(){
		String text = findViewById(R.id.dishu_value_id, TextView.class).getText().toString();
		try{
			return Integer.parseInt(text);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	private int getBieShu(){
		String text = findViewById(R.id.beishu_value_id, TextView.class).getText().toString();
		try{
			return Integer.parseInt(text);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	private int getZhadan(){
		return zhaDanCount;
	}
	private int getChuntian(){
		int count = 0;
		int i = NumberOfChuPaiRecorder.getInstance().get(getDiZhu().getPosition());
		if(i<=1){
			count++;
		}
		i = 0;
		for(Gamer gamer : gamers){
			if(gamer.getPosition()!=getDiZhu().getPosition()){
				i+=NumberOfChuPaiRecorder.getInstance().get(getDiZhu().getPosition());
			}
		}
		if(i==0)
			count++;
		return count;
	}
	private int getRightCoins(){
		return calculateScore(POSITION.RIGHTTOP);
	}
	private int getLeftCoins(){
		return calculateScore(POSITION.LEFTTOP);
	}
	private int getBottomCoins(){
		return calculateScore(POSITION.BOTTOM);
	}
	private int calculateScore(POSITION pos){
		int result = (getDiShu()*10)*getBieShu();
		for(int index=0;index < (getZhadan()+getChuntian()); index++){
			result*=2;
		}
		if(isWin(pos)){
			return result*2;
		}else{
			return result*(-1);
		}
	}
	private boolean isWin(){
		return isWin(POSITION.BOTTOM);
	}
	private boolean isWin(POSITION pos){
		for(Gamer gamer:gamers)
			if(gamer.getPokersCount()==0)
			{
				if(gamer.isDiZhu()){
					return gamer.getPosition()==pos;
				}else{
					return !getGamer(pos).isDiZhu();
				}
			}
		return false;
	}
	private boolean isGamerOver(){
		for(Gamer gamer : gamers){
			if(gamer.getPokersCount()==0)
				return true;
		}
		return false;
	}
	private void GamerSendCardPokers(){
		POSITION pos = InvocationOrder.instance().current();
		removePreviousCardPackageView(pos);
		if(pos == POSITION.RIGHTTOP){
			GamerSendRobotInRightTop();
		}else if(pos == POSITION.LEFTTOP){
			GamerSendRobotInLeftTop();
		}else if(pos == POSITION.BOTTOM){
			GamerSendRealPlayereInBottom();
		}
	}
	private void removePreviousCardPackageView(POSITION pos){
		PokerPackageView ppv = PokerPackageViewRecorder.instance().get(pos);
		if(ppv!=null){
			removeView(ppv);
			PokerPackageViewRecorder.instance().setData(pos, null);
		}
	}
	private void GamerSendRealPlayereInBottom(){
		showSendCardsOptionDialog();
	}
	private void GamerSendRobotInLeftTop(){
		showRobotBell(POSITION.LEFTTOP);
	}
	private void GamerSendRobotInRightTop(){
		showRobotBell(POSITION.RIGHTTOP);
	}
	private void GamerSendCardInLeftTop(){
		if(InvocationRecorder.instance().isPositive(POSITION.LEFTTOP)){//is positive
			GamerSendCardIn(POSITION.LEFTTOP,true);
		}else{// not positive
			GamerSendCardIn(POSITION.LEFTTOP,false);
		}
		updateCardsNumUnitViews(POSITION.LEFTTOP, getGamer(POSITION.LEFTTOP).getPokersCount());		
		callNext();
	}
	private void GamerSendCardInRightTop(){
			if(InvocationRecorder.instance().isPositive(POSITION.RIGHTTOP)){//is positive
				GamerSendCardIn(POSITION.RIGHTTOP,true);
			}else{// not positive
					GamerSendCardIn(POSITION.RIGHTTOP,false);
			}
		updateCardsNumUnitViews(POSITION.RIGHTTOP, getGamer(POSITION.RIGHTTOP).getPokersCount());
		callNext();
	}
	private void GamerSendCardIn(POSITION pos,boolean positive){
		Gamer gamer = getGamer(pos);
		List<Poker> sended = getGamerSendPokers(gamer.getPokers(), positive);
		InvocationRecorder.instance().add(sended, pos, isDiZhu(pos));
		if(sended!=null)//show cards
			showCards(sended,pos);
		else
			showBuChu(pos);
		
		if(gamer.getPokersCount()==2){
			soundCards(gamer.getGender()==Property.BOY?R.raw.nan_twocard:R.raw.nv_twocard);
		}else if(gamer.getPokersCount()==1){
			soundCards(gamer.getGender()==Property.BOY?R.raw.nan_onecard:R.raw.nv_onecard);
		}
	}
	private void showBuChu(POSITION pos){
		if(pos==POSITION.LEFTTOP)
			showLeftTopRobotViewNotification(GamerMessageEnum.BUCHU);
		else
			showRightRobotViewNotification(GamerMessageEnum.BUCHU);
	}
	private boolean isDiZhu(POSITION pos){
		if(pos == getDiZhu().getPosition())
			return true;
		return false;
	}
	private AnimationEnum getPokerType(List<Poker> cards,int gender){
		PokerGroupTypeEnum pgte = AI.getPokerGroupTypeEnum(Copy(cards));
		if(pgte==null)
			return null;
		if(pgte.equals(PokerGroupTypeEnum.BOMB) ||
				pgte.equals(PokerGroupTypeEnum.ROCKET)){
			if(pgte.equals(PokerGroupTypeEnum.BOMB)){SoundEffectPlayer.getInstance().playBombEffect();}
			else{SoundEffectPlayer.getInstance().playRocketEffect();}
			soundCards(gender==Property.BOY?R.raw.nan_zhadan:R.raw.nv_zhadan);
			zhaDanCount++;
			return AnimationEnum.BOMB;
		}else if(pgte.equals(PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DAN) ||
				pgte.equals(PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DUI)){
			SoundEffectPlayer.getInstance().playFeijiEffect();
			soundCards(gender==Property.BOY?R.raw.nan_feiji:R.raw.nv_feiji);
			return AnimationEnum.AIRPLANE;
		}else if(pgte.equals(PokerGroupTypeEnum.DAN_SHUAN) || 
				pgte.equals(PokerGroupTypeEnum.SHUANG_SHUN) ||
				pgte.equals(PokerGroupTypeEnum.SAN_SHUN)){
			if(pgte.equals(PokerGroupTypeEnum.DAN_SHUAN))
				SoundEffectPlayer.getInstance().playShunZiEffect();
			else
				SoundEffectPlayer.getInstance().playLianDuiEffect();
			soundCards(gender==Property.BOY?R.raw.nan_shunzi:R.raw.nv_shunzi);
			return AnimationEnum.SUNZI;
		}else if(pgte.equals(PokerGroupTypeEnum.SAN_DAI_YI_DAI_DAN))
			soundCards(gender==Property.BOY?R.raw.nan_sandaiyi:R.raw.nv_sandaiyi);
		else if(pgte.equals(PokerGroupTypeEnum.SAN_DAI_YI_DAI_DUI))
			soundCards(gender==Property.BOY?R.raw.nan_sandaiyidui:R.raw.nv_sandaiyidui);
		else if(pgte.equals(PokerGroupTypeEnum.SHI_DAI_ER))
			soundCards(gender==Property.BOY?R.raw.nan_sidaier:R.raw.nv_sidaier);
		
		return null;
	}
	private void showAnimationIn(final AnimationEnum type){
		if(type==null)
			return;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(type==AnimationEnum.BOMB)
					showBombAnimation();
				else if(type==AnimationEnum.AIRPLANE)
					showAirPlaneAnimation();
				else if(type==AnimationEnum.SUNZI)
					showSunZiAnimation();
			}
		});
	}
	private void showAirPlaneAnimation(){
		final AirplaneAnimationView view = new AirplaneAnimationView(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        TranslateAnimation translateAnimation =
                new TranslateAnimation(
                    Animation.ABSOLUTE,getScreenWidth(),
                    Animation.ABSOLUTE,-Util.dpToPx(200),
                    Animation.ABSOLUTE,getScreenHeight()/2,
                    Animation.ABSOLUTE,getScreenHeight()/2);
             translateAnimation.setDuration(2000);
             translateAnimation.setInterpolator(new LinearInterpolator());
		view.setAnimation(translateAnimation);
		translateAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				removeView(view);
			}
		});
		addView(view, params);
	}
	private void showSunZiAnimation(){
		final ShunZiAnimationView view = new ShunZiAnimationView(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = Util.dpToPx(300);
		params.height = Util.dpToPx(250);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		view.setCallbackListener(new GameDialogStopListener() {
			@Override
			public void call() {
				removeView(view);
			}
		});
		addView(view, params);
	}
	private void showBombAnimation(){
				final BombAnimationView view = new BombAnimationView(this);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
				params.width = params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				view.setCallbackListener(new GameDialogStopListener() {
					@Override
					public void call() {
						removeView(view);
					}
				});
				addView(view, params);
	}
	private void showCards(List<Poker> cards,POSITION pos){
		NumberOfChuPaiRecorder.getInstance().addOne(pos);
		Gamer gamer = getGamer(pos);
		AnimationEnum animationType = getPokerType(cards,gamer.getGender());
		showAnimationIn(animationType);
		PronounceCards(cards,pos);
		int pokersWidth = (int)(BitmapFactory.decodeResource(getResources(), R.drawable.skin_card_3_card_bk_l).getWidth()*(2.0/3));
		int pokersHeight = (int)(BitmapFactory.decodeResource(getResources(), R.drawable.skin_card_3_card_bk_l).getHeight()*(2.0/3));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.height = pokersHeight;
		params.width = (pokersWidth/2)*(cards.size()+1);
		if(pos!=POSITION.BOTTOM && params.width>((pokersWidth/2)*(Property.ROBOTINLINEMAXCARDS+1))){
			params.width = ((pokersWidth/2)*(Property.ROBOTINLINEMAXCARDS+1));
			params.height+=Util.dpToPx(Property.ROBOTMAXMOVEDOWN);
		}
		if(pos==POSITION.BOTTOM){
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.addRule(RelativeLayout.ALIGN_BOTTOM,HEADNAMEVIEWID);
		}else if(pos == POSITION.RIGHTTOP){
			params.addRule(RelativeLayout.LEFT_OF,gamer.getCoinViewId());
			params.addRule(RelativeLayout.ALIGN_BOTTOM, gamer.getCoinViewId());
			params.bottomMargin = -Util.dpToPx(10);
			params.rightMargin = Util.dpToPx(5);
		}else{
			params.addRule(RelativeLayout.RIGHT_OF,gamer.getCoinViewId());
			params.addRule(RelativeLayout.ALIGN_BOTTOM, gamer.getCoinViewId());
			params.leftMargin = -Util.dpToPx(5);
			params.bottomMargin = -Util.dpToPx(10);
		}
		PokerPackageView pokerPackageView = new PokerPackageView(this, cards, 
				pokersWidth, 
				pokersHeight,pos==POSITION.BOTTOM);
		
		PokerPackageViewRecorder.instance().setData(pos, pokerPackageView);
		
		addView(pokerPackageView, params);
	}
	private void PronounceCards(List<Poker> pokers,POSITION pos){
		if(pokers.size()>=3){
			return;
		}
		int gender = getGamer(pos).getGender();
		int size = pokers.size();
		if(pokers.get(0).getP().equals(P.P2)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_02:R.raw.dan_nv_02):(gender==Property.BOY?R.raw.dui_nan_02:R.raw.dui_nv_02));
		else if(pokers.get(0).getP().equals(P.P3)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_03:R.raw.dan_nv_03):(gender==Property.BOY?R.raw.dui_nan_03:R.raw.dui_nv_03));
		else if(pokers.get(0).getP().equals(P.P4)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_04:R.raw.dan_nv_04):(gender==Property.BOY?R.raw.dui_nan_04:R.raw.dui_nv_04));
		else if(pokers.get(0).getP().equals(P.P5)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_05:R.raw.dan_nv_05):(gender==Property.BOY?R.raw.dui_nan_05:R.raw.dui_nv_05));
		else if(pokers.get(0).getP().equals(P.P6)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_06:R.raw.dan_nv_06):(gender==Property.BOY?R.raw.dui_nan_06:R.raw.dui_nv_06));
		else if(pokers.get(0).getP().equals(P.P7)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_07:R.raw.dan_nv_07):(gender==Property.BOY?R.raw.dui_nan_07:R.raw.dui_nv_07));
		else if(pokers.get(0).getP().equals(P.P8)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_08:R.raw.dan_nv_08):(gender==Property.BOY?R.raw.dui_nan_08:R.raw.dui_nv_08));
		else if(pokers.get(0).getP().equals(P.P9)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_09:R.raw.dan_nv_09):(gender==Property.BOY?R.raw.dui_nan_09:R.raw.dui_nv_09));
		else if(pokers.get(0).getP().equals(P.P10)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_10:R.raw.dan_nv_10):(gender==Property.BOY?R.raw.dui_nan_10:R.raw.dui_nv_10));
		else if(pokers.get(0).getP().equals(P.PJ)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_j:R.raw.dan_nv_j):(gender==Property.BOY?R.raw.dui_nan_j:R.raw.dui_nv_j));
		else if(pokers.get(0).getP().equals(P.PQ)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_q:R.raw.dan_nv_q):(gender==Property.BOY?R.raw.dui_nan_q:R.raw.dui_nv_q));
		else if(pokers.get(0).getP().equals(P.PK)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_k:R.raw.dan_nv_k):(gender==Property.BOY?R.raw.dui_nan_k:R.raw.dui_nv_k));
		else if(pokers.get(0).getP().equals(P.PA)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_a:R.raw.dan_nv_a):(gender==Property.BOY?R.raw.dui_nan_a:R.raw.dui_nv_a));
		else if(pokers.get(0).getP().equals(P.PXW)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_small:R.raw.dan_nv_small):(-1));
		else if(pokers.get(0).getP().equals(P.PDW)) soundCards(size==1?(gender==Property.BOY?R.raw.dan_nan_big:R.raw.dan_nv_big):(-1));
	}
	private void soundCards(int id){
		if(id!=-1){
			SoundEffectPlayer.getInstance().playResource(id);
		}
	}
	private List<Poker> getGamerSendPokers(List<Poker> pokers,boolean isPositive){
		List<Poker> ownerPokers = Copy(pokers);
		List<Poker> result = null;
		if(isPositive){
			result = AI.sendPokersActively(ownerPokers, getAIDIZHU(), getAIPokerNum());
		}else{
			result = AI.sendPokersNegatively(ownerPokers, getPrePokers(), getAIDIZHU());
		}
		//remove them from original pokers
		if(result!=null)
			removeSendedPokersFrom(pokers,result);
		return result;
	}
	private void removeSendedPokersFrom(List<Poker> poker,List<Poker> removed){
		for(Poker p : removed){
			poker.remove(p);
		}
	}
	private List<Poker> getPrePokers(){
		return InvocationRecorder.instance().getLastPokers();
	}
	private PokerCounts getAIPokerNum(int ownCount){
		return new PokerCounts(getGamer(POSITION.LEFTTOP).getPokersCount(), 
				ownCount, 
				getGamer(POSITION.RIGHTTOP).getPokersCount());
	}	
	private PokerCounts getAIPokerNum(){
		return new PokerCounts(getGamer(POSITION.LEFTTOP).getPokersCount(), 
				getGamer(POSITION.BOTTOM).getPokersCount(), 
				getGamer(POSITION.RIGHTTOP).getPokersCount());
	}
	private DIZHU getAIDIZHU(){
		POSITION pos = getDiZhu().getPosition();
		if(pos.equals(POSITION.LEFTTOP))
			return DIZHU.PREVIOUS;
		else if(pos.equals(POSITION.BOTTOM))
			return DIZHU.MINE;
		else
			return DIZHU.NEXT;
	}
	private List<Poker> Copy(List<Poker> pokers){
		List<Poker> result = new ArrayList<Poker>();
		for(Poker p : pokers){
			result.add(p.clone());
		}
		return result;
	}
	@Override
	protected void addView(View view, LayoutParams params) {
		addViewToViewGroup(findPlayPanelView(), view, params);
	}
	private RelativeLayout findPlayPanelView(){
		return findViewById(R.id.play_panel_id, RelativeLayout.class);
	}
	@Override
	protected void removeView(View view) {
		removeViewFromViewGroup(findPlayPanelView(), view);
	}
	protected void removeView(int id){
		removeViewFromViewGroup(findPlayPanelView(), findViewById(id));
	}
	private void startGame(){
		clearPrivousView();
		Player player = getPlayer();
		if(player.getCoins()<=0){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showBoughtDialog();
				}
			});
		}else
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								MusicPlayer.getInstance().start();
								findViewById(R.id.textview1,TextView.class).setText(getText(string.mid_tip_text));
								addGamers();
								sendCards();
								showViewInBeginning();
							}
						});
				}
			}).start();
	}
	private void showBoughtDialog(){
		Dialog dialog = new Dialog(PlayActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View dialogView = LayoutInflater.from(PlayActivity.this).inflate(R.layout.dialog_buy,new RelativeLayout(PlayActivity.this));
		dialog.setContentView(dialogView);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.setCancelable(false);
		dialog.show();
		dialog.findViewById(R.id.returntohell).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoundEffectPlayer.getInstance().playClick();
				startToAnotherActivity(BoardActivity.class);
			}
		});
		dialog.findViewById(R.id.boughtcoins).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoundEffectPlayer.getInstance().playClick();
				Intent intent = new Intent(PlayActivity.this,BoardActivity.class);
				Intent intent2 = new Intent(PlayActivity.this,MallActivity.class);
				startToAnotherActivity(new Intent[]{intent,intent2});
			}
		});		
	}
	private void clearPrivousView(){
		int[] ids = new int[]{
				R.id.play_panel_id,
				R.id.cip_anchor,
				R.id.mid_tip_bg_id,
				R.id.mid_tip_icon_id,
				R.id.textview1,
				R.id.textview2,
				R.id.dishu_value_id,
				R.id.dishu_value_text_id,
				R.id.beishu_value_id,
				R.id.bieshu_value_text_id,
				R.id.store_coin_id};
		removeAllViewExcept(ids);
		findViewById(R.id.textview1,TextView.class).setText(getText(string.mid_tip_text_fenzhuo));
		findViewById(R.id.dishu_value_id,TextView.class).setText("0");
		findViewById(R.id.beishu_value_id, TextView.class).setText("0");
	}
	private void removeAllViewExcept(int[] ids){
		List<View> list = new ArrayList<View>();
		int chileCount = findPlayPanelView().getChildCount();
		for(int index = 0; index < chileCount; index++){
			View view = findPlayPanelView().getChildAt(index);
			if(!in(view.getId(),ids)){
				list.add(view);
			}
		}
		for(View view : list){
			removeView(view);
		}
		
	}
	private boolean in(int id,int[] ids){
		for(int v : ids)
			if(v==id)
				return true;
		return false;
	}
	private void updateTimeTextView(final Gamer gamer){
		startNewThread(new Runnable() {
			@Override
			public void run() {
				int count = Property.INITIALTIMESECONDS;
				int baseCount = count-Util.getRandom(2, 5);
				while(count-->baseCount){
					try {
						if(!checkOnTheForegound())
							return;
						updateTimeAreaText(count,gamer.getBlockTextViewId());
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//remove BlockText And BlockView
				removeRightBlockAndBlockText(gamer);
				
				if(stateEnum == StateEnum.QIANGDIZHU){
					int beishu = shouldHaveDiZHu(gamer.getPokers());
					GamerMessageEnum gme = null;
					if(beishu == 1) gme = GamerMessageEnum.YIFEN;
					else if(beishu == 2) gme = GamerMessageEnum.ERFEN;
					else if(beishu == 3) gme = GamerMessageEnum.SANFEN;
					else gme = GamerMessageEnum.BUJIAO;	
					
					if(getGamer(POSITION.BOTTOM).isDiZhu())
						gme = GamerMessageEnum.BUJIAO;
					
					if(gamer.getPosition() == POSITION.RIGHTTOP){
						if(gme!=GamerMessageEnum.BUJIAO)
							setDIZHU(gamer.getPosition());
						//is DiZhu
						showRightRobotViewNotification(gme);
						//invoke the left top
						InvokeLeftTopRobot();
					}else if(gamer.getPosition() == POSITION.LEFTTOP){
						if(getBeiShuValue()<=1)
							gme = GamerMessageEnum.SANFEN;
						if(gme!=GamerMessageEnum.BUJIAO)
							setDIZHU(gamer.getPosition());
						showLeftTopRobotViewNotification(gme);
						//check who is the DIZHU
						drawDiZhuView();
						//reverse Di Pai
						reverseDiPai();
						//send cards on Gamer
						sendDiPaiCards();
						//refresh pokers
						refreshDiPaiCards();
						//initialize invocation orders
						InitialInvocationOrder();
						stateEnum = StateEnum.OTHER;
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								callNext();
							}
						}).start();
					}
				}else{
					if(gamer.getPosition() == POSITION.LEFTTOP)
						GamerSendCardInLeftTop();
					else if(gamer.getPosition()==POSITION.RIGHTTOP)
						GamerSendCardInRightTop();
				}
				
			}
		});
	}
	private void callNext(){
		if(checkOnTheForegound()){
			ViewShowListen(R.id.mid_tip_bg_id, MIDTIPBGMSGID);
		};
	}
	private void InitialInvocationOrder(){
		InvocationOrder.instance().init(getDiZhu().getPosition());
	}
	private void refreshDiPaiCards(){
		Gamer gamer = getDiZhu();
		if(gamer.getPosition() != POSITION.BOTTOM){
			updateCardsNumUnitViews(gamer.getPosition(), gamer.getPokersCount());
		}else{
			updateRealPlayersCards();
		}
	}
	private void updateRealPlayersCards(){
		//remove all cards
		removeAllRealPlayerCards();
		//rebuild realPlayer cards views
		rebuildRealPlayerCardViews();
		//display cards static
		displayCardsStatic();
	}
	private void displayCardsStatic(){
		int leftMargin = getRealPokersLeftMargin();
		int pokerWidth = getBitmapFromImageView(buildImageView(R.drawable.poker_clue_10)).getWidth();
		int stepMargin = (int)((getScreenWidth()-leftMargin*2-pokerWidth)*1.0/(realPlayerCardsViews.size()-1));
		for(final RealPlayerCardUnit unit : realPlayerCardsViews){
			addRealPokerView(unit.getIv(),leftMargin,pokerWidth);
			leftMargin+=stepMargin;
		}
	}
	private void rebuildRealPlayerCardViews(){
		realPlayerCardsViews.clear();
		adjustRealPlayerBackData(getGamer(POSITION.BOTTOM).getPokers());
	}
	private void removeAllRealPlayerCards(){
		for(RealPlayerCardUnit pokerUnit : realPlayerCardsViews){
			removeView(pokerUnit.getIv());
		}
	}
	private void sendDiPaiCards(){
		Gamer dizhu = getDiZhu();
		List<Poker> list = getDiPaiPokers();
		dizhu.addPokers(list);
		sortCards();
	}
	private List<Poker> getDiPaiPokers(){
		List<Poker> pokerList = new ArrayList<Poker>();
		for(CardsDiPaiUnit cdp  : diPaiUnits){
			pokerList.add(cdp.getPoker());
		}
		return pokerList;
	}
	private Gamer getDiZhu(){
		for(Gamer gamer : gamers){
			if(gamer.isDiZhu())
				return gamer;
		}
		return null;
	}
	private void reverseDiPai(){
		removeDiPaiViews();
		addDiPaiPokerViews();
		drawDiPaiView();
	}
	private void addDiPaiPokerViews(){
		for(CardsDiPaiUnit cards : diPaiUnits){
			ImageView imageView = getImageViewFromPoker(cards.getPoker());
			imageView.setId(Util.getUniqueViewId());
			cards.setView(imageView);
		}
	}
	private void drawDiZhuView(){
		for(Gamer gamer : gamers){
			drawDiZhuViewOn(gamer,gamer.isDiZhu());
		}
	}
	private void drawDiZhuViewOn(Gamer gamer, boolean diZhu) {
		ImageView imageView = buildImageView(diZhu?R.drawable.play_head_dizhu:R.drawable.play_head_nongming);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		int headImgViewId = gamer.getHeadImgViewId();
		if(gamer.getPosition() == POSITION.BOTTOM){
			params.addRule(RelativeLayout.RIGHT_OF, headImgViewId);
			params.addRule(RelativeLayout.ALIGN_TOP, headImgViewId);
			params.topMargin = Util.dpToPx(5);
		}else if(gamer.getPosition() == POSITION.LEFTTOP){
			params.addRule(RelativeLayout.RIGHT_OF, headImgViewId);
			params.addRule(RelativeLayout.ALIGN_TOP,headImgViewId);
			params.topMargin = Util.dpToPx(5);
		}else if(gamer.getPosition() == POSITION.RIGHTTOP){
			params.addRule(RelativeLayout.LEFT_OF, headImgViewId);
			params.addRule(RelativeLayout.ALIGN_TOP, headImgViewId);
			params.topMargin = Util.dpToPx(5);
		}
		addView(imageView, params);
	}
	private void setDIZHU(POSITION pos){
		for(Gamer gamer : gamers){
			if(gamer.getPosition().equals(pos))
				gamer.setDiZhu(true);
			else
				gamer.setDiZhu(false);
		}
	}
	private int shouldHaveDiZHu(List<Poker> pokers){
		return AI.CanJiaBei(pokers);
	}
	private void removeRightBlockAndBlockText(Gamer gamer){
		removeView(gamer.getBlockTextViewId());
		removeView(gamer.getBlockViewId());
	}
	private void updateTimeTextView(){
		//start time count
		startNewThread(new Runnable() {
			@Override
			public void run() {
				if(stateEnum == StateEnum.OTHER){
					startNewThread(new Runnable() {
						@Override
						public void run() {
							setOnTouchListenerOnAllPokers();
						}
					});
				}
				int count = Property.INITIALTIMESECONDS;
				while(count-->0){
					if(!isTimeTickRunning)
						return;//exit
					try {
						if(!checkOnTheForegound()){
							return;
						};
						updateTimeAreaText(count,BLOCKVIEWID);
						if(count==4 || count==2)
							SoundEffectPlayer.getInstance().playAlarm();
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(stateEnum == StateEnum.QIANGDIZHU){
					discardOptionalViewsIn(OptionalCompEnum.FOUR);
				}else if(stateEnum == StateEnum.OTHER){
					optionalCompViewMaps.getThree().getOnClickListener().onClick(null);
					boolean hasNeedToSend = false;
					for(RealPlayerCardUnit unit : realPlayerCardsViews){
						if(unit.isPreSelected()){
							hasNeedToSend = true;
							break;
						}
					}
					if(hasNeedToSend)
						optionalCompViewMaps.getFour().getOnClickListener().onClick(null);
				}
			}
		});
	}
	private boolean isScreenOn(){
		PowerManager pm = (PowerManager) 
		this.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}
	private boolean checkOnTheForegound(){
		boolean b = isAppOnForeground(PlayActivity.this);
		boolean iso = isScreenOn();
		boolean isfocus = hasWindowFocus();
		Log(" on the foreground :"+b+"");
		Log(" is the screen on :"+iso+"");
		Log(" is the window focused:"+isfocus);
		if(!iso){
			startToAnotherActivity(InterceptActivity.class);
			return false;
		}
		if(!b){
			isBackFromBackground = true;
			return false;
		}
		if(!isfocus){
			isBackFromBackground = true;
			return false;
		}
		return true;
	}
	private void setOnTouchListenerOnAllPokers(){
		findPlayPanelView().setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					TouchLocRecorder.instance().setBegin(new Point(event.getX(), event.getY()));
					return true;
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					TouchLocRecorder.instance().setEnd(new Point(event.getX(), event.getY()));
					DistributePokerArea();
					drawAllPreSelectedView();
					checkReSelectAndChuPaiButton();
					return true;
				}
				return false;
			}
		});
	}
	private void checkReSelectAndChuPaiButton(){
		if( optionalCompViewMaps.getOne().getImageView()==null ||
			optionalCompViewMaps.getFour().getImageView()==null){
			return;
		}
		boolean has = false;
		for(RealPlayerCardUnit unit : realPlayerCardsViews){
			if(unit.isPreSelected()){
				has = true;
				break;
			}
		}
		if(has){
			turnOnReSelectBtn();
			turnOnChuPaiBtn();
		}else{
			turnOffReSelectBtn();
			turnOffChuPaiBtn();
		}
	}
	class OnClickReSelectOn implements OnClickListener{
		@Override
		public void onClick(View v) {
			for(RealPlayerCardUnit unit : realPlayerCardsViews){
				unit.setPreSelected(false);
			}
			drawAllPreSelectedView();
			turnOffReSelectBtn();
			turnOffChuPaiBtn();
		}
	}
	private List<Poker> getAllSelectedPokers(){
		List<Poker> pokers = new ArrayList<Poker>();
		for(RealPlayerCardUnit unit : realPlayerCardsViews){
			if(unit.isPreSelected())
				pokers.add(unit.getPoker());
		}
		return Copy(pokers);
	}
	class OnClickChupaiOn implements OnClickListener{
		@Override
		public void onClick(View v) {
			List<Poker> poker = getAllSelectedPokers();
			ChupaiOn(poker,false);
		}
	}
	private void ChupaiOn(List<Poker> poker,boolean isbuchuclick){
		PokerGroupTypeEnum pokerGroupTypeEnum = AI.getPokerGroupTypeEnum(poker);
		if(pokerGroupTypeEnum==null){
			showNoteNoRuleCards();
			return;
		}
		if(InvocationRecorder.instance().isPositive(POSITION.BOTTOM)){
			showBottomCards(poker,true);
		}else{
			List<Poker> prepokers = InvocationRecorder.instance().getLastPokers();
			if(AI.compare(poker, prepokers) > 0){
				showBottomCards(poker,false);
			}else{
				showNoteNoRuleCards();
			}
		}
		if(getGamer(POSITION.BOTTOM).getPokersCount()==2){
			soundCards(getGamer(POSITION.BOTTOM).getGender()==Property.BOY?R.raw.nan_twocard:R.raw.nv_twocard);
		}
		else if(getGamer(POSITION.BOTTOM).getPokersCount()==1){
			soundCards(getGamer(POSITION.BOTTOM).getGender()==Property.BOY?R.raw.nan_onecard:R.raw.nv_onecard);
		}
	}
	private void showBottomCards(List<Poker> poker,boolean pasitive){
		InvocationRecorder.instance().add(poker, POSITION.BOTTOM, isDiZhu(POSITION.BOTTOM));
		showCards(poker, POSITION.BOTTOM);
		removeAllDiscardOptionalViews();
		runTimeTickLock();
		removeCards(poker);
		updateRealPlayersCards();
		//remove views
		callNext();
	}
	private void removeCards(List<Poker> pokers){
		removeSendedPokersFrom(getGamer(POSITION.BOTTOM).getPokers(),pokers);
	}
	class OnClickReSelectOff implements OnClickListener{
		@Override
		public void onClick(View v) {
		}
	}
	class OnClickChupaiOff implements OnClickListener{
		@Override
		public void onClick(View v) {
		}
	}
	private void turnOffChuPaiBtn(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.playscene_2_putnocheck);
		if(!bitmap.equals(getBitmapFromImageView(optionalCompViewMaps.getFour().getImageView()))){
			OnClickListener clickListener = new OnClickChupaiOff();
			optionalCompViewMaps.getFour().setOnClickListener(clickListener);
			setBitmap(optionalCompViewMaps.getFour().getImageView(), 
					bitmap, clickListener);
		}
	}
	private void turnOnChuPaiBtn(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.playscene_2_put);
		if(!bitmap.equals(getBitmapFromImageView(optionalCompViewMaps.getFour().getImageView()))){
			OnClickListener clickListener = new OnClickChupaiOn();
			optionalCompViewMaps.getFour().setOnClickListener(clickListener);
			setBitmap(optionalCompViewMaps.getFour().getImageView(), 
					bitmap, clickListener);
		}
	}
	private void turnOffReSelectBtn(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.playscene_2_resetnocheck);
		if(!bitmap.equals(getBitmapFromImageView(optionalCompViewMaps.getOne().getImageView()))){
			OnClickListener clickListener = new OnClickReSelectOff();
			optionalCompViewMaps.getOne().setOnClickListener(clickListener);
			setBitmap(optionalCompViewMaps.getOne().getImageView(), 
					bitmap, clickListener);
		};
	}
	private void turnOnReSelectBtn(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.playscene_2_reset);
		if(!bitmap.equals(getBitmapFromImageView(optionalCompViewMaps.getOne().getImageView()))){
			OnClickListener clickListener = new OnClickReSelectOn();
			optionalCompViewMaps.getOne().setOnClickListener(clickListener);
			setBitmap(optionalCompViewMaps.getOne().getImageView(), 
					bitmap, clickListener);
		}
	}
	private void setBitmap(final ImageView imageView,final Bitmap bitmap,final OnClickListener click){
		if(Looper.myLooper() == Looper.getMainLooper()){
			imageView.setImageBitmap(bitmap);
			imageView.setOnClickListener(click);
		}else{
			imageView.post(new Runnable() {
				@Override
				public void run() {
					imageView.setImageBitmap(bitmap);
					imageView.setOnClickListener(click);
				}
			});
		}
	}
	private void DistributePokerArea(){
		Point begin = TouchLocRecorder.instance().getBegin();
		Point end = TouchLocRecorder.instance().getEnd();
		if(begin==null || end==null)
			return;
		if(begin.x>end.x){
			Point temp = begin;
			begin = end;
			end = temp;
		}
		//when click happen, the begin and end are the same value;
		if(begin.equals(end)){
			RealPlayerCardUnit upper = null;
			for(int index=0; index<realPlayerCardsViews.size(); index++){
				RealPlayerCardUnit unit = realPlayerCardsViews.get(index);
				int[] location = new int[2];
				unit.getIv().getLocationOnScreen(location);
				if( location[0] <= begin.x && location[1]<=begin.y)
					upper = unit;
			}
			if(upper!=null)
			upper.trigger();
		}else{
			for(RealPlayerCardUnit unit : realPlayerCardsViews)
			{
				int[] location = new int[2];
				unit.getIv().getLocationOnScreen(location);
				if(location[1]<=begin.y && 
					location[1]<=end.y && 
					location[0] >= begin.x && 
					location[0]<=end.x){
						unit.trigger();
				}
			}
			//get all Pokers on select
			handleOnSelectedPokersWithAI();
		}
	}
	private void handleOnSelectedPokersWithAI(){
		List<Poker> pokerList = new ArrayList<Poker>();
		for(RealPlayerCardUnit unit:realPlayerCardsViews){
			if(unit.isPreSelected()){
				pokerList.add(unit.getPoker().clone());
			}
		}
		if(pokerList.size()<=3)
			return;
		else{
			List<Poker> result = AI.HintPokersActively(Copy(pokerList), getAIPokerNum(pokerList.size()));
			if(result==null)
				result = pokerList;
			else{
				for(RealPlayerCardUnit unit:realPlayerCardsViews){
					boolean has = false;
					for(Poker s : result){
						if(s.equals(unit.getPoker()))
							has = true;
					}
					if(!has)
					{
						unit.setPreSelected(false);
					}else{
						unit.setPreSelected(true);
					}
				}
			}
		}
	}
	private void drawAllPreSelectedView(){
		for( RealPlayerCardUnit unit : realPlayerCardsViews){
			LayoutParams params = (LayoutParams) unit.getIv().getLayoutParams();
			if(unit.isPreSelected())
				params.bottomMargin = 20;
			else
				params.bottomMargin = 0;
			setLayout(unit.getIv(), params);
		}
	}
	protected void setLayout(final ImageView iv,final RelativeLayout.LayoutParams params)
	{
		if(Looper.myLooper() == Looper.getMainLooper()){
			iv.setLayoutParams(params);
		}else{
			iv.post(new Runnable() {
				@Override
				public void run() {
					iv.setLayoutParams(params);
				}
			});
		}
	}
	protected void discardOptionalViewsIn(OptionalCompEnum oce){
		ImageViewItem item = optionalCompViewMaps.getTargetOptionalCompViews(oce);
		item.getOnClickListener().onClick(null);
	}
	private void removeAllDiscardOptionalViews(){
		removeView(optionalCompViewMaps.getOne().getImageView());
		removeView(optionalCompViewMaps.getTwo().getImageView());
		removeView(optionalCompViewMaps.getThree().getImageView());
		removeView(optionalCompViewMaps.getFour().getImageView());
		removeView(optionalCompViewMaps.getBlock());
		removeView(optionalCompViewMaps.getBlockText());
	}
	protected void showViewInBeginning(){
		drawRobotsPicView();
		drawPlayerView();
		drawDiPaiView();
		drawBeishuView();
		drawDiShuView();
	}
	private void drawPlayerView(){
		Gamer gamer = getGamer(POSITION.BOTTOM);
		setRealViewScore(gamer.getCoins());
		adjustRealPlayerBackData(gamer.getPokers());
		displayCards();
		TextView realPlayerNameView = createRealPlayerNameView(gamer.getName());
		drawRealPlayerNameView(realPlayerNameView);
		drawRealPlayerHeadImageView(gamer.getHead_img(),realPlayerNameView.getId(),gamer);
	}
	private void drawRealPlayerHeadImageView(int headImageDrawable,int nameViewId,Gamer gamer){
		RelativeLayout.LayoutParams framePicParams = getHeadFramePicParams();
		framePicParams.addRule(RelativeLayout.ABOVE, nameViewId);
		framePicParams.addRule(RelativeLayout.ALIGN_LEFT, nameViewId);
		framePicParams.bottomMargin = Util.dpToPx(10);
		ImageView headFrame = createFramePic();
		gamer.setHeadImgViewId(headFrame.getId());
		addLeftTopFramePic(headFrame,framePicParams);
		addHeadImg(createHeadImg(headImageDrawable),headFrame.getId(),headFrame.getLayoutParams());
	}
	private void drawRealPlayerNameView(TextView textView){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.leftMargin = Util.dpToPx(3);
		params.addRule(RelativeLayout.ABOVE,R.id.store_coin_id);
		params.bottomMargin = getBitmapFromImageView(realPlayerCardsViews.get(0).getIv()).getHeight()+Util.dpToPx(10);
		addView(textView, params);
	}
	private TextView createRealPlayerNameView(String name){
		TextView tv = new TextView(this);
		tv.setText(name);
		tv.setTextSize(12);
		tv.setTextColor(Color.WHITE);
		tv.setId(HEADNAMEVIEWID);
		return tv;
	}
	private void showSendCardsOptionDialog(){
		ImageView imageOne = buildImageView(R.drawable.playscene_2_resetnocheck);
		imageOne.setId(Util.getUniqueViewId());
		OnClickListener oneClickListener = new OnClickListener() {
			@Override
			public void onClick(View args) {
			}
		};
		imageOne.setOnClickListener(oneClickListener);
		ImageViewItem oneItem = new ImageViewItem(imageOne,oneClickListener);
		
		ImageViewItem twoItem= null;
		if( InvocationRecorder.instance().isStart()||
			InvocationRecorder.instance().getLastSendPOSITION() == POSITION.BOTTOM){
			ImageView imageTwo = buildImageView(R.drawable.playscene_2_noputnocheck);
			imageTwo.setId(Util.getUniqueViewId());
			OnClickListener twoClickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			};
			imageTwo.setOnClickListener(twoClickListener);
			twoItem = new ImageViewItem(imageTwo, twoClickListener);
		}else{
			ImageView imageTwo = buildImageView(R.drawable.playscene_2_noput);
			imageTwo.setId(Util.getUniqueViewId());
			OnClickListener twoClickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					removeAllDiscardOptionalViews();
					showRealPlayerPlayingMessage(GamerMessageEnum.BUCHU,false);
				}
			};
			imageTwo.setOnClickListener(twoClickListener);
			twoItem = new ImageViewItem(imageTwo, twoClickListener);
		}
		
		ImageView imageThree = buildImageView(R.drawable.playscene_2_tip);
		imageThree.setId(Util.getUniqueViewId());
		OnClickListener threeClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				DownAllRealPlayerCardsView();
				List<Poker> poker_list = Copy(getGamer(POSITION.BOTTOM).getPokers());
				List<Poker> result = null;
				if(InvocationRecorder.instance().isPositive(POSITION.BOTTOM)){
					result = AI.sendPokersActively(poker_list, getAIDIZHU(), getAIPokerNum());
				}else{
					result = AI.sendPokersNegatively(poker_list, getPrePokers(), getAIDIZHU());
				}
				if(result==null){
					showNoteNoBigCards();
					optionalCompViewMaps.getTwo().getOnClickListener().onClick(null);
				}else{
					for(RealPlayerCardUnit unit:realPlayerCardsViews){
						boolean has = false;
						for(Poker s : result){
							if(s.equals(unit.getPoker()))
								has = true;
						}
						if(!has)
						{
							unit.setPreSelected(false);
						}else{
							unit.setPreSelected(true);
						}
					}
					drawAllPreSelectedView();
					checkReSelectAndChuPaiButton();
				}
			}
		};
		imageThree.setOnClickListener(threeClickListener);
		ImageViewItem threeItem = new ImageViewItem(imageThree,threeClickListener);
		
		ImageView imageFour = null;
		OnClickListener fourClickListener = null;
		if(hasPreSelected()){
			fourClickListener = new OnClickChupaiOn();
			imageFour = buildImageView(R.drawable.playscene_2_put);
		}
		else{
			fourClickListener = new OnClickChupaiOff();
			imageFour = buildImageView(R.drawable.playscene_2_putnocheck);
		}
		imageFour.setId(Util.getUniqueViewId());
		imageFour.setOnClickListener(fourClickListener);
		ImageViewItem fourItem = new ImageViewItem(imageFour, fourClickListener);
		
		
		showRealPlayerChoiseDialog(oneItem,twoItem,threeItem,fourItem);
	}
	private boolean hasPreSelected(){
		boolean has = false;
		for(RealPlayerCardUnit unit:realPlayerCardsViews){
			if(unit.isPreSelected())
			{
				has = true;
				break;
			}
		}
		return has;
	}
	private void DownAllRealPlayerCardsView(){
		for(RealPlayerCardUnit realPlayercard : realPlayerCardsViews){
			realPlayercard.setPreSelected(false);
		}
	}
	private void showChoiseScoreDialog(){
		ImageView imageOne = buildImageView(R.drawable.playscene_one);
		imageOne.setId(Util.getUniqueViewId());
		OnClickListener oneClickListener = new OnClickListener() {
			@Override
			public void onClick(View args) {
				removeAllDiscardOptionalViews();
				showRealPlayerPlayingMessage(GamerMessageEnum.YIFEN,true);
				setDIZHU(POSITION.BOTTOM);
			}
		};
		imageOne.setOnClickListener(oneClickListener);
		ImageViewItem oneItem = new ImageViewItem(imageOne,oneClickListener);
		
		ImageView imageTwo = buildImageView(R.drawable.playscene_two);
		imageTwo.setId(Util.getUniqueViewId());
		OnClickListener twoClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeAllDiscardOptionalViews();
				showRealPlayerPlayingMessage(GamerMessageEnum.ERFEN,true);
				setDIZHU(POSITION.BOTTOM);
			}
		};
		imageTwo.setOnClickListener(twoClickListener);
		ImageViewItem twoItem = new ImageViewItem(imageTwo, twoClickListener);
		
		ImageView imageThree = buildImageView(R.drawable.playscene_three);
		imageThree.setId(Util.getUniqueViewId());
		OnClickListener threeClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeAllDiscardOptionalViews();
				showRealPlayerPlayingMessage(GamerMessageEnum.SANFEN,true);
				setDIZHU(POSITION.BOTTOM);
			}
		};
		imageThree.setOnClickListener(threeClickListener);
		ImageViewItem threeItem = new ImageViewItem(imageThree,threeClickListener);
		
		ImageView imageFour = buildImageView(R.drawable.playscene_say);
		imageFour.setId(Util.getUniqueViewId());
		OnClickListener fourClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeAllDiscardOptionalViews();
				showRealPlayerPlayingMessage(GamerMessageEnum.BUJIAO,true);
			}
		};
		imageFour.setOnClickListener(fourClickListener);
		ImageViewItem fourItem = new ImageViewItem(imageFour, fourClickListener);
		
		showRealPlayerChoiseDialog(oneItem,twoItem,threeItem,fourItem);
	}
	private void runTimeTickLock(){
		if(!isTimeTickRunning)
			return;//already had showed
		isTimeTickRunning = false;
	}
	private void showRealPlayerPlayingMessage(GamerMessageEnum type,boolean value) {
		runTimeTickLock();
		showRealPlayerViewNotification(type);
		if(value)
			InvokeRightTopRobot();
		else
			callNext();
	}
	private void InvokeRightTopRobot(){
		showRobotBell(POSITION.RIGHTTOP);
	}
	private void InvokeLeftTopRobot(){
		showRobotBell(POSITION.LEFTTOP);
	}
	private void showRobotBell(POSITION pos){
		if(!checkOnTheForegound())
			return;
		
		Gamer gamer = getGamer(pos);
		if(pos == POSITION.LEFTTOP){
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
			params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
			params.addRule(RelativeLayout.RIGHT_OF,gamer.getCoinViewId());
			params.addRule(RelativeLayout.ALIGN_BOTTOM, gamer.getCoinViewId());
			params.leftMargin = Util.dpToPx(5);
			addBell(params,gamer.getBlockTextViewId(),gamer.getBlockViewId(),LEFTTOPROBOTBLOCKVIEWMSGID);
		}else if(pos == POSITION.RIGHTTOP){
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
			params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
			params.addRule(RelativeLayout.LEFT_OF,gamer.getCoinViewId());
			params.addRule(RelativeLayout.ALIGN_BOTTOM, gamer.getCoinViewId());
			params.rightMargin = Util.dpToPx(5);
			addBell(params,gamer.getBlockTextViewId(),gamer.getBlockViewId(),RIGHTTOPBLOCKVIEWMSGID);
		}
	}
	private void addBell(RelativeLayout.LayoutParams params,int tid, int vid,int msgId){
		ImageView clock = buildImageView(R.drawable.playscene_3_clock);
		clock.setId(vid);
		addView(clock, params);
		TextView clockText = createClockTextView(tid);
		addClockTextView(clockText,clock.getId());
		ViewShowListen(tid, msgId);
	}
	private void addBeishuView(int addition){
		setBeiShuScoreView(getBeiShuValue()+addition);
	}
	private void setBeiShuScoreView(int value){
		updateTextView(findViewById(R.id.beishu_value_id, TextView.class),value+"");
	}
	private int getBeiShuValue(){
		try{
			return Integer.parseInt(findViewById(R.id.beishu_value_id, TextView.class).getText().toString());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	private void showPlayerViewMessage(GamerMessageEnum type,RelativeLayout.LayoutParams params,int gender){
		if(type==GamerMessageEnum.YIFEN){
			addBeishuView(1);
			playYiFen(gender);
			showPlayersViewMessage(R.drawable.playscene_3_call_point_1,params);
		}
		else if(type==GamerMessageEnum.ERFEN){
			addBeishuView(2);
			playErFen(gender);
			showPlayersViewMessage(R.drawable.playscene_3_call_point_2,params);
		}
		else if(type==GamerMessageEnum.SANFEN){
			addBeishuView(3);
			playSanFen(gender);
			showPlayersViewMessage(R.drawable.playscene_3_call_point_3,params);
		}else if(type==GamerMessageEnum.BUJIAO){
			playBuJiao(gender);
			showPlayersViewMessage(R.drawable.playscene_3_tip_no_call,params);
		}
		else if(type == GamerMessageEnum.BUCHU){
			playPass(gender);
			showPlayersViewMessage(R.drawable.playscene_tip_no_put,params);
		}
		else if(type == GamerMessageEnum.NOBIG)
			showPlayersViewMessage(R.drawable.game_note_nobig,params);
		else if(type == GamerMessageEnum.NORULE)
			showPlayersViewMessage(R.drawable.game_note_noput,params);
	}
	private void showRealPlayerViewNotification(GamerMessageEnum type){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, HEADNAMEVIEWID);
		showPlayerViewMessage(type,params,getGamer(POSITION.BOTTOM).getGender());
	}
	private void showNoteNoBigCards(){
		showNoteNotification(GamerMessageEnum.NOBIG);
	}
	private void showNoteNoRuleCards(){
		showNoteNotification(GamerMessageEnum.NORULE);
	}
	private void showNoteNotification(GamerMessageEnum type){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.ABOVE,R.id.store_coin_id);
		params.bottomMargin = Util.dpToPx(10);
		showPlayerViewMessage(type,params,getGamer(POSITION.BOTTOM).getGender());
	}
	private void showLeftTopRobotViewNotification(GamerMessageEnum type){
		Gamer gamer = getGamer(POSITION.LEFTTOP);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.RIGHT_OF,gamer.getCoinViewId());
		params.addRule(RelativeLayout.ALIGN_BOTTOM, gamer.getCoinViewId());
		showPlayerViewMessage(type,params,gamer.getGender());
	}
	private void showRightRobotViewNotification(GamerMessageEnum type){
		Gamer gamer = getGamer(POSITION.RIGHTTOP);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.LEFT_OF,gamer.getCoinViewId());
		params.addRule(RelativeLayout.ALIGN_BOTTOM, gamer.getCoinViewId());
		showPlayerViewMessage(type,params,gamer.getGender());
	}
	private void showPlayersViewMessage(int id,RelativeLayout.LayoutParams params){
		final ImageView view = buildImageView(id);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.custom_fade_out);
        animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				removeView(view);
			}
		});
		view.startAnimation(animation);
		addView(view, params);
	}
	private void showRealPlayerChoiseDialog(ImageViewItem one,ImageViewItem two,
			ImageViewItem three,ImageViewItem four){
		isTimeTickRunning = true;
		ImageView clock = buildImageView(R.drawable.playscene_3_clock);
		clock.setId(Util.getUniqueViewId());
		addClockImageView(clock);
		
		TextView clockText = createClockTextView(BLOCKVIEWID);
		addClockTextView(clockText,clock.getId());
		
		addImageLeft(two.getImageView(),clock.getId());
		addImageLeft(one.getImageView(),two.getImageView().getId());
		addImageRight(three.getImageView(), clock.getId());
		addImageRight(four.getImageView(), three.getImageView().getId());
		
		optionalCompViewMaps = new OptionalCompViewMap(one,two,three,four,clock,clockText);
		
		ViewShowListen(BLOCKVIEWID, BLOCKVIEWMSGID);
	}
	private TextView createClockTextView(int id){
		TextView clockText = new TextView(this);
		clockText.setText("00");
		clockText.setId(id);
		clockText.setTextAppearance(this,android.R.style.TextAppearance_Medium);
		clockText.setTextColor(Color.BLACK);
		return clockText;
	}
	private void addClockTextView(TextView textView,int clockId){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_TOP, clockId);
		params.addRule(RelativeLayout.ALIGN_LEFT,clockId);
		params.leftMargin = params.topMargin = Util.dpToPx(12);
		addView(textView, params);
	}
	private void updateTimeAreaText(int seconds,int id){
		updateTextView(findViewById(id, TextView.class),Util.getStringBy(seconds, 2));
	}
	private void addImageRight(ImageView image,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_BOTTOM,id);
		params.addRule(RelativeLayout.RIGHT_OF,id);
		params.leftMargin = Util.dpToPx(2);
		addView(image, params);
	}
	private void addImageLeft(ImageView image,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_BOTTOM,id);
		params.addRule(RelativeLayout.LEFT_OF,id);
		params.rightMargin = Util.dpToPx(2);
		addView(image, params);
	}
	private void addClockImageView(ImageView imageView){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_BOTTOM,HEADNAMEVIEWID);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		addView(imageView, params);
	}
	private void displayCards(){
		startNewThread(new Runnable() {
			BlockingQueue<String> bq=new ArrayBlockingQueue<String>(1);
			int leftMargin = getRealPokersLeftMargin();
			int pokerWidth = getRealPokersWidth();
			@Override
			public void run() {
				putAdataInBlockingQueue();
				for(final RealPlayerCardUnit unit : realPlayerCardsViews){
					startNewThread(new Runnable() {
						@Override
						public void run() {
							SoundEffectPlayer.getInstance().playSendCards();
							Animation anim = getAnimation();
							anim.setAnimationListener(new AnimationListenerImp());
							unit.getIv().setAnimation(anim);
							addRealPokerView(unit.getIv(),leftMargin,pokerWidth);
						}
						class AnimationListenerImp implements AnimationListener{
							@Override
							public void onAnimationStart(
									Animation paramAnimation) {
							}
							@Override
							public void onAnimationEnd(Animation paramAnimation) {
								getAdataFromBlockingQueue();
							}
							@Override
							public void onAnimationRepeat(
									Animation paramAnimation) {
							}
						}
						public Animation getAnimation(){
							return AnimationUtils.loadAnimation(
					                PlayActivity.this, R.anim.translate);
						}
					});
					putAdataInBlockingQueue();
					leftMargin += pokerWidth/2;
				}
				Log.i("display cards", "display cards complete");
				showChoiseScoreDialog();
			}
			public void getAdataFromBlockingQueue(){
				try {
					bq.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			public void putAdataInBlockingQueue(){
				try {
					bq.put("");
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	private void startNewThread(Runnable runnable){
		new Thread(runnable).start();
	}
	private void addRealPokerView(ImageView iv,int leftMargin,int pokerWidth){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = pokerWidth;
		params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.leftMargin = leftMargin;
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.ABOVE, R.id.store_coin_id);
		addView(iv, params);
	}
	private int getRealPokersWidth(){
		if(getRealPokersLeftMargin()!=0)
			return getBitmapFromImageView(buildImageView(R.drawable.poker_clue_10)).getWidth();
		else
		{
			return (getScreenWidth()/(realPlayerCardsViews.size()+1))*2;
		}
	}
	private int getRealPokersLeftMargin(){
		int count = realPlayerCardsViews.size()+1;
		int halfWidth = getBitmapFromImageView(buildImageView(R.drawable.poker_clue_10)).getWidth()/2;
		int leftMargin = (getScreenWidth()-count*halfWidth)/2;
		if(leftMargin < 0)
			leftMargin = 0;
		return leftMargin;
	}
	private void adjustRealPlayerBackData(List<Poker> pokers){
		for(Poker p : pokers){
			realPlayerCardsViews.add(new RealPlayerCardUnit(p,
					getImageViewFromPoker(p)));
		}
	}
	private ImageView getImageViewFromPoker(Poker poker){
		int drawableId = PokerUtil.getImageDrawableIdFrom(poker);
		ImageView iview = buildImageView(drawableId);
		iview.setScaleType(ScaleType.FIT_XY);
		return iview;
	}
	private void setRealViewScore(int score){
		removeRealPlayerScoreViews();
		addRealPlayerScoreViews(score);
	}
	private void addRealPlayerScoreViews(int score){
		String strScore = score+"";
		int id = R.id.store_coin_id;
		for(int index=0; index<strScore.length(); index++){
			char ch = strScore.charAt(index);
			ImageView imageView = buildImageView(ScoinNumUnitUtil.getCoinsNumIdByNumber(ch));
			imageView.setId(Util.getUniqueViewId());
			RelativeLayout.LayoutParams params  = new RelativeLayout.LayoutParams(0, 0);
			params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
			params.addRule(RelativeLayout.RIGHT_OF, id);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params.bottomMargin = Util.dpToPx(2);
			addView(imageView, params);
			realPlayerScoreViews.add(imageView);
			id = imageView.getId();
		}
	}
	private void removeRealPlayerScoreViews(){
		for(ImageView iv:realPlayerScoreViews){
			removeView(iv);
		}
	}
	private void removeDiPaiViews(){
		for(CardsDiPaiUnit unit : diPaiUnits){
			removeView(unit.getView());
		}
	}
	private void drawDiPaiView(){
		int id = R.id.dishu_value_text_id;
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.skin_card_3_card_bk_s);
		for(CardsDiPaiUnit unit : diPaiUnits){
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
			params.width = bitmap.getWidth();
			params.height = bitmap.getHeight();
			params.addRule(RelativeLayout.LEFT_OF, id);
			params.rightMargin = Util.dpToPx(5);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.topMargin = Util.dpToPx(2);
			addView(unit.getView(), params);
			id = unit.getView().getId();
		}
	}
	private void drawBeishuView(){
		findViewById(R.id.beishu_value_id, TextView.class).setText("1");
	}
	private void drawDiShuView(){
		findViewById(R.id.dishu_value_id, TextView.class).setText("5");
	}
	private void drawRobotsPicView(){
		addLeftTopGamerInfo(getGamer(POSITION.LEFTTOP));
		addRightTopGamerInfo(getGamer(POSITION.RIGHTTOP));
	}
	private void addRightTopGamerInfo(Gamer gamer){
		RelativeLayout.LayoutParams framePicParams = getHeadFramePicParams();
		framePicParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		framePicParams.rightMargin = 30;
		framePicParams.topMargin = 10;
		addGamerInfo(gamer,framePicParams);
	}
	private void addLeftTopGamerInfo(Gamer gamer){
		RelativeLayout.LayoutParams framePicParams = getHeadFramePicParams();
		framePicParams.leftMargin = framePicParams.topMargin = 10;
		addGamerInfo(gamer,framePicParams);
	}
	private void addGamerInfo(Gamer gamer,RelativeLayout.LayoutParams framePicParams){
		ImageView headFrame = createFramePic();
		gamer.setHeadImgViewId(headFrame.getId());
		addLeftTopFramePic(headFrame,framePicParams);
		addHeadImg(createHeadImg(gamer.getHead_img()),headFrame.getId(),headFrame.getLayoutParams());
		TextView headNameView = createHeadName(gamer.getName());
		addHeadName(headNameView,headFrame.getId());
		TextView coinsView = createHeadCoins(gamer.getCoins(),gamer.getCoinViewId());
		addHeadCoins(coinsView,headNameView.getId());
		ImageView pokerBackLittleIconView = createPokerBackLittleIcon();
		addPokerBackLittleIcon(pokerBackLittleIconView,coinsView.getId());
		ImageView tenth = createCardsNumViews();
		addCardsNumTenthViews(tenth,pokerBackLittleIconView.getId());
		ImageView quantile = createCardsNumViews();
		addCardsNumQuantileViews(quantile,tenth.getId());
		addTenthAndQuantile(gamer.getPosition(),tenth,quantile);
		updateCardsNumUnitViews(gamer.getPosition(),gamer.getPokersCount());
	}
	private void updateCardsNumUnitViews(POSITION pos,int pokerCounts){
		CardsNumUnit unit = getCardsNumUnit(pos);
		String str = CardsNumUnitUtil.getPokersCountStr(pokerCounts);
		
		unit.updateTenth(BitmapFactory.decodeResource(getResources(), 
				CardsNumUnitUtil.getCardsNumIdByNumber(str.charAt(0))));
		
		unit.updateQuantile(BitmapFactory.decodeResource(getResources(), 
				CardsNumUnitUtil.getCardsNumIdByNumber(str.charAt(1))));
		validateView(unit.getTenth());
		validateView(unit.getQuantile());
	}
	private void validateView(ImageView imageView){
		if(getMainLooper() == Looper.myLooper())
			imageView.invalidate();
		else
			imageView.postInvalidate();
	}
	private void addTenthAndQuantile(POSITION pos,ImageView tenth,ImageView quantile){
		CardsNumUnit unit = getCardsNumUnit(pos);
		unit.setTenth(tenth);
		unit.setQuantile(quantile);
	}
	private CardsNumUnit getCardsNumUnit(POSITION pos){
		for(CardsNumUnit unit : cardsNumViews){
			if(unit.getPosition().equals(pos))
				return unit;
		}
		return null;
	}
	private void addCardsNumQuantileViews(ImageView imageView,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.RIGHT_OF, id);
		params.addRule(RelativeLayout.ALIGN_BOTTOM,id);
		params.leftMargin = Util.dpToPx(2);
		addView(imageView, params);
	}
	private void addCardsNumTenthViews(ImageView imageView,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.RIGHT_OF, id);
		params.addRule(RelativeLayout.ALIGN_BOTTOM,id);
		params.leftMargin = Util.dpToPx(8);
		params.bottomMargin = Util.dpToPx(4);
		addView(imageView, params);
	}
	private ImageView createCardsNumViews(){
		ImageView imageView = buildImageView(R.drawable.playscene_2_cardnum0);
		imageView.setId(Util.getUniqueViewId());
		return imageView;
	}
	private void addPokerBackLittleIcon(ImageView imageView,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_LEFT, id);
		params.addRule(RelativeLayout.BELOW, id);
		params.topMargin = Util.dpToPx(5);
		addView(imageView, params);
	}
	private ImageView createPokerBackLittleIcon(){
		ImageView imageView = buildImageView(R.drawable.skin_card_3_cardfornum);
		imageView.setId(Util.getUniqueViewId());
		return imageView;
	}
	private void addHeadCoins(TextView tv,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = Util.dpToPx(100);
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_LEFT, id);
		params.addRule(RelativeLayout.BELOW, id);
		addView(tv,params);
	}
	private TextView createHeadCoins(int coins,int coinViewId){
		TextView textView = new TextView(this);
		textView.setText("½ð±Ò:"+coins);
		textView.setTextSize(11);
		textView.setTextColor(Color.YELLOW);
		textView.setId(coinViewId);
		return textView;
	}
	private void addHeadName(TextView tv,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = Util.dpToPx(100);
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_LEFT, id);
		params.addRule(RelativeLayout.BELOW, id);
		params.topMargin = Util.dpToPx(10);
		addView(tv,params);
	}
	private TextView createHeadName(String name){
		TextView tv = new TextView(this);
		tv.setText(name);
		tv.setTextSize(12);
		tv.setTextColor(Color.WHITE);
		tv.setId(Util.getUniqueViewId());
		return tv;
	}
	private void addHeadImg(ImageView view,int id,ViewGroup.LayoutParams p){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width =p.width-2;
		params.height = p.height-2;
		params.addRule(RelativeLayout.ALIGN_LEFT,id);
		params.addRule(RelativeLayout.ALIGN_TOP,id);
		addView(view, params);
	}
	private ImageView createHeadImg(int drawableId){
		ImageView headImg = buildImageView(drawableId);
		headImg.setId(Util.getUniqueViewId());
		return headImg;
	}
	private int addLeftTopFramePic(ImageView view,RelativeLayout.LayoutParams params){
		addView(view, params);
		return view.getId();
	}
	private RelativeLayout.LayoutParams getHeadFramePicParams(){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = Util.dpToPx(50);
		return params;
	}
	private ImageView createFramePic(){
		ImageView headbg = buildImageView(R.drawable.gameroom_room_head_bg);
		ImageView headFrame = buildImageView(R.drawable.gameroom_room_head_frame);
		headFrame.setBackground(headbg.getDrawable());
		headFrame.setId(Util.getUniqueViewId());
		return headFrame;
	}
	private Gamer getGamer(POSITION pos){
		for(Gamer g : gamers){
			if(g.isPos(pos))
				return g;
		}
		return null;
	}
	protected void sendCards(){
		List<Poker> boxCards = createABoxCards();
		shiftCards(boxCards);
		distributeCards(boxCards);
		sortCards();
	}
	protected void sortCards(){
		for(Gamer gamer : gamers){
			List<Poker> pokers = gamer.getPokers();
			for(int oi = 0; oi < pokers.size(); oi++){
				for(int ii=0; ii < pokers.size()-oi-1; ii++){//use bubbling's sort
					if((pokers.get(ii).getP().getOrder() - 
							pokers.get(ii+1).getP().getOrder()) < 0){
						Poker temp = pokers.get(ii);
						pokers.set(ii, pokers.get(ii+1));
						pokers.set(ii+1,  temp);
					}
				}
			}
		}
	}
	protected void shiftCards(List<Poker> pokers){
		java.util.Collections.shuffle(pokers,new Random(System.currentTimeMillis()));
	}
	protected void distributeCards(List<Poker> boxCards){
		for(int i=0; i<boxCards.size();i++){
			if(i<3)
				addDiPai(boxCards.get(i));
			else
				gamers.get(i%3).addPoker(boxCards.get(i));
		}
	}
	protected void addDiPai(Poker poker){
		ImageView ivw = buildImageView(R.drawable.skin_card_3_card_bk_s);
		ivw.setId(Util.getUniqueViewId());
		diPaiUnits.add(new CardsDiPaiUnit(
				ivw,
				poker));
	}
	protected List<Poker> createABoxCards(){
		P[] ignoreTypeCards = new P[]{
				P.P3,P.P4,P.P5,P.P6,P.P7,P.P8,P.P9,P.P10,P.PJ,P.PQ,P.PK,P.PA,P.P2
		};
		PokerTypeEnum[] types = new PokerTypeEnum[]{
				PokerTypeEnum.Clue,PokerTypeEnum.Diamond,
				PokerTypeEnum.Heart,PokerTypeEnum.Spade
		};
		List<Poker> pokers = new ArrayList<Poker>();
		for(P p : ignoreTypeCards)
			for(PokerTypeEnum type : types)
				pokers.add(new Poker(p, type));
		pokers.add(new Poker(P.PXW, PokerTypeEnum.None));
		pokers.add(new Poker(P.PDW, PokerTypeEnum.None));
		return pokers;
	}
	protected void addGamers(){
		gamers.add(createPriviousGamer());
		gamers.add(createRealGamer());
		gamers.add(createNextGamer());
	}
	protected Gamer createPriviousGamer(){
		return RobotFactory.instance().build(POSITION.LEFTTOP);
	}
	protected Gamer createNextGamer(){
		return RobotFactory.instance().build(POSITION.RIGHTTOP);
	}
	protected Gamer createRealGamer(){
		return new Gamer(getPlayer(), POSITION.BOTTOM,Util.getUniqueViewId());
	}
	@Override
	protected void onStop() {
		Log.i("ungister", "unregister success");
		unregisterReceiver(receiver);		
		super.onStop();
	}
	@Override
	protected void onStart() {
		Log.i("register", "register success");
		receiver = new ScreenBroadcastReceiver();
		startScreenBroadcastReceiver();
		super.onStart();
	}
	@Override
	public void finish() {
		super.finish();
	}
    private void startScreenBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);
    }
    ScreenBroadcastReceiver receiver;
    class ScreenBroadcastReceiver extends BroadcastReceiver {
        String action = null;
        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            Log.i("action", action+"");
            if (Intent.ACTION_SCREEN_ON.equals(action)){
            	startToAnotherActivity(InterceptActivity.class);
            }
        }
    }
    private void playYiFen(int gender){
    	if(gender==Property.BOY)
    		SoundEffectPlayer.getInstance().playYiFenNan();
    	else
    		SoundEffectPlayer.getInstance().playYiFenNv();
    }
	private void playErFen(int gender){
    	if(gender==Property.BOY)
    		SoundEffectPlayer.getInstance().playErFenNan();
    	else
    		SoundEffectPlayer.getInstance().playErFenNv();
	}
	private void playSanFen(int gender){
    	if(gender==Property.BOY)
    		SoundEffectPlayer.getInstance().playSanFenNan();
    	else
    		SoundEffectPlayer.getInstance().playSanFenNv();
	}
	private void playBuJiao(int gender){
    	if(gender==Property.BOY)
    		SoundEffectPlayer.getInstance().playBuJiaoNan();
    	else
    		SoundEffectPlayer.getInstance().playBuJiaoNv();
	}
	private void playPass(int gender){
		int val =  Util.getRandom(0, 3);
		if( 0 == val){
			if(gender == Property.BOY)
				SoundEffectPlayer.getInstance().playGuoEngNan();
			else
				SoundEffectPlayer.getInstance().playGuoEngNv();
		}else if(1 == val){
			if(gender == Property.BOY)
				SoundEffectPlayer.getInstance().playBuYaoNan();
			else
				SoundEffectPlayer.getInstance().playBuYaoNv();
		}else{
			if(gender == Property.BOY)
				SoundEffectPlayer.getInstance().playYaoBuQiNan();
			else
				SoundEffectPlayer.getInstance().playYaoBuQiNv();
		}
	}
    @Override
    protected void onPause() {
    	MusicPlayer.getInstance().pause();
    	super.onPause();
    }
    @Override
    protected void onResume() {
    	super.onResume();
    	if(isBackFromBackground){
    		startToAnotherActivity(InterceptActivity.class);
    	}else{
    		MusicPlayer.getInstance().start();
    	}
    }
}
