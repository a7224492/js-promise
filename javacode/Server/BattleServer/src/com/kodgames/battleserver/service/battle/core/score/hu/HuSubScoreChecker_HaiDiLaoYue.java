package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:海底捞月(海底胡)
 */
public class HuSubScoreChecker_HaiDiLaoYue extends HuSubScoreChecker
{
	public HuSubScoreChecker_HaiDiLaoYue()
	{
		super(PlayType.HU_HAI_DI_LAO_YUE);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 当前最后一张
		if (context.getCardPool().getCards().size() > context.getCardPool().getStayCount())
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}