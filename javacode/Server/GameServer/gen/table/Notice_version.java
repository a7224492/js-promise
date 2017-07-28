package table;

public class Notice_version {
	private Notice_version() {
	}

	public static xbean.LongValue insert(Integer key) {
		return _Tables_.instance.notice_version.insert(key);
	}

	public static xbean.LongValue update(Integer key) {
		return _Tables_.instance.notice_version.update(key);
	}

	public static xbean.LongValue select(Integer key) {
		return _Tables_.instance.notice_version.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.notice_version.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.LongValue> get() {
		return _Tables_.instance.notice_version;
	}

}
