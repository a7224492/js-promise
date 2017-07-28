package com.kodgames.battleserver.service.battle.core.score.hu;

import java.util.Arrays;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:全带幺(带幺九)
 */
public class HuSubScoreChecker_QuanDaiYao extends HuSubScoreChecker
{
	public HuSubScoreChecker_QuanDaiYao()
	{
		super(PlayType.HU_QUAN_DAI_YAO);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		for (CardGroup group : inoutHuContext.scoreData.getCardGroups())
		{
			boolean hasYaoJiu = false;
			for (byte c : group.getCardList())
			{
				if (Arrays.binarySearch(CheckHelper.CARD_YAO, c) < 0)
					continue;

				hasYaoJiu = true;
			}

			if (hasYaoJiu == false)
				return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}
}