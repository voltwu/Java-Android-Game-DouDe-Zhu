package com.slb.poker.model;


public class TouchLocRecorder {
	private static TouchLocRecorder touchLocRecorder = new TouchLocRecorder();
	private TouchLocRecorder(){}
	
	public static TouchLocRecorder instance(){
		return touchLocRecorder;
	}
	private Point begin=null;
	private Point end = null;
	public Point getBegin() {
		return begin;
	}

	public void setBegin(Point begin) {
		this.begin = begin;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}
}
