package com.kodgames.game.service.security;

import java.util.List;

import qqzeng.ip.IpInfo;

/**
 * Created by Liufei on 2017/5/6.
 */
public class SecurityGroup {
    private List<SecurityCondition> securityConditions = null;

    // 组名
    private String groupName = "invalid";
    // 是否启用该组
    private boolean isActive = true;

    /**
     * 检查给定的信息是否满足该组的所有条件
     * @param registDays 注册天数
     * @param combat7Days 最近7天的局数
     * @param combatToday 当天战斗局数
     * @param combatTotal 战斗总局数
     * @param agencyId 最后购买房卡的代理商id
     * @param regionName ip所在地区
     * @param playerId 玩家id
     */
    public boolean matchGroup(int registDays, int combat7Days, int combatToday, int combatTotal, int agencyId, IpInfo ipInfo, int playerId)
    {
        // 满足所有条件才算满足该组
        for (SecurityCondition condition : securityConditions)
        {
            if (!condition.matchCondition(registDays, combat7Days, combatToday, combatTotal, agencyId, ipInfo, playerId))
                return false;
        }

        return true;
    }

    // 组名
    public void setName(String name)
    {
        groupName = name;
    }

    public String getName()
    {
        return groupName;
    }

    // 是否开启
    public void setActive(boolean active)
    {
        isActive = active;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setConditions(List<SecurityCondition> conditions)
    {
        securityConditions = conditions;
    }
}
