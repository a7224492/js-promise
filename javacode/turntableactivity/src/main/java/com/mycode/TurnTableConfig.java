package com.mycode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 大转盘配置类
 * 
 * @author jz
 *
 */
class TurnTableConfig {
	private long startTime, endTime;
	private Map<Integer, RewardRatioRange> rewardRationRangeMap = new ConcurrentHashMap<Integer, RewardRatioRange>();

	public TurnTableConfig() {
		
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	public long getEndTime() {
		return endTime;
	}

	public Map<Integer, RewardRatioRange> getRewardRationRangeMap() {
		return rewardRationRangeMap;
	}

	public Map<Integer, RewardRatioRange> getRewardRationRangeByDay(long time) {
		// TODO
		return new ConcurrentHashMap<Integer, RewardRatioRange>();
	}

	public boolean isReward(int rewardId) {
		return !(rewardId == 1);
	}

	public static class RewardRatioRange {
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
}