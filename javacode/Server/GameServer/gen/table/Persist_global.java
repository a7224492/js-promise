package table;

public class Persist_global {
	private Persist_global() {
	}

	public static xbean.PersistGlobalInfo insert(Integer key) {
		return _Tables_.instance.persist_global.insert(key);
	}

	public static xbean.PersistGlobalInfo update(Integer key) {
		return _Tables_.instance.persist_global.update(key);
	}

	public static xbean.PersistGlobalInfo select(Integer key) {
		return _Tables_.instance.persist_global.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.persist_global.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.PersistGlobalInfo> get() {
		return _Tables_.instance.persist_global;
	}

}
