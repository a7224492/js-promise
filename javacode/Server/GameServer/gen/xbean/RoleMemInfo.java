package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoleMemInfo extends limax.zdb.XBean {
    private int battleServerId; 
    private int roomId; 
    private int connectionId; 
    private long onlineTimeInDay; 
    private int addictionAlertTimesInDay; 
    private long lastAddictionAlertTime; 
    private long lastLogoutTime; 
    private long serverStartupTime; 

    RoleMemInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public RoleMemInfo() {
		this(null, null);
	}

	public RoleMemInfo(RoleMemInfo _o_) {
		this(_o_, null, null);
	}

	RoleMemInfo(RoleMemInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoleMemInfo", true);
        this.battleServerId = _o_.battleServerId;
        this.roomId = _o_.roomId;
        this.connectionId = _o_.connectionId;
        this.onlineTimeInDay = _o_.onlineTimeInDay;
        this.addictionAlertTimesInDay = _o_.addictionAlertTimesInDay;
        this.lastAddictionAlertTime = _o_.lastAddictionAlertTime;
        this.lastLogoutTime = _o_.lastLogoutTime;
        this.serverStartupTime = _o_.serverStartupTime;
	}

	public void copyFrom(RoleMemInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoleMemInfo", true);
		this.verifyStandaloneOrLockHeld("copyToRoleMemInfo", false);
        limax.zdb.Logs.logObject(this, "battleServerId");
        this.battleServerId = _o_.battleServerId;
        limax.zdb.Logs.logObject(this, "roomId");
        this.roomId = _o_.roomId;
        limax.zdb.Logs.logObject(this, "connectionId");
        this.connectionId = _o_.connectionId;
        limax.zdb.Logs.logObject(this, "onlineTimeInDay");
        this.onlineTimeInDay = _o_.onlineTimeInDay;
        limax.zdb.Logs.logObject(this, "addictionAlertTimesInDay");
        this.addictionAlertTimesInDay = _o_.addictionAlertTimesInDay;
        limax.zdb.Logs.logObject(this, "lastAddictionAlertTime");
        this.lastAddictionAlertTime = _o_.lastAddictionAlertTime;
        limax.zdb.Logs.logObject(this, "lastLogoutTime");
        this.lastLogoutTime = _o_.lastLogoutTime;
        limax.zdb.Logs.logObject(this, "serverStartupTime");
        this.serverStartupTime = _o_.serverStartupTime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.battleServerId);
        _os_.marshal(this.roomId);
        _os_.marshal(this.connectionId);
        _os_.marshal(this.onlineTimeInDay);
        _os_.marshal(this.addictionAlertTimesInDay);
        _os_.marshal(this.lastAddictionAlertTime);
        _os_.marshal(this.lastLogoutTime);
        _os_.marshal(this.serverStartupTime);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.battleServerId = _os_.unmarshal_int();
		this.roomId = _os_.unmarshal_int();
		this.connectionId = _os_.unmarshal_int();
		this.onlineTimeInDay = _os_.unmarshal_long();
		this.addictionAlertTimesInDay = _os_.unmarshal_int();
		this.lastAddictionAlertTime = _os_.unmarshal_long();
		this.lastLogoutTime = _os_.unmarshal_long();
		this.serverStartupTime = _os_.unmarshal_long();
		return _os_;
	}

	public int getBattleServerId() { 
		this.verifyStandaloneOrLockHeld("getBattleServerId", true);
		return this.battleServerId;
	}

	public int getRoomId() { 
		this.verifyStandaloneOrLockHeld("getRoomId", true);
		return this.roomId;
	}

	public int getConnectionId() { 
		this.verifyStandaloneOrLockHeld("getConnectionId", true);
		return this.connectionId;
	}

	public long getOnlineTimeInDay() { 
		this.verifyStandaloneOrLockHeld("getOnlineTimeInDay", true);
		return this.onlineTimeInDay;
	}

	public int getAddictionAlertTimesInDay() { 
		this.verifyStandaloneOrLockHeld("getAddictionAlertTimesInDay", true);
		return this.addictionAlertTimesInDay;
	}

	public long getLastAddictionAlertTime() { 
		this.verifyStandaloneOrLockHeld("getLastAddictionAlertTime", true);
		return this.lastAddictionAlertTime;
	}

	public long getLastLogoutTime() { 
		this.verifyStandaloneOrLockHeld("getLastLogoutTime", true);
		return this.lastLogoutTime;
	}

	public long getServerStartupTime() { 
		this.verifyStandaloneOrLockHeld("getServerStartupTime", true);
		return this.serverStartupTime;
	}

	public void setBattleServerId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setBattleServerId", false);
		limax.zdb.Logs.logObject(this, "battleServerId");
		this.battleServerId = _v_;
	}

	public void setRoomId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoomId", false);
		limax.zdb.Logs.logObject(this, "roomId");
		this.roomId = _v_;
	}

	public void setConnectionId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setConnectionId", false);
		limax.zdb.Logs.logObject(this, "connectionId");
		this.connectionId = _v_;
	}

	public void setOnlineTimeInDay(long _v_) { 
		this.verifyStandaloneOrLockHeld("setOnlineTimeInDay", false);
		limax.zdb.Logs.logObject(this, "onlineTimeInDay");
		this.onlineTimeInDay = _v_;
	}

	public void setAddictionAlertTimesInDay(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAddictionAlertTimesInDay", false);
		limax.zdb.Logs.logObject(this, "addictionAlertTimesInDay");
		this.addictionAlertTimesInDay = _v_;
	}

	public void setLastAddictionAlertTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setLastAddictionAlertTime", false);
		limax.zdb.Logs.logObject(this, "lastAddictionAlertTime");
		this.lastAddictionAlertTime = _v_;
	}

	public void setLastLogoutTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setLastLogoutTime", false);
		limax.zdb.Logs.logObject(this, "lastLogoutTime");
		this.lastLogoutTime = _v_;
	}

	public void setServerStartupTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setServerStartupTime", false);
		limax.zdb.Logs.logObject(this, "serverStartupTime");
		this.serverStartupTime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoleMemInfo _o_ = null;
		if ( _o1_ instanceof RoleMemInfo ) _o_ = (RoleMemInfo)_o1_;
		else return false;
		if (this.battleServerId != _o_.battleServerId) return false;
		if (this.roomId != _o_.roomId) return false;
		if (this.connectionId != _o_.connectionId) return false;
		if (this.onlineTimeInDay != _o_.onlineTimeInDay) return false;
		if (this.addictionAlertTimesInDay != _o_.addictionAlertTimesInDay) return false;
		if (this.lastAddictionAlertTime != _o_.lastAddictionAlertTime) return false;
		if (this.lastLogoutTime != _o_.lastLogoutTime) return false;
		if (this.serverStartupTime != _o_.serverStartupTime) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.battleServerId;
		_h_ += _h_ * 31 + this.roomId;
		_h_ += _h_ * 31 + this.connectionId;
		_h_ += _h_ * 31 + (int)(this.onlineTimeInDay ^ (this.onlineTimeInDay >>> 32));
		_h_ += _h_ * 31 + this.addictionAlertTimesInDay;
		_h_ += _h_ * 31 + (int)(this.lastAddictionAlertTime ^ (this.lastAddictionAlertTime >>> 32));
		_h_ += _h_ * 31 + (int)(this.lastLogoutTime ^ (this.lastLogoutTime >>> 32));
		_h_ += _h_ * 31 + (int)(this.serverStartupTime ^ (this.serverStartupTime >>> 32));
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.battleServerId).append(",");
		_sb_.append(this.roomId).append(",");
		_sb_.append(this.connectionId).append(",");
		_sb_.append(this.onlineTimeInDay).append(",");
		_sb_.append(this.addictionAlertTimesInDay).append(",");
		_sb_.append(this.lastAddictionAlertTime).append(",");
		_sb_.append(this.lastLogoutTime).append(",");
		_sb_.append(this.serverStartupTime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
