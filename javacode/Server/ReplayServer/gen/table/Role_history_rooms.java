package table;

public class Role_history_rooms {
	private Role_history_rooms() {
	}

	public static xbean.RoleInfo insert(Integer key) {
		return _Tables_.instance.role_history_rooms.insert(key);
	}

	public static xbean.RoleInfo update(Integer key) {
		return _Tables_.instance.role_history_rooms.update(key);
	}

	public static xbean.RoleInfo select(Integer key) {
		return _Tables_.instance.role_history_rooms.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.role_history_rooms.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.RoleInfo> get() {
		return _Tables_.instance.role_history_rooms;
	}

}
