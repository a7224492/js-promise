package table;

public class Purchase_order_table {
	private Purchase_order_table() {
	}

	public static xbean.Purchase_order_item insert(String key) {
		return _Tables_.instance.purchase_order_table.insert(key);
	}

	public static xbean.Purchase_order_item update(String key) {
		return _Tables_.instance.purchase_order_table.update(key);
	}

	public static xbean.Purchase_order_item select(String key) {
		return _Tables_.instance.purchase_order_table.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.purchase_order_table.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.Purchase_order_item> get() {
		return _Tables_.instance.purchase_order_table;
	}

}
