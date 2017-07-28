package com.kodgames.battleserver.service.battle.region.guangdong.shantou.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;

/**
 * 汕头检查是否为吃杠杠爆全包
 * @author kod
 *
 */
public class ScoreTargetFilter_GangBaoQuanBao_ShanTou extends ScoreTargetFilter
{

	@Override
	public boolean filter(BattleBean context, PlayerInfo player, PlayerInfo sourcePlayer, PlayerInfo checkingPlayer)
	{
		// 上一步是否为杠
		Step lastStep = context.getLastRecordStep(1);
		if (lastStep == null || lastStep.getRoleId() != player.getRoleId() || lastStep.getPlayType() != PlayType.OPERATE_GANG_A_CARD)
			return true;
		
		// 点杠的玩家是否为检测的玩家
		if (checkingPlayer.getRoleId() == context.getLastRecordStep(2).getRoleId())
			return true;
		
		return false;
	}

}
