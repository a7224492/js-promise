package com.kodgames.game.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.message.proto.game.GameProtoBuf.BGInviterRoomInfoRES;
import com.kodgames.message.proto.game.GameProtoBuf.GCInviterRoomInfoRES;

@ActionAnnotation(messageClass = BGInviterRoomInfoRES.class, actionClass = BGInviterRoomInfoRESAction.class, serviceClass = RoomService.class)
public class BGInviterRoomInfoRESAction extends ProtobufMessageHandler<RoomService, BGInviterRoomInfoRES>
{
	private static Logger logger = LoggerFactory.getLogger(BGInviterRoomInfoRESAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, BGInviterRoomInfoRES message, int callback)
	{
		logger.info("{}:{}->{}", getClass().getSimpleName(), connection.getConnectionID(), message);	
		Connection roleConnection = ConnectionManager.getInstance().getClientVirtualConnection(message.getRoleId());
		if ( roleConnection != null ) 
		{
			GCInviterRoomInfoRES.Builder builder = GCInviterRoomInfoRES.newBuilder();
			builder.setResult(message.getResult());
			builder.setInfo(message.getInfo());
			roleConnection.write(callback, builder.build());
		}
		else 
		{
			// 有可能玩家已下线
			logger.info("cannot found roleConnection.");
		}
	}

}
