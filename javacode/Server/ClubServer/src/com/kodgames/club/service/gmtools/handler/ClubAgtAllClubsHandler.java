package com.kodgames.club.service.gmtools.handler;

import com.kodgames.club.service.gmtools.ClubGmtBaseHandler;
import com.kodgames.club.service.gmtools.ClubGmToolsService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.httpserver.KodHttpMessage;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import xbean.ClubInfo;
import xbean.MemberInfo;

import java.util.*;

@GmtHandlerAnnotation(handler = "ClubAgtAllClubsHandler")
public class ClubAgtAllClubsHandler extends ClubGmtBaseHandler
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
		int agentId = (int)args.get("agentId");
		ClubGmToolsService gmtService= ServiceContainer.getInstance().getPublicService(ClubGmToolsService.class);
		Set<Integer> clubIds = new HashSet<>();
		if (agentId > 0)
		{
			clubIds.addAll(gmtService.getClubsFromAgent(agentId));
		}
		ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();
		List<ClubInfo> clubs = gmtService.getAllClub(clubIds);
		clubs.forEach( c -> {
			HashMap<String, Object> map = new HashMap<>();
			map.put("clubId", c.getClubId());
			map.put("clubName", c.getClubName());
			ArrayList<HashMap<String, Object>> memberList = new ArrayList<>();
			List<MemberInfo> members = c.getMembers();
			for (MemberInfo member : members) {
				HashMap<String, Object> m = new HashMap<>();
				m.put("roleId", member.getRole().getRoleId());
				m.put("name", member.getRole().getName());
				m.put("cardCount", member.getCardCount());
				memberList.add(m);
			}
			map.put("members", memberList);
			dataList.add(map);
		});
		result.put("data", dataList);
		return result;
	}

}
