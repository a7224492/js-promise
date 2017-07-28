package com.kodgames.manageserver.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "AddInterfaceIp")
public class AddInterfaceIp implements IGmtoolsHandler
{
	private static Logger logger = LoggerFactory.getLogger(AddInterfaceIp.class);

	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
//		ServerService service = ServiceContainer.getInstance().getPublicService(ServerService.class);
//		service.addInterfaceIpMapping((String)json.get("ip_inside"), (String)json.get("ip_outside"));
		result.put("result", 1);
		logger.info("Gmtools UpdateConfigHandler {}", result);
		return result;
	}
}
