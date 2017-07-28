package com.kodgames.activityserver.action.activity;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.kodgames.activityserver.service.activity.ActivityService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jiangzhen on 2017/7/26
 */
@ActionAnnotation(messageClass = GameProtoBuf.ActivityMemoryDataSYN.class, actionClass = ActivityMemoryDataSYNAction.class, serviceClass = ActivityService.class)
public class ActivityMemoryDataSYNAction extends ProtobufMessageHandler<ActivityService, GameProtoBuf.ActivityMemoryDataSYN>
{
	private static final Logger logger = LoggerFactory.getLogger(ActivityMemoryDataSYNAction.class);

	@Override
	public void handleMessage(Connection connection, ActivityService service,
		GameProtoBuf.ActivityMemoryDataSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", "ActivityMemoryDataSYNAction", connection.getConnectionID(), message);

		// 解析排行版个的数据
		ByteString str = message.getData(0);
		GameProtoBuf.RankActivityMemoryPROTO.Builder builder = GameProtoBuf.RankActivityMemoryPROTO.newBuilder();
		try
		{
			builder.mergeFrom(str.toByteArray());
		}
		catch (InvalidProtocolBufferException e)
		{
			logger.warn("rank activity memory data parse fails!!!");
		}
		// 设置排行榜内存数据
		service.resetRankMemoryData(builder.build());
	}
}
