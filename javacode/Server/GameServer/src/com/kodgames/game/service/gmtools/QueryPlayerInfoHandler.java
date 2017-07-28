package com.kodgames.game.service.gmtools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import xbean.ForbidRole;
import xbean.RoleInfo;

@GmtHandlerAnnotation(handler = "QueryPlayerInfoHandler")
public class QueryPlayerInfoHandler implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		int roleId = (int)args.get("playerID");
		RoleService roleService= ServiceContainer.getInstance().getPublicService(RoleService.class);
		GlobalService globalService= ServiceContainer.getInstance().getPublicService(GlobalService.class);
		RoleInfo role = roleService.getRoleInfoByRoleId(roleId);
		ForbidRole forbid = globalService.getForbidInfoByRoleId(roleId);
		long forbidTime = (null == forbid) ? 0 : forbid.getForbidTime();
		ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String,String>>();	
		if(role != null)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", Integer.toString(roleId));
			map.put("account", role.getNickname());
			map.put("platform", role.getChannel());
			map.put("card", Integer.toString(role.getCardCount()));
			map.put("points", Integer.toString(role.getPoints()));
			map.put("total", Integer.toString(role.getTotalGameCount()));
			SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			map.put("forbid_time", time.format(forbidTime));
			map.put("create_time", time.format(role.getRoleCreateTime()));
			dataList.add(map);
		}	
		result.put("data", dataList);
		return result;
	}

}
