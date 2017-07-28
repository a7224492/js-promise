package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "QueryAllowLoginPlatformHandler")
public class QueryAllowLoginPlatformHandler implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> arg0)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		result.put("data", service.queryAllowPlatform());

		return result;
	}

}
