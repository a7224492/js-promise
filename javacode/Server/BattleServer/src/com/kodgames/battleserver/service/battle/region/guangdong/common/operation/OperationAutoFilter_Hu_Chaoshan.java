package com.kodgames.battleserver.service.battle.region.guangdong.common.operation;

import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.operation.OperationCheckerFilter;

/**
 * 潮汕潮州地区修改可胡为自动胡
 * 
 * @author 毛建伟
 */
public class OperationAutoFilter_Hu_Chaoshan extends OperationCheckerFilter
{
	@Override
	public void filter(ControllerManager controller, int roleId, List<Step> result)
	{
		// 判断是否有可胡操作
		boolean hasHuType = false;

		for (Step step : result)
		{
			if (step.getPlayType() == PlayType.OPERATE_CAN_HU)
				hasHuType = true;
		}
		// 没有可胡的step
		if (hasHuType == false)
			return;
		// 修改可以胡牌为可以自动胡牌并删除掉其他操作
		result.removeIf(step -> {
			if (step.getPlayType() == PlayType.OPERATE_CAN_HU)
			{
				step.setPlayType(PlayType.OPERATE_CAN_AUTO_HU);
				return false;
			}
			return true;
		});
	}

}
