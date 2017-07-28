package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

//http://localhost:13101/gmtools?{"handler":"QueryPlayerIDExistHandler","playerID":0,"server_id":16842753}

/**
 * 代理商绑定用户ID时，GMT验证用户ID是否存在
 * 
 * @author 张斌
 */
@GmtHandlerAnnotation(handler = "QueryPlayerIDExistHandler")
public class QueryPlayerIDExistHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(QueryPlayerIDExistHandler.class);
	
	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		int roleId = 0;
		result.put("result", 1);
		result.put("data", 1);
		try
		{
			 roleId = (int)args.get("playerID");
		}
		catch (Throwable t)
		{
			logger.error("QueryPlayerIDExistHandler args error : {}", args);
			result.put("data", -1);
			
			return result;
		}
		
		RoleService roleService= ServiceContainer.getInstance().getPublicService(RoleService.class);
		if(roleService.getRoleInfoByRoleId(roleId) == null)
		{
			result.put("data", -2);
			return result;
		}
	
		return result;
	}
}
