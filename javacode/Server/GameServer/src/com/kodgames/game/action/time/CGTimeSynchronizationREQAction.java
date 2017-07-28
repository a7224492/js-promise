package com.kodgames.game.action.time;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.service.time.TimeService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.CGTimeSynchronizationREQ;

@ActionAnnotation(messageClass = CGTimeSynchronizationREQ.class, actionClass = CGTimeSynchronizationREQAction.class, serviceClass = TimeService.class)
public class CGTimeSynchronizationREQAction extends CGProtobufMessageHandler<TimeService, CGTimeSynchronizationREQ>
{
	@Override
	public void handleMessage(Connection connection, TimeService service, CGTimeSynchronizationREQ message,
		int callback)
	{
		service.onTimeSynchronizationREQ(connection, message, callback);
	}
	
}
