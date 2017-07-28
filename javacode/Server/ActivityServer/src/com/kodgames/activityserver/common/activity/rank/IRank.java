package com.kodgames.activityserver.common.activity.rank;

import java.util.List;

/**
 * Created by jiangzhen on 2017/7/27.
 * 排行榜结构
 */
public interface IRank
{
	/**
	 * 更新排行榜
	 * @param data
	 */
	public void updateRank(IRankData data);

	/**
	 * 查询排行榜的数据
	 * @return
	 */
	public List<IRankData> queryRank();
}
