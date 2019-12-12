package com.slb.poker.utils;

import com.slb.poker.R;

public class CardsNumUnitUtil {
	public static String getPokersCountStr(int pokersCount){
		if(pokersCount<10){
			return "0"+pokersCount;
		}
		return pokersCount+"";
	}
	public static int getCardsNumIdByNumber(char num){
		switch(num){
		case '0':
			return R.drawable.playscene_2_cardnum0;
		case '1':
			return R.drawable.playscene_2_cardnum1;
		case '2':
			return R.drawable.playscene_2_cardnum2;
		case '3':
			return R.drawable.playscene_2_cardnum3;
		case '4':
			return R.drawable.playscene_2_cardnum4;
		case '5':
			return R.drawable.playscene_2_cardnum5;
		case '6':
			return R.drawable.playscene_2_cardnum6;
		case '7':
			return R.drawable.playscene_2_cardnum7;
		case '8':
			return R.drawable.playscene_2_cardnum8;
		case '9':
			return R.drawable.playscene_2_cardnum9;
		}
		return -1;
	}
}
