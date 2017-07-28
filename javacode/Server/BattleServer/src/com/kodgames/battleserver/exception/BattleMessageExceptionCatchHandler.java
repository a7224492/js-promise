package com.kodgames.battleserver.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.common.Constant.RoomDestroyReason;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.IMessageExceptionCatchHandler;
import com.kodgames.corgi.core.service.ServiceContainer;

public class BattleMessageExceptionCatchHandler implements IMessageExceptionCatchHandler
{
	private Logger logger = LoggerFactory.getLogger(BattleMessageExceptionCatchHandler.class);

	@Override
	public void handleMessage(Connection connection, int protocolID, int callback, Object buffer)
	{
		// 协议处理异常后，将该玩家所在的room解散
		int roomId = connection.getRoomID();
		if (connection.getConnectionType() == Connection.CONNECTION_TYPE_INTERFACE_TRANSFER_CLIENT && roomId != 0)
		{
			logger.debug("connection {} handler protocolID {}-{} found exception then destory room {}", connection.toString(), protocolID, Integer.toHexString(protocolID), roomId);
			RoomService service = ServiceContainer.getInstance().getPublicService(RoomService.class);
			service.destroyRoom(roomId, RoomDestroyReason.EXCEPTION);
		}
	}

}
