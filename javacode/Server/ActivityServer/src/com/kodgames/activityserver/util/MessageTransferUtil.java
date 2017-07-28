package com.kodgames.activityserver.util;

import com.google.protobuf.GeneratedMessage;
import com.kodgames.activityserver.service.server.ServerService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.club.ClubProtoBuf;

/**
 * Created by liufei on 2017/6/22.
 */
public class MessageTransferUtil {

    // 通过GameServer转发消息给客户端
    public static void broadcastMsg2Game(int callback, int roleId, GeneratedMessage message)
    {
        ServerService ss = ServiceContainer.getInstance().getPublicService(ServerService.class);
        Connection gameConnection = ss.getGameConnection();

        ClubProtoBuf.CLGBroadcastMSG.Builder broadCast = ClubProtoBuf.CLGBroadcastMSG.newBuilder();
        int protocolID = ConnectionManager.getInstance().getMsgInitializer().getProtocolID(message.getClass());

        broadCast.addRoleId(roleId);
        broadCast.setProtocolId(protocolID);
        broadCast.setMessage(message.toByteString());

        gameConnection.write(callback, broadCast.build());
    }
}
