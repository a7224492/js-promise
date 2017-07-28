package com.kodgames.activityserver.common.activity.event;

import com.kodgames.message.proto.game.GameProtoBuf;

/**
 * Created by jiangzhen on 2017/7/27.
 */
public interface IActivityEventHandler
{
	/**
	 * 处理一个活动事件
	 * @param eventId 活动事件id
	 * @param param 活动事件参数
	 */
	public void handleEvent(int eventId, GameProtoBuf.ActivityEventParam param);
}
