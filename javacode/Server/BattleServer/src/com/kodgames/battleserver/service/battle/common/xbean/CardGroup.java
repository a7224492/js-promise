package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.ArrayList;
import java.util.List;

/** 胡牌组数据 */
public final class CardGroup
{
	/** 组的类型 */
	private int groupType;
	/** 组内数据 */
	private ArrayList<Byte> cardList;

	public CardGroup()
	{
		cardList = new ArrayList<Byte>();
	}

	public CardGroup(CardGroup cardGroup)
	{
		this();
		copyFrom(cardGroup);
	}

	public void copyFrom(CardGroup cardGroup)
	{
		this.groupType = cardGroup.groupType;
		this.cardList.clear();
		this.cardList.addAll(cardGroup.cardList);
	}

	public int getGroupType()
	{
		return this.groupType;
	}

	public List<Byte> getCardList()
	{
		return this.cardList;
	}

	public void setGroupType(int groupType)
	{
		this.groupType = groupType;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof CardGroup == false)
			return false;

		CardGroup cardGroup = (CardGroup)object;
		if (this.groupType != cardGroup.groupType)
			return false;
		if (!this.cardList.equals(cardGroup.cardList))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.groupType;
		_h_ += _h_ * 31 + this.cardList.hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.groupType).append(",");
		_sb_.append(this.cardList).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}