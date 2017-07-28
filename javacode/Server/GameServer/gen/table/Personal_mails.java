package table;

public class Personal_mails {
	private Personal_mails() {
	}

	public static xbean.Mail insert(Long key) {
		return _Tables_.instance.personal_mails.insert(key);
	}

	public static Long newKey() {
		return _Tables_.instance.personal_mails.newKey();
	}

	public static limax.util.Pair<Long, xbean.Mail> insert() {
		return _Tables_.instance.personal_mails.insert();
	}

	public static xbean.Mail update(Long key) {
		return _Tables_.instance.personal_mails.update(key);
	}

	public static xbean.Mail select(Long key) {
		return _Tables_.instance.personal_mails.select(key);
	}

	public static boolean delete(Long key) {
		return _Tables_.instance.personal_mails.delete(key);
	}

	public static limax.zdb.TTable<Long, xbean.Mail> get() {
		return _Tables_.instance.personal_mails;
	}

}
