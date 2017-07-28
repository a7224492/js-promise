package table;

import limax.xmlgen.Cbean;
import limax.xmlgen.Xbean;
import limax.xmlgen.Table;
import limax.xmlgen.Procedure;
import limax.xmlgen.Zdb;
import limax.xmlgen.Variable;

final class _Meta_ {
	private _Meta_(){}

	public static Zdb create() {
		Zdb.Builder _b_ = new Zdb.Builder(new limax.xmlgen.Naming.Root());
		_b_.zdbVerify(true);
		_b_.autoKeyInitValue(0).autoKeyStep(4096);
		_b_.corePoolSize(30);
		_b_.procPoolSize(10);
		_b_.schedPoolSize(5);
		_b_.checkpointPeriod(60000);
		_b_.deadlockDetectPeriod(1000);
		_b_.snapshotFatalTime(200);
		_b_.edbCacheSize(65536);
		_b_.edbLoggerPages(16384);
		Zdb _meta_ = _b_.build();

		new Procedure.Builder(_meta_).retryTimes(3).retryDelay(100).retrySerial(false);

		Cbean cGlobalRoomId = new Cbean(_meta_, "GlobalRoomId");
		new Variable.Builder(cGlobalRoomId,"createTime", "long");
		new Variable.Builder(cGlobalRoomId,"roomId", "int");


		Xbean xRoleInfo = new Xbean(_meta_, "RoleInfo");
		new Variable.Builder(xRoleInfo,"historyRooms", "vector").value("GlobalRoomId");

		Xbean xRoundRecord = new Xbean(_meta_, "RoundRecord");
		new Variable.Builder(xRoundRecord,"bytes", "vector").value("byte");
		new Variable.Builder(xRoundRecord,"playbackDatas", "vector").value("byte");

		Xbean xRoomHistory = new Xbean(_meta_, "RoomHistory");
		new Variable.Builder(xRoomHistory,"roomId", "int");
		new Variable.Builder(xRoomHistory,"createTime", "long");
		new Variable.Builder(xRoomHistory,"roundType", "int");
		new Variable.Builder(xRoomHistory,"roundCount", "int");
		new Variable.Builder(xRoomHistory,"playerMaxCardCount", "int");
		new Variable.Builder(xRoomHistory,"gameplays", "vector").value("int");
		new Variable.Builder(xRoomHistory,"playerInfo", "map").key("int").value("RoomHistoryPlayerInfo");
		new Variable.Builder(xRoomHistory,"roundRecord", "vector").value("RoundRecord");
		new Variable.Builder(xRoomHistory,"enableMutilHu", "boolean");

		Xbean xRoomHistoryPlayerInfo = new Xbean(_meta_, "RoomHistoryPlayerInfo");
		new Variable.Builder(xRoomHistoryPlayerInfo,"roleId", "int");
		new Variable.Builder(xRoomHistoryPlayerInfo,"position", "int");
		new Variable.Builder(xRoomHistoryPlayerInfo,"nickname", "string");
		new Variable.Builder(xRoomHistoryPlayerInfo,"headImgUrl", "string");
		new Variable.Builder(xRoomHistoryPlayerInfo,"sex", "int");
		new Variable.Builder(xRoomHistoryPlayerInfo,"totalPoint", "int");


		new Table.Builder(_meta_, "role_history_rooms", "int", "RoleInfo");
		new Table.Builder(_meta_, "room_history", "GlobalRoomId", "RoomHistory");

		return _meta_;
	}
}
