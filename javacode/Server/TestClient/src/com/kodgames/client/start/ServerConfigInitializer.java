package com.kodgames.client.start;

import java.util.Map;
import java.util.Random;

import com.kodgames.corgi.core.util.config_utils.ServerParser;
import com.sun.org.apache.xpath.internal.operations.Bool;

public class ServerConfigInitializer {
    private static ServerConfigInitializer instance = new ServerConfigInitializer();

    private static final String INTERFACE_HOST_KEY = "interface_host";
    private static final String INTERFACE_PORT_KEY = "interface_port";

    private static final String CLIENT_COUNT_KEY = "client_count";
    private static final String MINIMAL_CLIENT_COUNT_KEY = "minimal_client_count";
    private static final String USERNAME_PREFIX_KEY = "username_prefix";
    private static final String AREA_KEY = "area";
    private static final String PAY_TYPE_KEY = "paytypeAA";
    private static final String FREE_ROOM_COUNT_KEY = "max_free_room_count";

    private static final String TASK_JOIN_ROOM_KEY = "task_join_room";
    private static final String TASK_CREATE_ROOM_KEY = "task_create_room";

    private static final String TASK_ROOM_VOTE_DESTROY = "task_vote_destroy_room";
    private static final String TASK_RECONNECT_ROOM_KEY = "task_reconnect_room";
    private static final String TASK_ROOM_CHAT_TEXT = "task_chat_text";
    private static final String TASK_ROOM_CHAT_EMOJO = "task_chat_emojo";
    private static final String TASK_ROOM_CHAT_CUSTOM = "task_chat_custom";

    private static final String REENTER_FINISHED_ROOM_RATE = "reenter_finished_room_rate";
    private static final String VOTE_DESTROY_ROOM_TIME_KEY = "vote_destroy_room_time";
    private static final String VOTE_DESTROY_ROOM_RATE_KEY = "vote_destroy_room_rate";

    private String interfaceHost = null;
    private Integer interfacePort = null;
    private Integer clientCount = null;
    private Integer minClientCount = null;
    private String usernamePrefix = null;
    private String areaValue = null;
    private Float payTypeAA = null;
    private Integer freeRoomCount = null;

    private Float taskJoinRoom = null;
    private Float taskCreateRoom = null;

    private Float taskRoomVoteDestroy = null;
    private Float taskRoomReconnect = null;
    private Float taskRoomChatText = null;
    private Float taskRoomChatEmojo = null;
    private Float taskRoomChatCustom = null;

    private Float reenterFinishedRoomRate = null;
    private Float voteDestroyRoomTime = null;
    private Float voteDestroyRoomRate = null;

    private ServerConfigInitializer() {
    }

    public static ServerConfigInitializer getInstance() {
        return instance;
    }

    public void init(String configPath) {
        ServerParser sp = new ServerParser(Object.class.getResourceAsStream(configPath));
        sp.read();

        Map<String, String> config = sp.getConfig();
        interfaceHost = String.valueOf(config.get(INTERFACE_HOST_KEY));
        interfacePort = Integer.valueOf(config.get(INTERFACE_PORT_KEY));
        clientCount = Integer.valueOf(config.get(CLIENT_COUNT_KEY));
        minClientCount = Integer.valueOf(config.get(MINIMAL_CLIENT_COUNT_KEY));
        usernamePrefix = String.valueOf(config.get(USERNAME_PREFIX_KEY));
        areaValue = String.valueOf(config.get(AREA_KEY));
        payTypeAA = Float.valueOf(config.get(PAY_TYPE_KEY));
        freeRoomCount = Integer.valueOf(config.get(FREE_ROOM_COUNT_KEY));

        taskJoinRoom = Float.valueOf(config.get(TASK_JOIN_ROOM_KEY));
        taskCreateRoom = Float.valueOf(config.get(TASK_CREATE_ROOM_KEY));

        taskRoomVoteDestroy = Float.valueOf(config.get(TASK_ROOM_VOTE_DESTROY));
        taskRoomReconnect = Float.valueOf(config.get(TASK_RECONNECT_ROOM_KEY));
        taskRoomChatText = Float.valueOf(config.get(TASK_ROOM_CHAT_TEXT));
        taskRoomChatEmojo = Float.valueOf(config.get(TASK_ROOM_CHAT_EMOJO));
        taskRoomChatCustom = Float.valueOf(config.get(TASK_ROOM_CHAT_CUSTOM));

        reenterFinishedRoomRate = Float.valueOf(config.get(REENTER_FINISHED_ROOM_RATE));
        voteDestroyRoomTime = Float.valueOf(config.get(VOTE_DESTROY_ROOM_TIME_KEY));
        voteDestroyRoomRate = Float.valueOf(config.get(VOTE_DESTROY_ROOM_RATE_KEY));
    }

    public String getInterfaceHost() {
        return interfaceHost;
    }
    public Integer getInterfacePort() {
        return interfacePort;
    }

    public Integer getClientCount() { return clientCount; }
    public Integer getMinClientCount() { return minClientCount; }
    public String getUsernamePrefix() { return usernamePrefix; }
    public String getArea() { return areaValue; }

    public Integer getFreeRoomCount() { return freeRoomCount; }

    public Float getTaskJoinRoom() { return taskJoinRoom; }
    public Float getTaskCreateRoom() { return taskCreateRoom; }

    public Float getTaskRoomVoteDestroy() { return taskRoomVoteDestroy; }
    public Float getTaskRoomReconnect() { return taskRoomReconnect; }
    public Float getTaskRoomChatText() { return taskRoomChatText; }
    public Float getTaskRoomChatEmojo() { return taskRoomChatEmojo; }
    public Float getTaskRoomChatCustom() { return taskRoomChatCustom; }

    public Float getReenterFinishedRoomRate() { return reenterFinishedRoomRate; }
    public Float getVoteDestroyRoomTime() { return voteDestroyRoomTime; }
    public Float getVoteDestroyRoomRate() { return voteDestroyRoomRate; }

    public int getPayType() {
        Random r = new Random();
        if (r.nextFloat() < payTypeAA)
            return 13;      // AA
        return 11;          // 房主
    }
}
