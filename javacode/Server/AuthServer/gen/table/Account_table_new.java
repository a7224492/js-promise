package table;

public class Account_table_new {
	private Account_table_new() {
	}

	public static xbean.AccountInfo insert(cbean.AccountTableKey key) {
		return _Tables_.instance.account_table_new.insert(key);
	}

	public static xbean.AccountInfo update(cbean.AccountTableKey key) {
		return _Tables_.instance.account_table_new.update(key);
	}

	public static xbean.AccountInfo select(cbean.AccountTableKey key) {
		return _Tables_.instance.account_table_new.select(key);
	}

	public static boolean delete(cbean.AccountTableKey key) {
		return _Tables_.instance.account_table_new.delete(key);
	}

	public static limax.zdb.TTable<cbean.AccountTableKey, xbean.AccountInfo> get() {
		return _Tables_.instance.account_table_new;
	}

}
