package table;

public class Add_players_card_detail_table {
	private Add_players_card_detail_table() {
	}

	public static xbean.AddPlayersCardDetail insert(cbean.AddPlayersCardDetailKey key) {
		return _Tables_.instance.add_players_card_detail_table.insert(key);
	}

	public static xbean.AddPlayersCardDetail update(cbean.AddPlayersCardDetailKey key) {
		return _Tables_.instance.add_players_card_detail_table.update(key);
	}

	public static xbean.AddPlayersCardDetail select(cbean.AddPlayersCardDetailKey key) {
		return _Tables_.instance.add_players_card_detail_table.select(key);
	}

	public static boolean delete(cbean.AddPlayersCardDetailKey key) {
		return _Tables_.instance.add_players_card_detail_table.delete(key);
	}

	public static limax.zdb.TTable<cbean.AddPlayersCardDetailKey, xbean.AddPlayersCardDetail> get() {
		return _Tables_.instance.add_players_card_detail_table;
	}

}
