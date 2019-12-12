package com.ai.poker;

public class PokerCounts {
	private int previous_counts = 0;
	private int mine_counts = 0;
	private int next_counts = 0;
	/**
	 * 
	 * @param pc 前一个人牌的数量
	 * @param mc 自己牌的数量
	 * @param nc 后一个人的牌的数量
	 */
	public PokerCounts(int pc,int mc,int nc){
		this.previous_counts = pc;
		this.mine_counts = mc;
		this.next_counts = nc;
	}
	/**
	 * number cards of previous
	 * @return
	 */
	public int getPrevious_counts(){
		return previous_counts;
	}
	/**
	 * number cards of mine
	 * @return
	 */
	public int getMine_counts(){
		return mine_counts;
	}
	/**
	 * number cards of next
	 * @return
	 */
	public int getNext_counts(){
		return next_counts;
	}
}
