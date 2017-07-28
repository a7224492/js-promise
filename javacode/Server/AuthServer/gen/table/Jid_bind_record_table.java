package table;

public class Jid_bind_record_table {
	private Jid_bind_record_table() {
	}

	public static xbean.RecordList insert(Integer key) {
		return _Tables_.instance.jid_bind_record_table.insert(key);
	}

	public static xbean.RecordList update(Integer key) {
		return _Tables_.instance.jid_bind_record_table.update(key);
	}

	public static xbean.RecordList select(Integer key) {
		return _Tables_.instance.jid_bind_record_table.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.jid_bind_record_table.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.RecordList> get() {
		return _Tables_.instance.jid_bind_record_table;
	}

}
