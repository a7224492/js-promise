package com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.operation.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

/**
 * 广东推到胡的必须自摸的OperationResultFilter
 */
public class OperationResultFilter_ZiMo_TuiDaoHu extends OperationResultFilter
{

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 判断是否为平胡或者抢杠胡（抢杠胡如果不判断会在可以抢杠胡的时候过滤掉）
		Step lastStep = context.getLastRecordStep(0);
		if (!phaseDeal && lastStep.getPlayType() != PlayType.OPERATE_BU_GANG_A_CARD)
			return false;

		return true;
	}

}
