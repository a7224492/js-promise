package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RecordList extends limax.zdb.XBean {
    private java.util.ArrayList<xbean.JidBindRecordBean> record; 

    RecordList(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        record = new java.util.ArrayList<xbean.JidBindRecordBean>();
	}

	public RecordList() {
		this(null, null);
	}

	public RecordList(RecordList _o_) {
		this(_o_, null, null);
	}

	RecordList(RecordList _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RecordList", true);
        this.record = new java.util.ArrayList<xbean.JidBindRecordBean>();
        _o_.record.forEach(_v_ -> this.record.add(new JidBindRecordBean(_v_, this, "record")));
	}

	public void copyFrom(RecordList _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRecordList", true);
		this.verifyStandaloneOrLockHeld("copyToRecordList", false);
        java.util.List<xbean.JidBindRecordBean> this_record = limax.zdb.Logs.logList(this, "record", ()->{});
        this_record.clear();
        _o_.record.forEach(_v_ -> this_record.add(new JidBindRecordBean(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.record.size());
        for (xbean.JidBindRecordBean _v_ : this.record) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.JidBindRecordBean _v_ = new JidBindRecordBean(this, "record");
			_v_.unmarshal(_os_);
			this.record.add(_v_);
		}
		return _os_;
	}

	public java.util.List<xbean.JidBindRecordBean> getRecord() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "record", this.verifyStandaloneOrLockHeld("getRecord", true)) : this.record;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RecordList _o_ = null;
		if ( _o1_ instanceof RecordList ) _o_ = (RecordList)_o1_;
		else return false;
		if (!this.record.equals(_o_.record)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.record.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.record).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
