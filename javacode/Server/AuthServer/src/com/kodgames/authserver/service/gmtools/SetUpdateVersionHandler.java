package com.kodgames.authserver.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.service.global.GlobalService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

//http://localhost:13101/gmtools?{"handler":"SetUpdateVersionHandler","channel":"0", "subchannel":"0", "libVersion": "1.65", "lastLibVersion":"1.5", "libUrl":"", "proVersion": "2.0","proForceUpdate":true,"proUrl":"", "reviewVersion":"1.2", "reviewUrl":"","server_id":17104897}

@GmtHandlerAnnotation(handler = "SetUpdateVersionHandler")
public class SetUpdateVersionHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(SetUpdateVersionHandler.class);
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", 1);
		result.put("data", 1);
		String id = null;
		String channel = null;
		String subchannel = null;
		String libVersion = null;
		String lastLibVersion = null;
		String libUrl = null;
		String proVersion = null;
		String proUrl = null;
		String reviewVersion = null;
		String reviewUrl = null;
		Boolean proForceUpdate = false;
		try
		{
			channel = (String)args.get("channel");
			subchannel = (String)args.get("subchannel");
			libVersion = (String)args.get("libVersion");
			lastLibVersion = (String)args.get("lastLibVersion");
			libUrl = (String)args.get("libUrl");
			proVersion = (String)args.get("proVersion");
			proUrl = (String)args.get("proUrl");
			reviewVersion = (String)args.get("reviewVersion");
			reviewUrl = (String)args.get("reviewUrl");
			proForceUpdate = (Boolean)args.get("proForceUpdate");
			id = channel + "_" + subchannel;
		}
		catch (Throwable t)
		{
			logger.error("SetUpdateVersionHandler args error : {}", args);
			result.put("result", 0);
			result.put("data", "");

			return result;
		}
		
		logger.info("SetUpdateVersionHandler Success");
		
		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		service.setUpdateVersion(id, channel, subchannel, libVersion, lastLibVersion, libUrl, 
			proVersion, proUrl, reviewVersion, reviewUrl, proForceUpdate);
		
		return result;
	}
}
