package table;

public class Turntable_reward_dispatch_table {
	private Turntable_reward_dispatch_table() {
	}

	public static xbean.TurntableRewardDispatch insert(Integer key) {
		return _Tables_.instance.turntable_reward_dispatch_table.insert(key);
	}

	public static xbean.TurntableRewardDispatch update(Integer key) {
		return _Tables_.instance.turntable_reward_dispatch_table.update(key);
	}

	public static xbean.TurntableRewardDispatch select(Integer key) {
		return _Tables_.instance.turntable_reward_dispatch_table.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.turntable_reward_dispatch_table.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.TurntableRewardDispatch> get() {
		return _Tables_.instance.turntable_reward_dispatch_table;
	}

}
