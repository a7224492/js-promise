package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.marquee.MarqueeService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "QueryMarqueeHandler")
public class QueryMarqueeHandler implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<>();
		MarqueeService service = ServiceContainer.getInstance().getPublicService(MarqueeService.class);
		Object data = service.queryMaquee();
		result.put("result", 1);
		result.put("data", data);
		
		return result;
	}
	
}
