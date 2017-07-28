package com.kodgames.activityserver.common.activity.rank;

import com.kodgames.activityserver.common.activity.condition.ICondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangzhen on 2017/7/27
 */
public class DefaultRank implements IRank
{
	private List<IRankData> rankDataList = new ArrayList<>();
	private int rankCount;

	@Override
	public void updateRank(IRankData data)
	{
		// 删除玩家的旧记录
		for (int deleteIndex = 0; deleteIndex < rankDataList.size(); ++deleteIndex)
		{
			IRankData deleteRank = rankDataList.get(deleteIndex);
			if (deleteRank.getRoleId() == data.getRoleId())
			{
				rankDataList.remove(deleteIndex);
				break;
			}
		}

		// 处理空排行
		if (rankDataList.isEmpty() == true)
		{
			// 加入玩家排行信息
			rankDataList.add(data);
			return;
		}

		// 将玩家信息插入到排行列表中
		int index = rankDataList.size() - 1;
		while (index >= 0 && rankDataList.get(index).compareTo(data) < 0)
		{
			--index;
		}
		rankDataList.add(index + 1, data);

		// 移除多余的排名记录
		while (rankDataList.size() > rankCount)
		{
			rankDataList.remove(rankDataList.size() - 1);
		}
	}

	@Override
	public List<IRankData> queryRank()
	{
		return rankDataList;
	}
}
