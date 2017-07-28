package table;

public class Account_table_10 {
	private Account_table_10() {
	}

	public static xbean.AccountInfo insert(String key) {
		return _Tables_.instance.account_table_10.insert(key);
	}

	public static xbean.AccountInfo update(String key) {
		return _Tables_.instance.account_table_10.update(key);
	}

	public static xbean.AccountInfo select(String key) {
		return _Tables_.instance.account_table_10.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.account_table_10.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.AccountInfo> get() {
		return _Tables_.instance.account_table_10;
	}

}
