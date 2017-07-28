package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class TurntableActivityReward extends limax.zdb.XBean {
    private int roleId; 
    private int itemCount; 
    private int consumeNum; 
    private long consumeAddNumTime; 
    private long shareTime; 
    private java.util.ArrayList<xbean.TurntableActivityRewardRecord> rewards; 

    TurntableActivityReward(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        rewards = new java.util.ArrayList<xbean.TurntableActivityRewardRecord>();
	}

	public TurntableActivityReward() {
		this(null, null);
	}

	public TurntableActivityReward(TurntableActivityReward _o_) {
		this(_o_, null, null);
	}

	TurntableActivityReward(TurntableActivityReward _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.TurntableActivityReward", true);
        this.roleId = _o_.roleId;
        this.itemCount = _o_.itemCount;
        this.consumeNum = _o_.consumeNum;
        this.consumeAddNumTime = _o_.consumeAddNumTime;
        this.shareTime = _o_.shareTime;
        this.rewards = new java.util.ArrayList<xbean.TurntableActivityRewardRecord>();
        _o_.rewards.forEach(_v_ -> this.rewards.add(new TurntableActivityRewardRecord(_v_, this, "rewards")));
	}

	public void copyFrom(TurntableActivityReward _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromTurntableActivityReward", true);
		this.verifyStandaloneOrLockHeld("copyToTurntableActivityReward", false);
        limax.zdb.Logs.logObject(this, "roleId");
        this.roleId = _o_.roleId;
        limax.zdb.Logs.logObject(this, "itemCount");
        this.itemCount = _o_.itemCount;
        limax.zdb.Logs.logObject(this, "consumeNum");
        this.consumeNum = _o_.consumeNum;
        limax.zdb.Logs.logObject(this, "consumeAddNumTime");
        this.consumeAddNumTime = _o_.consumeAddNumTime;
        limax.zdb.Logs.logObject(this, "shareTime");
        this.shareTime = _o_.shareTime;
        java.util.List<xbean.TurntableActivityRewardRecord> this_rewards = limax.zdb.Logs.logList(this, "rewards", ()->{});
        this_rewards.clear();
        _o_.rewards.forEach(_v_ -> this_rewards.add(new TurntableActivityRewardRecord(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.roleId);
        _os_.marshal(this.itemCount);
        _os_.marshal(this.consumeNum);
        _os_.marshal(this.consumeAddNumTime);
        _os_.marshal(this.shareTime);
        _os_.marshal_size(this.rewards.size());
        for (xbean.TurntableActivityRewardRecord _v_ : this.rewards) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.roleId = _os_.unmarshal_int();
		this.itemCount = _os_.unmarshal_int();
		this.consumeNum = _os_.unmarshal_int();
		this.consumeAddNumTime = _os_.unmarshal_long();
		this.shareTime = _os_.unmarshal_long();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.TurntableActivityRewardRecord _v_ = new TurntableActivityRewardRecord(this, "rewards");
			_v_.unmarshal(_os_);
			this.rewards.add(_v_);
		}
		return _os_;
	}

	public int getRoleId() { 
		this.verifyStandaloneOrLockHeld("getRoleId", true);
		return this.roleId;
	}

	public int getItemCount() { 
		this.verifyStandaloneOrLockHeld("getItemCount", true);
		return this.itemCount;
	}

	public int getConsumeNum() { 
		this.verifyStandaloneOrLockHeld("getConsumeNum", true);
		return this.consumeNum;
	}

	public long getConsumeAddNumTime() { 
		this.verifyStandaloneOrLockHeld("getConsumeAddNumTime", true);
		return this.consumeAddNumTime;
	}

	public long getShareTime() { 
		this.verifyStandaloneOrLockHeld("getShareTime", true);
		return this.shareTime;
	}

	public java.util.List<xbean.TurntableActivityRewardRecord> getRewards() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "rewards", this.verifyStandaloneOrLockHeld("getRewards", true)) : this.rewards;
	}

	public void setRoleId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoleId", false);
		limax.zdb.Logs.logObject(this, "roleId");
		this.roleId = _v_;
	}

	public void setItemCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setItemCount", false);
		limax.zdb.Logs.logObject(this, "itemCount");
		this.itemCount = _v_;
	}

	public void setConsumeNum(int _v_) { 
		this.verifyStandaloneOrLockHeld("setConsumeNum", false);
		limax.zdb.Logs.logObject(this, "consumeNum");
		this.consumeNum = _v_;
	}

	public void setConsumeAddNumTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setConsumeAddNumTime", false);
		limax.zdb.Logs.logObject(this, "consumeAddNumTime");
		this.consumeAddNumTime = _v_;
	}

	public void setShareTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setShareTime", false);
		limax.zdb.Logs.logObject(this, "shareTime");
		this.shareTime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		TurntableActivityReward _o_ = null;
		if ( _o1_ instanceof TurntableActivityReward ) _o_ = (TurntableActivityReward)_o1_;
		else return false;
		if (this.roleId != _o_.roleId) return false;
		if (this.itemCount != _o_.itemCount) return false;
		if (this.consumeNum != _o_.consumeNum) return false;
		if (this.consumeAddNumTime != _o_.consumeAddNumTime) return false;
		if (this.shareTime != _o_.shareTime) return false;
		if (!this.rewards.equals(_o_.rewards)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roleId;
		_h_ += _h_ * 31 + this.itemCount;
		_h_ += _h_ * 31 + this.consumeNum;
		_h_ += _h_ * 31 + (int)(this.consumeAddNumTime ^ (this.consumeAddNumTime >>> 32));
		_h_ += _h_ * 31 + (int)(this.shareTime ^ (this.shareTime >>> 32));
		_h_ += _h_ * 31 + this.rewards.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roleId).append(",");
		_sb_.append(this.itemCount).append(",");
		_sb_.append(this.consumeNum).append(",");
		_sb_.append(this.consumeAddNumTime).append(",");
		_sb_.append(this.shareTime).append(",");
		_sb_.append(this.rewards).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
