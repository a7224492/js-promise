package com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.operation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

public class OperationResultFilter_TanBaZhang extends OperationResultFilter
{
	private Set<CardType> checkTypes = new HashSet<>();
	{
		checkTypes.add(CardType.WAN);
		checkTypes.add(CardType.TIAO);
		checkTypes.add(CardType.TONG);
		checkTypes.add(CardType.FENG);
	}

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		List<Byte> handCards = context.getPlayerById(result.getRoleId()).getCards().getHandCards();
		handCards.add(card);
		for (CardType t : checkTypes)
		{
			Integer count = 0;
			// 判断各种花色中是否有牌数量大于等于八的花色
			for (Byte handCard : handCards)
			{
				CardType type = CardType.getCardType(handCard);

				if (type == t)
					count++;
			}
			if (count >= 8)
			{
				handCards.remove(handCards.size() - 1);
				return true;
			}
		}

		handCards.remove(handCards.size() - 1);
		return false;
	}
}
