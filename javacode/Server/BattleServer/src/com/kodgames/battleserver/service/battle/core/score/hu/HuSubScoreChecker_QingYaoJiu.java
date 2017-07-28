package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌型：清幺九
 * 
 * 由序数牌一、九刻子组合成的和牌。不计碰碰胡（对对胡）。
 */
public class HuSubScoreChecker_QingYaoJiu extends HuSubScoreChecker
{

	public HuSubScoreChecker_QingYaoJiu()
	{
		super(PlayType.HU_QING_YAO_JIU);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			// 判断是否为刻，杠，或者将
			if (CardGroupType.isGang(cardGroup.getGroupType()) || CardGroupType.isJiang(cardGroup.getGroupType()) || CardGroupType.isKe(cardGroup.getGroupType()))
			{
				byte card = cardGroup.getCardList().get(0);
				
				// 清幺九没有字牌
				if (CardType.isNumberCard(card) == false)
					return false;

				// 设置shuFlag并且判断是否序数为幺九
				if (CardType.convertToCardIndex(card) != 0 && CardType.convertToCardIndex(card) != 8)
					return false;
			}
			else
				return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
