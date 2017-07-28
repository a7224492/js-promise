package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "AllowLoginPlatformHandler")
public class AllowLoginPlatformHandler implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		int operation = (int)args.get("operation");
		Object id = args.get("id");
		Object channel = args.get("allow_login");
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);

		GlobalService globalService = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		try
		{
			switch (operation)
			{
				case 0:
					result.put("data", globalService.addAllowPlatform((String)channel));
					break;

				case 1:
					result.put("data", globalService.updateAllowPlatform((int)id, (String)channel));
					break;

				case 2:
					result.put("data", globalService.removeAllowPlatform((int)id));
					break;

				default:
					result.put("data", 0);
			}
		}
		catch (Throwable t)
		{
			result.put("data", 0);
		}

		return result;
	}

}
