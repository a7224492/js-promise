package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class InviteeInfo extends limax.zdb.XBean {
    private int roundCount; 
    private int finished; 
    private long finishTime; 
    private long joinTime; 
    private String promoterUnionId; 

    InviteeInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        promoterUnionId = "";
	}

	public InviteeInfo() {
		this(null, null);
	}

	public InviteeInfo(InviteeInfo _o_) {
		this(_o_, null, null);
	}

	InviteeInfo(InviteeInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.InviteeInfo", true);
        this.roundCount = _o_.roundCount;
        this.finished = _o_.finished;
        this.finishTime = _o_.finishTime;
        this.joinTime = _o_.joinTime;
        this.promoterUnionId = _o_.promoterUnionId;
	}

	public void copyFrom(InviteeInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromInviteeInfo", true);
		this.verifyStandaloneOrLockHeld("copyToInviteeInfo", false);
        limax.zdb.Logs.logObject(this, "roundCount");
        this.roundCount = _o_.roundCount;
        limax.zdb.Logs.logObject(this, "finished");
        this.finished = _o_.finished;
        limax.zdb.Logs.logObject(this, "finishTime");
        this.finishTime = _o_.finishTime;
        limax.zdb.Logs.logObject(this, "joinTime");
        this.joinTime = _o_.joinTime;
        limax.zdb.Logs.logObject(this, "promoterUnionId");
        this.promoterUnionId = _o_.promoterUnionId;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.roundCount);
        _os_.marshal(this.finished);
        _os_.marshal(this.finishTime);
        _os_.marshal(this.joinTime);
        _os_.marshal(this.promoterUnionId);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.roundCount = _os_.unmarshal_int();
		this.finished = _os_.unmarshal_int();
		this.finishTime = _os_.unmarshal_long();
		this.joinTime = _os_.unmarshal_long();
		this.promoterUnionId = _os_.unmarshal_String();
		return _os_;
	}

	public int getRoundCount() { 
		this.verifyStandaloneOrLockHeld("getRoundCount", true);
		return this.roundCount;
	}

	public int getFinished() { 
		this.verifyStandaloneOrLockHeld("getFinished", true);
		return this.finished;
	}

	public long getFinishTime() { 
		this.verifyStandaloneOrLockHeld("getFinishTime", true);
		return this.finishTime;
	}

	public long getJoinTime() { 
		this.verifyStandaloneOrLockHeld("getJoinTime", true);
		return this.joinTime;
	}

	public String getPromoterUnionId() { 
		this.verifyStandaloneOrLockHeld("getPromoterUnionId", true);
		return this.promoterUnionId;
	}

	public void setRoundCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoundCount", false);
		limax.zdb.Logs.logObject(this, "roundCount");
		this.roundCount = _v_;
	}

	public void setFinished(int _v_) { 
		this.verifyStandaloneOrLockHeld("setFinished", false);
		limax.zdb.Logs.logObject(this, "finished");
		this.finished = _v_;
	}

	public void setFinishTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setFinishTime", false);
		limax.zdb.Logs.logObject(this, "finishTime");
		this.finishTime = _v_;
	}

	public void setJoinTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setJoinTime", false);
		limax.zdb.Logs.logObject(this, "joinTime");
		this.joinTime = _v_;
	}

	public void setPromoterUnionId(String _v_) { 
		this.verifyStandaloneOrLockHeld("setPromoterUnionId", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "promoterUnionId");
		this.promoterUnionId = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		InviteeInfo _o_ = null;
		if ( _o1_ instanceof InviteeInfo ) _o_ = (InviteeInfo)_o1_;
		else return false;
		if (this.roundCount != _o_.roundCount) return false;
		if (this.finished != _o_.finished) return false;
		if (this.finishTime != _o_.finishTime) return false;
		if (this.joinTime != _o_.joinTime) return false;
		if (!this.promoterUnionId.equals(_o_.promoterUnionId)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roundCount;
		_h_ += _h_ * 31 + this.finished;
		_h_ += _h_ * 31 + (int)(this.finishTime ^ (this.finishTime >>> 32));
		_h_ += _h_ * 31 + (int)(this.joinTime ^ (this.joinTime >>> 32));
		_h_ += _h_ * 31 + this.promoterUnionId.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roundCount).append(",");
		_sb_.append(this.finished).append(",");
		_sb_.append(this.finishTime).append(",");
		_sb_.append(this.joinTime).append(",");
		_sb_.append("T").append(this.promoterUnionId.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
