package com.ai.poker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Common {
	public static Object copy(Object o) throws IOException, ClassNotFoundException{
		ByteArrayOutputStream oaos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(oaos);
		oos.writeObject(o);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(oaos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}
	//Poker has not been serialized,so it can't invoke copy(Object) method
	public static PokerSeg copyPokers(PokerSeg pokers){
		PokerSeg poker = new PokerSeg();
		for(Poker p : pokers){
			poker.add(new Poker(p.getP(),p.getPokerTypeEnum()));
		}
		return poker;
	}
	public static PokerSegGroup copyPokerSegGroup(PokerSegGroup pokerSegGroup){
		PokerSegGroup psgp = new PokerSegGroup();
		for(PokerSeg psg : pokerSegGroup){
			PokerSeg tpsg = new PokerSeg();
			for(Poker pr : psg){
				tpsg.add(new Poker(pr.getP(),pr.getPokerTypeEnum()));
			}
			psgp.add(tpsg);
		}
		return psgp;
	}
	/**
	 * compare two pokers
	 * @param pokera
	 * @param pokerb
	 * @param typea
	 * @param typeb
	 * @return the value of two pokerSeg (pokera - pokerb),value bigger,distance bigger.
	 */
	public static int compare(PokerSeg pokera,PokerSeg pokerb,PokerGroupTypeEnum typea,PokerGroupTypeEnum typeb){
		int distance = PokerDistance.NONE;
		int rocketDistance = PokerDistance.ROCKET;
		int bombDistance = PokerDistance.BOMB;
		int ShunLengthDistance = PokerDistance.SHUNLENGTH;
		//3 dan pai
		if(typeb == PokerGroupTypeEnum.DAN_PAI){
			if(typea == PokerGroupTypeEnum.DAN_PAI){
				distance = pokera.get(0).getP().getOrder() - pokerb.get(0).getP().getOrder();
			}
			if(typea == PokerGroupTypeEnum.ROCKET){
				distance = rocketDistance;
			}
			if(typea == PokerGroupTypeEnum.BOMB){
				distance = bombDistance;
			}
		//4 dui pai
		}else if(typeb == PokerGroupTypeEnum.DUI_PAI){
			if(typea == PokerGroupTypeEnum.DUI_PAI){
				distance = pokera.get(0).getP().getOrder() - pokerb.get(0).getP().getOrder();
			}
			if(typea == PokerGroupTypeEnum.ROCKET){
				distance = rocketDistance;
			}
			if(typea == PokerGroupTypeEnum.BOMB){
				distance = bombDistance;
			}
		}
		//5 san zhang pai
		else if(typeb == PokerGroupTypeEnum.SAN_ZHANG_PAI){
			Poker pa = Common.getMostsIn(pokera);
			Poker pb = Common.getMostsIn(pokerb);
			if(typea == PokerGroupTypeEnum.SAN_ZHANG_PAI)
				distance = pa.getP().getOrder() - pb.getP().getOrder();
			if(typea == PokerGroupTypeEnum.ROCKET)
				distance = rocketDistance;
			if(typea == PokerGroupTypeEnum.BOMB)
				distance = bombDistance;
		}
		//6 san dai yi
		else if(typeb == PokerGroupTypeEnum.SAN_DAI_YI_DAI_DAN){
			Poker pa = Common.getMostsIn(pokera);
			Poker pb = Common.getMostsIn(pokerb);			
			if(typea == PokerGroupTypeEnum.SAN_DAI_YI_DAI_DAN)
				distance = pa.getP().getOrder() - pb.getP().getOrder();
			if(typea == PokerGroupTypeEnum.ROCKET)
				distance = rocketDistance;
			if(typea == PokerGroupTypeEnum.BOMB)
				distance = bombDistance;
		}
		else if(typeb == PokerGroupTypeEnum.SAN_DAI_YI_DAI_DUI){
			Poker pa = Common.getMostsIn(pokera);
			Poker pb = Common.getMostsIn(pokerb);			
			if(typea == PokerGroupTypeEnum.SAN_DAI_YI_DAI_DUI)
				distance = pa.getP().getOrder() - pb.getP().getOrder();
			if(typea == PokerGroupTypeEnum.ROCKET)
				distance = rocketDistance;
			if(typea == PokerGroupTypeEnum.BOMB)
				distance = bombDistance;
		}
		//8 dan shun
		else if(typeb == PokerGroupTypeEnum.DAN_SHUAN){
			if(typea == PokerGroupTypeEnum.DAN_SHUAN){
				sort(pokera);
				sort(pokerb);				
				if(pokera.size() == pokerb.size()){
					distance = pokera.get(0).getP().getOrder() - pokerb.get(0).getP().getOrder();
				}else{
//					int pa_length = pokera.size();
//					int pb_length = pokerb.size();
//					if(pa_length > pb_length){
//						distance = pokera.get(0).getP().getOrder() - pokerb.get(0).getP().getOrder();
//					}else{
						distance = ShunLengthDistance * (-1);
//					}
				}
			}
			if(typea == PokerGroupTypeEnum.ROCKET)
				distance = rocketDistance;
			if(typea == PokerGroupTypeEnum.BOMB)
				distance = bombDistance;
		}
		//8 shuang shuan
		else if(typeb == PokerGroupTypeEnum.SHUANG_SHUN){
			if(typea == PokerGroupTypeEnum.SHUANG_SHUN){
				sort(pokera);
				sort(pokerb);				
				if(pokera.size() == pokerb.size()){
					distance = pokera.get(0).getP().getOrder() - pokerb.get(0).getP().getOrder();
				}else{
//					int pa_length = pokera.size();
//					int pb_length = pokerb.size();
//					if(pa_length > pb_length){
//						distance = pokera.get(0).getP().getOrder() - pokerb.get(0).getP().getOrder();
//					}else{
						distance = ShunLengthDistance * (-1);
//					}
				}
			}
			if(typea == PokerGroupTypeEnum.ROCKET)
				distance = rocketDistance;
			if(typea == PokerGroupTypeEnum.BOMB)
				distance = bombDistance;			
		}
		//9 san shuan
		else if(typeb == PokerGroupTypeEnum.SAN_SHUN){
			if( typea == PokerGroupTypeEnum.SAN_SHUN){
				sort(pokera);
				sort(pokerb);
				if(pokera.size() == pokerb.size()){
					distance = pokera.get(0).getP().getOrder() - pokerb.get(0).getP().getOrder();
				}else{
//					int pa_length = pokera.size();
//					int pb_length = pokerb.size();
//					if(pa_length > pb_length){
//						distance = pokera.get(0).getP().getOrder() - pokerb.get(0).getP().getOrder();
//					}else{
						distance = ShunLengthDistance * (-1);
//					}
				}
			}
			if(typea == PokerGroupTypeEnum.ROCKET)
				distance = rocketDistance;
			if(typea == PokerGroupTypeEnum.BOMB)
				distance = bombDistance;
		}
		//10 FEI JI DAI CHI BANG DAI DAN
		else if(typeb == PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DAN){
			if( typea == PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DAN || 
					typea == PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DUI){
				PokerSeg tempa = copyPokers(pokera);
				PokerSeg tempb = copyPokers(pokerb);
				removeSingleIn(tempa);
				removeSingleIn(tempb);
				sort(tempa);
				sort(tempb);
				if(pokera.size() == pokerb.size()){
					return tempa.get(0).getP().getOrder() - tempb.get(0).getP().getOrder();
//				}if(pokera.size() > pokerb.size()){
//					return tempa.get(0).getP().getOrder() - tempb.get(0).getP().getOrder();
				}else{
					return ShunLengthDistance * (-1);
				}
			}
			if( typea == PokerGroupTypeEnum.ROCKET)
				distance = rocketDistance;
			if( typea == PokerGroupTypeEnum.BOMB)
				distance = bombDistance;
		}
		//11 FEI JI DAI CHI BANG DAI DUI
		else if(typeb == PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DUI){
			if( typea == PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DUI){
				PokerSeg tempa = copyPokers(pokera);
				PokerSeg tempb = copyPokers(pokerb);
				removeSingleIn(tempa);
				removeSingleIn(tempb);
				sort(tempa);
				sort(tempb);
				if(pokera.size() == pokerb.size()){
					return tempa.get(0).getP().getOrder() - tempb.get(0).getP().getOrder();
//				}if(pokera.size() > pokerb.size()){
//					return tempa.get(0).getP().getOrder() - tempb.get(0).getP().getOrder();
				}else{
					return ShunLengthDistance * (-1);
				}
			}
			if( typea == PokerGroupTypeEnum.ROCKET)
				distance = rocketDistance;
			if( typea == PokerGroupTypeEnum.BOMB)
				distance = bombDistance;
		}
		//12 SHI_DAI_ER
		else if(typeb == PokerGroupTypeEnum.SHI_DAI_ER){
			if( typea == PokerGroupTypeEnum.SHI_DAI_ER){
				PokerSeg tempa = copyPokers(pokera);
				PokerSeg tempb = copyPokers(pokerb);
				removeDoubleIn(tempa);
				removeDoubleIn(tempb);
				sort(tempa);
				sort(tempb);
				return tempa.get(0).getP().getOrder() - tempb.get(0).getP().getOrder();
			}
			if( typea == PokerGroupTypeEnum.ROCKET)
				distance = rocketDistance;
			if( typea == PokerGroupTypeEnum.BOMB)
				distance = bombDistance;
		}
		//2 bomb
		else if(typeb == PokerGroupTypeEnum.BOMB){
			if(typea == PokerGroupTypeEnum.BOMB)
				distance = pokera.get(0).getP().getOrder() - pokerb.get(0).getP().getOrder();
			if(typea == PokerGroupTypeEnum.ROCKET)
				distance = rocketDistance;
		}
		//1 rocket
		else if(typeb == PokerGroupTypeEnum.ROCKET){
			
		}
		return distance;
	}
	private static void removeDoubleIn(PokerSeg pokerSeg) {
		sort(pokerSeg);
		PokerSeg needRemove = new PokerSeg();
		for(int i = 0;i<pokerSeg.size(); i++){
			if( countIn(pokerSeg,pokerSeg.get(i).getP()) != 4){
				needRemove.add(pokerSeg.get(i));
			}
		}
		for(Poker poker : needRemove){
			pokerSeg.remove(poker);
		}
		
	}
	private static Poker getMostsIn(PokerSeg pokera) {
		int precount = 0;
		int i = 0;
		for(int index=0; index<pokera.size(); index++){
			int count = countIn(pokera, pokera.get(index).getP());
			if(count > precount){
				precount = count;
				i = index;
			}
		}
		return pokera.get(i);
	}
	public static boolean contains(PokerGroupTypeEnum[] arr,
			PokerGroupTypeEnum pokerGroupTypeEnum) {
		for(PokerGroupTypeEnum p : arr){
			if(p.equals(pokerGroupTypeEnum)){
				return true;
			}
		}
		return false;
	}
	public static int eqaulsCount(PokerSeg pokerSeg) {
		int count = 0;
		for(int index = 0; index<pokerSeg.size() ; index++){
			int thisCount = 1;
			for(int j = 0; j<pokerSeg.size(); j++){
				if(index != j){
					if(pokerSeg.get(j).getP().equals(pokerSeg.get(index).getP())){
						thisCount++;
					}
				}
			}
			if(thisCount > count)
				count = thisCount;
		}
		return count;
	}
	public static void sort(PokerSeg pokerSeg) {
		for(int i = 0; i<pokerSeg.size();i++){
			boolean noChanged = true;
			for(int j = 0; j<pokerSeg.size()-1-i; j++){
				if(pokerSeg.get(j).getP().getOrder() < pokerSeg.get(j+1).getP().getOrder()){
					pokerSeg.set(j, pokerSeg.set(j+1,pokerSeg.get(j)));
					noChanged = false;
				}
			}
			if(noChanged){
				break;
			}
		}
	}
	public static boolean isDanShun(PokerSeg pokerSeg) {
		if(pokerSeg.size() < 5){
			return false;
		}
		for(int i = 1; i<pokerSeg.size(); i++){
			if(pokerSeg.get(i).getP().getOrder()-pokerSeg.get(i-1).getP().getOrder() != -1){
				return false;
			}
		}
		return true;
	}
	public static boolean isShuangShun(PokerSeg pokerSeg) {
		if(pokerSeg.size()%2 != 0){
			return false;
		}
		for(int i = 1; i<pokerSeg.size(); i+=2){
			if(!pokerSeg.get(i).getP().equals(pokerSeg.get(i-1).getP())){
				return false;
			}
		}
		for(int index=2; index<pokerSeg.size(); index+=2){
			if(pokerSeg.get(index).getP().getOrder() - pokerSeg.get(index-2).getP().getOrder() != -1){
				return false;
			}
		}
		return true;
	}
	public static boolean isSanShun(PokerSeg pokerSeg) {
		for(int i = 2; i<pokerSeg.size(); i+=3){
			if( (!pokerSeg.get(i).getP().equals( pokerSeg.get(i-1).getP())) ||
				(!pokerSeg.get(i).getP().equals( pokerSeg.get(i-2).getP()))){
				return false;
			}
		}
		for(int i = 3; i<pokerSeg.size(); i+=3){
			if( pokerSeg.get(i).getP().getOrder() - pokerSeg.get(i-3).getP().getOrder() != -1){
				return false;
			}
		}
		return true;
	}
	public static boolean isFeiJiDaiChiBangDaiDui(PokerSeg pokerSeg) {
		PokerSeg removedPokerSeg = getFeiJiDaiChiBangRemovedPokers(pokerSeg);
		if(removedPokerSeg == null){//is not a legal fei ji dai chi bang
			return false;
		}
		//is dai dui
		sort(removedPokerSeg);
		for(int index=0; index<removedPokerSeg.size(); index+=2){
			if(removedPokerSeg.get(index).getP().getOrder() != 
					removedPokerSeg.get(index+1).getP().getOrder()){
				return false;
			}
		}
		
		return true;
	}
	public static boolean isFeiJiDaiChiBangDaiDan(PokerSeg pokerSeg) {
		PokerSeg removedPokerSeg = getFeiJiDaiChiBangRemovedPokers(pokerSeg);
		if(removedPokerSeg == null){
			return false;
		}
		//is dai dui
		sort(removedPokerSeg);
		for(int index=0; index<removedPokerSeg.size(); index+=2){
			if(removedPokerSeg.get(index).getP().getOrder() != 
					removedPokerSeg.get(index+1).getP().getOrder()){
				return true;
			}
		}
		return false;
	}
	public static PokerSeg getFeiJiDaiChiBangRemovedPokers(PokerSeg pokerSeg){
//		if(pokerSeg.size()%2 != 0){
//			return null;
//		}
		if(eqaulsCount(pokerSeg) != 3){
			return null;
		}
		PokerSeg copyPokerSeg = copyPokers(pokerSeg);
		PokerSeg removedPokerSeg = removeSingleIn(copyPokerSeg);
		if(isAllDuiZi(removedPokerSeg)){
			if(pokerSeg.size() != (copyPokerSeg.size()+((copyPokerSeg.size()/3)*2))){
				return null;
			}
		}else{
			if(pokerSeg.size() != (copyPokerSeg.size()+copyPokerSeg.size()/3)){
				return null;
			}
		}
		for(int index=3; index<copyPokerSeg.size(); index+=3){
			if(copyPokerSeg.get(index).getP().getOrder() - copyPokerSeg.get(index-3).getP().getOrder() != -1){
				return null;
			}
		}
		return removedPokerSeg;
	}
	public static PokerSeg removeSingleIn(PokerSeg pokerSeg){
		sort(pokerSeg);
		PokerSeg needRemove = new PokerSeg();
		for(int i = 0;i<pokerSeg.size(); i++){
			if( countIn(pokerSeg,pokerSeg.get(i).getP()) != 3){
				needRemove.add(pokerSeg.get(i));
			}
		}
		for(Poker poker : needRemove){
			pokerSeg.remove(poker);
		}
		return needRemove;
	}
	public static int countIn(PokerSeg pokerSeg,P p){
		int count = 0;
		for(Poker poker : pokerSeg){
			if(poker.getP().equals(p)){
				count++;
			}
		}
		return count;
	}
	public static boolean isShiDaiEr(PokerSeg pokerSeg) {
		if(eqaulsCount(pokerSeg)!=4){
			return false;
		}
		if( pokerSeg.contains(new Poker(P.PXW, PokerTypeEnum.None)) &&
			pokerSeg.contains(new Poker(P.PDW, PokerTypeEnum.None))){
			return false;
		}
		sort(pokerSeg);
		for(int index=0; index<pokerSeg.size(); index+=2){
			if(pokerSeg.get(index).getP().getOrder() != 
				pokerSeg.get(index+1).getP().getOrder())
				return false;
		}
		return true;
	}
	public static PokerGroup copyPokerGroup(PokerGroup allPokerGroup) {
		PokerGroup temp = new PokerGroup();
		for(PokerGroupEntry pokerGroupEntry : allPokerGroup){
			PokerGroupEntry t = new PokerGroupEntry(
					pokerGroupEntry.getPokerGroupTypeEnum(), 
					pokerGroupEntry.getPokerSegGroup().clone());
			temp.add(t);
		}
		return temp;
	}
	public static boolean isAllDuiZi(PokerSeg pokerSeg){
		if(pokerSeg.size()%2!=0)
			return false;
		sort(pokerSeg);
		for(int index=0; index<pokerSeg.size(); index=index+2)
			if(pokerSeg.get(index).getP().getOrder() != pokerSeg.get(index+1).getP().getOrder())
				return false;
		return true;
	}
	public static PokerSeg getUniquePokerNumber(PokerSeg pokerSeg){
		PokerSeg result = new PokerSeg();
		for(int index=0; index<pokerSeg.size(); index++){
			boolean containedBefore = false;
			for(int temp = 0; temp<result.size(); temp++){
				if(result.get(temp).getP().getOrder() == pokerSeg.get(index).getP().getOrder())
				{
					containedBefore = true;
					break;
				}
			}
			if(!containedBefore){
				result.add(pokerSeg.get(index));
			}
		}
		return result;
	}
}
