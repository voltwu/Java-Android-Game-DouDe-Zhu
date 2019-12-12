package com.ai.poker;

public class PokerGroupEntry  {
	private PokerGroupTypeEnum pokerGroupTypeEnum = null;
	private PokerSegGroup pokerSegGoup= null;
	public PokerGroupEntry(PokerGroupTypeEnum pokerGroupTypeEnum,
			PokerSegGroup pokerSegGoups){
		this.pokerGroupTypeEnum = pokerGroupTypeEnum;
		this.pokerSegGoup = pokerSegGoups;
	}
	public PokerGroupTypeEnum getPokerGroupTypeEnum(){
		return pokerGroupTypeEnum;
	}
	public PokerSegGroup getPokerSegGroup(){
		return pokerSegGoup;
	}
	public int getPokerGroupEntryOrder(){
		return pokerGroupTypeEnum.getOrder();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((pokerGroupTypeEnum == null) ? 0 : pokerGroupTypeEnum
						.hashCode());
		result = prime * result
				+ ((pokerSegGoup == null) ? 0 : pokerSegGoup.hashCode());
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
		PokerGroupEntry other = (PokerGroupEntry) obj;
		if (pokerGroupTypeEnum != other.pokerGroupTypeEnum)
			return false;
		if (pokerSegGoup == null) {
			if (other.pokerSegGoup != null)
				return false;
		} else if (!pokerSegGoup.equals(other.pokerSegGoup))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PokerGroupEntry [pokerGroupTypeEnum=" + pokerGroupTypeEnum
				+ ", pokerSegs=" + pokerSegGoup + "]";
	}
}
