package com.kodgames.club.service.gmtools.handler;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.service.gmtools.ClubGmToolsService;
import com.kodgames.club.service.gmtools.ClubGmtBaseHandler;
import com.kodgames.club.utils.ClubUtils;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.httpserver.KodHttpMessage;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;

@GmtHandlerAnnotation(handler = "ClubGmtChangeClubStatusHandler")
public class ClubGmtChangeClubStatusHandler extends ClubGmtBaseHandler
{

	@Override
	public HashMap<String, Object> getResult(KodHttpMessage msg)
	{
		HashMap<String, Object> result = new HashMap<>();
		Map<String, Object> args = this.getJson(msg);
		if (args == null)
		{
			result.put("result", -1);
			return result;
		}
		result.put("result", 1);
		int clubId = (int)args.get("clubId");
		int flag = (int)args.get("flag");
		ClubGmToolsService gmtService = ServiceContainer.getInstance().getPublicService(ClubGmToolsService.class);
		int data = gmtService.changeClubStatus(clubId, flag);
		if (data == ClubConstants.CLUB_GMT_ERROR.CHANGE_STATUS_ERROR)
		{
			result.put("data", data);
		}
		else
		{
			if (ClubUtils.checkClubStatus(data, ClubConstants.CLUB_STATUS.SAEL_FLAG))
			{
				ClubCommonService ccs = ServiceContainer.getInstance().getPublicService(ClubCommonService.class);
				ccs.synClubBanToClient(clubId);
			}
			HashMap<String, Object> map = new HashMap<>();
			map.put("clubId", clubId);
			map.put("status", data);
			result.put("data", map);
		}
		return result;
	}

}
