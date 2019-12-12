package com.slb.poker.model.user;

public class User {
	protected String name;
	protected int coins;
	protected int head_img;
	public User(){}
	public User(User user){
		this.name = user.name;
		this.coins = user.coins;
		this.head_img = user.head_img;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	public int getHead_img() {
		return head_img;
	}
	public void setHead_img(int head_img) {
		this.head_img = head_img;
	}
	public void addCoins(int coins){
		this.coins+=coins;
	}
}
