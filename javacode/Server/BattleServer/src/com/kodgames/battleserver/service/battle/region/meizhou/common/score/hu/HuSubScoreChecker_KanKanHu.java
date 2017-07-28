package com.kodgames.battleserver.service.battle.region.meizhou.common.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 坎坎胡：手里有4个暗刻,一个将
 */
public class HuSubScoreChecker_KanKanHu extends HuSubScoreChecker
{

	public HuSubScoreChecker_KanKanHu()
	{
		super(PlayType.HU_KAN_KAN_HU);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 如果cardGroupList为0, 表示不是胡牌牌形
		if (inoutHuContext.scoreData.getCardGroups().size() == 0)
			return false;

		boolean hasJiang = false;
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			// 是不是有将，坎坎胡只能有一个将
			if (CardGroupType.isJiang(cardGroup.getGroupType()))
				if (hasJiang)
					return false;
				else
					hasJiang = true;

			// 只有将和暗刻
			if (CardGroupType.isJiang(cardGroup.getGroupType()) == false && cardGroup.getGroupType() != CardGroupType.AN_KE)
				return false;
		}

		addScore(inoutHuContext.scoreData);

		return true;
	}

}
