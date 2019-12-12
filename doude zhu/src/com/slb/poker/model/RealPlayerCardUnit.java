package com.slb.poker.model;

import com.ai.poker.Poker;

import android.widget.ImageView;

public class RealPlayerCardUnit {
	private Poker poker;
	private ImageView iv;
	private boolean isPreSelected = false;
	
	public RealPlayerCardUnit(Poker poker,ImageView iv){
		this.poker = poker;
		this.iv = iv;
		this.isPreSelected = false;
	}
	public boolean isPreSelected(){
		return isPreSelected;
	}
	public void trigger(){
		this.isPreSelected = !this.isPreSelected;
	};
	public void setPreSelected(Boolean b){
		this.isPreSelected = b;
	}
	public Poker getPoker(){
		return poker;
	}
	public void setPoker(Poker poker){
		this.poker = poker;
	}
	public ImageView getIv(){
		return iv;
	}
	public void setIv(ImageView iv){
		this.iv = iv;
	}
}
