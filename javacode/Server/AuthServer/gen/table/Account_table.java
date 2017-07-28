package table;

public class Account_table {
	private Account_table() {
	}

	public static xbean.AccountInfo insert(String key) {
		return _Tables_.instance.account_table.insert(key);
	}

	public static xbean.AccountInfo update(String key) {
		return _Tables_.instance.account_table.update(key);
	}

	public static xbean.AccountInfo select(String key) {
		return _Tables_.instance.account_table.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.account_table.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.AccountInfo> get() {
		return _Tables_.instance.account_table;
	}

}
