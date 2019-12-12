package com.ai.poker;

public enum P {
	P3("3",3),P4("4",4),P5("5",5),P6("6",6),
	P7("7",7),P8("8",8),P9("9",9),P10("10",10),
	PJ("J",11),PQ("Q",12),PK("K",13),PA("A",14),
	P2("2",15),PXW("XW",16),PDW("DW",17);
	
	private String desc = "";
	private int order = 0;
	
	private P(String desc,int order){
		this.desc = desc;
		this.order = order;
	}
	
	public String getDesc(){
		return desc;
	}
	
	public int getOrder(){
		return order;
	}
	
	public P getNextP(){
		if(order == P.P3.getOrder())
			return P.P4;
		else if(order == P.P4.getOrder())
			return P.P5;
		else if(order == P.P5.getOrder())
			return P.P6;
		else if(order == P.P6.getOrder())
			return P.P7;
		else if(order == P.P7.getOrder())
			return P.P8;
		else if(order == P.P8.getOrder())
			return P.P9;
		else if(order == P.P9.getOrder())
			return P.P10;
		else if(order == P.P10.getOrder())
			return P.PJ;
		else if(order == P.PJ.getOrder())
			return P.PQ;
		else if(order == P.PQ.getOrder())
			return P.PK;
		else if(order == P.PK.getOrder())
			return P.PA;
		else if(order == P.PA.getOrder())
			return P.P2;
		else if(order == P.P2.getOrder())
			return P.PXW;
		else if(order == P.PXW.getOrder())
			return P.PDW;
		else
			return null;
	}
}
