package com.javacode.card.checker.poker.zhajinhua;

import com.javacode.card.checker.poker.Checker;
import com.javacode.card.comparator.ICard;
import com.javacode.card.comparator.ICardGroup;
import com.javacode.card.defines.poker.CardGroupType;
import java.util.List;

import static com.javacode.card.defines.poker.CardGroupType.CARD_GROUP_SHUN_ZI;
import static com.javacode.card.defines.poker.CardGroupType.CARD_GROUP_TONG_HUA;
import static com.javacode.card.defines.poker.CardGroupType.CARD_GROUP_TONG_HUA_SHUN;

/**
 * Created by jiangzhen on 2018/1/27
 */
public class TongHuaShunChecker extends ZhaJinHuaChecker
{
	private Checker tongHuaChecker = new TongHuaChecker();
	private Checker shunZiChecker = new ShunZiChecker();

	public TongHuaShunChecker()
	{
		super(CARD_GROUP_TONG_HUA_SHUN);
	}

	@Override
	protected ICardGroup zhaJinHuaChecker(List<ICard> cardList)
	{
		if (tongHuaChecker.check(cardList) != null && shunZiChecker.check(cardList) != null)
		{
			return produceCardGroup(cardList);
		}

		return null;
	}
}
