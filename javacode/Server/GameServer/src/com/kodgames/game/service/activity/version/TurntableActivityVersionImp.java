package com.kodgames.game.service.activity.version;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cbean.ActivityRewardKey;
import xbean.GameActivityReward;
import xbean.TurntableActivityVersion;

public class TurntableActivityVersionImp extends ActivityVersion
{
	private static final Logger logger = LoggerFactory.getLogger(TurntableActivityVersionImp.class);
	/**
	 * 版本号表的key
	 */
	private static final int VERSION_KEY = 123456;
	
	private Map<String, String> config = null;
	private int activityId = 0;
	
	public TurntableActivityVersionImp(Map<String, String> config, int activityId)
	{
		this.config = config;
		this.activityId = activityId;
	}

	@Override
	protected void clearData()
	{
		table.Game_activity_reward_table.get().walk((k, v)->{
			table.Game_activity_reward_table.delete(k);
			return true;
		});
		table.Last_reward_info_table.get().walk((k, v)->{
			table.Last_reward_info_table.delete(k);
			return true;
		});
		table.Games_activity_turntable_reward.get().walk((k, v)->{
			table.Games_activity_turntable_reward.delete(k);
			return true;
		});
		table.Turntable_reward_dispatch_table.get().walk((k, v)->{
			table.Turntable_reward_dispatch_table.delete(k);
			return true;
		});
	}

	@Override
	protected int oldVersion()
	{
		TurntableActivityVersion version = table.Turntable_activity_version_table.update(VERSION_KEY);
		if (version == null)
		{
			version = table.Turntable_activity_version_table.insert(VERSION_KEY);
			version.setVersion(0);
		}
		int oldVersion = version.getVersion();
		return oldVersion;		
	}

	@Override
	protected void insertData()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String rewardConfig = config.get("turntable_activity_reward_config");
		if (rewardConfig != null && !rewardConfig.equals(""))
		{
			try
			{
				String[] lines = rewardConfig.split(";");
				for (String line : lines)
				{
					String[] param = line.split(",");
					long date = dateFormat.parse(param[0]).getTime();
					int rewardId = Integer.valueOf(param[1]);
					String rewardName = param[2];
					int leftCountTmp = Integer.valueOf(param[3]);
					if (leftCountTmp == -1)
					{
						leftCountTmp = Integer.MAX_VALUE;
					}
					int leftCount = leftCountTmp;
					int ratio = (int)(100 * Float.valueOf(param[4]));
					int rewardCount = Integer.valueOf(param[5]);
					int isBroadcard = Integer.valueOf(param[6]);
					int isReward = Integer.valueOf(param[7]);
					int isCardReward = Integer.valueOf(param[8]);
					ActivityRewardKey key = new ActivityRewardKey(activityId, rewardId, date);
					GameActivityReward reward = table.Game_activity_reward_table.insert(key);
					if (reward == null)
					{
						reward = table.Game_activity_reward_table.update(key);
					}
					reward.setCondition("none");
					reward.setIsBroadcard(isBroadcard == 1);
					reward.setIsCardReward(isCardReward == 1);
					reward.setIsReward(isReward == 1);
					reward.setRewardCount(rewardCount);
					reward.setRewardDesc(rewardName);
					reward.setRewardLeftCount(leftCount);
					reward.setRewardName(rewardName);
					reward.setRewardRatio(ratio);
					logger.debug("rewardId={}, value={}", key, reward);
				}
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}	
		}
	}

	@Override
	protected void updateVersion(int newVersion)
	{
		TurntableActivityVersion version = table.Turntable_activity_version_table.update(VERSION_KEY);
		if (version == null)
		{
			version = table.Turntable_activity_version_table.insert(VERSION_KEY);
		}
		version.setVersion(newVersion);
	}
	
}
