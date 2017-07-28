package com.kodgames.game.service.gmtools;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.activity.WeiXinShareActivityService;
import com.kodgames.game.start.WxPromoterConfig;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.PromoterInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 001 on 2017/4/12.
 * 发起提现请求
 */
@GmtHandlerAnnotation(handler = "PromoterGetReward")
public class PromoterGetReward implements IGmtoolsHandler
{
    private static Logger logger = LoggerFactory.getLogger(PromoterGetReward.class);
    @Override
    public HashMap<String, Object> getResult(Map<String, Object> args)
    {
        String promoter = (String)args.get("promoter");
        if (promoter == null)
        {
            logger.debug("promoter should not be null");
            return responseErrorResult(1);
        }

        WeiXinShareActivityService service = ServiceContainer.getInstance().getPublicService(WeiXinShareActivityService.class);
        if (!service.isActive())
        {
            logger.debug("not in weixin activity time");
            return responseErrorResult(5);
        }

        WxPromoterConfig config = WxPromoterConfig.getInstance();
        PromoterInfo promoterInfo = table.Promoter_info.select(promoter);
        if (promoterInfo == null)
        {
            logger.debug("can't find promoter info: {}", promoter);
            return responseErrorResult(2);
        }


        boolean canTakeReward = service.canTakeReward(promoter);
        if (!canTakeReward)
        {
            logger.debug("can't take reward for promoter {}", promoter);
            return responseErrorResult(3);
        }

        if (!service.takeReward(promoter))
        {
            logger.debug("get reward failed for promoter {}", promoter);
            return responseErrorResult(4);
        }

        return responseResult(config.getGMWeixin(), promoterInfo.getUnreceivedRewards(), promoterInfo.getTotalRewards());
    }

    private HashMap<String, Object> responseResult(String gmWeixin, float leftRewards, float totalRewards)
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", 1);

        HashMap<String, Object> data = new HashMap<>();
        data.put("result", 0);
        data.put("gmWeixin", gmWeixin);
        data.put("leftRewards", leftRewards);
        data.put("totalRewards", totalRewards);

        result.put("data", data);
        return result;
    }

    private HashMap<String, Object> responseErrorResult(int data)
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", 1);

        HashMap<String, Object> inner = new HashMap<>();
        inner.put("result", data);

        result.put("data", inner);
        return result;
    }
}
