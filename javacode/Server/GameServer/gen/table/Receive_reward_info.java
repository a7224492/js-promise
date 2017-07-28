package table;

public class Receive_reward_info {
	private Receive_reward_info() {
	}

	public static xbean.ReceiveRewardInfo insert(Long key) {
		return _Tables_.instance.receive_reward_info.insert(key);
	}

	public static Long newKey() {
		return _Tables_.instance.receive_reward_info.newKey();
	}

	public static limax.util.Pair<Long, xbean.ReceiveRewardInfo> insert() {
		return _Tables_.instance.receive_reward_info.insert();
	}

	public static xbean.ReceiveRewardInfo update(Long key) {
		return _Tables_.instance.receive_reward_info.update(key);
	}

	public static xbean.ReceiveRewardInfo select(Long key) {
		return _Tables_.instance.receive_reward_info.select(key);
	}

	public static boolean delete(Long key) {
		return _Tables_.instance.receive_reward_info.delete(key);
	}

	public static limax.zdb.TTable<Long, xbean.ReceiveRewardInfo> get() {
		return _Tables_.instance.receive_reward_info;
	}

}
