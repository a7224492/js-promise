package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ClubRoleBaseInfo extends limax.zdb.XBean {
    private int roleId; 
    private String name; 
    private int title; 

    ClubRoleBaseInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        name = "";
	}

	public ClubRoleBaseInfo() {
		this(null, null);
	}

	public ClubRoleBaseInfo(ClubRoleBaseInfo _o_) {
		this(_o_, null, null);
	}

	ClubRoleBaseInfo(ClubRoleBaseInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ClubRoleBaseInfo", true);
        this.roleId = _o_.roleId;
        this.name = _o_.name;
        this.title = _o_.title;
	}

	public void copyFrom(ClubRoleBaseInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromClubRoleBaseInfo", true);
		this.verifyStandaloneOrLockHeld("copyToClubRoleBaseInfo", false);
        limax.zdb.Logs.logObject(this, "roleId");
        this.roleId = _o_.roleId;
        limax.zdb.Logs.logObject(this, "name");
        this.name = _o_.name;
        limax.zdb.Logs.logObject(this, "title");
        this.title = _o_.title;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.roleId);
        _os_.marshal(this.name);
        _os_.marshal(this.title);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.roleId = _os_.unmarshal_int();
		this.name = _os_.unmarshal_String();
		this.title = _os_.unmarshal_int();
		return _os_;
	}

	public int getRoleId() { 
		this.verifyStandaloneOrLockHeld("getRoleId", true);
		return this.roleId;
	}

	public String getName() { 
		this.verifyStandaloneOrLockHeld("getName", true);
		return this.name;
	}

	public int getTitle() { 
		this.verifyStandaloneOrLockHeld("getTitle", true);
		return this.title;
	}

	public void setRoleId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoleId", false);
		limax.zdb.Logs.logObject(this, "roleId");
		this.roleId = _v_;
	}

	public void setName(String _v_) { 
		this.verifyStandaloneOrLockHeld("setName", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "name");
		this.name = _v_;
	}

	public void setTitle(int _v_) { 
		this.verifyStandaloneOrLockHeld("setTitle", false);
		limax.zdb.Logs.logObject(this, "title");
		this.title = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ClubRoleBaseInfo _o_ = null;
		if ( _o1_ instanceof ClubRoleBaseInfo ) _o_ = (ClubRoleBaseInfo)_o1_;
		else return false;
		if (this.roleId != _o_.roleId) return false;
		if (!this.name.equals(_o_.name)) return false;
		if (this.title != _o_.title) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roleId;
		_h_ += _h_ * 31 + this.name.hashCode();
		_h_ += _h_ * 31 + this.title;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roleId).append(",");
		_sb_.append("T").append(this.name.length()).append(",");
		_sb_.append(this.title).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
