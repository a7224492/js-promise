package com.javacode.card.bean;

import com.javacode.card.comparator.ICard;
import com.javacode.card.comparator.ICardGroup;
import com.javacode.card.defines.poker.CardGroupType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.javacode.card.defines.poker.CardGroupType.UN_KOWN;

/**
 * Created by jiangzhen on 2018/1/27
 */
public class CardGroup implements ICardGroup
{
	/**
	 * 这副牌的所有牌
	 */
	private List<ICard> cardList = new ArrayList<>();

	/**
	 * 这副牌的牌型
	 */
	private CardGroupType type = UN_KOWN;

	public CardGroup(List<ICard> cardList)
	{
		this.cardList.addAll(cardList);
	}

	public CardGroup(List<ICard> cardList, CardGroupType type)
	{
		this.cardList.addAll(cardList);
		this.type = type;
	}

	@Override
	public List<ICard> getCardList()
	{
		return cardList;
	}

	@Override
	public CardGroupType getCardGroupType()
	{
		return type;
	}

	@Override
	public void setCardGroupType(CardGroupType type) {
		this.type = type;
	}
}