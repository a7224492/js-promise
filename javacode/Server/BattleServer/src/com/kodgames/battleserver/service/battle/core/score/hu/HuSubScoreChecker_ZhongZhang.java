package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:中张
 */
public class HuSubScoreChecker_ZhongZhang extends HuSubScoreChecker
{
	public HuSubScoreChecker_ZhongZhang()
	{
		super(PlayType.HU_ZHONG_ZHANG);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		for (byte i = 0; i < inoutHuContext.allCardCountList.length; i++)
		{
			if (inoutHuContext.allCardCountList[i] == 0)
			{
				continue;
			}
			if (CardType.isNumberCard(i) == false)
				return false;

			int cardIndex = CardType.convertToCardIndex(i);
			if (cardIndex == 0 || cardIndex == 8)
				return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}
}