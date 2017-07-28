package table;

public class Global_accountid {
	private Global_accountid() {
	}

	public static xbean.AccountIDSeed insert(Integer key) {
		return _Tables_.instance.global_accountid.insert(key);
	}

	public static xbean.AccountIDSeed update(Integer key) {
		return _Tables_.instance.global_accountid.update(key);
	}

	public static xbean.AccountIDSeed select(Integer key) {
		return _Tables_.instance.global_accountid.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.global_accountid.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.AccountIDSeed> get() {
		return _Tables_.instance.global_accountid;
	}

}
