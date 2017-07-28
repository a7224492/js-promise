package com.kodgames.battleserver.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.common.Constant.RoomDestroyReason;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.battleserver.service.server.ServerService;
import com.kodgames.battleserver.start.CBProtobufMessageHandler;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.game.GameProtoBuf.BGQuitRoomSYN;
import com.kodgames.message.proto.room.RoomProtoBuf.BCQuitRoomRES;
import com.kodgames.message.proto.room.RoomProtoBuf.CBQuitRoomREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(actionClass = CBQuitRoomREQAction.class, messageClass = CBQuitRoomREQ.class, serviceClass = RoomService.class)
public class CBQuitRoomREQAction extends CBProtobufMessageHandler<RoomService, CBQuitRoomREQ>
{
	final static Logger logger = LoggerFactory.getLogger(CBQuitRoomREQAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, CBQuitRoomREQ message, int callback)
	{

		int roleId = connection.getRemotePeerID();
		int roomId = connection.getRoomID();

		logger.info("QuitRoom : The role {} attempt quit room {}", roleId, roomId);

		int result = service.checkQuitRoom(roleId, roomId);
		BCQuitRoomRES.Builder builder = BCQuitRoomRES.newBuilder();
		builder.setResult(result);
		if (PlatformProtocolsConfig.BC_QUIT_ROOM_SUCCESS != result)
		{
			// 退出房间不成功也需要给客户端回协议
			connection.write(callback, builder.build());
			logger.warn("QuitRoom failed : The role {} attempt quit room {} errorCode {}", roleId, roomId, result);
			return;
		}

		BattleRoom room = service.getRoomInfo(roomId);
		// 将玩家信息同步到Game
		BGQuitRoomSYN.Builder bgQuitbuilder = BGQuitRoomSYN.newBuilder();
		bgQuitbuilder.setRoleId(roleId);
		bgQuitbuilder.setRoomId(room.getRoomId());
		ServerService serverService = ServiceContainer.getInstance().getPublicService(ServerService.class);
		Connection gameConnection = serverService.getGameConnection();
		if (null != gameConnection)
			gameConnection.write(GlobalConstants.DEFAULT_CALLBACK, bgQuitbuilder.build());

		// 不论是否解散房间，都需要应答客户端，防止客户端卡在收应答
		connection.write(callback, builder.build());

		if (!service.isHaveBeginFirstGame(room))
		{
			// 房主离开，则销毁房间
			if (roleId == room.getCreatorId())
			{
				service.destroyRoom(roomId, RoomDestroyReason.CREATOR);
			}
			else
			{
				service.quitRoom(roleId, room);

				// 将剩余玩家数据同步给Client
				service.syncRoomPlayerInfoToMembers(room);
				
				// 剩余玩家GPS数据同步给Client
				service.syncSecureDetectInfoToPlayers(room);
			}
		}
		else
		{
			PlayerInfo player = service.getRoomPlayerInfo(roomId, roleId);
			service.quitRoom(roleId, room);

			// 将剩余玩家数据同步给Client
			service.syncRoomPlayerInfoToMembers(room);
			
			// 剩余玩家GPS数据同步给Client
			service.syncSecureDetectInfoToPlayers(room);
		}

		connection.setRoomID(0);
	}
}