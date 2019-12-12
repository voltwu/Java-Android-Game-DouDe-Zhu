package com.ai.poker;

public class PokerSegUnit extends PokerSeg {
	private PokerGroupTypeEnum pokerGroupTypeEnum = null;
	
	public PokerSegUnit(PokerGroupTypeEnum pokerGroupTypeEnum,
			PokerSeg pokerSeg){
		super(pokerSeg);
		this.pokerGroupTypeEnum = pokerGroupTypeEnum;
	}
	
	public PokerGroupTypeEnum getPokerGroupTypeEnum(){
		return pokerGroupTypeEnum;
	}
}
