package table;

public class Activity_history_rank {
	private Activity_history_rank() {
	}

	public static xbean.RoleHistoryRank insert(Integer key) {
		return _Tables_.instance.activity_history_rank.insert(key);
	}

	public static xbean.RoleHistoryRank update(Integer key) {
		return _Tables_.instance.activity_history_rank.update(key);
	}

	public static xbean.RoleHistoryRank select(Integer key) {
		return _Tables_.instance.activity_history_rank.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.activity_history_rank.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.RoleHistoryRank> get() {
		return _Tables_.instance.activity_history_rank;
	}

}
