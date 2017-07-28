
package cbean;

public class ActivityRewardKey implements limax.codec.Marshal, Comparable<ActivityRewardKey> {

	private int activityId; 
	private int rewardId; 
	private long rewardDate; 

	public ActivityRewardKey() {
	}

	public ActivityRewardKey(int _activityId_, int _rewardId_, long _rewardDate_) {
		this.activityId = _activityId_;
		this.rewardId = _rewardId_;
		this.rewardDate = _rewardDate_;
	}

	public int getActivityId() { 
		return this.activityId;
	}

	public int getRewardId() { 
		return this.rewardId;
	}

	public long getRewardDate() { 
		return this.rewardDate;
	}

	@Override
	public limax.codec.OctetsStream marshal(limax.codec.OctetsStream _os_) {
		_os_.marshal(this.activityId);
		_os_.marshal(this.rewardId);
		_os_.marshal(this.rewardDate);
		return _os_;
	}

	@Override
	public limax.codec.OctetsStream unmarshal(limax.codec.OctetsStream _os_) throws limax.codec.MarshalException {
		this.activityId = _os_.unmarshal_int();
		this.rewardId = _os_.unmarshal_int();
		this.rewardDate = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public int compareTo(ActivityRewardKey _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = this.activityId - _o_.activityId;
		if (0 != _c_) return _c_;
		_c_ = this.rewardId - _o_.rewardId;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(this.rewardDate - _o_.rewardDate);
		if (0 != _c_) return _c_;
		return _c_;
	}

	@Override
	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ActivityRewardKey) {
			ActivityRewardKey _o_ = (ActivityRewardKey)_o1_;
			if (this.activityId != _o_.activityId) return false;
			if (this.rewardId != _o_.rewardId) return false;
			if (this.rewardDate != _o_.rewardDate) return false;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.activityId;
		_h_ += _h_ * 31 + this.rewardId;
		_h_ += _h_ * 31 + (int)(this.rewardDate ^ (this.rewardDate >>> 32));
		return _h_;
	}

}
