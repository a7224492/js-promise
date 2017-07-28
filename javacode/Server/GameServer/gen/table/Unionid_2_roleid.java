package table;

public class Unionid_2_roleid {
	private Unionid_2_roleid() {
	}

	public static Integer insert(String key, Integer value) {
		return _Tables_.instance.unionid_2_roleid.insert(key, value);
	}

	public static Integer update(String key) {
		return _Tables_.instance.unionid_2_roleid.update(key);
	}

	public static Integer select(String key) {
		return _Tables_.instance.unionid_2_roleid.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.unionid_2_roleid.delete(key);
	}

	public static limax.zdb.TTable<String, Integer> get() {
		return _Tables_.instance.unionid_2_roleid;
	}

}
