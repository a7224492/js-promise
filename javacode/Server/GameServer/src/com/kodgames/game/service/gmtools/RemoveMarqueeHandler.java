package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.marquee.MarqueeService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

/**
 * Created by lz on 2016/8/24.
 * 设置某个跑马灯为不活动
 */
@GmtHandlerAnnotation(handler = "RemoveMarquee")
public class RemoveMarqueeHandler implements IGmtoolsHandler
{
	@Override public HashMap<String, Object> getResult(Map<String, Object> map)
	{

		HashMap<String, Object> result = new HashMap<>();

		ServiceContainer.getInstance().getPublicService(MarqueeService.class).removeMarquee((int)map.get("id"));
		result.put("result", 1);
		return result;
	}
}
