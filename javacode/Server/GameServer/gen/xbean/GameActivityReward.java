package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class GameActivityReward extends limax.zdb.XBean {
    private String rewardName; 
    private String rewardDesc; 
    private int rewardCount; 
    private int rewardLeftCount; 
    private int rewardRatio; 
    private String condition; 
    private boolean isBroadcard; 
    private boolean isReward; 
    private boolean isCardReward; 

    GameActivityReward(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        rewardName = "";
        rewardDesc = "";
        condition = "";
	}

	public GameActivityReward() {
		this(null, null);
	}

	public GameActivityReward(GameActivityReward _o_) {
		this(_o_, null, null);
	}

	GameActivityReward(GameActivityReward _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.GameActivityReward", true);
        this.rewardName = _o_.rewardName;
        this.rewardDesc = _o_.rewardDesc;
        this.rewardCount = _o_.rewardCount;
        this.rewardLeftCount = _o_.rewardLeftCount;
        this.rewardRatio = _o_.rewardRatio;
        this.condition = _o_.condition;
        this.isBroadcard = _o_.isBroadcard;
        this.isReward = _o_.isReward;
        this.isCardReward = _o_.isCardReward;
	}

	public void copyFrom(GameActivityReward _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromGameActivityReward", true);
		this.verifyStandaloneOrLockHeld("copyToGameActivityReward", false);
        limax.zdb.Logs.logObject(this, "rewardName");
        this.rewardName = _o_.rewardName;
        limax.zdb.Logs.logObject(this, "rewardDesc");
        this.rewardDesc = _o_.rewardDesc;
        limax.zdb.Logs.logObject(this, "rewardCount");
        this.rewardCount = _o_.rewardCount;
        limax.zdb.Logs.logObject(this, "rewardLeftCount");
        this.rewardLeftCount = _o_.rewardLeftCount;
        limax.zdb.Logs.logObject(this, "rewardRatio");
        this.rewardRatio = _o_.rewardRatio;
        limax.zdb.Logs.logObject(this, "condition");
        this.condition = _o_.condition;
        limax.zdb.Logs.logObject(this, "isBroadcard");
        this.isBroadcard = _o_.isBroadcard;
        limax.zdb.Logs.logObject(this, "isReward");
        this.isReward = _o_.isReward;
        limax.zdb.Logs.logObject(this, "isCardReward");
        this.isCardReward = _o_.isCardReward;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.rewardName);
        _os_.marshal(this.rewardDesc);
        _os_.marshal(this.rewardCount);
        _os_.marshal(this.rewardLeftCount);
        _os_.marshal(this.rewardRatio);
        _os_.marshal(this.condition);
        _os_.marshal(this.isBroadcard);
        _os_.marshal(this.isReward);
        _os_.marshal(this.isCardReward);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.rewardName = _os_.unmarshal_String();
		this.rewardDesc = _os_.unmarshal_String();
		this.rewardCount = _os_.unmarshal_int();
		this.rewardLeftCount = _os_.unmarshal_int();
		this.rewardRatio = _os_.unmarshal_int();
		this.condition = _os_.unmarshal_String();
		this.isBroadcard = _os_.unmarshal_boolean();
		this.isReward = _os_.unmarshal_boolean();
		this.isCardReward = _os_.unmarshal_boolean();
		return _os_;
	}

	public String getRewardName() { 
		this.verifyStandaloneOrLockHeld("getRewardName", true);
		return this.rewardName;
	}

	public String getRewardDesc() { 
		this.verifyStandaloneOrLockHeld("getRewardDesc", true);
		return this.rewardDesc;
	}

	public int getRewardCount() { 
		this.verifyStandaloneOrLockHeld("getRewardCount", true);
		return this.rewardCount;
	}

	public int getRewardLeftCount() { 
		this.verifyStandaloneOrLockHeld("getRewardLeftCount", true);
		return this.rewardLeftCount;
	}

	public int getRewardRatio() { 
		this.verifyStandaloneOrLockHeld("getRewardRatio", true);
		return this.rewardRatio;
	}

	public String getCondition() { 
		this.verifyStandaloneOrLockHeld("getCondition", true);
		return this.condition;
	}

	public boolean getIsBroadcard() { 
		this.verifyStandaloneOrLockHeld("getIsBroadcard", true);
		return this.isBroadcard;
	}

	public boolean getIsReward() { 
		this.verifyStandaloneOrLockHeld("getIsReward", true);
		return this.isReward;
	}

	public boolean getIsCardReward() { 
		this.verifyStandaloneOrLockHeld("getIsCardReward", true);
		return this.isCardReward;
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

	public void setRewardCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRewardCount", false);
		limax.zdb.Logs.logObject(this, "rewardCount");
		this.rewardCount = _v_;
	}

	public void setRewardLeftCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRewardLeftCount", false);
		limax.zdb.Logs.logObject(this, "rewardLeftCount");
		this.rewardLeftCount = _v_;
	}

	public void setRewardRatio(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRewardRatio", false);
		limax.zdb.Logs.logObject(this, "rewardRatio");
		this.rewardRatio = _v_;
	}

	public void setCondition(String _v_) { 
		this.verifyStandaloneOrLockHeld("setCondition", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "condition");
		this.condition = _v_;
	}

	public void setIsBroadcard(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setIsBroadcard", false);
		limax.zdb.Logs.logObject(this, "isBroadcard");
		this.isBroadcard = _v_;
	}

	public void setIsReward(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setIsReward", false);
		limax.zdb.Logs.logObject(this, "isReward");
		this.isReward = _v_;
	}

	public void setIsCardReward(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setIsCardReward", false);
		limax.zdb.Logs.logObject(this, "isCardReward");
		this.isCardReward = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		GameActivityReward _o_ = null;
		if ( _o1_ instanceof GameActivityReward ) _o_ = (GameActivityReward)_o1_;
		else return false;
		if (!this.rewardName.equals(_o_.rewardName)) return false;
		if (!this.rewardDesc.equals(_o_.rewardDesc)) return false;
		if (this.rewardCount != _o_.rewardCount) return false;
		if (this.rewardLeftCount != _o_.rewardLeftCount) return false;
		if (this.rewardRatio != _o_.rewardRatio) return false;
		if (!this.condition.equals(_o_.condition)) return false;
		if (this.isBroadcard != _o_.isBroadcard) return false;
		if (this.isReward != _o_.isReward) return false;
		if (this.isCardReward != _o_.isCardReward) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.rewardName.hashCode();
		_h_ += _h_ * 31 + this.rewardDesc.hashCode();
		_h_ += _h_ * 31 + this.rewardCount;
		_h_ += _h_ * 31 + this.rewardLeftCount;
		_h_ += _h_ * 31 + this.rewardRatio;
		_h_ += _h_ * 31 + this.condition.hashCode();
		_h_ += _h_ * 31 + (this.isBroadcard ? 1231 : 1237);
		_h_ += _h_ * 31 + (this.isReward ? 1231 : 1237);
		_h_ += _h_ * 31 + (this.isCardReward ? 1231 : 1237);
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append("T").append(this.rewardName.length()).append(",");
		_sb_.append("T").append(this.rewardDesc.length()).append(",");
		_sb_.append(this.rewardCount).append(",");
		_sb_.append(this.rewardLeftCount).append(",");
		_sb_.append(this.rewardRatio).append(",");
		_sb_.append("T").append(this.condition.length()).append(",");
		_sb_.append(this.isBroadcard).append(",");
		_sb_.append(this.isReward).append(",");
		_sb_.append(this.isCardReward).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
