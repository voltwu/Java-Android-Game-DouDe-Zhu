package com.slb.poker.model;

public class InvocationOrder {
	private static InvocationOrder invocationOrder = new InvocationOrder();
	private POSITION[] orders = null;
	int index = 0;
	private InvocationOrder(){
	}
	public static InvocationOrder instance(){
		return invocationOrder;
	}
	public void clear(){
		this.orders = null;
	}
	public void init(POSITION dizhu){
		if(dizhu == POSITION.BOTTOM)
			orders = new POSITION[]{POSITION.BOTTOM,POSITION.RIGHTTOP,POSITION.LEFTTOP};
		else if(dizhu == POSITION.RIGHTTOP)
			orders = new POSITION[]{POSITION.RIGHTTOP,POSITION.LEFTTOP,POSITION.BOTTOM};
		else if(dizhu == POSITION.LEFTTOP)
			orders = new POSITION[]{POSITION.LEFTTOP,POSITION.BOTTOM,POSITION.RIGHTTOP};
		index = 0;
	}
	public POSITION current(){
		if(index>=orders.length)
			index = 0;
		return orders[index++];
	}
}
