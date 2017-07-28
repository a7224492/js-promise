package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;
import com.kodgames.message.proto.game.GameProtoBuf.GBGmtDestroyRoomSYN;


// http://127.0.0.1:13101/gmtools?{%22server_id%22:16842753,%22handler%22:%22ForceDestroyRoomHandler%22,%22roomID%22:299066}

@GmtHandlerAnnotation(handler = "ForceDestroyRoomHandler")
public class GmtDestroyRoomHandler implements IGmtoolsHandler
{

	Logger logger = LoggerFactory.getLogger(GmtDestroyRoomHandler.class);

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", 1);

		int roomId;
		try
		{
			roomId = (int)json.get("roomID");
		}
		catch (Throwable t)
		{
			logger.error("ForceDestroyRoomHandler throw : ", t);
			result.put("data", 0);
			return result;
		}

		GBGmtDestroyRoomSYN.Builder builder = GBGmtDestroyRoomSYN.newBuilder();
		builder.setRoomId(roomId);
		RoomService service = ServiceContainer.getInstance().getPublicService(RoomService.class);
		int battleId = service.getBattleIdByRoomId(roomId);
		if (0 == battleId)
		{
			result.put("data", 0);
			return result;
		}

		Connection connection = ConnectionManager.getInstance().getServerConnnection(battleId);
		connection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());

		result.put("data", 1);
		return result;
	}

}
