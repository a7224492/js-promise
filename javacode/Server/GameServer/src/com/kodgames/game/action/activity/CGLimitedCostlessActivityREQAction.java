package com.kodgames.game.action.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGLimitedCostlessActivityREQ;

@ActionAnnotation(messageClass = CGLimitedCostlessActivityREQ.class, actionClass = CGLimitedCostlessActivityREQAction.class, serviceClass = ActivityService.class)
public class CGLimitedCostlessActivityREQAction extends ProtobufMessageHandler<ActivityService, CGLimitedCostlessActivityREQ>
{
	private static Logger logger = LoggerFactory.getLogger(CGLimitedCostlessActivityREQAction.class);

	@Override
	public void handleMessage(Connection connection, ActivityService service, CGLimitedCostlessActivityREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getRemotePeerID(), message);
		
		service.onLimitedCostlessActivityREQ(connection, message, callback);
	}
	
}
