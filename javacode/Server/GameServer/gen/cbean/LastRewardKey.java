
package cbean;

public class LastRewardKey implements limax.codec.Marshal, Comparable<LastRewardKey> {

	private int roleId; 
	private long rewardTime; 

	public LastRewardKey() {
	}

	public LastRewardKey(int _roleId_, long _rewardTime_) {
		this.roleId = _roleId_;
		this.rewardTime = _rewardTime_;
	}

	public int getRoleId() { 
		return this.roleId;
	}

	public long getRewardTime() { 
		return this.rewardTime;
	}

	@Override
	public limax.codec.OctetsStream marshal(limax.codec.OctetsStream _os_) {
		_os_.marshal(this.roleId);
		_os_.marshal(this.rewardTime);
		return _os_;
	}

	@Override
	public limax.codec.OctetsStream unmarshal(limax.codec.OctetsStream _os_) throws limax.codec.MarshalException {
		this.roleId = _os_.unmarshal_int();
		this.rewardTime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public int compareTo(LastRewardKey _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = this.roleId - _o_.roleId;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(this.rewardTime - _o_.rewardTime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	@Override
	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof LastRewardKey) {
			LastRewardKey _o_ = (LastRewardKey)_o1_;
			if (this.roleId != _o_.roleId) return false;
			if (this.rewardTime != _o_.rewardTime) return false;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roleId;
		_h_ += _h_ * 31 + (int)(this.rewardTime ^ (this.rewardTime >>> 32));
		return _h_;
	}

}
