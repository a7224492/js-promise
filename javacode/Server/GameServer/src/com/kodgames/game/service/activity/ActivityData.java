package com.kodgames.game.service.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kodgames.corgi.core.constant.DateTimeConstants;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.common.Constant.ActivityId;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.message.proto.activity.ActivityProtoBuf.ActivityRankPROTO;
import com.kodgames.message.proto.activity.ActivityProtoBuf.PlayerRankPROTO;

import xbean.DateActivityRank;
import xbean.HistoryRank;
import xbean.RoleInfo;
import xbean.RoleRank;

public class ActivityData
{

	public static ActivityRankPROTO dateRankBeanToProto(int activityId, long date, DateActivityRank dateRank)
	{
		ActivityRankPROTO.Builder builder = ActivityRankPROTO.newBuilder();
		builder.setActivityId(activityId);

		if (dateRank == null)
		{
			return builder.build();
		}

		List<RoleRank> beanList = dateRank.getRoleRanks();
		for (int index = 0; index < beanList.size(); ++index)
		{
			RoleRank playerBean = beanList.get(index);
			PlayerRankPROTO playerProto = playerRankBeanToProto(playerBean, (index + 1), date);
			builder.addRecord(playerProto);
		}

		return builder.build();
	}

	private static PlayerRankPROTO playerRankBeanToProto(RoleRank bean, int rankOrder, long rankTime)
	{
		PlayerRankPROTO.Builder builder = PlayerRankPROTO.newBuilder();
		builder.setRoleId(bean.getRoleId());
		builder.setNickname(bean.getNickname());
		builder.setScore(bean.getScore());
		builder.setRankOrder(rankOrder);
		builder.setRankTime(rankTime);

		return builder.build();
	}

	public static ActivityRankPROTO historyRankBeanToProto(int roleId, int activityId, long date, Map<Long, HistoryRank> dateRankMap)
	{
		RoleInfo roleInfo = ServiceContainer.getInstance().getPublicService(RoleService.class).getRoleInfoByRoleId(roleId);
		String nickname = roleInfo == null ? "" + roleId : roleInfo.getNickname();

		ActivityRankPROTO.Builder builder = ActivityRankPROTO.newBuilder();
		builder.setActivityId(activityId);

		// 构造查询日期列表
		ActivityService activityService = ServiceContainer.getInstance().getPublicService(ActivityService.class);
		List<Long> dateList = getQueryDateList(date);

		for (Long queryDate : dateList)
		{
			PlayerRankPROTO.Builder recordBuilder = PlayerRankPROTO.newBuilder();
			recordBuilder.setRoleId(roleId);
			recordBuilder.setNickname(nickname);
			recordBuilder.setRankTime(queryDate);

			HistoryRank dateRank = dateRankMap.get(queryDate);
			if (dateRank == null)
			{
				dateRank = new HistoryRank();
			}
			recordBuilder.setScore(dateRank.getScore());
			int rank = activityService.getRank(roleId, activityId, queryDate, dateRank.getScore());
			recordBuilder.setRankOrder(rank);

			builder.addRecord(0, recordBuilder.build());
		}

		return builder.build();
	}

	private static List<Long> getQueryDateList(long date)
	{
		List<Long> dateList = new ArrayList<>();

		ActivityService activityService = ServiceContainer.getInstance().getPublicService(ActivityService.class);
		ActivityConfig config = activityService.getConfigMap().get(ActivityId.SCORE_RANK.getId());
		if (config == null)
		{
			return dateList;
		}
		
		long beginDate = DateTimeConstants.getDate(config.getActivityStartTime());
		for (long protoDate = beginDate; protoDate <= date; protoDate += DateTimeConstants.DAY)
		{
			dateList.add(protoDate);
		}

		return dateList;
	}

}
