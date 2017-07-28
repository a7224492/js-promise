package com.kodgames.battleserver.service.battle.core.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;

/**
 * 是否为最后一个操作的执行者
 * 
 * (用于一般胡)
 */
public class ScoreSourceFilter_LastOperator extends ScoreSourceFilter
{
	public boolean filter(BattleBean context, PlayerInfo player)
	{
		if (player == null)
			return false;

		Step lastStep = context.getLastRecordStep(0);
		if (lastStep == null)
			return false;

		return lastStep.getRoleId() == player.getRoleId();
	}
}