package com.kodgames.client.common;

import com.kodgames.client.service.account.RoleService;
import com.kodgames.corgi.core.service.ServiceContainer;
import io.netty.util.internal.ConcurrentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Room {
    private static Logger logger = LoggerFactory.getLogger(Room.class);
    private static int MAX_PLAYERS = 4;

    private Set<Integer> roles = new ConcurrentSet<>();       // 房间内的玩家
    private int maxPlayerCount = MAX_PLAYERS;           // 最大玩家数
    private List<Integer> gameplays = null;             // 玩法
    private boolean haveBegin = false;                  // 是否已开始第一局
    private boolean haveFinished = false;               // 是否已结束牌局 (收到FinalMatchResultSYN时算结束)

    private long lastActiveTime = 0;                    // 最后活动时间

    private int roomId = -1;
    private int battleId = -1;

    private List<Integer> outCards = Collections.synchronizedList(new LinkedList<>());     // 所有已打出的牌
    private List<Integer> handCards = Collections.synchronizedList(new LinkedList<>());    // 所有手牌

    public Room(int id, int battleId) {
        this.roomId = id;
        this.battleId = battleId;
        this.lastActiveTime = System.currentTimeMillis();
    }

    // 房间号
    public int getId() {
        return roomId;
    }

    // 房间所在的BattleServer
    public int getBattleId() {
        return battleId;
    }

    public void setFinished() {
        haveFinished = true;
    }

    // 获取房间的非活跃时间 (单位秒)
    public float getIdleTime() {
        long t = System.currentTimeMillis() - lastActiveTime;
        return (float)t / 1000.0f;
    }

    // 加手牌
    public void addHandCards(List<Integer> cards) {
        handCards.addAll(cards);
    }

    public void addOutCards(List<Integer> cards) {
        outCards.addAll(cards);
    }

    // 删除手牌 加入已出牌
    public void removeHandCards(List<Integer> cards) {
        handCards.addAll(cards);
        outCards.addAll(cards);
    }

    // 玩家进入房间
    public boolean join(Role role) {
        logger.info("role {} name {} join room {}", role.getRoleId(), role.getUsername(), roomId);
        if (roles.contains(role.getRoleId())) {
            logger.info("role {} name {} already in room {}, maybe reconnected", role.getRoleId(), role.getUsername(), roomId);
            return true;
        }

        if (roles.size() >= maxPlayerCount) {
            if (roles.contains(role.getRoleId())) {
                logger.info("role {} name {} already in room {}, maybe reconnect", role.getRoleId(), role.getUsername(), roomId);
                return true;
            }
            logger.info("current role count equals room {} size {}, role {} name {} can't join. roles {}", roomId, maxPlayerCount, role.getRoleId(), role.getUsername(), roles);
            RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
            boolean removed = false;
            for (Integer id : roles) {
                Role r = roleService.getRoleByRoleId(id);
                if (r != null) {
                    logger.info("role {} name {} in room {}", r.getRoleId(), r.getUsername(), r.getRoomId());
                    if (r.getRoomId() == 0) {
                        roles.remove(id);
                        removed = true;
                    } else if (r.getRoomId() != roomId) {
                        roles.remove(id);
                        removed = true;
                    }
                } else {
                    logger.info("can't find role {} in room {}", id, roomId);
                    roles.remove(id);
                    removed = true;
                }
            }

            if (!removed) {
                logger.error("current role count equals room {} size {}, role {} name {} can't join. roles {}", roomId, maxPlayerCount, role.getRoleId(), role.getUsername(), roles);
                return false;
            }
        }

        roles.add(role.getRoleId());
        return true;
    }

    // 玩家退出房间
    public boolean quit(Role role) {
        if (!roles.contains(role.getRoleId())) {
            logger.warn("role {} name {} not in room {}, doesn't need to quit this room", role.getRoleId(), role.getUsername());
            return false;
        }

        return roles.remove(role.getRoleId());
    }

    // 房间是否已满
    public boolean isFull() {
        if (roles.size() >= maxPlayerCount)
            return true;

        // 牌局结束后不允许再加入
        if (haveFinished)
            return true;

        return false;
    }

    // 设置房间参数
    public void setRoomData(List<Integer> gameplays, boolean haveBegin, int maxPlayerCount) {
        logger.info("set room {} data: haveBegin {}, maxPlayerCount {}", roomId, haveBegin, maxPlayerCount);
        this.gameplays = gameplays;
        this.haveBegin = haveBegin;
        this.maxPlayerCount = maxPlayerCount;
    }

    // 更新房间最后活跃时间
    public void touch() {
        this.lastActiveTime = System.currentTimeMillis();
    }

    // 获取房间内的玩家列表
    public List<Integer> getRoles() {
        List<Integer> rs = new ArrayList<>();
        rs.addAll(roles);
        return rs;
    }
}
