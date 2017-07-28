package com.kodgames.game.action.room;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLCreateRoomRES;
import com.kodgames.message.proto.game.GameProtoBuf.BGCreateRoomRES;
import com.kodgames.message.proto.game.GameProtoBuf.GCCreateRoomRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.RoomInfo;

@ActionAnnotation(messageClass = BGCreateRoomRES.class, actionClass = BGCreateRoomRESAction.class, serviceClass = RoomService.class)
public class BGCreateRoomRESAction extends ProtobufMessageHandler<RoomService, BGCreateRoomRES>
{
	private static final Logger logger = LoggerFactory.getLogger(BGCreateRoomRESAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, BGCreateRoomRES message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getRemotePeerID(), message);

		int battleId = connection.getRemotePeerID();
		int roomId = message.getRoomId();
		int creatorId = message.getCreatorId();
		int clubId = service.getRoomClubId(roomId);
		RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);

		int successCode	;
		if (clubId == 0)
		{
			GCCreateRoomRES.Builder builder = GCCreateRoomRES.newBuilder();
			successCode = PlatformProtocolsConfig.GC_CREATE_ROOM_SUCCESS;
			if (PlatformProtocolsConfig.BG_CREATE_ROOM_SUCCESS != message.getResult())
			{
				if (message.getResult() != PlatformProtocolsConfig.BG_CREATE_ROOM_FAILED_ROOM_EXIST)
				{
					// 对于非"房间已存在"的错误,删除正在创建的房间信息
					service.deleteCreatingRoom(roomId);
				}

				// 创建失败
				logger.error("BattleServer can not create a room : {} -> {}!", message.getResult(), roomId);
				builder.setResult(message.getResult());
			}
			else
			{
				// 创建成功
				roleService.joinRoom(creatorId, roomId, battleId);
				service.createRoom(creatorId, roomId, message.getGameplaysList());
				builder.setResult(successCode);
				builder.setRoomId(roomId);
				builder.setBattleId(battleId);
				builder.setVoice(message.getVoice());
			}

			Connection roleConnection = ConnectionManager.getInstance().getClientVirtualConnection(creatorId);
			if (roleConnection != null)
				// 有可能已经离线了
				roleConnection.write(callback, builder.build());
		}
		else
		{
			GCLCreateRoomRES.Builder builder = GCLCreateRoomRES.newBuilder();
			successCode = PlatformProtocolsConfig.GCL_CREATE_ROOM_SUCCESS;

			if (PlatformProtocolsConfig.BG_CREATE_ROOM_SUCCESS != message.getResult())
			{
				if (message.getResult() != PlatformProtocolsConfig.BG_CREATE_ROOM_FAILED_ROOM_EXIST)
				{
					// 对于非"房间已存在"的错误,删除正在创建的房间信息
					service.deleteCreatingRoom(roomId);
				}
				// 创建失败
				logger.error("BattleServer can not create a room : {} -> {}!", message.getResult(), roomId);
				builder.setResult(message.getResult());
			}
			else
			{
				// 创建成功
				roleService.joinRoom(creatorId, roomId, battleId);
				service.createRoom(creatorId, roomId, message.getGameplaysList());

				RoomInfo roomInfo = service.getRoom(roomId);
				if (roomInfo == null)
				{
					builder.setResult(PlatformProtocolsConfig.BG_CREATE_ROOM_FAILED);
					logger.warn("can't find room_info for room {}", roomId);
				}
				else
				{
					builder.setResult(successCode);
					builder.setRoomId(roomId);
					builder.setBattleId(battleId);
					builder.setClubId(clubId);
					builder.setCreatorId(creatorId);
					builder.setCost(roomInfo.getCost());
					builder.addAllGameplays(message.getGameplaysList());
					builder.setPlayerMax(message.getPlayerMax());
					builder.setPayType(roomInfo.getPayType());
					builder.setRoundCount(roomInfo.getRoundCount());
					builder.setVoice(message.getVoice());
				}
			}

			Connection clubConnection = ServiceContainer.getInstance().getPublicService(ServerService.class).getClubConnection();
			if (clubConnection != null)
				clubConnection.write(callback, builder.build());
		}
	}

	@Override
	public Object getMessageKey(Connection connection, BGCreateRoomRES message)
	{
		return message.getCreatorId() == 0 ? connection.getConnectionID() : message.getCreatorId();
	}
}
