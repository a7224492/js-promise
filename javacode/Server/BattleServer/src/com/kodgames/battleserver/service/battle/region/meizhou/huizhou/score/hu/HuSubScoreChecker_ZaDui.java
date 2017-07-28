package com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 惠州杂对：混一色+碰碰胡
 */
public class HuSubScoreChecker_ZaDui extends HuSubScoreChecker
{
	public HuSubScoreChecker_ZaDui()
	{
		super(PlayType.HU_ZA_DUI);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 是否符合碰碰胡牌型
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

		// 是否符合混一色牌型
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
