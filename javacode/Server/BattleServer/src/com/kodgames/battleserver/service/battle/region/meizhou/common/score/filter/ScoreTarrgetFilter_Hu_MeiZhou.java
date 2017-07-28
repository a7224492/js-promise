package com.kodgames.battleserver.service.battle.region.meizhou.common.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;

/**
 * 自摸和地胡吃胡收3家分，抢杠胡收1家分的3倍。
 */
public class ScoreTarrgetFilter_Hu_MeiZhou extends ScoreTargetFilter
{

	@Override
	public boolean filter(BattleBean context, PlayerInfo player, PlayerInfo sourcePlayer, PlayerInfo checkingPlayer)
	{
		// 自己不收自己分
		if (player.getRoleId() == checkingPlayer.getRoleId())
			return false;

		// 点炮收一家
		boolean isDianPaoHu = player.getRoleId() != sourcePlayer.getRoleId();

		// 地胡收三家
		boolean isDiHu = context.getRecords().size() == 2;

		// 点炮，且不是地胡，收点炮人的分，否则收三家
		if (isDianPaoHu && !isDiHu)
		{
			// 点炮
			return sourcePlayer.getRoleId() == checkingPlayer.getRoleId();
		}

		return true;
	}

}
