package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ApplicantInfo extends limax.zdb.XBean {
    private xbean.ClubRoleBaseInfo role; 
    private xbean.ClubRoleBaseInfo inviter; 
    private long applyTimestamp; 
    private int gameCount; 
    private String inviterIcon; 

    ApplicantInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        role = new ClubRoleBaseInfo(this, "role");
        inviter = new ClubRoleBaseInfo(this, "inviter");
        inviterIcon = "";
	}

	public ApplicantInfo() {
		this(null, null);
	}

	public ApplicantInfo(ApplicantInfo _o_) {
		this(_o_, null, null);
	}

	ApplicantInfo(ApplicantInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ApplicantInfo", true);
        this.role = new ClubRoleBaseInfo(_o_.role, this, "role");
        this.inviter = new ClubRoleBaseInfo(_o_.inviter, this, "inviter");
        this.applyTimestamp = _o_.applyTimestamp;
        this.gameCount = _o_.gameCount;
        this.inviterIcon = _o_.inviterIcon;
	}

	public void copyFrom(ApplicantInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromApplicantInfo", true);
		this.verifyStandaloneOrLockHeld("copyToApplicantInfo", false);
        this.role.copyFrom(_o_.role);
        this.inviter.copyFrom(_o_.inviter);
        limax.zdb.Logs.logObject(this, "applyTimestamp");
        this.applyTimestamp = _o_.applyTimestamp;
        limax.zdb.Logs.logObject(this, "gameCount");
        this.gameCount = _o_.gameCount;
        limax.zdb.Logs.logObject(this, "inviterIcon");
        this.inviterIcon = _o_.inviterIcon;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.role);
        _os_.marshal(this.inviter);
        _os_.marshal(this.applyTimestamp);
        _os_.marshal(this.gameCount);
        _os_.marshal(this.inviterIcon);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.role.unmarshal(_os_);
		this.inviter.unmarshal(_os_);
		this.applyTimestamp = _os_.unmarshal_long();
		this.gameCount = _os_.unmarshal_int();
		this.inviterIcon = _os_.unmarshal_String();
		return _os_;
	}

	public xbean.ClubRoleBaseInfo getRole() { 
		this.verifyStandaloneOrLockHeld("getRole", true);
		return this.role;
	}

	public xbean.ClubRoleBaseInfo getInviter() { 
		this.verifyStandaloneOrLockHeld("getInviter", true);
		return this.inviter;
	}

	public long getApplyTimestamp() { 
		this.verifyStandaloneOrLockHeld("getApplyTimestamp", true);
		return this.applyTimestamp;
	}

	public int getGameCount() { 
		this.verifyStandaloneOrLockHeld("getGameCount", true);
		return this.gameCount;
	}

	public String getInviterIcon() { 
		this.verifyStandaloneOrLockHeld("getInviterIcon", true);
		return this.inviterIcon;
	}

	public void setApplyTimestamp(long _v_) { 
		this.verifyStandaloneOrLockHeld("setApplyTimestamp", false);
		limax.zdb.Logs.logObject(this, "applyTimestamp");
		this.applyTimestamp = _v_;
	}

	public void setGameCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setGameCount", false);
		limax.zdb.Logs.logObject(this, "gameCount");
		this.gameCount = _v_;
	}

	public void setInviterIcon(String _v_) { 
		this.verifyStandaloneOrLockHeld("setInviterIcon", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "inviterIcon");
		this.inviterIcon = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ApplicantInfo _o_ = null;
		if ( _o1_ instanceof ApplicantInfo ) _o_ = (ApplicantInfo)_o1_;
		else return false;
		if (!this.role.equals(_o_.role)) return false;
		if (!this.inviter.equals(_o_.inviter)) return false;
		if (this.applyTimestamp != _o_.applyTimestamp) return false;
		if (this.gameCount != _o_.gameCount) return false;
		if (!this.inviterIcon.equals(_o_.inviterIcon)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.role.hashCode();
		_h_ += _h_ * 31 + this.inviter.hashCode();
		_h_ += _h_ * 31 + (int)(this.applyTimestamp ^ (this.applyTimestamp >>> 32));
		_h_ += _h_ * 31 + this.gameCount;
		_h_ += _h_ * 31 + this.inviterIcon.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.role).append(",");
		_sb_.append(this.inviter).append(",");
		_sb_.append(this.applyTimestamp).append(",");
		_sb_.append(this.gameCount).append(",");
		_sb_.append("T").append(this.inviterIcon.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
