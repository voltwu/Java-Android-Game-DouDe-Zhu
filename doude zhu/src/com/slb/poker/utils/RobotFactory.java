package com.slb.poker.utils;

import com.slb.poker.model.POSITION;
import com.slb.poker.model.user.Gamer;

public class RobotFactory {
	private static RobotFactory robot = new RobotFactory();
	private RobotFactory(){}
	public static RobotFactory instance(){
		return robot;
	}
	public Gamer build(POSITION pos){
		Gamer gamer = new Gamer(pos);
		buildCoins(gamer);
		buildName(gamer);
		buildHeadImg(gamer);
		buildGender(gamer);
		buildCoinViewId(gamer);
		return gamer;
	}
	public void buildCoinViewId(Gamer gamer){
		gamer.setCoinViewId(Util.getUniqueViewId());
	}
	public void buildGender(Gamer gamer){
		gamer.setGender(Util.getRandom(0, 2)+1);
	}
	public void buildHeadImg(Gamer gamer){
		gamer.setHead_img(Property.HEADIMGS_DRAWABLEID[Util.getRandom(0, Property.HEADIMGS_DRAWABLEID.length)]);
	}
	public void buildName(Gamer gamer){
		gamer.setName(Property.NAMES[Util.getRandom(0, Property.NAMES.length)]);
	}
	public void buildCoins(Gamer gamer){
		gamer.setCoins(Util.getRandom(3000, 8000));
	}
}
