package com.ai.poker;

public enum PokerGroupTypeEnum {
	ROCKET("���",1),BOMB("ը��",2),DAN_PAI("����",3),
	DUI_PAI("����",4),SAN_ZHANG_PAI("������",5),
	SAN_DAI_YI_DAI_DAN("����һ������",6),
	SAN_DAI_YI_DAI_DUI("����һ������",7),
	DAN_SHUAN("��˳",8),
	SHUANG_SHUN("˫˳",9),
	SAN_SHUN("��˳",10),
	//�ɻ����������� �����ߵ��Ʊ����ǵ��� 33344468
	FEI_JI_DAI_CHI_BANG_DAI_DAN("�ɻ�����������",11),
	//�ɻ����������� �����ߵ��Ʊ����Ƕ��� 3334446688
	FEI_JI_DAI_CHI_BANG_DAI_DUI("�ɻ�����������",12),
	//�Ĵ��� ֻ�ܴ�һ������ 333355
	SHI_DAI_ER("�Ĵ���",13);
	
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
