package com.kodgames.club.action.manager;

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

@ActionAnnotation(messageClass = ClubProtoBuf.CCLClubApplicantListREQ.class, actionClass = CCLClubApplicantListREQAction.class, serviceClass = ClubManagerService.class)
public class CCLClubApplicantListREQAction extends CLProtobufMessageHandler<ClubManagerService, ClubProtoBuf.CCLClubApplicantListREQ>
{
    private static final Logger logger = LoggerFactory.getLogger(CCLClubApplicantListREQAction.class);

    @Override
    public void handleMessage(Connection connection, ClubManagerService service, ClubProtoBuf.CCLClubApplicantListREQ message, int callback)
    {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        connection.write(callback, service.getClubApplicantsProto(message.getClubId(), connection.getRemotePeerID()));
    }
}
