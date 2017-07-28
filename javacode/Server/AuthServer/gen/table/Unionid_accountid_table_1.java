package table;

public class Unionid_accountid_table_1 {
	private Unionid_accountid_table_1() {
	}

	public static xbean.UnionidAccountidBean insert(String key) {
		return _Tables_.instance.unionid_accountid_table_1.insert(key);
	}

	public static xbean.UnionidAccountidBean update(String key) {
		return _Tables_.instance.unionid_accountid_table_1.update(key);
	}

	public static xbean.UnionidAccountidBean select(String key) {
		return _Tables_.instance.unionid_accountid_table_1.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.unionid_accountid_table_1.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.UnionidAccountidBean> get() {
		return _Tables_.instance.unionid_accountid_table_1;
	}

}
