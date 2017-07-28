package com.kodgames.battleserver.service.battle.region.neimeng.common.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.operation.AfterOperationProcessorBase;

/**
 * 碰牌上听玩法中，如果明杠之后可以上听，出牌之后, 强制进入上听状态
 */
public class AfterOperationProcessor_GangEnterTing extends AfterOperationProcessorBase
{

	@Override
	public List<Step> process(ControllerManager controller, Step prevStep)
	{
		List<Step> ret = new ArrayList<>();

		BattleBean battle = controller.getBattleBean();
		Macro.AssetTrue(null == battle);
		if (battle.getRecords().size() < 3)
			return ret;

		// 倒数第一步应该是打牌
		int roleId = prevStep.getRoleId();
		Step lastFirst = battle.getRecords().get(battle.getRecords().size() - 1);
		if (lastFirst.getRoleId() != roleId || lastFirst.getPlayType() != this.getPlayType())
			return ret;

		// 倒数第二步应该是摸牌
		Step lastSecond = battle.getRecords().get(battle.getRecords().size() - 2);
		if (lastSecond.getRoleId() != roleId || lastSecond.getPlayType() != PlayType.OPERATE_DEAL)
			return ret;

		// 倒数第三步应该是明杠
		Step lastThird = battle.getRecords().get(battle.getRecords().size() - 3);
		int lastThirdType = lastThird.getPlayType();
		if (lastThird.getRoleId() != roleId || (lastThirdType != PlayType.OPERATE_GANG_A_CARD && lastThirdType != PlayType.OPERATE_BU_GANG_A_CARD))
			return ret;

		// 添加听牌标记
		Step step = new Step(prevStep.getRoleId(), PlayType.DISPLAY_TING);
		ret.add(step);

		// 上听之后需要让开启客户端蒙灰状态 并自动打牌
		ret.add(new Step(prevStep.getRoleId(), PlayType.DISPLAY_MASK_ALL_HAND_CARD));
		ret.add(new Step(prevStep.getRoleId(), PlayType.DISPLAY_AUTO_PLAY_LAST_DEALED_CARD));

		return ret;
	}

}
