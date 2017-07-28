package table;

public class Channel_version_table {
	private Channel_version_table() {
	}

	public static xbean.VersionUpdateBean insert(String key) {
		return _Tables_.instance.channel_version_table.insert(key);
	}

	public static xbean.VersionUpdateBean update(String key) {
		return _Tables_.instance.channel_version_table.update(key);
	}

	public static xbean.VersionUpdateBean select(String key) {
		return _Tables_.instance.channel_version_table.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.channel_version_table.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.VersionUpdateBean> get() {
		return _Tables_.instance.channel_version_table;
	}

}
