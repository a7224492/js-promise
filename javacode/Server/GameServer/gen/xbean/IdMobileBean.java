package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class IdMobileBean extends limax.zdb.XBean {
    private int accountId; 
    private String accountMobile; 
    private long bindTime; 
    private int bindAward; 

    IdMobileBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        accountMobile = "";
	}

	public IdMobileBean() {
		this(null, null);
	}

	public IdMobileBean(IdMobileBean _o_) {
		this(_o_, null, null);
	}

	IdMobileBean(IdMobileBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.IdMobileBean", true);
        this.accountId = _o_.accountId;
        this.accountMobile = _o_.accountMobile;
        this.bindTime = _o_.bindTime;
        this.bindAward = _o_.bindAward;
	}

	public void copyFrom(IdMobileBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromIdMobileBean", true);
		this.verifyStandaloneOrLockHeld("copyToIdMobileBean", false);
        limax.zdb.Logs.logObject(this, "accountId");
        this.accountId = _o_.accountId;
        limax.zdb.Logs.logObject(this, "accountMobile");
        this.accountMobile = _o_.accountMobile;
        limax.zdb.Logs.logObject(this, "bindTime");
        this.bindTime = _o_.bindTime;
        limax.zdb.Logs.logObject(this, "bindAward");
        this.bindAward = _o_.bindAward;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.accountId);
        _os_.marshal(this.accountMobile);
        _os_.marshal(this.bindTime);
        _os_.marshal(this.bindAward);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.accountId = _os_.unmarshal_int();
		this.accountMobile = _os_.unmarshal_String();
		this.bindTime = _os_.unmarshal_long();
		this.bindAward = _os_.unmarshal_int();
		return _os_;
	}

	public int getAccountId() { 
		this.verifyStandaloneOrLockHeld("getAccountId", true);
		return this.accountId;
	}

	public String getAccountMobile() { 
		this.verifyStandaloneOrLockHeld("getAccountMobile", true);
		return this.accountMobile;
	}

	public long getBindTime() { 
		this.verifyStandaloneOrLockHeld("getBindTime", true);
		return this.bindTime;
	}

	public int getBindAward() { 
		this.verifyStandaloneOrLockHeld("getBindAward", true);
		return this.bindAward;
	}

	public void setAccountId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAccountId", false);
		limax.zdb.Logs.logObject(this, "accountId");
		this.accountId = _v_;
	}

	public void setAccountMobile(String _v_) { 
		this.verifyStandaloneOrLockHeld("setAccountMobile", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "accountMobile");
		this.accountMobile = _v_;
	}

	public void setBindTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setBindTime", false);
		limax.zdb.Logs.logObject(this, "bindTime");
		this.bindTime = _v_;
	}

	public void setBindAward(int _v_) { 
		this.verifyStandaloneOrLockHeld("setBindAward", false);
		limax.zdb.Logs.logObject(this, "bindAward");
		this.bindAward = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		IdMobileBean _o_ = null;
		if ( _o1_ instanceof IdMobileBean ) _o_ = (IdMobileBean)_o1_;
		else return false;
		if (this.accountId != _o_.accountId) return false;
		if (!this.accountMobile.equals(_o_.accountMobile)) return false;
		if (this.bindTime != _o_.bindTime) return false;
		if (this.bindAward != _o_.bindAward) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.accountId;
		_h_ += _h_ * 31 + this.accountMobile.hashCode();
		_h_ += _h_ * 31 + (int)(this.bindTime ^ (this.bindTime >>> 32));
		_h_ += _h_ * 31 + this.bindAward;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.accountId).append(",");
		_sb_.append("T").append(this.accountMobile.length()).append(",");
		_sb_.append(this.bindTime).append(",");
		_sb_.append(this.bindAward).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
