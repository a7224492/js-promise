package table;

public class Last_reward_info_table {
	private Last_reward_info_table() {
	}

	public static xbean.LastRewardInfo insert(cbean.LastRewardKey key) {
		return _Tables_.instance.last_reward_info_table.insert(key);
	}

	public static xbean.LastRewardInfo update(cbean.LastRewardKey key) {
		return _Tables_.instance.last_reward_info_table.update(key);
	}

	public static xbean.LastRewardInfo select(cbean.LastRewardKey key) {
		return _Tables_.instance.last_reward_info_table.select(key);
	}

	public static boolean delete(cbean.LastRewardKey key) {
		return _Tables_.instance.last_reward_info_table.delete(key);
	}

	public static limax.zdb.TTable<cbean.LastRewardKey, xbean.LastRewardInfo> get() {
		return _Tables_.instance.last_reward_info_table;
	}

}
