package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class LimitedCostlessActivity extends limax.zdb.XBean {
    private long startTime; 
    private long endTime; 
    private String activityName; 
    private int roomType; 
    private boolean isCancel; 
    private int opType; 

    LimitedCostlessActivity(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        activityName = "";
	}

	public LimitedCostlessActivity() {
		this(null, null);
	}

	public LimitedCostlessActivity(LimitedCostlessActivity _o_) {
		this(_o_, null, null);
	}

	LimitedCostlessActivity(LimitedCostlessActivity _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.LimitedCostlessActivity", true);
        this.startTime = _o_.startTime;
        this.endTime = _o_.endTime;
        this.activityName = _o_.activityName;
        this.roomType = _o_.roomType;
        this.isCancel = _o_.isCancel;
        this.opType = _o_.opType;
	}

	public void copyFrom(LimitedCostlessActivity _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromLimitedCostlessActivity", true);
		this.verifyStandaloneOrLockHeld("copyToLimitedCostlessActivity", false);
        limax.zdb.Logs.logObject(this, "startTime");
        this.startTime = _o_.startTime;
        limax.zdb.Logs.logObject(this, "endTime");
        this.endTime = _o_.endTime;
        limax.zdb.Logs.logObject(this, "activityName");
        this.activityName = _o_.activityName;
        limax.zdb.Logs.logObject(this, "roomType");
        this.roomType = _o_.roomType;
        limax.zdb.Logs.logObject(this, "isCancel");
        this.isCancel = _o_.isCancel;
        limax.zdb.Logs.logObject(this, "opType");
        this.opType = _o_.opType;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.startTime);
        _os_.marshal(this.endTime);
        _os_.marshal(this.activityName);
        _os_.marshal(this.roomType);
        _os_.marshal(this.isCancel);
        _os_.marshal(this.opType);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.startTime = _os_.unmarshal_long();
		this.endTime = _os_.unmarshal_long();
		this.activityName = _os_.unmarshal_String();
		this.roomType = _os_.unmarshal_int();
		this.isCancel = _os_.unmarshal_boolean();
		this.opType = _os_.unmarshal_int();
		return _os_;
	}

	public long getStartTime() { 
		this.verifyStandaloneOrLockHeld("getStartTime", true);
		return this.startTime;
	}

	public long getEndTime() { 
		this.verifyStandaloneOrLockHeld("getEndTime", true);
		return this.endTime;
	}

	public String getActivityName() { 
		this.verifyStandaloneOrLockHeld("getActivityName", true);
		return this.activityName;
	}

	public int getRoomType() { 
		this.verifyStandaloneOrLockHeld("getRoomType", true);
		return this.roomType;
	}

	public boolean getIsCancel() { 
		this.verifyStandaloneOrLockHeld("getIsCancel", true);
		return this.isCancel;
	}

	public int getOpType() { 
		this.verifyStandaloneOrLockHeld("getOpType", true);
		return this.opType;
	}

	public void setStartTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setStartTime", false);
		limax.zdb.Logs.logObject(this, "startTime");
		this.startTime = _v_;
	}

	public void setEndTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setEndTime", false);
		limax.zdb.Logs.logObject(this, "endTime");
		this.endTime = _v_;
	}

	public void setActivityName(String _v_) { 
		this.verifyStandaloneOrLockHeld("setActivityName", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "activityName");
		this.activityName = _v_;
	}

	public void setRoomType(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoomType", false);
		limax.zdb.Logs.logObject(this, "roomType");
		this.roomType = _v_;
	}

	public void setIsCancel(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setIsCancel", false);
		limax.zdb.Logs.logObject(this, "isCancel");
		this.isCancel = _v_;
	}

	public void setOpType(int _v_) { 
		this.verifyStandaloneOrLockHeld("setOpType", false);
		limax.zdb.Logs.logObject(this, "opType");
		this.opType = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		LimitedCostlessActivity _o_ = null;
		if ( _o1_ instanceof LimitedCostlessActivity ) _o_ = (LimitedCostlessActivity)_o1_;
		else return false;
		if (this.startTime != _o_.startTime) return false;
		if (this.endTime != _o_.endTime) return false;
		if (!this.activityName.equals(_o_.activityName)) return false;
		if (this.roomType != _o_.roomType) return false;
		if (this.isCancel != _o_.isCancel) return false;
		if (this.opType != _o_.opType) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + (int)(this.startTime ^ (this.startTime >>> 32));
		_h_ += _h_ * 31 + (int)(this.endTime ^ (this.endTime >>> 32));
		_h_ += _h_ * 31 + this.activityName.hashCode();
		_h_ += _h_ * 31 + this.roomType;
		_h_ += _h_ * 31 + (this.isCancel ? 1231 : 1237);
		_h_ += _h_ * 31 + this.opType;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.startTime).append(",");
		_sb_.append(this.endTime).append(",");
		_sb_.append("T").append(this.activityName.length()).append(",");
		_sb_.append(this.roomType).append(",");
		_sb_.append(this.isCancel).append(",");
		_sb_.append(this.opType).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
