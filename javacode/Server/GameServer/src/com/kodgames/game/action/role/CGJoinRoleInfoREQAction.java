package com.kodgames.game.action.role;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.RoleInfo;

/**
 * Created by 001 on 2017/3/21.
 */
@ActionAnnotation(messageClass = GameProtoBuf.CGJoinRoleInfoREQ.class, actionClass = CGJoinRoleInfoREQAction.class, serviceClass = RoleService.class)
public class CGJoinRoleInfoREQAction extends CGProtobufMessageHandler<RoleService, GameProtoBuf.CGJoinRoleInfoREQ>
{
    private static final Logger logger = LoggerFactory.getLogger(CGJoinRoleInfoREQAction.class);

    /**
     * ClubServer向GameServer请求查询玩家信息
     * 用于玩家加入俱乐部时, 根据roleId查询name, headPic, gameCount信息
     * 查询到的信息会在ClubServer上缓存, 并不会实时更新 (是否需要实时更新?)
     * @param connection
     * @param service
     * @param message
     * @param callback
     */
    @Override
    public void handleMessage(Connection connection, RoleService service, GameProtoBuf.CGJoinRoleInfoREQ message, int callback)
    {
        int roleId = message.getRoleId();
        int inviterId = message.getInviterId();
        int clubId = message.getClubId();

        GameProtoBuf.GCJoinRoleInfoRES.Builder builder = GameProtoBuf.GCJoinRoleInfoRES.newBuilder();
        builder.setClubId(clubId);
        builder.setRoleId(roleId);
        builder.setInviterId(inviterId);

        RoleInfo role = table.Role_info.select(roleId);
        RoleInfo inviter = table.Role_info.select(inviterId);
        if (role == null || inviter == null)
        {
            builder.setResult(PlatformProtocolsConfig.CLC_CLUB_NOT_FOUND);
            connection.write(callback, builder.build());
            return;
        }

        builder.setRoleName(role.getNickname());
        builder.setInviterName(inviter.getNickname());
        builder.setGameCount(role.getTotalGameCount());
        builder.setInviterGameCount(inviter.getTotalGameCount());
        builder.setRoleIcon(role.getHeadImgUrl());
        builder.setInviterIcon(inviter.getHeadImgUrl());
        builder.setResult(PlatformProtocolsConfig.CLC_CLUB_INFO_SUCCESS);
        connection.write(callback, builder.build());
    }
}
