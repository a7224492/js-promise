package table;

public class Activity_rank {
	private Activity_rank() {
	}

	public static xbean.ActivityRank insert(Integer key) {
		return _Tables_.instance.activity_rank.insert(key);
	}

	public static xbean.ActivityRank update(Integer key) {
		return _Tables_.instance.activity_rank.update(key);
	}

	public static xbean.ActivityRank select(Integer key) {
		return _Tables_.instance.activity_rank.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.activity_rank.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.ActivityRank> get() {
		return _Tables_.instance.activity_rank;
	}

}
