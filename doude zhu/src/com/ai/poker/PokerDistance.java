package com.ai.poker;

/**
 * PokerDistance represents the distance between two PokerSegs
 * <br/>
 * except under constant value, also have the compared result value of two List Pokers.
 * @author Administrator
 *
 */
public class PokerDistance {
	/**
	 * doesn't have distance
	 */
	public static int NONE = -9999999;
	/**
	 * rocket distance only use rocket
	 */
	public static int ROCKET = 999999;
	/**
	 * bomb distance only use bomb
	 */
	public static int BOMB = 9999;
	/**
	 * the distance only use when two length is different
	 */
	public static int SHUNLENGTH = 500;
}
