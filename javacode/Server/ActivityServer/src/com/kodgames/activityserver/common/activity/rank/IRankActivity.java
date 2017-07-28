package com.kodgames.activityserver.common.activity.rank;

import com.kodgames.activityserver.common.activity.IActivity;
import com.kodgames.message.proto.activity.ActivityProtoBuf;

import java.util.List;

/**
 * Created by jiangzhen on 2017/7/27.
 */
public interface IRankActivity extends IActivity
{
	/**
	 * 更新排行榜
	 * @param rankDateType
	 * @param data
	 */
	public void updateRank(int rankDateType, long date, IRankData data);

	/**
	 * 查询排行榜的数据
	 * @param rankdDateType
	 * @return
	 */
	public IRank queryRank(int rankdDateType, long date);

	/**
	 * 获取一个玩家 从活动开始到指定日期的 所有历史排行记录
	 *
	 * 最多只能查到活动最后一天
	 */
	public <T extends Object> T getProto(IRankData data);
}