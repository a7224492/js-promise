package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class LongValue extends limax.zdb.XBean {
    private long val; 

    LongValue(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public LongValue() {
		this(null, null);
	}

	public LongValue(LongValue _o_) {
		this(_o_, null, null);
	}

	LongValue(LongValue _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.LongValue", true);
        this.val = _o_.val;
	}

	public void copyFrom(LongValue _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromLongValue", true);
		this.verifyStandaloneOrLockHeld("copyToLongValue", false);
        limax.zdb.Logs.logObject(this, "val");
        this.val = _o_.val;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.val);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.val = _os_.unmarshal_long();
		return _os_;
	}

	public long getVal() { 
		this.verifyStandaloneOrLockHeld("getVal", true);
		return this.val;
	}

	public void setVal(long _v_) { 
		this.verifyStandaloneOrLockHeld("setVal", false);
		limax.zdb.Logs.logObject(this, "val");
		this.val = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		LongValue _o_ = null;
		if ( _o1_ instanceof LongValue ) _o_ = (LongValue)_o1_;
		else return false;
		if (this.val != _o_.val) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + (int)(this.val ^ (this.val >>> 32));
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.val).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
