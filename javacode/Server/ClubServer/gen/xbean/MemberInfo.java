package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class MemberInfo extends limax.zdb.XBean {
    private xbean.ClubRoleBaseInfo role; 
    private int cardCount; 
    private xbean.ClubRoleBaseInfo inviter; 
    private long joinTimestamp; 
    private int status; 
    private int totalGameCount; 
    private int todayGameCount; 

    MemberInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        role = new ClubRoleBaseInfo(this, "role");
        inviter = new ClubRoleBaseInfo(this, "inviter");
	}

	public MemberInfo() {
		this(null, null);
	}

	public MemberInfo(MemberInfo _o_) {
		this(_o_, null, null);
	}

	MemberInfo(MemberInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.MemberInfo", true);
        this.role = new ClubRoleBaseInfo(_o_.role, this, "role");
        this.cardCount = _o_.cardCount;
        this.inviter = new ClubRoleBaseInfo(_o_.inviter, this, "inviter");
        this.joinTimestamp = _o_.joinTimestamp;
        this.status = _o_.status;
        this.totalGameCount = _o_.totalGameCount;
        this.todayGameCount = _o_.todayGameCount;
	}

	public void copyFrom(MemberInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromMemberInfo", true);
		this.verifyStandaloneOrLockHeld("copyToMemberInfo", false);
        this.role.copyFrom(_o_.role);
        limax.zdb.Logs.logObject(this, "cardCount");
        this.cardCount = _o_.cardCount;
        this.inviter.copyFrom(_o_.inviter);
        limax.zdb.Logs.logObject(this, "joinTimestamp");
        this.joinTimestamp = _o_.joinTimestamp;
        limax.zdb.Logs.logObject(this, "status");
        this.status = _o_.status;
        limax.zdb.Logs.logObject(this, "totalGameCount");
        this.totalGameCount = _o_.totalGameCount;
        limax.zdb.Logs.logObject(this, "todayGameCount");
        this.todayGameCount = _o_.todayGameCount;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.role);
        _os_.marshal(this.cardCount);
        _os_.marshal(this.inviter);
        _os_.marshal(this.joinTimestamp);
        _os_.marshal(this.status);
        _os_.marshal(this.totalGameCount);
        _os_.marshal(this.todayGameCount);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.role.unmarshal(_os_);
		this.cardCount = _os_.unmarshal_int();
		this.inviter.unmarshal(_os_);
		this.joinTimestamp = _os_.unmarshal_long();
		this.status = _os_.unmarshal_int();
		this.totalGameCount = _os_.unmarshal_int();
		this.todayGameCount = _os_.unmarshal_int();
		return _os_;
	}

	public xbean.ClubRoleBaseInfo getRole() { 
		this.verifyStandaloneOrLockHeld("getRole", true);
		return this.role;
	}

	public int getCardCount() { 
		this.verifyStandaloneOrLockHeld("getCardCount", true);
		return this.cardCount;
	}

	public xbean.ClubRoleBaseInfo getInviter() { 
		this.verifyStandaloneOrLockHeld("getInviter", true);
		return this.inviter;
	}

	public long getJoinTimestamp() { 
		this.verifyStandaloneOrLockHeld("getJoinTimestamp", true);
		return this.joinTimestamp;
	}

	public int getStatus() { 
		this.verifyStandaloneOrLockHeld("getStatus", true);
		return this.status;
	}

	public int getTotalGameCount() { 
		this.verifyStandaloneOrLockHeld("getTotalGameCount", true);
		return this.totalGameCount;
	}

	public int getTodayGameCount() { 
		this.verifyStandaloneOrLockHeld("getTodayGameCount", true);
		return this.todayGameCount;
	}

	public void setCardCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setCardCount", false);
		limax.zdb.Logs.logObject(this, "cardCount");
		this.cardCount = _v_;
	}

	public void setJoinTimestamp(long _v_) { 
		this.verifyStandaloneOrLockHeld("setJoinTimestamp", false);
		limax.zdb.Logs.logObject(this, "joinTimestamp");
		this.joinTimestamp = _v_;
	}

	public void setStatus(int _v_) { 
		this.verifyStandaloneOrLockHeld("setStatus", false);
		limax.zdb.Logs.logObject(this, "status");
		this.status = _v_;
	}

	public void setTotalGameCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setTotalGameCount", false);
		limax.zdb.Logs.logObject(this, "totalGameCount");
		this.totalGameCount = _v_;
	}

	public void setTodayGameCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setTodayGameCount", false);
		limax.zdb.Logs.logObject(this, "todayGameCount");
		this.todayGameCount = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		MemberInfo _o_ = null;
		if ( _o1_ instanceof MemberInfo ) _o_ = (MemberInfo)_o1_;
		else return false;
		if (!this.role.equals(_o_.role)) return false;
		if (this.cardCount != _o_.cardCount) return false;
		if (!this.inviter.equals(_o_.inviter)) return false;
		if (this.joinTimestamp != _o_.joinTimestamp) return false;
		if (this.status != _o_.status) return false;
		if (this.totalGameCount != _o_.totalGameCount) return false;
		if (this.todayGameCount != _o_.todayGameCount) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.role.hashCode();
		_h_ += _h_ * 31 + this.cardCount;
		_h_ += _h_ * 31 + this.inviter.hashCode();
		_h_ += _h_ * 31 + (int)(this.joinTimestamp ^ (this.joinTimestamp >>> 32));
		_h_ += _h_ * 31 + this.status;
		_h_ += _h_ * 31 + this.totalGameCount;
		_h_ += _h_ * 31 + this.todayGameCount;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.role).append(",");
		_sb_.append(this.cardCount).append(",");
		_sb_.append(this.inviter).append(",");
		_sb_.append(this.joinTimestamp).append(",");
		_sb_.append(this.status).append(",");
		_sb_.append(this.totalGameCount).append(",");
		_sb_.append(this.todayGameCount).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
