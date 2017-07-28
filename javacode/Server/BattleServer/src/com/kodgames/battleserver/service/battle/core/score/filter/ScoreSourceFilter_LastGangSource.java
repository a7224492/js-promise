package com.kodgames.battleserver.service.battle.core.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 是否最后一个操作是杠,同时为点杠者,
 * 
 * (用于点杠花(点炮)：杠上花收分改为点杠者，不要轻易使用)
 */
public class ScoreSourceFilter_LastGangSource extends ScoreSourceFilter
{
	public boolean filter(BattleBean context, PlayerInfo player)
	{
		if (player == null)
			return false;

		Step lastStep = context.getLastRecordStep(1);
		if (lastStep == null || lastStep.getPlayType() != PlayType.OPERATE_GANG_A_CARD)
			return false;

		// 杠之前的操作一定是PlayType.OPERATE_PLAY_A_CARD
		Step lastlastStep = context.getLastRecordStep(2);
		if (lastlastStep == null)
			return false;

		return lastlastStep.getRoleId() == player.getRoleId();
	}
}