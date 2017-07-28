package com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌型：庄家
 * 
 * 坐庄玩家胡牌。
 */
public class HuSubScoreChecker_ZhuangJia extends HuSubScoreChecker
{

	public HuSubScoreChecker_ZhuangJia()
	{
		super(PlayType.HU_ZHUANG_JIA);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		if (roleId != context.getZhuang())
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
