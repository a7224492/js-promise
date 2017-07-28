package com.kodgames.battleserver.service.battle.region.meizhou.huizhou.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.operation.OperationCheckerFilter;

/**
 * 惠州自动胡牌
 */
public class OperationAutoFilter_Hu8Hua extends OperationCheckerFilter
{
	@Override
	public void filter(ControllerManager controller, int roleId, List<Step> result)
	{
		BattleBean context = controller.getBattleBean();

		// 得到花牌数量
		List<Byte> huaCards = new ArrayList<>();

		// 是否为可胡状态
		boolean hasHuType = false;

		// 是否为8花可胡
		boolean has8Hua = false;
		for (Step step : result)
		{
			if (step.getPlayType() == PlayType.OPERATE_CAN_HU)
			{
				hasHuType = true;
				huaCards = context.getPlayerById(step.getRoleId()).getCards().getExCards();
				if (huaCards.size() == 8)
					has8Hua = true;
			}

		}

		if (!hasHuType || !has8Hua)
			return;

		// 删除不是可以胡牌的类型
		result.removeIf(step -> {
			return step.getPlayType() != PlayType.OPERATE_CAN_HU;
		});

		// 修改可以胡牌为可以自动胡牌
		result.forEach(step -> {
			step.setPlayType(PlayType.OPERATE_CAN_AUTO_HU);
		});
	}
}
