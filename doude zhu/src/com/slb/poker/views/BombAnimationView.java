package com.slb.poker.views;

import com.slb.poker.R;

import android.content.Context;
import android.graphics.Rect;

public class BombAnimationView extends PokerAnimationView{
	public BombAnimationView(Context context) {
		super(context,R.drawable.bomb);
	}
	@Override
	protected Rect getDestinationRect(int step) {
		int celly = bitmap.getHeight()/4;
		return new Rect(0, celly*(step)
				, bitmap.getWidth(), 
				celly*(step+1));
	}
}
