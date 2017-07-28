package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.role.RoomCardService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

// http://127.0.0.1:13101/gmtools?{%22server_id%22:16842753,%22handler%22:%22SubPlayerCardHandler%22,%22playerId%22:5473886,%22gmAdminName%22:%22a%22,%22subCardNum%22:1,%22subCardReason%22:%22reason%22}

@GmtHandlerAnnotation(handler = "SubPlayerCardHandler")
public class SubPlayerCardHandler implements IGmtoolsHandler
{
	
	private static Logger logger = LoggerFactory.getLogger(SubPlayerCardHandler.class);

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		try
		{
			String gmAdminName = (String)json.get("gmAdminName");
			int playerId = (int)json.get("playerId");
			int subCardNum = (int)json.get("subCardNum");
			String subCardReason = (String)json.get("subCardReason");
			if (subCardNum <= 0)
			{
				result.put("data", -1);
			}
			else
			{
				result.put("data",
					ServiceContainer.getInstance().getPublicService(RoomCardService.class).subRoomCard(playerId,
						gmAdminName,
						subCardNum,
						subCardReason));
			}
		}
		catch (Throwable t)
		{
			result.put("data", 0);
			t.printStackTrace();
		}
		logger.info("Gmtools SubPlayerCardHandler {}", result);

		return result;
	}

}
