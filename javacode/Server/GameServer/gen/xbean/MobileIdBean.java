package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class MobileIdBean extends limax.zdb.XBean {
    private int accountId; 
    private long bindTime; 
    private String status; 
    private String code; 
    private long codeTime; 

    MobileIdBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        status = "";
        code = "";
	}

	public MobileIdBean() {
		this(null, null);
	}

	public MobileIdBean(MobileIdBean _o_) {
		this(_o_, null, null);
	}

	MobileIdBean(MobileIdBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.MobileIdBean", true);
        this.accountId = _o_.accountId;
        this.bindTime = _o_.bindTime;
        this.status = _o_.status;
        this.code = _o_.code;
        this.codeTime = _o_.codeTime;
	}

	public void copyFrom(MobileIdBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromMobileIdBean", true);
		this.verifyStandaloneOrLockHeld("copyToMobileIdBean", false);
        limax.zdb.Logs.logObject(this, "accountId");
        this.accountId = _o_.accountId;
        limax.zdb.Logs.logObject(this, "bindTime");
        this.bindTime = _o_.bindTime;
        limax.zdb.Logs.logObject(this, "status");
        this.status = _o_.status;
        limax.zdb.Logs.logObject(this, "code");
        this.code = _o_.code;
        limax.zdb.Logs.logObject(this, "codeTime");
        this.codeTime = _o_.codeTime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.accountId);
        _os_.marshal(this.bindTime);
        _os_.marshal(this.status);
        _os_.marshal(this.code);
        _os_.marshal(this.codeTime);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.accountId = _os_.unmarshal_int();
		this.bindTime = _os_.unmarshal_long();
		this.status = _os_.unmarshal_String();
		this.code = _os_.unmarshal_String();
		this.codeTime = _os_.unmarshal_long();
		return _os_;
	}

	public int getAccountId() { 
		this.verifyStandaloneOrLockHeld("getAccountId", true);
		return this.accountId;
	}

	public long getBindTime() { 
		this.verifyStandaloneOrLockHeld("getBindTime", true);
		return this.bindTime;
	}

	public String getStatus() { 
		this.verifyStandaloneOrLockHeld("getStatus", true);
		return this.status;
	}

	public String getCode() { 
		this.verifyStandaloneOrLockHeld("getCode", true);
		return this.code;
	}

	public long getCodeTime() { 
		this.verifyStandaloneOrLockHeld("getCodeTime", true);
		return this.codeTime;
	}

	public void setAccountId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAccountId", false);
		limax.zdb.Logs.logObject(this, "accountId");
		this.accountId = _v_;
	}

	public void setBindTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setBindTime", false);
		limax.zdb.Logs.logObject(this, "bindTime");
		this.bindTime = _v_;
	}

	public void setStatus(String _v_) { 
		this.verifyStandaloneOrLockHeld("setStatus", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "status");
		this.status = _v_;
	}

	public void setCode(String _v_) { 
		this.verifyStandaloneOrLockHeld("setCode", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "code");
		this.code = _v_;
	}

	public void setCodeTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setCodeTime", false);
		limax.zdb.Logs.logObject(this, "codeTime");
		this.codeTime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		MobileIdBean _o_ = null;
		if ( _o1_ instanceof MobileIdBean ) _o_ = (MobileIdBean)_o1_;
		else return false;
		if (this.accountId != _o_.accountId) return false;
		if (this.bindTime != _o_.bindTime) return false;
		if (!this.status.equals(_o_.status)) return false;
		if (!this.code.equals(_o_.code)) return false;
		if (this.codeTime != _o_.codeTime) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.accountId;
		_h_ += _h_ * 31 + (int)(this.bindTime ^ (this.bindTime >>> 32));
		_h_ += _h_ * 31 + this.status.hashCode();
		_h_ += _h_ * 31 + this.code.hashCode();
		_h_ += _h_ * 31 + (int)(this.codeTime ^ (this.codeTime >>> 32));
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.accountId).append(",");
		_sb_.append(this.bindTime).append(",");
		_sb_.append("T").append(this.status.length()).append(",");
		_sb_.append("T").append(this.code.length()).append(",");
		_sb_.append(this.codeTime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
