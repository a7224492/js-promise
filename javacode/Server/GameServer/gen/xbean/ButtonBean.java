package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ButtonBean extends limax.zdb.XBean {
    private int status; 

    ButtonBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public ButtonBean() {
		this(null, null);
	}

	public ButtonBean(ButtonBean _o_) {
		this(_o_, null, null);
	}

	ButtonBean(ButtonBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ButtonBean", true);
        this.status = _o_.status;
	}

	public void copyFrom(ButtonBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromButtonBean", true);
		this.verifyStandaloneOrLockHeld("copyToButtonBean", false);
        limax.zdb.Logs.logObject(this, "status");
        this.status = _o_.status;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.status);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.status = _os_.unmarshal_int();
		return _os_;
	}

	public int getStatus() { 
		this.verifyStandaloneOrLockHeld("getStatus", true);
		return this.status;
	}

	public void setStatus(int _v_) { 
		this.verifyStandaloneOrLockHeld("setStatus", false);
		limax.zdb.Logs.logObject(this, "status");
		this.status = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ButtonBean _o_ = null;
		if ( _o1_ instanceof ButtonBean ) _o_ = (ButtonBean)_o1_;
		else return false;
		if (this.status != _o_.status) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.status;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.status).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
