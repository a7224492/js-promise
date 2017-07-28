package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:碰碰和
 * 
 * 由4副刻子（或杠）、将牌组成的和牌
 */
public class HuSubScoreChecker_PengPengHu extends HuSubScoreChecker
{
	public HuSubScoreChecker_PengPengHu()
	{
		super(PlayType.HU_PENG_PENG_HU);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 如果cardGroupList为0, 表示不是平胡牌形
		if (inoutHuContext.scoreData.getCardGroups().size() == 0)
			return false;

		boolean hasJiang = false;
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			// 是不是有将，碰碰胡只能有一个将
			if (CardGroupType.isJiang(cardGroup.getGroupType()))
				if (hasJiang)
					return false;
				else
					hasJiang = true;
				
			if (cardGroup.getGroupType() != CardGroupType.JIANG && CardGroupType.isKe(cardGroup.getGroupType()) == false && CardGroupType.isGang(cardGroup.getGroupType()) == false)
				return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}
}