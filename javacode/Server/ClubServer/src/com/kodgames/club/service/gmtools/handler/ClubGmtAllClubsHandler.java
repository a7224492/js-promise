package com.kodgames.club.service.gmtools.handler;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.gmtools.ClubGmToolsService;
import com.kodgames.club.service.gmtools.ClubGmtBaseHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.httpserver.KodHttpMessage;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import xbean.ClubInfo;
import xbean.MemberInfo;

import java.util.*;

@GmtHandlerAnnotation(handler = "ClubGmtAllClubsHandler")
public class ClubGmtAllClubsHandler extends ClubGmtBaseHandler
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
		int clubId = (int)args.get("clubId");
		int managerId = (int)args.get("managerId");
		int agentId = (int)args.get("agentId");
		ClubGmToolsService gmtService= ServiceContainer.getInstance().getPublicService(ClubGmToolsService.class);
		Set<Integer> clubIds = new HashSet<>();
		if (agentId > 0)
		{
			clubIds.addAll(gmtService.getClubsFromAgent(agentId));
		}
		if (managerId > 0)
		{
			clubIds.addAll(gmtService.getClubsFromManager(managerId));
		}
		if (clubId > 0)
		{
			clubIds.add(clubId);
		}
		ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();
		List<ClubInfo> clubs = gmtService.getAllClub(clubIds);
		clubs.forEach( c -> {
			HashMap<String, Object> map = new HashMap<>();
			map.put("clubId", c.getClubId());
			map.put("agentId", c.getAgentId());
			map.put("managerId", c.getManager().getRoleId());
			map.put("clubName", c.getClubName());
			map.put("memberCount", c.getMemberCount());
			map.put("totalGameCount", c.getGameCount());
			map.put("todayGameCount", c.getTodayGameCount());
			int totalCards = 0;
			List<MemberInfo> members = c.getMembers();
			for (MemberInfo member : members) {
				if (member.getStatus() != ClubConstants.CLUB_MEMBER_STATUS.LEAVE_OUT)
					totalCards += member.getCardCount();
			}
			map.put("totalCardCount", totalCards);
			map.put("status", c.getStatus());

			dataList.add(map);
		});
		result.put("data", dataList);
		return result;
	}

}
