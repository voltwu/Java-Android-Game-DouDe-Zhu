package com.slb.poker.views;

import java.util.ArrayList;
import java.util.List;

import com.ai.poker.Poker;
import com.slb.poker.utils.PokerUtil;
import com.slb.poker.utils.Property;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class PokerPackageView extends View{
	public int pokerWidth = 0;
	public int pokerHeight = 0;
	public List<Bitmap> list = null;
	public Paint paint = null;
	public boolean isRealPlayer = false;
	public PokerPackageView(Context context,List<Poker> pokers,int pokerWidth,int pokerHeight,boolean isRealPlayer) {
		super(context);
		this.pokerWidth = pokerWidth;
		this.pokerHeight = pokerHeight;
		this.list = new ArrayList<Bitmap>();
		this.paint = new Paint();
		for(int index=0; index<pokers.size(); index++){
			list.add(BitmapFactory.decodeResource(getResources(), PokerUtil.getImageDrawableIdFrom(pokers.get(index))));
		}
		this.isRealPlayer = isRealPlayer;
	}
	public PokerPackageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public PokerPackageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		int x = 0;
		int y = 0;
		for(int index=0; index<list.size(); index++){
			canvas.drawBitmap(list.get(index), null, new Rect(x,y,x+pokerWidth,pokerHeight+y
					), paint);
			x += pokerWidth/2;
			if(!isRealPlayer && index != 0 && (index%(Property.ROBOTINLINEMAXCARDS-1)==0)){
				x = 0;
				y = Property.ROBOTMAXMOVEDOWN;
			}
		}
	}
	
}
