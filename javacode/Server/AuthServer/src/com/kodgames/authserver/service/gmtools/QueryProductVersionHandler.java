package com.kodgames.authserver.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.authserver.service.global.GlobalService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import xbean.ProVersion;

@GmtHandlerAnnotation(handler = "QueryProductVersionHandler")
public class QueryProductVersionHandler implements IGmtoolsHandler
{
	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<>();

		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		result.put("result", 1);
		HashMap<String, Object> data = new HashMap<>();
		ProVersion pv = service.queryProductVersion();
		data.put("product_version", pv.getVersion());
		data.put("product_description", pv.getDescription());
		result.put("data", data);

		return result;
	}

}
