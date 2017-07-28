package com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.operation.filter;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

/**
 * 过滤掉吃胡胡牌牌型（除了地胡）
 */
public class OperationResultFilter_Hu extends OperationResultFilter
{

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{

		Macro.AssetFalse(result.getPlayType() == PlayType.OPERATE_CAN_HU, "在不是可以胡的时候进行了检测");
		// 过滤后剩下可以胡牌类型
		if (result.getPlayType() != PlayType.OPERATE_CAN_HU)
		{
			return true;
		}
		// 过滤后剩下除了自摸以后的胡牌类型
		if (phaseDeal)
		{
			return true;
		}

		// 让抢杠胡通过
		{
			// 前一个操作是其他玩家补杠
			Step lastStep = context.getLastRecordStep(0);
			if (lastStep.getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD)
				return true;
		}

		// 让地胡通过
		{
			if (context.getRecords().size() == 2)
				return true;
		}

		// 其他吃胡的就不通过了
		return false;
	}

}
