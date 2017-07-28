package com.kodgames.game.service.gmtools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;
import com.kodgames.message.proto.activity.ActivityProtoBuf.ActivityRankPROTO;
import com.kodgames.message.proto.activity.ActivityProtoBuf.PlayerRankPROTO;

// http://127.0.0.1:13101/gmtools?{%22server_id%22:16842753,%22handler%22:%22RankInfoQueryHandler%22,%22rankType%22:1,%22timeMils%22:1483977600000}

@GmtHandlerAnnotation(handler = "RankInfoQueryHandler")
public class RankInfoQueryHandler implements IGmtoolsHandler
{
	
	private static final Logger logger = LoggerFactory.getLogger(RankInfoQueryHandler.class);

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> jsonMap)
	{
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", 1);
		List<Object> data = new ArrayList<>();

		try
		{
			// 解析请求参数
			int activityId = (int)jsonMap.get("rankType");
			long date = (long)jsonMap.get("timeMils");

			// 获取活动排行
			ActivityService activityService = ServiceContainer.getInstance().getPublicService(ActivityService.class);
			ActivityRankPROTO activityRank = activityService.getRankInfo(activityId, date);

			// 填充排行记录
			for (PlayerRankPROTO rankItem : activityRank.getRecordList())
			{
				Map<String, Object> rankItemData = new HashMap<>();
				rankItemData.put("playerId", rankItem.getRoleId());
				rankItemData.put("nickName", rankItem.getNickname());
				rankItemData.put("score", rankItem.getScore());
				rankItemData.put("rankLevel", rankItem.getRankOrder());
				data.add(rankItemData);
			}

			resultMap.put("data", data);
		}
		catch (Throwable t)
		{
			logger.error("{}", t);
			resultMap.put("data", 0);
		}

		return resultMap;
	}

}
