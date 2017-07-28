package com.kodgames.battleserver.service.battle.core.operation.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 过滤不可是定缺牌型
 */
public class OperationResultFilter_Lack extends OperationResultFilter
{
	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		if (result != null)
		{
			PlayerInfo playerInfo = context.getPlayers().get(result.getRoleId());

			// 获取玩家定缺类型
			CardType lackType = CardType.INVALID;
			for (Step step : playerInfo.getCards().getCardHeap())
			{
				if (step.getPlayType() == PlayType.OPERATE_LACK)
				{
					lackType = CardType.getCardType(step.getCards().get(0));
					break;
				}
			}

			// 如果操作牌型是定缺类型，过滤
			if (lackType != CardType.INVALID)
			{
				for (byte resultCard : result.getCards())
				{
					if (lackType.isBelongTo(resultCard))
						return false;
				}
			}
		}

		return true;
	}
}