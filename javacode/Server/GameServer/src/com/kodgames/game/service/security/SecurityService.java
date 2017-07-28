package com.kodgames.game.service.security;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.DateTimeConstants;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.game.common.Constant;

import limax.zdb.Procedure;
import qqzeng.ip.IpInfo;
import qqzeng.ip.IpSearch;
import xbean.CombatInfo;
import xbean.RoleInfo;
import xbean.RoleRecord;

/**
 * Created by Liufei on 2017/5/6.
 */
public class SecurityService extends PublicService {
    private static final long serialVersionUID = -9071999897030710867L;
    private static Logger logger = LoggerFactory.getLogger(SecurityService.class);

    // 统计最近7天的总局数
    public static final int COMBAT_DAYS = 7;

    private List<SecurityGroup> securityGroups = null;
    private boolean isOpen = true;
    private String defaultGroupName = "undefined";

    // 用于保护securityGroups的锁
    private ReentrantReadWriteLock groupLock = new ReentrantReadWriteLock();
    private Lock rLock = groupLock.readLock();
    private Lock wLock = groupLock.writeLock();

    @Override
    public int init()
    {
        try {
            Procedure.call(() -> {
                String content = table.String_tables.select(Constant.StringTablesKey.KEY_SECURITY_CONFIG);
                if (content == null)
                {
                    logger.warn("security group configuration is not defined in ZDB, use xml file");
                    SecurityGroupConfig.getInstance().load(Object.class.getResource("/securitygroups.xml").getPath(), this);
                }
                else
                {
                    SecurityGroupConfig.getInstance().reload(content, this);
                }
                return true;
            });
        } catch (Exception e)
        {
            logger.error("failed to load security service config file: {}", e.getMessage());
            return -1;
        }

        logger.info("security service load success");
        return 0;
    }

    /**
     * 根据玩家ip查询所属的安全组
     * @param roleId 角色id
     * @param ip ip地址
     * @return 所属的安全组
     */
    public String getGroup(int roleId, String ip)
    {
        if (!isOpen)
        {
            logger.info("security group configuration is not open");
            return defaultGroupName;
        }

        RoleRecord roleRecord = table.Role_records.update(roleId);
        if (roleRecord == null)
        {
            logger.info("can't find role records info for roleId {}", roleId);
        }

        RoleInfo roleInfo = table.Role_info.select(roleId);
        if (roleInfo == null)
        {
            logger.warn("can't find role info for roleId {}", roleId);
            return defaultGroupName;
        }

        int registDays = getDaysFrom(roleInfo.getRoleCreateTime());
        int combat7Days = roleRecord != null ? getCombatCounts(roleRecord.getCombatTimes()) : 0;
        int combatToday = roleRecord != null ? getTodayCounts(roleRecord.getCombatTimes()) : 0;
        int combatTotal = roleRecord != null ? getTotalCounts(roleRecord.getCombatTotal(), roleRecord.getCombatTimes()) : 0;
        int agencyId = roleRecord != null ? roleRecord.getAgencyId() : 0;

        IpInfo ipInfo = IpSearch.getInstance().Get(ip);

        // 只需要读锁, 多线程可同步执行
        rLock.lock();
        try {
            for (SecurityGroup group : securityGroups) {
                if (group.isActive() && group.matchGroup(registDays, combat7Days, combatToday, combatTotal, agencyId, ipInfo, roleId))
                    return group.getName();
            }
        } finally {
            rLock.unlock();
        }

        logger.info("can't find matching group for role {} and ip {}", roleId, ip);
        return defaultGroupName;
    }

    /**
     * 检查并删除过期的局数记录， 添加今天的局数记录
     * @param roleId 角色id
     */
    public void updateCombatRecords(int roleId)
    {
        RoleRecord rrecord = table.Role_records.update(roleId);
        if (rrecord == null)
        {
            logger.info("can't find Role_record for role {}", roleId);
            rrecord = table.Role_records.insert(roleId);
            rrecord.setRole_id(roleId);
            rrecord.setAgencyId(0);
        }

        // 最后一条记录是不是今天的
        List<CombatInfo> combats = rrecord.getCombatTimes();
        if (combats.size() > 0) {
            CombatInfo combatInfo = combats.get(combats.size() - 1);

            if (DateTimeConstants.isDateSame(combatInfo.getTime(), System.currentTimeMillis()))
                combatInfo.setCount(combatInfo.getCount() + 1);
            else {
                combatInfo = new CombatInfo();
                combatInfo.setTime(System.currentTimeMillis());
                combatInfo.setCount(1);
                combats.add(combatInfo);
            }
        }
        else {
            CombatInfo combatInfo = new CombatInfo();
            combatInfo.setTime(System.currentTimeMillis());
            combatInfo.setCount(1);
            combats.add(combatInfo);
        }

        // 删除过期的记录
        int count = combats.size();
        for (int i = count - 1; i >= 0; i--)
        {
            if (getDaysFrom(combats.get(i).getTime()) > COMBAT_DAYS)
                combats.remove(i);
        }

        while (combats.size() > COMBAT_DAYS + 1)
        {
            logger.warn("combat times error, count = {}", combats.size());
            combats.remove(0);
        }

        // 增加战斗总局数
        rrecord.setCombatTotal(rrecord.getCombatTotal() + 1);
    }

    /**
     * 返回配置的安全组列表
     */
    public List<String> getGroups()
    {
        List<String> data = new ArrayList<>();
        for (SecurityGroup group : securityGroups)
        {
            data.add(group.getName());
        }
        return data;
    }

    private int getDaysFrom(long time)
    {
        long now = DateTimeConstants.getDate(System.currentTimeMillis());
        long t = DateTimeConstants.getDate(time);
        return (int)((now - t) / DateTimeConstants.DAY);
    }

    // 获取7天内的局数 (不包括当天)
    private int getCombatCounts(List<CombatInfo> combats)
    {
        // 设置日期
        if (combats.size() > 0 && combats.get(0).getTime() == 0)
        {
            long time = System.currentTimeMillis();
            int day = 1;
            for (CombatInfo info : combats)
            {
                info.setTime(time - day * DateTimeConstants.DAY);
                day++;
            }
        }

        // 只计算7天内的局数
        int counts = 0;
        for (CombatInfo info : combats)
        {
            int day = getDaysFrom(info.getTime());
            if (day > 0 && day <= COMBAT_DAYS)
                counts += info.getCount();
        }
        return counts;
    }

    // 获取当天战斗局数
    private int getTodayCounts(List<CombatInfo> combats)
    {
        if (combats.size() > 0) {
            CombatInfo combatInfo = combats.get(combats.size() - 1);
            if (DateTimeConstants.isDateSame(combatInfo.getTime(), System.currentTimeMillis()))
                return combatInfo.getCount();
        }

        return 0;
    }

    // 获取总局数 (总局数需要减掉当天的局数)
    private int getTotalCounts(int total, List<CombatInfo> combats)
    {
        if (combats.size() > 0) {
            CombatInfo combatInfo = combats.get(combats.size() - 1);
            if (DateTimeConstants.isDateSame(combatInfo.getTime(), System.currentTimeMillis()))
                return total - combatInfo.getCount();
        }

        return total;
    }

    public void resetGroups(List<SecurityGroup> groups)
    {
        // 获取写锁
        wLock.lock();
        try {
            logger.warn("reset security groups");
            securityGroups = groups;
        } finally {
            wLock.unlock();
        }
    }

    // 是否开启
    public void setOpen(boolean open)
    {
        isOpen = open;
    }

    public boolean getOpen()
    {
        return isOpen;
    }

    // 默认组
    public void setDefaultGroup(String name)
    {
        defaultGroupName = name;
    }

    public String getDefaultGroup()
    {
        return defaultGroupName;
    }
}
