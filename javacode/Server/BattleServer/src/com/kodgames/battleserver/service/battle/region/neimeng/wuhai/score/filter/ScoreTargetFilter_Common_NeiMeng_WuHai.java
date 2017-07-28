package com.kodgames.battleserver.service.battle.region.neimeng.wuhai.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;

public class ScoreTargetFilter_Common_NeiMeng_WuHai extends ScoreTargetFilter
{
	@Override
	public boolean filter(BattleBean context, PlayerInfo player, PlayerInfo sourcePlayer, PlayerInfo checkingPlayer)
	{
		// 自己不收自己分
		if (player.getRoleId() == checkingPlayer.getRoleId())
			return false;

		return true;
	}
}
