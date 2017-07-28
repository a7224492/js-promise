package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class TurntableRewardDispatchBean extends limax.zdb.XBean {
    private int rewardId; 
    private String rewardName; 
    private String rewardDesc; 
    private long rewardTime; 
    private int rewardCount; 
    private boolean isCard; 
    private boolean isDispatch; 

    TurntableRewardDispatchBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        rewardName = "";
        rewardDesc = "";
	}

	public TurntableRewardDispatchBean() {
		this(null, null);
	}

	public TurntableRewardDispatchBean(TurntableRewardDispatchBean _o_) {
		this(_o_, null, null);
	}

	TurntableRewardDispatchBean(TurntableRewardDispatchBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.TurntableRewardDispatchBean", true);
        this.rewardId = _o_.rewardId;
        this.rewardName = _o_.rewardName;
        this.rewardDesc = _o_.rewardDesc;
        this.rewardTime = _o_.rewardTime;
        this.rewardCount = _o_.rewardCount;
        this.isCard = _o_.isCard;
        this.isDispatch = _o_.isDispatch;
	}

	public void copyFrom(TurntableRewardDispatchBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromTurntableRewardDispatchBean", true);
		this.verifyStandaloneOrLockHeld("copyToTurntableRewardDispatchBean", false);
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
        limax.zdb.Logs.logObject(this, "isCard");
        this.isCard = _o_.isCard;
        limax.zdb.Logs.logObject(this, "isDispatch");
        this.isDispatch = _o_.isDispatch;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.rewardId);
        _os_.marshal(this.rewardName);
        _os_.marshal(this.rewardDesc);
        _os_.marshal(this.rewardTime);
        _os_.marshal(this.rewardCount);
        _os_.marshal(this.isCard);
        _os_.marshal(this.isDispatch);
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
		this.isCard = _os_.unmarshal_boolean();
		this.isDispatch = _os_.unmarshal_boolean();
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

	public boolean getIsCard() { 
		this.verifyStandaloneOrLockHeld("getIsCard", true);
		return this.isCard;
	}

	public boolean getIsDispatch() { 
		this.verifyStandaloneOrLockHeld("getIsDispatch", true);
		return this.isDispatch;
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

	public void setIsCard(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setIsCard", false);
		limax.zdb.Logs.logObject(this, "isCard");
		this.isCard = _v_;
	}

	public void setIsDispatch(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setIsDispatch", false);
		limax.zdb.Logs.logObject(this, "isDispatch");
		this.isDispatch = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		TurntableRewardDispatchBean _o_ = null;
		if ( _o1_ instanceof TurntableRewardDispatchBean ) _o_ = (TurntableRewardDispatchBean)_o1_;
		else return false;
		if (this.rewardId != _o_.rewardId) return false;
		if (!this.rewardName.equals(_o_.rewardName)) return false;
		if (!this.rewardDesc.equals(_o_.rewardDesc)) return false;
		if (this.rewardTime != _o_.rewardTime) return false;
		if (this.rewardCount != _o_.rewardCount) return false;
		if (this.isCard != _o_.isCard) return false;
		if (this.isDispatch != _o_.isDispatch) return false;
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
		_h_ += _h_ * 31 + (this.isCard ? 1231 : 1237);
		_h_ += _h_ * 31 + (this.isDispatch ? 1231 : 1237);
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
		_sb_.append(this.isCard).append(",");
		_sb_.append(this.isDispatch).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
