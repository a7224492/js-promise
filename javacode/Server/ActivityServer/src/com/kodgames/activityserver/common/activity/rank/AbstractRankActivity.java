package com.kodgames.activityserver.common.activity.rank;

import com.kodgames.activityserver.common.activity.condition.ICondition;
import com.kodgames.activityserver.common.activity.event.IActivityEventHandler;
import com.kodgames.activityserver.service.event.ActivityEventService;
import com.kodgames.corgi.core.constant.DateTimeConstants;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.util.DateTimeUtil;
import com.kodgames.message.proto.activity.ActivityProtoBuf;
import com.kodgames.message.proto.game.GameProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiangzhen on 2017/7/27
 */
abstract class AbstractRankActivity implements IRankActivity
{
	/**
	 * 日榜
	 */
	private RankList dayRankList = new RankList();

	/**
	 * 周榜
	 */
	private RankList weekRankList = new RankList();

	/**
	 * 月榜
	 */
	private RankList monthRankList = new RankList();

	/**
	 * 总榜
	 */
	private IRank totalRank = new DefaultRank();

	/**
	 * 排行榜日期类型对应的排行榜map
	 */
	private Map<Integer, RankList> rankMap = new ConcurrentHashMap<>();

	public AbstractRankActivity()
	{
		rankMap.put(RankConstans.RANK_DATE_TYPE_DAY, dayRankList);
		rankMap.put(RankConstans.RANK_DATE_TYPE_WEEK, weekRankList);
		rankMap.put(RankConstans.RANK_DATE_TYPE_MONTH, monthRankList);

		ActivityEventService service = ServiceContainer.getInstance().getPublicService(ActivityEventService.class);
		service.addActivityEventHandler(PlatformProtocolsConfig.PLAYER_QUERY_RANK_EVENT, new PlayerQueryRankHandler());
	}

	@Override
	public void updateRank(int rankDateType, long date, IRankData data)
	{
		RankList rankList = rankMap.get(rankDateType);
		int index = getRankIndex(date);
		rankList.get(index).updateRank(data);
	}

	@Override
	public <S extends ICondition> S timeCondition()
	{
		// TODO
		return null;
	}

	/**
	 * 根据时间戳得到排行榜
	 * @param date
	 * @return
	 */
	private int getRankIndex(long date)
	{
		// TODO
		return 0;
	}

	private class PlayerQueryRankHandler implements IActivityEventHandler
	{

		@Override
		public void handleEvent(int eventId, GameProtoBuf.ActivityEventParam param)
		{
			int result = -1;
			ICondition timeCondition = timeCondition();
			if (!timeCondition.isSatisfy())
			{
				// TODO 不再活动的时间范围内
				result = -1;
			}

			long now = System.currentTimeMillis();
			IRank dayRank = queryRank(RankConstans.RANK_DATE_TYPE_DAY, now);

			ActivityProtoBuf.PlayerRankPROTO.Builder playerRank = ActivityProtoBuf.PlayerRankPROTO.newBuilder();

//			ActivityProtoBuf.GCActivityRankRES.Builder builder = ActivityProtoBuf.GCActivityRankRES.newBuilder();
//
//			// 检查排行榜是否可查询
//			int result;
//			ICondition timeCondition = timeCondition();
//			if (!timeCondition.isSatisfy())
//			{
//				// TODO 不在活动的时间范围内
//				result = -1;
//			}
//
////			builder.setResult(result);
//			if(result != PlatformProtocolsConfig.GC_ACTIVITY_RANK_SUCCESS)
//			{
//				// 告知客户端错误原因
////				connection.write(callback, builder.build());
//				return;
//			}
//
//			// 设置所有玩家排行记录
//			long today = DateTimeConstants.getDate(System.currentTimeMillis());
//			List<ActivityProtoBuf.ActivityRankPROTO> rankList = service.getRankInfo(today);
//			builder.addAllRank(rankList);
//
//			// 设置当前玩家的历史排行记录
////			int roleId = connection.getRemotePeerID();
//			List<ActivityProtoBuf.ActivityRankPROTO> historyRankList = service.getHistoryRankInfo(roleId, today);
//			builder.addAllHistoryRank(historyRankList);
//
//			// 应答排行请求
////			connection.write(callback, builder.build());
		}
	}
}

class RankList
{
	private List<IRank> list = new ArrayList<>();

	public IRank get(int index)
	{
		while (index >= list.size())
		{
			list.add(new DefaultRank());
		}

		return list.get(index);
	}

	public boolean add(IRank rank)
	{
		return list.add(rank);
	}

	public List<IRank> getRankList()
	{
		return list;
	}
}