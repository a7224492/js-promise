package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class TurntableActivityRewardRecord extends limax.zdb.XBean {
    private int rewardId; 
    private String rewardName; 
    private String rewardDesc; 
    private long rewardTime; 
    private int rewardCount; 

    TurntableActivityRewardRecord(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        rewardName = "";
        rewardDesc = "";
	}

	public TurntableActivityRewardRecord() {
		this(null, null);
	}

	public TurntableActivityRewardRecord(TurntableActivityRewardRecord _o_) {
		this(_o_, null, null);
	}

	TurntableActivityRewardRecord(TurntableActivityRewardRecord _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.TurntableActivityRewardRecord", true);
        this.rewardId = _o_.rewardId;
        this.rewardName = _o_.rewardName;
        this.rewardDesc = _o_.rewardDesc;
        this.rewardTime = _o_.rewardTime;
        this.rewardCount = _o_.rewardCount;
	}

	public void copyFrom(TurntableActivityRewardRecord _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromTurntableActivityRewardRecord", true);
		this.verifyStandaloneOrLockHeld("copyToTurntableActivityRewardRecord", false);
        limax.zdb.Logs.logObject(this, "rewardId");
        this.rewardId = _o_.rewardId;
        limax.zdb.Logs.logObject(this, "rewardName");
        this.rewardName = _o_.rewardName;
        limax.zdb.Logs.logObject(this, "rewardDesc");
        this.rewardDesc = _o_.rewardDesc;
        limax.zdb.Logs.logObject(this, "rewardTime");
        this.rewardTime = _o_.rewardTime;
        limax.zdb.Logs.logObject(this, "rewardCount");
        this.rewardCount = _o_.rewardCount;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.rewardId);
        _os_.marshal(this.rewardName);
        _os_.marshal(this.rewardDesc);
        _os_.marshal(this.rewardTime);
        _os_.marshal(this.rewardCount);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.rewardId = _os_.unmarshal_int();
		this.rewardName = _os_.unmarshal_String();
		this.rewardDesc = _os_.unmarshal_String();
		this.rewardTime = _os_.unmarshal_long();
		this.rewardCount = _os_.unmarshal_int();
		return _os_;
	}

	public int getRewardId() { 
		this.verifyStandaloneOrLockHeld("getRewardId", true);
		return this.rewardId;
	}

	public String getRewardName() { 
		this.verifyStandaloneOrLockHeld("getRewardName", true);
		return this.rewardName;
	}

	public String getRewardDesc() { 
		this.verifyStandaloneOrLockHeld("getRewardDesc", true);
		return this.rewardDesc;
	}

	public long getRewardTime() { 
		this.verifyStandaloneOrLockHeld("getRewardTime", true);
		return this.rewardTime;
	}

	public int getRewardCount() { 
		this.verifyStandaloneOrLockHeld("getRewardCount", true);
		return this.rewardCount;
	}

	public void setRewardId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRewardId", false);
		limax.zdb.Logs.logObject(this, "rewardId");
		this.rewardId = _v_;
	}

	public void setRewardName(String _v_) { 
		this.verifyStandaloneOrLockHeld("setRewardName", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "rewardName");
		this.rewardName = _v_;
	}

	public void setRewardDesc(String _v_) { 
		this.verifyStandaloneOrLockHeld("setRewardDesc", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "rewardDesc");
		this.rewardDesc = _v_;
	}

	public void setRewardTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setRewardTime", false);
		limax.zdb.Logs.logObject(this, "rewardTime");
		this.rewardTime = _v_;
	}

	public void setRewardCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRewardCount", false);
		limax.zdb.Logs.logObject(this, "rewardCount");
		this.rewardCount = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		TurntableActivityRewardRecord _o_ = null;
		if ( _o1_ instanceof TurntableActivityRewardRecord ) _o_ = (TurntableActivityRewardRecord)_o1_;
		else return false;
		if (this.rewardId != _o_.rewardId) return false;
		if (!this.rewardName.equals(_o_.rewardName)) return false;
		if (!this.rewardDesc.equals(_o_.rewardDesc)) return false;
		if (this.rewardTime != _o_.rewardTime) return false;
		if (this.rewardCount != _o_.rewardCount) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.rewardId;
		_h_ += _h_ * 31 + this.rewardName.hashCode();
		_h_ += _h_ * 31 + this.rewardDesc.hashCode();
		_h_ += _h_ * 31 + (int)(this.rewardTime ^ (this.rewardTime >>> 32));
		_h_ += _h_ * 31 + this.rewardCount;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.rewardId).append(",");
		_sb_.append("T").append(this.rewardName.length()).append(",");
		_sb_.append("T").append(this.rewardDesc.length()).append(",");
		_sb_.append(this.rewardTime).append(",");
		_sb_.append(this.rewardCount).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
