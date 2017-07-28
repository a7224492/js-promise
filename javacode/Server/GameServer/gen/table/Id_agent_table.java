package table;

public class Id_agent_table {
	private Id_agent_table() {
	}

	public static xbean.AgentSatusBean insert(Integer key) {
		return _Tables_.instance.id_agent_table.insert(key);
	}

	public static xbean.AgentSatusBean update(Integer key) {
		return _Tables_.instance.id_agent_table.update(key);
	}

	public static xbean.AgentSatusBean select(Integer key) {
		return _Tables_.instance.id_agent_table.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.id_agent_table.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.AgentSatusBean> get() {
		return _Tables_.instance.id_agent_table;
	}

}
