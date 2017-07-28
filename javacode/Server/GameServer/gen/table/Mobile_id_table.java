package table;

public class Mobile_id_table {
	private Mobile_id_table() {
	}

	public static xbean.MobileIdBean insert(String key) {
		return _Tables_.instance.mobile_id_table.insert(key);
	}

	public static xbean.MobileIdBean update(String key) {
		return _Tables_.instance.mobile_id_table.update(key);
	}

	public static xbean.MobileIdBean select(String key) {
		return _Tables_.instance.mobile_id_table.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.mobile_id_table.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.MobileIdBean> get() {
		return _Tables_.instance.mobile_id_table;
	}

}
