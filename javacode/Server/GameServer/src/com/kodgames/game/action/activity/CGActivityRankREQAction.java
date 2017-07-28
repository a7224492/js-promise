package com.kodgames.game.action.activity;

import java.util.List;

import com.kodgames.corgi.core.constant.DateTimeConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.game.common.Constant.ActivityId;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.message.proto.activity.ActivityProtoBuf.ActivityRankPROTO;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGActivityRankREQ;
import com.kodgames.message.proto.activity.ActivityProtoBuf.GCActivityRankRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = CGActivityRankREQ.class, actionClass = CGActivityRankREQAction.class, serviceClass = ActivityService.class)
public class CGActivityRankREQAction extends CGProtobufMessageHandler<ActivityService, CGActivityRankREQ>
{

	@Override
	public void handleMessage(Connection connection, ActivityService service, CGActivityRankREQ message, int callback)
	{
		GCActivityRankRES.Builder builder = GCActivityRankRES.newBuilder();

		// 检查排行榜是否可查询
		int result = service.checkQueryable(ActivityId.SCORE_RANK.getId());
		builder.setResult(result);
		if(result != PlatformProtocolsConfig.GC_ACTIVITY_RANK_SUCCESS)
		{
			// 告知客户端错误原因
			connection.write(callback, builder.build());
			return;
		}

		// 设置所有玩家排行记录
		long today = DateTimeConstants.getDate(System.currentTimeMillis());
		List<ActivityRankPROTO> rankList = service.getRankInfo(today);
		builder.addAllRank(rankList);

		// 设置当前玩家的历史排行记录
		int roleId = connection.getRemotePeerID();
		List<ActivityRankPROTO> historyRankList = service.getHistoryRankInfo(roleId, today);
		builder.addAllHistoryRank(historyRankList);

		// 应答排行请求
		connection.write(callback, builder.build());
	}

}
