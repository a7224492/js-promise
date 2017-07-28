package com.kodgames.game.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLDestroyRoomSYN;
import com.kodgames.message.proto.game.GameProtoBuf.BGDestroyRoomSYN;

@ActionAnnotation(messageClass = BGDestroyRoomSYN.class, actionClass = BGDestroyRoomSYNAction.class, serviceClass = RoomService.class)
public class BGDestroyRoomSYNAction extends ProtobufMessageHandler<RoomService, BGDestroyRoomSYN>
{
	private static final Logger logger = LoggerFactory.getLogger(BGDestroyRoomSYNAction.class);

	/*
	 * zdb锁使用顺序:
	 * 1. role_mem_info update (4个角色, roleService.roleQuitRoom)
	 * 2. room_info select (service.getRoomClubId)
	 * 3. room_info delete (锁升级 service.destroyRoom)
	 */
	@Override
	public void handleMessage(Connection connection, RoomService service, BGDestroyRoomSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roomId = message.getRoomId();

		RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
		for (Integer roleId : message.getRoleListList())
		{
			roleService.roleQuitRoom(roleId, message.getRoomId());
		}
		
		int clubId = service.getRoomClubId(roomId);
		service.destroyRoom(connection.getConnectionID(), message);
		
		// 通知club
		if (clubId != 0)
		{
			GCLDestroyRoomSYN.Builder clubBuilder = GCLDestroyRoomSYN.newBuilder();
			clubBuilder.setRoomId(roomId);
			clubBuilder.setClubId(clubId);

			Connection clubConnection =
				ServiceContainer.getInstance().getPublicService(ServerService.class).getClubConnection();
			clubConnection.write(callback, clubBuilder.build());
		}
	}

	@Override
	public Object getMessageKey(Connection connection, BGDestroyRoomSYN message)
	{
		return message.getRoomId() == 0 ? connection.getConnectionID() : message.getRoomId();
	}
}
