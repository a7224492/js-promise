package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:地和
 */
public class HuSubScoreChecker_DiHu extends HuSubScoreChecker
{
	public HuSubScoreChecker_DiHu()
	{
		super(PlayType.HU_DI_HU);
	}

	@Override
	public boolean skipCheckTing()
	{
		return true;
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 必须不是庄
		if (context.getZhuang() == roleId)
			return false;

		// 只进行过一次摸牌
		if (context.getPlayers().get(roleId).getCards().getRecordIndices().size() > 1)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}