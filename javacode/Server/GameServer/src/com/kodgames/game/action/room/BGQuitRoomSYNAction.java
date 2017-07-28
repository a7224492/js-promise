package com.kodgames.game.action.room;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.message.proto.club.ClubProtoBuf.ClubPlayerInfoPROTO;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLQuitRoomSYN;
import com.kodgames.message.proto.game.GameProtoBuf.BGQuitRoomSYN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.RoleInfo;

@ActionAnnotation(messageClass = BGQuitRoomSYN.class, actionClass = BGQuitRoomSYNAction.class, serviceClass = RoleService.class)
public class BGQuitRoomSYNAction extends ProtobufMessageHandler<RoleService, BGQuitRoomSYN>
{
	private static Logger logger = LoggerFactory.getLogger(BGQuitRoomSYNAction.class);

	/*
	 * zdb锁顺序:
	 * 1. role_mem_info update (4个角色, service.roleQuitRoom)
	 * 2. room_info select (roomService.getRoomClubId)
	 */
	@Override
	public void handleMessage(Connection connection, RoleService service, BGQuitRoomSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roomId = message.getRoomId();
		int roleId = message.getRoleId();
		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		
		service.roleQuitRoom(roleId, roomId);

		// 通知club
		int clubId = roomService.getRoomClubId(roomId);
		if (clubId != 0)
		{
			GCLQuitRoomSYN.Builder clubBuilder = GCLQuitRoomSYN.newBuilder();
			clubBuilder.setRoomId(roomId);
			clubBuilder.setClubId(clubId);
			RoleInfo roleInfo = service.getRoleInfoByRoleId(roleId);
			ClubPlayerInfoPROTO.Builder player = ClubPlayerInfoPROTO.newBuilder();
			player.setRoleId(roleId);
			player.setRoleName(roleInfo.getNickname());
			clubBuilder.setPlayer(player.build());

			Connection clubConnection =
				ServiceContainer.getInstance().getPublicService(ServerService.class).getClubConnection();
			clubConnection.write(callback, clubBuilder.build());
		}
	}

	@Override
	public Object getMessageKey(Connection connection, BGQuitRoomSYN message)
	{
		return message.getRoleId() == 0 ? connection.getConnectionID() : message.getRoleId();
	}
}
