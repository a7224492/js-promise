package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ForbidRole extends limax.zdb.XBean {
    private int accountId; 
    private String channel; 
    private String username; 
    private long forbidTime; 

    ForbidRole(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        channel = "";
        username = "";
	}

	public ForbidRole() {
		this(null, null);
	}

	public ForbidRole(ForbidRole _o_) {
		this(_o_, null, null);
	}

	ForbidRole(ForbidRole _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ForbidRole", true);
        this.accountId = _o_.accountId;
        this.channel = _o_.channel;
        this.username = _o_.username;
        this.forbidTime = _o_.forbidTime;
	}

	public void copyFrom(ForbidRole _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromForbidRole", true);
		this.verifyStandaloneOrLockHeld("copyToForbidRole", false);
        limax.zdb.Logs.logObject(this, "accountId");
        this.accountId = _o_.accountId;
        limax.zdb.Logs.logObject(this, "channel");
        this.channel = _o_.channel;
        limax.zdb.Logs.logObject(this, "username");
        this.username = _o_.username;
        limax.zdb.Logs.logObject(this, "forbidTime");
        this.forbidTime = _o_.forbidTime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.accountId);
        _os_.marshal(this.channel);
        _os_.marshal(this.username);
        _os_.marshal(this.forbidTime);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.accountId = _os_.unmarshal_int();
		this.channel = _os_.unmarshal_String();
		this.username = _os_.unmarshal_String();
		this.forbidTime = _os_.unmarshal_long();
		return _os_;
	}

	public int getAccountId() { 
		this.verifyStandaloneOrLockHeld("getAccountId", true);
		return this.accountId;
	}

	public String getChannel() { 
		this.verifyStandaloneOrLockHeld("getChannel", true);
		return this.channel;
	}

	public String getUsername() { 
		this.verifyStandaloneOrLockHeld("getUsername", true);
		return this.username;
	}

	public long getForbidTime() { 
		this.verifyStandaloneOrLockHeld("getForbidTime", true);
		return this.forbidTime;
	}

	public void setAccountId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAccountId", false);
		limax.zdb.Logs.logObject(this, "accountId");
		this.accountId = _v_;
	}

	public void setChannel(String _v_) { 
		this.verifyStandaloneOrLockHeld("setChannel", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "channel");
		this.channel = _v_;
	}

	public void setUsername(String _v_) { 
		this.verifyStandaloneOrLockHeld("setUsername", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "username");
		this.username = _v_;
	}

	public void setForbidTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setForbidTime", false);
		limax.zdb.Logs.logObject(this, "forbidTime");
		this.forbidTime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ForbidRole _o_ = null;
		if ( _o1_ instanceof ForbidRole ) _o_ = (ForbidRole)_o1_;
		else return false;
		if (this.accountId != _o_.accountId) return false;
		if (!this.channel.equals(_o_.channel)) return false;
		if (!this.username.equals(_o_.username)) return false;
		if (this.forbidTime != _o_.forbidTime) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.accountId;
		_h_ += _h_ * 31 + this.channel.hashCode();
		_h_ += _h_ * 31 + this.username.hashCode();
		_h_ += _h_ * 31 + (int)(this.forbidTime ^ (this.forbidTime >>> 32));
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.accountId).append(",");
		_sb_.append("T").append(this.channel.length()).append(",");
		_sb_.append("T").append(this.username.length()).append(",");
		_sb_.append(this.forbidTime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
