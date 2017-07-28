package table;

public class Turntable_activity_version_table {
	private Turntable_activity_version_table() {
	}

	public static xbean.TurntableActivityVersion insert(Integer key) {
		return _Tables_.instance.turntable_activity_version_table.insert(key);
	}

	public static xbean.TurntableActivityVersion update(Integer key) {
		return _Tables_.instance.turntable_activity_version_table.update(key);
	}

	public static xbean.TurntableActivityVersion select(Integer key) {
		return _Tables_.instance.turntable_activity_version_table.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.turntable_activity_version_table.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.TurntableActivityVersion> get() {
		return _Tables_.instance.turntable_activity_version_table;
	}

}
