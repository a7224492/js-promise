package table;

public class User_account {
	private User_account() {
	}

	public static String insert(Integer key, String value) {
		return _Tables_.instance.user_account.insert(key, value);
	}

	public static String update(Integer key) {
		return _Tables_.instance.user_account.update(key);
	}

	public static String select(Integer key) {
		return _Tables_.instance.user_account.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.user_account.delete(key);
	}

	public static limax.zdb.TTable<Integer, String> get() {
		return _Tables_.instance.user_account;
	}

}
