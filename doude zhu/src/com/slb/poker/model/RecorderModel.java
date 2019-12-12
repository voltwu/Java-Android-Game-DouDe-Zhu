package com.slb.poker.model;

import java.util.List;

import com.ai.poker.Poker;

public class RecorderModel {
	private List<Poker> pokers;
	private POSITION pos;
	private boolean isDiZhu = false;
	
	public RecorderModel(List<Poker> pokers,POSITION pos,boolean isDiZhu){
		this.pokers = pokers;
		this.pos = pos;
		this.isDiZhu = isDiZhu;
	}

	public List<Poker> getPokers() {
		return pokers;
	}

	public void setPokers(List<Poker> pokers) {
		this.pokers = pokers;
	}

	public POSITION getPos() {
		return pos;
	}

	public void setPos(POSITION pos) {
		this.pos = pos;
	}

	public boolean isDiZhu() {
		return isDiZhu;
	}

	public void setDiZhu(boolean isDiZhu) {
		this.isDiZhu = isDiZhu;
	}
	
}
