package com.kodgames.authserver.service.gmtools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.authserver.service.global.GlobalService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "QueryLibraryVersionHandler")
public class QueryLibraryVersionHandler implements IGmtoolsHandler
{
	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<>();

		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		result.put("result", 1);

		List<HashMap<String, Object>> data = new ArrayList<>();
		service.queryLibraryVersion().entrySet().forEach(e -> {
			HashMap<String, Object> record = new HashMap<>();
			record.put("platform", e.getKey());
			record.put("library_version", e.getValue().getVersion());
			record.put("library_description", e.getValue().getDescription());
			record.put("library_url", e.getValue().getUrl());
			record.put("force_update", e.getValue().getForceUpdate());
			data.add(record);
		});
		result.put("data", data);

		return result;
	}

}
