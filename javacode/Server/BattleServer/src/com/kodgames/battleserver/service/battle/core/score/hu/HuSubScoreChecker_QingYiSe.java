package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:清一色
 * 
 * 一种花色的序数牌组成的和牌
 */
public class HuSubScoreChecker_QingYiSe extends HuSubScoreChecker
{
	public HuSubScoreChecker_QingYiSe()
	{
		super(PlayType.HU_QING_YI_SE);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		CardType cardType = CardType.INVALID;
		for (int card = 0; card < inoutHuContext.allCardCountList.length; ++card)
		{
			if (inoutHuContext.allCardCountList[card] == 0)
				continue;

			if (CardType.isNumberCard((byte)card) == false)
				return false;

			if (cardType == CardType.INVALID)
				cardType = CardType.getCardType((byte)card);
			else if (cardType != CardType.getCardType((byte)card))
				return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}
}