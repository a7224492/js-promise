package com.javacode.card.bean;

/**
 * Created by jiangzhen on 2018/1/26
 * 表示一张牌，比如黑桃A，红桃2，梅花3，方块4
 */
public class Card
{
	/**
	 * 牌型，比如黑桃，红桃，梅花，方块，王
	 */
	private int type;

	/**
	 * 牌面值，比如黑桃A，红桃A的value是1，黑桃2，红桃2的value是2
	 */
	private int value;

	/**
	 * 用来比较大小的值，比如在诈金花中，黑桃A，红桃A的大小值就是13，黑桃2，红桃2的大小值就是1
	 */
	private int compareValue;

	public Card(int type, int value, int compareValue)
	{
		this.type = type;
		this.value = value;
		this.compareValue = compareValue;
	}

	public int getType()
	{
		return type;
	}

	public int getValue()
	{
		return value;
	}

	public int getCompareValue()
	{
		return compareValue;
	}
}