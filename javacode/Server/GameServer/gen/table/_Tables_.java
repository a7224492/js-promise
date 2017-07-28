package table;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

final class _Tables_ {
	volatile static _Tables_ instance;

	private _Tables_ () {
		instance = this;
	}

	class Role_info extends limax.zdb.TTable<Integer, xbean.RoleInfo> {
		@Override
		public String getName() {
			return "role_info";
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

	Role_info role_info = new Role_info();

	class Role_mem_info extends limax.zdb.TTable<Integer, xbean.RoleMemInfo> {
		@Override
		public String getName() {
			return "role_mem_info";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.RoleMemInfo value) {
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
		protected xbean.RoleMemInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleMemInfo value = new xbean.RoleMemInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.RoleMemInfo newValue() {
			xbean.RoleMemInfo value = new xbean.RoleMemInfo();
			return value;
		}

		xbean.RoleMemInfo insert(Integer key) {
			xbean.RoleMemInfo value = new xbean.RoleMemInfo();
			return add(key, value) ? value : null;
		}

		xbean.RoleMemInfo update(Integer key) {
			return get(key, true);
		}

		xbean.RoleMemInfo select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Role_mem_info role_mem_info = new Role_mem_info();

	class Room_info extends limax.zdb.TTable<Integer, xbean.RoomInfo> {
		@Override
		public String getName() {
			return "room_info";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.RoomInfo value) {
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
		protected xbean.RoomInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoomInfo value = new xbean.RoomInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.RoomInfo newValue() {
			xbean.RoomInfo value = new xbean.RoomInfo();
			return value;
		}

		xbean.RoomInfo insert(Integer key) {
			xbean.RoomInfo value = new xbean.RoomInfo();
			return add(key, value) ? value : null;
		}

		xbean.RoomInfo update(Integer key) {
			return get(key, true);
		}

		xbean.RoomInfo select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Room_info room_info = new Room_info();

	class Marquee_active extends limax.zdb.TTable<Long, xbean.Marquee> {
		@Override
		public String getName() {
			return "marquee_active";
		}

		@Override
		protected OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.Marquee value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = _os_.unmarshal_long();
			return key;
		}

		@Override
		protected xbean.Marquee unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Marquee value = new xbean.Marquee();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.Marquee newValue() {
			xbean.Marquee value = new xbean.Marquee();
			return value;
		}

		xbean.Marquee insert(Long key) {
			xbean.Marquee value = new xbean.Marquee();
			return add(key, value) ? value : null;
		}

		Long newKey() {
			return nextKey();
		}

		limax.util.Pair<Long, xbean.Marquee> insert() {
			Long next = nextKey();
			return new limax.util.Pair<Long, xbean.Marquee>(next, insert(next));
		}

		xbean.Marquee update(Long key) {
			return get(key, true);
		}

		xbean.Marquee select(Long key) {
			return get(key, false);
		}

		boolean delete(Long key) {
			return remove(key);
		}

	};

	Marquee_active marquee_active = new Marquee_active();

	class Marquee_version extends limax.zdb.TTable<Integer, xbean.LongValue> {
		@Override
		public String getName() {
			return "marquee_version";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.LongValue value) {
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
		protected xbean.LongValue unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LongValue value = new xbean.LongValue();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.LongValue newValue() {
			xbean.LongValue value = new xbean.LongValue();
			return value;
		}

		xbean.LongValue insert(Integer key) {
			xbean.LongValue value = new xbean.LongValue();
			return add(key, value) ? value : null;
		}

		xbean.LongValue update(Integer key) {
			return get(key, true);
		}

		xbean.LongValue select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Marquee_version marquee_version = new Marquee_version();

	class User_mails extends limax.zdb.TTable<Integer, xbean.UserMail> {
		@Override
		public String getName() {
			return "user_mails";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.UserMail value) {
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
		protected xbean.UserMail unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.UserMail value = new xbean.UserMail();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.UserMail newValue() {
			xbean.UserMail value = new xbean.UserMail();
			return value;
		}

		xbean.UserMail insert(Integer key) {
			xbean.UserMail value = new xbean.UserMail();
			return add(key, value) ? value : null;
		}

		xbean.UserMail update(Integer key) {
			return get(key, true);
		}

		xbean.UserMail select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	User_mails user_mails = new User_mails();

	class Personal_mails extends limax.zdb.TTable<Long, xbean.Mail> {
		@Override
		public String getName() {
			return "personal_mails";
		}

		@Override
		protected OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.Mail value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = _os_.unmarshal_long();
			return key;
		}

		@Override
		protected xbean.Mail unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Mail value = new xbean.Mail();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.Mail newValue() {
			xbean.Mail value = new xbean.Mail();
			return value;
		}

		xbean.Mail insert(Long key) {
			xbean.Mail value = new xbean.Mail();
			return add(key, value) ? value : null;
		}

		Long newKey() {
			return nextKey();
		}

		limax.util.Pair<Long, xbean.Mail> insert() {
			Long next = nextKey();
			return new limax.util.Pair<Long, xbean.Mail>(next, insert(next));
		}

		xbean.Mail update(Long key) {
			return get(key, true);
		}

		xbean.Mail select(Long key) {
			return get(key, false);
		}

		boolean delete(Long key) {
			return remove(key);
		}

	};

	Personal_mails personal_mails = new Personal_mails();

	class Public_mails extends limax.zdb.TTable<Long, xbean.Mail> {
		@Override
		public String getName() {
			return "public_mails";
		}

		@Override
		protected OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.Mail value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = _os_.unmarshal_long();
			return key;
		}

		@Override
		protected xbean.Mail unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Mail value = new xbean.Mail();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.Mail newValue() {
			xbean.Mail value = new xbean.Mail();
			return value;
		}

		xbean.Mail insert(Long key) {
			xbean.Mail value = new xbean.Mail();
			return add(key, value) ? value : null;
		}

		xbean.Mail update(Long key) {
			return get(key, true);
		}

		xbean.Mail select(Long key) {
			return get(key, false);
		}

		boolean delete(Long key) {
			return remove(key);
		}

	};

	Public_mails public_mails = new Public_mails();

	class Club_room_history extends limax.zdb.TTable<cbean.RoleClubId, xbean.ClubRoomHistory> {
		@Override
		public String getName() {
			return "club_room_history";
		}

		@Override
		protected OctetsStream marshalKey(cbean.RoleClubId key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.ClubRoomHistory value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected cbean.RoleClubId unmarshalKey(OctetsStream _os_) throws MarshalException {
			cbean.RoleClubId key = new cbean.RoleClubId();
			key.unmarshal(_os_);
			return key;
		}

		@Override
		protected xbean.ClubRoomHistory unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClubRoomHistory value = new xbean.ClubRoomHistory();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.ClubRoomHistory newValue() {
			xbean.ClubRoomHistory value = new xbean.ClubRoomHistory();
			return value;
		}

		xbean.ClubRoomHistory insert(cbean.RoleClubId key) {
			xbean.ClubRoomHistory value = new xbean.ClubRoomHistory();
			return add(key, value) ? value : null;
		}

		xbean.ClubRoomHistory update(cbean.RoleClubId key) {
			return get(key, true);
		}

		xbean.ClubRoomHistory select(cbean.RoleClubId key) {
			return get(key, false);
		}

		boolean delete(cbean.RoleClubId key) {
			return remove(key);
		}

	};

	Club_room_history club_room_history = new Club_room_history();

	class Normal_contact_table extends limax.zdb.TTable<Long, xbean.NormalContact> {
		@Override
		public String getName() {
			return "normal_contact_table";
		}

		@Override
		protected OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.NormalContact value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = _os_.unmarshal_long();
			return key;
		}

		@Override
		protected xbean.NormalContact unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NormalContact value = new xbean.NormalContact();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.NormalContact newValue() {
			xbean.NormalContact value = new xbean.NormalContact();
			return value;
		}

		xbean.NormalContact insert(Long key) {
			xbean.NormalContact value = new xbean.NormalContact();
			return add(key, value) ? value : null;
		}

		Long newKey() {
			return nextKey();
		}

		limax.util.Pair<Long, xbean.NormalContact> insert() {
			Long next = nextKey();
			return new limax.util.Pair<Long, xbean.NormalContact>(next, insert(next));
		}

		xbean.NormalContact update(Long key) {
			return get(key, true);
		}

		xbean.NormalContact select(Long key) {
			return get(key, false);
		}

		boolean delete(Long key) {
			return remove(key);
		}

	};

	Normal_contact_table normal_contact_table = new Normal_contact_table();

	class Limit_contact_table extends limax.zdb.TTable<Long, xbean.LimitContact> {
		@Override
		public String getName() {
			return "limit_contact_table";
		}

		@Override
		protected OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.LimitContact value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = _os_.unmarshal_long();
			return key;
		}

		@Override
		protected xbean.LimitContact unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LimitContact value = new xbean.LimitContact();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.LimitContact newValue() {
			xbean.LimitContact value = new xbean.LimitContact();
			return value;
		}

		xbean.LimitContact insert(Long key) {
			xbean.LimitContact value = new xbean.LimitContact();
			return add(key, value) ? value : null;
		}

		Long newKey() {
			return nextKey();
		}

		limax.util.Pair<Long, xbean.LimitContact> insert() {
			Long next = nextKey();
			return new limax.util.Pair<Long, xbean.LimitContact>(next, insert(next));
		}

		xbean.LimitContact update(Long key) {
			return get(key, true);
		}

		xbean.LimitContact select(Long key) {
			return get(key, false);
		}

		boolean delete(Long key) {
			return remove(key);
		}

	};

	Limit_contact_table limit_contact_table = new Limit_contact_table();

	class Add_card_table extends limax.zdb.TTable<Integer, xbean.PlayerAddCard> {
		@Override
		public String getName() {
			return "add_card_table";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.PlayerAddCard value) {
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
		protected xbean.PlayerAddCard unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PlayerAddCard value = new xbean.PlayerAddCard();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.PlayerAddCard newValue() {
			xbean.PlayerAddCard value = new xbean.PlayerAddCard();
			return value;
		}

		xbean.PlayerAddCard insert(Integer key) {
			xbean.PlayerAddCard value = new xbean.PlayerAddCard();
			return add(key, value) ? value : null;
		}

		xbean.PlayerAddCard update(Integer key) {
			return get(key, true);
		}

		xbean.PlayerAddCard select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Add_card_table add_card_table = new Add_card_table();

	class Sub_card_table extends limax.zdb.TTable<Integer, xbean.PlayerSubCard> {
		@Override
		public String getName() {
			return "sub_card_table";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.PlayerSubCard value) {
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
		protected xbean.PlayerSubCard unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PlayerSubCard value = new xbean.PlayerSubCard();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.PlayerSubCard newValue() {
			xbean.PlayerSubCard value = new xbean.PlayerSubCard();
			return value;
		}

		xbean.PlayerSubCard insert(Integer key) {
			xbean.PlayerSubCard value = new xbean.PlayerSubCard();
			return add(key, value) ? value : null;
		}

		xbean.PlayerSubCard update(Integer key) {
			return get(key, true);
		}

		xbean.PlayerSubCard select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Sub_card_table sub_card_table = new Sub_card_table();

	class Notice_table extends limax.zdb.TTable<Long, xbean.Notice> {
		@Override
		public String getName() {
			return "notice_table";
		}

		@Override
		protected OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.Notice value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = _os_.unmarshal_long();
			return key;
		}

		@Override
		protected xbean.Notice unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Notice value = new xbean.Notice();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.Notice newValue() {
			xbean.Notice value = new xbean.Notice();
			return value;
		}

		xbean.Notice insert(Long key) {
			xbean.Notice value = new xbean.Notice();
			return add(key, value) ? value : null;
		}

		Long newKey() {
			return nextKey();
		}

		limax.util.Pair<Long, xbean.Notice> insert() {
			Long next = nextKey();
			return new limax.util.Pair<Long, xbean.Notice>(next, insert(next));
		}

		xbean.Notice update(Long key) {
			return get(key, true);
		}

		xbean.Notice select(Long key) {
			return get(key, false);
		}

		boolean delete(Long key) {
			return remove(key);
		}

	};

	Notice_table notice_table = new Notice_table();

	class Notice_version extends limax.zdb.TTable<Integer, xbean.LongValue> {
		@Override
		public String getName() {
			return "notice_version";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.LongValue value) {
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
		protected xbean.LongValue unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LongValue value = new xbean.LongValue();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.LongValue newValue() {
			xbean.LongValue value = new xbean.LongValue();
			return value;
		}

		xbean.LongValue insert(Integer key) {
			xbean.LongValue value = new xbean.LongValue();
			return add(key, value) ? value : null;
		}

		xbean.LongValue update(Integer key) {
			return get(key, true);
		}

		xbean.LongValue select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Notice_version notice_version = new Notice_version();

	class Add_all_card_record extends limax.zdb.TTable<Long, xbean.AddAllCardRecord> {
		@Override
		public String getName() {
			return "add_all_card_record";
		}

		@Override
		protected OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AddAllCardRecord value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = _os_.unmarshal_long();
			return key;
		}

		@Override
		protected xbean.AddAllCardRecord unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AddAllCardRecord value = new xbean.AddAllCardRecord();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AddAllCardRecord newValue() {
			xbean.AddAllCardRecord value = new xbean.AddAllCardRecord();
			return value;
		}

		xbean.AddAllCardRecord insert(Long key) {
			xbean.AddAllCardRecord value = new xbean.AddAllCardRecord();
			return add(key, value) ? value : null;
		}

		Long newKey() {
			return nextKey();
		}

		limax.util.Pair<Long, xbean.AddAllCardRecord> insert() {
			Long next = nextKey();
			return new limax.util.Pair<Long, xbean.AddAllCardRecord>(next, insert(next));
		}

		xbean.AddAllCardRecord update(Long key) {
			return get(key, true);
		}

		xbean.AddAllCardRecord select(Long key) {
			return get(key, false);
		}

		boolean delete(Long key) {
			return remove(key);
		}

	};

	Add_all_card_record add_all_card_record = new Add_all_card_record();

	class Main_notice_table extends limax.zdb.TTable<String, xbean.MainNotice> {
		@Override
		public String getName() {
			return "main_notice_table";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.MainNotice value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = _os_.unmarshal_String();
			return key;
		}

		@Override
		protected xbean.MainNotice unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.MainNotice value = new xbean.MainNotice();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.MainNotice newValue() {
			xbean.MainNotice value = new xbean.MainNotice();
			return value;
		}

		xbean.MainNotice insert(String key) {
			xbean.MainNotice value = new xbean.MainNotice();
			return add(key, value) ? value : null;
		}

		xbean.MainNotice update(String key) {
			return get(key, true);
		}

		xbean.MainNotice select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Main_notice_table main_notice_table = new Main_notice_table();

	class Turntable_activity_version_table extends limax.zdb.TTable<Integer, xbean.TurntableActivityVersion> {
		@Override
		public String getName() {
			return "turntable_activity_version_table";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.TurntableActivityVersion value) {
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
		protected xbean.TurntableActivityVersion unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TurntableActivityVersion value = new xbean.TurntableActivityVersion();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.TurntableActivityVersion newValue() {
			xbean.TurntableActivityVersion value = new xbean.TurntableActivityVersion();
			return value;
		}

		xbean.TurntableActivityVersion insert(Integer key) {
			xbean.TurntableActivityVersion value = new xbean.TurntableActivityVersion();
			return add(key, value) ? value : null;
		}

		xbean.TurntableActivityVersion update(Integer key) {
			return get(key, true);
		}

		xbean.TurntableActivityVersion select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Turntable_activity_version_table turntable_activity_version_table = new Turntable_activity_version_table();

	class Game_activity_reward_table extends limax.zdb.TTable<cbean.ActivityRewardKey, xbean.GameActivityReward> {
		@Override
		public String getName() {
			return "game_activity_reward_table";
		}

		@Override
		protected OctetsStream marshalKey(cbean.ActivityRewardKey key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.GameActivityReward value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected cbean.ActivityRewardKey unmarshalKey(OctetsStream _os_) throws MarshalException {
			cbean.ActivityRewardKey key = new cbean.ActivityRewardKey();
			key.unmarshal(_os_);
			return key;
		}

		@Override
		protected xbean.GameActivityReward unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.GameActivityReward value = new xbean.GameActivityReward();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.GameActivityReward newValue() {
			xbean.GameActivityReward value = new xbean.GameActivityReward();
			return value;
		}

		xbean.GameActivityReward insert(cbean.ActivityRewardKey key) {
			xbean.GameActivityReward value = new xbean.GameActivityReward();
			return add(key, value) ? value : null;
		}

		xbean.GameActivityReward update(cbean.ActivityRewardKey key) {
			return get(key, true);
		}

		xbean.GameActivityReward select(cbean.ActivityRewardKey key) {
			return get(key, false);
		}

		boolean delete(cbean.ActivityRewardKey key) {
			return remove(key);
		}

	};

	Game_activity_reward_table game_activity_reward_table = new Game_activity_reward_table();

	class Games_activity_turntable_reward extends limax.zdb.TTable<Integer, xbean.TurntableActivityReward> {
		@Override
		public String getName() {
			return "games_activity_turntable_reward";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.TurntableActivityReward value) {
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
		protected xbean.TurntableActivityReward unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TurntableActivityReward value = new xbean.TurntableActivityReward();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.TurntableActivityReward newValue() {
			xbean.TurntableActivityReward value = new xbean.TurntableActivityReward();
			return value;
		}

		xbean.TurntableActivityReward insert(Integer key) {
			xbean.TurntableActivityReward value = new xbean.TurntableActivityReward();
			return add(key, value) ? value : null;
		}

		xbean.TurntableActivityReward update(Integer key) {
			return get(key, true);
		}

		xbean.TurntableActivityReward select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Games_activity_turntable_reward games_activity_turntable_reward = new Games_activity_turntable_reward();

	class Turntable_reward_dispatch_table extends limax.zdb.TTable<Integer, xbean.TurntableRewardDispatch> {
		@Override
		public String getName() {
			return "turntable_reward_dispatch_table";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.TurntableRewardDispatch value) {
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
		protected xbean.TurntableRewardDispatch unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TurntableRewardDispatch value = new xbean.TurntableRewardDispatch();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.TurntableRewardDispatch newValue() {
			xbean.TurntableRewardDispatch value = new xbean.TurntableRewardDispatch();
			return value;
		}

		xbean.TurntableRewardDispatch insert(Integer key) {
			xbean.TurntableRewardDispatch value = new xbean.TurntableRewardDispatch();
			return add(key, value) ? value : null;
		}

		xbean.TurntableRewardDispatch update(Integer key) {
			return get(key, true);
		}

		xbean.TurntableRewardDispatch select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Turntable_reward_dispatch_table turntable_reward_dispatch_table = new Turntable_reward_dispatch_table();

	class Last_reward_info_table extends limax.zdb.TTable<cbean.LastRewardKey, xbean.LastRewardInfo> {
		@Override
		public String getName() {
			return "last_reward_info_table";
		}

		@Override
		protected OctetsStream marshalKey(cbean.LastRewardKey key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.LastRewardInfo value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected cbean.LastRewardKey unmarshalKey(OctetsStream _os_) throws MarshalException {
			cbean.LastRewardKey key = new cbean.LastRewardKey();
			key.unmarshal(_os_);
			return key;
		}

		@Override
		protected xbean.LastRewardInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LastRewardInfo value = new xbean.LastRewardInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.LastRewardInfo newValue() {
			xbean.LastRewardInfo value = new xbean.LastRewardInfo();
			return value;
		}

		xbean.LastRewardInfo insert(cbean.LastRewardKey key) {
			xbean.LastRewardInfo value = new xbean.LastRewardInfo();
			return add(key, value) ? value : null;
		}

		xbean.LastRewardInfo update(cbean.LastRewardKey key) {
			return get(key, true);
		}

		xbean.LastRewardInfo select(cbean.LastRewardKey key) {
			return get(key, false);
		}

		boolean delete(cbean.LastRewardKey key) {
			return remove(key);
		}

	};

	Last_reward_info_table last_reward_info_table = new Last_reward_info_table();

	class Button_table extends limax.zdb.TTable<Integer, xbean.ButtonTableMap> {
		@Override
		public String getName() {
			return "button_table";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.ButtonTableMap value) {
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
		protected xbean.ButtonTableMap unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ButtonTableMap value = new xbean.ButtonTableMap();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.ButtonTableMap newValue() {
			xbean.ButtonTableMap value = new xbean.ButtonTableMap();
			return value;
		}

		xbean.ButtonTableMap insert(Integer key) {
			xbean.ButtonTableMap value = new xbean.ButtonTableMap();
			return add(key, value) ? value : null;
		}

		xbean.ButtonTableMap update(Integer key) {
			return get(key, true);
		}

		xbean.ButtonTableMap select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Button_table button_table = new Button_table();

	class Limited_costless_activity extends limax.zdb.TTable<Long, xbean.LimitedCostlessActivity> {
		@Override
		public String getName() {
			return "limited_costless_activity";
		}

		@Override
		protected OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.LimitedCostlessActivity value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = _os_.unmarshal_long();
			return key;
		}

		@Override
		protected xbean.LimitedCostlessActivity unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LimitedCostlessActivity value = new xbean.LimitedCostlessActivity();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.LimitedCostlessActivity newValue() {
			xbean.LimitedCostlessActivity value = new xbean.LimitedCostlessActivity();
			return value;
		}

		xbean.LimitedCostlessActivity insert(Long key) {
			xbean.LimitedCostlessActivity value = new xbean.LimitedCostlessActivity();
			return add(key, value) ? value : null;
		}

		Long newKey() {
			return nextKey();
		}

		limax.util.Pair<Long, xbean.LimitedCostlessActivity> insert() {
			Long next = nextKey();
			return new limax.util.Pair<Long, xbean.LimitedCostlessActivity>(next, insert(next));
		}

		xbean.LimitedCostlessActivity update(Long key) {
			return get(key, true);
		}

		xbean.LimitedCostlessActivity select(Long key) {
			return get(key, false);
		}

		boolean delete(Long key) {
			return remove(key);
		}

	};

	Limited_costless_activity limited_costless_activity = new Limited_costless_activity();

	class Add_players_card_record_table extends limax.zdb.TTable<Integer, xbean.AddPlayersCardRecord> {
		@Override
		public String getName() {
			return "add_players_card_record_table";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AddPlayersCardRecord value) {
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
		protected xbean.AddPlayersCardRecord unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AddPlayersCardRecord value = new xbean.AddPlayersCardRecord();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AddPlayersCardRecord newValue() {
			xbean.AddPlayersCardRecord value = new xbean.AddPlayersCardRecord();
			return value;
		}

		xbean.AddPlayersCardRecord insert(Integer key) {
			xbean.AddPlayersCardRecord value = new xbean.AddPlayersCardRecord();
			return add(key, value) ? value : null;
		}

		xbean.AddPlayersCardRecord update(Integer key) {
			return get(key, true);
		}

		xbean.AddPlayersCardRecord select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Add_players_card_record_table add_players_card_record_table = new Add_players_card_record_table();

	class Add_players_card_detail_table extends limax.zdb.TTable<cbean.AddPlayersCardDetailKey, xbean.AddPlayersCardDetail> {
		@Override
		public String getName() {
			return "add_players_card_detail_table";
		}

		@Override
		protected OctetsStream marshalKey(cbean.AddPlayersCardDetailKey key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AddPlayersCardDetail value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected cbean.AddPlayersCardDetailKey unmarshalKey(OctetsStream _os_) throws MarshalException {
			cbean.AddPlayersCardDetailKey key = new cbean.AddPlayersCardDetailKey();
			key.unmarshal(_os_);
			return key;
		}

		@Override
		protected xbean.AddPlayersCardDetail unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AddPlayersCardDetail value = new xbean.AddPlayersCardDetail();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AddPlayersCardDetail newValue() {
			xbean.AddPlayersCardDetail value = new xbean.AddPlayersCardDetail();
			return value;
		}

		xbean.AddPlayersCardDetail insert(cbean.AddPlayersCardDetailKey key) {
			xbean.AddPlayersCardDetail value = new xbean.AddPlayersCardDetail();
			return add(key, value) ? value : null;
		}

		xbean.AddPlayersCardDetail update(cbean.AddPlayersCardDetailKey key) {
			return get(key, true);
		}

		xbean.AddPlayersCardDetail select(cbean.AddPlayersCardDetailKey key) {
			return get(key, false);
		}

		boolean delete(cbean.AddPlayersCardDetailKey key) {
			return remove(key);
		}

	};

	Add_players_card_detail_table add_players_card_detail_table = new Add_players_card_detail_table();

	class Runtime_global extends limax.zdb.TTable<Integer, xbean.RuntimeGlobalInfo> {
		@Override
		public String getName() {
			return "runtime_global";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected OctetsStream marshalValue(xbean.RuntimeGlobalInfo value) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		protected xbean.RuntimeGlobalInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		protected xbean.RuntimeGlobalInfo newValue() {
			xbean.RuntimeGlobalInfo value = new xbean.RuntimeGlobalInfo();
			return value;
		}

		xbean.RuntimeGlobalInfo insert(Integer key) {
			xbean.RuntimeGlobalInfo value = new xbean.RuntimeGlobalInfo();
			return add(key, value) ? value : null;
		}

		xbean.RuntimeGlobalInfo update(Integer key) {
			return get(key, true);
		}

		xbean.RuntimeGlobalInfo select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Runtime_global runtime_global = new Runtime_global();

	class Persist_global extends limax.zdb.TTable<Integer, xbean.PersistGlobalInfo> {
		@Override
		public String getName() {
			return "persist_global";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.PersistGlobalInfo value) {
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
		protected xbean.PersistGlobalInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PersistGlobalInfo value = new xbean.PersistGlobalInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.PersistGlobalInfo newValue() {
			xbean.PersistGlobalInfo value = new xbean.PersistGlobalInfo();
			return value;
		}

		xbean.PersistGlobalInfo insert(Integer key) {
			xbean.PersistGlobalInfo value = new xbean.PersistGlobalInfo();
			return add(key, value) ? value : null;
		}

		xbean.PersistGlobalInfo update(Integer key) {
			return get(key, true);
		}

		xbean.PersistGlobalInfo select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Persist_global persist_global = new Persist_global();

	class Rank_activity_version_table extends limax.zdb.TTable<Integer, xbean.RankActivityVersion> {
		@Override
		public String getName() {
			return "rank_activity_version_table";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.RankActivityVersion value) {
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
		protected xbean.RankActivityVersion unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RankActivityVersion value = new xbean.RankActivityVersion();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.RankActivityVersion newValue() {
			xbean.RankActivityVersion value = new xbean.RankActivityVersion();
			return value;
		}

		xbean.RankActivityVersion insert(Integer key) {
			xbean.RankActivityVersion value = new xbean.RankActivityVersion();
			return add(key, value) ? value : null;
		}

		xbean.RankActivityVersion update(Integer key) {
			return get(key, true);
		}

		xbean.RankActivityVersion select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Rank_activity_version_table rank_activity_version_table = new Rank_activity_version_table();

	class Activity_rank extends limax.zdb.TTable<Integer, xbean.ActivityRank> {
		@Override
		public String getName() {
			return "activity_rank";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.ActivityRank value) {
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
		protected xbean.ActivityRank unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ActivityRank value = new xbean.ActivityRank();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.ActivityRank newValue() {
			xbean.ActivityRank value = new xbean.ActivityRank();
			return value;
		}

		xbean.ActivityRank insert(Integer key) {
			xbean.ActivityRank value = new xbean.ActivityRank();
			return add(key, value) ? value : null;
		}

		xbean.ActivityRank update(Integer key) {
			return get(key, true);
		}

		xbean.ActivityRank select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Activity_rank activity_rank = new Activity_rank();

	class Activity_history_rank extends limax.zdb.TTable<Integer, xbean.RoleHistoryRank> {
		@Override
		public String getName() {
			return "activity_history_rank";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.RoleHistoryRank value) {
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
		protected xbean.RoleHistoryRank unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleHistoryRank value = new xbean.RoleHistoryRank();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.RoleHistoryRank newValue() {
			xbean.RoleHistoryRank value = new xbean.RoleHistoryRank();
			return value;
		}

		xbean.RoleHistoryRank insert(Integer key) {
			xbean.RoleHistoryRank value = new xbean.RoleHistoryRank();
			return add(key, value) ? value : null;
		}

		xbean.RoleHistoryRank update(Integer key) {
			return get(key, true);
		}

		xbean.RoleHistoryRank select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Activity_history_rank activity_history_rank = new Activity_history_rank();

	class Purchase_order_table extends limax.zdb.TTable<String, xbean.Purchase_order_item> {
		@Override
		public String getName() {
			return "purchase_order_table";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.Purchase_order_item value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = _os_.unmarshal_String();
			return key;
		}

		@Override
		protected xbean.Purchase_order_item unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Purchase_order_item value = new xbean.Purchase_order_item();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.Purchase_order_item newValue() {
			xbean.Purchase_order_item value = new xbean.Purchase_order_item();
			return value;
		}

		xbean.Purchase_order_item insert(String key) {
			xbean.Purchase_order_item value = new xbean.Purchase_order_item();
			return add(key, value) ? value : null;
		}

		xbean.Purchase_order_item update(String key) {
			return get(key, true);
		}

		xbean.Purchase_order_item select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Purchase_order_table purchase_order_table = new Purchase_order_table();

	class Role_records extends limax.zdb.TTable<Integer, xbean.RoleRecord> {
		@Override
		public String getName() {
			return "role_records";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.RoleRecord value) {
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
		protected xbean.RoleRecord unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleRecord value = new xbean.RoleRecord();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.RoleRecord newValue() {
			xbean.RoleRecord value = new xbean.RoleRecord();
			return value;
		}

		xbean.RoleRecord insert(Integer key) {
			xbean.RoleRecord value = new xbean.RoleRecord();
			return add(key, value) ? value : null;
		}

		xbean.RoleRecord update(Integer key) {
			return get(key, true);
		}

		xbean.RoleRecord select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Role_records role_records = new Role_records();

	class String_tables extends limax.zdb.TTable<Integer, String> {
		@Override
		public String getName() {
			return "string_tables";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(String value) {
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
		protected String unmarshalValue(OctetsStream _os_) throws MarshalException {
			String value = _os_.unmarshal_String();
			return value;
		}

		@Override
		protected String newValue() {
			String value = "";
			return value;
		}

		String insert(Integer key, String value) {
			return add(key, value) ? value : null;
		}

		String update(Integer key) {
			return get(key, true);
		}

		String select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	String_tables string_tables = new String_tables();

	class Mobile_id_table extends limax.zdb.TTable<String, xbean.MobileIdBean> {
		@Override
		public String getName() {
			return "mobile_id_table";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.MobileIdBean value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = _os_.unmarshal_String();
			return key;
		}

		@Override
		protected xbean.MobileIdBean unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.MobileIdBean value = new xbean.MobileIdBean();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.MobileIdBean newValue() {
			xbean.MobileIdBean value = new xbean.MobileIdBean();
			return value;
		}

		xbean.MobileIdBean insert(String key) {
			xbean.MobileIdBean value = new xbean.MobileIdBean();
			return add(key, value) ? value : null;
		}

		xbean.MobileIdBean update(String key) {
			return get(key, true);
		}

		xbean.MobileIdBean select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Mobile_id_table mobile_id_table = new Mobile_id_table();

	class Id_mobile_table extends limax.zdb.TTable<Integer, xbean.IdMobileBean> {
		@Override
		public String getName() {
			return "id_mobile_table";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.IdMobileBean value) {
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
		protected xbean.IdMobileBean unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.IdMobileBean value = new xbean.IdMobileBean();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.IdMobileBean newValue() {
			xbean.IdMobileBean value = new xbean.IdMobileBean();
			return value;
		}

		xbean.IdMobileBean insert(Integer key) {
			xbean.IdMobileBean value = new xbean.IdMobileBean();
			return add(key, value) ? value : null;
		}

		xbean.IdMobileBean update(Integer key) {
			return get(key, true);
		}

		xbean.IdMobileBean select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Id_mobile_table id_mobile_table = new Id_mobile_table();

	class Diamond_mobilebind_table extends limax.zdb.TTable<String, xbean.DiamondMobileBindBean> {
		@Override
		public String getName() {
			return "diamond_mobilebind_table";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.DiamondMobileBindBean value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = _os_.unmarshal_String();
			return key;
		}

		@Override
		protected xbean.DiamondMobileBindBean unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.DiamondMobileBindBean value = new xbean.DiamondMobileBindBean();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.DiamondMobileBindBean newValue() {
			xbean.DiamondMobileBindBean value = new xbean.DiamondMobileBindBean();
			return value;
		}

		xbean.DiamondMobileBindBean insert(String key) {
			xbean.DiamondMobileBindBean value = new xbean.DiamondMobileBindBean();
			return add(key, value) ? value : null;
		}

		xbean.DiamondMobileBindBean update(String key) {
			return get(key, true);
		}

		xbean.DiamondMobileBindBean select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Diamond_mobilebind_table diamond_mobilebind_table = new Diamond_mobilebind_table();

	class Promoter_info extends limax.zdb.TTable<String, xbean.PromoterInfo> {
		@Override
		public String getName() {
			return "promoter_info";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.PromoterInfo value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = _os_.unmarshal_String();
			return key;
		}

		@Override
		protected xbean.PromoterInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PromoterInfo value = new xbean.PromoterInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.PromoterInfo newValue() {
			xbean.PromoterInfo value = new xbean.PromoterInfo();
			return value;
		}

		xbean.PromoterInfo insert(String key) {
			xbean.PromoterInfo value = new xbean.PromoterInfo();
			return add(key, value) ? value : null;
		}

		xbean.PromoterInfo update(String key) {
			return get(key, true);
		}

		xbean.PromoterInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Promoter_info promoter_info = new Promoter_info();

	class Receive_reward_info extends limax.zdb.TTable<Long, xbean.ReceiveRewardInfo> {
		@Override
		public String getName() {
			return "receive_reward_info";
		}

		@Override
		protected OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.ReceiveRewardInfo value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = _os_.unmarshal_long();
			return key;
		}

		@Override
		protected xbean.ReceiveRewardInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ReceiveRewardInfo value = new xbean.ReceiveRewardInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.ReceiveRewardInfo newValue() {
			xbean.ReceiveRewardInfo value = new xbean.ReceiveRewardInfo();
			return value;
		}

		xbean.ReceiveRewardInfo insert(Long key) {
			xbean.ReceiveRewardInfo value = new xbean.ReceiveRewardInfo();
			return add(key, value) ? value : null;
		}

		Long newKey() {
			return nextKey();
		}

		limax.util.Pair<Long, xbean.ReceiveRewardInfo> insert() {
			Long next = nextKey();
			return new limax.util.Pair<Long, xbean.ReceiveRewardInfo>(next, insert(next));
		}

		xbean.ReceiveRewardInfo update(Long key) {
			return get(key, true);
		}

		xbean.ReceiveRewardInfo select(Long key) {
			return get(key, false);
		}

		boolean delete(Long key) {
			return remove(key);
		}

	};

	Receive_reward_info receive_reward_info = new Receive_reward_info();

	class Invitee_info extends limax.zdb.TTable<String, xbean.InviteeInfo> {
		@Override
		public String getName() {
			return "invitee_info";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.InviteeInfo value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = _os_.unmarshal_String();
			return key;
		}

		@Override
		protected xbean.InviteeInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.InviteeInfo value = new xbean.InviteeInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.InviteeInfo newValue() {
			xbean.InviteeInfo value = new xbean.InviteeInfo();
			return value;
		}

		xbean.InviteeInfo insert(String key) {
			xbean.InviteeInfo value = new xbean.InviteeInfo();
			return add(key, value) ? value : null;
		}

		xbean.InviteeInfo update(String key) {
			return get(key, true);
		}

		xbean.InviteeInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Invitee_info invitee_info = new Invitee_info();

	class Unionid_2_roleid extends limax.zdb.TTable<String, Integer> {
		@Override
		public String getName() {
			return "unionid_2_roleid";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(Integer value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		protected String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = _os_.unmarshal_String();
			return key;
		}

		@Override
		protected Integer unmarshalValue(OctetsStream _os_) throws MarshalException {
			int value = _os_.unmarshal_int();
			return value;
		}

		@Override
		protected Integer newValue() {
			int value = 0;
			return value;
		}

		Integer insert(String key, Integer value) {
			return add(key, value) ? value : null;
		}

		Integer update(String key) {
			return get(key, true);
		}

		Integer select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Unionid_2_roleid unionid_2_roleid = new Unionid_2_roleid();

	class Pop_up_config extends limax.zdb.TTable<Integer, xbean.PopUpMessageInfo> {
		@Override
		public String getName() {
			return "pop_up_config";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.PopUpMessageInfo value) {
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
		protected xbean.PopUpMessageInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PopUpMessageInfo value = new xbean.PopUpMessageInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.PopUpMessageInfo newValue() {
			xbean.PopUpMessageInfo value = new xbean.PopUpMessageInfo();
			return value;
		}

		xbean.PopUpMessageInfo insert(Integer key) {
			xbean.PopUpMessageInfo value = new xbean.PopUpMessageInfo();
			return add(key, value) ? value : null;
		}

		xbean.PopUpMessageInfo update(Integer key) {
			return get(key, true);
		}

		xbean.PopUpMessageInfo select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Pop_up_config pop_up_config = new Pop_up_config();

	class Id_agent_table extends limax.zdb.TTable<Integer, xbean.AgentSatusBean> {
		@Override
		public String getName() {
			return "id_agent_table";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AgentSatusBean value) {
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
		protected xbean.AgentSatusBean unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AgentSatusBean value = new xbean.AgentSatusBean();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AgentSatusBean newValue() {
			xbean.AgentSatusBean value = new xbean.AgentSatusBean();
			return value;
		}

		xbean.AgentSatusBean insert(Integer key) {
			xbean.AgentSatusBean value = new xbean.AgentSatusBean();
			return add(key, value) ? value : null;
		}

		xbean.AgentSatusBean update(Integer key) {
			return get(key, true);
		}

		xbean.AgentSatusBean select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Id_agent_table id_agent_table = new Id_agent_table();

	class Authentiction_info extends limax.zdb.TTable<Integer, xbean.AuthentictionInfo> {
		@Override
		public String getName() {
			return "authentiction_info";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AuthentictionInfo value) {
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
		protected xbean.AuthentictionInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AuthentictionInfo value = new xbean.AuthentictionInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AuthentictionInfo newValue() {
			xbean.AuthentictionInfo value = new xbean.AuthentictionInfo();
			return value;
		}

		xbean.AuthentictionInfo insert(Integer key) {
			xbean.AuthentictionInfo value = new xbean.AuthentictionInfo();
			return add(key, value) ? value : null;
		}

		xbean.AuthentictionInfo update(Integer key) {
			return get(key, true);
		}

		xbean.AuthentictionInfo select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Authentiction_info authentiction_info = new Authentiction_info();


}
