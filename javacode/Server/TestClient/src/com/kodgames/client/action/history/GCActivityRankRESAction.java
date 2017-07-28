package com.kodgames.client.action.history;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.account.RoleService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.activity.ActivityProtoBuf.GCActivityRankRES;

@ActionAnnotation(actionClass = GCActivityRankRESAction.class, messageClass = GCActivityRankRES.class, serviceClass = RoleService.class)
public class GCActivityRankRESAction extends ProtobufMessageHandler<RoleService, GCActivityRankRES>
{
	private static final Logger logger = LoggerFactory.getLogger(GCActivityRankRESAction.class);

	@Override
	public void handleMessage(Connection connection, RoleService service, GCActivityRankRES message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
	}
}