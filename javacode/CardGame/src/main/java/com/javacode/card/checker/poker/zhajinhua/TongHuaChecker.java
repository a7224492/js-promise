package com.javacode.card.checker.poker.zhajinhua;

import com.javacode.card.comparator.ICard;
import com.javacode.card.comparator.ICardGroup;
import com.javacode.card.defines.poker.CardGroupType;
import java.util.List;

/**
 * Created by jiangzhen on 2018/1/27
 */
public class TongHuaChecker extends ZhaJinHuaChecker
{
	public TongHuaChecker()
	{
		super(CardGroupType.CARD_GROUP_TONG_HUA);
	}

	@Override
	protected ICardGroup zhaJinHuaChecker(List<ICard> cardList)
	{
		return cardList.stream().map(ICard::getType).distinct().count() == 1? produceCardGroup(cardList):null;
	}
}
