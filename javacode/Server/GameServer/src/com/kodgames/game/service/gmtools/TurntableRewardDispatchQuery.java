package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "TurntableRewardDispatchQuery")
public class TurntableRewardDispatchQuery implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);

		int roleId = (Integer)json.get("playerId");
		List<Map<String, Object>> rewardList =
			ServiceContainer.getInstance().getPublicService(ActivityService.class).turntableRewardDispatchQuery(roleId);

		result.put("data", rewardList);
		return result;
	}

}
