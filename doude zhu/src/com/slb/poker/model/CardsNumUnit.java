package com.slb.poker.model;

import com.slb.poker.utils.Util;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class CardsNumUnit {
	private POSITION position;
	private ImageView tenth;
	private ImageView quantile;
	public CardsNumUnit(POSITION position){
		this.position = position;
	}
	public POSITION getPosition() {
		return position;
	}
	public void setPosition(POSITION position) {
		this.position = position;
	}
	public ImageView getTenth() {
		return tenth;
	}
	public void setTenth(ImageView tenth) {
		this.tenth = tenth;
	}
	public ImageView getQuantile() {
		return quantile;
	}
	public void setQuantile(ImageView quantile) {
		this.quantile = quantile;
	}
	public void updateTenth(final Bitmap bm) {
		Util.runInMainThread(new Runnable() {
			@Override
			public void run() {
				tenth.setImageBitmap(bm);
			}
		});
	}
	public void updateQuantile(final Bitmap bm){
		Util.runInMainThread(new Runnable() {
			@Override
			public void run() {
				quantile.setImageBitmap(bm);
			}
		});
	}
}
