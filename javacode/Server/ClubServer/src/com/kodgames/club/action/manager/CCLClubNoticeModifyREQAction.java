package com.kodgames.club.action.manager;

import com.kodgames.club.service.manager.ClubManagerService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.ClubInfo;
import xbean.ClubRoleBaseInfo;

/**
 * Created by 001 on 2017/3/16.
 */

@ActionAnnotation(messageClass = ClubProtoBuf.CCLClubNoticeModifyREQ.class, actionClass = CCLClubNoticeModifyREQAction.class, serviceClass = ClubManagerService.class)
public class CCLClubNoticeModifyREQAction extends CLProtobufMessageHandler<ClubManagerService, ClubProtoBuf.CCLClubNoticeModifyREQ>
{
    private static final Logger logger = LoggerFactory.getLogger(CCLClubNoticeModifyREQAction.class);

    @Override
    public void handleMessage(Connection connection, ClubManagerService service, ClubProtoBuf.CCLClubNoticeModifyREQ message, int callback)
    {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        int clubId = message.getClubId();
        int roleId = connection.getRemotePeerID();
        String notice = message.getNotice();
        ClubProtoBuf.CLCClubNoticeModifyRES.Builder builder = ClubProtoBuf.CLCClubNoticeModifyRES.newBuilder();
        builder.setClubId(clubId);

        ClubInfo club = service.getClubInfoForWrite(clubId);
        if (club == null)
        {
            builder.setResult(PlatformProtocolsConfig.CLC_CLUB_NOTICEMODIFY_CLUB_NOT_FOUND);
        }
        else
        {
            ClubRoleBaseInfo manager = club.getManager();
            if (manager == null || manager.getRoleId() != roleId)
            {
                builder.setResult(PlatformProtocolsConfig.CLC_CLUB_NOTICEMODIFY_NOT_PERMITED);
                builder.setNotice(club.getNotice());
            }
            else
            {
                club.setNotice(notice);
                club.setNoticeTime(System.currentTimeMillis());
                builder.setResult(PlatformProtocolsConfig.CLC_CLUB_NOTICEMODIFY_SUCCESS);
                builder.setNotice(notice);
            }
        }

        connection.write(callback, builder.build());
    }

    @Override
    public Object getMessageKey(Connection connection, ClubProtoBuf.CCLClubNoticeModifyREQ message)
    {
        return  message.getClubId();
    }
}
