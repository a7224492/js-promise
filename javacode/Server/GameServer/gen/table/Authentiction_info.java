package table;

public class Authentiction_info {
	private Authentiction_info() {
	}

	public static xbean.AuthentictionInfo insert(Integer key) {
		return _Tables_.instance.authentiction_info.insert(key);
	}

	public static xbean.AuthentictionInfo update(Integer key) {
		return _Tables_.instance.authentiction_info.update(key);
	}

	public static xbean.AuthentictionInfo select(Integer key) {
		return _Tables_.instance.authentiction_info.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.authentiction_info.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.AuthentictionInfo> get() {
		return _Tables_.instance.authentiction_info;
	}

}
