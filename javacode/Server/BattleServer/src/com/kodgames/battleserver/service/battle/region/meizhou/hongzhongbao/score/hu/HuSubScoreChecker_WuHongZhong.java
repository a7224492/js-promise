package com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 牌型中没有红中，则胡牌分+1分
 */
public class HuSubScoreChecker_WuHongZhong extends HuSubScoreChecker
{

	public HuSubScoreChecker_WuHongZhong()
	{
		super(PlayType.HU_WU_HONG_ZHONG);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 计算真实牌值数组
		PlayerInfo playerInfo = context.getPlayers().get(roleId);
		byte[] cardCountArray = CheckHelper.converToCardCountArray(playerInfo.getCards());

		// 得到胡牌牌型中红中牌的张数
		int hongZhongCardCount = cardCountArray[CardType.JIAN.Value()];

		if (hongZhongCardCount > 0)
		{
			return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
