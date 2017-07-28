package table;

public class Role_info {
	private Role_info() {
	}

	public static xbean.RoleInfo insert(Integer key) {
		return _Tables_.instance.role_info.insert(key);
	}

	public static xbean.RoleInfo update(Integer key) {
		return _Tables_.instance.role_info.update(key);
	}

	public static xbean.RoleInfo select(Integer key) {
		return _Tables_.instance.role_info.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.role_info.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.RoleInfo> get() {
		return _Tables_.instance.role_info;
	}

}
