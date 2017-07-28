package table;

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


		Xbean xClubRoleBaseInfo = new Xbean(_meta_, "ClubRoleBaseInfo");
		new Variable.Builder(xClubRoleBaseInfo,"roleId", "int");
		new Variable.Builder(xClubRoleBaseInfo,"name", "string");
		new Variable.Builder(xClubRoleBaseInfo,"title", "int");

		Xbean xRoleClubs = new Xbean(_meta_, "RoleClubs");
		new Variable.Builder(xRoleClubs,"clubs", "vector").value("RoleClubInfo");
		new Variable.Builder(xRoleClubs,"app_key", "string");
		new Variable.Builder(xRoleClubs,"version", "string");
		new Variable.Builder(xRoleClubs,"channel", "string");

		Xbean xClubInfo = new Xbean(_meta_, "ClubInfo");
		new Variable.Builder(xClubInfo,"clubId", "int");
		new Variable.Builder(xClubInfo,"clubName", "string");
		new Variable.Builder(xClubInfo,"notice", "string");
		new Variable.Builder(xClubInfo,"noticeTime", "long");
		new Variable.Builder(xClubInfo,"createTimestamp", "long");
		new Variable.Builder(xClubInfo,"creator", "ClubRoleBaseInfo");
		new Variable.Builder(xClubInfo,"manager", "ClubRoleBaseInfo");
		new Variable.Builder(xClubInfo,"level", "int");
		new Variable.Builder(xClubInfo,"gameCount", "long");
		new Variable.Builder(xClubInfo,"todayGameCount", "long");
		new Variable.Builder(xClubInfo,"todayClearTime", "long");
		new Variable.Builder(xClubInfo,"roomCost", "RoomCost");
		new Variable.Builder(xClubInfo,"members", "vector").value("MemberInfo");
		new Variable.Builder(xClubInfo,"applicants", "vector").value("ApplicantInfo");
		new Variable.Builder(xClubInfo,"status", "int");
		new Variable.Builder(xClubInfo,"agentId", "int");
		new Variable.Builder(xClubInfo,"memberCount", "int");

		Xbean xRoomCost = new Xbean(_meta_, "RoomCost");
		new Variable.Builder(xRoomCost,"cost", "int");
		new Variable.Builder(xRoomCost,"payType", "int");

		Xbean xClubRoomInfo = new Xbean(_meta_, "ClubRoomInfo");
		new Variable.Builder(xClubRoomInfo,"roomId", "int");
		new Variable.Builder(xClubRoomInfo,"creator", "int");
		new Variable.Builder(xClubRoomInfo,"player", "vector").value("ClubRoleBaseInfo");
		new Variable.Builder(xClubRoomInfo,"maxPlayer", "int");
		new Variable.Builder(xClubRoomInfo,"gameplays", "vector").value("int");
		new Variable.Builder(xClubRoomInfo,"clubId", "int");
		new Variable.Builder(xClubRoomInfo,"enableSubCard", "boolean");
		new Variable.Builder(xClubRoomInfo,"roomCostSnap", "RoomCost");
		new Variable.Builder(xClubRoomInfo,"roundCount", "int");
		new Variable.Builder(xClubRoomInfo,"battleId", "int");

		Xbean xClubManager = new Xbean(_meta_, "ClubManager");
		new Variable.Builder(xClubManager,"firstClubId", "int");

		Xbean xApplicantInfo = new Xbean(_meta_, "ApplicantInfo");
		new Variable.Builder(xApplicantInfo,"role", "ClubRoleBaseInfo");
		new Variable.Builder(xApplicantInfo,"inviter", "ClubRoleBaseInfo");
		new Variable.Builder(xApplicantInfo,"applyTimestamp", "long");
		new Variable.Builder(xApplicantInfo,"gameCount", "int");
		new Variable.Builder(xApplicantInfo,"inviterIcon", "string");

		Xbean xClubIdSeed = new Xbean(_meta_, "ClubIdSeed");
		new Variable.Builder(xClubIdSeed,"seed", "int");

		Xbean xMemberInfo = new Xbean(_meta_, "MemberInfo");
		new Variable.Builder(xMemberInfo,"role", "ClubRoleBaseInfo");
		new Variable.Builder(xMemberInfo,"cardCount", "int");
		new Variable.Builder(xMemberInfo,"inviter", "ClubRoleBaseInfo");
		new Variable.Builder(xMemberInfo,"joinTimestamp", "long");
		new Variable.Builder(xMemberInfo,"status", "int");
		new Variable.Builder(xMemberInfo,"totalGameCount", "int");
		new Variable.Builder(xMemberInfo,"todayGameCount", "int");

		Xbean xClubAgent = new Xbean(_meta_, "ClubAgent");
		new Variable.Builder(xClubAgent,"clubs", "vector").value("int");

		Xbean xRoleClubInfo = new Xbean(_meta_, "RoleClubInfo");
		new Variable.Builder(xRoleClubInfo,"clubId", "int");
		new Variable.Builder(xRoleClubInfo,"invitationCode", "string");

		Xbean xClubRooms = new Xbean(_meta_, "ClubRooms");
		new Variable.Builder(xClubRooms,"rooms", "map").key("int").value("ClubRoomInfo");


		new Table.Builder(_meta_, "clubs", "int", "ClubInfo");
		new Table.Builder(_meta_, "club_room_info", "int", "ClubRooms").memory(true).cacheCap("100000");
		new Table.Builder(_meta_, "club_id_seed", "int", "ClubIdSeed");
		new Table.Builder(_meta_, "role_clubs", "int", "RoleClubs");
		new Table.Builder(_meta_, "club_manager", "int", "ClubManager");
		new Table.Builder(_meta_, "club_agent", "int", "ClubAgent");

		return _meta_;
	}
}
