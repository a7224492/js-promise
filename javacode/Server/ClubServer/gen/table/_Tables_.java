package table;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

final class _Tables_ {
	volatile static _Tables_ instance;

	private _Tables_ () {
		instance = this;
	}

	class Clubs extends limax.zdb.TTable<Integer, xbean.ClubInfo> {
		@Override
		public String getName() {
			return "clubs";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.ClubInfo value) {
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
		protected xbean.ClubInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClubInfo value = new xbean.ClubInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.ClubInfo newValue() {
			xbean.ClubInfo value = new xbean.ClubInfo();
			return value;
		}

		xbean.ClubInfo insert(Integer key) {
			xbean.ClubInfo value = new xbean.ClubInfo();
			return add(key, value) ? value : null;
		}

		xbean.ClubInfo update(Integer key) {
			return get(key, true);
		}

		xbean.ClubInfo select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Clubs clubs = new Clubs();

	class Club_room_info extends limax.zdb.TTable<Integer, xbean.ClubRooms> {
		@Override
		public String getName() {
			return "club_room_info";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected OctetsStream marshalValue(xbean.ClubRooms value) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		protected xbean.ClubRooms unmarshalValue(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		protected xbean.ClubRooms newValue() {
			xbean.ClubRooms value = new xbean.ClubRooms();
			return value;
		}

		xbean.ClubRooms insert(Integer key) {
			xbean.ClubRooms value = new xbean.ClubRooms();
			return add(key, value) ? value : null;
		}

		xbean.ClubRooms update(Integer key) {
			return get(key, true);
		}

		xbean.ClubRooms select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Club_room_info club_room_info = new Club_room_info();

	class Club_id_seed extends limax.zdb.TTable<Integer, xbean.ClubIdSeed> {
		@Override
		public String getName() {
			return "club_id_seed";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.ClubIdSeed value) {
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
		protected xbean.ClubIdSeed unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClubIdSeed value = new xbean.ClubIdSeed();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.ClubIdSeed newValue() {
			xbean.ClubIdSeed value = new xbean.ClubIdSeed();
			return value;
		}

		xbean.ClubIdSeed insert(Integer key) {
			xbean.ClubIdSeed value = new xbean.ClubIdSeed();
			return add(key, value) ? value : null;
		}

		xbean.ClubIdSeed update(Integer key) {
			return get(key, true);
		}

		xbean.ClubIdSeed select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Club_id_seed club_id_seed = new Club_id_seed();

	class Role_clubs extends limax.zdb.TTable<Integer, xbean.RoleClubs> {
		@Override
		public String getName() {
			return "role_clubs";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.RoleClubs value) {
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
		protected xbean.RoleClubs unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleClubs value = new xbean.RoleClubs();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.RoleClubs newValue() {
			xbean.RoleClubs value = new xbean.RoleClubs();
			return value;
		}

		xbean.RoleClubs insert(Integer key) {
			xbean.RoleClubs value = new xbean.RoleClubs();
			return add(key, value) ? value : null;
		}

		xbean.RoleClubs update(Integer key) {
			return get(key, true);
		}

		xbean.RoleClubs select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Role_clubs role_clubs = new Role_clubs();

	class Club_manager extends limax.zdb.TTable<Integer, xbean.ClubManager> {
		@Override
		public String getName() {
			return "club_manager";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.ClubManager value) {
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
		protected xbean.ClubManager unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClubManager value = new xbean.ClubManager();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.ClubManager newValue() {
			xbean.ClubManager value = new xbean.ClubManager();
			return value;
		}

		xbean.ClubManager insert(Integer key) {
			xbean.ClubManager value = new xbean.ClubManager();
			return add(key, value) ? value : null;
		}

		xbean.ClubManager update(Integer key) {
			return get(key, true);
		}

		xbean.ClubManager select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Club_manager club_manager = new Club_manager();

	class Club_agent extends limax.zdb.TTable<Integer, xbean.ClubAgent> {
		@Override
		public String getName() {
			return "club_agent";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.ClubAgent value) {
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
		protected xbean.ClubAgent unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClubAgent value = new xbean.ClubAgent();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.ClubAgent newValue() {
			xbean.ClubAgent value = new xbean.ClubAgent();
			return value;
		}

		xbean.ClubAgent insert(Integer key) {
			xbean.ClubAgent value = new xbean.ClubAgent();
			return add(key, value) ? value : null;
		}

		xbean.ClubAgent update(Integer key) {
			return get(key, true);
		}

		xbean.ClubAgent select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Club_agent club_agent = new Club_agent();


}
