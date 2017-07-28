package com.kodgames.game.service.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.message.proto.game.GameProtoBuf;
import jdk.nashorn.internal.objects.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.DateTimeConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.game.common.Constant.ActivityId;
import com.kodgames.game.common.Constant.ActivityType;
import com.kodgames.game.service.activity.version.IVersion;
import com.kodgames.game.service.activity.version.RankActivityVersionImp;
import com.kodgames.message.proto.activity.ActivityProtoBuf.ActivityRankPROTO;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGLimitedCostlessActivityREQ;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGQueryTurntableInfoREQ;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGShareTurntableRewardREQ;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGTurntableDrawREQ;
import com.kodgames.message.proto.game.GameProtoBuf.BGFinalMatchResultSYN;
import com.kodgames.message.proto.game.GameProtoBuf.PlayerHistoryPROTO;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import limax.codec.OctetsStream;
import limax.zdb.XBean;
import xbean.ActivityHistoryRank;
import xbean.ActivityRank;
import xbean.DateActivityRank;
import xbean.HistoryRank;
import xbean.RoleHistoryRank;
import xbean.RoleRank;

public class ActivityService extends PublicService
{

	private static final long serialVersionUID = 4349572819234608708L;
	private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:m:s");
	private final String activityStartTimeKey = "activityStart";
	private final String activityEndTimeKey = "activityEnd";
	private final String queryStartTimeKey = "activityQueryStart";
	private final String queryEndTimeKey = "activityQueryEnd";
	private final String rankCountKey = "activityRankCount";
	private final String rankRuleGroupsKey = "activityRankRules";

	private Map<Integer, ActivityConfig> configMap = new HashMap<>();
	private int rankCount;
	private Set<Integer> rankRoundTypes = new HashSet<>();

	/**
	 * 转盘活动
	 */
	TurntableActivity turntable = new TurntableActivity();

	/**
	 * 限免活动
	 */
	LimitedCostlessActivityService lca = new LimitedCostlessActivityService();
	
	/**
	 * 初始化活动配置
	 * 
	 * @throws ParseException
	 */
	public void initConfig(Map<String, String> config)
		throws ParseException
	{
		// 设置活动时间
		for (ActivityId type : ActivityId.getActivityIds())
		{
			ActivityConfig activityConfig = new ActivityConfig();

			// 活动开始时间
			String activityStartStr = config.get(activityStartTimeKey + "_" + type.getId());
			if (activityStartStr == null)
			{
				// 忽略没配置的活动
				continue;
			}
			long activityStartTime = dateStringToLong(activityStartStr);
			activityConfig.setActivityStartTime(activityStartTime);

			// 活动结束时间
			String activityEndStr = config.get(activityEndTimeKey + "_" + type.getId());
			long activityEndTime = dateStringToLong(activityEndStr);
			activityConfig.setActivityEndTime(activityEndTime);

			// 活动显示开始时间
			String queryStartStr = config.get(queryStartTimeKey + "_" + type.getId());
			long queryStartTime = dateStringToLong(queryStartStr);
			activityConfig.setQueryStartTime(queryStartTime);

			// 活动显示结束时间
			String queryEndStr = config.get(queryEndTimeKey + "_" + type.getId());
			long queryEndTime = dateStringToLong(queryEndStr);
			activityConfig.setQueryEndTime(queryEndTime);

			// 保存活动配置
			configMap.put(type.getId(), activityConfig);
		}

		// 设置排行榜人数
		rankCount = Integer.parseInt(config.get(rankCountKey));
		
		// 设置排行榜支持的圈局规则
		String rankRoundTypePropValue = config.get(rankRuleGroupsKey);
		if (rankRoundTypePropValue != null)
		{
			String[] rankRoundTypeStrArray = rankRoundTypePropValue.split(",");
			for(String rankRoundTypeStr : rankRoundTypeStrArray)
			{
				int roundType = Integer.parseInt(rankRoundTypeStr);
				rankRoundTypes.add(roundType);
			}	
		}
		
		// 初始化版本号
		IVersion iVersion = new RankActivityVersionImp();
		iVersion.init(config.get("rankActivityVersion"));
	}

	int getRankCount()
	{
		return rankCount;
	}
	
	/**
	 * 当前圈局规则是否支持排行榜
	 */
	public boolean isRankSupported(int roundType)
	{
		// 没有配置，默认支持排行榜
		if (rankRoundTypes.isEmpty())
		{
			return true;
		}
		
		return rankRoundTypes.contains(roundType);
	}

	public Map<Integer, ActivityConfig> getConfigMap()
	{
		return configMap;
	}

	private long dateStringToLong(String str)
		throws ParseException
	{
		Date date = null;
		date = dateFormat.parse(str);

		return date.getTime();
	}

	private long getCurrentTime()
	{
		return System.currentTimeMillis();
	}

	private long getCurrentDate()
	{
		return DateTimeConstants.getDate(getCurrentTime());
	}

	/**
	 * 活动是否处于激活状态
	 */
	public boolean isActivated(int activityId)
	{
		ActivityConfig activityConfig = configMap.get(activityId);
		if (activityConfig == null)
		{
			return false;
		}

		long now = getCurrentTime();
		if (now < activityConfig.getActivityStartTime() || now > activityConfig.getActivityEndTime())
		{
			return false;
		}

		return true;
	}

	/**
	 * 活动是否处于激活状态
	 */
	public int checkQueryable(int activityId)
	{
		ActivityConfig activityConfig = configMap.get(activityId);
		if (activityConfig == null)
		{
			return PlatformProtocolsConfig.GC_ACTIVITY_FAILED_NOT_EXIST;
		}

		long now = getCurrentTime();
		if (now < activityConfig.getQueryStartTime())
		{
			return PlatformProtocolsConfig.GC_ACTIVITY_FAILED_TOO_EARLY;
		}
		if (now > activityConfig.getQueryEndTime())
		{
			return PlatformProtocolsConfig.GC_ACTIVITY_FAILED_TOO_LATE;
		}

		return PlatformProtocolsConfig.GC_ACTIVITY_RANK_SUCCESS;
	}

	/**
	 * 更新活动排行
	 */
	public void updateRank(final BGFinalMatchResultSYN result)
	{
		long date = getCurrentDate();

		for (PlayerHistoryPROTO playerHistory : result.getPlayerRecordsList())
		{
			// 只计算正分
			int totalPoint = playerHistory.getTotalPoint();
			if (totalPoint <= 0)
			{
				continue;
			}

			// 更新所有排名活动
			for (ActivityType type : ActivityType.getRankTypes())
			{
				// 查询排行历史
				int roleId = playerHistory.getRoleId();
				HistoryRank historyRank = getActivityHistoryRank(roleId, type.getType(), date, true);

				// 累计分数
				historyRank.setScore(historyRank.getScore() + totalPoint);

				// 更新排行
				updateRank(roleId, playerHistory.getNickname(), type.getType(), date, historyRank);
			}
		}
	}

	/**
	 * 更新指定玩家在指定日期的活动排行
	 */
	private void updateRank(int roleId, String nickName, int activityId, long date, HistoryRank historyRank)
	{
		// 构造玩家排名
		RoleRank roleRank = new RoleRank();
		roleRank.setRoleId(roleId);
		roleRank.setNickname(nickName);
		roleRank.setScore(historyRank.getScore());

		// 获取指定活动、指定日期排行信息
		DateActivityRank dateRank = getDateActivityRank(activityId, date, true);
		List<RoleRank> roleRankList = dateRank.getRoleRanks();

		// 删除玩家的旧记录
		for (int deleteIndex = 0; deleteIndex < roleRankList.size(); ++deleteIndex)
		{
			RoleRank deleteRank = roleRankList.get(deleteIndex);
			if (deleteRank.getRoleId() == roleId)
			{
				roleRankList.remove(deleteIndex);
				break;
			}
		}

		// 处理空排行
		if (roleRankList.isEmpty() == true)
		{
			// 加入玩家排行信息
			roleRankList.add(roleRank);
			return;
		}

		// 将玩家信息插入到排行列表中
		int index = roleRankList.size() - 1;
		while (index >= 0 && roleRankList.get(index).getScore() < roleRank.getScore())
		{
			--index;
		}
		roleRankList.add(index + 1, roleRank);

		// 移除多余的排名记录
		while (roleRankList.size() > rankCount)
		{
			roleRankList.remove(roleRankList.size() - 1);
		}
	}

	private ActivityRank getAndFillActivityRank(int activityId)
	{
		ActivityRank activityRank = table.Activity_rank.update(activityId);
		if (activityRank == null)
		{
			activityRank = table.Activity_rank.insert(activityId);
		}

		return activityRank;
	}

	/**
	 * 获取指定活动、指定日期的所有玩家排名
	 */
	private DateActivityRank getDateActivityRank(int activityId, long date, boolean write)
	{
		ActivityRank activityRank = getAndFillActivityRank(activityId);
		Map<Long, DateActivityRank> dateRankMap = activityRank.getDateRank();

		try
		{
			return getAndFillDateRank(activityId, date, dateRankMap, DateActivityRank.class, write);
		}
		catch (Throwable t)
		{
			logger.error("{}", t);
			return null;
		}
	}

	/**
	 * 获取指定玩家在指定日期、指定活动的历史排行
	 */
	private HistoryRank getActivityHistoryRank(int roleId, int activityId, long date, boolean write)
	{
		ActivityHistoryRank activityRank = getActivityHistoryRank(roleId, activityId);
		Map<Long, HistoryRank> dateRankMap = activityRank.getRanks();

		try
		{
			return getAndFillDateRank(activityId, date, dateRankMap, HistoryRank.class, write);
		}
		catch (Throwable t)
		{
			logger.error("{}", t);
			return null;
		}
	}

	/**
	 * 获取指定活动、指定日期的排行
	 * 
	 * 该日期无记录的累计排名，将自动填充为之前的排名结果
	 */
	private <T extends XBean> T getAndFillDateRank(int activityId, long date, Map<Long, T> dateRankMapProto, Class<T> rankClass, boolean write)
		throws Exception
	{
		// 如果允许修改原数据，就使用原数据填充，否则用原数据的拷贝来填充
		Map<Long, T> dateRankMap = write ? dateRankMapProto : new HashMap<>(dateRankMapProto);

		// 已有相应日期的排行，直接返回
		T dateRank = dateRankMap.get(date);
		if (dateRank != null)
		{
			return dateRank;
		}

		// 获取上一次排行记录
		T lastDateRank = null;
		ActivityConfig config = configMap.get(ActivityId.SCORE_RANK.getId());
		long beginDate = DateTimeConstants.getDate(config.getActivityStartTime());
		long lastDate = 0;
		for (long queryDate = date - DateTimeConstants.DAY; queryDate >= beginDate; queryDate -= DateTimeConstants.DAY)
		{
			lastDateRank = dateRankMap.get(queryDate);
			if (lastDateRank != null)
			{
				lastDate = queryDate;
				break;
			}
		}
		if (lastDateRank == null)
		{
			lastDate = beginDate;
			dateRankMap.put(lastDate, rankClass.newInstance());
		}

		// 用上次排行记录填补之后无排行的日期
		boolean isAccumulated = ActivityType.isAccumulated(activityId);
		for (long fillDate = lastDate + DateTimeConstants.DAY; fillDate <= date; fillDate += DateTimeConstants.DAY)
		{
			T rank = rankClass.newInstance();
			if (isAccumulated == true && lastDateRank != null)
			{
				OctetsStream os = new OctetsStream();
				lastDateRank.marshal(os);
				rank.unmarshal(os);
			}

			dateRankMap.put(fillDate, rank);
		}

		return dateRankMap.get(date);
	}

	private RoleHistoryRank getAndFillRoleHistoryRank(int roleId)
	{
		RoleHistoryRank roleHistoryRank = table.Activity_history_rank.update(roleId);
		if (roleHistoryRank == null)
		{
			roleHistoryRank = table.Activity_history_rank.insert(roleId);
		}

		return roleHistoryRank;
	}

	/**
	 * 获取指定玩家在指定日期的历史排行
	 */
	private ActivityHistoryRank getActivityHistoryRank(int roleId, int activityId)
	{
		RoleHistoryRank roleHistoryRank = getAndFillRoleHistoryRank(roleId);
		Map<Integer, ActivityHistoryRank> activityRankMap = roleHistoryRank.getRanks();
		ActivityHistoryRank activityRank = activityRankMap.get(activityId);
		if (activityRank == null)
		{
			activityRank = new ActivityHistoryRank();
			activityRankMap.put(activityId, activityRank);
		}

		return activityRank;
	}

	/**
	 * 获取指定日期的所有玩家排行
	 */
	public List<ActivityRankPROTO> getRankInfo(long date)
	{
		List<ActivityRankPROTO> rankList = new ArrayList<>();

		for (ActivityType type : ActivityType.getRankTypes())
		{
			ActivityRankPROTO proto = getRankInfo(type.getType(), date);
			rankList.add(proto);
		}

		return rankList;
	}

	/**
	 * 获取指定日期的所有玩家排行
	 * 
	 * 最多只能查到活动最后一天
	 */
	public ActivityRankPROTO getRankInfo(int activityType, long date)
	{
		ActivityConfig config = configMap.get(ActivityId.SCORE_RANK.getId());
		long queryDate = Math.min(date, DateTimeConstants.getDate(config.getActivityEndTime()));

		// 查询排名，并转为PROTO
		DateActivityRank dateRank = getDateActivityRank(activityType, queryDate, false);
		ActivityRankPROTO proto = ActivityData.dateRankBeanToProto(activityType, queryDate, dateRank);

		return proto;
	}

	/**
	 * 获取一个玩家 从活动开始到指定日期的 所有历史排行记录
	 * 
	 * 最多只能查到活动最后一天
	 */
	public List<ActivityRankPROTO> getHistoryRankInfo(int roleId, long date)
	{
		List<ActivityRankPROTO> rankList = new ArrayList<>();
		ActivityConfig config = configMap.get(ActivityId.SCORE_RANK.getId());
		long queryDate = Math.min(date, DateTimeConstants.getDate(config.getActivityEndTime()));

		for (ActivityType type : ActivityType.getRankTypes())
		{
			// 获取玩家在一种活动中的所有排行记录
			RoleHistoryRank roleHistoryRank = getAndFillRoleHistoryRank(roleId);
			Map<Integer, xbean.ActivityHistoryRank> activityHistoryRankMap = roleHistoryRank.getRanks();
			ActivityHistoryRank activityHistoryRank = activityHistoryRankMap.get(type.getType());
			if (activityHistoryRank == null)
			{
				activityHistoryRank = new ActivityHistoryRank();
			}

			// 填充没有排行记录的日期
			Map<Long, HistoryRank> dateRankMap = new HashMap<Long, HistoryRank>(activityHistoryRank.getRanks());
			try
			{
				getAndFillDateRank(type.getType(), queryDate, dateRankMap, HistoryRank.class, true);
			}
			catch (Exception e)
			{
				logger.error("{}", e);
			}

			// 转换数据格式
			ActivityRankPROTO proto = ActivityData.historyRankBeanToProto(roleId, type.getType(), queryDate, dateRankMap);
			rankList.add(proto);
		}

		return rankList;
	}

	/**
	 * 指定玩家、指定活动、指定日期的排名
	 */
	int getRank(int roleId, int activityId, long date, int score)
	{
		if (score == 0)
		{
			return rankCount + 1;
		}

		DateActivityRank dateRank = getDateActivityRank(activityId, date, false);
		List<RoleRank> roleRankList = dateRank.getRoleRanks();
		int rank = rankCount + 1;
		for (int index = roleRankList.size() - 1; index >= 0; --index)
		{
			RoleRank roleRank = roleRankList.get(index);
			if (score < roleRank.getScore())
			{
				break;
			}

			if (roleRank.getRoleId() == roleId)
			{
				rank = index + 1;
				break;
			}
		}

		return rank;
	}

	public void initTurntableActivity(Map<String, String> config)
	{
		turntable.initConfig(config);
	}

	/**
	 * 处理查询转盘的请求
	 * 
	 * @param connection 客户端的连接
	 * @param message 请求消息
	 * @param callback 回调码
	 */
	public void onCGQueryTurntableInfoREQ(Connection connection, CGQueryTurntableInfoREQ message, int callback)
	{
		turntable.queryRewardInfo(connection, message, callback);
	}

	public void onCGShareTurntableRewardREQ(Connection connection, CGShareTurntableRewardREQ message, int callback)
	{
		turntable.shareTurntableReward(connection, message, callback);
	}

	public void onCGTurntableDrawREQ(Connection connection, CGTurntableDrawREQ message, int callback)
	{
		turntable.turntableDraw(connection, message, callback);
	}

	public void playerCostCard(int roleId, int cardCount)
	{
		if (!turntable.isActive())
		{
			return;
		}
		turntable.playerCostCard(roleId, cardCount);
	}

	public TurntableActivity getTurntableActivity()
	{
		return turntable;
	}

	public List<Map<String, Object>> turntableRewardDispatchQuery(int roleId)
	{
		return turntable.queryRewardDispatchQuery(roleId);
	}

	public LimitedCostlessActivityService getLca()
	{
		return this.lca;
	}

	public void onLimitedCostlessActivityREQ(Connection connection, CGLimitedCostlessActivityREQ message, int callback)
	{
		this.lca.onLimitedCostlessActivityREQ(connection, message, callback);
	}

	/**
	 * 根据传进来的活动事件list，设置所有的活动事件状态
	 * @param eventListList
	 */
	public void setAllActivityEventStatus(List<GameProtoBuf.ActivityEventPROTO> eventListList)
	{
		// TODO
	}

	/**
	 * 判断一个活动事件是否开启
	 * @param activityEventId
	 */
	public boolean isActivityEventActive(int activityEventId)
	{
		// TODO
		return false;
	}

	/**
	 * 向活动服务器发送事件
	 * @param activityEventId
	 * @param param
	 */
	public void triggerActivityEvent(int activityEventId, GameProtoBuf.ActivityEventParam param)
	{
		if (isActivityEventActive(activityEventId))
		{
			// 设置事件id
			GameProtoBuf.ActivityEventPROTO.Builder event = GameProtoBuf.ActivityEventPROTO.newBuilder();
			event.setEventId(activityEventId);

			// 设置事件消息
			GameProtoBuf.ActivityEventTriggerSYN.Builder builder = GameProtoBuf.ActivityEventTriggerSYN.newBuilder();
			builder.setEvent(event);
			builder.setParam(param);

			Connection activityServerConnection = ServiceContainer.getInstance().getPublicService(ServerService.class).getActivityServerConnection();
			activityServerConnection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
		}
	}
}
