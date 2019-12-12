package com.ai.poker;

public class Poker {
	private PokerTypeEnum pokerType;
	private P p;
	
	public Poker(P p,PokerTypeEnum pokerTypeEnum){
		this.p = p;
		this.pokerType = pokerTypeEnum;
	}
	
	public PokerTypeEnum getPokerTypeEnum(){
		return pokerType;
	}
	
	public P getP(){
		return p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		result = prime * result
				+ ((pokerType == null) ? 0 : pokerType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Poker other = (Poker) obj;
		if (p != other.p)
			return false;
		if (pokerType != other.pokerType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Poker [pokerType=" + pokerType.getName() + ", p=" + p.getDesc() + "]";
	}
	
	public Poker clone(){
		return new Poker(p,pokerType);
	}
}
