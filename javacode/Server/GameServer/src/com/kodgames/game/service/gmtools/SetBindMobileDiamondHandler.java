package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

//http://localhost:13101/gmtools?{"handler":"SetBindMobileDiamondHandler","diamond":1,"server_id":16842753}

/**
 * GMT调用，更改绑定手机所获钻石数量
 * 
 * @author 张斌
 */
@GmtHandlerAnnotation(handler = "SetBindMobileDiamondHandler")
public class SetBindMobileDiamondHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(SetBindMobileDiamondHandler.class);

	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", 1);
		result.put("data", 1);
		int diamond;
		try
		{
			diamond = (int)args.get("diamond");
		}
		catch (Throwable t)
		{
			logger.error("SetBindMobileDiamondHandler args error : {}", args);
			result.put("data", -1);

			return result;
		}

		if (diamond < 1 || diamond > 98)
		{
			result.put("data", -2);
			return result;
		}

		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		service.setBindMobileDiamond(diamond);

		return result;
	}
}
