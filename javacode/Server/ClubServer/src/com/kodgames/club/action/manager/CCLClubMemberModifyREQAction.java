package com.kodgames.club.action.manager;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.manager.ClubManagerService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.club.ClubProtoBuf.CCLClubMemberModifyREQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 001 on 2017/3/17.
 */
@ActionAnnotation(messageClass = CCLClubMemberModifyREQ.class, actionClass = CCLClubMemberModifyREQAction.class, serviceClass = ClubManagerService.class)
public class CCLClubMemberModifyREQAction extends CLProtobufMessageHandler<ClubManagerService, CCLClubMemberModifyREQ>
{
    private static final Logger logger = LoggerFactory.getLogger(CCLClubNoticeModifyREQAction.class);

    @Override
    public void handleMessage(Connection connection, ClubManagerService service, CCLClubMemberModifyREQ message, int callback)
    {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        int managerId = connection.getRemotePeerID();
        connection.write(callback, service.getClubMemberModifyProto(message, managerId));
    }

    @Override
    public Object getMessageKey(Connection connection, CCLClubMemberModifyREQ message)
    {
        return  message.getClubId();
    }
}