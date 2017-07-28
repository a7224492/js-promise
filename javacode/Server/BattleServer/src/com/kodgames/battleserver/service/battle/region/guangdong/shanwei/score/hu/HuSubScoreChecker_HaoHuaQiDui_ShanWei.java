package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 汕尾检测牌型：豪华七对
 * 
 * 7小对中，有多个4张牌一样的
 */
public class HuSubScoreChecker_HaoHuaQiDui_ShanWei extends HuSubScoreChecker
{
	public HuSubScoreChecker_HaoHuaQiDui_ShanWei()
	{
		super(PlayType.HU_HAO_HUA_QI_DUI);
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

		// 统计4张牌一样的个数
		int count = 0;

		for (byte cardCount : inoutHuContext.allCardCountList)
			if (cardCount == 4)
				count++;

		// 豪华七对中可以有多个四张做两对
		if (count < 1)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
