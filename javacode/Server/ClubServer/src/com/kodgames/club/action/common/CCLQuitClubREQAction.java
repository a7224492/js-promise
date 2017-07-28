package com.kodgames.club.action.common;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.service.manager.ClubManagerService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.club.utils.KodBiLogHelper;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.ClubInfo;
import xbean.MemberInfo;
import xbean.RoleClubs;

/**
 * Created by 001 on 2017/3/17.
 */

@ActionAnnotation(messageClass = ClubProtoBuf.CCLQuitClubREQ.class, actionClass = CCLQuitClubREQAction.class, serviceClass = ClubCommonService.class)
public class CCLQuitClubREQAction extends CLProtobufMessageHandler<ClubCommonService, ClubProtoBuf.CCLQuitClubREQ>
{
    private static final Logger logger = LoggerFactory.getLogger(CCLQuitClubREQAction.class);

    @Override
    public void handleMessage(Connection connection, ClubCommonService service, ClubProtoBuf.CCLQuitClubREQ message, int callback)
    {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        int clubId = message.getClubId();
        int roleId = connection.getRemotePeerID();
        ClubProtoBuf.CLCQuitClubRES.Builder builder = ClubProtoBuf.CLCQuitClubRES.newBuilder();
        builder.setClubId(clubId);

        ClubManagerService cms = ServiceContainer.getInstance().getPublicService(ClubManagerService.class);

        ClubInfo club = service.getClubForWrite(clubId);
        if (club == null)
        {
            builder.setResult(PlatformProtocolsConfig.CLC_CLUB_NOT_FOUND);
            connection.write(callback, builder.build());
            return;
        }

        for (MemberInfo member : club.getMembers())
        {
            if (member.getRole().getRoleId() == roleId)
            {
                // 删除玩家俱乐部信息
                RoleClubs roleClubs = table.Role_clubs.update(roleId);
                if (roleClubs != null)
                {
                    roleClubs.getClubs().removeIf(s -> s.getClubId() == clubId);
                }

                member.setStatus(ClubConstants.CLUB_MEMBER_STATUS.LEAVE_OUT);
                cms.clubMemberCountChange(club, -1); // 有效玩家-1

                KodBiLogHelper.clubLeaveLog(clubId, club.getAgentId(), club.getManager().getRoleId(),
                        roleId, roleClubs.getVersion(), roleClubs.getChannel());

                builder.setResult(PlatformProtocolsConfig.CLC_CLUB_QUIT_SUCCESS);
                connection.write(callback, builder.build());
                return;
            }
        }

        builder.setResult(PlatformProtocolsConfig.CLC_CLUB_QUIT_NOT_IN_CLUB);
        connection.write(callback, builder.build());
    }
}
