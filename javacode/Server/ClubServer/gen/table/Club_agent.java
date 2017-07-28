package table;

public class Club_agent {
	private Club_agent() {
	}

	public static xbean.ClubAgent insert(Integer key) {
		return _Tables_.instance.club_agent.insert(key);
	}

	public static xbean.ClubAgent update(Integer key) {
		return _Tables_.instance.club_agent.update(key);
	}

	public static xbean.ClubAgent select(Integer key) {
		return _Tables_.instance.club_agent.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.club_agent.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.ClubAgent> get() {
		return _Tables_.instance.club_agent;
	}

}
