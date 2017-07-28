package table;

public class Main_notice_table {
	private Main_notice_table() {
	}

	public static xbean.MainNotice insert(String key) {
		return _Tables_.instance.main_notice_table.insert(key);
	}

	public static xbean.MainNotice update(String key) {
		return _Tables_.instance.main_notice_table.update(key);
	}

	public static xbean.MainNotice select(String key) {
		return _Tables_.instance.main_notice_table.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.main_notice_table.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.MainNotice> get() {
		return _Tables_.instance.main_notice_table;
	}

}
