package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:自摸
 * 
 * 和牌时，全部的牌都用作碰或者杠，手中只剩下唯一的一张牌 . (全求人&单钓将)
 */
public class HuSubScoreChecker_ZiMo extends HuSubScoreChecker
{
	public HuSubScoreChecker_ZiMo()
	{
		super(PlayType.HU_ZI_MO);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 不能用判断上一个操作是否是自己的操作来判断, 点杠花(点炮)会转移分数来源
		if (inoutHuContext.scoreData.getSourceId() != roleId)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}