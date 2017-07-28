package table;

public class Sub_card_table {
	private Sub_card_table() {
	}

	public static xbean.PlayerSubCard insert(Integer key) {
		return _Tables_.instance.sub_card_table.insert(key);
	}

	public static xbean.PlayerSubCard update(Integer key) {
		return _Tables_.instance.sub_card_table.update(key);
	}

	public static xbean.PlayerSubCard select(Integer key) {
		return _Tables_.instance.sub_card_table.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.sub_card_table.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.PlayerSubCard> get() {
		return _Tables_.instance.sub_card_table;
	}

}
