package com.kodgames.battleserver.service.battle.core.score.hu;

import java.util.Arrays;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:将对
 * 
 * 玩家手牌全为2,5,8的碰碰胡
 */
public class HuSubScoreChecker_JiangDui extends HuSubScoreChecker
{
	public HuSubScoreChecker_JiangDui()
	{
		super(PlayType.HU_JIANG_DUI);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			// 碰碰胡
			if (cardGroup.getGroupType() != CardGroupType.JIANG && CardGroupType.isKe(cardGroup.getGroupType()) == false && CardGroupType.isGang(cardGroup.getGroupType()) == false)
				return false;

			// 都为258
			for (byte card : cardGroup.getCardList())
				if (Arrays.binarySearch(CheckHelper.CARD_258_JIANG, card) < 0)
					return false;
		}

		for (byte i = 0; i < inoutHuContext.cardCountList.length; i++)
		{
			if (inoutHuContext.cardCountList[i] <= 0)
				continue;

			if (Arrays.binarySearch(CheckHelper.CARD_258_JIANG, i) < 0)
				return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}
}