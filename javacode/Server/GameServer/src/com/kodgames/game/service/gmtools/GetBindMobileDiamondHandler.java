package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

//http://localhost:13101/gmtools?{"handler":"GetBindMobileDiamondHandler","server_id":16842753}

/**
 * GMT调用 获取改绑定手机所获钻石数量
 * 
 * @author 张斌
 */
@GmtHandlerAnnotation(handler = "GetBindMobileDiamondHandler")
public class GetBindMobileDiamondHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(GetBindMobileDiamondHandler.class);

	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<>();

		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		int diamondNum = service.getBindMobileDiamond();

		result.put("result", 1);
		result.put("data", diamondNum);

		logger.debug("GetBindMobileDiamondHandler data diamond:" + diamondNum);

		return result;
	}
}
