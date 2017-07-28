package table;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

final class _Tables_ {
	volatile static _Tables_ instance;

	private _Tables_ () {
		instance = this;
	}

	class InscribableObject extends limax.zdb.TTable<Integer, String> {
		@Override
		public String getName() {
			return "InscribableObject";
		}

		@Override
		protected OctetsStream marshalKey(Integer key) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected OctetsStream marshalValue(String value) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		protected String unmarshalValue(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
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

	InscribableObject InscribableObject = new InscribableObject();


}
