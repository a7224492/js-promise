package table;

public class Deviceid_accountid_table {
	private Deviceid_accountid_table() {
	}

	public static xbean.DeviceidAccountidBean insert(String key) {
		return _Tables_.instance.deviceid_accountid_table.insert(key);
	}

	public static xbean.DeviceidAccountidBean update(String key) {
		return _Tables_.instance.deviceid_accountid_table.update(key);
	}

	public static xbean.DeviceidAccountidBean select(String key) {
		return _Tables_.instance.deviceid_accountid_table.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.deviceid_accountid_table.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.DeviceidAccountidBean> get() {
		return _Tables_.instance.deviceid_accountid_table;
	}

}
