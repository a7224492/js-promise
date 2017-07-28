package table;

public class Server_id {
	private Server_id() {
	}

	public static xbean.IntValue insert(Integer key) {
		return _Tables_.instance.server_id.insert(key);
	}

	public static xbean.IntValue update(Integer key) {
		return _Tables_.instance.server_id.update(key);
	}

	public static xbean.IntValue select(Integer key) {
		return _Tables_.instance.server_id.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.server_id.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.IntValue> get() {
		return _Tables_.instance.server_id;
	}

}
