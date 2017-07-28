package table;

public class Room_history {
	private Room_history() {
	}

	public static xbean.RoomHistory insert(cbean.GlobalRoomId key) {
		return _Tables_.instance.room_history.insert(key);
	}

	public static xbean.RoomHistory update(cbean.GlobalRoomId key) {
		return _Tables_.instance.room_history.update(key);
	}

	public static xbean.RoomHistory select(cbean.GlobalRoomId key) {
		return _Tables_.instance.room_history.select(key);
	}

	public static boolean delete(cbean.GlobalRoomId key) {
		return _Tables_.instance.room_history.delete(key);
	}

	public static limax.zdb.TTable<cbean.GlobalRoomId, xbean.RoomHistory> get() {
		return _Tables_.instance.room_history;
	}

}
