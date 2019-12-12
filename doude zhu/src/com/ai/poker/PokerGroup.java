package com.ai.poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * Remember:
 * <p/>		PokerGroup can't be used actual pokers of users,it's only sorting pokers.
 * <p/>1 ROCKET
 * <p/>2 BOMB
 * <p/>3 DAN_PAI
 * <p/>4 DUI_PAI
 * <p/>5 SAN_ZHANG_PAI
 * <p/>6 SAN_DAI_YI
 * <p/>7 DAN_SHUAN
 * <p/>8 SHUANG_SHUN
 * <p/>9 SAN_SHUN
 * <p/>10 FEI_JI_DAI_CHI_BANG
 * <p/>11 SHI_DAI_ER
 * 
 * @author Administrator
 */
public class PokerGroup implements List<PokerGroupEntry>{
	private List<PokerGroupEntry> pokerGroupEntries;
	public PokerGroup(){
		this(new ArrayList<PokerGroupEntry>());
	}
	public PokerGroup(List<PokerGroupEntry> pokerGroupEntries){
		this.pokerGroupEntries = pokerGroupEntries;
	}
	public void remove(PokerGroupTypeEnum pokerGroupTypeEnum){
		PokerGroupEntry needRemove = null;
		for(PokerGroupEntry pge : pokerGroupEntries){
			if(pge.getPokerGroupTypeEnum().equals(pokerGroupTypeEnum)){
				needRemove = pge;
				break;
			}
		}
		if (needRemove!=null)
			pokerGroupEntries.remove(needRemove);
	}
	public PokerGroupEntry getPokerGroupEntry(PokerGroupTypeEnum pokerGroupTypeEnum){
		for(PokerGroupEntry pge : pokerGroupEntries){
			if(pge.getPokerGroupTypeEnum() == pokerGroupTypeEnum){
				return pge;
			}
		}
		return null;
	}
	public boolean containGroupNamed(PokerGroupTypeEnum pokerGroupTypeEnum){
		for(PokerGroupEntry pge : pokerGroupEntries){
			if(pge.getPokerGroupTypeEnum().equals(pokerGroupTypeEnum)){
				return true;
			}
		}
		return false;
	}
	@Override
	public int size() {
		return pokerGroupEntries.size();
	}
	@Override
	public boolean isEmpty() {
		return size()==0?true:false;
	}
	@Override
	public boolean contains(Object o) {
		return pokerGroupEntries.contains(o);
	}
	@Override
	public Iterator<PokerGroupEntry> iterator() {
		return pokerGroupEntries.iterator();
	}
	@Override
	public Object[] toArray() {
		return pokerGroupEntries.toArray();
	}
	@Override
	public <T> T[] toArray(T[] a) {
		return pokerGroupEntries.toArray(a);
	}
	@Override
	public boolean add(PokerGroupEntry e) {
		return pokerGroupEntries.add(e);
	}
	public PokerGroup addp(PokerGroupEntry e){
		this.pokerGroupEntries.add(e);
		return this;
	}
	@Override
	public boolean remove(Object o) {
		return pokerGroupEntries.remove(o);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return pokerGroupEntries.containsAll(c);
	}
	@Override
	public boolean addAll(Collection<? extends PokerGroupEntry> c) {
		return pokerGroupEntries.addAll(c);
	}
	@Override
	public boolean addAll(int index, Collection<? extends PokerGroupEntry> c) {
		return pokerGroupEntries.addAll(index, c);
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		return pokerGroupEntries.removeAll(c);
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		return pokerGroupEntries.retainAll(c);
	}
	@Override
	public void clear() {
		pokerGroupEntries.clear();
	}
	@Override
	public PokerGroupEntry get(int index) {
		return pokerGroupEntries.get(index);
	}
	@Override
	public PokerGroupEntry set(int index, PokerGroupEntry element) {
		return pokerGroupEntries.set(index, element);
	}
	@Override
	public void add(int index, PokerGroupEntry element) {
		pokerGroupEntries.add(index,element);
	}
	@Override
	public PokerGroupEntry remove(int index) {
		return pokerGroupEntries.remove(index);
	}
	@Override
	public int indexOf(Object o) {
		return pokerGroupEntries.indexOf(o);
	}
	@Override
	public int lastIndexOf(Object o) {
		return pokerGroupEntries.lastIndexOf(o);
	}
	@Override
	public ListIterator<PokerGroupEntry> listIterator() {
		return pokerGroupEntries.listIterator();
	}
	@Override
	public ListIterator<PokerGroupEntry> listIterator(int index) {
		return pokerGroupEntries.listIterator(index);
	}
	@Override
	public List<PokerGroupEntry> subList(int fromIndex, int toIndex) {
		return pokerGroupEntries.subList(fromIndex, toIndex);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((pokerGroupEntries == null) ? 0 : pokerGroupEntries
						.hashCode());
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
		PokerGroup other = (PokerGroup) obj;
		if (pokerGroupEntries == null) {
			if (other.pokerGroupEntries != null)
				return false;
		} else if (!pokerGroupEntries.equals(other.pokerGroupEntries))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PokerGroup [pokerGroupEntries=" + pokerGroupEntries + "]";
	}
	public boolean removePokerSegContainsExcepb(Poker poker,PokerGroupTypeEnum[] arr){
		for(int i = 0;i<pokerGroupEntries.size(); i++){
			PokerGroupEntry pokerGroupEntry = pokerGroupEntries.get(i);
			if(Common.contains(arr,pokerGroupEntry.getPokerGroupTypeEnum())){
				for(PokerSeg pokerSeg : pokerGroupEntry.getPokerSegGroup()){
					if(pokerSeg.contains(poker)){
						return true;
					}
				}
			}
		}
		return false;
	}
	public void removePokerSegContainsExcep(Poker poker, PokerGroupTypeEnum type) {
		for(int i = 0;i<pokerGroupEntries.size(); i++){
			PokerGroupEntry pokerGroupEntry = pokerGroupEntries.get(i);
			if(!pokerGroupEntry.getPokerGroupTypeEnum().equals(type)){
				List<PokerSeg> needRemove = new ArrayList<PokerSeg>();
				for(PokerSeg pokerSeg : pokerGroupEntry.getPokerSegGroup()){
					if(pokerSeg.contains(poker)){
						needRemove.add(pokerSeg);
					}
				}
				for(PokerSeg pokerSeg : needRemove){
					pokerGroupEntry.getPokerSegGroup().remove(pokerSeg);
				}
			}
		}
	}
	public boolean removePokerSegDanpaiIn(Poker poker,
			PokerGroupTypeEnum[] arr) {
		boolean needRemove = false;
		for(int i = 0;i<pokerGroupEntries.size(); i++){
			PokerGroupEntry pokerGroupEntry = pokerGroupEntries.get(i);
			if(Common.contains(arr,pokerGroupEntry.getPokerGroupTypeEnum())){
				for(PokerSeg pokerSeg : pokerGroupEntry.getPokerSegGroup()){
					if(pokerSeg.contains(poker)){
						needRemove = true;
					}
				}
			}
		}
		return needRemove;
	}
	public void removeEmptyPokerGroupEntry() {
		List<PokerGroupEntry> needremove = new ArrayList<PokerGroupEntry>();
		for(PokerGroupEntry pokerGroupEntry : pokerGroupEntries){
			if(pokerGroupEntry.getPokerSegGroup().size()<=0){
				needremove.add(pokerGroupEntry);
			}
		}
		for(PokerGroupEntry p : needremove){
			pokerGroupEntries.remove(p);
		}
	}
}
