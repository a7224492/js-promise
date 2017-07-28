package table;

public class Runtime_global {
	private Runtime_global() {
	}

	public static xbean.RuntimeGlobalInfo insert(Integer key) {
		return _Tables_.instance.runtime_global.insert(key);
	}

	public static xbean.RuntimeGlobalInfo update(Integer key) {
		return _Tables_.instance.runtime_global.update(key);
	}

	public static xbean.RuntimeGlobalInfo select(Integer key) {
		return _Tables_.instance.runtime_global.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.runtime_global.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.RuntimeGlobalInfo> get() {
		return _Tables_.instance.runtime_global;
	}

}
