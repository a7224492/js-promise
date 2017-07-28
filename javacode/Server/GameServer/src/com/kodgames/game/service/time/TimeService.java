package com.kodgames.game.service.time;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.util.DateTimeUtil;
import com.kodgames.message.proto.game.GameProtoBuf.CGTimeSynchronizationREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCTimeSynchronizationRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

public class TimeService extends PublicService
{
	private static final long serialVersionUID = -2740300027926829576L;

	/**
	 * 时间同步, 返回服务器当前时间
	 * @param connection
	 * @param message
	 * @param callback
     */
	public void onTimeSynchronizationREQ(Connection connection, CGTimeSynchronizationREQ message, int callback)
	{
		connection.write(callback,
			GCTimeSynchronizationRES.newBuilder()
				.setResult(PlatformProtocolsConfig.GC_TIME_SYNCHRONIZATION_SUCCESS)
				.setTimeStamp(DateTimeUtil.getCurrentTimeMillis())
				.build());
	}

}
