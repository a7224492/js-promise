package com.kodgames.battleserver.service.battle.core.playcard.filter;

import java.util.List;
import java.util.stream.Collectors;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 检测定缺
 * 
 * 当定缺开启时，当前手牌中有定缺花色的牌时必须优先出定缺花色。
 */
public class PlayCardFilter_Lack extends PlayCardFilter
{
	@Override
	public List<Byte> filterCard(BattleBean context, int roleId, List<Byte> cards)
	{
		CardType lackType = CardType.INVALID;
		for (Step step : context.getPlayers().get(roleId).getCards().getCardHeap())
		{
			if (step.getPlayType() == PlayType.OPERATE_LACK)
			{
				lackType = CardType.getCardType(step.getCards().get(0));
				break;
			}
		}

		if (lackType == CardType.INVALID)
			return cards;

		boolean hasLackCard = false;
		for (byte card : cards)
		{
			if (CardType.getCardType(card) == lackType)
			{
				hasLackCard = true;
				break;
			}
		}

		if (hasLackCard)
		{
			final CardType filterType = lackType;
			cards = cards.stream().filter(card -> filterType.isBelongTo(card)).collect(Collectors.toList());
		}

		return cards;
	}

}