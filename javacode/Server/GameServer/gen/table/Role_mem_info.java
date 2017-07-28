package table;

public class Role_mem_info {
	private Role_mem_info() {
	}

	public static xbean.RoleMemInfo insert(Integer key) {
		return _Tables_.instance.role_mem_info.insert(key);
	}

	public static xbean.RoleMemInfo update(Integer key) {
		return _Tables_.instance.role_mem_info.update(key);
	}

	public static xbean.RoleMemInfo select(Integer key) {
		return _Tables_.instance.role_mem_info.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.role_mem_info.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.RoleMemInfo> get() {
		return _Tables_.instance.role_mem_info;
	}

}
