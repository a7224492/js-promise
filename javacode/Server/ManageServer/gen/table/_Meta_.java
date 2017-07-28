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


		Xbean xIntValue = new Xbean(_meta_, "IntValue");
		new Variable.Builder(xIntValue,"id", "int");

		Xbean xServerInfo = new Xbean(_meta_, "ServerInfo");
		new Variable.Builder(xServerInfo,"id", "int");
		new Variable.Builder(xServerInfo,"type", "int");
		new Variable.Builder(xServerInfo,"ip", "string");
		new Variable.Builder(xServerInfo,"port4server", "int");
		new Variable.Builder(xServerInfo,"port4webSocketClient", "int");
		new Variable.Builder(xServerInfo,"port4SocketClient", "int");
		new Variable.Builder(xServerInfo,"ip4Client", "string");

		Xbean xPortBase = new Xbean(_meta_, "PortBase");
		new Variable.Builder(xPortBase,"ip", "string");
		new Variable.Builder(xPortBase,"serverType", "int");
		new Variable.Builder(xPortBase,"portType", "int");
		new Variable.Builder(xPortBase,"area", "int");
		new Variable.Builder(xPortBase,"port", "int");


		new Table.Builder(_meta_, "server_info", "int", "ServerInfo").memory(true);
		new Table.Builder(_meta_, "server_id", "int", "IntValue");
		new Table.Builder(_meta_, "port_table", "string", "PortBase").memory(true);

		return _meta_;
	}
}
