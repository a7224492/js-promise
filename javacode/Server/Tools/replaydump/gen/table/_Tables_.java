package table;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

final class _Tables_ {
	volatile static _Tables_ instance;

	private _Tables_ () {
		instance = this;
	}

	class Role_history_rooms extends limax.zdb.TTable<Integer, xbean.RoleInfo> {
		@Override
		public String getName() {
			return "role_history_rooms";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.RoleInfo value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = _os_.unmarshal_int();
			return key;
		}

		@Override
		protected xbean.RoleInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleInfo value = new xbean.RoleInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.RoleInfo newValue() {
			xbean.RoleInfo value = new xbean.RoleInfo();
			return value;
		}

		xbean.RoleInfo insert(Integer key) {
			xbean.RoleInfo value = new xbean.RoleInfo();
			return add(key, value) ? value : null;
		}

		xbean.RoleInfo update(Integer key) {
			return get(key, true);
		}

		xbean.RoleInfo select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Role_history_rooms role_history_rooms = new Role_history_rooms();

	class Room_history extends limax.zdb.TTable<cbean.GlobalRoomId, xbean.RoomHistory> {
		@Override
		public String getName() {
			return "room_history";
		}

		@Override
		protected OctetsStream marshalKey(cbean.GlobalRoomId key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.RoomHistory value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected cbean.GlobalRoomId unmarshalKey(OctetsStream _os_) throws MarshalException {
			cbean.GlobalRoomId key = new cbean.GlobalRoomId();
			key.unmarshal(_os_);
			return key;
		}

		@Override
		protected xbean.RoomHistory unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoomHistory value = new xbean.RoomHistory();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.RoomHistory newValue() {
			xbean.RoomHistory value = new xbean.RoomHistory();
			return value;
		}

		xbean.RoomHistory insert(cbean.GlobalRoomId key) {
			xbean.RoomHistory value = new xbean.RoomHistory();
			return add(key, value) ? value : null;
		}

		xbean.RoomHistory update(cbean.GlobalRoomId key) {
			return get(key, true);
		}

		xbean.RoomHistory select(cbean.GlobalRoomId key) {
			return get(key, false);
		}

		boolean delete(cbean.GlobalRoomId key) {
			return remove(key);
		}

	};

	Room_history room_history = new Room_history();


}
