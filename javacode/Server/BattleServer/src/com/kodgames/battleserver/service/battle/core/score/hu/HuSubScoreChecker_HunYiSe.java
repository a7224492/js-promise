package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测是否为混一色，
 * 
 * 混一色是手牌只有字牌加上万，条，饼中的一种牌。
 */
public class HuSubScoreChecker_HunYiSe extends HuSubScoreChecker
{
	public HuSubScoreChecker_HunYiSe()
	{
		super(PlayType.HU_HUN_YI_SE);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		CardType cardType = CardType.INVALID;
		// 至少需要一副字牌才成混一色，否则就是清一色
		boolean ziFlag = false;
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			byte card = cardGroup.getCardList().get(0);
			if (CardType.ZI.isBelongTo(card))
			{
				ziFlag = true;
				continue;
			}

			// 初始化cardType并判断是否同一个颜色
			if (cardType == CardType.INVALID)
				cardType = CardType.getCardType(card);
			else if (cardType != CardType.getCardType(card))
				return false;
		}

		// 判断是否有字牌,是否有万条筒牌
		if (ziFlag == false || cardType == CardType.INVALID)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
