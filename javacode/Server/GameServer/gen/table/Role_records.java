package table;

public class Role_records {
	private Role_records() {
	}

	public static xbean.RoleRecord insert(Integer key) {
		return _Tables_.instance.role_records.insert(key);
	}

	public static xbean.RoleRecord update(Integer key) {
		return _Tables_.instance.role_records.update(key);
	}

	public static xbean.RoleRecord select(Integer key) {
		return _Tables_.instance.role_records.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.role_records.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.RoleRecord> get() {
		return _Tables_.instance.role_records;
	}

}
