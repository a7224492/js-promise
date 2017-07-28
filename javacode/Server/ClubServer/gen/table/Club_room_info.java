package table;

public class Club_room_info {
	private Club_room_info() {
	}

	public static xbean.ClubRooms insert(Integer key) {
		return _Tables_.instance.club_room_info.insert(key);
	}

	public static xbean.ClubRooms update(Integer key) {
		return _Tables_.instance.club_room_info.update(key);
	}

	public static xbean.ClubRooms select(Integer key) {
		return _Tables_.instance.club_room_info.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.club_room_info.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.ClubRooms> get() {
		return _Tables_.instance.club_room_info;
	}

}
