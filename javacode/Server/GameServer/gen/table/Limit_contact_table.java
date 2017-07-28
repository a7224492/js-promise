package table;

public class Limit_contact_table {
	private Limit_contact_table() {
	}

	public static xbean.LimitContact insert(Long key) {
		return _Tables_.instance.limit_contact_table.insert(key);
	}

	public static Long newKey() {
		return _Tables_.instance.limit_contact_table.newKey();
	}

	public static limax.util.Pair<Long, xbean.LimitContact> insert() {
		return _Tables_.instance.limit_contact_table.insert();
	}

	public static xbean.LimitContact update(Long key) {
		return _Tables_.instance.limit_contact_table.update(key);
	}

	public static xbean.LimitContact select(Long key) {
		return _Tables_.instance.limit_contact_table.select(key);
	}

	public static boolean delete(Long key) {
		return _Tables_.instance.limit_contact_table.delete(key);
	}

	public static limax.zdb.TTable<Long, xbean.LimitContact> get() {
		return _Tables_.instance.limit_contact_table;
	}

}
