package com.kodgames.battleserver.service.battle.core.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 基础规则
 * 
 * 排除自己, 排除已经胡牌者, 如果是点炮, 只算点炮这, 如果是自摸, 所有人都算
 */
public class ScoreTargetFilter_Common extends ScoreTargetFilter
{
	@Override
	public boolean filter(BattleBean context, PlayerInfo player, PlayerInfo sourcePlayer, PlayerInfo checkingPlayer)
	{
		// 自己不收自己分
		if (player.getRoleId() == checkingPlayer.getRoleId())
			return false;

		if (player.getRoleId() == sourcePlayer.getRoleId())
		{
			// 自摸排除胡牌者
			for (Step step : checkingPlayer.getCards().getCardHeap())
			{
				if (step.getPlayType() == PlayType.OPERATE_HU)
					return false;
			}

			return true;
		}
		else
		{
			// 点炮
			return sourcePlayer.getRoleId() == checkingPlayer.getRoleId();
		}
	}
}
