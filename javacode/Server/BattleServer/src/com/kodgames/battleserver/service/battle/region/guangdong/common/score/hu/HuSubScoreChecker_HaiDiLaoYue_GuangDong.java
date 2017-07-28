package com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌形:海底捞月(海底胡)
 */
public class HuSubScoreChecker_HaiDiLaoYue_GuangDong extends HuSubScoreChecker
{
	public HuSubScoreChecker_HaiDiLaoYue_GuangDong()
	{
		super(PlayType.HU_HAI_DI_LAO_YUE);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 当前最后一张并且海底捞月只能自摸
		if (context.getCardPool().getCards().size() > context.getCardPool().getStayCount() || inoutHuContext.scoreData.getSourceId() != roleId)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}