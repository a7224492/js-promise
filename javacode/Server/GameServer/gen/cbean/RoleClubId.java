
package cbean;

public class RoleClubId implements limax.codec.Marshal, Comparable<RoleClubId> {

	private int clubId; 
	private int roleId; 

	public RoleClubId() {
	}

	public RoleClubId(int _clubId_, int _roleId_) {
		this.clubId = _clubId_;
		this.roleId = _roleId_;
	}

	public int getClubId() { 
		return this.clubId;
	}

	public int getRoleId() { 
		return this.roleId;
	}

	@Override
	public limax.codec.OctetsStream marshal(limax.codec.OctetsStream _os_) {
		_os_.marshal(this.clubId);
		_os_.marshal(this.roleId);
		return _os_;
	}

	@Override
	public limax.codec.OctetsStream unmarshal(limax.codec.OctetsStream _os_) throws limax.codec.MarshalException {
		this.clubId = _os_.unmarshal_int();
		this.roleId = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public int compareTo(RoleClubId _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = this.clubId - _o_.clubId;
		if (0 != _c_) return _c_;
		_c_ = this.roleId - _o_.roleId;
		if (0 != _c_) return _c_;
		return _c_;
	}

	@Override
	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleClubId) {
			RoleClubId _o_ = (RoleClubId)_o1_;
			if (this.clubId != _o_.clubId) return false;
			if (this.roleId != _o_.roleId) return false;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.clubId;
		_h_ += _h_ * 31 + this.roleId;
		return _h_;
	}

}
