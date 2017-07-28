package table;

public class Pop_up_config {
	private Pop_up_config() {
	}

	public static xbean.PopUpMessageInfo insert(Integer key) {
		return _Tables_.instance.pop_up_config.insert(key);
	}

	public static xbean.PopUpMessageInfo update(Integer key) {
		return _Tables_.instance.pop_up_config.update(key);
	}

	public static xbean.PopUpMessageInfo select(Integer key) {
		return _Tables_.instance.pop_up_config.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.pop_up_config.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.PopUpMessageInfo> get() {
		return _Tables_.instance.pop_up_config;
	}

}
