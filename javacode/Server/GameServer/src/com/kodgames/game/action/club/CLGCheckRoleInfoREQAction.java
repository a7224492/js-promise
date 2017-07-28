package com.kodgames.game.action.club;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.RoleInfo;

/**
 * Created by 001 on 2017/3/21.
 */
@ActionAnnotation(messageClass = ClubProtoBuf.CLGCheckRoleInfoREQ.class, actionClass = CLGCheckRoleInfoREQAction.class, serviceClass = RoleService.class)
public class CLGCheckRoleInfoREQAction extends CGProtobufMessageHandler<RoleService, ClubProtoBuf.CLGCheckRoleInfoREQ>
{
    private static final Logger logger = LoggerFactory.getLogger(CLGCheckRoleInfoREQAction.class);

    @Override
    public void handleMessage(Connection connection, RoleService service, ClubProtoBuf.CLGCheckRoleInfoREQ message, int callback)
    {
        int roleId = message.getRoleId();
        int seqId = message.getSeqId();

        ClubProtoBuf.GCLCheckRoleInfoRES.Builder builder = ClubProtoBuf.GCLCheckRoleInfoRES.newBuilder();
        builder.setSeqId(seqId);

        RoleInfo role = table.Role_info.select(roleId);
        if (role == null)
        {
            builder.setResult(PlatformProtocolsConfig.GCL_CHECK_ROLE_INFO_ROLE_NOT_FOUND);
            builder.setRoleId(0);
            connection.write(callback, builder.build());
            return;
        }

        builder.setRoleId(roleId);
        builder.setName(role.getNickname());
        builder.setResult(PlatformProtocolsConfig.GCL_CHECK_ROLE_INFO_SUCCESS);
        connection.write(callback, builder.build());
    }
}
