package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 大哥：清一色+对对胡
 */
public class HuSubScoreChecker_DaGe extends HuSubScoreChecker
{

	public HuSubScoreChecker_DaGe()
	{
		super(PlayType.HU_DA_GE);
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

		// 是否符合清一色牌型
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
