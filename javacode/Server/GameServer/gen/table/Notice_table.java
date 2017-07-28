package table;

public class Notice_table {
	private Notice_table() {
	}

	public static xbean.Notice insert(Long key) {
		return _Tables_.instance.notice_table.insert(key);
	}

	public static Long newKey() {
		return _Tables_.instance.notice_table.newKey();
	}

	public static limax.util.Pair<Long, xbean.Notice> insert() {
		return _Tables_.instance.notice_table.insert();
	}

	public static xbean.Notice update(Long key) {
		return _Tables_.instance.notice_table.update(key);
	}

	public static xbean.Notice select(Long key) {
		return _Tables_.instance.notice_table.select(key);
	}

	public static boolean delete(Long key) {
		return _Tables_.instance.notice_table.delete(key);
	}

	public static limax.zdb.TTable<Long, xbean.Notice> get() {
		return _Tables_.instance.notice_table;
	}

}
