package com.kodgames.battleserver.service.battle.core.operation.filter;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.PassInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * Pass碰牌之后, 摸牌之前, 不能碰同一张牌
 */
public class OperationResultFilter_PassPeng extends OperationResultFilter
{
	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		if (Macro.AssetTrue(result.getPlayType() == PlayType.OPERATE_PENG_A_CARD))
			return true;

		CardInfo cardInfo = context.getPlayers().get(result.getRoleId()).getCards();

		// 如果PassInfo中包含碰，摸牌之前, 不能碰同一张牌
		for (PassInfo passInfo : cardInfo.getPassInfos())
		{
			if (passInfo.getPlayType() != PlayType.OPERATE_CAN_PENG_A_CARD)
				continue;

			if (passInfo.getPlayRound() != cardInfo.getPlayRound())
				continue;

			if (passInfo.getCard().get(0) == card)
				return false;
		}

		return true;
	}
}
