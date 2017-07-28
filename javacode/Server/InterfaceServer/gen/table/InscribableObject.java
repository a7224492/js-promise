package table;

public class InscribableObject {
	private InscribableObject() {
	}

	public static String insert(Integer key, String value) {
		return _Tables_.instance.InscribableObject.insert(key, value);
	}

	public static String update(Integer key) {
		return _Tables_.instance.InscribableObject.update(key);
	}

	public static String select(Integer key) {
		return _Tables_.instance.InscribableObject.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.InscribableObject.delete(key);
	}

	public static limax.zdb.TTable<Integer, String> get() {
		return _Tables_.instance.InscribableObject;
	}

}
