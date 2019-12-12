package com.ai.poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PokerSeg implements List<Poker>{

	private List<Poker> poker = null;
	
	public PokerSeg(){
		this(new ArrayList<Poker>());
	}
	
	public PokerSeg(List<Poker> pokers){
		this.poker = pokers;
	}
	
	public PokerSeg(PokerSeg pokerSeg){
		this.poker = pokerSeg;
	}
	/**
	 * Bubble sort,from bigger to smaller
	 */
	public void sort(){
		for(int i = 0;i<poker.size();i++){
			boolean NoChanged = true;
			for(int j = 0;j<poker.size()-1-i;j++){
				if(poker.get(j).getP().getOrder() < poker.get(j+1).getP().getOrder()){
					poker.set(j, poker.set(j+1, poker.get(j)));
					NoChanged = false;
				}
			}
			if(NoChanged){
				break;
			}
		}
	}
	@Override
	public int size() {
		return poker.size();
	}

	@Override
	public boolean isEmpty() {
		return size()==0?true:false;
	}

	@Override
	public boolean contains(Object o) {
		return poker.contains(o);
	}

	@Override
	public Iterator<Poker> iterator() {
		return poker.iterator();
	}

	@Override
	public Object[] toArray() {
		return poker.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return poker.toArray(a);
	}

	@Override
	public boolean add(Poker e) {
		return poker.add(e);
	}
	public PokerSeg addp(Poker e){
		this.poker.add(e);
		return this;
	}
	@Override
	public boolean remove(Object o) {
		return poker.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return poker.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Poker> c) {
		return poker.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Poker> c) {
		return poker.addAll(index,c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return poker.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return poker.retainAll(c);
	}

	@Override
	public void clear() {
		poker.clear();
	}

	@Override
	public Poker get(int index) {
		return poker.get(index);
	}

	@Override
	public Poker set(int index, Poker element) {
		return poker.set(index, element);
	}

	@Override
	public void add(int index, Poker element) {
		poker.add(index,element);
	}

	@Override
	public Poker remove(int index) {
		return poker.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return poker.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return poker.lastIndexOf(o);
	}

	@Override
	public ListIterator<Poker> listIterator() {
		return poker.listIterator();
	}

	@Override
	public ListIterator<Poker> listIterator(int index) {
		return poker.listIterator(index);
	}

	@Override
	public List<Poker> subList(int fromIndex, int toIndex) {
		return poker.subList(fromIndex, toIndex);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((poker == null) ? 0 : poker.hashCode());
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
		PokerSeg other = (PokerSeg) obj;
		if (poker == null) {
			if (other.poker != null)
				return false;
		} else if (!poker.equals(other.poker))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PokerSeg [poker=" + poker + "]";
	}
}
