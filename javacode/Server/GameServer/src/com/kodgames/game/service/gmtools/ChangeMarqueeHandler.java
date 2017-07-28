package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.marquee.MarqueeService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "ChangeMarqueeHandler")
public class ChangeMarqueeHandler implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		long id = (Integer)args.get("marqueeId");
		boolean active = (boolean)args.get("active");
		
		HashMap<String, Object> result = new HashMap<>();
		MarqueeService service = ServiceContainer.getInstance().getPublicService(MarqueeService.class);
		Object data = service.changeMaquee(id, active);
		result.put("result", 1);
		result.put("data", data);
		
		return result;
	}
	
}
