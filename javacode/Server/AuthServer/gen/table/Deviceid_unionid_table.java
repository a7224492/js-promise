package table;

public class Deviceid_unionid_table {
	private Deviceid_unionid_table() {
	}

	public static xbean.DeviceidUnionidBean insert(String key) {
		return _Tables_.instance.deviceid_unionid_table.insert(key);
	}

	public static xbean.DeviceidUnionidBean update(String key) {
		return _Tables_.instance.deviceid_unionid_table.update(key);
	}

	public static xbean.DeviceidUnionidBean select(String key) {
		return _Tables_.instance.deviceid_unionid_table.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.deviceid_unionid_table.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.DeviceidUnionidBean> get() {
		return _Tables_.instance.deviceid_unionid_table;
	}

}
