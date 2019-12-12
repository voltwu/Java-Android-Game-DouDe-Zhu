package com.ai.poker;

public enum PokerGroupTypeEnum {
	ROCKET("火箭",1),BOMB("炸弹",2),DAN_PAI("单牌",3),
	DUI_PAI("对牌",4),SAN_ZHANG_PAI("三张牌",5),
	SAN_DAI_YI_DAI_DAN("三带一带单牌",6),
	SAN_DAI_YI_DAI_DUI("三带一带对牌",7),
	DAN_SHUAN("单顺",8),
	SHUANG_SHUN("双顺",9),
	SAN_SHUN("三顺",10),
	//飞机带翅膀带单牌 被带走的牌必须是单牌 33344468
	FEI_JI_DAI_CHI_BANG_DAI_DAN("飞机带翅膀带单牌",11),
	//飞机带翅膀带对牌 被带走的牌必须是对牌 3334446688
	FEI_JI_DAI_CHI_BANG_DAI_DUI("飞机带翅膀带对牌",12),
	//四带二 只能带一个对子 333355
	SHI_DAI_ER("四带二",13);
	
	private String name = "";
	private int order = 0;
	private PokerGroupTypeEnum(String name,int order){
		this.name = name;
		this.order = order;
	}
	public String getName(){
		return name;
	}
	public int getOrder(){
		return order;
	}
	public static PokerGroupTypeEnum getPokerGroupTypeNum(int order){
		PokerGroupTypeEnum pgte = null;
		switch(order){
		case 1:
			pgte = ROCKET;
			break;
		case 2:
			pgte = BOMB;
			break;
		case 3:
			pgte = DAN_PAI;
			break;
		case 4:
			pgte = DUI_PAI;
			break;
		case 5:
			pgte = SAN_ZHANG_PAI;
			break;
		case 6:
			pgte = SAN_DAI_YI_DAI_DAN;
			break;
		case 7:
			pgte = SAN_DAI_YI_DAI_DUI;
			break;
		case 8:
			pgte = DAN_SHUAN;
			break;
		case 9:
			pgte = SHUANG_SHUN;
			break;
		case 10:
			pgte = SAN_SHUN;
			break;
		case 11:
			pgte = FEI_JI_DAI_CHI_BANG_DAI_DAN;
			break;
		case 12:
			pgte = FEI_JI_DAI_CHI_BANG_DAI_DUI;
			break;
		default:
			pgte = SHI_DAI_ER;
			break;
		}
		return pgte;
	}
}
