package com.slb.poker.model;

import android.graphics.Rect;

import com.slb.poker.listener.DragSetListener;

public class SettingItem {
	private String name = null;
	private float volumePercent = 0;
	private DragSetListener listener;
	private Rect coinArea = null;
	private int trackLen = 0;
	private int slidderId;
	private int leftHalCircleViewId;
	private int brickId;
	private int coinViewId;
	
	public int getCoinViewId() {
		return coinViewId;
	}

	public void setCoinViewId(int coinViewId) {
		this.coinViewId = coinViewId;
	}

	public SettingItem(String name,float volumePercent,DragSetListener listener){
		this.name = name;
		this.volumePercent = volumePercent;
		this.coinArea = new Rect();
		this.listener = listener;
	}

	public int getTrackLen(){
		return trackLen;
	}
	public void setTrackLen(int trackLen){
		this.trackLen = trackLen;
	}
	public int getGreenTrackLen(){
		return (int)(getVolumePercent()*getTrackLen());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getVolumePercent() {
		return volumePercent;
	}
	public void setVolumePercent(float volumePercent) {
		this.volumePercent = volumePercent;
	}
	public DragSetListener getListener() {
		return listener;
	}
	public void setListener(DragSetListener listener) {
		this.listener = listener;
	}
	public Rect getCoinArea() {
		return coinArea;
	}
	public void setCoinArea(Rect coinArea) {
		this.coinArea = coinArea;
	}
	public void setSlidderId(int SlidderId) {
		this.slidderId = SlidderId;
	}
	public int getSlidderId() {
		return slidderId;
	}
	public void setLeftHalCircleViewId(int leftHalCircleViewId) {
		this.leftHalCircleViewId = leftHalCircleViewId;
	}
	public int getLeftHalCircleViewId(){
		return leftHalCircleViewId;
	}

	public void setBrickId(int brickId) {
		this.brickId = brickId;
	}
	public int getBrickId(){
		return brickId;
	}
}
