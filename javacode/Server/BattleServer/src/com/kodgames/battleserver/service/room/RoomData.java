package com.kodgames.battleserver.service.room;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.message.proto.room.RoomProtoBuf.RoomPlayerInfoPROTO;

public class RoomData
{
	public static RoomPlayerInfoPROTO playerInfoBeanToRoomProto(PlayerInfo player)
	{
		RoomPlayerInfoPROTO.Builder builder = RoomPlayerInfoPROTO.newBuilder();
		builder.setRoleId(player.getRoleId());
		builder.setPosition(player.getPosition());
		builder.setNickname(player.getNickname());
		builder.setHeadImageUrl(player.getHeadImageUrl());
		builder.setSex(player.getSex());
		builder.setIp(player.getIp());
		builder.setStatus(player.getStatus());
		builder.setTotalPoint(player.getTotalPoint());
		builder.setPointInGame(player.getPointInGame());
		return builder.build();
	}
}
