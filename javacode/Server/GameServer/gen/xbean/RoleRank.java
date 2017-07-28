package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoleRank extends limax.zdb.XBean {
    private int roleId; 
    private String nickname; 
    private int score; 

    RoleRank(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        nickname = "";
	}

	public RoleRank() {
		this(null, null);
	}

	public RoleRank(RoleRank _o_) {
		this(_o_, null, null);
	}

	RoleRank(RoleRank _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoleRank", true);
        this.roleId = _o_.roleId;
        this.nickname = _o_.nickname;
        this.score = _o_.score;
	}

	public void copyFrom(RoleRank _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoleRank", true);
		this.verifyStandaloneOrLockHeld("copyToRoleRank", false);
        limax.zdb.Logs.logObject(this, "roleId");
        this.roleId = _o_.roleId;
        limax.zdb.Logs.logObject(this, "nickname");
        this.nickname = _o_.nickname;
        limax.zdb.Logs.logObject(this, "score");
        this.score = _o_.score;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.roleId);
        _os_.marshal(this.nickname);
        _os_.marshal(this.score);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.roleId = _os_.unmarshal_int();
		this.nickname = _os_.unmarshal_String();
		this.score = _os_.unmarshal_int();
		return _os_;
	}

	public int getRoleId() { 
		this.verifyStandaloneOrLockHeld("getRoleId", true);
		return this.roleId;
	}

	public String getNickname() { 
		this.verifyStandaloneOrLockHeld("getNickname", true);
		return this.nickname;
	}

	public int getScore() { 
		this.verifyStandaloneOrLockHeld("getScore", true);
		return this.score;
	}

	public void setRoleId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoleId", false);
		limax.zdb.Logs.logObject(this, "roleId");
		this.roleId = _v_;
	}

	public void setNickname(String _v_) { 
		this.verifyStandaloneOrLockHeld("setNickname", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "nickname");
		this.nickname = _v_;
	}

	public void setScore(int _v_) { 
		this.verifyStandaloneOrLockHeld("setScore", false);
		limax.zdb.Logs.logObject(this, "score");
		this.score = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoleRank _o_ = null;
		if ( _o1_ instanceof RoleRank ) _o_ = (RoleRank)_o1_;
		else return false;
		if (this.roleId != _o_.roleId) return false;
		if (!this.nickname.equals(_o_.nickname)) return false;
		if (this.score != _o_.score) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roleId;
		_h_ += _h_ * 31 + this.nickname.hashCode();
		_h_ += _h_ * 31 + this.score;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roleId).append(",");
		_sb_.append("T").append(this.nickname.length()).append(",");
		_sb_.append(this.score).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
