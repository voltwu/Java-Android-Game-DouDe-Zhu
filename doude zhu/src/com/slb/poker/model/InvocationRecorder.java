package com.slb.poker.model;

import java.util.ArrayList;
import java.util.List;

import com.ai.poker.Poker;

public class InvocationRecorder {
	private static InvocationRecorder invocationRecorder = new InvocationRecorder();
	public static InvocationRecorder instance(){
		return invocationRecorder;
	}
	private InvocationRecorder(){
		this.list = new ArrayList<RecorderModel>();
	}
	private List<RecorderModel> list = null;
	
	public void add(List<Poker> pokers,POSITION pos,boolean isDiZhu){
		list.add(new RecorderModel(pokers, pos, isDiZhu));
	}
	public boolean isPositive(POSITION pos){
		if(list.size() == 0)
			return true;
		for(int index=list.size()-1; index>=0; index--){
			if( list.get(index).getPokers()!=null &&
				list.get(index).getPokers().size()!=0){
				if(pos == list.get(index).getPos()){
					clear();
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	public void clear(){
		list.clear();
	}
	public List<Poker> getLastPokers() {
		for(int index=list.size()-1; index>=0; index--){
			if(list.get(index).getPokers()!=null &&
				list.get(index).getPokers().size()!=0){
				return list.get(index).getPokers();
			}
		}
		return null;
	}
	public POSITION getLastSendPOSITION(){
		for(int index=list.size()-1; index>=0; index--){
			if( list.get(index).getPokers()!=null &&
				list.get(index).getPokers().size()!=0){
				return list.get(index).getPos();
			}
		}
		return null;
	}
	public boolean isStart() {
		return list.size() == 0;
	}
}
