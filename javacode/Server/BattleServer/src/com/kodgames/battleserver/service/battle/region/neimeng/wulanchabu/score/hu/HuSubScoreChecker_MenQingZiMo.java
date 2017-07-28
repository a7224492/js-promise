package com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 门清自摸
 */
public class HuSubScoreChecker_MenQingZiMo extends HuSubScoreChecker
{

	public HuSubScoreChecker_MenQingZiMo()
	{
		super(PlayType.HU_MEN_QING_ZI_MO);

	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		if ((CheckHelper.check_MenQianQing(inoutHuContext.scoreData.getCardGroups()) && inoutHuContext.scoreData.getSourceId() == roleId) == false)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}
