package com.kodgames.client.common;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.battle.BattleProtoBuf;
import com.kodgames.message.proto.room.RoomProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by liufei on 2017/6/23.
 * 每个Role代表一个角色(玩家)
 */
public class Role {
    private static Logger logger = LoggerFactory.getLogger(Role.class);

    // 玩家所处的状态定义
    public enum RoleState {
        // 登录----------------------------------
        Login,

        // 大厅----------------------------------
        Lobby,

        // 牌桌----------------------------------
        Room,

        // 回放----------------------------------
        Replay,
    }

    private Connection interfaceConnection = null;
    private Connection gameConnection = null;
    private Connection battleConnection = null;

    private List<Integer> handCards = null;
    private List<Integer> outCards = null;

    private int roleId = 0;
    private String username;
    private String nickName;
    private String headImg;
    private int sex = 0;

    private int roomId = 0;
    private int requestEnterRoomId = 0;     // 请求进入的房间号

    private Random random = null;

    public Role(Connection conn) {
        interfaceConnection = conn;
        random = new Random();
    }

    // 到InterfaceServer的连接 (第一个连接)
    public Connection getConnection() { return interfaceConnection; }
    public int getConnectionId() { return interfaceConnection.getConnectionID(); }

    public void setRoleId(int value, String username, String nickname, String headImg, int sex) {
        if (this.roleId != 0) {
            logger.warn("role {} already set id, set to {} again", this.roleId, value);
        }

        this.roleId = value;
        this.username = username;
        this.nickName = nickname;
        this.headImg = headImg;
        this.sex = sex;
    }
    public int getRoleId() { return roleId; }
    public String getUsername() { return username; }
    public String getNickName() { return nickName; }
    public String getHeadImg() { return headImg; }
    public int getSex() { return sex; }

    // 房间号
    public void setRoom(int roomId) {
        logger.info("set role {} name {} to room {}, oldRoom {}, requestEnterRoom {}", roleId, username, roomId, this.roomId, requestEnterRoomId);
        this.roomId = roomId;
        requestEnterRoomId = 0;

        handCards = null;
        outCards = null;
    }
    public int getRoomId() {
        return roomId;
    }

    public int getRequestEnterRoomId() {
        return requestEnterRoomId;
    }

    // 设置手牌
    public void setCards(List<Integer> handCards, List<Integer> outCards) {
        this.handCards = handCards;
        this.outCards = outCards;

        RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
        Room room = roomService.getRoom(roomId);
        if (room != null) {
            room.addHandCards(handCards);
            room.addOutCards(outCards);
        }
    }

    // 加手牌
    public void addHandCard(Integer card) {
        List<Integer> cards = new ArrayList<>();
        cards.add(card);
        addHandCards(cards);
    }

    public void addHandCards(List<Integer> cards) {
        if (this.handCards != null)
            this.handCards.addAll(cards);
        else
            logger.error("hand cards has not been initialized, add cards: {}", cards);

        RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
        Room room = roomService.getRoom(roomId);
        if (room != null) {
            room.addHandCards(cards);
        }
    }

    // 删手牌
    public void removeHandCard(Integer card) {
        List<Integer> cards = new ArrayList<>();
        cards.add(card);
        removeHandCards(cards);
    }

    public void removeHandCards(List<Integer> cards) {
        if (this.handCards != null)
            this.handCards.removeAll(cards);
        if (this.outCards != null)
            this.outCards.addAll(cards);

        RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
        Room room = roomService.getRoom(roomId);
        if (room != null) {
            room.removeHandCards(cards);
        }
    }

    // 获取一张要出的牌
    public Integer getPlayCard() {
        if (handCards == null || handCards.size() == 0)
            return 0;
        int index = random.nextInt(handCards.size());
        Integer card = handCards.get(index);
        return card;
    }

    // 处理打牌流程
    public void step(int pointInGame, int playType, List<Integer> cards, int sourceRoleId) {
        if (roomId == 0) {
            logger.info("user {} already not in room", username);
            return;
        }

        RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
        Room room = roomService.getRoom(roomId);
        if (room != null)
            room.touch();
        else
            logger.error("can't find room {} when role {} name {} step", roomId, roleId, username);

        switch (playType) {
            case MahjongConstant.PlayType.OPERATE_DEAL:                 // 摸牌
                addHandCards(cards);
                break;

            case MahjongConstant.PlayType.OPERATE_CAN_PLAY_A_CARD:      // 可以出牌
                Integer card = getPlayCard();
                List<Integer> playCards = new ArrayList<>();
                playCards.add(card);
                playCard(MahjongConstant.PlayType.OPERATE_PLAY_A_CARD, playCards);
                break;
            case MahjongConstant.PlayType.OPERATE_PLAY_A_CARD:          // 出牌
                if (cards.size() > 0) {
                    removeHandCards(cards);
                }
                break;

            case MahjongConstant.PlayType.DISPLAY_BE_CHI:               // 被吃
            case MahjongConstant.PlayType.DISPLAY_BE_PENG:              // 被碰
            case MahjongConstant.PlayType.DISPLAY_BE_GANG:              // 被杠
                break;

            case MahjongConstant.PlayType.DISPLAY_SHOW_MASTER_CARD:     // 鬼牌
            case MahjongConstant.PlayType.DISPLAY_MASTER_HONG_ZHONG:
                break;

            case MahjongConstant.PlayType.OPERATE_CAN_PASS:             // 过
                if (random.nextFloat() < 0.2)
                    playCard(MahjongConstant.PlayType.OPERATE_PASS, cards);
                break;

            case MahjongConstant.PlayType.OPERATE_CAN_CHI_A_CARD:       // 吃
                playCard(MahjongConstant.PlayType.OPERATE_CHI_A_CARD, cards);
                break;
            case MahjongConstant.PlayType.OPERATE_CHI_A_CARD:
                if (cards.size() > 0) {
                    removeHandCards(cards);
                }
                break;

            case MahjongConstant.PlayType.OPERATE_CAN_PENG_A_CARD:      // 碰
                playCard(MahjongConstant.PlayType.OPERATE_PENG_A_CARD, cards);
                break;
            case MahjongConstant.PlayType.OPERATE_PENG_A_CARD:
                if (cards.size() > 0) {
                    removeHandCards(cards);
                }
                break;

            case MahjongConstant.PlayType.OPERATE_CAN_GANG_A_CARD:      // 杠
                playCard(MahjongConstant.PlayType.OPERATE_GANG_A_CARD, cards);
                break;
            case MahjongConstant.PlayType.OPERATE_GANG_A_CARD:
                if (cards.size() > 0) {
                    removeHandCards(cards);
                }
                break;

            case MahjongConstant.PlayType.OPERATE_CAN_BU_GANG_A_CARD:   // 补杠
                playCard(MahjongConstant.PlayType.OPERATE_BU_GANG_A_CARD, cards);
                break;
            case MahjongConstant.PlayType.OPERATE_BU_GANG_A_CARD:
                if (cards.size() > 0) {
                    // 补杠只删一张牌
                    removeHandCard(cards.get(0));
                }
                break;

            case MahjongConstant.PlayType.OPERATE_CAN_AN_GANG:          // 暗杠
                playCard(MahjongConstant.PlayType.OPERATE_AN_GANG, cards);
                break;
            case MahjongConstant.PlayType.OPERATE_AN_GANG:
                if (cards.size() > 0) {
                    removeHandCards(cards);
                }
                break;

            case MahjongConstant.PlayType.OPERATE_CAN_HU:               // 胡
            case MahjongConstant.PlayType.OPERATE_CAN_AUTO_HU:               // 自动胡
                playCard(MahjongConstant.PlayType.OPERATE_HU, cards);
                break;
            case MahjongConstant.PlayType.OPERATE_HU:
                break;

            case MahjongConstant.PlayType.OPERATE_DUN_LA_PAO_START:     // 蹲拉跑开始 （内蒙）
                cards.clear();
                cards.add(0);
                playCard(MahjongConstant.PlayType.OPERATE_DUN_LA_PAO, cards);
                break;

            case MahjongConstant.PlayType.OPERATE_DUN_LA_PAO:
            case MahjongConstant.PlayType.OPERATE_DUN_LA_PAO_FINISH:
            case MahjongConstant.PlayType.OPERATE_DUN_LA_PAO_SELECT:
                break;

            case MahjongConstant.PlayType.DISPLAY_EX_CARD:              // 花牌
            case MahjongConstant.PlayType.DISPLAY_TING:                 // 听
            case MahjongConstant.PlayType.DISPLAY_MASK_ALL_HAND_CARD:   // 蒙灰
            case MahjongConstant.PlayType.DISPLAY_AUTO_PLAY_LAST_DEALED_CARD:
            case MahjongConstant.PlayType.OPERATE_CAN_AUTO_PLAY_LAST_DEALED_CARD:
            case MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE:
            case MahjongConstant.PlayType.DISPLAY_BUY_HORSE:
            case MahjongConstant.PlayType.DISPLAY_PUNISH_HORSE:
            case MahjongConstant.PlayType.DISPLAY_HORSE_CARD:
            case MahjongConstant.PlayType.DISPLAY_WIN_HORSE_CARD:
            case MahjongConstant.PlayType.DISPLAY_LOSE_HORSE_CARD:
            case MahjongConstant.PlayType.DISPLAY_HORSE_END:
            case MahjongConstant.PlayType.DISPLAY_DEAL_BETTING_HOUSE:
                break;

            default:
                logger.warn("unknown play type {}, cards {}", playType, cards);
                break;
        }
    }

    // 通过已经创建的BattleServer连接获取当前所在的BattleServer
    public int getBattleId() {
        if (battleConnection != null)
            return battleConnection.getRemoteConnectionID();
        return 0;
    }

    private void playCard(int playType, List<Integer> cards) {
        byte[] cardBytes = new byte[cards.size()];
        for (int i = 0; i < cards.size(); i++)
            cardBytes[i] = cards.get(i).byteValue();
        ByteString cardString = ByteString.copyFrom(cardBytes);

        playCard(playType, cardString);
    }

    private void playCard(int playType, ByteString cards) {
        logger.debug("playCard(), roleId={}, playType={}, card={}, handCard={}", roleId, playType, cards, this.handCards);

        BattleProtoBuf.CBPlayCardREQ.Builder builder = BattleProtoBuf.CBPlayCardREQ.newBuilder();
        builder.setCards(cards);
        builder.setPlayType(playType);

        this.sendToBattle(builder.build());
    }

    // 初始化GameServer连接 (虚拟连接)
    public void initGameConnection(int gameServerId) {
        if (gameConnection != null)
            logger.warn("game connection already initialized to server id {}, initilized again to {}", gameConnection.getRemoteConnectionID(), gameServerId);

        gameConnection = new Connection(this.interfaceConnection.getConnectionID(), this.interfaceConnection.getNettyNode(), this.interfaceConnection.getRemotePeerIP());
        gameConnection.setConnectionType(Connection.CONNECTION_TYPE_INTERFACE_TRANSFER_CLIENT);
        gameConnection.setRemotePeerID(this.roleId);
        gameConnection.setRemoteConnectionID(gameServerId);
        gameConnection.setTransferConnectoin(this.interfaceConnection);
    }

    // 重置BattleServer连接 (虚拟连接)
    public void resetBattleConnection(int battleServerId) {
        battleConnection = new Connection(this.interfaceConnection.getConnectionID(), this.interfaceConnection.getNettyNode(), this.interfaceConnection.getRemotePeerIP());
        battleConnection.setConnectionType(Connection.CONNECTION_TYPE_INTERFACE_TRANSFER_CLIENT);
        battleConnection.setRemotePeerID(this.roleId);
        battleConnection.setRemoteConnectionID(battleServerId);
        battleConnection.setTransferConnectoin(this.interfaceConnection);
    }

    // -----------------------------------------------------------------------------

    // 请求进入牌桌
    public void requestEnterRoom(int roomId) {
        if (requestEnterRoomId != 0) {
            logger.info("role {} name {} already request enter room {}, request enter another room {} again", roleId, username, requestEnterRoomId, roomId);
            RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
            roleService.addRoleToLobbyList(this);
            return;
        }

        RoomProtoBuf.CBEnterRoomREQ.Builder builder = RoomProtoBuf.CBEnterRoomREQ.newBuilder();
        builder.setRoleId(roleId);
        builder.setRoomId(roomId);
        builder.setNickname(nickName);
        builder.setHeadImageUrl(headImg);
        builder.setSex(sex);
        sendToBattle(builder.build());

        requestEnterRoomId = roomId;
    }

    // 已准备好
    public void requestBattleReady() {
        RoomProtoBuf.CBUpdateStatusREQ.Builder builder = RoomProtoBuf.CBUpdateStatusREQ.newBuilder();
        builder.setStatus(MahjongConstant.PlayerStatus.READY);
        sendToBattle(builder.build());
    }

    // 请求退出房间 (进入大厅)
    public void requestQuitRoom() {
        RoomProtoBuf.CBQuitRoomREQ.Builder builder = RoomProtoBuf.CBQuitRoomREQ.newBuilder();
        sendToBattle(builder.build());
    }

    // 投票解散房间
    public void requestVoteDestroyRoom() {
        RoomProtoBuf.CBStartVoteDestroyREQ.Builder builder = RoomProtoBuf.CBStartVoteDestroyREQ.newBuilder();
        sendToBattle(builder.build());
    }

    // 发包到GameServer
    public void sendToGame(GeneratedMessage message) {
        this.gameConnection.write(RoleService.callbackSeed.getAndIncrement(), message);
    }

    // 发包到BattleServer
    public void sendToBattle(GeneratedMessage message) {
        if (this.battleConnection != null)
            this.battleConnection.write(RoleService.callbackSeed.getAndIncrement(), message);
        else
            logger.warn("battleConnection is null");
    }
}
