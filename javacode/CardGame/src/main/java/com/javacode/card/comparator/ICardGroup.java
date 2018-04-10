package com.javacode.card.comparator;

import com.javacode.card.defines.poker.CardGroupType;
import java.util.List;

/**
 * Created by jiangzhen on 2018/1/30.
 * function:
 */
public interface ICardGroup
{
	List<ICard> getCardList();

	CardGroupType getCardGroupType();

	void setCardGroupType(CardGroupType type);
}
