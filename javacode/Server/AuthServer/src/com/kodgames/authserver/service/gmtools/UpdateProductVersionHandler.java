package com.kodgames.authserver.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.service.global.GlobalService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "UpdateProductVersionHandler")
public class UpdateProductVersionHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(UpdateProductVersionHandler.class);

	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", 1);
		String version = null;
		String description = null;
		try
		{
			version = (String)args.get("product_version");
			description = (String)args.get("product_description");
		}
		catch (Throwable t)
		{
			logger.error("UpdateProductVersionHandler args error : {}", args);
			result.put("data", 0);

			return result;
		}

		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		result.put("data", service.updateProductVersion(version, description));

		return result;
	}
}
