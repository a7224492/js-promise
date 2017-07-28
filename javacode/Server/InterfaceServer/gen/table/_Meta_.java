package table;

import limax.xmlgen.Table;
import limax.xmlgen.Procedure;
import limax.xmlgen.Zdb;

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



		new Table.Builder(_meta_, "InscribableObject", "int", "string").memory(true);

		return _meta_;
	}
}
