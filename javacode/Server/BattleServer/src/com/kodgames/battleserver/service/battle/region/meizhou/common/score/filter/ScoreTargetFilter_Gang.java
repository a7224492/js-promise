package com.kodgames.battleserver.service.battle.region.meizhou.common.score.filter;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;
import com.kodgames.battleserver.service.battle.region.meizhou.Rules_MeiZhou;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.service.ServiceContainer;

/**
 * 包杠：玩家不能补杠，当玩家结算自己明杠时，明杠的得分由被明杠的玩家一家承担三家的分数。 <br>
 * 不包杠：玩家可以补杠，当玩家结算自己明杠分时，可以向三家玩家收取分数。
 */
public class ScoreTargetFilter_Gang extends ScoreTargetFilter
{

	@Override
	public boolean filter(BattleBean context, PlayerInfo player, PlayerInfo sourcePlayer, PlayerInfo checkingPlayer)
	{
		// 得到roomservice的单例对象
		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		BattleRoom room = roomService.getRoomInfo(context.getRoomId());
		List<Integer> rulesList = room.getGameplays();

		for (Integer rule : rulesList)
		{
			if (rule == Rules_MeiZhou.GAME_PLAY_BU_BAO_GANG)
			{
				// 除了明杠的玩家都通过
				return checkingPlayer.getRoleId() != player.getRoleId();
			}
			else if (rule == Rules_MeiZhou.GAME_PLAY_BAO_GANG)
			{

				// 暗杠和补杠就还是收三家
				if (player.getRoleId() == sourcePlayer.getRoleId())
				{
					return checkingPlayer.getRoleId() != player.getRoleId();
				}

				// 只通过被明杠的玩家
				return checkingPlayer.getRoleId() == sourcePlayer.getRoleId();
			}
		}
		return false;

	}

}
