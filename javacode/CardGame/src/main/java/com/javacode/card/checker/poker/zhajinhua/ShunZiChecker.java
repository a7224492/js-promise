package com.javacode.card.checker.poker.zhajinhua;

import com.javacode.card.comparator.ICard;
import com.javacode.card.comparator.ICardGroup;
import com.javacode.card.defines.poker.CardGroupType;
import com.javacode.card.defines.poker.CardValue;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jiangzhen on 2018/1/27
 */
public class ShunZiChecker extends ZhaJinHuaChecker
{
	public ShunZiChecker()
	{
		super(CardGroupType.CARD_GROUP_SHUN_ZI);
	}

	@Override
	protected ICardGroup zhaJinHuaChecker(List<ICard> cardList)
	{
		cardList.sort(Comparator.comparingInt(c -> c.getValue().v()));
		if (cardList.get(2).getValue() == CardValue.CARD_A)
		{
			if (cardList.get(0).getValue() == CardValue.CARD_2 && cardList.get(1).getValue() == CardValue.CARD_3)
			{
				return produceCardGroup(cardList);
			}
		}
		else
		{
			if (cardList.get(2).getValue().v() - cardList.get(1).getValue().v() == 1 && cardList.get(1).getValue().v() - cardList.get(0).getValue().v() == 1)
			{
				return produceCardGroup(cardList);
			}
		}

		return null;
	}

	public boolean isShunZi(int cards)
	{
		return cards % 7 == 0;
	}
}