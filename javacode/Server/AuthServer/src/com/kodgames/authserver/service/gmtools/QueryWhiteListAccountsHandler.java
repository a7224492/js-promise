package com.kodgames.authserver.service.gmtools;

import com.kodgames.authserver.service.account.WhiteListService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liufei on 2017/7/10.
 * 查询白名单玩家列表
 */
@GmtHandlerAnnotation(handler = "QueryWhiteListAccountsHandler")
public class QueryWhiteListAccountsHandler implements IGmtoolsHandler {
    private static final Logger logger = LoggerFactory.getLogger(QueryWhiteListAccountsHandler.class);

    // 返回: {accountId} 账号列表
    public HashMap<String, Object> getResult(Map<String, Object> args) {
        HashMap<String, Object> result = new HashMap<>();

        WhiteListService service = ServiceContainer.getInstance().getPublicService(WhiteListService.class);
        List<Integer> list = service.getList();
        result.put("result", 1);
        result.put("data", list);

        return result;
    }
}
