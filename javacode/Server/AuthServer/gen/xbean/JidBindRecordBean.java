package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class JidBindRecordBean extends limax.zdb.XBean {
    private String deviceId; 
    private int appCode; 
    private int accountId; 
    private String openid; 
    private String nickname; 
    private int status; 

    JidBindRecordBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        deviceId = "";
        openid = "";
        nickname = "";
	}

	public JidBindRecordBean() {
		this(null, null);
	}

	public JidBindRecordBean(JidBindRecordBean _o_) {
		this(_o_, null, null);
	}

	JidBindRecordBean(JidBindRecordBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.JidBindRecordBean", true);
        this.deviceId = _o_.deviceId;
        this.appCode = _o_.appCode;
        this.accountId = _o_.accountId;
        this.openid = _o_.openid;
        this.nickname = _o_.nickname;
        this.status = _o_.status;
	}

	public void copyFrom(JidBindRecordBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromJidBindRecordBean", true);
		this.verifyStandaloneOrLockHeld("copyToJidBindRecordBean", false);
        limax.zdb.Logs.logObject(this, "deviceId");
        this.deviceId = _o_.deviceId;
        limax.zdb.Logs.logObject(this, "appCode");
        this.appCode = _o_.appCode;
        limax.zdb.Logs.logObject(this, "accountId");
        this.accountId = _o_.accountId;
        limax.zdb.Logs.logObject(this, "openid");
        this.openid = _o_.openid;
        limax.zdb.Logs.logObject(this, "nickname");
        this.nickname = _o_.nickname;
        limax.zdb.Logs.logObject(this, "status");
        this.status = _o_.status;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.deviceId);
        _os_.marshal(this.appCode);
        _os_.marshal(this.accountId);
        _os_.marshal(this.openid);
        _os_.marshal(this.nickname);
        _os_.marshal(this.status);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.deviceId = _os_.unmarshal_String();
		this.appCode = _os_.unmarshal_int();
		this.accountId = _os_.unmarshal_int();
		this.openid = _os_.unmarshal_String();
		this.nickname = _os_.unmarshal_String();
		this.status = _os_.unmarshal_int();
		return _os_;
	}

	public String getDeviceId() { 
		this.verifyStandaloneOrLockHeld("getDeviceId", true);
		return this.deviceId;
	}

	public int getAppCode() { 
		this.verifyStandaloneOrLockHeld("getAppCode", true);
		return this.appCode;
	}

	public int getAccountId() { 
		this.verifyStandaloneOrLockHeld("getAccountId", true);
		return this.accountId;
	}

	public String getOpenid() { 
		this.verifyStandaloneOrLockHeld("getOpenid", true);
		return this.openid;
	}

	public String getNickname() { 
		this.verifyStandaloneOrLockHeld("getNickname", true);
		return this.nickname;
	}

	public int getStatus() { 
		this.verifyStandaloneOrLockHeld("getStatus", true);
		return this.status;
	}

	public void setDeviceId(String _v_) { 
		this.verifyStandaloneOrLockHeld("setDeviceId", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "deviceId");
		this.deviceId = _v_;
	}

	public void setAppCode(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAppCode", false);
		limax.zdb.Logs.logObject(this, "appCode");
		this.appCode = _v_;
	}

	public void setAccountId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAccountId", false);
		limax.zdb.Logs.logObject(this, "accountId");
		this.accountId = _v_;
	}

	public void setOpenid(String _v_) { 
		this.verifyStandaloneOrLockHeld("setOpenid", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "openid");
		this.openid = _v_;
	}

	public void setNickname(String _v_) { 
		this.verifyStandaloneOrLockHeld("setNickname", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "nickname");
		this.nickname = _v_;
	}

	public void setStatus(int _v_) { 
		this.verifyStandaloneOrLockHeld("setStatus", false);
		limax.zdb.Logs.logObject(this, "status");
		this.status = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		JidBindRecordBean _o_ = null;
		if ( _o1_ instanceof JidBindRecordBean ) _o_ = (JidBindRecordBean)_o1_;
		else return false;
		if (!this.deviceId.equals(_o_.deviceId)) return false;
		if (this.appCode != _o_.appCode) return false;
		if (this.accountId != _o_.accountId) return false;
		if (!this.openid.equals(_o_.openid)) return false;
		if (!this.nickname.equals(_o_.nickname)) return false;
		if (this.status != _o_.status) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.deviceId.hashCode();
		_h_ += _h_ * 31 + this.appCode;
		_h_ += _h_ * 31 + this.accountId;
		_h_ += _h_ * 31 + this.openid.hashCode();
		_h_ += _h_ * 31 + this.nickname.hashCode();
		_h_ += _h_ * 31 + this.status;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append("T").append(this.deviceId.length()).append(",");
		_sb_.append(this.appCode).append(",");
		_sb_.append(this.accountId).append(",");
		_sb_.append("T").append(this.openid.length()).append(",");
		_sb_.append("T").append(this.nickname.length()).append(",");
		_sb_.append(this.status).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
