package com.slb.poker.views;

import com.slb.poker.listener.GameDialogStopListener;

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

public abstract class PokerAnimationView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    boolean mRunning=false;
    SurfaceHolder holder=null;
    Thread td=null;
    Paint paint = null;
    Bitmap bitmap = null;
    int millSecond = 100;
    GameDialogStopListener listener;
    public PokerAnimationView(Context context,int drawableId) {
        super(context);
        holder=getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        paint = new Paint();
        bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
    }
    public void setCallbackListener(GameDialogStopListener listener){
    	this.listener = listener;
    }
    public GameDialogStopListener getCallbackListener(){
    	return this.listener;
    }
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mRunning=true;
        td=new Thread(this);
        td.start();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mRunning=false;
    }
    @Override
    public void run() {
        Canvas c = null;
        int count = 4;
        int step =0;
        while(mRunning){
            try{
            	c = null;
                if(count>=4){
                    c = holder.lockCanvas();
                	c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
                	c.drawBitmap(bitmap, getDestinationRect(step), new Rect(0, 0, getWidth(), getHeight()), paint);
                	if(step==3){
                		mRunning = false;
                	}
                	step++;
                	count=0;
                }
                Thread.sleep(millSecond);
                count++;
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(c!= null)
                    holder.unlockCanvasAndPost(c);
            }
        }
        if(listener!=null)
        	listener.call();
    }
    protected abstract Rect getDestinationRect(int step);
}
