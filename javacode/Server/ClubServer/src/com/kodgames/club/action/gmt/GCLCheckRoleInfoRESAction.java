package com.kodgames.club.action.gmt;

import com.kodgames.club.action.room.GCLCreateRoomRESAction;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.service.gmtools.ClubGmToolsCheckCreateService;
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
@ActionAnnotation(messageClass = ClubProtoBuf.GCLCheckRoleInfoRES.class, actionClass = GCLCheckRoleInfoRESAction.class, serviceClass = ClubGmToolsCheckCreateService.class)
public class GCLCheckRoleInfoRESAction extends ProtobufMessageHandler<ClubGmToolsCheckCreateService, ClubProtoBuf.GCLCheckRoleInfoRES>
{
    private static final Logger logger = LoggerFactory.getLogger(GCLCreateRoomRESAction.class);

    @Override
    public void handleMessage(Connection connection, ClubGmToolsCheckCreateService service, ClubProtoBuf.GCLCheckRoleInfoRES message, int callback)
    {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        service.doCheckRoleResponse(message);
    }
}
