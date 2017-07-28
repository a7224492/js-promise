package table;

public class Invitee_info {
	private Invitee_info() {
	}

	public static xbean.InviteeInfo insert(String key) {
		return _Tables_.instance.invitee_info.insert(key);
	}

	public static xbean.InviteeInfo update(String key) {
		return _Tables_.instance.invitee_info.update(key);
	}

	public static xbean.InviteeInfo select(String key) {
		return _Tables_.instance.invitee_info.select(key);
	}

	public static boolean delete(String key) {
		return _Tables_.instance.invitee_info.delete(key);
	}

	public static limax.zdb.TTable<String, xbean.InviteeInfo> get() {
		return _Tables_.instance.invitee_info;
	}

}
