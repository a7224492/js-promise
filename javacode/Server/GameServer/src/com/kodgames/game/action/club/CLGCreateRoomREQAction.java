package com.kodgames.game.action.club;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.common.Constant.RoomContinueStatu;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.club.ClubProtoBuf.CLGCreateRoomREQ;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLCreateRoomRES;
import com.kodgames.message.proto.game.GameProtoBuf.GBCreateRoomREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import limax.zdb.exception.ZdbRollbackException;

@ActionAnnotation(messageClass = CLGCreateRoomREQ.class, actionClass = CLGCreateRoomREQAction.class, serviceClass = RoomService.class)
public class CLGCreateRoomREQAction extends CGProtobufMessageHandler<RoomService, CLGCreateRoomREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CLGCreateRoomREQAction.class);

	/**
	 * 创建房间后，将RoleMemInfo中的信息设置好，通过RoomInfo知道该RoomId正在创建
	 */
	@Override
	public void handleMessage(Connection connection, RoomService service, CLGCreateRoomREQ message, int callback)
	{
		int creatorId = message.getCreatorId();

		int roomId = ServiceContainer.getInstance().getPublicService(RoleService.class).getRoomIdByRoleId(creatorId);
		if (roomId != 0)
		{
			// 房间已在创建中
			if (service.isCreatingRoom(roomId))
			{
				GCLCreateRoomRES.Builder builder = GCLCreateRoomRES.newBuilder();
				builder.setResult(PlatformProtocolsConfig.GCL_CREATE_ROOM_FAILED_CREATING);
				connection.write(callback, builder.build());

				logger.debug("crateRoom: rold {} is creating room return false", creatorId);
				// 此处也没有数据库操作，不需要抛出数据库异常
				return;
			}

			// 已在房间中的玩家不能再创建房间
			GCLCreateRoomRES.Builder builder = GCLCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.GCL_CREATE_ROOM_FAILED_ALREADY_IN_ROOM);
			connection.write(callback, builder.build());

			logger.warn("createRoom:role {} createroom found already has roomId {}", creatorId, roomId);
			// 此处没有数据库操作，不需要回滚
			return;
		}

		// 没有可用的BattleServer
		Integer battleId = service.getAvailableBattleId();
		if (null == battleId)
		{
			GCLCreateRoomRES.Builder builder = GCLCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.GCL_CREATE_ROOM_FAILED_NO_BATTLE);
			connection.write(callback, builder.build());

			logger.error("createRoom: roleId {} without available battleId", creatorId);
			throw new ZdbRollbackException(String.format("roleId %d createRoom without available battleId", creatorId));
		}

		int newRoomId = service.getNewRoomId();
		if (newRoomId == 0)
		{
			GCLCreateRoomRES.Builder builder = GCLCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.GCL_CREATE_ROOM_FAILED_CREATING);
			connection.write(callback, builder.build());

			logger.debug("crateRoom for club failed for can't get free roomId, creator: {}", creatorId);
			return;
		}

		// 将房间号置为创建中
		if (!service.creatingRoom(battleId, newRoomId, message.getRoundCount(), false, message.getClubId(), message.getCost(), message.getPayType()))
		{
			GCLCreateRoomRES.Builder builder = GCLCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.GCL_CREATE_ROOM_FAILED_CREATING);
			connection.write(callback, builder.build());

			logger.debug("crateRoom for club failed for roomId already exist in table.Room_info, creator: {} roomId: {}", creatorId, newRoomId);
			return;
		}

		// 向Battle请求创建房间
		GBCreateRoomREQ.Builder builder = GBCreateRoomREQ.newBuilder();
		builder.setCreatorId(creatorId);
		builder.setRoomId(newRoomId);
		builder.setRoomType(message.getRoomType());
		builder.addAllGameplays(message.getGameplaysList());
		builder.setRoundCount(message.getRoundCount());
		builder.setVoice(message.getVoice());
		Connection battleConnection = ConnectionManager.getInstance().getServerConnnection(battleId);
		battleConnection.write(callback, builder.build());
	}

}
