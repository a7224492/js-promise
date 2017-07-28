package table;

public class Server_info {
	private Server_info() {
	}

	public static xbean.ServerInfo insert(Integer key) {
		return _Tables_.instance.server_info.insert(key);
	}

	public static xbean.ServerInfo update(Integer key) {
		return _Tables_.instance.server_info.update(key);
	}

	public static xbean.ServerInfo select(Integer key) {
		return _Tables_.instance.server_info.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.server_info.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.ServerInfo> get() {
		return _Tables_.instance.server_info;
	}

}
