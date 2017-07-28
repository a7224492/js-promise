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
 * 添加白名单账号
 */
@GmtHandlerAnnotation(handler = "AddWhiteListAccountHandler")
public class AddWhiteListAccountHandler implements IGmtoolsHandler {
    private static final Logger logger = LoggerFactory.getLogger(AddWhiteListAccountHandler.class);

    // 参数: account 玩家id
    // 返回: data 0失败 1成功
    public HashMap<String, Object> getResult(Map<String, Object> args) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", 1);

        String key = "account";
        if (!args.containsKey(key)) {
            logger.warn("can't find account in param list");
            result.put("data", 0);
            return result;
        }

        String value = "";
        Integer id;
        try {
            value = (String) args.get(key);
            id = Integer.valueOf(value);
        } catch (ClassCastException e) {
            id = (Integer) args.get(key);
        } catch (Exception e) {
            logger.warn("invalid account id {}", value);
            result.put("data", 0);
            return result;
        }

        WhiteListService service = ServiceContainer.getInstance().getPublicService(WhiteListService.class);
        boolean ret = service.addAccount(id);

        result.put("data", ret ? "1" : "0");
        return result;
    }
}
