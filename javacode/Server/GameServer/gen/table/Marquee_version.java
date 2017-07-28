package table;

public class Marquee_version {
	private Marquee_version() {
	}

	public static xbean.LongValue insert(Integer key) {
		return _Tables_.instance.marquee_version.insert(key);
	}

	public static xbean.LongValue update(Integer key) {
		return _Tables_.instance.marquee_version.update(key);
	}

	public static xbean.LongValue select(Integer key) {
		return _Tables_.instance.marquee_version.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.marquee_version.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.LongValue> get() {
		return _Tables_.instance.marquee_version;
	}

}
