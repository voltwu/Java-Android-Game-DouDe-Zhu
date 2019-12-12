package com.slb.poker.model;

import java.util.HashMap;
import java.util.Map;

import com.slb.poker.views.PokerPackageView;

public class PokerPackageViewRecorder {
	private static PokerPackageViewRecorder pokerViews = new PokerPackageViewRecorder();
	private PokerPackageViewRecorder(){
		this.data = new HashMap<POSITION, PokerPackageView>();
	}
	public static PokerPackageViewRecorder instance(){
		return pokerViews;
	}
	private Map<POSITION,PokerPackageView> data;
	public void setData(POSITION pos,PokerPackageView ppv){
		data.put(pos, ppv);
	}
	public PokerPackageView get(POSITION pos){
		return data.get(pos);
	}
	public void clear() {
		data.clear();
	}
}
