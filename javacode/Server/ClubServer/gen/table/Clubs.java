package table;

public class Clubs {
	private Clubs() {
	}

	public static xbean.ClubInfo insert(Integer key) {
		return _Tables_.instance.clubs.insert(key);
	}

	public static xbean.ClubInfo update(Integer key) {
		return _Tables_.instance.clubs.update(key);
	}

	public static xbean.ClubInfo select(Integer key) {
		return _Tables_.instance.clubs.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.clubs.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.ClubInfo> get() {
		return _Tables_.instance.clubs;
	}

}
