package com.slb.poker.model;

import java.util.HashMap;
import java.util.Map;

public class NumberOfChuPaiRecorder {
	private static NumberOfChuPaiRecorder recorder = new NumberOfChuPaiRecorder();
	private Map<POSITION,Integer> map = null;
	private NumberOfChuPaiRecorder(){
		map = new HashMap<POSITION,Integer>();
	}
	public static NumberOfChuPaiRecorder getInstance(){
		return recorder;
	}
	public void clear(){
		map.clear();
	}
	public void addOne(POSITION pos){
		Integer integer = get(pos);
		map.put(pos, integer+1);
	}
	public int get(POSITION pos){
		Integer integer = (Integer)map.get(pos);
		if(integer==null) integer=0;
		return integer;
	}
}
