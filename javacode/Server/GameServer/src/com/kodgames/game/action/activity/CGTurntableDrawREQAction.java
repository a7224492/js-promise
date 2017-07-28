package com.kodgames.game.action.activity;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGTurntableDrawREQ;
import com.kodgames.message.proto.game.GameProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = CGTurntableDrawREQ.class, actionClass = CGTurntableDrawREQAction.class, serviceClass = ActivityService.class)
public class CGTurntableDrawREQAction extends CGProtobufMessageHandler<ActivityService, CGTurntableDrawREQ>
{

	@Override
	public void handleMessage(Connection connection, ActivityService service, CGTurntableDrawREQ message,
		int callback)
	{
		GameProtoBuf.ActivityEventParam.Builder param = GameProtoBuf.ActivityEventParam.newBuilder();
		param.setInt32Param(0, connection.getRemotePeerID());

		service.triggerActivityEvent(PlatformProtocolsConfig.PLAYER_DRAW_REWARD_EVENT, param.build());
	}
	
}
