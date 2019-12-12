package com.ai.poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class PokerSegGroup implements List<PokerSeg>{

	private List<PokerSeg> pokerSegs = null;
	
	public PokerSegGroup(){
		this(new ArrayList<PokerSeg>());
	}
	
	public PokerSegGroup(List<PokerSeg> pokerSegs){
		this.pokerSegs = pokerSegs;
	}
	
	public PokerSeg getLast(){
		return pokerSegs.get(pokerSegs.size() - 1);
	}
	public PokerSegGroup clone(){
		List<PokerSeg> temp = new ArrayList<PokerSeg>();
		for(PokerSeg pokerSeg : pokerSegs){
			temp.add(Common.copyPokers(pokerSeg));
		}
		return new PokerSegGroup(temp);
	}
	@Override
	public int size() {
		return pokerSegs.size();
	}

	@Override
	public boolean isEmpty() {
		return size()==0?true:false;
	}

	@Override
	public boolean contains(Object o) {
		return pokerSegs.contains(o);
	}

	@Override
	public Iterator<PokerSeg> iterator() {
		return pokerSegs.iterator();
	}

	@Override
	public Object[] toArray() {
		return pokerSegs.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return pokerSegs.toArray(a);
	}

	@Override
	public boolean add(PokerSeg e) {
		return pokerSegs.add(e);
	}
	
	public PokerSegGroup addp(PokerSeg e){
		this.pokerSegs.add(e);
		return this;
	}
	
	@Override
	public boolean remove(Object o) {
		return pokerSegs.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return pokerSegs.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends PokerSeg> c) {
		return pokerSegs.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends PokerSeg> c) {
		return pokerSegs.addAll(index,c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return pokerSegs.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return pokerSegs.retainAll(c);
	}

	@Override
	public void clear() {
		pokerSegs.clear();
	}

	@Override
	public PokerSeg get(int index) {
		return pokerSegs.get(index);
	}

	@Override
	public PokerSeg set(int index, PokerSeg element) {
		return pokerSegs.set(index, element);
	}

	@Override
	public void add(int index, PokerSeg element) {
		pokerSegs.add(index, element);
	}

	@Override
	public PokerSeg remove(int index) {
		return pokerSegs.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return pokerSegs.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return pokerSegs.lastIndexOf(o);
	}

	@Override
	public ListIterator<PokerSeg> listIterator() {
		return pokerSegs.listIterator();
	}

	@Override
	public ListIterator<PokerSeg> listIterator(int index) {
		return pokerSegs.listIterator(index);
	}

	@Override
	public List<PokerSeg> subList(int fromIndex, int toIndex) {
		return pokerSegs.subList(fromIndex, toIndex);
	}

	public void removeLast() {
		pokerSegs.remove(pokerSegs.size()-1);
	}

	public PokerSeg popLast() {
		PokerSeg last = pokerSegs.get(pokerSegs.size()-1);
		removeLast();
		return last;
	}

	public List<Poker> pop() {
		PokerSeg first = pokerSegs.get(0);
		pokerSegs.remove(0);
		return first;
	}
}
