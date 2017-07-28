package com.kodgames.game.service.gmtools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.game.start.WxPromoterConfig;
import com.kodgames.game.util.TimeUtil;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import xbean.PromoterInfo;
import xbean.ReceiveRewardInfo;
import xbean.RoleInfo;

/**
 * Created by 001 on 2017/4/12.
 * 查询提现 GMT
 */
@GmtHandlerAnnotation(handler = "QueryPromoterRewardListGMT")
public class QueryPromoterRewardListGMT implements IGmtoolsHandler
{
    private static final Logger logger = LoggerFactory.getLogger(QueryPromoterRewardListGMT.class);

    @Override
    public HashMap<String, Object> getResult(Map<String, Object> args)
    {
        int promoterRoleId = (int)args.get("promoter");
        int allRecords = (int)args.get("allRecords");
        
        boolean isAll = (allRecords == 1);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", 1);

        HashMap<String, Object> data = new HashMap<>();
        RoleInfo roleInfo = table.Role_info.select(promoterRoleId);
        if (roleInfo == null || roleInfo.getUnionid().isEmpty())
        {
            logger.warn("can't get unionId for promoter {}", promoterRoleId);
            data.put("remainRewards", 0);
            data.put("records", null);
            result.put("data", data);
            return result;
        }
        String promoterUnionId = roleInfo.getUnionid();

        WxPromoterConfig config = WxPromoterConfig.getInstance();
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        PromoterInfo promoterInfo = table.Promoter_info.select(promoterUnionId);
        if (promoterInfo == null)
        {
            logger.debug("can't find promoter info: {}", promoterUnionId);
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

                // 过滤已处理的记录
                if (!isAll && info.getIsHandled() == 1)
                    continue;

                Map<String, Object> d = new HashMap<>();
                d.put("id", id);
                d.put("date", TimeUtil.timeString(info.getDate(), "yyyy-MM-dd"));
                d.put("time", TimeUtil.timeString(info.getDate(), "HH:mm"));
                if (config.getRewardType() == WxPromoterConfig.RewardType.Cash)
                {
                    d.put("diamond", 0);
                    d.put("cash", info.getReceivedThisTime());
                    d.put("totalDiamond", 0);
                    d.put("totalCash", info.getReceivedTotal());
                }
                else
                {
                    d.put("diamond", info.getReceivedThisTime());
                    d.put("cash", 0);
                    d.put("totalDiamond", info.getReceivedTotal());
                    d.put("totalCash", 0);
                }
                d.put("status", info.getIsHandled());

                listMap.add(d);
            }
        }
        data.put("records", listMap);
        result.put("data", data);

        return result;
    }
}