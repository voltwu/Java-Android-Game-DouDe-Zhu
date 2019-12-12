package com.slb.poker.model.user;

import java.util.ArrayList;
import java.util.List;

import com.ai.poker.Poker;
import com.slb.poker.model.POSITION;
import com.slb.poker.utils.Util;

public class Gamer extends Player{
	POSITION position;
	List<Poker> pokers;
	int coinViewId = 0;
	int blockViewId = 0;
	int blockTextViewId = 0;
	boolean dizhu = false;
	int headImgViewId = 0;
	public Gamer(Player player,POSITION position, int coinViewId){
		super(player);
		this.position = position;
		this.pokers = new ArrayList<Poker>();
		this.coinViewId = coinViewId;
		this.blockViewId = Util.getUniqueViewId();
		this.blockTextViewId = Util.getUniqueViewId();
		this.dizhu = false;
	}
	public Gamer(POSITION position){
		this.position = position;
		pokers = new ArrayList<Poker>();
		this.blockViewId = Util.getUniqueViewId();
		this.blockTextViewId = Util.getUniqueViewId();		
		this.dizhu = false;
	}
	public void setHeadImgViewId(int headImgViewId){
		this.headImgViewId = headImgViewId;
	}
	public int getHeadImgViewId(){
		return headImgViewId;
	}
	public void setDiZhu(boolean b){
		this.dizhu = b;
	}
	public boolean isDiZhu(){
		return dizhu;
	}
	public int getBlockViewId(){
		return blockViewId;
	}
	public int getBlockTextViewId(){
		return blockTextViewId;
	}
	public int getCoinViewId(){
		return coinViewId;
	}
	public void setCoinViewId(int coinViewId){
		this.coinViewId = coinViewId;
	}
	public void addPoker(Poker p){
		pokers.add(p);
	}
	public POSITION getPosition(){
		return position;
	}
	public boolean isPos(POSITION pos){
		return pos.equals(position)?true:false;
	}
	public List<Poker> getPokers(){
		return pokers;
	}
	public int getPokersCount(){
		return pokers.size();
	}
	public void addPokers(List<Poker> list) {
		for(Poker p : list){
			pokers.add(p.clone());
		}
	}
}
