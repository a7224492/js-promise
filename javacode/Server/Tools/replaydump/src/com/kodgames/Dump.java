package com.kodgames;

import cbean.GlobalRoomId;
import limax.util.Trace;
import limax.zdb.Procedure;
import limax.zdb.Zdb;
import util.SystemPropertyUtil;
import xbean.RoleInfo;
import xbean.RoomHistory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class Dump {
    private static List<GlobalRoomId> roomIdList = new ArrayList<>();
    private static List<RoomHistory> roomHistoryList = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        // zdb连接配置
        Config config = new Config();

        new java.io.File("zdb").mkdir();
        Trace.set(Trace.INFO);
        Trace.openNew();
        limax.xmlgen.Zdb meta = limax.xmlgen.Zdb.loadFromClass();


        do {
            // 通过控制台获取远程roleId
            int remoteRoleId = getRoleFromStdin("remote");
            if (remoteRoleId == -1)
                break;
            if (remoteRoleId == 0)
                continue;

            roomIdList.clear();
            roomHistoryList.clear();

            // 从远程Replay库取该用户的所有回放数据
            String remotedb = "jdbc:mysql://" + config.getRemoteHost() + ":3306/" + config.getDbname() + "?user=" + config.getRemoteUser() + "&password=" + config.getRemotePassword();
            System.out.println(remotedb);
            meta.setDbHome(remotedb);
            try {
                Zdb.getInstance().start(meta);
                Procedure.call(() -> {
                    if (!loadRoleHistory(remoteRoleId)) {
                        return true;
                    }
                    return true;
                });
            } catch (Throwable e) {
                Trace.error("exception {}", e);
                break;
            } finally {
                Zdb.getInstance().stop();
            }



            // 插入本地数据库
            String localdb = "jdbc:mysql://" + config.getLocalHost() + ":3306/" + config.getDbname() + "?user=" + config.getLocalUser() + "&password=" + config.getLocalPassword();
            System.out.println(localdb);
            meta.setDbHome(localdb);
            try {
                Zdb.getInstance().start(meta);
                Procedure.call(() -> {
                    RoleInfo localRoleInfo = table.Role_history_rooms.update(remoteRoleId);
                    if (localRoleInfo == null)
                        localRoleInfo = table.Role_history_rooms.insert(remoteRoleId);

                    localRoleInfo.getHistoryRooms().clear();
                    for (GlobalRoomId id : roomIdList)
                        localRoleInfo.getHistoryRooms().add(id);

                    int count = roomHistoryList.size();
                    for (int i = 0; i < count; i++) {
                        GlobalRoomId id = roomIdList.get(i);
                        RoomHistory data = roomHistoryList.get(i);

                        RoomHistory history = table.Room_history.insert(id);
                        if (history == null)
                            history = table.Room_history.update(id);
                        history.copyFrom(data);
                    }
                    System.out.println("successfully insert replays to local zdb");
                    return true;
                });
            } catch (Throwable e) {
                Trace.error("exception {}", e);
                break;
            } finally {
                Zdb.getInstance().stop();
            }

        } while (true);
    }

    // 通过控制台获取roleId
    private static int getRoleFromStdin(String desc) {
        DataInputStream in = new DataInputStream(new BufferedInputStream(System.in));
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        System.out.println("please input " + desc + " role id:");
        String roleStr = "";
        try {
            roleStr = reader.readLine();
            if (roleStr.length() == 0)
                return 0;

            if (roleStr.equalsIgnoreCase("quit") || roleStr.equalsIgnoreCase("exit"))
                return -1;

            Integer roleId = Integer.valueOf(roleStr);
            return roleId;
        } catch (Exception e) {
            System.out.println("invalid " + desc + " role id " + roleStr);
            return 0;
        }
    }

    // 从远程数据库读取回放数据
    private static boolean loadRoleHistory(int roleId) {
        RoleInfo remoteRoleInfo = table.Role_history_rooms.select(roleId);
        if (remoteRoleInfo == null) {
            System.out.println("can't find replay data for role id " + roleId);
            return false;
        } else {
            roomIdList = remoteRoleInfo.getHistoryRooms();
            List<GlobalRoomId> realList = new ArrayList<>();

            for (GlobalRoomId roomId : roomIdList) {
                RoomHistory roomHistory = table.Room_history.select(roomId);
                if (roomHistory == null) {
                    System.out.println("can't find room history " + roomId.toString());
                } else {
                    // realList 和 roomHistoryList中的记录是按顺序一一对应的
                    roomHistoryList.add(roomHistory);
                    realList.add(roomId);
                }
            }

            roomIdList = realList;
            System.out.println("load replay data from remote zdb success. count: " + roomHistoryList.size());
        }

        return true;
    }
}
