package table;

public class Add_card_table {
	private Add_card_table() {
	}

	public static xbean.PlayerAddCard insert(Integer key) {
		return _Tables_.instance.add_card_table.insert(key);
	}

	public static xbean.PlayerAddCard update(Integer key) {
		return _Tables_.instance.add_card_table.update(key);
	}

	public static xbean.PlayerAddCard select(Integer key) {
		return _Tables_.instance.add_card_table.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.add_card_table.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.PlayerAddCard> get() {
		return _Tables_.instance.add_card_table;
	}

}
