package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class AuthentictionInfo extends limax.zdb.XBean {
    private String realname; 
    private String idnumber; 

    AuthentictionInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        realname = "";
        idnumber = "";
	}

	public AuthentictionInfo() {
		this(null, null);
	}

	public AuthentictionInfo(AuthentictionInfo _o_) {
		this(_o_, null, null);
	}

	AuthentictionInfo(AuthentictionInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.AuthentictionInfo", true);
        this.realname = _o_.realname;
        this.idnumber = _o_.idnumber;
	}

	public void copyFrom(AuthentictionInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromAuthentictionInfo", true);
		this.verifyStandaloneOrLockHeld("copyToAuthentictionInfo", false);
        limax.zdb.Logs.logObject(this, "realname");
        this.realname = _o_.realname;
        limax.zdb.Logs.logObject(this, "idnumber");
        this.idnumber = _o_.idnumber;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.realname);
        _os_.marshal(this.idnumber);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.realname = _os_.unmarshal_String();
		this.idnumber = _os_.unmarshal_String();
		return _os_;
	}

	public String getRealname() { 
		this.verifyStandaloneOrLockHeld("getRealname", true);
		return this.realname;
	}

	public String getIdnumber() { 
		this.verifyStandaloneOrLockHeld("getIdnumber", true);
		return this.idnumber;
	}

	public void setRealname(String _v_) { 
		this.verifyStandaloneOrLockHeld("setRealname", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "realname");
		this.realname = _v_;
	}

	public void setIdnumber(String _v_) { 
		this.verifyStandaloneOrLockHeld("setIdnumber", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "idnumber");
		this.idnumber = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		AuthentictionInfo _o_ = null;
		if ( _o1_ instanceof AuthentictionInfo ) _o_ = (AuthentictionInfo)_o1_;
		else return false;
		if (!this.realname.equals(_o_.realname)) return false;
		if (!this.idnumber.equals(_o_.idnumber)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.realname.hashCode();
		_h_ += _h_ * 31 + this.idnumber.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append("T").append(this.realname.length()).append(",");
		_sb_.append("T").append(this.idnumber.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
