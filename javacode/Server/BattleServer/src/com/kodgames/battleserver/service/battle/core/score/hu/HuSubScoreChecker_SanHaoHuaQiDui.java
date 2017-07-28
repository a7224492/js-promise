package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌型：三豪华七小队
 * 
 * 7小对中，有3个4张牌一样
 */
public class HuSubScoreChecker_SanHaoHuaQiDui extends HuSubScoreChecker
{

	public HuSubScoreChecker_SanHaoHuaQiDui()
	{
		super(PlayType.HU_SAN_HAO_HUA_QI_DUI);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 检测基本牌形是否有七对胡
		boolean has7DuiHu = false;
		for (ScorePoint scorePoint : inoutHuContext.scoreData.getPoints())
			if (scorePoint.getScoreType() == PlayType.HU_QI_DUI)
			{
				has7DuiHu = true;
				break;
			}

		if (has7DuiHu == false)
			return false;

		// 有4张牌一样就认为是豪华七对
		int count = 0;
		for (byte cardCount : inoutHuContext.allCardCountList)
			if (cardCount == 4)
				count++;

		// 三豪华七对只能有3个4张的
		if (count != 3)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
