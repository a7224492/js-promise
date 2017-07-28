package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:幺九胡
 * 
 * 由序数牌1、9和字牌的刻子、将牌组成的和牌，不计碰碰胡、全带幺、幺九刻。番数为32番。
 */
public class HuSubScoreChecker_HunYaoJiu extends HuSubScoreChecker
{

	public HuSubScoreChecker_HunYaoJiu()
	{
		super(PlayType.HU_HUN_YAO_JIU);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 是否有字牌
		boolean ziFlag = false;
		// 是否有数牌
		boolean shuFlag = false;

		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			// 判断是否为刻，杠，或者将
			if (CardGroupType.isGang(cardGroup.getGroupType()) || CardGroupType.isJiang(cardGroup.getGroupType()) || CardGroupType.isKe(cardGroup.getGroupType()))
			{
				byte card = cardGroup.getCardList().get(0);

				// 有字牌设置ziFlag为true
				if (CardType.ZI.isBelongTo(card))
					ziFlag = true;

				// 设置shuFlag并且判断是否序数为幺九
				if (CardType.isNumberCard(card))
				{
					if (CardType.convertToCardIndex(card) != 0 && CardType.convertToCardIndex(card) != 8)
						return false;
					shuFlag = true;
				}
			}
			else
				return false;
		}

		// 必须同时有字和数才算正确
		if (ziFlag == false || shuFlag == false)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
