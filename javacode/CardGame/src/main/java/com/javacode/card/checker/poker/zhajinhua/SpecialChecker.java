package com.javacode.card.checker.poker.zhajinhua;

import com.javacode.card.comparator.ICard;
import com.javacode.card.comparator.ICardGroup;
import com.javacode.card.defines.poker.CardGroupType;
import java.util.Comparator;
import java.util.List;

import static com.javacode.card.defines.poker.CardValue.CARD_2;
import static com.javacode.card.defines.poker.CardValue.CARD_3;
import static com.javacode.card.defines.poker.CardValue.CARD_5;

/**
 * Created by jiangzhen on 2018/1/30
 */
public class SpecialChecker extends ZhaJinHuaChecker
{
	public SpecialChecker()
	{
		super(CardGroupType.CARD_GROUP_SPECIAL);
	}

	@Override
	protected ICardGroup zhaJinHuaChecker(List<ICard> cardList)
	{
		cardList.sort(Comparator.comparing(card -> card.getValue().v()));
		if (cardList.get(0).getValue() == CARD_2 && cardList.get(1).getValue() == CARD_3 && cardList.get(2).getValue() == CARD_5)
		{
			return produceCardGroup(cardList);
		}

		return null;
	}
}
