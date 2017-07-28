package table;

public class Club_id_seed {
	private Club_id_seed() {
	}

	public static xbean.ClubIdSeed insert(Integer key) {
		return _Tables_.instance.club_id_seed.insert(key);
	}

	public static xbean.ClubIdSeed update(Integer key) {
		return _Tables_.instance.club_id_seed.update(key);
	}

	public static xbean.ClubIdSeed select(Integer key) {
		return _Tables_.instance.club_id_seed.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.club_id_seed.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.ClubIdSeed> get() {
		return _Tables_.instance.club_id_seed;
	}

}
