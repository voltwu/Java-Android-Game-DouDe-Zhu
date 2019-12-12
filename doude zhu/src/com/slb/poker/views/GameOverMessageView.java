package com.slb.poker.views;

import com.slb.poker.R;
import com.slb.poker.listener.GameDialogStopListener;
import com.slb.poker.utils.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameOverMessageView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
	boolean iswin = false;
	int leftCoins = 0;
	int bottomCoins = 0;
	int rightCoins = 0;
	Paint paint = null;
	int chuntian = 0;
	int dishu = 0;
	int beishu = 0;
	int zhadan = 0;
	int step = 0;
	int item = 0;
	int maxStep = 0;
	GameDialogStopListener gameDialogStopListener;
	public GameOverMessageView(Context context,boolean iswin,
			int leftCoins,
			int bottomCoins,
			int rightCoins,
			int chuntian,
			int zhadan,
			int beishu,
			int dishu) {
		super(context);
		this.iswin = iswin;
		this.leftCoins = leftCoins;
		this.bottomCoins = bottomCoins;
		this.rightCoins = rightCoins;
		this.paint = new Paint();
		this.chuntian = chuntian;
		this.zhadan = zhadan;
		this.beishu = beishu;
		this.dishu = dishu;
        holder=getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);		
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        maxStep = Util.dpToPx(50);
        item = 0;
	}
	public void setGameDialogStopListener(GameDialogStopListener gameDialogStopListener){
		this.gameDialogStopListener = gameDialogStopListener;
	} 
	private void drawFailView(Canvas canvas){
		int[] ids = new int[]{
				R.drawable.anifail_1,
				R.drawable.anifail_2,
				R.drawable.anifail_3,
				R.drawable.anifail_4,
		};
		int id = 0;
		int dur = 2;
		if(item<=dur) id = ids[0];
		else if(item<=dur*2) id = ids[1];
		else if(item<=dur*3) id = ids[2];
		else id = ids[3];
		item++;
		drawView(canvas,id);
	}
	private void drawWinView(Canvas canvas){
		drawView(canvas,R.drawable.aniwin);
	}
	private void drawView(Canvas canvas,int id){
		Bitmap bitmapBottomBg = createBitmap(R.drawable.play_bottom_bg);
		Bitmap bitmaplog = createBitmap(id);
		int left = (getWidth() - bitmapBottomBg.getWidth())/2;
		int top = (getHeight()-bitmaplog.getHeight()-bitmapBottomBg.getHeight())/2;
		canvas.drawBitmap(bitmapBottomBg, 
				null, 
				new Rect(left, 
						top + bitmaplog.getHeight(), 
						left + bitmapBottomBg.getWidth(), 
						top + bitmaplog.getHeight()+bitmapBottomBg.getHeight()), 
				paint);
		int left2 = (getWidth() - bitmaplog.getWidth())/2;
		canvas.drawBitmap(bitmaplog,null,
				new Rect(left2,top,
						left2+bitmaplog.getWidth(),
						top+bitmaplog.getHeight()),
				paint);
		drawCoins(canvas,left,top + bitmaplog.getHeight()-Util.dpToPx(30)-step,bottomCoins,getPaintAlpha(step,maxStep));
		drawCoins(canvas, left-Util.dpToPx(20), top-step, leftCoins,getPaintAlpha(step,maxStep));
		drawCoins(canvas, left+bitmapBottomBg.getWidth()-Util.dpToPx(30), top-step, rightCoins,getPaintAlpha(step,maxStep));
		int itemTop = top+ bitmaplog.getHeight()+bitmapBottomBg.getHeight()-Util.dpToPx(35);
		int itemcellWidth = (bitmapBottomBg.getWidth()-Util.dpToPx(30))/4;
		drawBottomItems(canvas,left+Util.dpToPx(30),itemTop,R.drawable.play_bottom_dishu,dishu);
		drawBottomItems(canvas,left+Util.dpToPx(30)+itemcellWidth,itemTop,R.drawable.play_bottom_beishu,beishu);
		drawBottomItems(canvas,left+Util.dpToPx(30)+itemcellWidth*2,itemTop,R.drawable.play_bottom_zhadan,zhadan);
		drawBottomItems(canvas,left+Util.dpToPx(30)+itemcellWidth*3,itemTop,R.drawable.play_bottom_chuntian,chuntian);
		step+=5;
		if(step>=maxStep){
			mRunning = false;
		}
	}
	private int getPaintAlpha(int step,int max){
		double doub = step*1.0/max;
		int v = (int)(255*(1-doub));
		if(v<0) v = 0;
		return v;
	}
	private void drawBottomItems(Canvas canvas,int left,int top,int id,int val){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
		canvas.drawBitmap(bitmap,left,top, paint);
		drawCharacter(canvas, left+bitmap.getWidth(), top, val+"");
	}
	private void drawCoins(Canvas canvas,int left,int top,int coins,int alpha){
		String coin = coins+"";
		if(!coin.startsWith("-"))
			coin = "+"+coin;
		coin+="金币";
		Paint paint = new Paint();
		paint.setAlpha(alpha);
		drawCharacter(canvas, left, top, coin,paint);
	}
	private void drawCharacter(Canvas canvas,int left,int top,String str,Paint paint){
		for(int index=0; index<str.length(); index++){
			char ch = str.charAt(index);
			int id = getDrawableId(ch);
			Bitmap bp = createBitmap(id);
			canvas.drawBitmap(bp, left, top, paint);
			left+=bp.getWidth();
		}
	}
	private void drawCharacter(Canvas canvas,int left,int top,String str){
		drawCharacter(canvas,left,top,str,paint);
	}
	private int getDrawableId(char c){
		int drawableId = 0;
		switch(c){
			case '+':
				drawableId = R.drawable.goldnum_1_plus;
				break;
			case '-':
				drawableId = R.drawable.goldnum_1_subtract;
				break;
			case '0':
				drawableId = R.drawable.goldnum_1_zero;
				break;
			case '1':
				drawableId = R.drawable.goldnum_1_one;
				break;
			case '2':
				drawableId = R.drawable.goldnum_1_two;
				break;
			case '3':
				drawableId = R.drawable.goldnum_1_three;
				break;
			case '4':
				drawableId = R.drawable.goldnum_1_four;
				break;
			case '5':
				drawableId = R.drawable.goldnum_1_five;
				break;
			case '6':
				drawableId = R.drawable.goldnum_1_six;
				break;
			case '7':
				drawableId = R.drawable.goldnum_1_seven;
				break;
			case '8':
				drawableId = R.drawable.goldnum_1_eight;
				break;
			case '9':
				drawableId = R.drawable.goldnum_1_nine;
				break;
			case '金':
				drawableId = R.drawable.goldnum_1_jing;
				break;
			case '币':
				drawableId = R.drawable.goldnum_1_bi;
				break;
		}
		return drawableId;
	}
	private Bitmap createBitmap(int drawableId){
		return BitmapFactory.decodeResource(getResources(), drawableId);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
	}
    boolean mRunning=false;
    SurfaceHolder holder=null;
    Thread td=null;
	@Override
	public void run() {
	     Canvas c = null;
         while(mRunning){
             try{
                 c = holder.lockCanvas();
                 c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
			         if(iswin){
			        	 drawWinView(c);
			 		}else{
			 			drawFailView(c);
			 		}
                 Thread.sleep(100);
		     }catch(Exception e){
		         e.printStackTrace();
		     }finally{
		         if(c!= null)
		             holder.unlockCanvasAndPost(c);//结束锁定画图，并提交改变。
		     }
         }
         if(gameDialogStopListener!=null)
         gameDialogStopListener.call();
	}
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	}
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
        mRunning=true;
        td=new Thread(this);
        td.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		mRunning=false;
	}
}
