package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ClubInfo extends limax.zdb.XBean {
    private int clubId; 
    private String clubName; 
    private String notice; 
    private long noticeTime; 
    private long createTimestamp; 
    private xbean.ClubRoleBaseInfo creator; 
    private xbean.ClubRoleBaseInfo manager; 
    private int level; 
    private long gameCount; 
    private long todayGameCount; 
    private long todayClearTime; 
    private xbean.RoomCost roomCost; 
    private java.util.ArrayList<xbean.MemberInfo> members; 
    private java.util.ArrayList<xbean.ApplicantInfo> applicants; 
    private int status; 
    private int agentId; 
    private int memberCount; 

    ClubInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        clubName = "";
        notice = "";
        creator = new ClubRoleBaseInfo(this, "creator");
        manager = new ClubRoleBaseInfo(this, "manager");
        roomCost = new RoomCost(this, "roomCost");
        members = new java.util.ArrayList<xbean.MemberInfo>();
        applicants = new java.util.ArrayList<xbean.ApplicantInfo>();
	}

	public ClubInfo() {
		this(null, null);
	}

	public ClubInfo(ClubInfo _o_) {
		this(_o_, null, null);
	}

	ClubInfo(ClubInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ClubInfo", true);
        this.clubId = _o_.clubId;
        this.clubName = _o_.clubName;
        this.notice = _o_.notice;
        this.noticeTime = _o_.noticeTime;
        this.createTimestamp = _o_.createTimestamp;
        this.creator = new ClubRoleBaseInfo(_o_.creator, this, "creator");
        this.manager = new ClubRoleBaseInfo(_o_.manager, this, "manager");
        this.level = _o_.level;
        this.gameCount = _o_.gameCount;
        this.todayGameCount = _o_.todayGameCount;
        this.todayClearTime = _o_.todayClearTime;
        this.roomCost = new RoomCost(_o_.roomCost, this, "roomCost");
        this.members = new java.util.ArrayList<xbean.MemberInfo>();
        _o_.members.forEach(_v_ -> this.members.add(new MemberInfo(_v_, this, "members")));
        this.applicants = new java.util.ArrayList<xbean.ApplicantInfo>();
        _o_.applicants.forEach(_v_ -> this.applicants.add(new ApplicantInfo(_v_, this, "applicants")));
        this.status = _o_.status;
        this.agentId = _o_.agentId;
        this.memberCount = _o_.memberCount;
	}

	public void copyFrom(ClubInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromClubInfo", true);
		this.verifyStandaloneOrLockHeld("copyToClubInfo", false);
        limax.zdb.Logs.logObject(this, "clubId");
        this.clubId = _o_.clubId;
        limax.zdb.Logs.logObject(this, "clubName");
        this.clubName = _o_.clubName;
        limax.zdb.Logs.logObject(this, "notice");
        this.notice = _o_.notice;
        limax.zdb.Logs.logObject(this, "noticeTime");
        this.noticeTime = _o_.noticeTime;
        limax.zdb.Logs.logObject(this, "createTimestamp");
        this.createTimestamp = _o_.createTimestamp;
        this.creator.copyFrom(_o_.creator);
        this.manager.copyFrom(_o_.manager);
        limax.zdb.Logs.logObject(this, "level");
        this.level = _o_.level;
        limax.zdb.Logs.logObject(this, "gameCount");
        this.gameCount = _o_.gameCount;
        limax.zdb.Logs.logObject(this, "todayGameCount");
        this.todayGameCount = _o_.todayGameCount;
        limax.zdb.Logs.logObject(this, "todayClearTime");
        this.todayClearTime = _o_.todayClearTime;
        this.roomCost.copyFrom(_o_.roomCost);
        java.util.List<xbean.MemberInfo> this_members = limax.zdb.Logs.logList(this, "members", ()->{});
        this_members.clear();
        _o_.members.forEach(_v_ -> this_members.add(new MemberInfo(_v_)));
        java.util.List<xbean.ApplicantInfo> this_applicants = limax.zdb.Logs.logList(this, "applicants", ()->{});
        this_applicants.clear();
        _o_.applicants.forEach(_v_ -> this_applicants.add(new ApplicantInfo(_v_)));
        limax.zdb.Logs.logObject(this, "status");
        this.status = _o_.status;
        limax.zdb.Logs.logObject(this, "agentId");
        this.agentId = _o_.agentId;
        limax.zdb.Logs.logObject(this, "memberCount");
        this.memberCount = _o_.memberCount;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.clubId);
        _os_.marshal(this.clubName);
        _os_.marshal(this.notice);
        _os_.marshal(this.noticeTime);
        _os_.marshal(this.createTimestamp);
        _os_.marshal(this.creator);
        _os_.marshal(this.manager);
        _os_.marshal(this.level);
        _os_.marshal(this.gameCount);
        _os_.marshal(this.todayGameCount);
        _os_.marshal(this.todayClearTime);
        _os_.marshal(this.roomCost);
        _os_.marshal_size(this.members.size());
        for (xbean.MemberInfo _v_ : this.members) {
        	_os_.marshal(_v_);
        }
        _os_.marshal_size(this.applicants.size());
        for (xbean.ApplicantInfo _v_ : this.applicants) {
        	_os_.marshal(_v_);
        }
        _os_.marshal(this.status);
        _os_.marshal(this.agentId);
        _os_.marshal(this.memberCount);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.clubId = _os_.unmarshal_int();
		this.clubName = _os_.unmarshal_String();
		this.notice = _os_.unmarshal_String();
		this.noticeTime = _os_.unmarshal_long();
		this.createTimestamp = _os_.unmarshal_long();
		this.creator.unmarshal(_os_);
		this.manager.unmarshal(_os_);
		this.level = _os_.unmarshal_int();
		this.gameCount = _os_.unmarshal_long();
		this.todayGameCount = _os_.unmarshal_long();
		this.todayClearTime = _os_.unmarshal_long();
		this.roomCost.unmarshal(_os_);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.MemberInfo _v_ = new MemberInfo(this, "members");
			_v_.unmarshal(_os_);
			this.members.add(_v_);
		}
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.ApplicantInfo _v_ = new ApplicantInfo(this, "applicants");
			_v_.unmarshal(_os_);
			this.applicants.add(_v_);
		}
		this.status = _os_.unmarshal_int();
		this.agentId = _os_.unmarshal_int();
		this.memberCount = _os_.unmarshal_int();
		return _os_;
	}

	public int getClubId() { 
		this.verifyStandaloneOrLockHeld("getClubId", true);
		return this.clubId;
	}

	public String getClubName() { 
		this.verifyStandaloneOrLockHeld("getClubName", true);
		return this.clubName;
	}

	public String getNotice() { 
		this.verifyStandaloneOrLockHeld("getNotice", true);
		return this.notice;
	}

	public long getNoticeTime() { 
		this.verifyStandaloneOrLockHeld("getNoticeTime", true);
		return this.noticeTime;
	}

	public long getCreateTimestamp() { 
		this.verifyStandaloneOrLockHeld("getCreateTimestamp", true);
		return this.createTimestamp;
	}

	public xbean.ClubRoleBaseInfo getCreator() { 
		this.verifyStandaloneOrLockHeld("getCreator", true);
		return this.creator;
	}

	public xbean.ClubRoleBaseInfo getManager() { 
		this.verifyStandaloneOrLockHeld("getManager", true);
		return this.manager;
	}

	public int getLevel() { 
		this.verifyStandaloneOrLockHeld("getLevel", true);
		return this.level;
	}

	public long getGameCount() { 
		this.verifyStandaloneOrLockHeld("getGameCount", true);
		return this.gameCount;
	}

	public long getTodayGameCount() { 
		this.verifyStandaloneOrLockHeld("getTodayGameCount", true);
		return this.todayGameCount;
	}

	public long getTodayClearTime() { 
		this.verifyStandaloneOrLockHeld("getTodayClearTime", true);
		return this.todayClearTime;
	}

	public xbean.RoomCost getRoomCost() { 
		this.verifyStandaloneOrLockHeld("getRoomCost", true);
		return this.roomCost;
	}

	public java.util.List<xbean.MemberInfo> getMembers() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "members", this.verifyStandaloneOrLockHeld("getMembers", true)) : this.members;
	}

	public java.util.List<xbean.ApplicantInfo> getApplicants() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "applicants", this.verifyStandaloneOrLockHeld("getApplicants", true)) : this.applicants;
	}

	public int getStatus() { 
		this.verifyStandaloneOrLockHeld("getStatus", true);
		return this.status;
	}

	public int getAgentId() { 
		this.verifyStandaloneOrLockHeld("getAgentId", true);
		return this.agentId;
	}

	public int getMemberCount() { 
		this.verifyStandaloneOrLockHeld("getMemberCount", true);
		return this.memberCount;
	}

	public void setClubId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setClubId", false);
		limax.zdb.Logs.logObject(this, "clubId");
		this.clubId = _v_;
	}

	public void setClubName(String _v_) { 
		this.verifyStandaloneOrLockHeld("setClubName", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "clubName");
		this.clubName = _v_;
	}

	public void setNotice(String _v_) { 
		this.verifyStandaloneOrLockHeld("setNotice", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "notice");
		this.notice = _v_;
	}

	public void setNoticeTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setNoticeTime", false);
		limax.zdb.Logs.logObject(this, "noticeTime");
		this.noticeTime = _v_;
	}

	public void setCreateTimestamp(long _v_) { 
		this.verifyStandaloneOrLockHeld("setCreateTimestamp", false);
		limax.zdb.Logs.logObject(this, "createTimestamp");
		this.createTimestamp = _v_;
	}

	public void setLevel(int _v_) { 
		this.verifyStandaloneOrLockHeld("setLevel", false);
		limax.zdb.Logs.logObject(this, "level");
		this.level = _v_;
	}

	public void setGameCount(long _v_) { 
		this.verifyStandaloneOrLockHeld("setGameCount", false);
		limax.zdb.Logs.logObject(this, "gameCount");
		this.gameCount = _v_;
	}

	public void setTodayGameCount(long _v_) { 
		this.verifyStandaloneOrLockHeld("setTodayGameCount", false);
		limax.zdb.Logs.logObject(this, "todayGameCount");
		this.todayGameCount = _v_;
	}

	public void setTodayClearTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setTodayClearTime", false);
		limax.zdb.Logs.logObject(this, "todayClearTime");
		this.todayClearTime = _v_;
	}

	public void setStatus(int _v_) { 
		this.verifyStandaloneOrLockHeld("setStatus", false);
		limax.zdb.Logs.logObject(this, "status");
		this.status = _v_;
	}

	public void setAgentId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAgentId", false);
		limax.zdb.Logs.logObject(this, "agentId");
		this.agentId = _v_;
	}

	public void setMemberCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setMemberCount", false);
		limax.zdb.Logs.logObject(this, "memberCount");
		this.memberCount = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ClubInfo _o_ = null;
		if ( _o1_ instanceof ClubInfo ) _o_ = (ClubInfo)_o1_;
		else return false;
		if (this.clubId != _o_.clubId) return false;
		if (!this.clubName.equals(_o_.clubName)) return false;
		if (!this.notice.equals(_o_.notice)) return false;
		if (this.noticeTime != _o_.noticeTime) return false;
		if (this.createTimestamp != _o_.createTimestamp) return false;
		if (!this.creator.equals(_o_.creator)) return false;
		if (!this.manager.equals(_o_.manager)) return false;
		if (this.level != _o_.level) return false;
		if (this.gameCount != _o_.gameCount) return false;
		if (this.todayGameCount != _o_.todayGameCount) return false;
		if (this.todayClearTime != _o_.todayClearTime) return false;
		if (!this.roomCost.equals(_o_.roomCost)) return false;
		if (!this.members.equals(_o_.members)) return false;
		if (!this.applicants.equals(_o_.applicants)) return false;
		if (this.status != _o_.status) return false;
		if (this.agentId != _o_.agentId) return false;
		if (this.memberCount != _o_.memberCount) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.clubId;
		_h_ += _h_ * 31 + this.clubName.hashCode();
		_h_ += _h_ * 31 + this.notice.hashCode();
		_h_ += _h_ * 31 + (int)(this.noticeTime ^ (this.noticeTime >>> 32));
		_h_ += _h_ * 31 + (int)(this.createTimestamp ^ (this.createTimestamp >>> 32));
		_h_ += _h_ * 31 + this.creator.hashCode();
		_h_ += _h_ * 31 + this.manager.hashCode();
		_h_ += _h_ * 31 + this.level;
		_h_ += _h_ * 31 + (int)(this.gameCount ^ (this.gameCount >>> 32));
		_h_ += _h_ * 31 + (int)(this.todayGameCount ^ (this.todayGameCount >>> 32));
		_h_ += _h_ * 31 + (int)(this.todayClearTime ^ (this.todayClearTime >>> 32));
		_h_ += _h_ * 31 + this.roomCost.hashCode();
		_h_ += _h_ * 31 + this.members.hashCode();
		_h_ += _h_ * 31 + this.applicants.hashCode();
		_h_ += _h_ * 31 + this.status;
		_h_ += _h_ * 31 + this.agentId;
		_h_ += _h_ * 31 + this.memberCount;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.clubId).append(",");
		_sb_.append("T").append(this.clubName.length()).append(",");
		_sb_.append("T").append(this.notice.length()).append(",");
		_sb_.append(this.noticeTime).append(",");
		_sb_.append(this.createTimestamp).append(",");
		_sb_.append(this.creator).append(",");
		_sb_.append(this.manager).append(",");
		_sb_.append(this.level).append(",");
		_sb_.append(this.gameCount).append(",");
		_sb_.append(this.todayGameCount).append(",");
		_sb_.append(this.todayClearTime).append(",");
		_sb_.append(this.roomCost).append(",");
		_sb_.append(this.members).append(",");
		_sb_.append(this.applicants).append(",");
		_sb_.append(this.status).append(",");
		_sb_.append(this.agentId).append(",");
		_sb_.append(this.memberCount).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
