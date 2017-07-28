package table;

public class Role_clubs {
	private Role_clubs() {
	}

	public static xbean.RoleClubs insert(Integer key) {
		return _Tables_.instance.role_clubs.insert(key);
	}

	public static xbean.RoleClubs update(Integer key) {
		return _Tables_.instance.role_clubs.update(key);
	}

	public static xbean.RoleClubs select(Integer key) {
		return _Tables_.instance.role_clubs.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.role_clubs.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.RoleClubs> get() {
		return _Tables_.instance.role_clubs;
	}

}
