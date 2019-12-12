package com.slb.poker.views;

import com.slb.poker.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class AirplaneAnimationView extends ImageView{
	
	public AirplaneAnimationView(Context context) {
		super(context);
		setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.airplane));
	}
	
}
