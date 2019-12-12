package com.ai.poker;

public enum PokerTypeEnum {
	/**
	 * 方块
	 */
	Diamond("方块"),
	/**
	 * 梅花
	 */
	Clue("梅花"),
	/**
	 * 红心
	 */
	Heart("红心"),
	/**
	 * 黑桃
	 */
	Spade("黑桃"),
	/**
	 * 无牌型，适用于小王和大王
	 */
	None("无");
	
	private String name = "";
	private PokerTypeEnum(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
