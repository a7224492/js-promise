package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.role.RoomCardService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "QuerySubPlayerCardHandler")
public class QuerySubPlayerCardHandler implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);

		RoomCardService service = ServiceContainer.getInstance().getPublicService(RoomCardService.class);
		int playerID = (int)json.get("playerID");
		result.put("data", service.querySubCard(playerID));

		return result;
	}

}
