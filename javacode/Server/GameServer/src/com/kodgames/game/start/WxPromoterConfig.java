package com.kodgames.game.start;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import limax.util.XMLUtils;

/**
 * Created by 001 on 2017/4/11.
 * 微信推广员配置
 */
public class WxPromoterConfig
{
    private static Logger logger = LoggerFactory.getLogger(WxPromoterConfig.class);
    private static WxPromoterConfig instance = new WxPromoterConfig();

    private static final String XML_ACTIVITYCONFIG = "ActivityConfig";
    private static final String XML_INVITEE = "InviteeConfig";
    private static final String XML_PROMOTER = "PromoterConfig";
    private static final String XML_GETREWARD = "GetRewardConfig";

    private static final String XML_ACTIVITY_REWARDTYPE = "rewardType";
    private static final String XML_ACTIVITY_WCPUBLICACCOUNT = "wechatPublicAccount";
    private static final String XML_ACTIVITY_START = "StartTime";
    private static final String XML_ACTIVITY_END = "EndTime";

    private static final String REWARDTYPE_DIAMOND = "Diamond";
    private static final String REWARDTYPE_CASH = "Cash";

    private static final String XML_INVITEE_GAMECOUNT = "gameCountForActivity";

    private static final String XML_PROMOTER_NEEDTIME = "needRegisteredTime";
    private static final String XML_PROMOTER_REWARDLIMIT = "rewardLimitPerCycle";
    private static final String XML_PROMOTER_HOURSPERCYCLE = "hoursPerCycle";

    private static final String XML_PROMOTER_REWARDITEM = "Reward";
    private static final String XML_PROMOTER_INVITEECOUNT = "inviteeCount";
    private static final String XML_PROMOTER_REWARDCOUNT = "rewardCount";

    private static final String XML_GETREWARD_GMWEIXIN = "gmWeixin";
    private static final String XML_GETREWARD_MINGET = "minGet";
    private static final String XML_GETREWARD_MAXGET = "maxGet";
    private static final String XML_GETREWARD_MAXCOUNT = "maxCount";

    // 活动时间配置
    private long activityStartTime = 0;
    private long activityEndTime = 0;

    // 奖励类型
    public enum RewardType
    {
        Diamond,                // 钻石
        Cash                   // 现金
    }
    private RewardType rewardType = RewardType.Diamond;
    private String wechatPublicAccount = "";

    // 被邀请人任务完成条件配置
    private int gameCountForActivity = 1;               // 客户端进行n局游戏后完成被邀请人任务

    // 推广员奖励配置
    public class RewardInfo
    {
        public int inviteeCount = 0;                    // 每推广X玩家
        public float rewardCount = 0;                   // 可以获取n的奖励
    }
    private List<RewardInfo> rewardsInfo = new ArrayList<>();
    private int promoterNeedTime = 5;                   // 推广员需要满足的注册天数
    private float rewardLimitPerCycle = 25.0f;          // 每个周期内可领取的奖励上限
    private int hoursPerCycle = 24;                     // 每个周期的时间 (小时)

    // 领取奖励配置
    private String gmWeixin = "xxxxxx";                 // GM微信
    private float minGetReward = 1.0f;                  // 最小领奖数
    private float maxGetReward = 200.0f;                // 最大领奖数
    private int maxGetCount = 3;                        // 每天最多领奖次数

    private WxPromoterConfig()
    {
    }

    public static WxPromoterConfig getInstance()
    {
        return instance;
    }

    /**
     * 活动开始时间
     * @return
     */
    public long getStartTime()
    {
        return activityStartTime;
    }

    public void setStartTime(long t)
    {
        activityStartTime = t;
    }

    /**
     * 活动结束时间
     * @return
     */
    public long getEndTime()
    {
        return activityEndTime;
    }

    public void setEndTime(long t)
    {
        activityEndTime = t;
    }

    /**
     * 奖励类型 (不允许重设)
     * @return
     */
    public RewardType getRewardType()
    {
        return rewardType;
    }

    /**
     * 微信公众号
     * @return
     */
    public String getWechatPublicAccount()
    {
        return wechatPublicAccount;
    }

    public void setWechatPublicAccount(String value)
    {
        wechatPublicAccount = value;
    }

    /**
     * 被邀请人完成任务需要完成的游戏局数
     * @return
     */
    public int getGameCountForActivity()
    {
        return gameCountForActivity;
    }

    public void setGameCountForActivity(int value)
    {
        gameCountForActivity = value;
    }

    /**
     * 推广员需要满足的注册天数
     * @return
     */
    public int getPromoterNeedTime()
    {
        return promoterNeedTime;
    }

    public void setPromoterNeedTime(int value)
    {
        promoterNeedTime = value;
    }

    /**
     * 发送奖励的GM微信号
     * @return
     */
    public String getGMWeixin()
    {
        return gmWeixin;
    }

    public void setGmWeixin(String wx)
    {
        gmWeixin = wx;
    }

    /**
     * 最少领取奖励数额
     * @return
     */
    public float getMinGetReward()
    {
        return minGetReward;
    }

    public void setMinGetReward(float value)
    {
        minGetReward = value;
    }

    /**
     * 最大领取奖励数额
     * @return
     */
    public float getMaxGetReward()
    {
        return maxGetReward;
    }

    public void setMaxGetReward(float value)
    {
        maxGetReward = value;
    }

    /**
     * 每天最多领取的次数
     * @return
     */
    public int getMaxGetCount()
    {
        return maxGetCount;
    }

    public void setMaxGetCount(int count)
    {
        maxGetCount = count;
    }

    /**
     * 根据被邀请人数量返回奖励数量
     * 注意:
     *    由于配置文件中被邀请人数量并不连续, 所以不是每次增加被邀请人都会有奖励, 只有达到下一级时才会有奖励
     *    所以这里仅根据被邀请人数量等于配置项中的每个条件时才返回有奖励
     *
     *    要求: 每次增加被邀请人时都需要判断并增加奖励, 否则会跳过奖励
     *
     * @param inviteeCount
     * @return
     */
    public float GetRewardCount(int inviteeCount)
    {
        for (int i = 0; i < rewardsInfo.size(); i++)
        {
            if (inviteeCount == rewardsInfo.get(i).inviteeCount)
                return rewardsInfo.get(i).rewardCount;
        }
        return 0;
    }

    /**
     * 返回当前的奖励是第几档, 用于记录BI日志
     * @param inviteeCount
     * @return 0无奖励
     *         1-6 钻石奖励的六档
     *         7 现金奖励
     */
    public int GetRewardCode(int inviteeCount)
    {
        for (int i = 0; i < rewardsInfo.size(); i++)
        {
            if (inviteeCount == rewardsInfo.get(i).inviteeCount)
            {
                if (rewardType == RewardType.Diamond)
                    return i + 1;
                else
                    return 7;
            }
        }
        return 0;
    }

    /**
     * 每个周期最多奖励数额
     * @return
     */
    public float getRewardLimitPerCycle()
    {
        return rewardLimitPerCycle;
    }

    public void setRewardLimitPerCycle(float value)
    {
        rewardLimitPerCycle = value;
    }

    /**
     * 每个周期的时间 (小时)
     * @return
     */
    public int getHoursPerCycle()
    {
        return hoursPerCycle;
    }

    public void setHoursPerCycle(int value)
    {
        hoursPerCycle = value;
    }

    /**
     * 加载配置文件
     * @param fileName
     */
    public void load(String fileName)
    {
        try
        {
            Element element = XMLUtils.getRootElement(fileName);
            loadImpl(element);
        }
        catch (Exception e)
        {
            logger.error("load wxpromoter config failed, exception = {}", e.getMessage());
        }
    }

    /**
     * 重新加载配置文件, 提供给GMT工具用于刷新配置
     * @param content
     */
    public void reload(String content)
    {
        InputStream is = new ByteArrayInputStream(content.getBytes());
        try
        {
            Element element = XMLUtils.getRootElement(is);
            loadImpl(element);
        }
        catch (Exception e)
        {
            logger.error("reload wxpromoter config failed, exception = {}", e.getMessage());
        }
    }

    private void loadImpl(Element element)
    {
        XMLUtils.getChildElements(element).forEach(subElem -> {
            if (subElem.getTagName().equals(XML_ACTIVITYCONFIG))
                parseActivityConfig(subElem);
            else if (subElem.getTagName().equals(XML_INVITEE))
                parseInviteeConfig(subElem);
            else if (subElem.getTagName().equals(XML_PROMOTER))
                parsePromoterConfig(subElem);
            else if (subElem.getTagName().equals(XML_GETREWARD))
                parseGetRewardConfig(subElem);
            else
                logger.error("invalid xml element {} for WxPromoterConfig.xml", subElem.getTagName());
        });
    }

    private void parseActivityConfig(Element element)
    {
        String value = element.getAttribute(XML_ACTIVITY_REWARDTYPE);
        if (value.equals(REWARDTYPE_DIAMOND))
            rewardType = RewardType.Diamond;
        else if (value.equals(REWARDTYPE_CASH))
            rewardType = RewardType.Cash;
        else
        {
            logger.error("invalid reward type {}", value);
            return;
        }

        wechatPublicAccount = element.getAttribute(XML_ACTIVITY_WCPUBLICACCOUNT);

        NodeList nodes = element.getChildNodes();
        if(nodes.getLength() < 2)
        {
            logger.error("invalid activity time config");
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            try
            {
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;

                Element e = (Element)node;
                if (node.getNodeName() == XML_ACTIVITY_START)
                    activityStartTime = dateFormat.parse(e.getAttribute("time")).getTime();
                else if (node.getNodeName() == XML_ACTIVITY_END)
                    activityEndTime = dateFormat.parse(e.getAttribute("time")).getTime();
                else
                    logger.error("invalid weixin promoter config for element {}", node.getNodeName());
            }
            catch (ParseException e)
            {
                logger.warn("weixin promoter activity time format error, exception={}", e.getMessage());
                return;
            }
        }
    }

    private void parseInviteeConfig(Element element)
    {
        String value = "";
        try
        {
            value = element.getAttribute(XML_INVITEE_GAMECOUNT);
            gameCountForActivity = Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            logger.error("invalid XML_INVITEE_GAMECOUNT for value {}, exception={}", value, e.getMessage());
        }
    }

    private void parsePromoterConfig(Element element)
    {
        String value = "";
        try
        {
            value = element.getAttribute(XML_PROMOTER_NEEDTIME);
            promoterNeedTime = Integer.parseInt(value);

            value = element.getAttribute(XML_PROMOTER_REWARDLIMIT);
            rewardLimitPerCycle = Float.parseFloat(value);

            value = element.getAttribute(XML_PROMOTER_HOURSPERCYCLE);
            hoursPerCycle = Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            logger.error("invalid value for promoter config {}, exception={}", value, e.getMessage());
            return;
        }

        NodeList nodes = element.getChildNodes();
        if (nodes.getLength() == 0)
        {
            logger.error("can't find Reward entry in promoter config");
            return;
        }
        
        rewardsInfo.clear();
        
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node n = nodes.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE)
                continue;

            if (n.getNodeName() != XML_PROMOTER_REWARDITEM)
            {
                logger.error("invalid element {} in promoter config", n.getNodeName());
                continue;
            }

            Element e = (Element)n;
            try
            {
                value = e.getAttribute(XML_PROMOTER_INVITEECOUNT);
                int inviteeCount = Integer.parseInt(value);

                value = e.getAttribute(XML_PROMOTER_REWARDCOUNT);
                float rewardCount = Float.parseFloat(value);

                RewardInfo info = new RewardInfo();
                info.rewardCount = rewardCount;
                info.inviteeCount = inviteeCount;
                rewardsInfo.add(info);
            }
            catch (NumberFormatException er)
            {
                logger.error("invalid value for promoter config {}, exception={}", value, er.getMessage());
                return;
            }
        }
    }

    private void parseGetRewardConfig(Element element)
    {
        String value = "";
        try
        {
            gmWeixin = element.getAttribute(XML_GETREWARD_GMWEIXIN);

            value = element.getAttribute(XML_GETREWARD_MINGET);
            minGetReward = Float.parseFloat(value);

            value = element.getAttribute(XML_GETREWARD_MAXGET);
            maxGetReward = Float.parseFloat(value);

            value = element.getAttribute(XML_GETREWARD_MAXCOUNT);
            maxGetCount = Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            logger.error("invalid getreward config for value {}, exception={}", value, e.getMessage());
        }
    }
}
