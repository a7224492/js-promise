package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoleClubInfo extends limax.zdb.XBean {
    private int clubId; 
    private String invitationCode; 

    RoleClubInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        invitationCode = "";
	}

	public RoleClubInfo() {
		this(null, null);
	}

	public RoleClubInfo(RoleClubInfo _o_) {
		this(_o_, null, null);
	}

	RoleClubInfo(RoleClubInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoleClubInfo", true);
        this.clubId = _o_.clubId;
        this.invitationCode = _o_.invitationCode;
	}

	public void copyFrom(RoleClubInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoleClubInfo", true);
		this.verifyStandaloneOrLockHeld("copyToRoleClubInfo", false);
        limax.zdb.Logs.logObject(this, "clubId");
        this.clubId = _o_.clubId;
        limax.zdb.Logs.logObject(this, "invitationCode");
        this.invitationCode = _o_.invitationCode;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.clubId);
        _os_.marshal(this.invitationCode);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.clubId = _os_.unmarshal_int();
		this.invitationCode = _os_.unmarshal_String();
		return _os_;
	}

	public int getClubId() { 
		this.verifyStandaloneOrLockHeld("getClubId", true);
		return this.clubId;
	}

	public String getInvitationCode() { 
		this.verifyStandaloneOrLockHeld("getInvitationCode", true);
		return this.invitationCode;
	}

	public void setClubId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setClubId", false);
		limax.zdb.Logs.logObject(this, "clubId");
		this.clubId = _v_;
	}

	public void setInvitationCode(String _v_) { 
		this.verifyStandaloneOrLockHeld("setInvitationCode", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "invitationCode");
		this.invitationCode = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoleClubInfo _o_ = null;
		if ( _o1_ instanceof RoleClubInfo ) _o_ = (RoleClubInfo)_o1_;
		else return false;
		if (this.clubId != _o_.clubId) return false;
		if (!this.invitationCode.equals(_o_.invitationCode)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.clubId;
		_h_ += _h_ * 31 + this.invitationCode.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.clubId).append(",");
		_sb_.append("T").append(this.invitationCode.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
