package com.slb.poker.utils;

import java.lang.reflect.Field;

import com.ai.poker.P;
import com.ai.poker.Poker;
import com.ai.poker.PokerTypeEnum;
import com.slb.poker.R;

public class PokerUtil {
	public static int getImageDrawableIdFrom(Poker poker){
		String str = "poker_";
		if(poker.getPokerTypeEnum().equals(PokerTypeEnum.Clue))
			str+="clue_";
		else if(poker.getPokerTypeEnum().equals(PokerTypeEnum.Diamond))
			str+="diamond_";
		else if(poker.getPokerTypeEnum().equals(PokerTypeEnum.Heart))
			str+="heart_";
		else if(poker.getPokerTypeEnum().equals(PokerTypeEnum.Spade))
			str+="spade_";
		
		if(poker.getP().equals(P.P2))
			str+="2";
		else if(poker.getP().equals(P.P3))
			str+="3";
		else if(poker.getP().equals(P.P4))
			str+="4";
		else if(poker.getP().equals(P.P5))
			str+="5";
		else if(poker.getP().equals(P.P6))
			str+="6";
		else if(poker.getP().equals(P.P7))
			str+="7";
		else if(poker.getP().equals(P.P8))
			str+="8";
		else if(poker.getP().equals(P.P9))
			str+="9";
		else if(poker.getP().equals(P.P10))
			str+="10";
		else if(poker.getP().equals(P.PA))
			str+="a";
		else if(poker.getP().equals(P.PJ))
			str+="j";
		else if(poker.getP().equals(P.PQ))
			str+="q";
		else if(poker.getP().equals(P.PK))
			str+="k";
		else if(poker.getP().equals(P.PXW))
			str+="xw";
		else if(poker.getP().equals(P.PDW))
			str+="dw";
		Class<?> clazz = R.drawable.class;
		int res = 0;
		try {
			Field f = clazz.getField(str);
			res = f.getInt(null);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return res;
	}
}
