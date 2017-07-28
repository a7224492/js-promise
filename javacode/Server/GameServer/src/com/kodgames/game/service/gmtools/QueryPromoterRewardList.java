package com.kodgames.game.service.gmtools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.activity.WeiXinShareActivityService;
import com.kodgames.game.util.TimeUtil;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import xbean.PromoterInfo;
import xbean.ReceiveRewardInfo;

/**
 * Created by 001 on 2017/4/12.
 * 查询账单
 */
@GmtHandlerAnnotation(handler = "QueryPromoterRewardList")
public class QueryPromoterRewardList implements IGmtoolsHandler
{
    private static final Logger logger = LoggerFactory.getLogger(QueryPromoterRewardList.class);

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
        if (!service.isActive())
        {
            logger.debug("not in weixin activity time");
            return responseErrorResult();
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", 1);

        HashMap<String, Object> data = new HashMap<>();
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        PromoterInfo promoterInfo = table.Promoter_info.select(promoter);
        if (promoterInfo == null)
        {
            logger.debug("can't find promoter info: {}", promoter);
            data.put("remainRewards", 0);
        }
        else
        {
            data.put("remainRewards", promoterInfo.getUnreceivedRewards());

            List<Long> recordList = promoterInfo.getRewardList();
            for (Long id : recordList)
            {
                ReceiveRewardInfo info = table.Receive_reward_info.select(id);
                if (info == null)
                {
                    logger.warn("can't find received_reward_info for {}", id);
                    continue;
                }

                Map<String, Object> d = new HashMap<>();
                d.put("dateTime", TimeUtil.timeString(info.getDate(), "yyyy-MM-dd HH:mm"));
                d.put("getCount", info.getReceivedThisTime());
                d.put("totalRewards", info.getReceivedTotal());

                listMap.add(d);
            }
        }
        data.put("records", listMap);
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
