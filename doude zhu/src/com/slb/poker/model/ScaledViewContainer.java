package com.slb.poker.model;

import java.util.Map;

import android.view.View;

public class ScaledViewContainer {
	private View view = null;
	private Map<?,?> map = null;
	public ScaledViewContainer(View view,Map<String,String> map){
		this.view = view;
		this.map = map;
	}
	public View getView(){
		return view;
	}
	public Map<?,?> getMap(){
		return map;
	}
}
