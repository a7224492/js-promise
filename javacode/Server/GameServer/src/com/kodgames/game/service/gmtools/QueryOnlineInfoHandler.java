package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import xbean.RuntimeBattleInfo;

@GmtHandlerAnnotation(handler = "QueryOnlineInfoHandler")
public class QueryOnlineInfoHandler implements IGmtoolsHandler
{
	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		GlobalService statisticService = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
		Map<String, Object> data = new HashMap<String, Object>();
		int roomCount = 0;
		int postiveRoomCount = 0;
		int slientRoomCount = 0;
		Map<Integer, RuntimeBattleInfo> battles = statisticService.getAllBattleInfo();
		if (null != battles)
		{
			for (Map.Entry<Integer, RuntimeBattleInfo> entry : battles.entrySet())
			{
				RuntimeBattleInfo battle = entry.getValue();
				if (null != battle)
				{
					roomCount += battle.getTotalRoomCount();
					postiveRoomCount += battle.getPositiveRoomCount();
					slientRoomCount += battle.getSilentRoomCount();
				}
			}
		}
		
		data.put("accountCount", roleService.getOnlinePlayerCount());
		data.put("roomCount", roomCount);
		data.put("postiveRoomCount", postiveRoomCount);
		data.put("slientRoomCount", slientRoomCount);

		result.put("result", 1);
		result.put("data", data);
		return result;

	}
}
