package com.kodgames.authserver.service.gmtools;

import com.kodgames.authserver.service.account.WhiteListService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liufei on 2017/7/10.
 * 查询白名单开启状态
 */
@GmtHandlerAnnotation(handler = "QueryWhiteListEnableHander")
public class QueryWhiteListEnableHander implements IGmtoolsHandler {
    private static final Logger logger = LoggerFactory.getLogger(QueryWhiteListEnableHander.class);

    // 返回: data 0关闭 1开启
    public HashMap<String, Object> getResult(Map<String, Object> args) {
        HashMap<String, Object> result = new HashMap<>();
        WhiteListService service = ServiceContainer.getInstance().getPublicService(WhiteListService.class);
        result.put("result", 1);
        result.put("data", service.getEnabled() ? "1" : "0");
        return result;
    }
}
