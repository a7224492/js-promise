package com.kodgames.authserver.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.service.global.GlobalService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

//http://localhost:13101/gmtools?{"handler":"RemoveUpdateVersionHandler","channel":"0", "subchannel":"0", "server_id":17104897}

@GmtHandlerAnnotation(handler = "RemoveUpdateVersionHandler")
public class RemoveUpdateVersionHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(RemoveUpdateVersionHandler.class);
	
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", 1);
		result.put("data", 1);
		String id = null;
		try
		{
			id = (String)args.get("channel")+"_"+(String)args.get("subchannel");
		}
		catch(Throwable t)
		{
			logger.error("RemoveUpdateVersionHandler args error : {}", args);
			result.put("result", 0);
			result.put("data", "");

			return result;
		}
		
		logger.info("RemoveUpdateVersionHandler Success");
		
		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		service.removeUpdateVersion(id);

		return result;
	}
}
