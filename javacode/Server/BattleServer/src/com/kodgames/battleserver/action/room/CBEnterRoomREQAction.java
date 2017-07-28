package com.kodgames.battleserver.action.room;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.battle.BattleService;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.battleserver.service.server.ServerService;
import com.kodgames.battleserver.start.CBProtobufMessageHandler;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.game.GameProtoBuf.BGQuitRoomSYN;
import com.kodgames.message.proto.room.RoomProtoBuf.BCEnterRoomRES;
import com.kodgames.message.proto.room.RoomProtoBuf.CBEnterRoomREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(actionClass = CBEnterRoomREQAction.class, messageClass = CBEnterRoomREQ.class, serviceClass = RoomService.class)
public class CBEnterRoomREQAction extends CBProtobufMessageHandler<RoomService, CBEnterRoomREQ>
{
	final static Logger logger = LoggerFactory.getLogger(CBEnterRoomREQAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, CBEnterRoomREQ message, int callback)
	{
		final int roleId = message.getRoleId();
		final int roomId = message.getRoomId();

		logger.info("EnterRoom : The role {} attempt enter room {}", roleId, roomId);

		// 玩家已经在房间里面了
		if (connection.getRoomID() != 0 && service.isRoomExist(connection.getRoomID()))
		{
			logger.error("EnterRoom : The role {} has in other room:{}", roleId, roomId);
			BCEnterRoomRES.Builder bcResbuilder = BCEnterRoomRES.newBuilder();
			bcResbuilder.setResult(PlatformProtocolsConfig.BC_ENTER_ROOM_FAILED_ROOM_NOT_EXIST);
			bcResbuilder.setIsHaveBeginFirstGame(false);
			bcResbuilder.setRoomType(0);
			bcResbuilder.addAllGameplays(new ArrayList<>());
			bcResbuilder.setRoundCount(0);
			bcResbuilder.setMaxPlayerCount(0);
			bcResbuilder.setPayType(0);
			bcResbuilder.setVoice(false);

			connection.write(callback, bcResbuilder.build());
			return;
		}

		Connection existedConnection = ConnectionManager.getInstance().getClientVirtualConnection(roleId);

		// 新的链接
		if (existedConnection == null)
		{
			// 设置Connection
			ConnectionManager.getInstance().addToClientVirtualConnections(connection);
		}
		// 存在一个老的链接
		else if (existedConnection != null && existedConnection.getConnectionID() != connection.getConnectionID())
		{
			ConnectionManager.getInstance().removeConnection(existedConnection);
			if (roomId != existedConnection.getRoomID())
			{
				logger.warn("EnterRoom failed : The role {} existed in other room {}, but attempt enter room {}", roleId, existedConnection.getRoomID(), roomId);
				RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
				roomService.roleDisconnect(roleId, existedConnection.getRoomID());
			}

			ConnectionManager.getInstance().addToClientVirtualConnections(connection);
		}

		// 响应Client进入房间请求
		BCEnterRoomRES.Builder bcResbuilder = BCEnterRoomRES.newBuilder();
		int error = service.checkEnterRoom(roleId, roomId);
		bcResbuilder.setResult(error);
		if (PlatformProtocolsConfig.BC_ENTER_ROOM_SUCCESS != error)
		{
			// 通知给game，处理那些game上存在房间记录，但是battle上不存在房间
			// TODO: 其实手动输入输入房间号加入房间的情况，如果房间不存在不需要通知game，但现在不能区分手动输入还是自动加入
			BGQuitRoomSYN.Builder bgQuitbuilder = BGQuitRoomSYN.newBuilder();
			bgQuitbuilder.setRoleId(roleId);
			bgQuitbuilder.setRoomId(roomId);
			ServerService serverService = ServiceContainer.getInstance().getPublicService(ServerService.class);
			Connection gameConnection = serverService.getGameConnection();
			if (null != gameConnection)
				gameConnection.write(GlobalConstants.DEFAULT_CALLBACK, bgQuitbuilder.build());

			// 将错误返回给客户端
			bcResbuilder.setIsHaveBeginFirstGame(false);
			bcResbuilder.setRoomType(0);
			bcResbuilder.setRoundCount(0);
			bcResbuilder.setMaxPlayerCount(0);
			bcResbuilder.setPayType(0);
			bcResbuilder.setVoice(false);
			connection.write(callback, bcResbuilder.build());
			logger.info("enterRoom role {} roomId {} code {}", roleId, roomId, error);
			return;
		}

		BattleService battleService = ServiceContainer.getInstance().getPublicService(BattleService.class);
		BattleRoom room = service.enterRoom(roleId, roomId, message.getNickname(), message.getHeadImageUrl(), message.getSex(), connection);

		// 判断是否已经开始了第一局
		bcResbuilder.setIsHaveBeginFirstGame(service.isHaveBeginFirstGame(room));
		bcResbuilder.setRoomType(room.getRoomType());
		bcResbuilder.addAllGameplays(room.getGameplays());
		bcResbuilder.setRoundCount(room.getCountType());
		bcResbuilder.setMaxPlayerCount(room.getMaxMemberCount());
		bcResbuilder.setPayType(room.getPayType());
		bcResbuilder.setVoice(room.isVoice());

		// 响应客户端进入房间
		connection.write(callback, bcResbuilder.build());

		service.synBattleToGame(roleId, roomId);

		// 将玩家数据同步给Client
		service.syncRoomPlayerInfoToMembers(room);

		// 房间内正在打牌，向该玩家同步打牌信息
		if (battleService.isBattleRunning(roomId))
		{
			battleService.rejoin(roomId, roleId);
			logger.debug("role {} rejoin room {}", roleId, roomId);
		}
		// 如果可以开牌就开牌
		else if (!battleService.isBattleRunning(roomId) && service.isRoomReadyForBattle(room))
		{
			battleService.startGame(room);
		}
		// 如果房间内正在投票，向该玩家同步投票信息
		if (service.isRoomVotingDestroy(room))
		{
			service.syncVoteDestroyInfoToPlayers(room, roleId);
		}
	}

	@Override
	public Object getMessageKey(Connection connection, CBEnterRoomREQ message)
	{
		return message.getRoomId();
	}
}
