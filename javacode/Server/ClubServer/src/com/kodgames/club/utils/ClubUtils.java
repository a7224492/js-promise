package com.kodgames.club.utils;

import com.google.protobuf.GeneratedMessage;
import com.kodgames.club.service.server.ServerService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.club.ClubProtoBuf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClubUtils
{
    /**
     * 修改标志位
     * @param status club.status
     * @param flag CLUB_STATUS
     * @return CLUB_STATUS
     */
    public static int changeClubStatus(int status, int flag)
    {
        return status ^ flag;
    }

    /**
     * 检查标志位
     * @param status club.status
     * @param flag CLUB_STATUS
     * @return boolean on or off
     */
    public static boolean checkClubStatus(int status, int flag)
    {
        return (status & flag) > 0;
    }

    public static boolean isBeforeYesterday(long time) {
        Date d = new Date(time);

        Calendar cal   =   Calendar.getInstance();
        cal.add(Calendar.DATE,   -1);
        Date yesterday = cal.getTime();
        long yesterdayTimestamp = cal.getTimeInMillis();


        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );

        // 超过24小时 或者 日期是昨天
        return time < yesterdayTimestamp || format.format(yesterday).equals(format.format(d));
    }

    /**
     * 将消息通过game广播给role
     * @param callback
     * @param roleId
     * @param message
     */
    public static void broadcastMsg2Game(int callback, int roleId, GeneratedMessage message)
    {
        List<Integer> list = new ArrayList<>();
        list.add(roleId);
        broadcastMsg2Game(callback, list, message);
    }

    /**
     * 将消息通过game广播给role
     * @param callback
     * @param roleIds
     * @param message
     */
    public static void broadcastMsg2Game(int callback, List<Integer> roleIds, GeneratedMessage message)
    {
        ServerService ss = ServiceContainer.getInstance().getPublicService(ServerService.class);
        Connection gameConnection = ss.getGameConnection();

        ClubProtoBuf.CLGBroadcastMSG.Builder broadCast = ClubProtoBuf.CLGBroadcastMSG.newBuilder();
        int protocolID = ConnectionManager.getInstance().getMsgInitializer().getProtocolID(message.getClass());

        broadCast.addAllRoleId(roleIds);
        broadCast.setProtocolId(protocolID);
        broadCast.setMessage(message.toByteString());

        gameConnection.write(callback, broadCast.build());
    }
}
