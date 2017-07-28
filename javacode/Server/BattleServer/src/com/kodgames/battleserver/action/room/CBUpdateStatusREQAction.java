package com.kodgames.battleserver.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.battle.BattleService;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.battleserver.start.CBProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.room.RoomProtoBuf.BCUpdateStatusRES;
import com.kodgames.message.proto.room.RoomProtoBuf.CBUpdateStatusREQ;

@ActionAnnotation(actionClass = CBUpdateStatusREQAction.class, messageClass = CBUpdateStatusREQ.class, serviceClass = RoomService.class)
public class CBUpdateStatusREQAction extends CBProtobufMessageHandler<RoomService, CBUpdateStatusREQ>
{
	final static Logger logger = LoggerFactory.getLogger(CBUpdateStatusREQAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, CBUpdateStatusREQ message, int callback)
	{
		// 修改玩家状态
		int roleId = connection.getRemotePeerID();
		int roomId = connection.getRoomID();

		logger.info("UpdateStatus : The role {} room {}", roleId, roomId);

		BattleRoom room = service.getRoomInfo(roomId);
		if (room == null)
		{
			logger.error("UpdateStatus failed : roleId {} roomId {}, Roomservice get room is null", roleId, roomId);
			return;
		}

		BCUpdateStatusRES.Builder builder = BCUpdateStatusRES.newBuilder();
		int newStatus = message.getStatus();
		int result = service.updatePlayerStatus(roleId, roomId, newStatus);
		builder.setResult(result);

		connection.write(callback, builder.build());

		// 同步玩家状态
		service.syncRoomPlayerInfoToMembers(room);

		// 开始打牌
		BattleService battleService = ServiceContainer.getInstance().getPublicService(BattleService.class);
		// 如果房间正在打牌，重新加入房间
		if (battleService.isBattleRunning(roomId))
			battleService.rejoin(roomId, roleId);
		// 所有人都在准备状态并且这个room不是在running状态
		if (!battleService.isBattleRunning(roomId) && service.isRoomReadyForBattle(room))
			battleService.startGame(room);
	}
}