package com.kodgames.activityserver.service.event;

import com.kodgames.activityserver.common.activity.event.IActivityEventHandler;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiangzhen on 2017/7/27
 */
public class ActivityEventService extends PublicService
{
	private static final Logger logger = LoggerFactory.getLogger(ActivityEventService.class);
	private Map<Integer, List<IActivityEventHandler>> activityEventHandlerMap = new ConcurrentHashMap<>();

	/**
	 * 添加一个活动事件handler
	 * @param eventId
	 * @param handler
	 */
	public void addActivityEventHandler(int eventId, IActivityEventHandler handler)
	{
		List<IActivityEventHandler> list = activityEventHandlerMap.get(eventId);
		if (list == null)
		{
			list = new ArrayList<>();
		}
		list.add(handler);

		logger.info("add activity event handler : eventId={}, handler={}", eventId, handler);
	}

	/**
	 * 处理一个活动事件
	 * @param eventId
	 * @param param
	 */
	public void triggerActivityEvent(int eventId, GameProtoBuf.ActivityEventParam param)
	{
		List<IActivityEventHandler> list = activityEventHandlerMap.get(eventId);
		if (list == null)
		{
			logger.warn("can't find activity event handler : eventId={}", eventId);
			return;
		}

		for (IActivityEventHandler handler : list)
		{
			handler.handleEvent(eventId, param);
		}
	}
}
