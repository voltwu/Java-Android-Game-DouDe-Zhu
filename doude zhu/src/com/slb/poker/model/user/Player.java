package com.slb.poker.model.user;

public class Player extends User{
	private int gender;
	public Player(){}
	public Player(Player player){
		super(player);
		this.gender = player.getGender();
	}
	public void setGender(int gender){
		this.gender = gender;
	}
	public int getGender(){
		return this.gender;
	}
}
