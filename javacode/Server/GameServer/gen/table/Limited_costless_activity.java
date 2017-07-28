package table;

public class Limited_costless_activity {
	private Limited_costless_activity() {
	}

	public static xbean.LimitedCostlessActivity insert(Long key) {
		return _Tables_.instance.limited_costless_activity.insert(key);
	}

	public static Long newKey() {
		return _Tables_.instance.limited_costless_activity.newKey();
	}

	public static limax.util.Pair<Long, xbean.LimitedCostlessActivity> insert() {
		return _Tables_.instance.limited_costless_activity.insert();
	}

	public static xbean.LimitedCostlessActivity update(Long key) {
		return _Tables_.instance.limited_costless_activity.update(key);
	}

	public static xbean.LimitedCostlessActivity select(Long key) {
		return _Tables_.instance.limited_costless_activity.select(key);
	}

	public static boolean delete(Long key) {
		return _Tables_.instance.limited_costless_activity.delete(key);
	}

	public static limax.zdb.TTable<Long, xbean.LimitedCostlessActivity> get() {
		return _Tables_.instance.limited_costless_activity;
	}

}
