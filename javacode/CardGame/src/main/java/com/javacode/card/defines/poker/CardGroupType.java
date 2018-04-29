package com.javacode.card.defines.poker;

/**
 * Created by jiangzhen on 2018/1/30.
 * function:
 */
public enum CardGroupType
{
	CARD_GROUP_BAO_ZI(7), CARD_GROUP_TONG_HUA_SHUN(6), CARD_GROUP_TONG_HUA(5), CARD_GROUP_SHUN_ZI(4),
	CARD_GROUP_DUI_ZI(3), CARD_GROUP_DAN_ZHANG(2), CARD_GROUP_SPECIAL(1), UN_KOWN(0);

	private int type;
	CardGroupType(int type)
	{
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int v()
	{
		return this.type;
	}
}
