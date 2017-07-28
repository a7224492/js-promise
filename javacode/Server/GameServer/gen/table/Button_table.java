package table;

public class Button_table {
	private Button_table() {
	}

	public static xbean.ButtonTableMap insert(Integer key) {
		return _Tables_.instance.button_table.insert(key);
	}

	public static xbean.ButtonTableMap update(Integer key) {
		return _Tables_.instance.button_table.update(key);
	}

	public static xbean.ButtonTableMap select(Integer key) {
		return _Tables_.instance.button_table.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.button_table.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.ButtonTableMap> get() {
		return _Tables_.instance.button_table;
	}

}
