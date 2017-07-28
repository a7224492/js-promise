package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "ForbidAccountHandler")
public class ForbidAccountHandler implements IGmtoolsHandler
{
	Logger logger = LoggerFactory.getLogger(ForbidAccountHandler.class);
	
	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		int roleId;
		long forbidTime;
		try
		{
			roleId = (int)args.get("ID");
			forbidTime = Long.parseLong((String)args.get("forbid_time"));
		}
		catch (Throwable t)
		{
			logger.info("ForbidAccountHandler throw : ", t);
			result.put("data", 0);
			return result;
		}

		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		result.put("data", service.forbidRole(roleId, forbidTime));

		return result;
	}
}
