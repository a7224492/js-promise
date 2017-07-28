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


		Xbean xDeviceIdBindRecordBean = new Xbean(_meta_, "DeviceIdBindRecordBean");
		new Variable.Builder(xDeviceIdBindRecordBean,"accountId", "int");

		Xbean xProVersion = new Xbean(_meta_, "ProVersion");
		new Variable.Builder(xProVersion,"version", "string");
		new Variable.Builder(xProVersion,"description", "string");

		Xbean xAccountInfo = new Xbean(_meta_, "AccountInfo");
		new Variable.Builder(xAccountInfo,"accountId", "int");
		new Variable.Builder(xAccountInfo,"platform", "string");
		new Variable.Builder(xAccountInfo,"channel", "string");
		new Variable.Builder(xAccountInfo,"username", "string");
		new Variable.Builder(xAccountInfo,"refreshToken", "string");
		new Variable.Builder(xAccountInfo,"nickname", "string");
		new Variable.Builder(xAccountInfo,"sex", "int");
		new Variable.Builder(xAccountInfo,"headImgUrl", "string");
		new Variable.Builder(xAccountInfo,"createTime", "long");
		new Variable.Builder(xAccountInfo,"authTime", "long");
		new Variable.Builder(xAccountInfo,"tokenTime", "long");
		new Variable.Builder(xAccountInfo,"province", "string");
		new Variable.Builder(xAccountInfo,"city", "string");
		new Variable.Builder(xAccountInfo,"country", "string");
		new Variable.Builder(xAccountInfo,"unionid", "string");
		new Variable.Builder(xAccountInfo,"deviceId", "string");

		Xbean xVersionUpdateBean = new Xbean(_meta_, "VersionUpdateBean");
		new Variable.Builder(xVersionUpdateBean,"channel", "string");
		new Variable.Builder(xVersionUpdateBean,"subchannel", "string");
		new Variable.Builder(xVersionUpdateBean,"libVersion", "string");
		new Variable.Builder(xVersionUpdateBean,"lastLibVersion", "string");
		new Variable.Builder(xVersionUpdateBean,"LibUrl", "string");
		new Variable.Builder(xVersionUpdateBean,"proVersion", "string");
		new Variable.Builder(xVersionUpdateBean,"proForceUpdate", "boolean");
		new Variable.Builder(xVersionUpdateBean,"proUrl", "string");
		new Variable.Builder(xVersionUpdateBean,"reviewVersion", "string");
		new Variable.Builder(xVersionUpdateBean,"reviewUrl", "string");

		Xbean xUnionidAccountidBean = new Xbean(_meta_, "UnionidAccountidBean");
		new Variable.Builder(xUnionidAccountidBean,"accountId", "int");
		new Variable.Builder(xUnionidAccountidBean,"lastLoginTime", "long");
		new Variable.Builder(xUnionidAccountidBean,"mergeList", "vector").value("int");

		Xbean xDeviceidUnionidBean = new Xbean(_meta_, "DeviceidUnionidBean");
		new Variable.Builder(xDeviceidUnionidBean,"UnionidList", "vector").value("string");

		Xbean xLibVersion = new Xbean(_meta_, "LibVersion");
		new Variable.Builder(xLibVersion,"version", "string");
		new Variable.Builder(xLibVersion,"description", "string");
		new Variable.Builder(xLibVersion,"url", "string");
		new Variable.Builder(xLibVersion,"forceUpdate", "boolean");

		Xbean xJidBindRecordBean = new Xbean(_meta_, "JidBindRecordBean");
		new Variable.Builder(xJidBindRecordBean,"deviceId", "string");
		new Variable.Builder(xJidBindRecordBean,"appCode", "int");
		new Variable.Builder(xJidBindRecordBean,"accountId", "int");
		new Variable.Builder(xJidBindRecordBean,"openid", "string");
		new Variable.Builder(xJidBindRecordBean,"nickname", "string");
		new Variable.Builder(xJidBindRecordBean,"status", "int");

		Xbean xAccountIDSeed = new Xbean(_meta_, "AccountIDSeed");
		new Variable.Builder(xAccountIDSeed,"seed", "int");

		Xbean xRecordList = new Xbean(_meta_, "RecordList");
		new Variable.Builder(xRecordList,"record", "vector").value("JidBindRecordBean");

		Xbean xDeviceidAccountidBean = new Xbean(_meta_, "DeviceidAccountidBean");
		new Variable.Builder(xDeviceidAccountidBean,"accountIdList", "vector").value("int");

		Xbean xClientVersion = new Xbean(_meta_, "ClientVersion");
		new Variable.Builder(xClientVersion,"proVersion", "ProVersion");
		new Variable.Builder(xClientVersion,"libVersions", "map").key("string").value("LibVersion");


		new Table.Builder(_meta_, "account_table", "string", "AccountInfo");
		new Table.Builder(_meta_, "account_table_1", "string", "AccountInfo");
		new Table.Builder(_meta_, "account_table_2", "string", "AccountInfo");
		new Table.Builder(_meta_, "account_table_3", "string", "AccountInfo");
		new Table.Builder(_meta_, "account_table_4", "string", "AccountInfo");
		new Table.Builder(_meta_, "account_table_5", "string", "AccountInfo");
		new Table.Builder(_meta_, "account_table_6", "string", "AccountInfo");
		new Table.Builder(_meta_, "account_table_7", "string", "AccountInfo");
		new Table.Builder(_meta_, "account_table_8", "string", "AccountInfo");
		new Table.Builder(_meta_, "account_table_9", "string", "AccountInfo");
		new Table.Builder(_meta_, "account_table_10", "string", "AccountInfo");
		new Table.Builder(_meta_, "user_account", "int", "string");
		new Table.Builder(_meta_, "global_accountid", "int", "AccountIDSeed");
		new Table.Builder(_meta_, "unionid_accountid_table", "string", "UnionidAccountidBean");
		new Table.Builder(_meta_, "unionid_accountid_table_1", "string", "UnionidAccountidBean");
		new Table.Builder(_meta_, "deviceid_unionid_table", "string", "DeviceidUnionidBean");
		new Table.Builder(_meta_, "jid_bind_record_table", "int", "RecordList");
		new Table.Builder(_meta_, "deviceid_bind_record_table", "string", "DeviceIdBindRecordBean");
		new Table.Builder(_meta_, "deviceid_accountid_table", "string", "DeviceidAccountidBean");
		new Table.Builder(_meta_, "global_client_version", "int", "ClientVersion");
		new Table.Builder(_meta_, "channel_version_table", "string", "VersionUpdateBean");

		return _meta_;
	}
}
