package com.javacode.card.checker.poker;

import com.javacode.card.comparator.ICard;
import com.javacode.card.comparator.ICardGroup;
import com.javacode.card.defines.poker.CardGroupType;
import java.util.List;

/**
 * Created by jiangzhen on 2018/1/27
 */
public abstract class Checker
{
	private CardGroupType type;

	protected Checker(CardGroupType type) {
		this.type = type;
	}

	/**
	 * 判断这个牌的牌型
	 * @param cardList 这副牌
	 * @return null 未知的牌型 非null 牌型
	 */
	public ICardGroup check(List<ICard> cardList)
	{
		if (cardList == null || cardList.isEmpty())
		{
			return null;
		}

		return _check(cardList);
	}

	protected abstract ICardGroup _check(List<ICard> cardList);
	protected abstract ICardGroup produceCardGroup(List<ICard> cardList);

	protected CardGroupType getType()
	{
		return this.type;
	}
}
