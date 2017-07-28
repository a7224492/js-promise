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
 * 查询当前可提现额
 */
@GmtHandlerAnnotation(handler = "QueryPromoterRewards")
public class QueryPromoterRewards implements IGmtoolsHandler
{
    private static final Logger logger = LoggerFactory.getLogger(QueryPromoterRewards.class);

    @Override
    public HashMap<String, Object> getResult(Map<String, Object> args)
    {
        String promoter = (String)args.get("promoter");
        if (promoter == null)
        {
            logger.debug("promoter should not be null");
            return responseErrorResult();
        }

        WeiXinShareActivityService service = ServiceContainer.getInstance().getPublicService(WeiXinShareActivityService.class);
        WxPromoterConfig config = WxPromoterConfig.getInstance();
        if (!service.isActive())
        {
            logger.debug("not in weixin share activity time");
            return responseResult(0, 0, config.getMaxGetCount(), config.getMaxGetCount(), 0, config.getMaxGetReward(), config.getMinGetReward());
        }

        PromoterInfo promoterInfo = table.Promoter_info.select(promoter);
        if (promoterInfo == null)
        {
            logger.debug("can't find promoter info: {}", promoter);
            return responseResult(0, 0, config.getMaxGetCount(), config.getMaxGetCount(), 0, config.getMaxGetReward(), config.getMinGetReward());
        }

        boolean canTakeReward = service.canTakeReward(promoter);
        int leftCountToday = config.getMaxGetCount() - service.GetRewardCountToday(promoter);

        return responseResult(
                promoterInfo.getUnreceivedRewards(),
                promoterInfo.getTotalRewards(),
                leftCountToday,
                config.getMaxGetCount(),
                canTakeReward ? 1 : 0,
                config.getMaxGetReward(),
                config.getMinGetReward()
        );
    }

    private HashMap<String, Object> responseResult(float activeRewards, float totalRewards, int leftCountToday,
        int maxCountPerDay, int canTakeReward, float maxRewardPerDay, float minReward)
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", 1);

        HashMap<String, Object> data = new HashMap<>();
        data.put("activeRewards", activeRewards);
        data.put("totalRewards", totalRewards);
        data.put("leftCountToday", leftCountToday);
        data.put("maxCountPerDay", maxCountPerDay);
        data.put("canTakeReward", canTakeReward);
        data.put("maxRewardPerDay", maxRewardPerDay);
        data.put("minReward", minReward);

        result.put("data", data);
        return result;
    }

    private HashMap<String, Object> responseErrorResult()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", 1);
        result.put("data", null);
        return result;
    }
}
