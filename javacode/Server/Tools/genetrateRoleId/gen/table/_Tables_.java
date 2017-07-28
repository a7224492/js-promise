package table;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

final class _Tables_ {
	volatile static _Tables_ instance;

	private _Tables_ () {
		instance = this;
	}

	class Account_table extends limax.zdb.TTable<String, xbean.AccountInfo> {
		@Override
		public String getName() {
			return "account_table";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountInfo value) {
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
		protected xbean.AccountInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountInfo value = new xbean.AccountInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountInfo newValue() {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return value;
		}

		xbean.AccountInfo insert(String key) {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return add(key, value) ? value : null;
		}

		xbean.AccountInfo update(String key) {
			return get(key, true);
		}

		xbean.AccountInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Account_table account_table = new Account_table();

	class Account_table_1 extends limax.zdb.TTable<String, xbean.AccountInfo> {
		@Override
		public String getName() {
			return "account_table_1";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountInfo value) {
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
		protected xbean.AccountInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountInfo value = new xbean.AccountInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountInfo newValue() {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return value;
		}

		xbean.AccountInfo insert(String key) {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return add(key, value) ? value : null;
		}

		xbean.AccountInfo update(String key) {
			return get(key, true);
		}

		xbean.AccountInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Account_table_1 account_table_1 = new Account_table_1();

	class Account_table_2 extends limax.zdb.TTable<String, xbean.AccountInfo> {
		@Override
		public String getName() {
			return "account_table_2";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountInfo value) {
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
		protected xbean.AccountInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountInfo value = new xbean.AccountInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountInfo newValue() {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return value;
		}

		xbean.AccountInfo insert(String key) {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return add(key, value) ? value : null;
		}

		xbean.AccountInfo update(String key) {
			return get(key, true);
		}

		xbean.AccountInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Account_table_2 account_table_2 = new Account_table_2();

	class Account_table_3 extends limax.zdb.TTable<String, xbean.AccountInfo> {
		@Override
		public String getName() {
			return "account_table_3";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountInfo value) {
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
		protected xbean.AccountInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountInfo value = new xbean.AccountInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountInfo newValue() {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return value;
		}

		xbean.AccountInfo insert(String key) {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return add(key, value) ? value : null;
		}

		xbean.AccountInfo update(String key) {
			return get(key, true);
		}

		xbean.AccountInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Account_table_3 account_table_3 = new Account_table_3();

	class Account_table_4 extends limax.zdb.TTable<String, xbean.AccountInfo> {
		@Override
		public String getName() {
			return "account_table_4";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountInfo value) {
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
		protected xbean.AccountInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountInfo value = new xbean.AccountInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountInfo newValue() {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return value;
		}

		xbean.AccountInfo insert(String key) {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return add(key, value) ? value : null;
		}

		xbean.AccountInfo update(String key) {
			return get(key, true);
		}

		xbean.AccountInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Account_table_4 account_table_4 = new Account_table_4();

	class Account_table_5 extends limax.zdb.TTable<String, xbean.AccountInfo> {
		@Override
		public String getName() {
			return "account_table_5";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountInfo value) {
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
		protected xbean.AccountInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountInfo value = new xbean.AccountInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountInfo newValue() {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return value;
		}

		xbean.AccountInfo insert(String key) {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return add(key, value) ? value : null;
		}

		xbean.AccountInfo update(String key) {
			return get(key, true);
		}

		xbean.AccountInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Account_table_5 account_table_5 = new Account_table_5();

	class Account_table_6 extends limax.zdb.TTable<String, xbean.AccountInfo> {
		@Override
		public String getName() {
			return "account_table_6";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountInfo value) {
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
		protected xbean.AccountInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountInfo value = new xbean.AccountInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountInfo newValue() {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return value;
		}

		xbean.AccountInfo insert(String key) {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return add(key, value) ? value : null;
		}

		xbean.AccountInfo update(String key) {
			return get(key, true);
		}

		xbean.AccountInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Account_table_6 account_table_6 = new Account_table_6();

	class Account_table_7 extends limax.zdb.TTable<String, xbean.AccountInfo> {
		@Override
		public String getName() {
			return "account_table_7";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountInfo value) {
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
		protected xbean.AccountInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountInfo value = new xbean.AccountInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountInfo newValue() {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return value;
		}

		xbean.AccountInfo insert(String key) {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return add(key, value) ? value : null;
		}

		xbean.AccountInfo update(String key) {
			return get(key, true);
		}

		xbean.AccountInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Account_table_7 account_table_7 = new Account_table_7();

	class Account_table_8 extends limax.zdb.TTable<String, xbean.AccountInfo> {
		@Override
		public String getName() {
			return "account_table_8";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountInfo value) {
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
		protected xbean.AccountInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountInfo value = new xbean.AccountInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountInfo newValue() {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return value;
		}

		xbean.AccountInfo insert(String key) {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return add(key, value) ? value : null;
		}

		xbean.AccountInfo update(String key) {
			return get(key, true);
		}

		xbean.AccountInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Account_table_8 account_table_8 = new Account_table_8();

	class Account_table_9 extends limax.zdb.TTable<String, xbean.AccountInfo> {
		@Override
		public String getName() {
			return "account_table_9";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountInfo value) {
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
		protected xbean.AccountInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountInfo value = new xbean.AccountInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountInfo newValue() {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return value;
		}

		xbean.AccountInfo insert(String key) {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return add(key, value) ? value : null;
		}

		xbean.AccountInfo update(String key) {
			return get(key, true);
		}

		xbean.AccountInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Account_table_9 account_table_9 = new Account_table_9();

	class Account_table_10 extends limax.zdb.TTable<String, xbean.AccountInfo> {
		@Override
		public String getName() {
			return "account_table_10";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountInfo value) {
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
		protected xbean.AccountInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountInfo value = new xbean.AccountInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountInfo newValue() {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return value;
		}

		xbean.AccountInfo insert(String key) {
			xbean.AccountInfo value = new xbean.AccountInfo();
			return add(key, value) ? value : null;
		}

		xbean.AccountInfo update(String key) {
			return get(key, true);
		}

		xbean.AccountInfo select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Account_table_10 account_table_10 = new Account_table_10();

	class User_account extends limax.zdb.TTable<Integer, String> {
		@Override
		public String getName() {
			return "user_account";
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

	User_account user_account = new User_account();

	class Global_accountid extends limax.zdb.TTable<Integer, xbean.AccountIDSeed> {
		@Override
		public String getName() {
			return "global_accountid";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.AccountIDSeed value) {
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
		protected xbean.AccountIDSeed unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccountIDSeed value = new xbean.AccountIDSeed();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.AccountIDSeed newValue() {
			xbean.AccountIDSeed value = new xbean.AccountIDSeed();
			return value;
		}

		xbean.AccountIDSeed insert(Integer key) {
			xbean.AccountIDSeed value = new xbean.AccountIDSeed();
			return add(key, value) ? value : null;
		}

		xbean.AccountIDSeed update(Integer key) {
			return get(key, true);
		}

		xbean.AccountIDSeed select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Global_accountid global_accountid = new Global_accountid();

	class Unionid_accountid_table extends limax.zdb.TTable<String, xbean.UnionidAccountidBean> {
		@Override
		public String getName() {
			return "unionid_accountid_table";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.UnionidAccountidBean value) {
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
		protected xbean.UnionidAccountidBean unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.UnionidAccountidBean value = new xbean.UnionidAccountidBean();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.UnionidAccountidBean newValue() {
			xbean.UnionidAccountidBean value = new xbean.UnionidAccountidBean();
			return value;
		}

		xbean.UnionidAccountidBean insert(String key) {
			xbean.UnionidAccountidBean value = new xbean.UnionidAccountidBean();
			return add(key, value) ? value : null;
		}

		xbean.UnionidAccountidBean update(String key) {
			return get(key, true);
		}

		xbean.UnionidAccountidBean select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Unionid_accountid_table unionid_accountid_table = new Unionid_accountid_table();

	class Unionid_accountid_table_1 extends limax.zdb.TTable<String, xbean.UnionidAccountidBean> {
		@Override
		public String getName() {
			return "unionid_accountid_table_1";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.UnionidAccountidBean value) {
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
		protected xbean.UnionidAccountidBean unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.UnionidAccountidBean value = new xbean.UnionidAccountidBean();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.UnionidAccountidBean newValue() {
			xbean.UnionidAccountidBean value = new xbean.UnionidAccountidBean();
			return value;
		}

		xbean.UnionidAccountidBean insert(String key) {
			xbean.UnionidAccountidBean value = new xbean.UnionidAccountidBean();
			return add(key, value) ? value : null;
		}

		xbean.UnionidAccountidBean update(String key) {
			return get(key, true);
		}

		xbean.UnionidAccountidBean select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Unionid_accountid_table_1 unionid_accountid_table_1 = new Unionid_accountid_table_1();

	class Deviceid_unionid_table extends limax.zdb.TTable<String, xbean.DeviceidUnionidBean> {
		@Override
		public String getName() {
			return "deviceid_unionid_table";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.DeviceidUnionidBean value) {
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
		protected xbean.DeviceidUnionidBean unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.DeviceidUnionidBean value = new xbean.DeviceidUnionidBean();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.DeviceidUnionidBean newValue() {
			xbean.DeviceidUnionidBean value = new xbean.DeviceidUnionidBean();
			return value;
		}

		xbean.DeviceidUnionidBean insert(String key) {
			xbean.DeviceidUnionidBean value = new xbean.DeviceidUnionidBean();
			return add(key, value) ? value : null;
		}

		xbean.DeviceidUnionidBean update(String key) {
			return get(key, true);
		}

		xbean.DeviceidUnionidBean select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Deviceid_unionid_table deviceid_unionid_table = new Deviceid_unionid_table();

	class Jid_bind_record_table extends limax.zdb.TTable<Integer, xbean.RecordList> {
		@Override
		public String getName() {
			return "jid_bind_record_table";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.RecordList value) {
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
		protected xbean.RecordList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RecordList value = new xbean.RecordList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.RecordList newValue() {
			xbean.RecordList value = new xbean.RecordList();
			return value;
		}

		xbean.RecordList insert(Integer key) {
			xbean.RecordList value = new xbean.RecordList();
			return add(key, value) ? value : null;
		}

		xbean.RecordList update(Integer key) {
			return get(key, true);
		}

		xbean.RecordList select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Jid_bind_record_table jid_bind_record_table = new Jid_bind_record_table();

	class Deviceid_bind_record_table extends limax.zdb.TTable<String, xbean.DeviceIdBindRecordBean> {
		@Override
		public String getName() {
			return "deviceid_bind_record_table";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.DeviceIdBindRecordBean value) {
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
		protected xbean.DeviceIdBindRecordBean unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.DeviceIdBindRecordBean value = new xbean.DeviceIdBindRecordBean();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.DeviceIdBindRecordBean newValue() {
			xbean.DeviceIdBindRecordBean value = new xbean.DeviceIdBindRecordBean();
			return value;
		}

		xbean.DeviceIdBindRecordBean insert(String key) {
			xbean.DeviceIdBindRecordBean value = new xbean.DeviceIdBindRecordBean();
			return add(key, value) ? value : null;
		}

		xbean.DeviceIdBindRecordBean update(String key) {
			return get(key, true);
		}

		xbean.DeviceIdBindRecordBean select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Deviceid_bind_record_table deviceid_bind_record_table = new Deviceid_bind_record_table();

	class Deviceid_accountid_table extends limax.zdb.TTable<String, xbean.DeviceidAccountidBean> {
		@Override
		public String getName() {
			return "deviceid_accountid_table";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.DeviceidAccountidBean value) {
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
		protected xbean.DeviceidAccountidBean unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.DeviceidAccountidBean value = new xbean.DeviceidAccountidBean();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.DeviceidAccountidBean newValue() {
			xbean.DeviceidAccountidBean value = new xbean.DeviceidAccountidBean();
			return value;
		}

		xbean.DeviceidAccountidBean insert(String key) {
			xbean.DeviceidAccountidBean value = new xbean.DeviceidAccountidBean();
			return add(key, value) ? value : null;
		}

		xbean.DeviceidAccountidBean update(String key) {
			return get(key, true);
		}

		xbean.DeviceidAccountidBean select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Deviceid_accountid_table deviceid_accountid_table = new Deviceid_accountid_table();

	class Global_client_version extends limax.zdb.TTable<Integer, xbean.ClientVersion> {
		@Override
		public String getName() {
			return "global_client_version";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.ClientVersion value) {
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
		protected xbean.ClientVersion unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClientVersion value = new xbean.ClientVersion();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.ClientVersion newValue() {
			xbean.ClientVersion value = new xbean.ClientVersion();
			return value;
		}

		xbean.ClientVersion insert(Integer key) {
			xbean.ClientVersion value = new xbean.ClientVersion();
			return add(key, value) ? value : null;
		}

		xbean.ClientVersion update(Integer key) {
			return get(key, true);
		}

		xbean.ClientVersion select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Global_client_version global_client_version = new Global_client_version();

	class Channel_version_table extends limax.zdb.TTable<String, xbean.VersionUpdateBean> {
		@Override
		public String getName() {
			return "channel_version_table";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.VersionUpdateBean value) {
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
		protected xbean.VersionUpdateBean unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.VersionUpdateBean value = new xbean.VersionUpdateBean();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.VersionUpdateBean newValue() {
			xbean.VersionUpdateBean value = new xbean.VersionUpdateBean();
			return value;
		}

		xbean.VersionUpdateBean insert(String key) {
			xbean.VersionUpdateBean value = new xbean.VersionUpdateBean();
			return add(key, value) ? value : null;
		}

		xbean.VersionUpdateBean update(String key) {
			return get(key, true);
		}

		xbean.VersionUpdateBean select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Channel_version_table channel_version_table = new Channel_version_table();


}
