package com.ai.poker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * analysis poker
 * @author Administrator
 */
public class AI {
	/**
	 * check if pokera is bigger than pokerb.
	 * if(pokera > pokerb) result >0
	 * 
	 * @param pokera List<Poker>
	 * @param pokerb List<Poker>
	 * @return big than 0, then pokera bigger than pokerb.
	 */
	public static int compare(List<Poker> pokera,List<Poker> pokerb){
		PokerSeg pokerSega = new PokerSeg(pokera);
		PokerSeg pokerSegb = new PokerSeg(pokerb);
		PokerGroupTypeEnum pokerTypea = AI.getPokerGroupTypeEnum(pokerSega);
		PokerGroupTypeEnum pokerTypeb = AI.getPokerGroupTypeEnum(pokerSegb);
		return Common.compare(pokerSega, pokerSegb, pokerTypea, pokerTypeb);
	}
	/**
	 * hint actively pokers for real player.
	 * 
	 * hint out the most right pokers that can send out
	 * 
	 * Have a same strategy with SendPokersActively
	 * @param candidatePokers candidate pokers that are waiting for processing.
	 * @param pcs
	 * @return List<Poker> data type, hinted pokers
	 */
	public static List<Poker> HintPokersActively(List<Poker> candidatePokers,PokerCounts pcs){
		return sendPokersActively(candidatePokers,DIZHU.NEXT,pcs);//assume the next player is DIZHU
	}
	/**
	 * hint negatively pokers for real player.
	 * 
	 * Through pokers that are sent out by the previous player.
	 * it's same strategy with sendPokersNegatively method
	 * @return pokers of hinted.
	 */
	public static List<Poker> HintPokersNegatively(List<Poker> owner,List<Poker> prePokers,DIZHU dz){
		return sendPokersNegatively(owner,
				prePokers,
				dz);
	}
	/**
	 * 加倍数,
	 * 0 represents no JiaBei, 3 is the highest level of JiaBei.
	 * @param pokers all pokers that players had
	 * @return The amounts of JiaBei.
	 * 			0, no JiaBei.
	 * 			1, 1 times
	 * 			2, 2 times
	 * 			3, 3 times
	 */
	public static int CanJiaBei(List<Poker> pokers){
		int jiabei = 0;
		PokerSeg pokerSeg = Common.copyPokers(new PokerSeg(pokers));
		PokerGroup pokerGroup = types(pokerSeg);
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.ROCKET)){
			jiabei = 3;
		}else if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.BOMB)){
			int bumbCount = pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.BOMB).getPokerSegGroup().size();
			if(bumbCount>2)
				bumbCount = 2;
			jiabei = bumbCount+1;//least is 2
		}else{
			int bigPokersAmounts = 0;
			for(int i = 0 ; i<pokerSeg.size(); i++){
				if(pokerSeg.get(i).getP().equals(P.P2)){
					bigPokersAmounts += 1;
				}else if(pokerSeg.get(i).getP().equals(P.PXW) || pokerSeg.get(i).getP().equals(P.PDW)){
					bigPokersAmounts++;
				}
			}
			if(bigPokersAmounts>=3)
				jiabei = 1;
		}
		return jiabei;
	}
	/**
	 * 能不能够抢地主
	 * @param pokers
	 * @return
	 */
	public static boolean CanOwnDIZHU(List<Poker> pokers){
		int count = 0;
		PokerSeg pokerSeg = Common.copyPokers(new PokerSeg(pokers));
		PokerGroup pokerGroup = types(pokerSeg);
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.ROCKET)){
			count += 3;
		}
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.BOMB)){
			count += pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.BOMB).getPokerSegGroup().size();
		}
		for(int i = 0 ; i<pokerSeg.size(); i++){
			if(pokerSeg.get(i).getP().equals(P.P2)){
				count += 1;
			}else if(pokerSeg.get(i).getP().equals(P.PXW) || pokerSeg.get(i).getP().equals(P.PDW)){
				count++;
			}
		}
		if(count >= 3){
			return true;
		}
		return false;
	}
	/**
	 * 被动出牌
	 * 
	 * <br/>
	 * if Mine is DIZHU :
	 * send poker only over it in the same level ,it permit cross step level.
	 * 
	 * <br/>
	 * if PRE is DIZHU
	 * send poker only over it in the same level,it don't have same level poker,it permit cross step level
	 * 
	 * <br/>
	 * if　Nex is DIZHU
	 * send the most big poker in the same level
	 * 
	 * @param owner all pokers that myself had.
	 * @param prePokers all pokers that previous player send out.
	 * @param dz who is DIZHU
	 * @return pokers that needs to send out.null if don't send cards.
	 */
	public static List<Poker> sendPokersNegatively(List<Poker> owner,
			List<Poker> prePokers,
			DIZHU dz){
		PokerSeg prePokerSeg = new PokerSeg(prePokers);
		//判断prePokers是什么牌型
		PokerGroupTypeEnum prePokerType = getPokerGroupTypeEnum(prePokerSeg);
		PokerGroup allPokerGroup = types(new PokerSeg(owner));
		PokerGroup optimizedAllPokerGroup = optimize(Common.copyPokerGroup(allPokerGroup));
		
		//要出的牌
		PokerSeg resPokerSeg = null;
		
		if(dz.equals(DIZHU.MINE)){//地主是我
			resPokerSeg = getPokerWhenDIZHUIsMine(allPokerGroup,optimizedAllPokerGroup,prePokerType,prePokerSeg);
		}else if(dz.equals(DIZHU.PREVIOUS)){//地主是上一家
			resPokerSeg = getPokerWenDIZHUISPrevious(allPokerGroup,optimizedAllPokerGroup,prePokerType,prePokerSeg);
		}else{//地主是下一家
			resPokerSeg = getPokerWenDIZHUISNext(allPokerGroup,optimizedAllPokerGroup,prePokerType,prePokerSeg);
		}
		if(resPokerSeg != null)
			for(Poker poker : resPokerSeg){
					owner.remove(poker);
			}
		return resPokerSeg;
	}
	private static PokerSeg getPokerWenDIZHUISNext(PokerGroup allPokerGroup,
			PokerGroup optimizedAllPokerGroup, PokerGroupTypeEnum prePokerType,
			PokerSeg prePokerSeg) {
		PokerSeg resPokerSeg = null;
		if(prePokerType.equals(PokerGroupTypeEnum.ROCKET))
			resPokerSeg = null;
		else{
			PokerGroupEntry allPokerGroupEntry = allPokerGroup.getPokerGroupEntry(prePokerType);
			PokerGroupEntry optimizedPokerGroupEntry =optimizedAllPokerGroup.getPokerGroupEntry(prePokerType);
			if(allPokerGroupEntry != null){
				for(int index=0; index<allPokerGroupEntry.getPokerSegGroup().size(); index++){
					PokerSeg pokerSeg = allPokerGroupEntry.getPokerSegGroup().get(index);
					int res = Common.compare(pokerSeg, prePokerSeg, 
							allPokerGroupEntry.getPokerGroupTypeEnum(), 
							prePokerType);
					if(res>0){//bigger
						resPokerSeg = pokerSeg;
						break;
					}else{}//equals or smaller
				}
				if(resPokerSeg != null && optimizedPokerGroupEntry != null){
					
					for(int index=0; index<optimizedPokerGroupEntry.getPokerSegGroup().size(); index++){
						PokerSeg pokerSeg = optimizedPokerGroupEntry.getPokerSegGroup().get(index);
						int res = Common.compare(pokerSeg, prePokerSeg, 
								optimizedPokerGroupEntry.getPokerGroupTypeEnum(), 
								prePokerType);
						if(res>0){//bigger
							resPokerSeg = pokerSeg;
							break;
						}else{}//equals or smaller
					}
				}
			}
			if(resPokerSeg == null){
				allPokerGroupEntry = allPokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.BOMB);
				optimizedPokerGroupEntry = optimizedAllPokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.BOMB);
				
				if(allPokerGroupEntry != null){
					for(int index=0; index<allPokerGroupEntry.getPokerSegGroup().size(); index++){
						PokerSeg pokerSeg = allPokerGroupEntry.getPokerSegGroup().get(index);
						int res = Common.compare(pokerSeg, prePokerSeg, 
								PokerGroupTypeEnum.BOMB, 
								prePokerType);
						if(res>0){//bigger
							resPokerSeg = pokerSeg;
							break;
						}else{}//equals or smaller
					}
					if(resPokerSeg != null && optimizedPokerGroupEntry != null){
						
						for(int index=0; index<optimizedPokerGroupEntry.getPokerSegGroup().size(); index++){
							PokerSeg pokerSeg = optimizedPokerGroupEntry.getPokerSegGroup().get(index);
							int res = Common.compare(pokerSeg, prePokerSeg, 
									PokerGroupTypeEnum.BOMB, 
									prePokerType);
							if(res>0){//bigger
								resPokerSeg = pokerSeg;
								break;
							}else{}//equals or smaller
						}
					}
				}
			}
			if(resPokerSeg == null){
				allPokerGroupEntry = allPokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.ROCKET);
				optimizedPokerGroupEntry = optimizedAllPokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.ROCKET);
				
				if(allPokerGroupEntry!=null){
					resPokerSeg = allPokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.ROCKET).getPokerSegGroup().getLast();
				}
			}
			
		}
		return resPokerSeg;
	}
	private static PokerSeg getPokerWenDIZHUISPrevious(PokerGroup allPokerGroup,
			PokerGroup optimizedAllPokerGroup, PokerGroupTypeEnum prePokerType,
			PokerSeg prePokerSeg) {
		return getPokerWhenDIZHUIsMine(allPokerGroup,optimizedAllPokerGroup,prePokerType,prePokerSeg);
	}
	private static PokerSeg getPokerWhenDIZHUIsMine(PokerGroup allPokerGroup,
			PokerGroup optimizedAllPokerGroup, PokerGroupTypeEnum prePokerType,
			PokerSeg prePokerSeg) {
		PokerSeg resPokerSeg = null;
		if(PokerGroupTypeEnum.ROCKET.equals(prePokerType))
			resPokerSeg = null;
		else{
			PokerGroupEntry allPokerGroupEntry = allPokerGroup.getPokerGroupEntry(prePokerType);
			PokerGroupEntry optimizedPokerGroupEntry =optimizedAllPokerGroup.getPokerGroupEntry(prePokerType);
			if(allPokerGroupEntry != null){
				for(int index=allPokerGroupEntry.getPokerSegGroup().size()-1; index>=0; index--){
					PokerSeg pokerSeg = allPokerGroupEntry.getPokerSegGroup().get(index);
					int res = Common.compare(pokerSeg, prePokerSeg, 
							allPokerGroupEntry.getPokerGroupTypeEnum(), 
							prePokerType);
					if(res>0){//bigger
						resPokerSeg = pokerSeg;
						break;
					}else{}//equals or smaller
				}
				if(resPokerSeg != null && optimizedPokerGroupEntry != null){
					
					for(int index=optimizedPokerGroupEntry.getPokerSegGroup().size()-1; index>=0; index--){
						PokerSeg pokerSeg = optimizedPokerGroupEntry.getPokerSegGroup().get(index);
						int res = Common.compare(pokerSeg, prePokerSeg, 
								optimizedPokerGroupEntry.getPokerGroupTypeEnum(), 
								prePokerType);
						if(res>0){//bigger
							resPokerSeg = pokerSeg;
							break;
						}else{}//equals or smaller
					}
				}
			}
			if(resPokerSeg == null){
				allPokerGroupEntry = allPokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.BOMB);
				optimizedPokerGroupEntry = optimizedAllPokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.BOMB);
				
				if(allPokerGroupEntry != null){
					for(int index=allPokerGroupEntry.getPokerSegGroup().size()-1; index>=0; index--){
						PokerSeg pokerSeg = allPokerGroupEntry.getPokerSegGroup().get(index);
						int res = Common.compare(pokerSeg, prePokerSeg, 
								PokerGroupTypeEnum.BOMB, 
								prePokerType);
						if(res>0){//bigger
							resPokerSeg = pokerSeg;
							break;
						}else{}//equals or smaller
					}
					if(resPokerSeg != null && optimizedPokerGroupEntry != null){
						
						for(int index=optimizedPokerGroupEntry.getPokerSegGroup().size()-1; index>=0; index--){
							PokerSeg pokerSeg = optimizedPokerGroupEntry.getPokerSegGroup().get(index);
							int res = Common.compare(pokerSeg, prePokerSeg, 
									PokerGroupTypeEnum.BOMB, 
									prePokerType);
							if(res>0){//bigger
								resPokerSeg = pokerSeg;
								break;
							}else{}//equals or smaller
						}
					}
				}
			}
			if(resPokerSeg == null){
				allPokerGroupEntry = allPokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.ROCKET);
				optimizedPokerGroupEntry = optimizedAllPokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.ROCKET);
				
				if(allPokerGroupEntry!=null){
					resPokerSeg = allPokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.ROCKET).getPokerSegGroup().getLast();
				}
			}
			
		}
		return resPokerSeg;
	}
	/**
	 * 主动出牌
	 * 
	 * @param owner all pokers that myself owned
	 * @param dz who is DIZHU
	 * @param pcs all poker's amounts of every player
	 * @return pokers that needs to be send out.
	 */
	public static List<Poker> sendPokersActively(List<Poker> owner,DIZHU dz,PokerCounts pcs){
		int[] orders = null;
		//strategy of send cards from small to big
		int[] smallToBig_danfirst = new int[]{3,4,5,6,7,8,9,10,11,12,2,1,13};//dan pai first
		//strategy of send cards from big to small
		int[] bigToSmall_duifirst = new int[]{12,11,10,9,8,7,6,5,4,2,1,3,13};//dui pai first
		int[] bigToSmall_danfirst = new int[]{12,11,10,9,8,7,6,5,3,2,1,4,13};//dan pai first
		
		boolean sendBigger = false;//如果下面一个地主,那么出牌总是从大的往小的出
		if(dz.equals(DIZHU.PREVIOUS)){//previous
			orders = smallToBig_danfirst;
		}else if(dz.equals(DIZHU.MINE)){//mine
			orders = smallToBig_danfirst;
		}else{//next
			orders = bigToSmall_duifirst;
			sendBigger = true;
		}
		PokerGroup pokerGroup = optimize(types(new PokerSeg(owner)));
		if(dz == DIZHU.PREVIOUS){// DIZHU is previous
			if(pcs.getNext_counts() == 1){
				PokerGroupEntry pge = pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DAN_PAI);
				if(pge!=null)
					return pge.getPokerSegGroup().getLast();
			}
			if(pcs.getNext_counts() == 2){
				PokerGroupEntry pge = pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI);
				if(pge!=null)
					return pge.getPokerSegGroup().getLast();
			}
		}else if(dz == DIZHU.NEXT){// DIZHU is NEXT
			if(pcs.getNext_counts() == 1){
				orders = bigToSmall_duifirst; //dui_pai first, dan_pai later
			}
			if(pcs.getNext_counts() == 2){
				orders = bigToSmall_danfirst;//dan_pai first,dui_pai later
			}
		}else{//DIZHU is mine
			if(pcs.getNext_counts() == 1){
				orders = bigToSmall_duifirst; //dui_pai first, dan_pai later
			}
			if(pcs.getNext_counts() == 2){
				orders = bigToSmall_danfirst;//dan_pai first,dui_pai later
			}
		}
		for(int i = 0; i<orders.length; i++){
			int orderIndex = orders[i];
			PokerGroupTypeEnum pokerGroupTypeEnum = PokerGroupTypeEnum.getPokerGroupTypeNum(orderIndex);
			PokerGroupEntry pokerGroupEntry = pokerGroup.getPokerGroupEntry(pokerGroupTypeEnum);
			if(pokerGroupEntry!=null){
				List<Poker> pokers = null;
				if(sendBigger){
					pokers = pokerGroupEntry.getPokerSegGroup().pop();
				}else{
					pokers = pokerGroupEntry.getPokerSegGroup().popLast();
				}
				pokerGroup.removeEmptyPokerGroupEntry();
				return pokers;
			}
		}
		return null;
	}
	/**
	 * 得到一组牌的最优组合
	 * @param pokerGroup
	 * @return
	 */
	public static PokerGroup optimize(PokerGroup pokerGroup){
		//优化火箭
		optimizeRocket(pokerGroup);
		//优化炸弹
		optimizeBomb(pokerGroup);
		//优化三张牌
		optimizeSanZhangPai(pokerGroup);
		//优化两张牌
		optimizeDuiPai(pokerGroup);
		//优化单牌
		optimizeDanPai(pokerGroup);
		//重新组合单顺
		optimizeDanShun(pokerGroup);
		//重新组合双顺
		optimizeShuangShun(pokerGroup);
		//重新组合三顺
		optimizeSanShun(pokerGroup);
		//重新组合三带一
		optimizeSanDaiYi(pokerGroup);	
		//优化飞机带翅膀
		optimizeFeijiDaiChibang(pokerGroup);
		//优化四带二
		optimizeShiDaiEr(pokerGroup);
		pokerGroup.removeEmptyPokerGroupEntry();
		return pokerGroup;
	}
	public static void optimizeFeijiDaiChibang(PokerGroup pokerGroup){
		pokerGroup.remove(PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DAN);
		if(pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_SHUN) != null){
			//计算需要多少张单牌
			int count = 0;
			PokerSegGroup sanshun = Common.copyPokerSegGroup(pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_SHUN).getPokerSegGroup());
			for(int index=0; index<sanshun.size(); index++){
				PokerSeg pokerSeg = sanshun.get(index);
				count+=pokerSeg.size()/3;
			}
			PokerSeg danpai_list = new PokerSeg();
			PokerGroupEntry danpaiGroupEntry = pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DAN_PAI);
			List<PokerSeg> danpaineedRemove = new ArrayList<PokerSeg>();			
			if(danpaiGroupEntry != null){
				outer:
				for(int i = danpaiGroupEntry.getPokerSegGroup().size()-1; i >= 0; i--){
					PokerSeg pokerSeg = danpaiGroupEntry.getPokerSegGroup().get(i);
					for(Poker poker : pokerSeg){
						if(count<=0){
							break outer;
						}
						danpaineedRemove.add(pokerSeg);
						danpai_list.add(poker);
						count--;
					}
				}
			}
			PokerGroupEntry duipaiGroupEntry = pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI);
			List<PokerSeg> duipaineedRemove = new ArrayList<PokerSeg>();
			Poker duo = null;
			if(duipaiGroupEntry != null){
				outer:
				for(int i = duipaiGroupEntry.getPokerSegGroup().size()-1; i >= 0; i--){
					PokerSeg pokerSeg = duipaiGroupEntry.getPokerSegGroup().get(i);
					for(int index = 0; index<pokerSeg.size(); index++){
						Poker poker = pokerSeg.get(index);
						if(count<=0){
							if(index != 0){
								duo = pokerSeg.get(1);
							}
							break outer;
						}
						duipaineedRemove.add(pokerSeg);
						danpai_list.add(poker);
						count--;
					}
				}
			}
			if(count>0){//如果附加牌不够，那么直接结束
				return ;
			}
			for(int i = 0 ; i< danpaineedRemove.size(); i++){
				danpaiGroupEntry.getPokerSegGroup().remove(danpaineedRemove.get(i));
			}
			for(int i = 0 ; i< duipaineedRemove.size(); i++){
				duipaiGroupEntry.getPokerSegGroup().remove(duipaineedRemove.get(i));
			}
			if(duo!=null){//将多余的那个牌，添加到单牌中
				danpaiGroupEntry.getPokerSegGroup().add( new PokerSeg().addp(duo) );
			}
			//开始遍历三顺
			PokerGroupEntry feiji = new PokerGroupEntry(PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DAN, new PokerSegGroup());
			for(int index=0; index<sanshun.size(); index++){
				PokerSeg ss = Common.copyPokers(sanshun.get(index));
				int ecount = ss.size()/3;
				while(ecount>0){
					Poker poker = danpai_list.get(0);
					ss.add(poker);
					danpai_list.remove(0);
					ecount--;
				}
				feiji.getPokerSegGroup().add(ss);
			}
			pokerGroup.add(feiji);
			pokerGroup.remove(PokerGroupTypeEnum.SAN_SHUN);
		}
	}
	public static void optimizeShiDaiEr(PokerGroup pokerGroup){
		pokerGroup.remove(PokerGroupTypeEnum.SHI_DAI_ER);
	}
	public static void optimizeSanShun(PokerGroup pokerGroup){
		pokerGroup.remove(PokerGroupTypeEnum.SAN_SHUN);
		
		PokerGroupEntry sanzhangpai_pokerGroupEntry = pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_ZHANG_PAI);
		if(sanzhangpai_pokerGroupEntry!=null){
			PokerSegGroup sanzhangpai = Common.copyPokerSegGroup(sanzhangpai_pokerGroupEntry.getPokerSegGroup());
			List<PokerSeg> needRemove = new ArrayList<PokerSeg>();
			for(PokerSeg pokerSeg : sanzhangpai){
				if(pokerSeg.get(0).getP().equals(P.P2)){
					needRemove.add(pokerSeg);
				}
			}
			for(PokerSeg pokerSeg : needRemove){
				sanzhangpai.remove(pokerSeg);
			}
			for(int i = 0; i<sanzhangpai.size(); i++){
				boolean nochanged = true;
				for(int j=0; j<sanzhangpai.size()-1-i; j++){
					if(sanzhangpai.get(j).get(0).getP().getOrder() < sanzhangpai.get(j+1).get(0).getP().getOrder()){
						sanzhangpai.set(j, sanzhangpai.set(j+1, sanzhangpai.get(j)));
						nochanged = false;
					}
				}
				if(nochanged)
					break;
			}
			if(sanzhangpai.size() > 1){
				PokerGroupEntry pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.SAN_SHUN, new PokerSegGroup());
				PokerSeg ad = new PokerSeg();
				for(int index=1; index<sanzhangpai.size(); index++){
					PokerSeg now =  sanzhangpai.get(index);
					PokerSeg pre = sanzhangpai.get(index-1);
					if(now.get(0).getP().getOrder() - pre.get(0).getP().getOrder() == -1){
						ad.addAll(pre);
						if(index == sanzhangpai.size() - 1 ){
							ad.addAll(now);
						}
					}else{
						if(index >= 2){
							if(sanzhangpai.get(index-1).get(0).getP().getOrder() - sanzhangpai.get(index-2).get(0).getP().getOrder() == -1){
								ad.addAll(sanzhangpai.get(index-1));
							}
						}
						if(ad.size() > 5){
							pokerGroupEntry.getPokerSegGroup().add(ad);
						}
						ad = new PokerSeg();
					}
				}
				if(ad.size()>5){
					pokerGroupEntry.getPokerSegGroup().add(ad);
				}
				//在添加之前，从三张牌中移除
				PokerSegGroup needRemovePokerSeg = new PokerSegGroup();
				for(PokerSeg temp_sanzhangpai : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_ZHANG_PAI).getPokerSegGroup()){
					for(PokerSeg t_d : pokerGroupEntry.getPokerSegGroup()){
						if(t_d.containsAll(temp_sanzhangpai)){
							needRemovePokerSeg.add(temp_sanzhangpai);
						}
					}
				}
				for(PokerSeg p : needRemovePokerSeg){
					pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_ZHANG_PAI).getPokerSegGroup().remove(p);
				}
				
				if(pokerGroupEntry.getPokerSegGroup().size() != 0){
					pokerGroup.add(pokerGroupEntry);
				}
			}
		}
	}
	public static void optimizeShuangShun(PokerGroup pokerGroup){
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.DUI_PAI)){
			pokerGroup.remove(PokerGroupTypeEnum.SHUANG_SHUN);
			PokerSegGroup tempduipai = pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI).getPokerSegGroup();
			PokerSegGroup duipai = Common.copyPokerSegGroup(tempduipai);
			PokerSegGroup needRemove = new PokerSegGroup();
			for(PokerSeg p : duipai){
				if( p.get(0).getP() .equals(P.P2) ){
					needRemove.add(p);
				}
			}
			for(PokerSeg p : needRemove){
				duipai.remove(p);
			}
			for(int i=0; i<duipai.size(); i++){
				for(int j=0; j<duipai.size()-i-1; j++){
					if( duipai.get(j).get(0).getP().getOrder() < 
						duipai.get(j+1).get(0).getP().getOrder()){
						duipai.set(j, duipai.set(j+1, duipai.get(j)));
					}
				}
			}
			if(duipai.size()>1){
				PokerSegGroup temp = new PokerSegGroup();
				PokerSeg pokerSeg = new PokerSeg();
				for(int index=1; index<duipai.size(); index++){
					PokerSeg pokerSegNex = duipai.get(index);
					PokerSeg pokerSegPre = duipai.get(index-1);
					if(pokerSegNex.get(0).getP().getOrder() - pokerSegPre.get(0).getP().getOrder() == -1){
						pokerSeg.addAll(pokerSegPre);
						if(index == duipai.size() - 1){
							pokerSeg.addAll(pokerSegNex);
						}
					}else{
						if(index > 2 && index != duipai.size()){
							if(duipai.get(index-1).get(0).getP().getOrder() - duipai.get(index-2).get(0).getP().getOrder() == -1){
								pokerSeg.addAll(duipai.get(index-1));
							}
						}
						if(pokerSeg.size()>4){
							temp.add(pokerSeg);
						}
						pokerSeg = new PokerSeg();
					}
				}
				if(pokerSeg.size()>4){
					temp.add(pokerSeg);
				}
				PokerSegGroup needRemovePokerSeg = new PokerSegGroup();
				for(PokerSeg temp_duipai : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI).getPokerSegGroup()){
					for(PokerSeg t_d : temp){
						if(t_d.containsAll(temp_duipai)){
							needRemovePokerSeg.add(temp_duipai);
						}
					}
				}
				for(PokerSeg p : needRemovePokerSeg){
					pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI).getPokerSegGroup().remove(p);
				}
				PokerGroupEntry entry = new PokerGroupEntry(PokerGroupTypeEnum.SHUANG_SHUN, temp);
				pokerGroup.add(entry);
			}
		}
	}
	public static void optimizeDanShun(PokerGroup pokerGroup){
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.DAN_PAI)){
			pokerGroup.remove(PokerGroupTypeEnum.DAN_SHUAN);
			PokerSegGroup danpai = pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DAN_PAI).getPokerSegGroup();
			List<Poker> dlist = new ArrayList<Poker>();
			for(PokerSeg pokerSeg : danpai){
				dlist.add(pokerSeg.get(0));
			}
			dlist.remove(new Poker(P.PXW,PokerTypeEnum.None));
			dlist.remove(new Poker(P.PDW,PokerTypeEnum.None));
			dlist.remove(new Poker(P.P2,PokerTypeEnum.Clue));
			dlist.remove(new Poker(P.P2,PokerTypeEnum.Diamond));
			dlist.remove(new Poker(P.P2,PokerTypeEnum.Heart));
			dlist.remove(new Poker(P.P2,PokerTypeEnum.Spade));
			if(dlist.size() > 1){
				PokerSegGroup temp = new PokerSegGroup();
				PokerSeg tempPokerSeg = new PokerSeg();
				for(int i = 1 ; i < dlist.size(); i++){
					Poker nexPoker = dlist.get(i);
					Poker prePoker = dlist.get(i-1);
					if(nexPoker.getP().getOrder() - prePoker.getP().getOrder() == -1){
						tempPokerSeg.add(prePoker);
						if(i == dlist.size()-1){
							tempPokerSeg.add(nexPoker);
						}
					}else{
						if(i != dlist.size() && i>2){
							if(dlist.get(i-1).getP().getOrder() - dlist.get(i-2).getP().getOrder() == -1){
								tempPokerSeg.add(dlist.get(i-1));
							}
						}
						if(tempPokerSeg.size() > 4){
							temp.add(tempPokerSeg);
						}
						tempPokerSeg = new PokerSeg();
					}
				}
				if(tempPokerSeg.size() > 4){
					temp.add(tempPokerSeg);
				}
				PokerSegGroup pg = new PokerSegGroup();
				for(PokerSeg t : temp){
					for(PokerSeg tdp : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DAN_PAI).getPokerSegGroup()){
						if(t.containsAll(tdp)){
							pg.add(tdp);
						}
					}
				}
				for(PokerSeg p : pg){
					pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DAN_PAI).getPokerSegGroup().remove(p);
				}
				if(temp.size() > 0){
					PokerGroupEntry pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.DAN_SHUAN,temp);
					pokerGroup.add(pokerGroupEntry);
				}
			}
		}
	}
	public static void optimizeDanPai(PokerGroup pokerGroup){
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.DAN_PAI)){
			PokerSegGroup list = new PokerSegGroup();
			for(PokerSeg pokerSeg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DAN_PAI).getPokerSegGroup()){
				for(Poker poker : pokerSeg){
					if(pokerGroup.removePokerSegDanpaiIn(poker, new PokerGroupTypeEnum[]{
							PokerGroupTypeEnum.BOMB,
							PokerGroupTypeEnum.ROCKET,
							PokerGroupTypeEnum.SAN_ZHANG_PAI,
							PokerGroupTypeEnum.DUI_PAI
					})){
						list.add(pokerSeg);
					}
				}
			}
			for(PokerSeg pokerSeg : list){
				pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DAN_PAI).getPokerSegGroup().remove(pokerSeg);
			}
		}
	}
	public static void optimizeDuiPai(PokerGroup pokerGroup){
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.DUI_PAI)){
			Set<PokerSeg> sets = new HashSet<PokerSeg>();
			for(PokerSeg pokerSeg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI).getPokerSegGroup()){
				for(Poker poker : pokerSeg){
					if(pokerGroup.removePokerSegContainsExcepb(poker, new PokerGroupTypeEnum[]{
							PokerGroupTypeEnum.BOMB,
							PokerGroupTypeEnum.ROCKET,
							PokerGroupTypeEnum.SAN_ZHANG_PAI
					})){
						sets.add(pokerSeg);
					}
				}
			}
			for(PokerSeg p : sets){
				pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI).getPokerSegGroup().remove(p);
			}
		}
	}
	public static void optimizeSanZhangPai(PokerGroup pokerGroup){
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.SAN_ZHANG_PAI)){
			Set<PokerSeg> sets = new HashSet<PokerSeg>();
			for(PokerSeg pokerSeg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_ZHANG_PAI).getPokerSegGroup()){
				for(Poker poker : pokerSeg){
					if(pokerGroup.removePokerSegContainsExcepb(poker, new PokerGroupTypeEnum[]{
							PokerGroupTypeEnum.BOMB,
							PokerGroupTypeEnum.SAN_ZHANG_PAI
					})){
						sets.add(pokerSeg);
					};
				}
			}
			for(PokerSeg p : sets){
				pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI).getPokerSegGroup().remove(p);
			}
		}
	}
	public static void optimizeBomb(PokerGroup pokerGroup){
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.BOMB)){
			for(PokerSeg pokerSeg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.BOMB).getPokerSegGroup()){
				for(Poker poker : pokerSeg){
					pokerGroup.removePokerSegContainsExcep(poker, PokerGroupTypeEnum.BOMB);
				}
			}
		}
	}
	public static void optimizeRocket(PokerGroup pokerGroup){
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.ROCKET)){
			pokerGroup.removePokerSegContainsExcep(new Poker(P.PXW,PokerTypeEnum.None),PokerGroupTypeEnum.ROCKET);
			pokerGroup.removePokerSegContainsExcep(new Poker(P.PDW,PokerTypeEnum.None),PokerGroupTypeEnum.ROCKET);
		}
	}
	public static void optimizeSanDaiYi(PokerGroup pokerGroup){
		//remove it
		pokerGroup.remove(PokerGroupTypeEnum.SAN_DAI_YI_DAI_DAN);
		PokerSegGroup pokerSegGroup = pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DAN_PAI).getPokerSegGroup();
		if(pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_ZHANG_PAI)==null){
			return;
		}
		PokerSegGroup sanzhangpai = pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_ZHANG_PAI).getPokerSegGroup();
		PokerSegGroup needRemove = new PokerSegGroup();
		PokerGroupEntry sandaiyi = new PokerGroupEntry(PokerGroupTypeEnum.SAN_DAI_YI_DAI_DAN,new PokerSegGroup());
		for(PokerSeg szp : sanzhangpai){
			if(pokerSegGroup.size()<=0){
				break;
			}
			PokerSeg last = pokerSegGroup.getLast();
			PokerSeg psg = new PokerSeg();
			psg.addAll(szp);
			psg.addAll(last);
			sandaiyi.getPokerSegGroup().add(psg);
			needRemove.add(szp);
			pokerSegGroup.removeLast();
		}
		for(PokerSeg szp : needRemove){
			sanzhangpai.remove(szp);
		}
		pokerGroup.add(sandaiyi);
	}
	public static boolean isSanZhangPaiIn(PokerSeg psgs,PokerGroupEntry pokerGroupEntry){
		if(pokerGroupEntry.getPokerGroupTypeEnum() == PokerGroupTypeEnum.BOMB){
			for(PokerSeg psg : pokerGroupEntry.getPokerSegGroup()){
				for(Poker pr : psg){
					if(psgs.get(0).equals(pr)){
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * the sequence is "1 2 9 8 7 5 4 3 11 10 6"
	 * @param pokers 
	 * @return 
	 */
	public static PokerGroup types(PokerSeg pokers){
		pokers = Common.copyPokers(pokers);
		PokerGroup pokerGroup = new PokerGroup();
		
		//1 rocket
		if( pokers.contains(new Poker(P.PXW,PokerTypeEnum.None)) &&
			pokers.contains(new Poker(P.PDW,PokerTypeEnum.None))){
			
			pokerGroup.addp(new PokerGroupEntry(PokerGroupTypeEnum.ROCKET, new PokerSegGroup().
					addp(new PokerSeg().
							addp(new Poker(P.PXW,PokerTypeEnum.None)).
							addp(new Poker(P.PDW,PokerTypeEnum.None)))));
			
		};
		
		//2 bomb
		List<P> temp = new ArrayList<P>();
		for(Poker p : pokers){
			if( pokers.contains(new Poker(p.getP(),PokerTypeEnum.Diamond)) &&
				pokers.contains(new Poker(p.getP(),PokerTypeEnum.Clue)) &&
				pokers.contains(new Poker(p.getP(),PokerTypeEnum.Heart)) &&
				pokers.contains(new Poker(p.getP(),PokerTypeEnum.Spade))){
				if(!temp.contains(p.getP())){
					temp.add(p.getP());
				}
			}
			continue;
		}
		PokerGroupEntry pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.BOMB,new PokerSegGroup());
		for(P p : temp){
			pokerGroupEntry.getPokerSegGroup().addp(new PokerSeg().
					addp(new Poker(p, PokerTypeEnum.Diamond)).
					addp(new Poker(p, PokerTypeEnum.Clue)).
					addp(new Poker(p, PokerTypeEnum.Heart)).
					addp(new Poker(p, PokerTypeEnum.Spade)));
		}
		sortBumbDes(pokerGroupEntry.getPokerSegGroup());
		
		if( !pokerGroupEntry.getPokerSegGroup().isEmpty() )
			pokerGroup.add(pokerGroupEntry);
		
		//9 SAN_SHUN
		pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.SAN_SHUN,new PokerSegGroup());
		temp = new ArrayList<P>();
		for(Poker p : pokers){
			if(p.getP() != P.P2 && 
				p.getP()!=P.PXW && 
				p.getP() !=P.PDW &&
				hasPointInNumber(3, p.getP(), pokers)){
				if(!temp.contains(p.getP()))
					temp.add(p.getP());
			}
		}
		removeIsolate(temp);
		List<List<P>> res = sort_ALL_SUB_SHUN(temp,1);
		
		for(int i = res.size()-1; i>=0; i--){
			PokerSeg psg = new PokerSeg();
			for(int j = 0; j<res.get(i).size(); j++){
				int count = 0;
				P p = res.get(i).get(j);
				if(pokers.contains(new Poker(p,PokerTypeEnum.Clue))){
					psg.add(new Poker(p,PokerTypeEnum.Clue));
					count++;
				}
				if(pokers.contains(new Poker(p,PokerTypeEnum.Diamond))){
					psg.add(new Poker(p,PokerTypeEnum.Diamond));
					count++;
				}
				if(pokers.contains(new Poker(p,PokerTypeEnum.Heart))){
					psg.add(new Poker(p,PokerTypeEnum.Heart));
					count++;
				}
				if( count < 3 && 
					pokers.contains(new Poker(p,PokerTypeEnum.Spade))){
					psg.add(new Poker(p,PokerTypeEnum.Spade));
				}
			}
			pokerGroupEntry.getPokerSegGroup().add(psg);
		}
		if( !pokerGroupEntry.getPokerSegGroup().isEmpty() )
			pokerGroup.add(pokerGroupEntry);
		
		//8 SHUANG_SHUN
		pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.SHUANG_SHUN,new PokerSegGroup());
		temp = new ArrayList<P>();
		for(Poker p : pokers){
			if(p.getP() != P.P2 && 
				p.getP()!=P.PXW && 
				p.getP() !=P.PDW &&
				hasPointInNumber(2, p.getP(), pokers)){
				if(!temp.contains(p.getP()))
					temp.add(p.getP());
			}
		}
		removeLessDoubleConnected(temp);
		res = sort_ALL_SUB_SHUN(temp,2);
		for(int i = res.size()-1; i>=0; i--){
			PokerSeg psg = new PokerSeg();
			for(int j = 0; j<res.get(i).size(); j++){
				int count = 0;
				P p = res.get(i).get(j);
				if(pokers.contains(new Poker(p,PokerTypeEnum.Clue))){
					psg.add(new Poker(p,PokerTypeEnum.Clue));
					count++;
				}
				if(pokers.contains(new Poker(p,PokerTypeEnum.Diamond))){
					psg.add(new Poker(p,PokerTypeEnum.Diamond));
					count++;
				}
				if( count < 2 &&
					pokers.contains(new Poker(p,PokerTypeEnum.Heart))){
					psg.add(new Poker(p,PokerTypeEnum.Heart));
					count++;
				}
				if( count < 2 && 
					pokers.contains(new Poker(p,PokerTypeEnum.Spade))){
					psg.add(new Poker(p,PokerTypeEnum.Spade));
				}
			}
			pokerGroupEntry.getPokerSegGroup().add(psg);
		}
		if( !pokerGroupEntry.getPokerSegGroup().isEmpty() )
			pokerGroup.add(pokerGroupEntry);
		
		//7 DAN_SHUN
		pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.DAN_SHUAN,new PokerSegGroup());
		temp = new ArrayList<P>();
		for(Poker p : pokers){
			if(p.getP() != P.P2 && 
				p.getP()!=P.PXW && 
				p.getP() !=P.PDW &&
				hasPointInNumber(1, p.getP(), pokers)){
				if(!temp.contains(p.getP()))
					temp.add(p.getP());
			}
		}
		removeLessFourConnected(temp);
		res = sort_ALL_SUB_SHUN(temp,4);
		for(int i = res.size()-1; i>=0; i--){
			PokerSeg psg = new PokerSeg();
			for(int j = 0; j<res.get(i).size(); j++){
				int count = 0;
				P p = res.get(i).get(j);
				if(pokers.contains(new Poker(p,PokerTypeEnum.Clue))){
					psg.add(new Poker(p,PokerTypeEnum.Clue));
					count++;
				}
				if( count < 1 && 
					pokers.contains(new Poker(p,PokerTypeEnum.Diamond))){
					psg.add(new Poker(p,PokerTypeEnum.Diamond));
					count++;
				}
				if( count < 1 &&
					pokers.contains(new Poker(p,PokerTypeEnum.Heart))){
					psg.add(new Poker(p,PokerTypeEnum.Heart));
					count++;
				}
				if( count < 1 && 
					pokers.contains(new Poker(p,PokerTypeEnum.Spade))){
					psg.add(new Poker(p,PokerTypeEnum.Spade));
				}
			}
			pokerGroupEntry.getPokerSegGroup().add(psg);
		}
		if( !pokerGroupEntry.getPokerSegGroup().isEmpty() )
			pokerGroup.add(pokerGroupEntry);
		
		//5 SAN_ZHANG_PAI
		pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.SAN_ZHANG_PAI,new PokerSegGroup());
		temp = new ArrayList<P>();
		for(Poker p : pokers){
			if( p.getP() != P.PXW && 
				p.getP() != P.PDW &&
				hasPointInNumber(3, p.getP(), pokers)){
				if(!temp.contains(p.getP()))
					temp.add(p.getP());
			}
		}
		sortSanZhangPai(temp);
		for(int i = temp.size()-1; i>=0; i--){
			PokerSeg psg = new PokerSeg();
			int count = 0;
			P p =temp.get(i);
			if(pokers.contains(new Poker(p,PokerTypeEnum.Clue))){
				psg.add(new Poker(p,PokerTypeEnum.Clue));
				count++;
			}
			if( pokers.contains(new Poker(p,PokerTypeEnum.Diamond))){
				psg.add(new Poker(p,PokerTypeEnum.Diamond));
				count++;
			}
			if( pokers.contains(new Poker(p,PokerTypeEnum.Heart))){
				psg.add(new Poker(p,PokerTypeEnum.Heart));
				count++;
			}
			if( count < 3 && 
				pokers.contains(new Poker(p,PokerTypeEnum.Spade))){
				psg.add(new Poker(p,PokerTypeEnum.Spade));
			}
			pokerGroupEntry.getPokerSegGroup().add(psg);
		}
		if( !pokerGroupEntry.getPokerSegGroup().isEmpty() )
			pokerGroup.add(pokerGroupEntry);
		
		//4 DUI_PAI
		pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.DUI_PAI,new PokerSegGroup());
		temp = new ArrayList<P>();
		for(Poker p : pokers){
			if( p.getP() != P.PXW && 
				p.getP() != P.PDW &&
				hasPointInNumber(2, p.getP(), pokers)){
				if(!temp.contains(p.getP()))
					temp.add(p.getP());
			}
		}
		for(int i = temp.size()-1; i>=0; i--){
			PokerSeg psg = new PokerSeg();
			int count = 0;
			P p =temp.get(i);
			if(pokers.contains(new Poker(p,PokerTypeEnum.Clue))){
				psg.add(new Poker(p,PokerTypeEnum.Clue));
				count++;
			}
			if( count < 2 &&
				pokers.contains(new Poker(p,PokerTypeEnum.Diamond))){
				psg.add(new Poker(p,PokerTypeEnum.Diamond));
				count++;
			}
			if( count < 2 &&
				pokers.contains(new Poker(p,PokerTypeEnum.Heart))){
				psg.add(new Poker(p,PokerTypeEnum.Heart));
				count++;
			}
			if( count < 2 && 
				pokers.contains(new Poker(p,PokerTypeEnum.Spade))){
				psg.add(new Poker(p,PokerTypeEnum.Spade));
			}
			pokerGroupEntry.getPokerSegGroup().add(psg);
		}
		if( !pokerGroupEntry.getPokerSegGroup().isEmpty() )
			pokerGroup.add(pokerGroupEntry);
		
		//3 DAN PAI
		pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.DAN_PAI,new PokerSegGroup());
		temp = new ArrayList<P>();
		for(Poker p : pokers){
			if(hasPointInNumber(1, p.getP(), pokers)){
				if(!temp.contains(p.getP()))
					temp.add(p.getP());
			}
		}		
		for(int i = temp.size()-1; i>=0; i--){
			PokerSeg psg = new PokerSeg();
			int count = 0;
			P p =temp.get(i);
			if(pokers.contains(new Poker(p,PokerTypeEnum.Clue))){
				psg.add(new Poker(p,PokerTypeEnum.Clue));
				count++;
			}
			if( count < 1 &&
				pokers.contains(new Poker(p,PokerTypeEnum.Diamond))){
				psg.add(new Poker(p,PokerTypeEnum.Diamond));
				count++;
			}
			if( count < 1 &&
				pokers.contains(new Poker(p,PokerTypeEnum.Heart))){
				psg.add(new Poker(p,PokerTypeEnum.Heart));
				count++;
			}
			if( count < 1 && 
				pokers.contains(new Poker(p,PokerTypeEnum.Spade))){
				psg.add(new Poker(p,PokerTypeEnum.Spade));
			}
			if( (p == P.PXW || 
				p == P.PDW) &&
				pokers.contains(new Poker(p,PokerTypeEnum.None))){
				psg.add(new Poker(p,PokerTypeEnum.None));
			}
			pokerGroupEntry.getPokerSegGroup().add(psg);
		}
		for(int m = 0; m<pokerGroupEntry.getPokerSegGroup().size(); m++){
			for(int n = 0; n<pokerGroupEntry.getPokerSegGroup().size() - m -1; n++){
				if(pokerGroupEntry.getPokerSegGroup().get(n).get(0).getP().getOrder() 
				 < pokerGroupEntry.getPokerSegGroup().get(n+1).get(0).getP().getOrder()){
					pokerGroupEntry.getPokerSegGroup().set(n,
							pokerGroupEntry.getPokerSegGroup().set(n+1, pokerGroupEntry.getPokerSegGroup().get(n)));
				}
			}
		}
		if( !pokerGroupEntry.getPokerSegGroup().isEmpty() )
			pokerGroup.add(pokerGroupEntry);
		
		//12 SHI_DAI_ER
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.BOMB)){
			pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.SHI_DAI_ER,new PokerSegGroup());
			for(PokerSeg psg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.BOMB).getPokerSegGroup()){
				for(PokerSeg tpsg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI).getPokerSegGroup()){
					if(psg.get(0).getP().equals(tpsg.get(0).getP())){
						continue;
					}
					PokerSeg needAdd = new PokerSeg();
					for(Poker poker : psg){
						needAdd.add(poker);
					}
					for(Poker poker : tpsg){
						needAdd.add(poker);
					}
					pokerGroupEntry.getPokerSegGroup().add(needAdd);
				}
//				Calculate dan pai list
//				PokerSeg dan_pai_list = new PokerSeg();
//				for(PokerSeg danpaips : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DAN_PAI).getPokerSegGroup()){
//					for(Poker p : danpaips){
//						if(!p.getP().equals(psg.get(0).getP())){
//							dan_pai_list.add(p);
//						}
//					}
//				}
//				List<PokerSeg> lists = nArrangeSortPoker(dan_pai_list);
//				for(PokerSeg psglist : lists){
//					if( psglist.size() == 2 &&
//						((	!psglist.get(0).getP().equals(P.PXW) && 
//							!psglist.get(0).getP().equals(P.PDW)  )
//							||
//						(	!psglist.get(1).getP().equals(P.PXW) &&
//							!psglist.get(1).getP().equals(P.PDW)
//						))){
//						
//						PokerSeg needAdd = new PokerSeg();
//						for(Poker poker : psg){
//							needAdd.add(poker);
//						}
//						for(Poker poker : psglist){
//							needAdd.add(poker);
//						}
//						pokerGroupEntry.getPokerSegGroup().add(needAdd);
//					}
//				}
			}
			if( !pokerGroupEntry.getPokerSegGroup().isEmpty() )
				pokerGroup.add(pokerGroupEntry);
		}
		
		//10 FEI_JI_DAI_CHI_BANG_DAI_DAN
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.SAN_SHUN)){
			pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DAN,new PokerSegGroup());
			for(PokerSeg psg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_SHUN).getPokerSegGroup()){
				PokerSeg list = new PokerSeg();
				for(PokerSeg duipsg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI).getPokerSegGroup()){
					boolean isLeagel = true;
					for(Poker p : psg){
						if(p.getP().equals(duipsg.get(0).getP())){
							isLeagel = false;
						}
					}
					if(isLeagel){
						list.add(duipsg.get(0));
						list.add(duipsg.get(1));
					}
				}				
				//add dan pai
				outer:
				for(PokerSeg danpaipsg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DAN_PAI).getPokerSegGroup()){
					int i = 0;
					for(i = 0;i< list.size(); i++){
						if( list.get(i).getP().equals(danpaipsg.get(0).getP()) ){//when it has same point then it can't add again
							continue outer;
						}
					}
					for(i = 0;i < psg.size(); i++){
						if( psg.get(i).getP().equals(danpaipsg.get(0).getP()) ){
							continue outer;
						}
					}
					list.add(danpaipsg.get(0));
				}
				if((psg.size()/3)>list.size()){
					continue;
				}
				List<PokerSeg> lists = nArrangeSortPoker(list,psg.size()/3);
				for(PokerSeg ssg : lists){
					PokerSeg tempadd = new PokerSeg();
					for(Poker ssggg : psg){
						tempadd.add(ssggg);
					}
					for(Poker p : ssg){
						tempadd.add(p);
					}
					pokerGroupEntry.getPokerSegGroup().add(tempadd);
				}
			}
			if( !pokerGroupEntry.getPokerSegGroup().isEmpty() )
				pokerGroup.add(pokerGroupEntry);
		}
		//11 FEI_JI_DAI_CHI_BANG_DAI_DUI
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.SAN_SHUN)){
			pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DUI,new PokerSegGroup());
			for(PokerSeg psg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_SHUN).getPokerSegGroup()){
				PokerSeg list = new PokerSeg();
				//add dui pai
				for(PokerSeg duipsg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI).getPokerSegGroup()){
					boolean isLeagel = true;
					for(Poker p : psg){
						if(p.getP().equals(duipsg.get(0).getP())){
							isLeagel = false;
						}
					}
					if(isLeagel){
						list.add(duipsg.get(0));
						list.add(duipsg.get(1));
					}
				}
				//add dan pai
				if((psg.size()/3)>list.size()){
					continue;
				}
				List<PokerSeg> lists = nArrangeSortPokerDui(list,psg.size()/3);
				for(PokerSeg ssg : lists){
					PokerSeg tempadd = new PokerSeg();
					for(Poker ssggg : psg){
						tempadd.add(ssggg);
					}
					for(Poker p : ssg){
						tempadd.add(p);
					}
					pokerGroupEntry.getPokerSegGroup().add(tempadd);
				}
			}
			if(!pokerGroupEntry.getPokerSegGroup().isEmpty() )
				pokerGroup.add(pokerGroupEntry);
		}
		//6 SAN_DAI_YI_DAI_DAN
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.SAN_ZHANG_PAI)){
			pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.SAN_DAI_YI_DAI_DAN,new PokerSegGroup());
			for(PokerSeg psg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_ZHANG_PAI).getPokerSegGroup()){
				PokerSeg list = new PokerSeg();
				//add dui pai
				for(PokerSeg duipsg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI).getPokerSegGroup()){
					boolean isLeagel = true;
					for(Poker p : psg){
						if(p.getP().equals(duipsg.get(0).getP())){
							isLeagel = false;
						}
					}
					if(isLeagel){
						list.add(duipsg.get(0));
					}
				}
				//add dan pai
				outer:
				for(PokerSeg danpaipsg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DAN_PAI).getPokerSegGroup()){
					int i = 0;
					for(i = 0;i< list.size(); i++){
						if( list.get(i).getP().equals(danpaipsg.get(0).getP()) ){//when it has same point then it can't add again
							continue outer;
						}
					}
					for(i = 0;i < psg.size(); i++){
						if( psg.get(i).getP().equals(danpaipsg.get(0).getP()) ){
							continue outer;
						}
					}
					list.add(danpaipsg.get(0));
				}
				for(Poker p : list){
					PokerSeg tempadd = new PokerSeg();
					for(Poker ssggg : psg){
						tempadd.add(ssggg);
					}
					tempadd.add(p);
					pokerGroupEntry.getPokerSegGroup().add(tempadd);
				}
			}
			if( !pokerGroupEntry.getPokerSegGroup().isEmpty() )
				pokerGroup.add(pokerGroupEntry);
		}
		//7 SAN_DAI_YI_DAI_DUI
		if(pokerGroup.containGroupNamed(PokerGroupTypeEnum.SAN_ZHANG_PAI)){
			pokerGroupEntry = new PokerGroupEntry(PokerGroupTypeEnum.SAN_DAI_YI_DAI_DUI,new PokerSegGroup());
			for(PokerSeg psg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.SAN_ZHANG_PAI).getPokerSegGroup()){
				List<PokerSeg> list = new ArrayList<PokerSeg>();
				//add dui pai
				for(PokerSeg duipsg : pokerGroup.getPokerGroupEntry(PokerGroupTypeEnum.DUI_PAI).getPokerSegGroup()){
					boolean isLeagel = true;
					for(Poker p : psg){
						if(p.getP().equals(duipsg.get(0).getP())){
							isLeagel = false;
						}
					}
					if(isLeagel){
						list.add(duipsg);
					}
				}
				//add dan pai
				for(PokerSeg pokerSeg : list){
					PokerSeg tempadd = new PokerSeg();
					for(Poker ssggg : psg){
						tempadd.add(ssggg);
					}
					for(Poker p : pokerSeg){
						tempadd.add(p);
					}
					pokerGroupEntry.getPokerSegGroup().add(tempadd);
				}
			}
			if( !pokerGroupEntry.getPokerSegGroup().isEmpty() )
				pokerGroup.add(pokerGroupEntry);
		}
		return pokerGroup;
	}
	private static void sortSanZhangPai(List<P> temp) {
		for(int i = 0;i < temp.size(); i++){
			for(int j = 0;j < temp.size() - 1 - i; j++){
				if(temp.get(j).getOrder() > temp.get(j+1).getOrder()){
					temp.set(j, temp.set(j+1, temp.get(j)));
				}
			}
		}
	}
	public static void removeLessFourConnected(List<P> p){
		if(p.size()<=4){
			p.clear();
			return;
		}
		for(int i = 0;i < p.size(); i++){
			for(int j = 0;j < p.size()-1-i ;j++){
				if(p.get(j).getOrder() > p.get(j+1).getOrder()){
					p.set(j, p.set(j+1, p.get(j)));
				}
			}
		}
		List<Integer> abandon = new ArrayList<Integer>();
		int counts = 0;
		for(int i = 0;i<p.size();i++){
			if( i == 0 ){
				counts++;
			}else{
				if(p.get(i).getOrder() - p.get(i-1).getOrder() == 1){
					counts++;
				}else{
					if(counts <= 4){
						while(counts>=1){
							abandon.add(i-counts);
							counts--;
						}
						counts = 1;
					}else{
						counts = 1;
					}
				}
			}
		}
		if(counts <= 4){
			while(counts>=1){
				abandon.add(p.size()-counts);
				counts--;
			}
		}
		for(int i = 0;i<abandon.size();i++){
			for(int j = 0; j<abandon.size()-1-i; j++){
				if(abandon.get(j) > abandon.get(j+1)){
					abandon.set(j, abandon.set(j+1, abandon.get(j)));
				}
			}
		}
		for(int i = abandon.size()-1; i>=0; i--){
			p.remove((int)abandon.get(i));
		}
	}
	public static List<List<P>> sort_ALL_SUB_SHUN(List<P> p,int count){
		List<List<P>> temp = new ArrayList<List<P>>();
		if(p.size() <= 1)
			return temp;
		int preIndex = 0;
		for(int i = 1;i < p.size();i++){
			if( p.get(i).getOrder() - p.get(i-1).getOrder() == 1 ){
				continue;
			}else{
				List<P> ttp = new ArrayList<P>();
				while(preIndex < i){
					ttp.add(p.get(preIndex));
					preIndex++;
				}
				temp.add(ttp);
				preIndex = i;
			}
		}
		List<P> ttp = new ArrayList<P>();
		while(preIndex < p.size()){
			ttp.add(p.get(preIndex));
			preIndex++;
		}
		temp.add(ttp);
		
		List<List<P>> allList = new ArrayList<List<P>>();		
		for(int i = 0;i<temp.size();i++){
			allList.addAll(nArrangeSort(temp.get(i)));
		}
		List<List<P>> res = new ArrayList<List<P>>();
		//remove one number and can't connected number
		outer:
		for(int i = 0; i<allList.size(); i++){
			if(allList.get(i).size() <= count){
				continue;
			}else{
				int j = 0;
				for(j = 1; j<allList.get(i).size(); j++){
					if(allList.get(i).get(j).getOrder() - allList.get(i).get(j-1).getOrder() != 1){
						continue outer;
					}
				}
				res.add(allList.get(i));
			}
		}
		//sort by number
		for(int i = 0; i<res.size(); i++){
			for(int m = 0; m<res.get(i).size(); m++){
				for(int n = 0; n<res.get(i).size()-1-m; n++){
					if(res.get(i).get(n).getOrder() < res.get(i).get(n+1).getOrder()){
						res.get(i).set(n, res.get(i).set(n+1, res.get(i).get(n)));
					}
				}
			}
		}
		return res;
	}
	public static List<PokerSeg> nArrangeSortPokerDui(PokerSeg pdui,int count){
		PokerSeg p = Common.getUniquePokerNumber(pdui);
		List<PokerSeg> tempp = new ArrayList<PokerSeg>();
		PokerSeg tempps = new PokerSeg();
		if(count <= -1){
			for(int i = 1; i<p.size(); i++){
				PokerSeg pplist = new PokerSeg();
				PermPoker(0,0,p.size(),i+1,p,new boolean[p.size()],pplist);
				for(int j = 0;j<pplist.size() ;j++){
					if(pplist.get(j) == null){
						tempp.add(tempps);
						tempps = new PokerSeg();
					}else{
						tempps.add(pplist.get(j));
					}
				}
			}
		}else{
			PokerSeg pplist = new PokerSeg();
			PermPoker(0,0,p.size(),count,p,new boolean[p.size()],pplist);
			for(int j = 0;j<pplist.size() ;j++){
				if(pplist.get(j) == null){
					tempp.add(tempps);
					tempps = new PokerSeg();
				}else{
					tempps.add(pplist.get(j));
				}
			}
		}
		for(Poker pps : p){
			PokerSeg tps = new PokerSeg();
			tps.add(pps);
			tempp.add(tps);
		}
		//Sprang out
		List<PokerSeg> result = new ArrayList<PokerSeg>();
		for(int outindex=0; outindex<tempp.size(); outindex++){
			result.add(new PokerSeg());
			for(int index=0; index<tempp.get(outindex).size(); index++){
				Poker temppoker = tempp.get(outindex).get(index);
				
				for(int pduiindex=0; pduiindex<pdui.size(); pduiindex++){
					if(temppoker.getP().getOrder() == 
							pdui.get(pduiindex).getP().getOrder())
						result.get(outindex).add(pdui.get(pduiindex));
				}
				
			}
		}
		return result;
	}	
	//zhu he pai lie 
	public static List<PokerSeg> nArrangeSortPoker(PokerSeg p,int count){
		List<PokerSeg> tempp = new ArrayList<PokerSeg>();
		PokerSeg tempps = new PokerSeg();
		if(count <= -1){
			for(int i = 1; i<p.size(); i++){
				PokerSeg pplist = new PokerSeg();
				PermPoker(0,0,p.size(),i+1,p,new boolean[p.size()],pplist);
				for(int j = 0;j<pplist.size() ;j++){
					if(pplist.get(j) == null){
						tempp.add(tempps);
						tempps = new PokerSeg();
					}else{
						tempps.add(pplist.get(j));
					}
				}
			}
		}else{
			PokerSeg pplist = new PokerSeg();
			PermPoker(0,0,p.size(),count,p,new boolean[p.size()],pplist);
			for(int j = 0;j<pplist.size() ;j++){
				if(pplist.get(j) == null){
					tempp.add(tempps);
					tempps = new PokerSeg();
				}else{
					tempps.add(pplist.get(j));
				}
			}
		}
		for(Poker pps : p){
			PokerSeg tps = new PokerSeg();
			tps.add(pps);
			tempp.add(tps);
		}
		return tempp;
	}
	public static List<PokerSeg> nArrangeSortPoker(PokerSeg p){
		return nArrangeSortPoker(p,-1);
	}
	public static void PermPoker(int pos,int cnt,int n,int k, PokerSeg a,boolean[] visited,PokerSeg container){
	    if (cnt == k) {
	        for (int i = 0; i < n; i++)
	            if (visited[i]) {
	            	container.add(a.get(i));
	            }
	        container.add(null);
	        return;
	    }

	    if (pos == n) return;

	    if (!visited[pos]) {
	        visited[pos] = true;
	        PermPoker(pos + 1, cnt + 1, n, k, a,visited,container);
	        visited[pos] = false;   
	    }
	    PermPoker(pos + 1, cnt, n, k, a, visited,container);
	}
	public static List<List<P>> nArrangeSort(List<P> p){
		List<List<P>> tempp = new ArrayList<List<P>>();
		List<P> tempps = new ArrayList<P>();
		for(int i = 1; i<p.size(); i++){
			List<P> pplist = new ArrayList<P>();
			Perm(0,0,p.size(),i+1,p,new boolean[p.size()],pplist);
			for(int j = 0;j<pplist.size() ;j++){
				if(pplist.get(j) == null){
					tempp.add(tempps);
					tempps = new ArrayList<P>();
				}else{
					tempps.add(pplist.get(j));
				}
			}
		}
		for(P pps : p){
			List<P> tps = new ArrayList<P>();
			tps.add(pps);
			tempp.add(tps);
		}
		return tempp;
	}
	
	public static void Perm(int pos,int cnt,int n,int k, List<P> a,boolean[] visited,List<P> container){
	    if (cnt == k) {
	        for (int i = 0; i < n; i++)
	            if (visited[i]) {
	            	container.add(a.get(i));
	            }
	        container.add(null);
	        return;
	    }

	    if (pos == n) return;

	    if (!visited[pos]) {
	        visited[pos] = true;
	        Perm(pos + 1, cnt + 1, n, k, a,visited,container);
	        visited[pos] = false;   
	    }
	    Perm(pos + 1, cnt, n, k, a, visited,container);
	}
	
	public static void sortBumbDes(PokerSegGroup pokerSegGroup){
		for(int i = 0; i<pokerSegGroup.size(); i++){
			for(int j = 0; j<pokerSegGroup.size()-i-1; j++){
				if(pokerSegGroup.get(j).get(0).getP().getOrder() < pokerSegGroup.get(j+1).get(0).getP().getOrder()){
					pokerSegGroup.set(j, pokerSegGroup.set(j+1, pokerSegGroup.get(j)));
				}
			}
		}
	}
	public static void removeLessDoubleConnected(List<P> p){
		if(p.size()<=2){
			p.clear();
			return;
		}
		for(int i = 0;i < p.size(); i++){
			for(int j = 0;j < p.size()-1-i ;j++){
				if(p.get(j).getOrder() > p.get(j+1).getOrder()){
					p.set(j, p.set(j+1, p.get(j)));
				}
			}
		}
		List<Integer> abandon = new ArrayList<Integer>();
		int counts = 0;
		for(int i = 0;i<p.size();i++){
			if( i == 0 ){
				counts++;
			}else{
				if(p.get(i).getOrder() - p.get(i-1).getOrder() == 1){
					counts++;
				}else{
					if(counts <= 2){
						while(counts>=1){
							abandon.add(i-counts);
							counts--;
						}
						counts = 1;
					}else{
						counts = 1;
					}
				}
			}
		}
		if(counts <= 2){
			while(counts>=1){
				abandon.add(p.size()-counts);
				counts--;
			}
		}
		for(int i = 0;i<abandon.size();i++){
			for(int j = 0; j<abandon.size()-1-i; j++){
				if(abandon.get(j) > abandon.get(j+1)){
					abandon.set(j, abandon.set(j+1, abandon.get(j)));
				}
			}
		}
		for(int i = abandon.size()-1; i>=0; i--){
			p.remove((int)abandon.get(i));
		}
	}
	public static void removeIsolate(List<P> p){
		List<Integer> abandon = new ArrayList<Integer>();
		if(p.size() == 1){
			p.remove(0);
			return;
		}
		for(int i = 0;i < p.size(); i++){
			for(int j = 0;j < p.size()-1-i ;j++){
				if(p.get(j).getOrder() > p.get(j+1).getOrder()){
					p.set(j, p.set(j+1, p.get(j)));
				}
			}
		}
		for(int i = 0;i<p.size();i++){
			if(i == 0){
				if(p.get(0).getOrder() - p.get(1).getOrder() != -1)
					abandon.add(0);
			}else if(i == p.size()-1){
				if(p.get(i-1).getOrder() - p.get(i).getOrder() != -1)
					abandon.add(i);
			}else{
				if( p.get(i).getOrder() - p.get(i-1).getOrder() != 1 &&
					p.get(i).getOrder() - p.get(i+1).getOrder() != -1)
					abandon.add(i);
			}
		}
		for(int i = 0;i<abandon.size();i++){
			for(int j = 0; j<abandon.size()-1-i; j++){
				if(abandon.get(j) > abandon.get(j+1)){
					abandon.set(j, abandon.set(j+1, abandon.get(j)));
				}
			}
		}
		for(int i = abandon.size()-1; i>=0; i--){
			p.remove((int)abandon.get(i));
		}
	}
	public static boolean hasPointInNumber(int num,P p,PokerSeg pokerSeg){
		for(int i = 0;i<pokerSeg.size();i++){
			if(pokerSeg.get(i).getP().equals(p)){
				num--;
			}
		}
		if(num<=0)
			return true;
		return false;
	}
	/**
	 * @param pokers 
	 * @return null if is not a legal Poker Type
	 */
	public static PokerGroupTypeEnum getPokerGroupTypeEnum(List<Poker> pokers){
		return getPokerGroupTypeEnum(new PokerSeg(pokers));
	}
	public static PokerGroupTypeEnum getPokerGroupTypeEnum(PokerSeg pokerSeg) {
		if(pokerSeg.size() == 1){
			return PokerGroupTypeEnum.DAN_PAI;
		}
		if(pokerSeg.size() == 2){
			if( pokerSeg.contains(new Poker(P.PXW,PokerTypeEnum.None)) &&
				pokerSeg.contains(new Poker(P.PDW,PokerTypeEnum.None))){
				return PokerGroupTypeEnum.ROCKET;
			}
			if( pokerSeg.get(0).getP().equals( pokerSeg.get(1).getP()) ){
				return PokerGroupTypeEnum.DUI_PAI;
			}
			return null;
		}
		if(pokerSeg.size() == 3){
			if((pokerSeg.get(0).getP().equals( pokerSeg.get(1).getP())) && 
				(pokerSeg.get(0).getP().equals( pokerSeg.get(2).getP()))){
				return PokerGroupTypeEnum.SAN_ZHANG_PAI;
			}
			return null;
		}
		if(pokerSeg.size() == 4){
			if( (pokerSeg.get(0).getP().equals( pokerSeg.get(1).getP())) &&
				(pokerSeg.get(0).getP().equals( pokerSeg.get(2).getP())) &&
				(pokerSeg.get(0).getP().equals( pokerSeg.get(3).getP())) ){
				return PokerGroupTypeEnum.BOMB;
			}
			int equalsCount = 0;
			equalsCount = Common.eqaulsCount(pokerSeg);
			if(equalsCount==3){
				return PokerGroupTypeEnum.SAN_DAI_YI_DAI_DAN;
			}
			return null;
		}
		if(pokerSeg.size() == 5){
			Common.sort(pokerSeg);
			boolean isShun = true;
			for(int index=1; index<pokerSeg.size(); index++){
				if(pokerSeg.get(index).getP().getOrder() - pokerSeg.get(index-1).getP().getOrder() != -1){
					isShun = false;
					break;
				}
			}
			if(isShun){
				return PokerGroupTypeEnum.DAN_SHUAN;
			}else{
				//conside if it's a san dai yi dai dui
				int unEqualCount = 0;
				for(int index=1; index<pokerSeg.size()-1; index++){
					if(pokerSeg.get(index).getP().getOrder() != pokerSeg.get(index+1).getP().getOrder())
						unEqualCount++;
				}
				if(unEqualCount==1){//only have one unequal situation
					return PokerGroupTypeEnum.SAN_DAI_YI_DAI_DUI;
				}
				return null;
			}
		}
		if(pokerSeg.size() == 6){
			Common.sort(pokerSeg);
			if(Common.isDanShun(pokerSeg)){
				return PokerGroupTypeEnum.DAN_SHUAN;
			}else if(Common.isShuangShun(pokerSeg)){
				return PokerGroupTypeEnum.SHUANG_SHUN;
			}else if(Common.isSanShun(pokerSeg)){
				return PokerGroupTypeEnum.SAN_SHUN;
			}else if(Common.isShiDaiEr(pokerSeg)){
				return PokerGroupTypeEnum.SHI_DAI_ER;
			}
		}
		if(pokerSeg.size() >= 7){
			Common.sort(pokerSeg);
			if(Common.isDanShun(pokerSeg)){
				return PokerGroupTypeEnum.DAN_SHUAN;
			}else if(Common.isShuangShun(pokerSeg)){
				return PokerGroupTypeEnum.SHUANG_SHUN;
			}else if(Common.isSanShun(pokerSeg)){
				return PokerGroupTypeEnum.SAN_SHUN;
			}else if(Common.isFeiJiDaiChiBangDaiDui(pokerSeg)){
				return PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DUI;
			}else if(Common.isFeiJiDaiChiBangDaiDan(pokerSeg)){
				return PokerGroupTypeEnum.FEI_JI_DAI_CHI_BANG_DAI_DAN;
			}
		}
		return null;
	}
}