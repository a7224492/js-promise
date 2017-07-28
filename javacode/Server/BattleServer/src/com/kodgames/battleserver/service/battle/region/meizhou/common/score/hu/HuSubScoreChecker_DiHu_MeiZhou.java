package com.kodgames.battleserver.service.battle.region.meizhou.common.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

public class HuSubScoreChecker_DiHu_MeiZhou extends HuSubScoreChecker
{
	public HuSubScoreChecker_DiHu_MeiZhou()
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

		// 牌局只能有两步操作：庄家摸牌、庄家打牌
		if (context.getRecords().size() != 2)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}
