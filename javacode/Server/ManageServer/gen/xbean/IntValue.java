package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class IntValue extends limax.zdb.XBean {
    private int id; 

    IntValue(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public IntValue() {
		this(null, null);
	}

	public IntValue(IntValue _o_) {
		this(_o_, null, null);
	}

	IntValue(IntValue _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.IntValue", true);
        this.id = _o_.id;
	}

	public void copyFrom(IntValue _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromIntValue", true);
		this.verifyStandaloneOrLockHeld("copyToIntValue", false);
        limax.zdb.Logs.logObject(this, "id");
        this.id = _o_.id;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.id);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.id = _os_.unmarshal_int();
		return _os_;
	}

	public int getId() { 
		this.verifyStandaloneOrLockHeld("getId", true);
		return this.id;
	}

	public void setId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setId", false);
		limax.zdb.Logs.logObject(this, "id");
		this.id = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		IntValue _o_ = null;
		if ( _o1_ instanceof IntValue ) _o_ = (IntValue)_o1_;
		else return false;
		if (this.id != _o_.id) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.id;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.id).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
