package com.kodgames.club.action.manager;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.manager.ClubManagerService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.club.ClubProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 001 on 2017/3/16.
 */

@ActionAnnotation(messageClass = ClubProtoBuf.CCLClubApplicantREQ.class, actionClass = CCLClubApplicantREQAction.class, serviceClass = ClubManagerService.class)
public class CCLClubApplicantREQAction extends CLProtobufMessageHandler<ClubManagerService, ClubProtoBuf.CCLClubApplicantREQ>
{
    private static final Logger logger = LoggerFactory.getLogger(CCLClubNoticeModifyREQAction.class);

    @Override
    public void handleMessage(Connection connection, ClubManagerService service, ClubProtoBuf.CCLClubApplicantREQ message, int callback)
    {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        if (message.getOptype() == ClubConstants.CLUB_APPLICANT_OPTYPE.ACCEPT)
            connection.write(callback, service.getClubApplicantAcceptProto(message.getClubId(), connection.getRemotePeerID(), message.getRoleId(), message.getTitle()));
        else
            connection.write(callback, service.getClubApplicantRejectProto(message.getClubId(), connection.getRemotePeerID(), message.getRoleId()));
    }

    @Override
    public Object getMessageKey(Connection connection, ClubProtoBuf.CCLClubApplicantREQ message)
    {
        return  message.getClubId();
    }
}
