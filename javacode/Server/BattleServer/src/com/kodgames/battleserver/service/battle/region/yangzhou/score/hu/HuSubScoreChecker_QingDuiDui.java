package com.kodgames.battleserver.service.battle.region.yangzhou.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌形:清对对（清一色+对对胡）
 * 清一色：条，饼，万其中14张牌为一种颜色糊牌
 * 对对胡：由4副刻子+将牌组成的胡牌
 */
public class HuSubScoreChecker_QingDuiDui extends HuSubScoreChecker
{
	public HuSubScoreChecker_QingDuiDui() 
	{
		super(PlayType.HU_QING_DUI_DUI);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext) 
	{
		// 如果cardGroupList为0, 表示不是胡牌牌形
		if (inoutHuContext.scoreData.getCardGroups().size() == 0)
			return false;

		//1.先判断是不是清一色
		CardType cardType = CardType.INVALID;
		for (int card = 0; card < inoutHuContext.allCardCountList.length; ++card)
		{
			if (inoutHuContext.allCardCountList[card] == 0)
				continue;

			//非条、筒、万，不为清一色
			if (CardType.isNumberCard((byte)card) == false)
				return false;

			if (cardType == CardType.INVALID)
				cardType = CardType.getCardType((byte)card);
			else if (cardType != CardType.getCardType((byte)card))
				return false;
		}
		
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
