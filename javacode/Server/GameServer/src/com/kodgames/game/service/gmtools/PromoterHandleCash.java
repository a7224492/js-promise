package com.kodgames.game.service.gmtools;

import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.PromoterInfo;
import xbean.ReceiveRewardInfo;
import xbean.RoleInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 001 on 2017/4/12.
 * 发放提现款 GMT
 */
@GmtHandlerAnnotation(handler = "PromoterHandleCash")
public class PromoterHandleCash implements IGmtoolsHandler
{
    private static final Logger logger = LoggerFactory.getLogger(PromoterHandleCash.class);

    @Override
    public HashMap<String, Object> getResult(Map<String, Object> args)
    {
        String promoterStr = (String)args.get("promoter");
        String idStr = (String)args.get("id");
        String countStr = (String)args.get("count");
        String gmName = (String)args.get("gm");
        if (promoterStr == null || idStr == null || countStr == null || gmName == null)
        {
            logger.debug("promoter should not be null");
            return responseErrorResult(1);
        }

        int promoterRoleId = 0;
        long id = 0;
        try
        {
            promoterRoleId = Integer.parseInt(promoterStr);
            id = Long.parseLong(idStr);
        }
        catch (NumberFormatException e)
        {
            logger.warn("invalid promoterStr {} or idStr {} or countStr {}, exception={}", promoterStr, idStr, countStr, e.getMessage());
            return responseErrorResult(1);
        }

        RoleInfo roleInfo = table.Role_info.select(promoterRoleId);
        if (roleInfo == null)
        {
            logger.debug("can't find role info: {}", promoterStr);
            return responseErrorResult(2);
        }

        PromoterInfo promoterInfo = table.Promoter_info.select(roleInfo.getUnionid());
        if (promoterInfo == null)
        {
            logger.debug("can't find promoter info: {}", roleInfo.getUnionid());
            return responseErrorResult(2);
        }

        // id必须在promoter的记录列表中
        if (!promoterInfo.getRewardList().contains(id))
        {
            logger.debug("record id {} not exist in promoter {} recoreList", id, promoterRoleId);
            return responseErrorResult(3);
        }

        ReceiveRewardInfo rewardInfo = table.Receive_reward_info.update(id);
        if (rewardInfo == null)
        {
            logger.debug("can't find received_reward_info {}", id);
            return responseErrorResult(4);
        }

        if (rewardInfo.getIsHandled() == 1)
        {
            logger.debug("record already handled by {}", rewardInfo.getGmName());
            return responseErrorResult(5);
        }

        rewardInfo.setIsHandled(1);
        rewardInfo.setHandleTime(System.currentTimeMillis());
        rewardInfo.setGmName(gmName);

        return responseErrorResult(0);
    }

    private HashMap<String, Object> responseErrorResult(int code)
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", 1);
        result.put("data", code);
        return result;
    }
}
