package table;

public class Add_players_card_record_table {
	private Add_players_card_record_table() {
	}

	public static xbean.AddPlayersCardRecord insert(Integer key) {
		return _Tables_.instance.add_players_card_record_table.insert(key);
	}

	public static xbean.AddPlayersCardRecord update(Integer key) {
		return _Tables_.instance.add_players_card_record_table.update(key);
	}

	public static xbean.AddPlayersCardRecord select(Integer key) {
		return _Tables_.instance.add_players_card_record_table.select(key);
	}

	public static boolean delete(Integer key) {
		return _Tables_.instance.add_players_card_record_table.delete(key);
	}

	public static limax.zdb.TTable<Integer, xbean.AddPlayersCardRecord> get() {
		return _Tables_.instance.add_players_card_record_table;
	}

}
