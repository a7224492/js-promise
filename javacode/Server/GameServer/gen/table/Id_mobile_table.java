package table;

public class Id_mobile_table {
	private Id_mobile_table() {
	}

	public static xbean.IdMobileBean insert(Integer key) {
		return _Tables_.instance.id_mobile_table.insert(key);
	}

	public static xbean.IdMobileBean update(Integer key) {
		return _Tables_.instance.id_mobile_table.update(key);
	}

	public static xbean.IdMobileBean select(Integer key) {
		return _Tables_.instance.id_mobile_table.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.id_mobile_table.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.IdMobileBean> get() {
		return _Tables_.instance.id_mobile_table;
	}

}
