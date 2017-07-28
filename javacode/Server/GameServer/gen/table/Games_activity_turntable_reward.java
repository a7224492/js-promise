package table;

public class Games_activity_turntable_reward {
	private Games_activity_turntable_reward() {
	}

	public static xbean.TurntableActivityReward insert(Integer key) {
		return _Tables_.instance.games_activity_turntable_reward.insert(key);
	}

	public static xbean.TurntableActivityReward update(Integer key) {
		return _Tables_.instance.games_activity_turntable_reward.update(key);
	}

	public static xbean.TurntableActivityReward select(Integer key) {
		return _Tables_.instance.games_activity_turntable_reward.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.games_activity_turntable_reward.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.TurntableActivityReward> get() {
		return _Tables_.instance.games_activity_turntable_reward;
	}

}
