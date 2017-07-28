package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoleClubs extends limax.zdb.XBean {
    private java.util.ArrayList<xbean.RoleClubInfo> clubs; 
    private String app_key; 
    private String version; 
    private String channel; 

    RoleClubs(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        clubs = new java.util.ArrayList<xbean.RoleClubInfo>();
        app_key = "";
        version = "";
        channel = "";
	}

	public RoleClubs() {
		this(null, null);
	}

	public RoleClubs(RoleClubs _o_) {
		this(_o_, null, null);
	}

	RoleClubs(RoleClubs _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoleClubs", true);
        this.clubs = new java.util.ArrayList<xbean.RoleClubInfo>();
        _o_.clubs.forEach(_v_ -> this.clubs.add(new RoleClubInfo(_v_, this, "clubs")));
        this.app_key = _o_.app_key;
        this.version = _o_.version;
        this.channel = _o_.channel;
	}

	public void copyFrom(RoleClubs _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoleClubs", true);
		this.verifyStandaloneOrLockHeld("copyToRoleClubs", false);
        java.util.List<xbean.RoleClubInfo> this_clubs = limax.zdb.Logs.logList(this, "clubs", ()->{});
        this_clubs.clear();
        _o_.clubs.forEach(_v_ -> this_clubs.add(new RoleClubInfo(_v_)));
        limax.zdb.Logs.logObject(this, "app_key");
        this.app_key = _o_.app_key;
        limax.zdb.Logs.logObject(this, "version");
        this.version = _o_.version;
        limax.zdb.Logs.logObject(this, "channel");
        this.channel = _o_.channel;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.clubs.size());
        for (xbean.RoleClubInfo _v_ : this.clubs) {
        	_os_.marshal(_v_);
        }
        _os_.marshal(this.app_key);
        _os_.marshal(this.version);
        _os_.marshal(this.channel);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.RoleClubInfo _v_ = new RoleClubInfo(this, "clubs");
			_v_.unmarshal(_os_);
			this.clubs.add(_v_);
		}
		this.app_key = _os_.unmarshal_String();
		this.version = _os_.unmarshal_String();
		this.channel = _os_.unmarshal_String();
		return _os_;
	}

	public java.util.List<xbean.RoleClubInfo> getClubs() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "clubs", this.verifyStandaloneOrLockHeld("getClubs", true)) : this.clubs;
	}

	public String getApp_key() { 
		this.verifyStandaloneOrLockHeld("getApp_key", true);
		return this.app_key;
	}

	public String getVersion() { 
		this.verifyStandaloneOrLockHeld("getVersion", true);
		return this.version;
	}

	public String getChannel() { 
		this.verifyStandaloneOrLockHeld("getChannel", true);
		return this.channel;
	}

	public void setApp_key(String _v_) { 
		this.verifyStandaloneOrLockHeld("setApp_key", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "app_key");
		this.app_key = _v_;
	}

	public void setVersion(String _v_) { 
		this.verifyStandaloneOrLockHeld("setVersion", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "version");
		this.version = _v_;
	}

	public void setChannel(String _v_) { 
		this.verifyStandaloneOrLockHeld("setChannel", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "channel");
		this.channel = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoleClubs _o_ = null;
		if ( _o1_ instanceof RoleClubs ) _o_ = (RoleClubs)_o1_;
		else return false;
		if (!this.clubs.equals(_o_.clubs)) return false;
		if (!this.app_key.equals(_o_.app_key)) return false;
		if (!this.version.equals(_o_.version)) return false;
		if (!this.channel.equals(_o_.channel)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.clubs.hashCode();
		_h_ += _h_ * 31 + this.app_key.hashCode();
		_h_ += _h_ * 31 + this.version.hashCode();
		_h_ += _h_ * 31 + this.channel.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.clubs).append(",");
		_sb_.append("T").append(this.app_key.length()).append(",");
		_sb_.append("T").append(this.version.length()).append(",");
		_sb_.append("T").append(this.channel.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
