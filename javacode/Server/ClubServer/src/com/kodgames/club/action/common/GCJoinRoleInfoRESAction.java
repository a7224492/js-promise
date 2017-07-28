package com.kodgames.club.action.common;

import com.kodgames.club.action.room.GCLCreateRoomRESAction;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.utils.ClubUtils;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.proto.game.GameProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 001 on 2017/3/17.
 */
@ActionAnnotation(messageClass = GameProtoBuf.GCJoinRoleInfoRES.class, actionClass = GCJoinRoleInfoRESAction.class, serviceClass = ClubCommonService.class)
public class GCJoinRoleInfoRESAction extends ProtobufMessageHandler<ClubCommonService, GameProtoBuf.GCJoinRoleInfoRES>
{
    private static final Logger logger = LoggerFactory.getLogger(GCLCreateRoomRESAction.class);

    @Override
    public void handleMessage(Connection connection, ClubCommonService service, GameProtoBuf.GCJoinRoleInfoRES message, int callback)
    {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        int roleId = message.getRoleId();
        String roleName = message.getRoleName();
        int gameCount = message.getGameCount();
        String roleIcon = message.getRoleIcon();
        int inviterId = message.getInviterId();
        String inviterName = message.getInviterName();
        String inviterIcon = message.getInviterIcon();
        int inviterGameCount = message.getInviterGameCount();
        int clubId = message.getClubId();

        // 客户端通过invitation code查询俱乐部和邀请人信息时, 将查询请求转发给GameServer
        // 从GameServer返回信息后再将查询结果返回给客户端
        // 同时把查询到的信息加到ClubServer的缓存
        service.addRoleInfo(roleId, roleName, gameCount, roleIcon);
        service.addRoleInfo(inviterId, inviterName, inviterGameCount, inviterIcon);

        ClubProtoBuf.CLCJoinClubInfoRES.Builder builder = ClubProtoBuf.CLCJoinClubInfoRES.newBuilder();
        ClubProtoBuf.ClubInfoPROTO.Builder protoBuilder = service.getClubLiteInfo(clubId);
        if (protoBuilder == null)
        {
            builder.setResult(PlatformProtocolsConfig.CLC_CLUB_NOT_FOUND);
            builder.setInfo("club not found");
        } else {
            builder.setClubInfo(protoBuilder);
            builder.setInviter(inviterId);
            builder.setInviterName(inviterName);
            builder.setInviterIcon(inviterIcon);
            builder.setResult(PlatformProtocolsConfig.CLC_CLUB_INFO_SUCCESS);
        }

        // game转播
        ClubUtils.broadcastMsg2Game(callback, roleId, builder.build());
    }
}
