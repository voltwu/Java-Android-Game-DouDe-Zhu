package com.slb.poker.utils;

import com.slb.poker.R;

public class ScoinNumUnitUtil {
	public static String getCoinsCountStr(int coins){
		return coins+"";
	}
	public static int getCoinsNumIdByNumber(char num){
		switch(num){
		case '0':
			return R.drawable.head_coin_number_0;
		case '1':
			return R.drawable.head_coin_number_1;
		case '2':
			return R.drawable.head_coin_number_2;
		case '3':
			return R.drawable.head_coin_number_3;
		case '4':
			return R.drawable.head_coin_number_4;
		case '5':
			return R.drawable.head_coin_number_5;
		case '6':
			return R.drawable.head_coin_number_6;
		case '7':
			return R.drawable.head_coin_number_7;
		case '8':
			return R.drawable.head_coin_number_8;
		case '9':
			return R.drawable.head_coin_number_9;
		}
		return -1;
	}
}
