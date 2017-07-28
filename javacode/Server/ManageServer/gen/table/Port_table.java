package table;

public class Port_table {
	private Port_table() {
	}

	public static xbean.PortBase insert(String key) {
		return _Tables_.instance.port_table.insert(key);
	}

	public static xbean.PortBase update(String key) {
		return _Tables_.instance.port_table.update(key);
	}

	public static xbean.PortBase select(String key) {
		return _Tables_.instance.port_table.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.port_table.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.PortBase> get() {
		return _Tables_.instance.port_table;
	}

}
