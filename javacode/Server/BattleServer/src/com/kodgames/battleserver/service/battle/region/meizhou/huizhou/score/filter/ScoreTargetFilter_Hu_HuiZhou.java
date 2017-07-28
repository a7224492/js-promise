package com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;

/**
 * 自摸收3家分，抢杠胡收1家分的3倍，吃胡收1家分。
 */
public class ScoreTargetFilter_Hu_HuiZhou extends ScoreTargetFilter
{
	@Override
	public boolean filter(BattleBean context, PlayerInfo player, PlayerInfo sourcePlayer, PlayerInfo checkingPlayer)
	{
		// 自己不收自己分
		if (player.getRoleId() == checkingPlayer.getRoleId())
			return false;

		// 点炮收一家
		boolean isDianPaoHu = player.getRoleId() != sourcePlayer.getRoleId();

		// 点炮，收点炮人的分，否则收三家
		if (isDianPaoHu)
		{
			// 点炮
			return sourcePlayer.getRoleId() == checkingPlayer.getRoleId();
		}

		return true;
	}
}
