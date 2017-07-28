package com.kodgames.game.action.activity;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.message.proto.activity.ActivityProtoBuf;
import com.kodgames.message.proto.game.GameProtoBuf;

/**
 * Created by jiangzhen on 2017/7/26
 */
@ActionAnnotation(messageClass = GameProtoBuf.ActivityEventListSYN.class, actionClass = ActivityEventListSYNAction.class, serviceClass = ActivityService.class)
public class ActivityEventListSYNAction extends ProtobufMessageHandler<ActivityService, GameProtoBuf.ActivityEventListSYN>
{
	@Override
	public void handleMessage(Connection connection, ActivityService service, GameProtoBuf.ActivityEventListSYN message,
		int callback)
	{
		service.setAllActivityEventStatus(message.getEventListList());
	}
}
