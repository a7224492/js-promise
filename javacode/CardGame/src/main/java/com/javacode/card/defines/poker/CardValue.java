package com.javacode.card.defines.poker;

/**
 * Created by jiangzhen on 2018/1/26.
 * function:
 */
public enum CardValue
{
	CARD_A(1), CARD_2(2), CARD_3(3), CARD_4(4), CARD_5(5), CARD_6(6), CARD_7(7), CARD_8(8), CARD_9(9), CARD_10(10), CARD_J(11), CARD_Q(12), CARD_K(13);

	private int v;
	CardValue(int v)
	{
		this.v = v;
	}

	public int v()
	{
		return v;
	}
}
