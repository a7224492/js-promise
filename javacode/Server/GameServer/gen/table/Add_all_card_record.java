package table;

public class Add_all_card_record {
	private Add_all_card_record() {
	}

	public static xbean.AddAllCardRecord insert(Long key) {
		return _Tables_.instance.add_all_card_record.insert(key);
	}

	public static Long newKey() {
		return _Tables_.instance.add_all_card_record.newKey();
	}

	public static limax.util.Pair<Long, xbean.AddAllCardRecord> insert() {
		return _Tables_.instance.add_all_card_record.insert();
	}

	public static xbean.AddAllCardRecord update(Long key) {
		return _Tables_.instance.add_all_card_record.update(key);
	}

	public static xbean.AddAllCardRecord select(Long key) {
		return _Tables_.instance.add_all_card_record.select(key);
	}

	public static boolean delete(Long key) {
		return _Tables_.instance.add_all_card_record.delete(key);
	}

	public static limax.zdb.TTable<Long, xbean.AddAllCardRecord> get() {
		return _Tables_.instance.add_all_card_record;
	}

}
