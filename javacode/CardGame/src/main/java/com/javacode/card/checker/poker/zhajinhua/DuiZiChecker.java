package com.javacode.card.checker.poker.zhajinhua;

import com.javacode.card.comparator.ICard;
import com.javacode.card.comparator.ICardGroup;
import com.javacode.card.defines.poker.CardGroupType;
import java.util.List;

/**
 * Created by jiangzhen on 2018/1/29
 */
public class DuiZiChecker extends ZhaJinHuaChecker
{
	public DuiZiChecker()
	{
		super(CardGroupType.CARD_GROUP_DUI_ZI);
	}

	@Override
	protected ICardGroup zhaJinHuaChecker(List<ICard> cardList)
	{
		if (cardList.get(0).getValue() == cardList.get(1).getValue() && cardList.get(0).getValue() != cardList.get(2).getValue())
		{
			return produceCardGroup(cardList);
		}

		if (cardList.get(1).getValue() == cardList.get(2).getValue() && cardList.get(1).getValue() != cardList.get(0).getValue())
		{
			return produceCardGroup(cardList);
		}

		if (cardList.get(2).getValue() == cardList.get(0).getValue() && cardList.get(2).getValue() != cardList.get(1).getValue())
		{
			return produceCardGroup(cardList);
		}

		return null;
	}
}
