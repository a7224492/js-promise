package table;

public class Club_room_history {
	private Club_room_history() {
	}

	public static xbean.ClubRoomHistory insert(cbean.RoleClubId key) {
		return _Tables_.instance.club_room_history.insert(key);
	}

	public static xbean.ClubRoomHistory update(cbean.RoleClubId key) {
		return _Tables_.instance.club_room_history.update(key);
	}

	public static xbean.ClubRoomHistory select(cbean.RoleClubId key) {
		return _Tables_.instance.club_room_history.select(key);
	}

	public static boolean delete(cbean.RoleClubId key) {
		return _Tables_.instance.club_room_history.delete(key);
	}

	public static limax.zdb.TTable<cbean.RoleClubId, xbean.ClubRoomHistory> get() {
		return _Tables_.instance.club_room_history;
	}

}
