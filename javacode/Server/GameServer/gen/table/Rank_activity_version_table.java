package table;

public class Rank_activity_version_table {
	private Rank_activity_version_table() {
	}

	public static xbean.RankActivityVersion insert(Integer key) {
		return _Tables_.instance.rank_activity_version_table.insert(key);
	}

	public static xbean.RankActivityVersion update(Integer key) {
		return _Tables_.instance.rank_activity_version_table.update(key);
	}

	public static xbean.RankActivityVersion select(Integer key) {
		return _Tables_.instance.rank_activity_version_table.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.rank_activity_version_table.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.RankActivityVersion> get() {
		return _Tables_.instance.rank_activity_version_table;
	}

}
