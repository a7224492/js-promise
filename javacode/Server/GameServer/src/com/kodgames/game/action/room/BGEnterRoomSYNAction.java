package com.kodgames.game.action.room;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.message.proto.club.ClubProtoBuf.ClubPlayerInfoPROTO;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLEnterRoomSYN;
import com.kodgames.message.proto.game.GameProtoBuf.BGEnterRoomSYN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.RoleInfo;

@ActionAnnotation(messageClass = BGEnterRoomSYN.class, actionClass = BGEnterRoomSYNAction.class, serviceClass = RoleService.class)
public class BGEnterRoomSYNAction extends ProtobufMessageHandler<RoleService, BGEnterRoomSYN>
{
	private static Logger logger = LoggerFactory.getLogger(BGEnterRoomSYNAction.class);

	@Override
	public void handleMessage(Connection connection, RoleService service, BGEnterRoomSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roomId = message.getRoomId();
		int roleId = message.getRoleId();
		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		int battleId = roomService.getBattleIdByRoomId(roomId);
		service.joinRoom(roleId, roomId, battleId);

		// 通知club
		int clubId = roomService.getRoomClubId(roomId);
		if (clubId != 0)
		{
			GCLEnterRoomSYN.Builder clubBuilder = GCLEnterRoomSYN.newBuilder();
			clubBuilder.setRoomId(roomId);
			clubBuilder.setClubId(clubId);
			RoleInfo roleInfo = service.getRoleInfoByRoleId(roleId);
			ClubPlayerInfoPROTO.Builder player = ClubPlayerInfoPROTO.newBuilder();
			player.setRoleId(roleId);
			player.setRoleName(roleInfo.getNickname());
			clubBuilder.setPlayer(player.build());

			Connection clubConnection = ServiceContainer.getInstance().getPublicService(ServerService.class).getClubConnection();
			clubConnection.write(callback, clubBuilder.build());
		}
	}

	@Override
	public Object getMessageKey(Connection connection, BGEnterRoomSYN message)
	{
		return message.getRoleId() == 0 ? connection.getConnectionID() : message.getRoleId();
	}
}
