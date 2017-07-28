package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形：点炮胡（吃胡）
 * 
 * 不是自摸的胡牌，即为点炮胡
 */
public class HuSubScoreChecker_DianPaoHu extends HuSubScoreChecker
{
	public HuSubScoreChecker_DianPaoHu()
	{
		super(PlayType.HU_DIAN_PAO);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 不能用判断上一个操作是否是自己的操作来判断, 点杠花(点炮)会转移分数来源
		if (inoutHuContext.scoreData.getSourceId() == roleId)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}