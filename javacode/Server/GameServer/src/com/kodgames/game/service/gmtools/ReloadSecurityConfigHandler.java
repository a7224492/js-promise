package com.kodgames.game.service.gmtools;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.common.Constant;
import com.kodgames.game.service.security.SecurityGroupConfig;
import com.kodgames.game.service.security.SecurityService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liufei on 2017/5/8.
 * 重新加载SecurityGroup的配置文件
 */
@GmtHandlerAnnotation(handler = "ReloadSecurityConfigHandler")
public class ReloadSecurityConfigHandler implements IGmtoolsHandler {
    @Override
    public HashMap<String, Object> getResult(Map<String, Object> json)
    {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", 1);
        String content = (String)json.get("content");
        SecurityGroupConfig.getInstance().reload(content, null);

        // 更新zdb表
        table.String_tables.delete(Constant.StringTablesKey.KEY_SECURITY_CONFIG);
        table.String_tables.insert(Constant.StringTablesKey.KEY_SECURITY_CONFIG, content);

        // 返回安全组名称列表
        SecurityService service = ServiceContainer.getInstance().getPublicService(SecurityService.class);
        result.put("data", service.getGroups());
        return result;
    }
}
