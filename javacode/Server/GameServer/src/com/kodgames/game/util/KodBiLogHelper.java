package com.kodgames.game.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.util.DateTimeUtil;
import com.kodgames.game.common.Constant;
import com.kodgames.game.common.rule.RoomTypeConfig;
import com.kodgames.game.common.rule.RuleManager;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.kodbilog.KodBiLog;

import com.kodgames.message.proto.game.GameProtoBuf;
import xbean.RoleInfo;
import xbean.RoomInfo;

public class KodBiLogHelper {
    public static void roomCreateLog(int playerID, int accountID, int roomID, int roomCount, List<Integer> gamePlays, String platform) {
        List<Object> args = new ArrayList<Object>();
        args.add("room_create_wf");
        args.add(playerID);
        String account = accountID + "";
        args.add(account);
        args.add(platform); // 玩家platform
        args.add(roomID); // 房间ID
        args.add(roomCount); // 局数索引，0--8， 1--16
        args.add(0);

        // 创建BI需要的规则，地区:规则1-规则2-规则3...
        int area = gamePlays.get(0);
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(area + ":" + roomCount);
        for (int i = 1; i < gamePlays.size(); i++) {
            sBuffer.append("-");
            sBuffer.append(gamePlays.get(i));
        }

        args.add(sBuffer.toString()); // 玩法类型
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime())); // 记录生成时间
        KodBiLog.getInstance().doLog(args);
    }

    public static void loginLog(RoleInfo bean, int roleId, String playerIP, long lastLoginTime, long now) {
        List<Object> args = new ArrayList<Object>();
        args.add("account_login");
        args.add(0); // channel
        args.add(roleId); // 玩家ID
        args.add(bean.getAccountId());
        args.add(bean.getChannel());
        args.add(playerIP);
        args.add(bean.getCardCount());
        args.add(bean.getPoints());
        args.add(bean.getTotalGameCount()); // 总局数
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(bean.getRoleCreateTime())));
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(lastLoginTime)));
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(now)));
        KodBiLog.getInstance().doLog(args);
    }

    // ?
    public static void registerLog(RoleInfo bean, int roleId, String playerIP, String platform) {
        List<Object> args = new ArrayList<Object>();
        args.add("account_register");
        args.add(0); // channel
        args.add(roleId);
        args.add(bean.getAccountId());
        args.add(platform);
        args.add(playerIP);
        args.add(bean.getCardCount());
        args.add(bean.getPoints());
        args.add(bean.getTotalGameCount());
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(bean.getRoleCreateTime())));
        KodBiLog.getInstance().doLog(args);
    }

    public static void logoutLog(RoleInfo bean, int roleId, String playerIP, Date logoutTime) {
        List<Object> args = new ArrayList<Object>();
        args.add("account_logout");
        args.add(0); // channel
        args.add(roleId);
        args.add(bean.getAccountId());
        args.add(bean.getChannel());
        args.add(playerIP);
        args.add(bean.getCardCount());
        args.add(bean.getPoints());
        args.add(bean.getTotalGameCount());
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(bean.getRoleCreateTime())));
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(bean.getLastLoginTime()))); // 登录时间
        long onlinePeriod = logoutTime.getTime() - bean.getLastLoginTime();
        args.add(onlinePeriod); // 在线时长
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(logoutTime)); // 登出时间
        KodBiLog.getInstance().doLog(args);
    }

    // ?
    public static void roomState(int positiveRoomCount, int silentRoomCount) {
        List<Object> args = new ArrayList<Object>();
        args.add("room_state");
        args.add(positiveRoomCount); // 活跃房间数
        args.add(silentRoomCount); // 沉默房间数
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime())); // 记录生成时间
        KodBiLog.getInstance().doLog(args);
    }

    /**
     * 统计在线时长
     *
     * @param channel 渠道
     * @param roleId  玩家id
     * @param time    本次在线时间
     */
    public static void onlineTimeLog(String channel, int roleId, int time) {
        List<Object> args = new ArrayList<>();
        args.add("online_long");
        args.add(getPlayerChannel(roleId));
        args.add(roleId);
        args.add(time);
        args.add(new SimpleDateFormat("YYYY-MM-dd").format(new Date().getTime())); // 记录生成时间
        KodBiLog.getInstance().doLog(args);
    }

    /**
     * 牌局统计, 由玩法服务器提供统计数据
     *
     * @param message 牌局记录
     */
    public static void roomDestroyWf(GameProtoBuf.BGRoomDestroyBiSYN message) {
        // roleIds必须有两个玩家
        if (message.getRoleIdsCount() < 2) {
            return;
        }

        // 计算消耗的房卡数量 (aa扣卡类型需要处理)
        RoomInfo roomInfo = table.Room_info.select(message.getRoomId());
        if (roomInfo == null) {
            return;
        }
        int clubId = roomInfo.getClubId();
        RoomTypeConfig rc = RuleManager.getInstance().getRoomConfig(roomInfo.getRoundCount());
        int cardConsumer = (null == rc) ? 0 : rc.getCardCount();
        int aaCardConsumer = (null == rc) ? 0 : rc.getAACardCount();
		int payType = roomInfo.getPayType();
		// 如果是aa开房，消耗的房卡数是所有玩家的总数
		if (payType == Constant.PAY_TYPE.AA_PAY) {
			cardConsumer = message.getRoleIdsCount() * aaCardConsumer;
		}

        // 玩法
        List<Integer> gamePlays = message.getGameplaysList();
        if (gamePlays.size() == 0)
            return;
        // 创建BI需要的规则，地区:规则1-规则2-规则3...
        int area = gamePlays.get(0);
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(area + ":" + roomInfo.getRoundCount());
        for (int i = 1; i < gamePlays.size(); i++) {
            sBuffer.append("-");
            sBuffer.append(gamePlays.get(i));
        }
        String playType = sBuffer.toString();

        // 销毁类型 (协议中未定义)
        int destroyType = message.getDestroyReason();

        List<Object> args = new ArrayList<>();
        args.add("room_destroy_wf");
        args.add(message.getCreatorId());                                // 房主id
        args.add(getPlayerChannel(message.getCreatorId()));                // 房主渠道
        args.add(message.getRoleIds(0));                            // 玩家1id
        args.add(getPlayerChannel(message.getRoleIds(0)));        // 玩家1渠道
        args.add(message.getRoleIds(1));                            // 玩家2id
        args.add(getPlayerChannel(message.getRoleIds(1)));        // 玩家2渠道
        if (message.getRoleIdsCount() > 2) {
            args.add(message.getRoleIds(2));                    // 玩家3id
            args.add(getPlayerChannel(message.getRoleIds(2)));  // 玩家3渠道
        } else {
            args.add("0");
            args.add("0");
        }
        if (message.getRoleIdsCount() > 3) {
            args.add(message.getRoleIds(3));                    // 玩家4id
            args.add(getPlayerChannel(message.getRoleIds(3)));  // 玩家4渠道
        } else {
            args.add("0");
            args.add("0");
        }
        args.add(message.getRoomId());                    // 房间id
        args.add(message.getCreateTime());                // 创建时间戳
        args.add(message.getDestroyTime());                // 销毁时间戳
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(message.getCreateTime()).getTime()));        // 创建时间
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(message.getDestroyTime()).getTime()));    // 销毁时间
        args.add(destroyType);                            // 销毁类型
        args.add(message.getMaxRoundCount());            // 房间最大局数
        args.add(message.getRealRoundCount());            // 房间实际局数
        args.add(message.getAverageBattleTime());        // 局中平均秒数
        args.add(message.getAverageIdleTime());            // 局中空闲平均秒数
        args.add(message.getStartIdleTime());            // 开局空闲时长
        args.add(message.getRoomLifeTime());            // 房间存续时间
        args.add(playType);                                // 玩法
        args.add(clubId);                               // 俱乐部id
        args.add(payType);                              // 支付类型
        args.add(cardConsumer);                            // 消耗房卡数
        args.add(message.getLosePlayerCount());            // 房间失败人数
        args.add(message.getTotalLoseScore());            // 房间失败分数总和
        KodBiLog.getInstance().doLog(args);
    }

    private static String getPlayerChannel(int roleId) {
//        if (roleId > 0) {
//            RoleInfo roleInfo = table.Role_info.select(roleId);
//            if (roleInfo != null)
//                return roleInfo.getChannel();
//        }

        return "0";
    }

    /**
     * 统计在线人数
     * @param amout 当前在线人数
     * @param register 该统计时间段内(10分钟)的新增人数
     * @param round 该统计时间段内的牌局数
     */
    public static void onlinePlayersLog(int amout, int register, int round) {
        List<Object> args = new ArrayList<Object>();
        args.add("online_user");
        args.add(amout);
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime()));
        args.add(register);
        args.add(round);
        KodBiLog.getInstance().doLog(args);
    }

    public static void subPlayerCard(int playerID, int roomID, int roomType, int subCardCount) {
        List<Object> args = new ArrayList<Object>();
        args.add("sub_player_card");
        args.add(playerID); // 玩家ID
        args.add(roomID); // 房间ID
        args.add(roomType); // 房间类型
        args.add(subCardCount); // 扣卡数值
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime())); // 记录生成时间
        KodBiLog.getInstance().doLog(args);
    }

    public static void addAllCardRecord(String gmName, int amount, long time) {
        List<Object> args = new ArrayList<Object>();
        args.add("add_all_card_record");
        args.add(gmName);
        args.add(amount);
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(time).getTime())); // 记录生成时间
        KodBiLog.getInstance().doLog(args);
    }

    /*
     * 销毁方式 0:主动， 1 ： 房主解散； 2：投票解散
     */
    public static void roomDestroyLog(int roomID, int destroyType, String comment) {
        List<Object> args = new ArrayList<Object>();
        args.add("room_destroy");
        args.add(roomID); // 房间ID
        args.add(destroyType); // 销毁方式 0:主动， 1 ： 房主解散； 2：投票解散
        args.add(comment);
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime())); // 记录生成时间
        KodBiLog.getInstance().doLog(args);
    }

    public static void roundFinish(Integer roomID, Date roomStartTime, List<Integer> playerIDs, int ownerID, Boolean isFinish, int roomType, Date roundRecordStartTime, int currRoundCount) {
        List<Object> args = new ArrayList<Object>();
        args.add("round_finish");
        args.add(roomID); // 房间ID
        args.add(DateTimeUtil.getGMT8String(roomStartTime));// 房间创建时间
        args.add(roomType); // 房间类型

        int playerNum = 0;
        for (; playerNum < playerIDs.size(); ++playerNum) {
            args.add(playerIDs.get(playerNum)); // 各个玩家ID
        }
        for (; playerNum < 4; ++playerNum) {
            args.add(0);
        }
        args.add(ownerID); // 房主
        args.add(isFinish ? 1 : 0); // 房间是否结束
        Date roomEndTime = isFinish ? DateTimeUtil.getNowDate() : new Date(0);
        args.add(DateTimeUtil.getGMT8String(roomEndTime)); // 房间结束时间，如果没有结束，则为1970
        args.add(DateTimeUtil.getGMT8String(roundRecordStartTime)); // 本局开始时间
        args.add(DateTimeUtil.getGMT8String(DateTimeUtil.getNowDate())); // 本局结束时间
        args.add(currRoundCount); // 当前局
        KodBiLog.getInstance().doLog(args);
    }

    public static void turntableDraw(Integer roleId, Integer rewardId, String rewardName, Integer rewardCount, Date dateTime) {
        List<Object> args = new ArrayList<Object>();
        args.add("activity_Turntable_reward");
        args.add(roleId);
        args.add(rewardId);
        args.add(rewardName);
        args.add(rewardCount);
        args.add(DateTimeUtil.getGMT8String(dateTime));
        args.add(new SimpleDateFormat("YYYY-MM-dd").format(new Date().getTime())); // 记录生成时间
        KodBiLog.getInstance().doLog(args);
    }

    public static void mgergePlayerInfo(int oldRoleId, int oldCardNum, int newRoleId, int newCardNum) {
        List<Object> args = new ArrayList<Object>();
        args.add("merge_player_info");
        args.add(oldRoleId);
        args.add(oldCardNum);
        args.add(newRoleId);
        args.add(newCardNum);
        KodBiLog.getInstance().doLog(args);
    }

    /**
     * gmt给玩家加卡
     */
    public static void gmtAddPlayersCard(int playerId, String gmtUsername, int cardNum, long time, boolean isSuccess) {
        List<Object> args = new ArrayList<Object>();
        args.add("gmt_add_players_card");
        args.add(playerId);
        args.add(gmtUsername);
        args.add(cardNum);
        args.add(DateTimeUtil.getGMT8String(new Date(time)));
        args.add(isSuccess);
        args.add(new SimpleDateFormat("YYYY-MM-dd").format(new Date().getTime())); // 记录生成时间
        KodBiLog.getInstance().doLog(args);
    }

    /**
     * 绑定手机
     */
    public static void bindPhone(int playerId, String phoneNumber) {
        List<Object> args = new ArrayList<Object>();
        args.add("phone_number_binding");
        args.add(playerId);
        args.add(phoneNumber);
        args.add(new SimpleDateFormat("YYYY-MM-dd").format(new Date().getTime()));
        KodBiLog.getInstance().doLog(args);
    }

    /**
     * 微信分享成功后记录
     *
     * @param roleId
     * @param unionId
     */
    public static void shareLinkRecord(int roleId, String unionId) {
        List<Object> args = new ArrayList<>();
        args.add("share_link_record");
        args.add(roleId);
        args.add(unionId);
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime()));
        KodBiLog.getInstance().doLog(args);
    }

    /**
     * 邀请人完成任务后回调
     *
     * @param inviterRoleId
     * @param inviterUnionId
     * @param rewardCode     邀请人获奖编号
     * @param rewardNum      邀请人获奖数量
     * @param inviteeRoleId
     * @param inviteeUnionId
     */
    public static void inviteSuccessRecord(int inviterRoleId, String inviterUnionId, int rewardCode, float rewardNum, int inviteeRoleId, String inviteeUnionId) {
        List<Object> args = new ArrayList<>();
        args.add("invite_success_record");
        args.add(inviterRoleId);
        args.add(inviterUnionId);
        args.add(rewardCode);
        args.add(rewardNum);
        args.add(inviteeRoleId);
        args.add(inviteeUnionId);
        args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime()));
        KodBiLog.getInstance().doLog(args);
    }
	/**
	 * 微信邀请新玩家（所有的通过微信邀请活动进入游戏的新玩家）
	 * 
	 * @param inviterRoleId 邀请者玩家id
	 * @param inviteeRoleId 被邀请这玩家id
	 */
	public static void inviteRecordRegister(int inviterRoleId, int inviteeRoleId)
	{
		List<Object> args = new ArrayList<>();
		args.add("invite_record_register");
		args.add(inviterRoleId);
		args.add(inviteeRoleId);
		args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime()));
		KodBiLog.getInstance().doLog(args);
	}
}