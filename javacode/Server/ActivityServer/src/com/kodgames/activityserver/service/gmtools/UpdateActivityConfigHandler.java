package com.kodgames.activityserver.service.gmtools;

import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangzhen on 2017/7/26
 */
@GmtHandlerAnnotation(handler = "UpdateActivityConfigHandler")
public class UpdateActivityConfigHandler implements IGmtoolsHandler
{
	@Override
	public HashMap<String, Object> getResult(Map<String, Object> map)
	{
		int activityId = (int)map.get("activityId");
	}
}
