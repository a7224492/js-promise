package com.kodgames.game.service.gmtools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import xbean.TurntableActivityReward;
import xbean.TurntableActivityRewardRecord;

@GmtHandlerAnnotation(handler = "TurntableQueryHandler")
public class TurntableQueryHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(TurntableQueryHandler.class);
	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		
		int roleId = (Integer)json.get("playerId");
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		TurntableActivityReward reward = table.Games_activity_turntable_reward.select(roleId);
		if (reward == null)
		{
			logger.info("roleId={} has no reward record", roleId);
			result.put("data", retList);
			return result;
		}
		List<TurntableActivityRewardRecord> list = reward.getRewards();
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		
		for (TurntableActivityRewardRecord record : list)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("playerId", roleId);
			map.put("rewardId", record.getRewardId());
			map.put("rewardName", record.getRewardName());
			map.put("rewardCount", record.getRewardCount());
			map.put("rewardDatetime", format.format(new Date(record.getRewardTime())));
			retList.add(map);
		}
		result.put("data", retList);
		
		return result;
	}

}
