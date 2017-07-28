package com.kodgames.client.service.room;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.common.Room;
import com.kodgames.corgi.core.service.PublicService;

public class RoomService extends PublicService {
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    // 所有房间 (key为roomId)
    private Map<Integer, Room> idRoomMap = new ConcurrentHashMap<>();
    private List<Integer> roomList = Collections.synchronizedList(new ArrayList<>());

    // 空闲的房间 (人员未满的房间)
    private Set<Integer> freeRooms = Collections.synchronizedSet(new HashSet<>());

    public RoomService() {
    }

    public boolean hasRoom(int roomId) {
        return idRoomMap.containsKey(roomId);
    }

    public Room createRoom(int roomId, int battleId) {
        logger.info("create room {} in battle {}", roomId, battleId);
        if (hasRoom(roomId)) {
            logger.error("room {} already exist, can't create again", roomId);
            return null;
        }

        Room room = new Room(roomId, battleId);
        if (idRoomMap.putIfAbsent(roomId, room) != null) {
            logger.error("failed to add room {} to list", roomId);
            return null;
        }

        // 新创建的房间为空房间
        addFreeRoom(roomId);

        return room;
    }

    public Room getRoom(int roomId) {
        if (roomId == 0)
            return null;
        return idRoomMap.get(roomId);
    }

    public int getRoomCount() {
        return idRoomMap.size();
    }

    // 返回空闲房间
    public Room getFreeRoom() {
        do {
            if (freeRooms.isEmpty())
                return null;

            try {
                Integer[] ids = freeRooms.toArray(new Integer[0]);
                Random r = new Random();
                int index = r.nextInt(ids.length);
                Integer id = ids[index];

                Room room = getRoom(id);
                if (room == null) {
                    logger.info("invalid room id {}, maybe has been destroyed", id);
                    freeRooms.remove(id);
                } else {
                    if (!room.isFull())
                        return room;
                    else
                        freeRooms.remove(id);
                }
            } catch (NoSuchElementException e) {
                logger.info("free room list is empty");
                return null;
            }
        } while (true);
    }

    public int getFreeRoomCount() {
        return freeRooms.size();
    }

    // 添加空间房间
    public void addFreeRoom(int roomId) {
        freeRooms.add(roomId);
    }

    // 设置房间牌局已结束
    public void roomFinished(int roomId) {
        Room room = getRoom(roomId);
        if (room != null) {
            idRoomMap.remove(roomId);
            room.setFinished();
        }
    }

    // 设置房间已满
    public void setRoomFull(int roomId) {
        logger.info("setRoomFull room {} is full, remove from free room list", roomId);
        freeRooms.remove(roomId);
    }

    // 返回一个房间用于执行房间任务
    public Room getRoomForTask() {
        if (roomList.isEmpty()) {
            idRoomMap.forEach((key, value) -> {
                roomList.add(key);
            });
        }

        try {
            do {
                if (roomList.isEmpty())
                    return null;

                Integer id = roomList.remove(0);
                Room room = getRoom(id);
                if (room != null)
                    return room;
            } while (true);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}