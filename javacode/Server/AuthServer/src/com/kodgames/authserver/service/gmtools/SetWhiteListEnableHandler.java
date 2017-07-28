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
 * 开启或关闭白名单功能
 */
@GmtHandlerAnnotation(handler = "SetWhiteListEnableHandler")
public class SetWhiteListEnableHandler implements IGmtoolsHandler {
    private static final Logger logger = LoggerFactory.getLogger(SetWhiteListEnableHandler.class);

    // 参数: enabled: 0关闭 1打开
    // 返回: data 0失败 1成功
    public HashMap<String, Object> getResult(Map<String, Object> args) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", 1);

        String key = "enabled";
        if (!args.containsKey(key)) {
            result.put("data", 0);
            return result;
        }

        Boolean enabled;
        try {
            String value = (String) args.get(key);
            enabled = !value.equalsIgnoreCase("0");
        } catch (ClassCastException e) {
            Integer value = (Integer) args.get(key);
            enabled = value == 1;
        }

        WhiteListService service = ServiceContainer.getInstance().getPublicService(WhiteListService.class);
        service.setEnabled(enabled);
        logger.info("set white list enabled to {}", enabled);

        result.put("data", 1);
        return result;
    }
}
