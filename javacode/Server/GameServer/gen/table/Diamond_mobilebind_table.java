package table;

public class Diamond_mobilebind_table {
	private Diamond_mobilebind_table() {
	}

	public static xbean.DiamondMobileBindBean insert(String key) {
		return _Tables_.instance.diamond_mobilebind_table.insert(key);
	}

	public static xbean.DiamondMobileBindBean update(String key) {
		return _Tables_.instance.diamond_mobilebind_table.update(key);
	}

	public static xbean.DiamondMobileBindBean select(String key) {
		return _Tables_.instance.diamond_mobilebind_table.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.diamond_mobilebind_table.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.DiamondMobileBindBean> get() {
		return _Tables_.instance.diamond_mobilebind_table;
	}

}
