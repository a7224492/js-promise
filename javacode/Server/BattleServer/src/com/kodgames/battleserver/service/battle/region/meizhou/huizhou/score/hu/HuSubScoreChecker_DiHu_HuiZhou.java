package com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 惠州地胡
 */
public class HuSubScoreChecker_DiHu_HuiZhou extends HuSubScoreChecker
{
	public HuSubScoreChecker_DiHu_HuiZhou()
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
		boolean isDianHu = false;
		boolean isZiMo = false;
		// 必须不是庄
		if (context.getZhuang() == roleId)
			return false;

		// 牌局只能有两步操作：庄家摸牌、庄家打牌,就是地胡吃胡
		if (context.getRecords().size() == 2)
			isDianHu = true;

		// 这个是地胡自摸
		if (context.getPlayers().get(roleId).getCards().getRecordIndices().size() == 1)
			isZiMo = true;

		if (!isZiMo && !isDianHu)
		{
			return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}
}
