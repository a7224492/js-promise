package com.kodgames.club.action.common;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.constant.ClubRights;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.service.server.ServerService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.club.utils.ClubUtils;
import com.kodgames.club.utils.InviteCodeEncoder;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.proto.game.GameProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.ClubManager;
import xbean.MemberInfo;

/**
 * Created by 001 on 2017/3/21.
 */

@ActionAnnotation(messageClass = ClubProtoBuf.CCLJoinClubInfoREQ.class, actionClass = CCLJoinClubInfoREQAction.class, serviceClass = ClubCommonService.class)
public class CCLJoinClubInfoREQAction extends CLProtobufMessageHandler<ClubCommonService, ClubProtoBuf.CCLJoinClubInfoREQ>
{
    private static final Logger logger = LoggerFactory.getLogger(CCLJoinClubInfoREQAction.class);

    @Override
    public void handleMessage(Connection connection, ClubCommonService service, ClubProtoBuf.CCLJoinClubInfoREQ message, int callback)
    {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        String code = message.getInvitationCode();
        ClubProtoBuf.CLCJoinClubInfoRES.Builder builder = ClubProtoBuf.CLCJoinClubInfoRES.newBuilder();

        int roleId = connection.getRemotePeerID();
        int clubId = 0;
        int inviterId = 0;
        try
        {
            InviteCodeEncoder.ClubCodeInfo clubInfo = InviteCodeEncoder.getClubInfo(code);
            inviterId = clubInfo.getRoleId();
            clubId = clubInfo.getClubId();
        }
        catch (Exception e)
        {
            logger.warn("get club info from invitation code failed: {}, reason: {}", code, e.getMessage());
            builder.setResult(PlatformProtocolsConfig.CLC_CLUB_WRONG_CODE);
            builder.setInfo("invalid invitation code");
            connection.write(callback, builder.build());
            return;
        }

        if (!service.checkClubId(clubId))
        {
            builder.setResult(PlatformProtocolsConfig.CLC_CLUB_WRONG_CODE);
            builder.setInfo("club not found");
            connection.write(callback, builder.build());
            return;
        }

        // 检查邀请人是否有权限
        MemberInfo inviterMember = service.getMemberInfo(inviterId, clubId);
        if (inviterMember == null || !ClubRights.hasRight(inviterMember, ClubConstants.CLUB_ACTIONS.Invitation))
        {
            builder.setResult(PlatformProtocolsConfig.CLC_CLUB_WRONG_CODE);
            builder.setInfo("inviter not permitted");
            connection.write(callback, builder.build());
            return;
        }

        // 经理不允许加入其他俱乐部
        ClubManager managerInfo = table.Club_manager.select(roleId);
        if (managerInfo != null)
        {
            builder.setResult(PlatformProtocolsConfig.CLC_CLUB_JOIN_NOT_PERMITTED);
            builder.setInfo("manager can't join other club");
            connection.write(callback, builder.build());
            return;
        }

        // 检查本地缓存中有没有邀请人信息
        ClubCommonService.GameRoleInfo roleInfo = service.getRoleInfo(roleId);
        ClubCommonService.GameRoleInfo inviterInfo = service.getRoleInfo(inviterId);
        if (roleInfo != null && inviterInfo != null)
        {
            ClubProtoBuf.ClubInfoPROTO.Builder protoBuilder = service.getClubLiteInfo(clubId);
            assert (protoBuilder != null);

            builder.setClubInfo(protoBuilder);
            builder.setInviter(inviterId);
            builder.setInviterName(inviterInfo.roleName);
            builder.setInviterIcon(inviterInfo.roleIcon);
            builder.setResult(PlatformProtocolsConfig.CLC_CLUB_INFO_SUCCESS);
            connection.write(callback, builder.build());
            return;
        }

        // 没有则向GameServer请求
        ServerService ss = ServiceContainer.getInstance().getPublicService(ServerService.class);
        Connection gameConnection = ss.getGameConnection();

        GameProtoBuf.CGJoinRoleInfoREQ.Builder ssBuilder = GameProtoBuf.CGJoinRoleInfoREQ.newBuilder();
        ssBuilder.setRoleId(roleId);
        ssBuilder.setInviterId(inviterId);
        ssBuilder.setClubId(clubId);
        gameConnection.write(callback, ssBuilder.build());
    }
}
