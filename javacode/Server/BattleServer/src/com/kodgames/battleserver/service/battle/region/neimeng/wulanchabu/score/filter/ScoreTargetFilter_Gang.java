package com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;

/**
 * 杠分收三家
 */
public class ScoreTargetFilter_Gang extends ScoreTargetFilter
{
	@Override
	public boolean filter(BattleBean context, PlayerInfo player, PlayerInfo sourcePlayer, PlayerInfo checkingPlayer)
	{
		return player.getRoleId() != checkingPlayer.getRoleId();
	}
}
