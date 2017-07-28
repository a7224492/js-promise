package com.kodgames.client.service.account;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

import com.kodgames.client.common.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.PublicService;

public class RoleService extends PublicService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
    // 发送网络请求时的callback值
    public static AtomicInteger callbackSeed = new AtomicInteger(0);
    // 最大的Connection id值
    private static AtomicInteger maxConnId = new AtomicInteger(0);

    // 保存所有玩家, key为connectionId
    private Map<Integer, Role> connRoleMap = new ConcurrentHashMap<>();
    // key为roleId
    private Map<Integer, Role> idRoleMap = new ConcurrentHashMap<>();

    // 用户名到RoleId的映射
    private Map<String, Integer> usernameToRoles = new ConcurrentHashMap<>();

    // 保存大厅玩家列表
    private Queue<Integer> lobbyRoles = new ConcurrentLinkedDeque<>();
    // 保存在房间里的玩家列表
    private Queue<Integer> roomRoles = new ConcurrentLinkedDeque<>();
    // 保存在回放的玩家列表 (包括战绩)
    private Queue<Integer> replayRoles = new ConcurrentLinkedDeque<>();

    public RoleService() {
    }

    // 添加新玩家, 当与Interface连接成功并创建Role后调用
    public void addRole(Role role) {
        int connId = role.getConnectionId();
        if (connRoleMap.putIfAbsent(connId, role) != null)
            logger.error("addRole() role with connection id {} already exist", connId);

        if (role.getConnectionId() > maxConnId.get())
            maxConnId.set(role.getConnectionId());
    }

    public Role getRole(int connectionId) {
        Role role = connRoleMap.get(connectionId);
        if (role == null) {
            logger.info("getRole() role with connection id {} not exist", connectionId);
        }

        return role;
    }

    public void updateRoleId(Role role, int id, String username) {
        if (idRoleMap.putIfAbsent(id, role) != null)
            logger.error("updateRoleId() role with id {} name {} already exist", id, username);
        if (usernameToRoles.putIfAbsent(username, id) != null)
            logger.error("updateRoleId() role {} with username {} already exist", id, username);
    }

    public Role getRoleByUsername(String name) {
        Integer id = usernameToRoles.get(name);
        if (id != null)
            return idRoleMap.get(id);
        return null;
    }

    public Role getRoleByRoleId(Integer roleId) {
        return idRoleMap.get(roleId);
    }

    // 获取在线玩家数量
    public int getRoleCount() {
        return idRoleMap.size();
    }

    // 获取当前的最大Connection id值
    public int getMaxConnId() {
        return maxConnId.get();
    }

    // 删除玩家, 当网络连接断开时调用
    public void removeRole(int connectionId) {
        Role role = connRoleMap.get(connectionId);
        if (role == null) {
            logger.error("removeRole() role with connection id {} not exist", connectionId);
            return;
        }

        int roleId = role.getRoleId();
        if (roleId > 0) {
            idRoleMap.remove(roleId);
        }

        String username = role.getUsername();
        if (username != null)
            usernameToRoles.remove(username);

        connRoleMap.remove(connectionId);
    }

    // 将玩家加入大厅队列
    public void addRoleToLobbyList(Role role) {
        lobbyRoles.add(role.getRoleId());
    }

    // 取大厅玩家, 并从该队列中删除
    // 队列中记录的是玩家id, 当玩家离线时并没有从队列中删除, 所以获取玩家时需要检查一下
    public Role getRoleFromLobbyList() {
        do {
            Integer roleId = lobbyRoles.poll();
            if (roleId == null)
                return null;

            Role role = idRoleMap.get(roleId);
            if (role != null)
                return role;
        } while (true);
    }

    // 加入房间队列
    public void addRoleToRoomList(Role role) {
        roomRoles.add(role.getRoleId());
    }
}
