package com.ai.poker;

public enum PokerTypeEnum {
	/**
	 * ����
	 */
	Diamond("����"),
	/**
	 * ÷��
	 */
	Clue("÷��"),
	/**
	 * ����
	 */
	Heart("����"),
	/**
	 * ����
	 */
	Spade("����"),
	/**
	 * �����ͣ�������С���ʹ���
	 */
	None("��");
	
	private String name = "";
	private PokerTypeEnum(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
