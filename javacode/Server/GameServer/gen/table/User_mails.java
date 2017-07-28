package table;

public class User_mails {
	private User_mails() {
	}

	public static xbean.UserMail insert(Integer key) {
		return _Tables_.instance.user_mails.insert(key);
	}

	public static xbean.UserMail update(Integer key) {
		return _Tables_.instance.user_mails.update(key);
	}

	public static xbean.UserMail select(Integer key) {
		return _Tables_.instance.user_mails.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.user_mails.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.UserMail> get() {
		return _Tables_.instance.user_mails;
	}

}
