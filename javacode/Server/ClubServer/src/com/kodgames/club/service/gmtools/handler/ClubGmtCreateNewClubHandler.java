package com.kodgames.club.service.gmtools.handler;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.gmtools.ClubGmToolsCheckCreateService;
import com.kodgames.club.service.gmtools.ClubGmtBaseHandler;
import com.kodgames.club.service.gmtools.ClubGmToolsService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.httpserver.HttpReturnContent;
import com.kodgames.corgi.httpserver.KodHttpMessage;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;

import java.util.HashMap;
import java.util.Map;

@GmtHandlerAnnotation(handler = "ClubGmtCreateNewClubHandler")
public class ClubGmtCreateNewClubHandler extends ClubGmtBaseHandler
{

	@Override
	public HashMap<String, Object> getResult(KodHttpMessage msg)
	{
		HashMap<String, Object> result = new HashMap<>();
		Map<String, Object> args = this.getJson(msg);
		if (args == null) {
			result.put("result", -1);
			return result;
		}
		result.put("result", 1);
		int roleId = (int)args.get("roleId");
		int agentId = (int)args.get("agentId");
		ClubGmToolsService gmtService= ServiceContainer.getInstance().getPublicService(ClubGmToolsService.class);
		int agentValid = gmtService.checkAgentValid(agentId);
		if (agentValid < ClubConstants.CLUB_GMT_ERROR.SUCCESS) {
			int data = agentValid;
			result.put("data", data);
		} else if (roleId <= 0) {
			int data = ClubConstants.CLUB_GMT_ERROR.CREATE_CLUB_ERROR_ROLEID_INVALID;
			result.put("data", data);
		} else {
			// agentId 的检查过了，那么去game请求检查roleId
			ClubGmToolsCheckCreateService gmtCheckService = ServiceContainer.getInstance().getPublicService(ClubGmToolsCheckCreateService.class);
			gmtCheckService.sendCheckRoleRequest(args, msg);
			result.put("sync", HttpReturnContent.NO_RETURN);
		}
		return result;
	}

}
