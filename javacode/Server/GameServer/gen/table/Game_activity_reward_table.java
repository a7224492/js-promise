package table;

public class Game_activity_reward_table {
	private Game_activity_reward_table() {
	}

	public static xbean.GameActivityReward insert(cbean.ActivityRewardKey key) {
		return _Tables_.instance.game_activity_reward_table.insert(key);
	}

	public static xbean.GameActivityReward update(cbean.ActivityRewardKey key) {
		return _Tables_.instance.game_activity_reward_table.update(key);
	}

	public static xbean.GameActivityReward select(cbean.ActivityRewardKey key) {
		return _Tables_.instance.game_activity_reward_table.select(key);
	}

	public static boolean delete(cbean.ActivityRewardKey key) {
		return _Tables_.instance.game_activity_reward_table.delete(key);
	}

	public static limax.zdb.TTable<cbean.ActivityRewardKey, xbean.GameActivityReward> get() {
		return _Tables_.instance.game_activity_reward_table;
	}

}
