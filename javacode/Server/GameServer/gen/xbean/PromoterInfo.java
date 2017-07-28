package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class PromoterInfo extends limax.zdb.XBean {
    private java.util.ArrayList<String> inviteeUnionidList; 
    private int totalEffectiveInvitee; 
    private float unreceivedRewards; 
    private float totalRewards; 
    private int inviteeCountThisCycle; 
    private float rewardsThisCycle; 
    private long cycleStartTime; 
    private int receivedCountToday; 
    private float receivedRewardsToday; 
    private long receivedTimeToday; 
    private java.util.ArrayList<Long> rewardList; 

    PromoterInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        inviteeUnionidList = new java.util.ArrayList<String>();
        rewardList = new java.util.ArrayList<Long>();
	}

	public PromoterInfo() {
		this(null, null);
	}

	public PromoterInfo(PromoterInfo _o_) {
		this(_o_, null, null);
	}

	PromoterInfo(PromoterInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.PromoterInfo", true);
        this.inviteeUnionidList = new java.util.ArrayList<String>();
        this.inviteeUnionidList.addAll(_o_.inviteeUnionidList);
        this.totalEffectiveInvitee = _o_.totalEffectiveInvitee;
        this.unreceivedRewards = _o_.unreceivedRewards;
        this.totalRewards = _o_.totalRewards;
        this.inviteeCountThisCycle = _o_.inviteeCountThisCycle;
        this.rewardsThisCycle = _o_.rewardsThisCycle;
        this.cycleStartTime = _o_.cycleStartTime;
        this.receivedCountToday = _o_.receivedCountToday;
        this.receivedRewardsToday = _o_.receivedRewardsToday;
        this.receivedTimeToday = _o_.receivedTimeToday;
        this.rewardList = new java.util.ArrayList<Long>();
        this.rewardList.addAll(_o_.rewardList);
	}

	public void copyFrom(PromoterInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromPromoterInfo", true);
		this.verifyStandaloneOrLockHeld("copyToPromoterInfo", false);
        java.util.List<String> this_inviteeUnionidList = limax.zdb.Logs.logList(this, "inviteeUnionidList", ()->{});
        this_inviteeUnionidList.clear();
        this_inviteeUnionidList.addAll(_o_.inviteeUnionidList);
        limax.zdb.Logs.logObject(this, "totalEffectiveInvitee");
        this.totalEffectiveInvitee = _o_.totalEffectiveInvitee;
        limax.zdb.Logs.logObject(this, "unreceivedRewards");
        this.unreceivedRewards = _o_.unreceivedRewards;
        limax.zdb.Logs.logObject(this, "totalRewards");
        this.totalRewards = _o_.totalRewards;
        limax.zdb.Logs.logObject(this, "inviteeCountThisCycle");
        this.inviteeCountThisCycle = _o_.inviteeCountThisCycle;
        limax.zdb.Logs.logObject(this, "rewardsThisCycle");
        this.rewardsThisCycle = _o_.rewardsThisCycle;
        limax.zdb.Logs.logObject(this, "cycleStartTime");
        this.cycleStartTime = _o_.cycleStartTime;
        limax.zdb.Logs.logObject(this, "receivedCountToday");
        this.receivedCountToday = _o_.receivedCountToday;
        limax.zdb.Logs.logObject(this, "receivedRewardsToday");
        this.receivedRewardsToday = _o_.receivedRewardsToday;
        limax.zdb.Logs.logObject(this, "receivedTimeToday");
        this.receivedTimeToday = _o_.receivedTimeToday;
        java.util.List<Long> this_rewardList = limax.zdb.Logs.logList(this, "rewardList", ()->{});
        this_rewardList.clear();
        this_rewardList.addAll(_o_.rewardList);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.inviteeUnionidList.size());
        for (String _v_ : this.inviteeUnionidList) {
        	_os_.marshal(_v_);
        }
        _os_.marshal(this.totalEffectiveInvitee);
        _os_.marshal(this.unreceivedRewards);
        _os_.marshal(this.totalRewards);
        _os_.marshal(this.inviteeCountThisCycle);
        _os_.marshal(this.rewardsThisCycle);
        _os_.marshal(this.cycleStartTime);
        _os_.marshal(this.receivedCountToday);
        _os_.marshal(this.receivedRewardsToday);
        _os_.marshal(this.receivedTimeToday);
        _os_.marshal_size(this.rewardList.size());
        for (Long _v_ : this.rewardList) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			String _v_ = _os_.unmarshal_String();
			this.inviteeUnionidList.add(_v_);
		}
		this.totalEffectiveInvitee = _os_.unmarshal_int();
		this.unreceivedRewards = _os_.unmarshal_float();
		this.totalRewards = _os_.unmarshal_float();
		this.inviteeCountThisCycle = _os_.unmarshal_int();
		this.rewardsThisCycle = _os_.unmarshal_float();
		this.cycleStartTime = _os_.unmarshal_long();
		this.receivedCountToday = _os_.unmarshal_int();
		this.receivedRewardsToday = _os_.unmarshal_float();
		this.receivedTimeToday = _os_.unmarshal_long();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			long _v_ = _os_.unmarshal_long();
			this.rewardList.add(_v_);
		}
		return _os_;
	}

	public java.util.List<String> getInviteeUnionidList() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "inviteeUnionidList", this.verifyStandaloneOrLockHeld("getInviteeUnionidList", true)) : this.inviteeUnionidList;
	}

	public int getTotalEffectiveInvitee() { 
		this.verifyStandaloneOrLockHeld("getTotalEffectiveInvitee", true);
		return this.totalEffectiveInvitee;
	}

	public float getUnreceivedRewards() { 
		this.verifyStandaloneOrLockHeld("getUnreceivedRewards", true);
		return this.unreceivedRewards;
	}

	public float getTotalRewards() { 
		this.verifyStandaloneOrLockHeld("getTotalRewards", true);
		return this.totalRewards;
	}

	public int getInviteeCountThisCycle() { 
		this.verifyStandaloneOrLockHeld("getInviteeCountThisCycle", true);
		return this.inviteeCountThisCycle;
	}

	public float getRewardsThisCycle() { 
		this.verifyStandaloneOrLockHeld("getRewardsThisCycle", true);
		return this.rewardsThisCycle;
	}

	public long getCycleStartTime() { 
		this.verifyStandaloneOrLockHeld("getCycleStartTime", true);
		return this.cycleStartTime;
	}

	public int getReceivedCountToday() { 
		this.verifyStandaloneOrLockHeld("getReceivedCountToday", true);
		return this.receivedCountToday;
	}

	public float getReceivedRewardsToday() { 
		this.verifyStandaloneOrLockHeld("getReceivedRewardsToday", true);
		return this.receivedRewardsToday;
	}

	public long getReceivedTimeToday() { 
		this.verifyStandaloneOrLockHeld("getReceivedTimeToday", true);
		return this.receivedTimeToday;
	}

	public java.util.List<Long> getRewardList() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "rewardList", this.verifyStandaloneOrLockHeld("getRewardList", true)) : this.rewardList;
	}

	public void setTotalEffectiveInvitee(int _v_) { 
		this.verifyStandaloneOrLockHeld("setTotalEffectiveInvitee", false);
		limax.zdb.Logs.logObject(this, "totalEffectiveInvitee");
		this.totalEffectiveInvitee = _v_;
	}

	public void setUnreceivedRewards(float _v_) { 
		this.verifyStandaloneOrLockHeld("setUnreceivedRewards", false);
		limax.zdb.Logs.logObject(this, "unreceivedRewards");
		this.unreceivedRewards = _v_;
	}

	public void setTotalRewards(float _v_) { 
		this.verifyStandaloneOrLockHeld("setTotalRewards", false);
		limax.zdb.Logs.logObject(this, "totalRewards");
		this.totalRewards = _v_;
	}

	public void setInviteeCountThisCycle(int _v_) { 
		this.verifyStandaloneOrLockHeld("setInviteeCountThisCycle", false);
		limax.zdb.Logs.logObject(this, "inviteeCountThisCycle");
		this.inviteeCountThisCycle = _v_;
	}

	public void setRewardsThisCycle(float _v_) { 
		this.verifyStandaloneOrLockHeld("setRewardsThisCycle", false);
		limax.zdb.Logs.logObject(this, "rewardsThisCycle");
		this.rewardsThisCycle = _v_;
	}

	public void setCycleStartTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setCycleStartTime", false);
		limax.zdb.Logs.logObject(this, "cycleStartTime");
		this.cycleStartTime = _v_;
	}

	public void setReceivedCountToday(int _v_) { 
		this.verifyStandaloneOrLockHeld("setReceivedCountToday", false);
		limax.zdb.Logs.logObject(this, "receivedCountToday");
		this.receivedCountToday = _v_;
	}

	public void setReceivedRewardsToday(float _v_) { 
		this.verifyStandaloneOrLockHeld("setReceivedRewardsToday", false);
		limax.zdb.Logs.logObject(this, "receivedRewardsToday");
		this.receivedRewardsToday = _v_;
	}

	public void setReceivedTimeToday(long _v_) { 
		this.verifyStandaloneOrLockHeld("setReceivedTimeToday", false);
		limax.zdb.Logs.logObject(this, "receivedTimeToday");
		this.receivedTimeToday = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		PromoterInfo _o_ = null;
		if ( _o1_ instanceof PromoterInfo ) _o_ = (PromoterInfo)_o1_;
		else return false;
		if (!this.inviteeUnionidList.equals(_o_.inviteeUnionidList)) return false;
		if (this.totalEffectiveInvitee != _o_.totalEffectiveInvitee) return false;
		if (this.unreceivedRewards != _o_.unreceivedRewards) return false;
		if (this.totalRewards != _o_.totalRewards) return false;
		if (this.inviteeCountThisCycle != _o_.inviteeCountThisCycle) return false;
		if (this.rewardsThisCycle != _o_.rewardsThisCycle) return false;
		if (this.cycleStartTime != _o_.cycleStartTime) return false;
		if (this.receivedCountToday != _o_.receivedCountToday) return false;
		if (this.receivedRewardsToday != _o_.receivedRewardsToday) return false;
		if (this.receivedTimeToday != _o_.receivedTimeToday) return false;
		if (!this.rewardList.equals(_o_.rewardList)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.inviteeUnionidList.hashCode();
		_h_ += _h_ * 31 + this.totalEffectiveInvitee;
		_h_ += _h_ * 31 + Float.floatToIntBits(this.unreceivedRewards);
		_h_ += _h_ * 31 + Float.floatToIntBits(this.totalRewards);
		_h_ += _h_ * 31 + this.inviteeCountThisCycle;
		_h_ += _h_ * 31 + Float.floatToIntBits(this.rewardsThisCycle);
		_h_ += _h_ * 31 + (int)(this.cycleStartTime ^ (this.cycleStartTime >>> 32));
		_h_ += _h_ * 31 + this.receivedCountToday;
		_h_ += _h_ * 31 + Float.floatToIntBits(this.receivedRewardsToday);
		_h_ += _h_ * 31 + (int)(this.receivedTimeToday ^ (this.receivedTimeToday >>> 32));
		_h_ += _h_ * 31 + this.rewardList.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.inviteeUnionidList).append(",");
		_sb_.append(this.totalEffectiveInvitee).append(",");
		_sb_.append(this.unreceivedRewards).append(",");
		_sb_.append(this.totalRewards).append(",");
		_sb_.append(this.inviteeCountThisCycle).append(",");
		_sb_.append(this.rewardsThisCycle).append(",");
		_sb_.append(this.cycleStartTime).append(",");
		_sb_.append(this.receivedCountToday).append(",");
		_sb_.append(this.receivedRewardsToday).append(",");
		_sb_.append(this.receivedTimeToday).append(",");
		_sb_.append(this.rewardList).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
