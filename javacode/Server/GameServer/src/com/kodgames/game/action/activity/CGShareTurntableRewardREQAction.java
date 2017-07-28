package com.kodgames.game.action.activity;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGShareTurntableRewardREQ;

@ActionAnnotation(messageClass = CGShareTurntableRewardREQ.class, actionClass = CGShareTurntableRewardREQAction.class, serviceClass = ActivityService.class)
public class CGShareTurntableRewardREQAction extends CGProtobufMessageHandler<ActivityService, CGShareTurntableRewardREQ>
{

	@Override
	public void handleMessage(Connection connection, ActivityService service, CGShareTurntableRewardREQ message,
		int callback)
	{
		service.onCGShareTurntableRewardREQ(connection, message, callback);
	}
	
}
