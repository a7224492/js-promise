package com.kodgames.activityserver.service.activity;

import com.google.protobuf.ByteString;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.message.proto.game.GameProtoBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangzhen on 2017/7/26
 */
public class ActivityService extends PublicService
{
	/**
	 * 把事件列表推送给connection
	 * @param connection
	 */
	public void synEventList(Connection connection)
	{
		GameProtoBuf.ActivityEventListSYN.Builder builder = GameProtoBuf.ActivityEventListSYN.newBuilder();
		List<GameProtoBuf.ActivityEventPROTO> list = returnActivityEventList();
		list.stream().forEach(builder::addEventList);
		connection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
	}

	/**
	 * 取出所有的活动事件
	 * @return
	 */
	private List<GameProtoBuf.ActivityEventPROTO> returnActivityEventList()
	{
		// TODO
		return new ArrayList<GameProtoBuf.ActivityEventPROTO>();
	}

	/**
	 * 处理一个活动事件
	 * @param eventId
	 */
	public void handleActivityEvent(int eventId)
	{
		// TODO
	}

	/**
	 *	返回排行榜的内存数据
	 * @return
	 */
	public GameProtoBuf.RankActivityMemoryPROTO returnRankActivityMemoryPROTO()
	{
		// TODO
		return GameProtoBuf.RankActivityMemoryPROTO.newBuilder().build();
	}

	/**
	 * 重新设置排行的内存数据
	 * @param build
	 */
	public void resetRankMemoryData(GameProtoBuf.RankActivityMemoryPROTO build)
	{
		// TODO
	}
}
