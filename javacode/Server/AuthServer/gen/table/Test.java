package table;

public class Test {
	private Test() {
	}

	public static Integer insert(Integer key, Integer value) {
		return _Tables_.instance.test.insert(key, value);
	}

	public static Integer update(Integer key) {
		return _Tables_.instance.test.update(key);
	}

	public static Integer select(Integer key) {
		return _Tables_.instance.test.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.test.delete(key);
	}

	public static limax.zdb.TTable<Integer, Integer> get() {
		return _Tables_.instance.test;
	}

}
