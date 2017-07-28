
package cbean;

public class AddPlayersCardDetailKey implements limax.codec.Marshal, Comparable<AddPlayersCardDetailKey> {

	private int id; 
	private int roleId; 

	public AddPlayersCardDetailKey() {
	}

	public AddPlayersCardDetailKey(int _id_, int _roleId_) {
		this.id = _id_;
		this.roleId = _roleId_;
	}

	public int getId() { 
		return this.id;
	}

	public int getRoleId() { 
		return this.roleId;
	}

	@Override
	public limax.codec.OctetsStream marshal(limax.codec.OctetsStream _os_) {
		_os_.marshal(this.id);
		_os_.marshal(this.roleId);
		return _os_;
	}

	@Override
	public limax.codec.OctetsStream unmarshal(limax.codec.OctetsStream _os_) throws limax.codec.MarshalException {
		this.id = _os_.unmarshal_int();
		this.roleId = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public int compareTo(AddPlayersCardDetailKey _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = this.id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = this.roleId - _o_.roleId;
		if (0 != _c_) return _c_;
		return _c_;
	}

	@Override
	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AddPlayersCardDetailKey) {
			AddPlayersCardDetailKey _o_ = (AddPlayersCardDetailKey)_o1_;
			if (this.id != _o_.id) return false;
			if (this.roleId != _o_.roleId) return false;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.id;
		_h_ += _h_ * 31 + this.roleId;
		return _h_;
	}

}
