package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌型：字一色
 * 
 * 由字牌组成的刻（杠）、将牌组成的和牌
 */
public class HuSubScoreChecker_ZiYiSe extends HuSubScoreChecker
{

	public HuSubScoreChecker_ZiYiSe()
	{
		super(PlayType.HU_ZI_YI_SE);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 所有的牌是否为字牌
		for (byte card = 0; card < inoutHuContext.allCardCountList.length; card++)
		{
			if (inoutHuContext.allCardCountList[card] == 0)
				continue;
			
			if (CardType.ZI.isBelongTo(card) == false)
				return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
