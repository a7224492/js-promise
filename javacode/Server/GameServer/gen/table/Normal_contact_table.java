package table;

public class Normal_contact_table {
	private Normal_contact_table() {
	}

	public static xbean.NormalContact insert(Long key) {
		return _Tables_.instance.normal_contact_table.insert(key);
	}

	public static Long newKey() {
		return _Tables_.instance.normal_contact_table.newKey();
	}

	public static limax.util.Pair<Long, xbean.NormalContact> insert() {
		return _Tables_.instance.normal_contact_table.insert();
	}

	public static xbean.NormalContact update(Long key) {
		return _Tables_.instance.normal_contact_table.update(key);
	}

	public static xbean.NormalContact select(Long key) {
		return _Tables_.instance.normal_contact_table.select(key);
	}

	public static boolean delete(Long key) {
		return _Tables_.instance.normal_contact_table.delete(key);
	}

	public static limax.zdb.TTable<Long, xbean.NormalContact> get() {
		return _Tables_.instance.normal_contact_table;
	}

}
