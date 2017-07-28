package com.kodgames.game.service.gmtools;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.security.SecurityService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liufei on 2017/5/8.
 * 查询安全组名称列表
 */
@GmtHandlerAnnotation(handler = "QuerySecurityGroupsHandler")
public class QuerySecurityGroupsHandler implements IGmtoolsHandler {
    @Override
    public HashMap<String, Object> getResult(Map<String, Object> json)
    {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", 1);
        SecurityService service = ServiceContainer.getInstance().getPublicService(SecurityService.class);
        result.put("data", service.getGroups());
        return result;
    }
}
