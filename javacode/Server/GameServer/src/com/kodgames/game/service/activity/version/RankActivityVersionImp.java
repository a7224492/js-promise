package com.kodgames.game.service.activity.version;

import xbean.RankActivityVersion;

public class RankActivityVersionImp extends ActivityVersion
{
	private static final int VERSION_KEY = 123;
	@Override
	protected void clearData()
	{
		table.Activity_history_rank.get().walk((k, v)->{
			table.Activity_history_rank.delete(k);
			return true;
		});
		table.Activity_rank.get().walk((k, v)->{
			table.Activity_rank.delete(k);
			return true;
		});		
	}

	@Override
	protected int oldVersion()
	{
		RankActivityVersion version = table.Rank_activity_version_table.update(VERSION_KEY);
		if (version == null)
		{
			version = table.Rank_activity_version_table.insert(VERSION_KEY);
			version.setVersion(0);
		}
		int oldVersion = version.getVersion();
		return oldVersion;
	}

	@Override
	protected void insertData()
	{
		// 排行榜不用插入数据
	}

	@Override
	protected void updateVersion(int newVersion)
	{
		RankActivityVersion version = table.Rank_activity_version_table.update(VERSION_KEY);
		if (version == null)
		{
			version = table.Rank_activity_version_table.insert(VERSION_KEY);
		}
		version.setVersion(newVersion);
	}

}
