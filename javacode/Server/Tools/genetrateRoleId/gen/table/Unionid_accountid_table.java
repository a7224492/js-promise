package table;

public class Unionid_accountid_table {
	private Unionid_accountid_table() {
	}

	public static xbean.UnionidAccountidBean insert(String key) {
		return _Tables_.instance.unionid_accountid_table.insert(key);
	}

	public static xbean.UnionidAccountidBean update(String key) {
		return _Tables_.instance.unionid_accountid_table.update(key);
	}

	public static xbean.UnionidAccountidBean select(String key) {
		return _Tables_.instance.unionid_accountid_table.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.unionid_accountid_table.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.UnionidAccountidBean> get() {
		return _Tables_.instance.unionid_accountid_table;
	}

}
