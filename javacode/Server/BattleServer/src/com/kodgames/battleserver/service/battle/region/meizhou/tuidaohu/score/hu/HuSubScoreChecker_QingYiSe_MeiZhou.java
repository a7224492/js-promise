package com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌形:清一色
 * 
 * 一种花色的序数牌组成的和牌
 */
public class HuSubScoreChecker_QingYiSe_MeiZhou extends HuSubScoreChecker
{
	public HuSubScoreChecker_QingYiSe_MeiZhou()
	{
		super(PlayType.HU_QING_YI_SE);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		CardType cardType = CardType.INVALID;
		// 统计次数，确定是否为五组
		int count = 0;

		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			// 获取这组的第一张，成组了就代表这组牌都是同一花色
			byte card = cardGroup.getCardList().get(0);
			// 不是万条筒不对
			if (CardType.isNumberCard((byte)card) == false)
				return false;

			// 计数+1
			++count;

			// 判断是否为同一个花色
			if (cardType == CardType.INVALID)
				cardType = CardType.getCardType((byte)card);
			else if (cardType != CardType.getCardType((byte)card))
				return false;
		}

		// 平胡只有5组cardGroup，多于5个就是错误的（防止七对）
		if (count > 5)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}
