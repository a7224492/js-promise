package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:门前清(门清)
 */
public class HuSubScoreChecker_MenQianQing extends HuSubScoreChecker
{
	public HuSubScoreChecker_MenQianQing()
	{
		super(PlayType.HU_MEN_QIAN_QING);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		if (CheckHelper.check_MenQianQing(inoutHuContext.scoreData.getCardGroups()) == false)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}