package table;

public class Global_client_version {
	private Global_client_version() {
	}

	public static xbean.ClientVersion insert(Integer key) {
		return _Tables_.instance.global_client_version.insert(key);
	}

	public static xbean.ClientVersion update(Integer key) {
		return _Tables_.instance.global_client_version.update(key);
	}

	public static xbean.ClientVersion select(Integer key) {
		return _Tables_.instance.global_client_version.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.global_client_version.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.ClientVersion> get() {
		return _Tables_.instance.global_client_version;
	}

}
