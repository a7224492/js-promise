package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:天和
 */
public class HuSubScoreChecker_TianHu extends HuSubScoreChecker
{
	public HuSubScoreChecker_TianHu()
	{
		super(PlayType.HU_TIAN_HU);
	}

	@Override
	public boolean skipCheckTing()
	{
		return true;
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 必须是庄
		if (context.getZhuang() != roleId)
			return false;

		// 只进行过一次摸牌, 庄家第一张也是摸牌
		if (context.getPlayers().get(roleId).getCards().getRecordIndices().size() > 1)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}
