package com.javacode.card.checker.poker.zhajinhua;

import com.javacode.card.bean.CardGroup;
import com.javacode.card.checker.poker.Checker;
import com.javacode.card.comparator.ICard;
import com.javacode.card.comparator.ICardGroup;
import com.javacode.card.defines.poker.CardGroupType;
import java.util.ArrayList;
import java.util.List;

import static com.javacode.card.defines.poker.CardGroupType.*;

/**
 * Created by jiangzhen on 2018/1/27
 */
abstract class ZhaJinHuaChecker extends Checker
{
	protected ZhaJinHuaChecker(CardGroupType type) {
		super(type);
	}

	@Override
	protected ICardGroup _check(List<ICard> cardList)
	{
		if (cardList.size() != 3)
		{
			return null;
		}

		return zhaJinHuaChecker(cardList);
	}

	protected abstract ICardGroup zhaJinHuaChecker(List<ICard> cardList);

	@Override
	protected ICardGroup produceCardGroup(List<ICard> cardList)
	{
		return new CardGroup(cardList, getType());
	}
}