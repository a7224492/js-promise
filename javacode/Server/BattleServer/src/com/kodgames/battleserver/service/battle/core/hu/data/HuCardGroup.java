package com.kodgames.battleserver.service.battle.core.hu.data;

public final class HuCardGroup
{
	public int groupType;
	public byte[] cardList;

	public HuCardGroup(int cardCount)
	{
		this.cardList = new byte[cardCount];
	}
	
	public HuCardGroup(HuCardGroup other)
	{
		this.groupType = other.groupType;
		this.cardList = other.cardList.clone();
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(this.groupType).append(",");
		
		_sb_.append("cardList=[");
		for (byte card : cardList)
			_sb_.append(card + ",");
		_sb_.append("],");
		
		_sb_.append(")");
		return _sb_.toString();
	}
}
