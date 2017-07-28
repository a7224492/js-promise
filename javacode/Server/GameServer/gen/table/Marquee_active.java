package table;

public class Marquee_active {
	private Marquee_active() {
	}

	public static xbean.Marquee insert(Long key) {
		return _Tables_.instance.marquee_active.insert(key);
	}

	public static Long newKey() {
		return _Tables_.instance.marquee_active.newKey();
	}

	public static limax.util.Pair<Long, xbean.Marquee> insert() {
		return _Tables_.instance.marquee_active.insert();
	}

	public static xbean.Marquee update(Long key) {
		return _Tables_.instance.marquee_active.update(key);
	}

	public static xbean.Marquee select(Long key) {
		return _Tables_.instance.marquee_active.select(key);
	}

	public static boolean delete(Long key) {
		return _Tables_.instance.marquee_active.delete(key);
	}

	public static limax.zdb.TTable<Long, xbean.Marquee> get() {
		return _Tables_.instance.marquee_active;
	}

}
