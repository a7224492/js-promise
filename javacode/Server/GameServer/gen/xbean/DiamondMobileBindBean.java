package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class DiamondMobileBindBean extends limax.zdb.XBean {
    private int diamond; 

    DiamondMobileBindBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public DiamondMobileBindBean() {
		this(null, null);
	}

	public DiamondMobileBindBean(DiamondMobileBindBean _o_) {
		this(_o_, null, null);
	}

	DiamondMobileBindBean(DiamondMobileBindBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.DiamondMobileBindBean", true);
        this.diamond = _o_.diamond;
	}

	public void copyFrom(DiamondMobileBindBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromDiamondMobileBindBean", true);
		this.verifyStandaloneOrLockHeld("copyToDiamondMobileBindBean", false);
        limax.zdb.Logs.logObject(this, "diamond");
        this.diamond = _o_.diamond;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.diamond);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.diamond = _os_.unmarshal_int();
		return _os_;
	}

	public int getDiamond() { 
		this.verifyStandaloneOrLockHeld("getDiamond", true);
		return this.diamond;
	}

	public void setDiamond(int _v_) { 
		this.verifyStandaloneOrLockHeld("setDiamond", false);
		limax.zdb.Logs.logObject(this, "diamond");
		this.diamond = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		DiamondMobileBindBean _o_ = null;
		if ( _o1_ instanceof DiamondMobileBindBean ) _o_ = (DiamondMobileBindBean)_o1_;
		else return false;
		if (this.diamond != _o_.diamond) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.diamond;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.diamond).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
