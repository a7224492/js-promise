package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:杠上花
 */
public class HuSubScoreChecker_GangShangHua extends HuSubScoreChecker
{
	public HuSubScoreChecker_GangShangHua()
	{
		super(PlayType.HU_GANG_SHANG_HUA);
	}

	@Override
	public boolean skipCheckTing()
	{
		return false;
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 杠上花肯定是自摸
		if (CheckHelper.checkHu_ZiMo(context, roleId) == false)
			return false;

		// 检测前一个操作是这个玩家的杠
		Step lastLastStep = context.getLastRecordStep(1);
		if (lastLastStep == null || lastLastStep.getRoleId() != roleId || PlayType.isGangOperator(lastLastStep.getPlayType()) == false)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}