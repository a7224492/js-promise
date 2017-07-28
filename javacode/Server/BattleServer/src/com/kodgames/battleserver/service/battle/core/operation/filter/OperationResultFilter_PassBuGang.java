package com.kodgames.battleserver.service.battle.core.operation.filter;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 如果Pass了补杠, 以后再也不能补杠
 */
public class OperationResultFilter_PassBuGang extends OperationResultFilter
{
	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 只检测补杠
		if (Macro.AssetTrue(result.getPlayType() != PlayType.OPERATE_CAN_BU_GANG_A_CARD))
			return true;

		// 玩家上上一个操作应该是摸到需要补杠的这张牌
		Step lastStep = context.getLastRecordStep(0);
		if (lastStep.getPlayType() != PlayType.OPERATE_DEAL)
			return false;

		// 如果操作的不是当前摸到的卡，就返回false
		for (byte resultCard : result.getCards())
			if (resultCard != card)
				return false;

		return true;
	}
}
