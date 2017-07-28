package com.kodgames.activityserver.action.activity;

import com.kodgames.activityserver.service.activity.ActivityService;
import com.kodgames.activityserver.service.activity.TurntableActivityService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.activity.ActivityProtoBuf;
import com.kodgames.message.proto.game.GameProtoBuf;

import java.util.List;

/**
 * Created by jiangzhen on 2017/7/26
 */
@ActionAnnotation(messageClass = GameProtoBuf.ActivityEventTriggerSYN.class, actionClass = ActivityEventTriggerSYNAction.class, serviceClass = ActivityService.class)
public class ActivityEventTriggerSYNAction extends ProtobufMessageHandler<ActivityService, GameProtoBuf.ActivityEventTriggerSYN>
{
	@Override
	public void handleMessage(Connection connection, ActivityService service,
		GameProtoBuf.ActivityEventTriggerSYN message, int callback)
	{
		// 处理活动事件
		service.handleActivityEvent(message.getEvent().getEventId());
	}
}
