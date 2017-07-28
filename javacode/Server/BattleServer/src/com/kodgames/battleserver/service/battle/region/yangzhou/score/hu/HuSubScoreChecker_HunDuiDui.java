package com.kodgames.battleserver.service.battle.region.yangzhou.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌形:混对对（混一色+对对胡）
 * 混一色：除了风头只为一种颜色糊，东西南北中发白都算风头
 * 对对胡：由4副刻子+将牌组成的胡牌
 */
public class HuSubScoreChecker_HunDuiDui extends HuSubScoreChecker
{
	public HuSubScoreChecker_HunDuiDui()
	{
		super(PlayType.HU_HUN_DUI_DUI);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 如果cardGroupList为0, 表示不是胡牌牌形
		if (inoutHuContext.scoreData.getCardGroups().size() == 0)
			return false;

		//1.先判断是不是混一色
		// 至少需要一副字牌才成混一色，否则就是清一色
		boolean ziFlag = false;
		CardType cardType = CardType.INVALID;
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			byte card = cardGroup.getCardList().get(0);
			
			//判断是否有字牌
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
		
		//2.再判断是不是对对胡
		boolean hasJiang = false;
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			// 是不是有将，对对胡只能有一个将
			if (CardGroupType.isJiang(cardGroup.getGroupType()))
			{
				if (hasJiang)
					return false;
				else
					hasJiang = true;
			}

			// 只有将和刻/杠
			if (CardGroupType.isJiang(cardGroup.getGroupType()) == false 
					&& CardGroupType.isKe(cardGroup.getGroupType()) == false
					&& CardGroupType.isGang(cardGroup.getGroupType()) == false)
			{
				return false;
			}				
		}

		addScore(inoutHuContext.scoreData);

		return true;
	}
}
