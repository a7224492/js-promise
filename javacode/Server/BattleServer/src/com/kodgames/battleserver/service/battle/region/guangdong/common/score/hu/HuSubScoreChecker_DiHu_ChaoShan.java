package com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 潮州地胡，不准许吃碰杠
 */
public class HuSubScoreChecker_DiHu_ChaoShan extends HuSubScoreChecker
{

	public HuSubScoreChecker_DiHu_ChaoShan()
	{
		super(PlayType.HU_DI_HU);
	}
	
	/**
	 * 在检测ting牌的情况是否要忽略这个分数
	 */
	public boolean skipCheckTing()
	{
		return true;
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 没有吃碰杠操作
		for (Step step : context.getRecords())
			if (PlayType.isChiPengGang(step.getPlayType()))
				return false;

		// 必须不是庄并且只进行过一次摸牌
		if (context.getZhuang() == roleId || context.getPlayers().get(roleId).getCards().getRecordIndices().size() > 1)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
