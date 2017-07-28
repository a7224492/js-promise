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

		Cbean cRoleClubId = new Cbean(_meta_, "RoleClubId");
		new Variable.Builder(cRoleClubId,"clubId", "int");
		new Variable.Builder(cRoleClubId,"roleId", "int");

		Cbean cLastRewardKey = new Cbean(_meta_, "LastRewardKey");
		new Variable.Builder(cLastRewardKey,"roleId", "int");
		new Variable.Builder(cLastRewardKey,"rewardTime", "long");

		Cbean cAddPlayersCardDetailKey = new Cbean(_meta_, "AddPlayersCardDetailKey");
		new Variable.Builder(cAddPlayersCardDetailKey,"id", "int");
		new Variable.Builder(cAddPlayersCardDetailKey,"roleId", "int");

		Cbean cActivityRewardKey = new Cbean(_meta_, "ActivityRewardKey");
		new Variable.Builder(cActivityRewardKey,"activityId", "int");
		new Variable.Builder(cActivityRewardKey,"rewardId", "int");
		new Variable.Builder(cActivityRewardKey,"rewardDate", "long");

		Cbean cGlobalRoomId = new Cbean(_meta_, "GlobalRoomId");
		new Variable.Builder(cGlobalRoomId,"createTime", "long");
		new Variable.Builder(cGlobalRoomId,"roomId", "int");


		Xbean xAddAllCardRecord = new Xbean(_meta_, "AddAllCardRecord");
		new Variable.Builder(xAddAllCardRecord,"gmAdmin", "string");
		new Variable.Builder(xAddAllCardRecord,"count", "int");
		new Variable.Builder(xAddAllCardRecord,"time", "long");

		Xbean xLimitedCostlessActivity = new Xbean(_meta_, "LimitedCostlessActivity");
		new Variable.Builder(xLimitedCostlessActivity,"startTime", "long");
		new Variable.Builder(xLimitedCostlessActivity,"endTime", "long");
		new Variable.Builder(xLimitedCostlessActivity,"activityName", "string");
		new Variable.Builder(xLimitedCostlessActivity,"roomType", "int");
		new Variable.Builder(xLimitedCostlessActivity,"isCancel", "boolean");
		new Variable.Builder(xLimitedCostlessActivity,"opType", "int");

		Xbean xRuntimeGlobalInfo = new Xbean(_meta_, "RuntimeGlobalInfo");
		new Variable.Builder(xRuntimeGlobalInfo,"battles", "map").key("int").value("RuntimeBattleInfo");
		new Variable.Builder(xRuntimeGlobalInfo,"serverStartupTime", "long");

		Xbean xAddCard = new Xbean(_meta_, "AddCard");
		new Variable.Builder(xAddCard,"agencyId", "int");
		new Variable.Builder(xAddCard,"count", "int");
		new Variable.Builder(xAddCard,"time", "long");

		Xbean xLimitContact = new Xbean(_meta_, "LimitContact");
		new Variable.Builder(xLimitContact,"id", "int");
		new Variable.Builder(xLimitContact,"agencyId", "int");
		new Variable.Builder(xLimitContact,"content", "string");
		new Variable.Builder(xLimitContact,"startTime", "long");
		new Variable.Builder(xLimitContact,"endTime", "long");
		new Variable.Builder(xLimitContact,"sender", "string");

		Xbean xTurntableActivityRewardRecord = new Xbean(_meta_, "TurntableActivityRewardRecord");
		new Variable.Builder(xTurntableActivityRewardRecord,"rewardId", "int");
		new Variable.Builder(xTurntableActivityRewardRecord,"rewardName", "string");
		new Variable.Builder(xTurntableActivityRewardRecord,"rewardDesc", "string");
		new Variable.Builder(xTurntableActivityRewardRecord,"rewardTime", "long");
		new Variable.Builder(xTurntableActivityRewardRecord,"rewardCount", "int");

		Xbean xRoleRecord = new Xbean(_meta_, "RoleRecord");
		new Variable.Builder(xRoleRecord,"role_id", "int");
		new Variable.Builder(xRoleRecord,"combatTimes", "vector").value("CombatInfo");
		new Variable.Builder(xRoleRecord,"agencyId", "int");
		new Variable.Builder(xRoleRecord,"combatTotal", "int");

		Xbean xMobileIdBean = new Xbean(_meta_, "MobileIdBean");
		new Variable.Builder(xMobileIdBean,"accountId", "int");
		new Variable.Builder(xMobileIdBean,"bindTime", "long");
		new Variable.Builder(xMobileIdBean,"status", "string");
		new Variable.Builder(xMobileIdBean,"code", "string");
		new Variable.Builder(xMobileIdBean,"codeTime", "long");

		Xbean xRoomInfo = new Xbean(_meta_, "RoomInfo");
		new Variable.Builder(xRoomInfo,"battleId", "int");
		new Variable.Builder(xRoomInfo,"status", "int");
		new Variable.Builder(xRoomInfo,"roundCount", "int");
		new Variable.Builder(xRoomInfo,"isLca", "boolean");
		new Variable.Builder(xRoomInfo,"clubId", "int");
		new Variable.Builder(xRoomInfo,"payType", "int");
		new Variable.Builder(xRoomInfo,"cost", "int");

		Xbean xPopUpMessageTypes = new Xbean(_meta_, "PopUpMessageTypes");
		new Variable.Builder(xPopUpMessageTypes,"tab", "int");
		new Variable.Builder(xPopUpMessageTypes,"style", "int");
		new Variable.Builder(xPopUpMessageTypes,"content", "vector").value("string");

		Xbean xDiamondMobileBindBean = new Xbean(_meta_, "DiamondMobileBindBean");
		new Variable.Builder(xDiamondMobileBindBean,"diamond", "int");

		Xbean xAuthentictionInfo = new Xbean(_meta_, "AuthentictionInfo");
		new Variable.Builder(xAuthentictionInfo,"realname", "string");
		new Variable.Builder(xAuthentictionInfo,"idnumber", "string");

		Xbean xRuntimeBattleInfo = new Xbean(_meta_, "RuntimeBattleInfo");
		new Variable.Builder(xRuntimeBattleInfo,"totalRoomCount", "int");
		new Variable.Builder(xRuntimeBattleInfo,"positiveRoomCount", "int");
		new Variable.Builder(xRuntimeBattleInfo,"silentRoomCount", "int");

		Xbean xPlayerSubCard = new Xbean(_meta_, "PlayerSubCard");
		new Variable.Builder(xPlayerSubCard,"records", "vector").value("SubCard");

		Xbean xLongValue = new Xbean(_meta_, "LongValue");
		new Variable.Builder(xLongValue,"val", "long");

		Xbean xInviteeInfo = new Xbean(_meta_, "InviteeInfo");
		new Variable.Builder(xInviteeInfo,"roundCount", "int");
		new Variable.Builder(xInviteeInfo,"finished", "int");
		new Variable.Builder(xInviteeInfo,"finishTime", "long");
		new Variable.Builder(xInviteeInfo,"joinTime", "long");
		new Variable.Builder(xInviteeInfo,"promoterUnionId", "string");

		Xbean xMainNotice = new Xbean(_meta_, "MainNotice");
		new Variable.Builder(xMainNotice,"type", "string");
		new Variable.Builder(xMainNotice,"id", "long");
		new Variable.Builder(xMainNotice,"title", "string");
		new Variable.Builder(xMainNotice,"content", "string");
		new Variable.Builder(xMainNotice,"startTime", "long");
		new Variable.Builder(xMainNotice,"endTime", "long");
		new Variable.Builder(xMainNotice,"isCancel", "boolean");

		Xbean xHistoryRank = new Xbean(_meta_, "HistoryRank");
		new Variable.Builder(xHistoryRank,"score", "int");

		Xbean xNormalContact = new Xbean(_meta_, "NormalContact");
		new Variable.Builder(xNormalContact,"id", "int");
		new Variable.Builder(xNormalContact,"agencyId", "int");
		new Variable.Builder(xNormalContact,"content", "string");
		new Variable.Builder(xNormalContact,"sender", "string");

		Xbean xRoleHistoryRank = new Xbean(_meta_, "RoleHistoryRank");
		new Variable.Builder(xRoleHistoryRank,"ranks", "map").key("int").value("ActivityHistoryRank");

		Xbean xForbidRole = new Xbean(_meta_, "ForbidRole");
		new Variable.Builder(xForbidRole,"accountId", "int");
		new Variable.Builder(xForbidRole,"channel", "string");
		new Variable.Builder(xForbidRole,"username", "string");
		new Variable.Builder(xForbidRole,"forbidTime", "long");

		Xbean xRoleInfo = new Xbean(_meta_, "RoleInfo");
		new Variable.Builder(xRoleInfo,"accountId", "int");
		new Variable.Builder(xRoleInfo,"channel", "string");
		new Variable.Builder(xRoleInfo,"username", "string");
		new Variable.Builder(xRoleInfo,"nickname", "string");
		new Variable.Builder(xRoleInfo,"headImgUrl", "string");
		new Variable.Builder(xRoleInfo,"sex", "int");
		new Variable.Builder(xRoleInfo,"points", "int");
		new Variable.Builder(xRoleInfo,"cardCount", "int");
		new Variable.Builder(xRoleInfo,"totalCostCardCount", "int");
		new Variable.Builder(xRoleInfo,"totalGameCount", "int");
		new Variable.Builder(xRoleInfo,"roleCreateTime", "long");
		new Variable.Builder(xRoleInfo,"lastLoginTime", "long");
		new Variable.Builder(xRoleInfo,"historyRooms", "vector").value("GlobalRoomId");
		new Variable.Builder(xRoleInfo,"mergeList", "vector").value("int");
		new Variable.Builder(xRoleInfo,"unionid", "string");

		Xbean xPopUpMessageTimes = new Xbean(_meta_, "PopUpMessageTimes");
		new Variable.Builder(xPopUpMessageTimes,"start", "string");
		new Variable.Builder(xPopUpMessageTimes,"end", "string");

		Xbean xPopUpMessageInfo = new Xbean(_meta_, "PopUpMessageInfo");
		new Variable.Builder(xPopUpMessageInfo,"version", "long");
		new Variable.Builder(xPopUpMessageInfo,"create", "long");
		new Variable.Builder(xPopUpMessageInfo,"update", "long");
		new Variable.Builder(xPopUpMessageInfo,"pop", "int");
		new Variable.Builder(xPopUpMessageInfo,"mode", "int");
		new Variable.Builder(xPopUpMessageInfo,"times", "vector").value("PopUpMessageTimes");
		new Variable.Builder(xPopUpMessageInfo,"types", "vector").value("PopUpMessageTypes");

		Xbean xMarquee = new Xbean(_meta_, "Marquee");
		new Variable.Builder(xMarquee,"id", "long");
		new Variable.Builder(xMarquee,"type", "int");
		new Variable.Builder(xMarquee,"showType", "int");
		new Variable.Builder(xMarquee,"msg", "string");
		new Variable.Builder(xMarquee,"weeklyRepeat", "int");
		new Variable.Builder(xMarquee,"absoluteDate", "long");
		new Variable.Builder(xMarquee,"hourAndMinute", "vector").value("long");
		new Variable.Builder(xMarquee,"rollTimes", "int");
		new Variable.Builder(xMarquee,"intervalTime", "int");
		new Variable.Builder(xMarquee,"color", "string");
		new Variable.Builder(xMarquee,"active", "boolean");
		new Variable.Builder(xMarquee,"operateBy", "int");

		Xbean xActivityHistoryRank = new Xbean(_meta_, "ActivityHistoryRank");
		new Variable.Builder(xActivityHistoryRank,"ranks", "map").key("long").value("HistoryRank");

		Xbean xPromoterInfo = new Xbean(_meta_, "PromoterInfo");
		new Variable.Builder(xPromoterInfo,"inviteeUnionidList", "vector").value("string");
		new Variable.Builder(xPromoterInfo,"totalEffectiveInvitee", "int").value("int");
		new Variable.Builder(xPromoterInfo,"unreceivedRewards", "float");
		new Variable.Builder(xPromoterInfo,"totalRewards", "float");
		new Variable.Builder(xPromoterInfo,"inviteeCountThisCycle", "int");
		new Variable.Builder(xPromoterInfo,"rewardsThisCycle", "float");
		new Variable.Builder(xPromoterInfo,"cycleStartTime", "long");
		new Variable.Builder(xPromoterInfo,"receivedCountToday", "int");
		new Variable.Builder(xPromoterInfo,"receivedRewardsToday", "float");
		new Variable.Builder(xPromoterInfo,"receivedTimeToday", "long");
		new Variable.Builder(xPromoterInfo,"rewardList", "vector").value("long");

		Xbean xUserMail = new Xbean(_meta_, "UserMail");
		new Variable.Builder(xUserMail,"personalMails", "vector").value("long");
		new Variable.Builder(xUserMail,"allUserMails", "vector").value("long");
		new Variable.Builder(xUserMail,"lastCheckTime", "long");

		Xbean xTurntableRewardDispatchBean = new Xbean(_meta_, "TurntableRewardDispatchBean");
		new Variable.Builder(xTurntableRewardDispatchBean,"rewardId", "int");
		new Variable.Builder(xTurntableRewardDispatchBean,"rewardName", "string");
		new Variable.Builder(xTurntableRewardDispatchBean,"rewardDesc", "string");
		new Variable.Builder(xTurntableRewardDispatchBean,"rewardTime", "long");
		new Variable.Builder(xTurntableRewardDispatchBean,"rewardCount", "int");
		new Variable.Builder(xTurntableRewardDispatchBean,"isCard", "boolean");
		new Variable.Builder(xTurntableRewardDispatchBean,"isDispatch", "boolean");

		Xbean xAddPlayersCardRecord = new Xbean(_meta_, "AddPlayersCardRecord");
		new Variable.Builder(xAddPlayersCardRecord,"createTime", "long");
		new Variable.Builder(xAddPlayersCardRecord,"gmtUsername", "string");
		new Variable.Builder(xAddPlayersCardRecord,"planCardNum", "int");
		new Variable.Builder(xAddPlayersCardRecord,"realCardNum", "int");
		new Variable.Builder(xAddPlayersCardRecord,"playerNum", "int");
		new Variable.Builder(xAddPlayersCardRecord,"successNum", "int");
		new Variable.Builder(xAddPlayersCardRecord,"failNum", "int");

		Xbean xRankActivityVersion = new Xbean(_meta_, "RankActivityVersion");
		new Variable.Builder(xRankActivityVersion,"version", "int");

		Xbean xPurchase_order_item = new Xbean(_meta_, "Purchase_order_item");
		new Variable.Builder(xPurchase_order_item,"orderId", "string");
		new Variable.Builder(xPurchase_order_item,"channelOrderId", "string");
		new Variable.Builder(xPurchase_order_item,"playerId", "int");
		new Variable.Builder(xPurchase_order_item,"channelId", "string");
		new Variable.Builder(xPurchase_order_item,"channelUid", "string");
		new Variable.Builder(xPurchase_order_item,"areaId", "int");
		new Variable.Builder(xPurchase_order_item,"deviceType", "string");
		new Variable.Builder(xPurchase_order_item,"rmb", "int");
		new Variable.Builder(xPurchase_order_item,"itemId", "string");
		new Variable.Builder(xPurchase_order_item,"status", "int");
		new Variable.Builder(xPurchase_order_item,"sign", "string");
		new Variable.Builder(xPurchase_order_item,"createTime", "long");

		Xbean xIdMobileBean = new Xbean(_meta_, "IdMobileBean");
		new Variable.Builder(xIdMobileBean,"accountId", "int");
		new Variable.Builder(xIdMobileBean,"accountMobile", "string");
		new Variable.Builder(xIdMobileBean,"bindTime", "long");
		new Variable.Builder(xIdMobileBean,"bindAward", "int");

		Xbean xLastRewardInfo = new Xbean(_meta_, "LastRewardInfo");
		new Variable.Builder(xLastRewardInfo,"rewardId", "int");
		new Variable.Builder(xLastRewardInfo,"rewardDesc", "string");

		Xbean xRoleRank = new Xbean(_meta_, "RoleRank");
		new Variable.Builder(xRoleRank,"roleId", "int");
		new Variable.Builder(xRoleRank,"nickname", "string");
		new Variable.Builder(xRoleRank,"score", "int");

		Xbean xClubRoomHistory = new Xbean(_meta_, "ClubRoomHistory");
		new Variable.Builder(xClubRoomHistory,"rooms", "vector").value("GlobalRoomId");

		Xbean xAgentSatusBean = new Xbean(_meta_, "AgentSatusBean");
		new Variable.Builder(xAgentSatusBean,"agentId", "int");
		new Variable.Builder(xAgentSatusBean,"phoneNumber", "string");
		new Variable.Builder(xAgentSatusBean,"bindStatus", "string");

		Xbean xGameActivityReward = new Xbean(_meta_, "GameActivityReward");
		new Variable.Builder(xGameActivityReward,"rewardName", "string");
		new Variable.Builder(xGameActivityReward,"rewardDesc", "string");
		new Variable.Builder(xGameActivityReward,"rewardCount", "int");
		new Variable.Builder(xGameActivityReward,"rewardLeftCount", "int");
		new Variable.Builder(xGameActivityReward,"rewardRatio", "int");
		new Variable.Builder(xGameActivityReward,"condition", "string");
		new Variable.Builder(xGameActivityReward,"isBroadcard", "boolean");
		new Variable.Builder(xGameActivityReward,"isReward", "boolean");
		new Variable.Builder(xGameActivityReward,"isCardReward", "boolean");

		Xbean xPersistGlobalInfo = new Xbean(_meta_, "PersistGlobalInfo");
		new Variable.Builder(xPersistGlobalInfo,"allowLoginChannelKeySeed", "int");
		new Variable.Builder(xPersistGlobalInfo,"allowLoginChannel", "map").key("int").value("string");
		new Variable.Builder(xPersistGlobalInfo,"forbidPlayers", "map").key("int").value("ForbidRole");

		Xbean xMail = new Xbean(_meta_, "Mail");
		new Variable.Builder(xMail,"id", "long");
		new Variable.Builder(xMail,"type", "int");
		new Variable.Builder(xMail,"msg", "string");
		new Variable.Builder(xMail,"time", "long");
		new Variable.Builder(xMail,"sender", "int");

		Xbean xPlayerAddCard = new Xbean(_meta_, "PlayerAddCard");
		new Variable.Builder(xPlayerAddCard,"records", "vector").value("AddCard");

		Xbean xDateActivityRank = new Xbean(_meta_, "DateActivityRank");
		new Variable.Builder(xDateActivityRank,"roleRanks", "vector").value("RoleRank");

		Xbean xCombatInfo = new Xbean(_meta_, "CombatInfo");
		new Variable.Builder(xCombatInfo,"time", "long");
		new Variable.Builder(xCombatInfo,"count", "int");

		Xbean xNotice = new Xbean(_meta_, "Notice");
		new Variable.Builder(xNotice,"id", "long");
		new Variable.Builder(xNotice,"content", "string");
		new Variable.Builder(xNotice,"imgUrl", "string");
		new Variable.Builder(xNotice,"startTime", "long");
		new Variable.Builder(xNotice,"endTime", "long");
		new Variable.Builder(xNotice,"isCancel", "boolean");
		new Variable.Builder(xNotice,"noticeName", "string");
		new Variable.Builder(xNotice,"popTimes", "int");

		Xbean xTurntableRewardDispatch = new Xbean(_meta_, "TurntableRewardDispatch");
		new Variable.Builder(xTurntableRewardDispatch,"bean", "vector").value("TurntableRewardDispatchBean");

		Xbean xTurntableActivityReward = new Xbean(_meta_, "TurntableActivityReward");
		new Variable.Builder(xTurntableActivityReward,"roleId", "int");
		new Variable.Builder(xTurntableActivityReward,"itemCount", "int");
		new Variable.Builder(xTurntableActivityReward,"consumeNum", "int");
		new Variable.Builder(xTurntableActivityReward,"consumeAddNumTime", "long");
		new Variable.Builder(xTurntableActivityReward,"shareTime", "long");
		new Variable.Builder(xTurntableActivityReward,"rewards", "vector").value("TurntableActivityRewardRecord");

		Xbean xButtonTableMap = new Xbean(_meta_, "ButtonTableMap");
		new Variable.Builder(xButtonTableMap,"buttonMap", "map").key("int").value("ButtonBean");

		Xbean xTurntableActivityVersion = new Xbean(_meta_, "TurntableActivityVersion");
		new Variable.Builder(xTurntableActivityVersion,"version", "int");

		Xbean xActivityRank = new Xbean(_meta_, "ActivityRank");
		new Variable.Builder(xActivityRank,"dateRank", "map").key("long").value("DateActivityRank");

		Xbean xAddPlayersCardDetail = new Xbean(_meta_, "AddPlayersCardDetail");
		new Variable.Builder(xAddPlayersCardDetail,"cardNum", "int");
		new Variable.Builder(xAddPlayersCardDetail,"status", "int");
		new Variable.Builder(xAddPlayersCardDetail,"creaetTime", "long");

		Xbean xReceiveRewardInfo = new Xbean(_meta_, "ReceiveRewardInfo");
		new Variable.Builder(xReceiveRewardInfo,"id", "long");
		new Variable.Builder(xReceiveRewardInfo,"date", "long");
		new Variable.Builder(xReceiveRewardInfo,"receivedThisTime", "float");
		new Variable.Builder(xReceiveRewardInfo,"receivedTotal", "float");
		new Variable.Builder(xReceiveRewardInfo,"isHandled", "int");
		new Variable.Builder(xReceiveRewardInfo,"handleTime", "long");
		new Variable.Builder(xReceiveRewardInfo,"gmName", "string");

		Xbean xButtonBean = new Xbean(_meta_, "ButtonBean");
		new Variable.Builder(xButtonBean,"status", "int");

		Xbean xRoleMemInfo = new Xbean(_meta_, "RoleMemInfo");
		new Variable.Builder(xRoleMemInfo,"battleServerId", "int");
		new Variable.Builder(xRoleMemInfo,"roomId", "int");
		new Variable.Builder(xRoleMemInfo,"connectionId", "int");
		new Variable.Builder(xRoleMemInfo,"onlineTimeInDay", "long");
		new Variable.Builder(xRoleMemInfo,"addictionAlertTimesInDay", "int");
		new Variable.Builder(xRoleMemInfo,"lastAddictionAlertTime", "long");
		new Variable.Builder(xRoleMemInfo,"lastLogoutTime", "long");
		new Variable.Builder(xRoleMemInfo,"serverStartupTime", "long");

		Xbean xSubCard = new Xbean(_meta_, "SubCard");
		new Variable.Builder(xSubCard,"gmAdmin", "string");
		new Variable.Builder(xSubCard,"count", "int");
		new Variable.Builder(xSubCard,"reason", "string");
		new Variable.Builder(xSubCard,"time", "long");


		new Table.Builder(_meta_, "role_info", "int", "RoleInfo").cacheCap("30000");
		new Table.Builder(_meta_, "role_mem_info", "int", "RoleMemInfo").cacheCap("30000");
		new Table.Builder(_meta_, "room_info", "int", "RoomInfo").cacheCap("30000");
		new Table.Builder(_meta_, "marquee_active", "long", "Marquee").autoIncrement(true);
		new Table.Builder(_meta_, "marquee_version", "int", "LongValue");
		new Table.Builder(_meta_, "user_mails", "int", "UserMail").cacheCap("30000");
		new Table.Builder(_meta_, "personal_mails", "long", "Mail").autoIncrement(true);
		new Table.Builder(_meta_, "public_mails", "long", "Mail");
		new Table.Builder(_meta_, "club_room_history", "RoleClubId", "ClubRoomHistory");
		new Table.Builder(_meta_, "normal_contact_table", "long", "NormalContact").autoIncrement(true);
		new Table.Builder(_meta_, "limit_contact_table", "long", "LimitContact").autoIncrement(true);
		new Table.Builder(_meta_, "add_card_table", "int", "PlayerAddCard");
		new Table.Builder(_meta_, "sub_card_table", "int", "PlayerSubCard");
		new Table.Builder(_meta_, "notice_table", "long", "Notice").autoIncrement(true);
		new Table.Builder(_meta_, "notice_version", "int", "LongValue");
		new Table.Builder(_meta_, "add_all_card_record", "long", "AddAllCardRecord").autoIncrement(true);
		new Table.Builder(_meta_, "main_notice_table", "string", "MainNotice");
		new Table.Builder(_meta_, "turntable_activity_version_table", "int", "TurntableActivityVersion");
		new Table.Builder(_meta_, "game_activity_reward_table", "ActivityRewardKey", "GameActivityReward");
		new Table.Builder(_meta_, "games_activity_turntable_reward", "int", "TurntableActivityReward").attr("type", "vector");
		new Table.Builder(_meta_, "turntable_reward_dispatch_table", "int", "TurntableRewardDispatch");
		new Table.Builder(_meta_, "last_reward_info_table", "LastRewardKey", "LastRewardInfo");
		new Table.Builder(_meta_, "button_table", "int", "ButtonTableMap");
		new Table.Builder(_meta_, "limited_costless_activity", "long", "LimitedCostlessActivity").autoIncrement(true);
		new Table.Builder(_meta_, "add_players_card_record_table", "int", "AddPlayersCardRecord");
		new Table.Builder(_meta_, "add_players_card_detail_table", "AddPlayersCardDetailKey", "AddPlayersCardDetail");
		new Table.Builder(_meta_, "runtime_global", "int", "RuntimeGlobalInfo").memory(true);
		new Table.Builder(_meta_, "persist_global", "int", "PersistGlobalInfo");
		new Table.Builder(_meta_, "rank_activity_version_table", "int", "RankActivityVersion");
		new Table.Builder(_meta_, "activity_rank", "int", "ActivityRank");
		new Table.Builder(_meta_, "activity_history_rank", "int", "RoleHistoryRank");
		new Table.Builder(_meta_, "purchase_order_table", "string", "Purchase_order_item");
		new Table.Builder(_meta_, "role_records", "int", "RoleRecord");
		new Table.Builder(_meta_, "string_tables", "int", "string");
		new Table.Builder(_meta_, "mobile_id_table", "string", "MobileIdBean");
		new Table.Builder(_meta_, "id_mobile_table", "int", "IdMobileBean");
		new Table.Builder(_meta_, "diamond_mobilebind_table", "string", "DiamondMobileBindBean");
		new Table.Builder(_meta_, "promoter_info", "string", "PromoterInfo");
		new Table.Builder(_meta_, "receive_reward_info", "long", "ReceiveRewardInfo").autoIncrement(true);
		new Table.Builder(_meta_, "invitee_info", "string", "InviteeInfo");
		new Table.Builder(_meta_, "unionid_2_roleid", "string", "int");
		new Table.Builder(_meta_, "pop_up_config", "int", "PopUpMessageInfo");
		new Table.Builder(_meta_, "id_agent_table", "int", "AgentSatusBean");
		new Table.Builder(_meta_, "authentiction_info", "int", "AuthentictionInfo");

		return _meta_;
	}
}
