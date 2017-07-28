package com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 
 * <一句话功能简述> 潮州麻将计算混幺九（是混一色和幺九胡的组合） <功能详细描述> 判断数字牌是否为1和9，并且是否为同一花色
 * 
 * @author 姓名 毛建伟
 * @version [版本号, 2017年1月5日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class HuSubScoreChecker_HunYaoJiu_ChaoShan extends HuSubScoreChecker
{

	public HuSubScoreChecker_HunYaoJiu_ChaoShan()
	{
		super(PlayType.HU_HUN_YAO_JIU_CHAOZHOU);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		CardType cardType = CardType.INVALID;

		// 是否有字牌
		boolean hasZi = false;

		// 是否有数字牌
		boolean hasNum = false;
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			// 混幺九必须是刻杠和将的组合
			if (CardGroupType.isGang(cardGroup.getGroupType()) || CardGroupType.isJiang(cardGroup.getGroupType()) || CardGroupType.isKe(cardGroup.getGroupType()))
			{
				byte card = cardGroup.getCardList().get(0);
				// 这张牌不是数字牌，进入下一循环
				if (CardType.isNumberCard(card) == false)
				{
					hasZi = true;
					continue;
				}

				hasNum = true;

				// 判断序数是否为0或8（分别对应1和9）
				int index = CardType.convertToCardIndex(card);
				if (index != 0 && index != 8)
					return false;

				// 数字牌是否为同一种花色
				if (cardType == CardType.INVALID)
					cardType = CardType.getCardType(card);
				else if (cardType != CardType.getCardType(card))
					return false;
			}
			else
				return false;
		}

		// 有字并且有数字牌加分
		if (hasNum && hasZi)
		{
			addScore(inoutHuContext.scoreData);
			return true;
		}

		return false;
	}

}
