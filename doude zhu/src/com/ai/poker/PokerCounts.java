package com.ai.poker;

public class PokerCounts {
	private int previous_counts = 0;
	private int mine_counts = 0;
	private int next_counts = 0;
	/**
	 * 
	 * @param pc ǰһ�����Ƶ�����
	 * @param mc �Լ��Ƶ�����
	 * @param nc ��һ���˵��Ƶ�����
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
