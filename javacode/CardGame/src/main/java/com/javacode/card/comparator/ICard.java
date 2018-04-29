package com.javacode.card.comparator;

import com.javacode.card.defines.poker.CardType;
import com.javacode.card.defines.poker.CardValue;

/**
 * Created by jiangzhen on 2018/1/26.
 * function:
 */
public interface ICard
{
	/**
	 * @see com.javacode.card.bean.Card
	 * @return 牌面值
	 */
	CardValue getValue();

	/**
	 * @see com.javacode.card.bean.Card
	 * @return 牌型
	 */
	CardType getType();

	/**
	 * @see com.javacode.card.bean.Card
	 * @return 比较值
	 */
	int getCompareValue();
}
