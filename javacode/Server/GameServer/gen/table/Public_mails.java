package table;

public class Public_mails {
	private Public_mails() {
	}

	public static xbean.Mail insert(Long key) {
		return _Tables_.instance.public_mails.insert(key);
	}

	public static xbean.Mail update(Long key) {
		return _Tables_.instance.public_mails.update(key);
	}

	public static xbean.Mail select(Long key) {
		return _Tables_.instance.public_mails.select(key);
	}

	public static boolean delete(Long key) {
		return _Tables_.instance.public_mails.delete(key);
	}

	public static limax.zdb.TTable<Long, xbean.Mail> get() {
		return _Tables_.instance.public_mails;
	}

}
