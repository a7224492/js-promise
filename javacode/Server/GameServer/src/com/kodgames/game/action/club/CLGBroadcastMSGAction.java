package com.kodgames.game.action.club;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.RoleInfo;

import java.util.List;

/**
 * Created by 001 on 2017/3/21.
 */
@ActionAnnotation(messageClass = ClubProtoBuf.CLGBroadcastMSG.class, actionClass = CLGBroadcastMSGAction.class, serviceClass = RoleService.class)
public class CLGBroadcastMSGAction extends CGProtobufMessageHandler<RoleService, ClubProtoBuf.CLGBroadcastMSG>
{
    private static final Logger logger = LoggerFactory.getLogger(CLGBroadcastMSGAction.class);

    @Override
    public void handleMessage(Connection connection, RoleService service, ClubProtoBuf.CLGBroadcastMSG message, int callback)
    {
        List<Integer> roleIds = message.getRoleIdList();
        int protocolId = message.getProtocolId();
        byte[] msg = message.getMessage().toByteArray();

        roleIds.forEach(roleId -> {
            Connection clientConnection = ConnectionManager.getInstance().getClientVirtualConnection(roleId);
            if (null != clientConnection) {
                clientConnection.write(callback, protocolId, msg);
            }
        });
    }
}
