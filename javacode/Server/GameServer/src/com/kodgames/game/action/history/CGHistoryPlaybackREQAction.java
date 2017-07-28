package com.kodgames.game.action.history;

import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.message.proto.game.GameProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.CGHistoryPlaybackREQ;

import java.util.ArrayList;

/**
 * 查询战绩回放数据
 */
@ActionAnnotation(messageClass = CGHistoryPlaybackREQ.class, actionClass = CGHistoryPlaybackREQAction.class, serviceClass = ServerService.class)
public class CGHistoryPlaybackREQAction extends CGProtobufMessageHandler<ServerService, CGHistoryPlaybackREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGHistoryPlaybackREQAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service, CGHistoryPlaybackREQ message, int callback)
	{
		logger.info("QueryHistoryPlaybackDataReq: roomId={} createTime={} index={}.", message.getRoomId(), message.getCreatTime(), message.getRecordIndex());

		// 将请求转发给ReplayServer
		CGHistoryPlaybackREQ.Builder builder = CGHistoryPlaybackREQ.newBuilder();
		builder.mergeFrom(message).setClientId(connection.getRemotePeerID());

		Connection replay = service.getReplayConnection();
		if (replay == null)
		{
			GameProtoBuf.GCHistoryRES.Builder result = GameProtoBuf.GCHistoryRES.newBuilder();
			result.setResult(PlatformProtocolsConfig.GC_HISTORY_SUCCESS);

			result.setVersion(0);

			// 向客户端回复消息
			connection.write(callback, result.build());
			logger.error("replay server connection is null, drop CGHistoryPlaybackREQ packet roleId {}", connection.getRemotePeerID());
		}
		else
			replay.write(callback, builder.build());
	}
}
