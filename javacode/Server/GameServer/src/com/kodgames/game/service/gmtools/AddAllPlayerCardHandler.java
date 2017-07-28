package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "AddAllPlayerCardHandler")
public class AddAllPlayerCardHandler implements IGmtoolsHandler
{
	private static Logger logger = LoggerFactory.getLogger(AddAllPlayerCardHandler.class);

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		result.put("result", 1);
		
		int amount = -1;
		String gmName = "";
		
		try
		{
			amount = (int)json.get("addAmount");
			gmName = (String)json.get("gmAdminName");
		}
		catch (Throwable e)
		{
			logger.error("AddAllPlayerCardHandler arg error, json={}", json);
			
			result.put("data", -1);
			
			return result;
		}
		
		GlobalService globalService = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		int res = globalService.addAllPlayerCard(amount, gmName);
		
		result.put("data", res);
		
		return result;
	}

}
