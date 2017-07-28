package com.kodgames.authserver.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.service.global.GlobalService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "DeleteLibraryVersionHandler")
public class DeleteLibraryVersionHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(DeleteLibraryVersionHandler.class);

	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", 1);
		String platform = null;
		try
		{
			platform = (String)args.get("platform");
		}
		catch (Throwable t)
		{
			logger.error("DeleteLibraryVersionHandler args error : {}", args);
			result.put("data", 0);

			return result;
		}

		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		result.put("data", service.deleteLibraryVersion(platform));

		return result;
	}
}
