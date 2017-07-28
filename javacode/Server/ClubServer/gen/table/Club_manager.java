package table;

public class Club_manager {
	private Club_manager() {
	}

	public static xbean.ClubManager insert(Integer key) {
		return _Tables_.instance.club_manager.insert(key);
	}

	public static xbean.ClubManager update(Integer key) {
		return _Tables_.instance.club_manager.update(key);
	}

	public static xbean.ClubManager select(Integer key) {
		return _Tables_.instance.club_manager.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.club_manager.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.ClubManager> get() {
		return _Tables_.instance.club_manager;
	}

}
