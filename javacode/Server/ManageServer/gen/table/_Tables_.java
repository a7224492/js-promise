package table;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

final class _Tables_ {
	volatile static _Tables_ instance;

	private _Tables_ () {
		instance = this;
	}

	class Server_info extends limax.zdb.TTable<Integer, xbean.ServerInfo> {
		@Override
		public String getName() {
			return "server_info";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected OctetsStream marshalValue(xbean.ServerInfo value) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		protected xbean.ServerInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		protected xbean.ServerInfo newValue() {
			xbean.ServerInfo value = new xbean.ServerInfo();
			return value;
		}

		xbean.ServerInfo insert(Integer key) {
			xbean.ServerInfo value = new xbean.ServerInfo();
			return add(key, value) ? value : null;
		}

		xbean.ServerInfo update(Integer key) {
			return get(key, true);
		}

		xbean.ServerInfo select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Server_info server_info = new Server_info();

	class Server_id extends limax.zdb.TTable<Integer, xbean.IntValue> {
		@Override
		public String getName() {
			return "server_id";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		protected OctetsStream marshalValue(xbean.IntValue value) {
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
		protected xbean.IntValue unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.IntValue value = new xbean.IntValue();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		protected xbean.IntValue newValue() {
			xbean.IntValue value = new xbean.IntValue();
			return value;
		}

		xbean.IntValue insert(Integer key) {
			xbean.IntValue value = new xbean.IntValue();
			return add(key, value) ? value : null;
		}

		xbean.IntValue update(Integer key) {
			return get(key, true);
		}

		xbean.IntValue select(Integer key) {
			return get(key, false);
		}

		boolean delete(Integer key) {
			return remove(key);
		}

	};

	Server_id server_id = new Server_id();

	class Port_table extends limax.zdb.TTable<String, xbean.PortBase> {
		@Override
		public String getName() {
			return "port_table";
		}

		@Override
		protected OctetsStream marshalKey(String key) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected OctetsStream marshalValue(xbean.PortBase value) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected String unmarshalKey(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		protected xbean.PortBase unmarshalValue(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		protected xbean.PortBase newValue() {
			xbean.PortBase value = new xbean.PortBase();
			return value;
		}

		xbean.PortBase insert(String key) {
			xbean.PortBase value = new xbean.PortBase();
			return add(key, value) ? value : null;
		}

		xbean.PortBase update(String key) {
			return get(key, true);
		}

		xbean.PortBase select(String key) {
			return get(key, false);
		}

		boolean delete(String key) {
			return remove(key);
		}

	};

	Port_table port_table = new Port_table();


}
