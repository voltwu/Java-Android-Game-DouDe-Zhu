package com.slb.poker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class SelectedBoxView extends View{

	public SelectedBoxView(Context context) {
		super(context);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStrokeWidth(3);
		canvas.drawLine(0, 0, getWidth(), 0,paint);
		canvas.drawLine(0, 0, 0, getHeight(), paint);
		canvas.drawLine(getWidth(), getHeight(),getWidth(),0, paint);
		canvas.drawLine(getWidth(),getHeight(), 0, getHeight(), paint);
	}
}
