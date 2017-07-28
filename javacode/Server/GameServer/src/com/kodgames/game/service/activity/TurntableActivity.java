package com.kodgames.game.service.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.activity.version.IVersion;
import com.kodgames.game.service.activity.version.TurntableActivityVersionImp;
import com.kodgames.game.util.KodBiLogHelper;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGQueryTurntableInfoREQ;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGShareTurntableRewardREQ;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGShareTurntableRewardRES;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGTurntableDrawREQ;
import com.kodgames.message.proto.activity.ActivityProtoBuf.GCPlayerHasItemCountSYN;
import com.kodgames.message.proto.activity.ActivityProtoBuf.GCQueryTurntableInfoRES;
import com.kodgames.message.proto.activity.ActivityProtoBuf.GCTurntableDrawRES;
import com.kodgames.message.proto.activity.ActivityProtoBuf.TurntableRewardPROTO;
import com.kodgames.message.proto.game.GameProtoBuf.GCRoomCardModifySYNC;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import cbean.ActivityRewardKey;
import cbean.LastRewardKey;
import io.netty.util.internal.ThreadLocalRandom;
import limax.zdb.Procedure;
import xbean.GameActivityReward;
import xbean.LastRewardInfo;
import xbean.RoleInfo;
import xbean.TurntableActivityReward;
import xbean.TurntableActivityRewardRecord;
import xbean.TurntableRewardDispatch;
import xbean.TurntableRewardDispatchBean;

/**
 * 转盘活动
 * 
 * @author jiangzhen
 *
 */
public class TurntableActivity
{
	private static final Logger logger = LoggerFactory.getLogger(TurntableActivity.class);

	/**
	 * 房卡消耗奖励类型
	 */
	private static final int REWARD_TYPE_CARD_COST = 1;

	/**
	 * 转盘活动的id
	 */
	private int activityId = 0;

	/**
	 * key 奖励类型 value 奖励条件和数量
	 */
	private Map<Integer, List<RewardThresholdNum>> rewardNumMap =
		new ConcurrentHashMap<Integer, List<RewardThresholdNum>>();

	/**
	 * key 奖品id value 概率范围
	 */
	private Map<Long, Map<Integer, RewardRatioRange>> rewardRatioMap =
		new ConcurrentHashMap<Long, Map<Integer, RewardRatioRange>>();

	/**
	 * 最近5个需要发跑马灯的获奖信息
	 */
	private LinkedList<LastRewardInfoBean> lastRewardInfos = new LinkedList<LastRewardInfoBean>();

	/**
	 * 活动开始时间
	 */
	private long startTime = 0;
	
	/**
	 * 活动结束时间
	 */
	private long endTime = 0;

	/**
	 * 活动开始显示时间
	 */
	private long showTime = 0;
	
	/**
	 * 活动结束显示时间
	 */
	private long blankTime = 0;

	/**
	 * 读写锁
	 */
	ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	WriteLock wLock = rwLock.writeLock();
	ReadLock rLock = rwLock.readLock();

	/**
	 * 存储奖品概率的map
	 */
	private Map<Long, Integer> ratioMap = new ConcurrentHashMap<Long, Integer>();

	/**
	 * 没有抽到奖品时，发给客户端的奖品描述
	 */
	private static String noRewardDesc = "";
	
	/**
	 * 没有抽到奖品时，发给客户端的奖品id
	 */
	private static int noRewardId = -1;

	/**
	 * 跑马灯字符串格式
	 */
	private String nickFormatStr = "";
	
	/**
	 * 默认日期
	 */
	private long defaultTime = 0L;

	public TurntableActivity()
	{
		defaultTime = defaultTime();
		table.Game_activity_reward_table.get().walk((k, v)->{
			Procedure.call(()->{
				table.Game_activity_reward_table.select(k);
				return true;
			});
			return true;
		});
	}

	/**
	 * 判断现在是否处于活动期间
	 * @return true 处于活动期间
	 */
	public boolean isActive()
	{
		long now = System.currentTimeMillis();
		if (now > this.startTime && now < this.endTime)
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断现在是否处于显示时间
	 * @return true 处于显示时间
	 */
	public boolean isShow()
	{
		long now = System.currentTimeMillis();
		if (now > this.showTime && now < this.blankTime)
		{
			return true;
		}
		return false;
	}

	/**
	 * 初始化转盘配置
	 * @param config 配置
	 */
	public void initConfig(Map<String, String> config)
	{
		String showTimeStr = config.get("turntable_activity_show_time");
		String startTimeStr = config.get("turntable_activity_start_time");
		String endTimeStr = config.get("turntable_activity_end_time");
		String blankTimeStr = config.get("turntable_activity_blank_time");
		String activityIdStr = config.get("turntable_activity_id");

		this.activityId = Integer.valueOf(activityIdStr);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
		try
		{
			this.showTime = dateFormat.parse(showTimeStr).getTime();
			this.startTime = dateFormat.parse(startTimeStr).getTime();
			this.endTime = dateFormat.parse(endTimeStr).getTime();
			this.blankTime = dateFormat.parse(blankTimeStr).getTime();
		}
		catch (ParseException e)
		{
			logger.warn("activity time format error, exception={}", e);
			return;
		}
		this.nickFormatStr = config.get("nameFormatStr");

		this.initRewardThreshold(config);
		IVersion iVersion = new TurntableActivityVersionImp(config, activityId);
		iVersion.init(config.get("turntable_activity_version"));
		this.initConfig();
	}

	/**
	 * 初始化奖励阈值
	 * 
	 * @param config 配置
	 */
	private void initRewardThreshold(Map<String, String> config)
	{
		String rewardThresholdConfigStr = config.get("turntable_activity_reward_threshold");
		String[] thresholdStr = rewardThresholdConfigStr.split(";");

		List<RewardThresholdNum> list1 = new ArrayList<RewardThresholdNum>();
		for (int i = 0; i < thresholdStr.length; ++i)
		{
			String e = thresholdStr[i];
			String[] es = e.split(":");
			int threshold = Integer.valueOf(es[0]);
			int thresholdNum = Integer.valueOf(es[1]);
			RewardThresholdNum rewardThresholdNum = new RewardThresholdNum(threshold, thresholdNum);
			list1.add(rewardThresholdNum);
		}
		RewardThresholdNum thresholdNum3 = new RewardThresholdNum(0, 1);
		List<RewardThresholdNum> list2 = new ArrayList<RewardThresholdNum>();
		list2.add(thresholdNum3);
		this.rewardNumMap.put(1, list1);
		this.rewardNumMap.put(2, list2);
	}

	/**
	 * 初始化转盘的配置： 玩家获得抽奖机会的消耗房卡数
	 */
	private void initConfig()
	{
		// 读取奖品概率配置
		table.Game_activity_reward_table.get().getCache().walk((key, value) -> {
			if (key.getActivityId() != activityId)
			{
				// 不是转盘活动
				return ;
			}
			Integer ratio = this.ratioMap.get(key.getRewardDate());
			if (ratio == null)
			{
				ratio = 0;
			}
			int lowerBound = ratio;
			int upperBound = ratio + value.getRewardRatio();
			ratio += value.getRewardRatio();
			this.ratioMap.put(key.getRewardDate(), ratio);
			RewardRatioRange range = new RewardRatioRange(lowerBound, upperBound);
			Map<Integer, RewardRatioRange> map = this.rewardRatioMap.get(key.getRewardDate());
			if (map == null)
			{
				map = new ConcurrentHashMap<Integer, RewardRatioRange>();
			}
			map.put(key.getRewardId(), range);
			this.rewardRatioMap.put(key.getRewardDate(), map);
			if (!value.getIsReward())
			{
				noRewardDesc = value.getRewardDesc();
				noRewardId = key.getRewardId();
			}
			return ;
		});

		// 读取活动配置表
		try
		{
			rLock.lock();
			table.Last_reward_info_table.get().walk((key, value) -> {
				if (lastRewardInfos.size() >= 5)
				{
					return true;
				}
				lastRewardInfos.add(new LastRewardInfoBean(key, value));
				return true;
			});
			Collections.sort(lastRewardInfos);
		}
		finally
		{
			rLock.unlock();
		}
	}

	/**
	 * 玩家消耗房卡的时候调用此方法
	 * 
	 * @param roleId 玩家id
	 * @param cardNum 消耗的房卡
	 */
	public void playerCostCard(int roleId, int cardCount)
	{
		Procedure.call(() -> {
			int beforeCardCount = 0;
			int currCardCount = 0;

			long now = System.currentTimeMillis();

			TurntableActivityReward reward = table.Games_activity_turntable_reward.select(roleId);
			if (reward == null)
			{
				logger.debug("roleId={} has no turntable reward record", roleId);
				reward = table.Games_activity_turntable_reward.insert(roleId);
				this.setDefaultActivityReward(reward, roleId);
			}

			List<RewardThresholdNum> list = this.rewardNumMap.get(REWARD_TYPE_CARD_COST);
			if (list == null)
			{
				logger.info("turntable has no card cost type!!!!!");
				return false;
			}
			boolean isOneDay = this.isOneDayToZeroTime(now, reward.getConsumeAddNumTime());
			// 如果当前时间和奖励时间是同一天
			if (isOneDay)
			{
				beforeCardCount = reward.getConsumeNum();
				currCardCount = beforeCardCount + cardCount;
			}
			else
			{
				beforeCardCount = 0;
				currCardCount = cardCount;
			}

			// 玩家当天的奖励次数已用完
			if (beforeCardCount >= list.get(list.size() - 1).getThreshold())
			{
				logger.debug("roleId={} has no more reward today");
				reward.setConsumeNum(currCardCount);
				return true;
			}

			// 奖励的次数
			int addItemCount = 0;

			for (RewardThresholdNum thresholdNum : list)
			{
				// 达到奖励标准
				if (thresholdNum.getThreshold() > beforeCardCount && thresholdNum.getThreshold() <= currCardCount)
				{
					++addItemCount;
					this.synHasItemCount(roleId, GlobalConstants.DEFAULT_CALLBACK);
				}
				else if (currCardCount < thresholdNum.getThreshold())
				{
					// 没必要循环了
					break;
				}
			}

			if (!isOneDay)
			{
				// 当天的零点入库
				reward.setConsumeAddNumTime(zeroTime(now));
			}
			// 入库抽奖次数
			reward.setItemCount(reward.getItemCount() + addItemCount);
			reward.setConsumeNum(currCardCount);

			return true;
		});
	}

	/**
	 * 客户端请求转盘信息
	 * 
	 * @param connection 客户端连接
	 * @param message 请求消息
	 * @param callback 回调码
	 */
	public void queryRewardInfo(Connection connection, CGQueryTurntableInfoREQ message, int callback)
	{
		int roleId = connection.getRemotePeerID();
		Procedure.call(() -> {
			TurntableActivityReward reward = table.Games_activity_turntable_reward.select(roleId);
			if (reward == null)
			{
				logger.debug("roleId={} has no turntable reward record", roleId);
				reward = table.Games_activity_turntable_reward.insert(roleId);
				setDefaultActivityReward(reward, roleId);
			}
			GCQueryTurntableInfoRES.Builder builder = GCQueryTurntableInfoRES.newBuilder();

			// 设置错误码
			builder.setResult(PlatformProtocolsConfig.GC_QUERY_TURNTABLE_INFO_SUCCESS);

			// 设置抽奖次数
			builder.setItemCount(reward.getItemCount());

			// 设置获奖信息
			List<TurntableActivityRewardRecord> list = reward.getRewards();
			for (TurntableActivityRewardRecord record : list)
			{
				builder.addRewards(recordToRewardPROTO(record));
			}

			// 设置最近5个需要发跑马灯的获奖信息
			List<String> strList = lastRewardInfoString();
			if (strList == null)
			{
				connection.write(callback, builder.build());
				return true;
			}
			builder.addAllLastRewardInfos(strList);

			connection.write(callback, builder.build());
			return true;
		});
	}

	private List<String> lastRewardInfoString()
	{
		List<String> lastRewardString = new ArrayList<String>();
		try
		{
			rLock.lock();
			for (LastRewardInfoBean rewardInfoBean : this.lastRewardInfos)
			{
				LastRewardKey rewardInfo = rewardInfoBean.getLastRewardKey();
				long time = rewardInfo.getRewardTime();
				SimpleDateFormat format = new SimpleDateFormat("HH:mm");
				String timeStr = format.format(new Date(time));
				int roleId = rewardInfo.getRoleId();

				RoleInfo roleInfo = table.Role_info.select(roleId);
				if (roleInfo == null)
				{
					logger.error("roleId={} has no info !!!!!!!!");
					return null;
				}
				String nickName = roleInfo.getNickname();
				String desc = rewardInfoBean.getLastRewardInfo().getRewardDesc();
				lastRewardString.add(String.format(this.nickFormatStr, timeStr, nickName, desc));
			}
		}
		finally
		{
			rLock.unlock();
		}

		return lastRewardString;
	}

	private TurntableRewardPROTO recordToRewardPROTO(TurntableActivityRewardRecord record)
	{
		TurntableRewardPROTO.Builder builder = TurntableRewardPROTO.newBuilder();
		builder.setActivityId(activityId);
		builder.setGainTime(record.getRewardTime());
		builder.setRewardDesc(record.getRewardDesc());
		builder.setRewardId(record.getRewardId());

		return builder.build();
	}

	/**
	 * 表示奖励阈值和奖励数量的类
	 * 
	 * @author jiangzhen
	 *
	 */
	private static class RewardThresholdNum implements Comparable<RewardThresholdNum>
	{
		/**
		 * 奖励阈值 初始化活动配置的时候会添加元素，初始化完成之后，需要排序，让元素保持递增
		 */
		private int threshold = -1;

		/**
		 * 每个阈值对应的奖励数量
		 */
		private int num = -1;

		private int hashCode = 1;

		public RewardThresholdNum(int threshold, int num)
		{
			this.threshold = threshold;
			this.num = num;
			hashCode = (17 * 31 + this.threshold) * 31 + num;
		}

		public int getThreshold()
		{
			return this.threshold;
		}

		@SuppressWarnings("unused")
		public int getNum()
		{
			return this.num;
		}

		@Override
		public int compareTo(RewardThresholdNum o)
		{
			return (this.threshold - o.threshold);
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o)
			{
				return true;
			}
			if (o == null)
			{
				return false;
			}
			if (!(o instanceof RewardThresholdNum))
			{
				return false;
			}
			RewardThresholdNum rtn = (RewardThresholdNum)o;
			if (this.threshold != rtn.threshold || this.num != rtn.num)
			{
				return false;
			}
			return true;
		}

		@Override
		public int hashCode()
		{
			return this.hashCode;
		}

		@Override
		public String toString()
		{
			return new StringBuilder().append(" threshold=")
				.append(this.threshold)
				.append(" num=")
				.append(this.num)
				.toString();
		}
	}

	/**
	 * 用来表示奖品获取概率的类
	 * 
	 * @author jiangzhen
	 *
	 */
	private static class RewardRatioRange
	{
		/**
		 * 获取值的下界
		 */
		private int lowerBound = -1;

		/**
		 * 获取值的上界
		 */
		private int upperBound = -1;

		public RewardRatioRange(int lowerBound, int upperBound)
		{
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
		}

		public int getLowerBound()
		{
			return this.lowerBound;
		}

		public int getUpperBound()
		{
			return this.upperBound;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("[lowerBound=");
			sb.append(this.lowerBound);
			sb.append(", upperBound=");
			sb.append(this.upperBound);
			sb.append("]");
			return sb.toString();
		}
	}

	private static class LastRewardInfoBean implements Comparable<LastRewardInfoBean>
	{
		private LastRewardKey key = null;
		private LastRewardInfo bean = null;

		public LastRewardInfoBean(LastRewardKey key, LastRewardInfo bean)
		{
			this.key = key;
			this.bean = bean;
		}

		public LastRewardInfo getLastRewardInfo()
		{
			return this.bean;
		}

		public LastRewardKey getLastRewardKey()
		{
			return this.key;
		}

		@Override
		public String toString()
		{
			return key.toString();
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o)
			{
				return true;
			}
			if (o == null || !(o instanceof LastRewardInfoBean))
			{
				return false;
			}
			LastRewardInfoBean instance = (LastRewardInfoBean)o;
			return this.key.equals(instance.key);
		}

		@Override
		public int compareTo(LastRewardInfoBean instance)
		{
			long thisTime = this.key.getRewardTime();
			long instanceTime = instance.getLastRewardKey().getRewardTime();
			if (thisTime > instanceTime)
			{
				return -1;
			}
			else if (thisTime == instanceTime)
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}

		@Override
		public int hashCode()
		{
			return this.key.hashCode();
		}
	}

	/**
	 * 玩家向服务器发送分享奖励的请求
	 * 
	 * @param connection 客户端的连接
	 * @param message 客户端发送的请求
	 * @param callback 回调码
	 */
	public void shareTurntableReward(Connection connection, CGShareTurntableRewardREQ message, int callback)
	{
		int roleId = connection.getRemotePeerID();
		Procedure.call(() -> {
			TurntableActivityReward reward = table.Games_activity_turntable_reward.update(roleId);
			if (reward == null)
			{
				logger.debug("roleId={} has no turntable reward record", roleId);
				reward = table.Games_activity_turntable_reward.insert(roleId);
				this.setDefaultActivityReward(reward, roleId);
			}
			long now = System.currentTimeMillis();
			if (isOneDayToZeroTime(now, reward.getShareTime()))
			{
				logger.debug("roleId={} has no more share reward today");
				connection.write(callback,
					CGShareTurntableRewardRES.newBuilder()
						.setResult(PlatformProtocolsConfig.GC_SHARE_TURNTABLE_REWARD_SUCCESS)
						.setItemCount(reward.getItemCount())
						.build());
				return true;
			}
			// 是第一次分享奖励，给一次抽奖机会
			reward.setItemCount(reward.getItemCount() + 1);
			reward.setShareTime(zeroTime(now));

			this.synHasItemCount(connection, GlobalConstants.DEFAULT_CALLBACK);

			connection.write(callback,
				CGShareTurntableRewardRES.newBuilder()
					.setResult(PlatformProtocolsConfig.GC_SHARE_TURNTABLE_REWARD_SUCCESS)
					.setItemCount(reward.getItemCount())
					.build());
			return true;
		});
	}

	private void setDefaultActivityReward(TurntableActivityReward reward, int roleId)
	{
		reward.setConsumeAddNumTime(defaultTime);
		reward.setConsumeNum(0);
		reward.setItemCount(0);
		reward.setRoleId(roleId);
		reward.setShareTime(defaultTime);
	}

	/**
	 * 把now时间和某一零点比较，判断now和零点是否是同一天
	 * 
	 * @param now 当前时间
	 * @param zeroTime 零点
	 * @return true 是同一天 false 不是同一天
	 */
	private boolean isOneDayToZeroTime(long now, long zeroTime)
	{
		return zeroTime(now) == zeroTime;
	}

	/**
	 * 得到当天的零点
	 * 
	 * @param time 需要计算的时间
	 * @return 当天的零点
	 */
	private long zeroTime(long time)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(time));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	/**
	 * 默认日期 20160101
	 * 
	 * @return
	 */
	private long defaultTime()
	{
		return time(2016, 1, 1);
	}

	/**
	 * 获取指定时间的时间戳
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	private long time(int year, int month, int date)
	{
		return time(year, month, date, 0, 0, 0);
	}

	private long time(int year, int month, int date, int hour, int minute, int second)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, date, hour, minute, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	@SuppressWarnings("unused")
	private void initActiveTimer()
	{

	}

	@SuppressWarnings("unused")
	private void initInactiveTimer()
	{

	}

	/**
	 * 处理玩家抽奖请求
	 * 
	 * @param connection 和客户端的连接
	 * @param message 客户端发来的消息
	 * @param callback 回调码
	 */
	public void turntableDraw(Connection connection, CGTurntableDrawREQ message, int callback)
	{
		if (!isActive())
		{
			connection.write(callback,
				GCTurntableDrawRES.newBuilder()
					.setItemCount(0)
					.setResult(PlatformProtocolsConfig.GC_TURNTABLE_DRAW_INACTIVE)
					.build());
			return;
		}
		Procedure.call(() -> {
			int roleId = connection.getRemotePeerID();
			GCTurntableDrawRES.Builder builder = GCTurntableDrawRES.newBuilder();
			long now = System.currentTimeMillis();

			TurntableActivityReward playerReward = table.Games_activity_turntable_reward.update(roleId);
			if (playerReward == null)
			{
				logger.info("roleId={} has no reward record", roleId);
				playerReward = table.Games_activity_turntable_reward.insert(roleId);
				this.setDefaultActivityReward(playerReward, roleId);
			}

			// 得到玩家当前的抽奖次数
			int itemCount = playerReward.getItemCount();
			if (itemCount <= 0)
			{
				logger.info("roleId={} has no more itemCount", roleId);
				builder.setResult(PlatformProtocolsConfig.GC_TURNTABLE_DRAW_NO_MORE_ITEM).setItemCount(itemCount);
				connection.write(callback, builder.build());
				return true;
			}

			// 玩家抽奖次数减1
			playerReward.setItemCount(itemCount - 1);

			int randomInt = ThreadLocalRandom.current().nextInt(1, 10000);
			logger.debug("randomInt={}", randomInt);
			int rewardId = -1;
			// 判断玩家抽到了什么奖品
			Map<Integer, RewardRatioRange> map = this.rewardRatioMap.get(zeroTime(now));
			for (Integer id : map.keySet())
			{
				RewardRatioRange range = map.get(id);
				if (range == null)
				{
					logger.info("range is null");
				}
				if (randomInt > range.getLowerBound() && randomInt <= range.getUpperBound())
				{
					rewardId = id;
					break;
				}
			}

			// 产生的随机数没有在奖品区间（应该是不会发生的）
			if (rewardId == -1)
			{
				logger.warn("rewardId == -1");
				KodBiLogHelper.turntableDraw(roleId, noRewardId, noRewardDesc, 0, new Date(now));
				this.noReward(builder, playerReward.getItemCount(), now);
				connection.write(callback, builder.build());
				return true;
			}

			// 当天的零点
			long zeroTime = zeroTime(now);

			ActivityRewardKey key = new ActivityRewardKey(activityId, rewardId, zeroTime);
			// 得到奖品的库存等等的信息
			GameActivityReward reward = table.Game_activity_reward_table.update(key);

			// 奖品没有库存了，
			if (reward.getRewardLeftCount() <= 0)
			{
				logger.debug("rewardId={} leftCount is zero", rewardId);
				KodBiLogHelper.turntableDraw(roleId, noRewardId, noRewardDesc, 0, new Date(now));
				this.noReward(builder, playerReward.getItemCount(), now);
				connection.write(callback, builder.build());
				return true;
			}

			KodBiLogHelper.turntableDraw(roleId,
				rewardId,
				reward.getRewardName(),
				reward.getRewardCount(),
				new Date(now));
			// 转盘抽奖回复
			if (reward.getIsReward())
			{
				// 记录玩家抽到的奖品
				List<TurntableActivityRewardRecord> recordList = playerReward.getRewards();
				TurntableActivityRewardRecord record = new TurntableActivityRewardRecord();
				record.setRewardCount(reward.getRewardCount());
				record.setRewardName(reward.getRewardName());
				record.setRewardId(rewardId);
				record.setRewardTime(now);
				record.setRewardDesc(reward.getRewardDesc());
				recordList.add(record);

				if (reward.getIsCardReward())
				{
					// 给玩家加房卡
					this.addPlayerCard(connection, roleId, reward.getRewardCount());
					this.recordRewardDispatch(roleId,
						rewardId,
						reward.getRewardName(),
						reward.getRewardDesc(),
						reward.getRewardCount(),
						now,
						true,
						true);
				}
				else
				{
					this.recordRewardDispatch(roleId,
						rewardId,
						reward.getRewardName(),
						reward.getRewardDesc(),
						reward.getRewardCount(),
						now,
						false,
						true);
				}

				LastRewardKey lastRewardKey = new LastRewardKey(roleId, now);
				LastRewardInfo rewardInfo = new LastRewardInfo();
				rewardInfo.setRewardDesc(reward.getRewardDesc());
				rewardInfo.setRewardId(rewardId);
				LastRewardInfoBean bean = new LastRewardInfoBean(lastRewardKey, rewardInfo);

				LastRewardInfoBean deleteBean = null;
				// 抽斗奖品后是否需要广播
				if (reward.getIsBroadcard())
				{
					try
					{
						wLock.lock();
						if (this.lastRewardInfos.size() >= 5)
						{
							deleteBean = this.lastRewardInfos.pollLast();
						}
						this.lastRewardInfos.offerFirst(bean);
					}
					finally
					{
						wLock.unlock();
					}

					// 更新表信息
					if (deleteBean != null)
					{
						table.Last_reward_info_table.delete(deleteBean.getLastRewardKey());
					}
					LastRewardInfo info = table.Last_reward_info_table.insert(bean.getLastRewardKey());
					info.copyFrom(bean.getLastRewardInfo());
				}
			}

			builder.setItemCount(playerReward.getItemCount());

			// 奖品库存
			reward.setRewardLeftCount(reward.getRewardLeftCount() - 1);

			// 玩家抽到的奖品
			TurntableRewardPROTO.Builder protoBuilder = TurntableRewardPROTO.newBuilder();
			protoBuilder.setActivityId(activityId);
			protoBuilder.setGainTime(now);
			protoBuilder.setRewardDesc(reward.getRewardDesc());
			protoBuilder.setRewardId(rewardId);
			builder.setReward(protoBuilder.build());

			builder.addAllLastRewardInfos(this.lastRewardInfoString());
			builder.setResult(PlatformProtocolsConfig.GC_TURNTABLE_DRAW_SUCCESS);

			connection.write(callback, builder.build());
			return true;
		});
	}

	/**
	 * 给玩家加房卡
	 * 
	 * @param roleId 玩家id
	 * @param cardCount 加卡的数量
	 */
	private void addPlayerCard(Connection connection, int roleId, int cardCount)
	{
		RoleInfo roleInfo = table.Role_info.update(roleId);
		if (roleInfo == null)
		{
			logger.warn("roleId={} has no roleInfo");
			roleInfo = table.Role_info.insert(roleId);
		}
		roleInfo.setCardCount(roleInfo.getCardCount() + cardCount);
		GCRoomCardModifySYNC.Builder syn = GCRoomCardModifySYNC.newBuilder();
		syn.setRoomCardCount(roleInfo.getCardCount());
		if (connection != null)
		{
			connection.write(GlobalConstants.DEFAULT_CALLBACK, syn.build());
		}
		else
		{
			logger.error("roleId={} has no connection", roleId);
		}

	}

	public int getActivityId()
	{
		return activityId;
	}

	public long getStartTime()
	{
		return this.startTime;
	}

	public long getEndTime()
	{
		return this.endTime;
	}

	private void noReward(GCTurntableDrawRES.Builder builder, int itemCount, long now)
	{
		builder.setItemCount(itemCount);
		builder.addAllLastRewardInfos(this.lastRewardInfoString());
		builder.setResult(PlatformProtocolsConfig.GC_TURNTABLE_DRAW_SUCCESS);
		TurntableRewardPROTO.Builder proto = TurntableRewardPROTO.newBuilder();
		proto.setActivityId(activityId);
		proto.setGainTime(now);
		proto.setRewardDesc(noRewardDesc);
		proto.setRewardId(noRewardId);
		builder.setReward(proto);
	}

	public long getShowTime()
	{
		return this.showTime;
	}

	public long getBlankTime()
	{
		return this.blankTime;
	}

	/**
	 * 推送玩家有抽奖机会的消息
	 * 
	 * @param connection
	 * @param callback
	 */
	private void synHasItemCount(int roleId, int callback)
	{
		Connection clientConnection = ConnectionManager.getInstance().getClientVirtualConnection(roleId);
		clientConnection.write(callback, GCPlayerHasItemCountSYN.newBuilder().build());
	}

	private void synHasItemCount(Connection connection, int callback)
	{
		connection.write(callback, GCPlayerHasItemCountSYN.newBuilder().build());
	}

	public void synIfPlayerHasItemCount(Connection connection, int callback)
	{
		if (!this.isActive())
		{
			return;
		}
		int roleId = connection.getRemotePeerID();
		TurntableActivityReward reward = table.Games_activity_turntable_reward.select(roleId);
		if (reward == null)
		{
			logger.debug("roleId={} has no turntable reward record", roleId);
			reward = table.Games_activity_turntable_reward.insert(roleId);
			setDefaultActivityReward(reward, roleId);
		}
		if (reward.getItemCount() <= 0)
		{
			return;
		}
		connection.write(callback, GCPlayerHasItemCountSYN.newBuilder().build());
	}

	public List<Map<String, Object>> queryRewardDispatchQuery(int roleId)
	{
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		TurntableRewardDispatch reward = table.Turntable_reward_dispatch_table.select(roleId);
		if (reward == null)
		{
			reward = table.Turntable_reward_dispatch_table.insert(roleId);
			return retList;
		}

		reward.getBean().forEach(bean -> {
			if (bean.getIsCard())
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("playerId", roleId);
				map.put("rewardId", bean.getRewardId());
				map.put("rewardName", bean.getRewardName());
				map.put("rewardCount", bean.getRewardCount());
				map.put("rewardDatetime", format.format(new Date(bean.getRewardTime())));
				map.put("isDispatch", bean.getIsDispatch());
				retList.add(map);
			}
		});

		return retList;
	}

	private void recordRewardDispatch(int roleId, int rewardId, String rewardName, String rewardDesc, int rewardCount,
		long rewardDatetime, boolean isCard, boolean isDispatch)
	{
		TurntableRewardDispatch reward = table.Turntable_reward_dispatch_table.update(roleId);
		if (reward == null)
		{
			reward = table.Turntable_reward_dispatch_table.insert(roleId);
		}
		TurntableRewardDispatchBean bean = new TurntableRewardDispatchBean();
		bean.setIsCard(isCard);
		bean.setIsDispatch(isDispatch);
		bean.setRewardCount(rewardCount);
		bean.setRewardDesc(rewardDesc);
		bean.setRewardName(rewardName);
		bean.setRewardId(rewardId);
		bean.setRewardTime(rewardDatetime);
		reward.getBean().add(bean);
	}

	public void test()
	{
		Procedure.call(() -> {
			final int roleId = 123456;
			for (int i = 0; i < 20; ++i)
			{
				this.playerCostCard(roleId, 3);
				TurntableActivityReward reward = table.Games_activity_turntable_reward.select(roleId);
				System.out.println(reward.getItemCount());
			}
			return true;
		});

	}
}
