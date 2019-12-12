package com.slb.poker.views;

import android.content.Context;
import android.graphics.Rect;

import com.slb.poker.R;

public class ShunZiAnimationView extends PokerAnimationView{
	public ShunZiAnimationView(Context context) {
		super(context, R.drawable.shunzi);
	}
	@Override
	protected Rect getDestinationRect(int step) {
		int cellh = bitmap.getHeight()/4;
		int cellw = bitmap.getWidth();
		if(step==0){
			return new Rect(0, 0, cellw, cellh);
		}
		else if(step==1){
			return new Rect(0,cellh-1,cellw,cellh*2-1);
		}else if(step==2){
			return new Rect(1,cellh*2+2,cellw+1,cellh*3+2);
		}else{
			return new Rect(0,cellh*3+2,cellw,cellh*4+2);
		}
	}
}
