package table;

public class Account_white_list {
	private Account_white_list() {
	}

	public static xbean.WhiteList insert(Integer key) {
		return _Tables_.instance.account_white_list.insert(key);
	}

	public static xbean.WhiteList update(Integer key) {
		return _Tables_.instance.account_white_list.update(key);
	}

	public static xbean.WhiteList select(Integer key) {
		return _Tables_.instance.account_white_list.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.account_white_list.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.WhiteList> get() {
		return _Tables_.instance.account_white_list;
	}

}
