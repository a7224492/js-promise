package com.kodgames.battleserver.service.battle.core.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 添加CanPass操作
 */
public class OperationChecker_Pass extends OperationCheckerBase
{
	@Override
	public void check(BattleBean context, List<Step> inoutResult, int roleId, byte card, boolean phaseDeal)
	{
		// 玩家没有可以进行的操作，跳过
		if (inoutResult.size() <= 0)
			return;

		super.check(context, inoutResult, roleId, card, phaseDeal);
	}

	@Override
	public List<Step> doCheck(BattleBean context, int roleId, byte card, boolean phaseDeal)
	{
		// 直接添加可以过的操作
		List<Step> result = new ArrayList<>();
		result.add(new Step(roleId, PlayType.OPERATE_CAN_PASS));
		return result;
	}
}
