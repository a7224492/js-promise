package com.kodgames.battleserver.service.battle.region.yangzhou.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;

/**
 * 玩家进园子时，可以被杠，但不收取杠分
 */
public class ScoreTargetFilter_Gang_YangZhou extends ScoreTargetFilter
{
	@Override
	public boolean filter(BattleBean context, PlayerInfo player, PlayerInfo sourcePlayer, PlayerInfo checkingPlayer)
	{
		//自己不收自己的分
		if(player.getRoleId() == checkingPlayer.getRoleId())
			return false;
		
		//吃杠时只有被吃杠者通过
		if((player.getRoleId() != sourcePlayer.getRoleId()) && (checkingPlayer.getRoleId() != sourcePlayer.getRoleId()))
		{
			return false;
		}
		
//		//得到roomservice的单例对象
//		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
//		BattleRoom room = roomService.getRoomInfo(context.getRoomId());
//		List<Integer> rules = room.getGameplays();
//		int yuanZi = Rules_YangZhou.getYuanZi(rules);
//		if(yuanZi != 0 && checkingPlayer.getTotalPoint() <= -yuanZi)
//		{
//			//玩家已进园子不能收取他的杠分
//			return false;
//		}
		
		return true;
	}
}
