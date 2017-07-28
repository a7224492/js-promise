package table;

public class Deviceid_bind_record_table {
	private Deviceid_bind_record_table() {
	}

	public static xbean.DeviceIdBindRecordBean insert(String key) {
		return _Tables_.instance.deviceid_bind_record_table.insert(key);
	}

	public static xbean.DeviceIdBindRecordBean update(String key) {
		return _Tables_.instance.deviceid_bind_record_table.update(key);
	}

	public static xbean.DeviceIdBindRecordBean select(String key) {
		return _Tables_.instance.deviceid_bind_record_table.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.deviceid_bind_record_table.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.DeviceIdBindRecordBean> get() {
		return _Tables_.instance.deviceid_bind_record_table;
	}

}
