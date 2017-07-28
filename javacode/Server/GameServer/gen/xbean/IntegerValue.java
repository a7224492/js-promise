package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class IntegerValue extends limax.zdb.XBean {
    private int val; 

    IntegerValue(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public IntegerValue() {
		this(null, null);
	}

	public IntegerValue(IntegerValue _o_) {
		this(_o_, null, null);
	}

	IntegerValue(IntegerValue _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.IntegerValue", true);
        this.val = _o_.val;
	}

	public void copyFrom(IntegerValue _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromIntegerValue", true);
		this.verifyStandaloneOrLockHeld("copyToIntegerValue", false);
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
		this.val = _os_.unmarshal_int();
		return _os_;
	}

	public int getVal() { 
		this.verifyStandaloneOrLockHeld("getVal", true);
		return this.val;
	}

	public void setVal(int _v_) { 
		this.verifyStandaloneOrLockHeld("setVal", false);
		limax.zdb.Logs.logObject(this, "val");
		this.val = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		IntegerValue _o_ = null;
		if ( _o1_ instanceof IntegerValue ) _o_ = (IntegerValue)_o1_;
		else return false;
		if (this.val != _o_.val) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.val;
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
