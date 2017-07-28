package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.role.RoomCardService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "QueryAddPlayersCardHandler")
public class QueryAddPlayersCardHandler implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		List<Map<String, Object>> list =
			ServiceContainer.getInstance().getPublicService(RoomCardService.class).onQueryAddPlayersCard(json);
		result.put("data", list);
		return result;
	}

}
