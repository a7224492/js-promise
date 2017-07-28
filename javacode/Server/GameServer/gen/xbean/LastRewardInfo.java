package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class LastRewardInfo extends limax.zdb.XBean {
    private int rewardId; 
    private String rewardDesc; 

    LastRewardInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        rewardDesc = "";
	}

	public LastRewardInfo() {
		this(null, null);
	}

	public LastRewardInfo(LastRewardInfo _o_) {
		this(_o_, null, null);
	}

	LastRewardInfo(LastRewardInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.LastRewardInfo", true);
        this.rewardId = _o_.rewardId;
        this.rewardDesc = _o_.rewardDesc;
	}

	public void copyFrom(LastRewardInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromLastRewardInfo", true);
		this.verifyStandaloneOrLockHeld("copyToLastRewardInfo", false);
        limax.zdb.Logs.logObject(this, "rewardId");
        this.rewardId = _o_.rewardId;
        limax.zdb.Logs.logObject(this, "rewardDesc");
        this.rewardDesc = _o_.rewardDesc;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.rewardId);
        _os_.marshal(this.rewardDesc);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.rewardId = _os_.unmarshal_int();
		this.rewardDesc = _os_.unmarshal_String();
		return _os_;
	}

	public int getRewardId() { 
		this.verifyStandaloneOrLockHeld("getRewardId", true);
		return this.rewardId;
	}

	public String getRewardDesc() { 
		this.verifyStandaloneOrLockHeld("getRewardDesc", true);
		return this.rewardDesc;
	}

	public void setRewardId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRewardId", false);
		limax.zdb.Logs.logObject(this, "rewardId");
		this.rewardId = _v_;
	}

	public void setRewardDesc(String _v_) { 
		this.verifyStandaloneOrLockHeld("setRewardDesc", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "rewardDesc");
		this.rewardDesc = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		LastRewardInfo _o_ = null;
		if ( _o1_ instanceof LastRewardInfo ) _o_ = (LastRewardInfo)_o1_;
		else return false;
		if (this.rewardId != _o_.rewardId) return false;
		if (!this.rewardDesc.equals(_o_.rewardDesc)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.rewardId;
		_h_ += _h_ * 31 + this.rewardDesc.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.rewardId).append(",");
		_sb_.append("T").append(this.rewardDesc.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
