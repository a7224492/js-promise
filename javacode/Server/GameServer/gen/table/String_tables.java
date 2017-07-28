package table;

public class String_tables {
	private String_tables() {
	}

	public static String insert(Integer key, String value) {
		return _Tables_.instance.string_tables.insert(key, value);
	}

	public static String update(Integer key) {
		return _Tables_.instance.string_tables.update(key);
	}

	public static String select(Integer key) {
		return _Tables_.instance.string_tables.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.string_tables.delete(key);
	}

	public static limax.zdb.TTable<Integer, String> get() {
		return _Tables_.instance.string_tables;
	}

}
