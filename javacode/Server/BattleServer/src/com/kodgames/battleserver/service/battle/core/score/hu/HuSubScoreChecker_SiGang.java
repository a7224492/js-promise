package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌型：四杠
 * 
 * 和牌中，有四副杠牌（暗杠加计）。也叫十八罗汉
 */
public class HuSubScoreChecker_SiGang extends HuSubScoreChecker
{

	public HuSubScoreChecker_SiGang()
	{
		super(PlayType.HU_SI_GANG);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		int count = 0;
		// 在CardGroup中找出杠的个数，应该可以判断cardGroup中是否只有杠和将
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
			if (CardGroupType.isGang(cardGroup.getGroupType()))
				count++;

		if (count < 4)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
