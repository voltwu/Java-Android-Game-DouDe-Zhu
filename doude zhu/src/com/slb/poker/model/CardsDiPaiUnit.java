package com.slb.poker.model;

import com.ai.poker.Poker;

import android.widget.ImageView;

public class CardsDiPaiUnit {
	private ImageView view;
	private Poker poker;
	public CardsDiPaiUnit(ImageView view,Poker poker){
		this.view = view;
		this.poker = poker;
	}
	public ImageView getView(){
		return view;
	}
	public Poker getPoker(){
		return poker;
	}
	public void setView(ImageView view){
		this.view = view;
	}
	public void setPoker(Poker poker){
		this.poker = poker;
	}
}
