package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:金钩
 * 
 * 和牌时，全部的牌都用作碰或者杠，手中只剩下唯一的一张牌 . (全求人&单钓将)
 */
public class HuSubScoreChecker_JinGou extends HuSubScoreChecker
{
	public HuSubScoreChecker_JinGou()
	{
		super(PlayType.HU_JIN_GOU);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		int totalCardCount = 0;
		for (byte cardCount : inoutHuContext.cardCountList)
			totalCardCount += cardCount;

		// 手牌中只能有将
		if (totalCardCount != 2)
			return false;

		// 其他成台的牌形为刻或者杠
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
			if (!(CardGroupType.isKe(cardGroup.getGroupType()) || CardGroupType.isGang(cardGroup.getGroupType()) || CardGroupType.isJiang(cardGroup.getGroupType())))
				return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}