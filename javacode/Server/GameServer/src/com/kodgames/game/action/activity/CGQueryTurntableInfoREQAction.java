package com.kodgames.game.action.activity;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGQueryTurntableInfoREQ;

@ActionAnnotation(messageClass = CGQueryTurntableInfoREQ.class, actionClass = CGQueryTurntableInfoREQAction.class, serviceClass = ServerService.class)
public class CGQueryTurntableInfoREQAction extends CGProtobufMessageHandler<ServerService, CGQueryTurntableInfoREQ>
{

	@Override
	public void handleMessage(Connection connection, ServerService service, CGQueryTurntableInfoREQ message, int callback)
	{
		CGQueryTurntableInfoREQ.Builder builder = CGQueryTurntableInfoREQ.newBuilder();
		builder.mergeFrom(message).setClientId(connection.getRemotePeerID());

		Connection activityServerConnection = service.getActivityServerConnection();
		activityServerConnection.write(callback, builder.build());
	}

}
