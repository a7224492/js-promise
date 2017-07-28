package com.kodgames.battleserver.service.battle.core.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * TingCard 只会的附加操作
 */
public class AfterOperationProcessor_TingCard extends AfterOperationProcessorBase
{
	@Override
	public List<Step> process(ControllerManager controller, Step prevStep)
	{
		List<Step> results = new ArrayList<>();

		// 上听之后需要让开启客户端蒙灰状态 并自动打牌
		results.add(new Step(prevStep.getRoleId(), PlayType.DISPLAY_TING));
		results.add(new Step(prevStep.getRoleId(), PlayType.DISPLAY_MASK_ALL_HAND_CARD));
		results.add(new Step(prevStep.getRoleId(), PlayType.DISPLAY_AUTO_PLAY_LAST_DEALED_CARD));

		return results;
	}
}
