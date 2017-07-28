package table;

public class Room_info {
	private Room_info() {
	}

	public static xbean.RoomInfo insert(Integer key) {
		return _Tables_.instance.room_info.insert(key);
	}

	public static xbean.RoomInfo update(Integer key) {
		return _Tables_.instance.room_info.update(key);
	}

	public static xbean.RoomInfo select(Integer key) {
		return _Tables_.instance.room_info.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.room_info.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.RoomInfo> get() {
		return _Tables_.instance.room_info;
	}

}
