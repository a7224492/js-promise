package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.game.service.activity.LimitedCostlessActivityService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "ChangeLimitedCostlessActivityHandler")
public class ChangeLimitedCostlessActivityHandler implements IGmtoolsHandler
{
	
	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		result.put("result", 1);
		
		LimitedCostlessActivityService lcaService = ServiceContainer.getInstance().getPublicService(ActivityService.class).getLca();
		
		int data = lcaService.changeLimitedCostlessActivity(json);
		
		result.put("data", data);
		
		return result;
	}

}
