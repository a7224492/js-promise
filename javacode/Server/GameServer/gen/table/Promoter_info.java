package table;

public class Promoter_info {
	private Promoter_info() {
	}

	public static xbean.PromoterInfo insert(String key) {
		return _Tables_.instance.promoter_info.insert(key);
	}

	public static xbean.PromoterInfo update(String key) {
		return _Tables_.instance.promoter_info.update(key);
	}

	public static xbean.PromoterInfo select(String key) {
		return _Tables_.instance.promoter_info.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.promoter_info.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.PromoterInfo> get() {
		return _Tables_.instance.promoter_info;
	}

}
