package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.role.RoomCardService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "QueryCardLogHandler")
public class QueryAddCardHandler implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", 1);

		RoomCardService service = ServiceContainer.getInstance().getPublicService(RoomCardService.class);
		int roleId = (int)args.get("playerID");
		result.put("data", service.queryAddCard(roleId));

		return result;
	}

}
