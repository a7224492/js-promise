package com.kodgames.battleserver.service.battle.core.operation.filter;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/** 检测碰杠只能2色 */
public class OperationResultFilter_PengGang extends OperationResultFilter
{
	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		if (result != null)
		{
			// 获取已经碰杠的牌的类型
			List<CardType> cardTypes = new ArrayList<>();
			CardInfo cardInfo = context.getPlayers().get(result.getRoleId()).getCards();
			cardInfo.getCardHeap().stream().filter(step -> PlayType.isChiPengGang(step.getPlayType())).forEach(step -> {
				CardType cardType = CardType.getCardType(step.getCards().get(0));
				if (!cardTypes.contains(cardType))
					cardTypes.add(cardType);
			});

			// 如果碰杠牌类型超过2种，这次碰杠操作不是其中任何一种类型，那么不可以碰杠操作
			for (byte resultCard : result.getCards())
			{
				if (cardTypes.size() >= 2 && !cardTypes.contains(CardType.getCardType(resultCard)))
					return false;
			}

			return true;
		}
		else
			return true;
	}
}