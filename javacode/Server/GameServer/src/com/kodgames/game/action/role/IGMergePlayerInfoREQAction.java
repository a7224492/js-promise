package com.kodgames.game.action.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.message.proto.auth.AuthProtoBuf.IGMergePlayerInfoREQ;


@ActionAnnotation(messageClass = IGMergePlayerInfoREQ.class, actionClass = IGMergePlayerInfoREQAction.class, serviceClass = RoleService.class)
public class IGMergePlayerInfoREQAction extends ProtobufMessageHandler<RoleService, IGMergePlayerInfoREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(IGMergePlayerInfoREQAction.class);
	
	@Override
	public void handleMessage(Connection connection, RoleService service, IGMergePlayerInfoREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		service.mergePlayerInfo(connection, message, callback);
	}
	
}
