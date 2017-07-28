package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RuntimeBattleInfo extends limax.zdb.XBean {
    private int totalRoomCount; 
    private int positiveRoomCount; 
    private int silentRoomCount; 

    RuntimeBattleInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public RuntimeBattleInfo() {
		this(null, null);
	}

	public RuntimeBattleInfo(RuntimeBattleInfo _o_) {
		this(_o_, null, null);
	}

	RuntimeBattleInfo(RuntimeBattleInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RuntimeBattleInfo", true);
        this.totalRoomCount = _o_.totalRoomCount;
        this.positiveRoomCount = _o_.positiveRoomCount;
        this.silentRoomCount = _o_.silentRoomCount;
	}

	public void copyFrom(RuntimeBattleInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRuntimeBattleInfo", true);
		this.verifyStandaloneOrLockHeld("copyToRuntimeBattleInfo", false);
        limax.zdb.Logs.logObject(this, "totalRoomCount");
        this.totalRoomCount = _o_.totalRoomCount;
        limax.zdb.Logs.logObject(this, "positiveRoomCount");
        this.positiveRoomCount = _o_.positiveRoomCount;
        limax.zdb.Logs.logObject(this, "silentRoomCount");
        this.silentRoomCount = _o_.silentRoomCount;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.totalRoomCount);
        _os_.marshal(this.positiveRoomCount);
        _os_.marshal(this.silentRoomCount);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.totalRoomCount = _os_.unmarshal_int();
		this.positiveRoomCount = _os_.unmarshal_int();
		this.silentRoomCount = _os_.unmarshal_int();
		return _os_;
	}

	public int getTotalRoomCount() { 
		this.verifyStandaloneOrLockHeld("getTotalRoomCount", true);
		return this.totalRoomCount;
	}

	public int getPositiveRoomCount() { 
		this.verifyStandaloneOrLockHeld("getPositiveRoomCount", true);
		return this.positiveRoomCount;
	}

	public int getSilentRoomCount() { 
		this.verifyStandaloneOrLockHeld("getSilentRoomCount", true);
		return this.silentRoomCount;
	}

	public void setTotalRoomCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setTotalRoomCount", false);
		limax.zdb.Logs.logObject(this, "totalRoomCount");
		this.totalRoomCount = _v_;
	}

	public void setPositiveRoomCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPositiveRoomCount", false);
		limax.zdb.Logs.logObject(this, "positiveRoomCount");
		this.positiveRoomCount = _v_;
	}

	public void setSilentRoomCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setSilentRoomCount", false);
		limax.zdb.Logs.logObject(this, "silentRoomCount");
		this.silentRoomCount = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RuntimeBattleInfo _o_ = null;
		if ( _o1_ instanceof RuntimeBattleInfo ) _o_ = (RuntimeBattleInfo)_o1_;
		else return false;
		if (this.totalRoomCount != _o_.totalRoomCount) return false;
		if (this.positiveRoomCount != _o_.positiveRoomCount) return false;
		if (this.silentRoomCount != _o_.silentRoomCount) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.totalRoomCount;
		_h_ += _h_ * 31 + this.positiveRoomCount;
		_h_ += _h_ * 31 + this.silentRoomCount;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.totalRoomCount).append(",");
		_sb_.append(this.positiveRoomCount).append(",");
		_sb_.append(this.silentRoomCount).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
