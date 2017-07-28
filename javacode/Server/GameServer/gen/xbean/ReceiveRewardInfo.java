package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ReceiveRewardInfo extends limax.zdb.XBean {
    private long id; 
    private long date; 
    private float receivedThisTime; 
    private float receivedTotal; 
    private int isHandled; 
    private long handleTime; 
    private String gmName; 

    ReceiveRewardInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        gmName = "";
	}

	public ReceiveRewardInfo() {
		this(null, null);
	}

	public ReceiveRewardInfo(ReceiveRewardInfo _o_) {
		this(_o_, null, null);
	}

	ReceiveRewardInfo(ReceiveRewardInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ReceiveRewardInfo", true);
        this.id = _o_.id;
        this.date = _o_.date;
        this.receivedThisTime = _o_.receivedThisTime;
        this.receivedTotal = _o_.receivedTotal;
        this.isHandled = _o_.isHandled;
        this.handleTime = _o_.handleTime;
        this.gmName = _o_.gmName;
	}

	public void copyFrom(ReceiveRewardInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromReceiveRewardInfo", true);
		this.verifyStandaloneOrLockHeld("copyToReceiveRewardInfo", false);
        limax.zdb.Logs.logObject(this, "id");
        this.id = _o_.id;
        limax.zdb.Logs.logObject(this, "date");
        this.date = _o_.date;
        limax.zdb.Logs.logObject(this, "receivedThisTime");
        this.receivedThisTime = _o_.receivedThisTime;
        limax.zdb.Logs.logObject(this, "receivedTotal");
        this.receivedTotal = _o_.receivedTotal;
        limax.zdb.Logs.logObject(this, "isHandled");
        this.isHandled = _o_.isHandled;
        limax.zdb.Logs.logObject(this, "handleTime");
        this.handleTime = _o_.handleTime;
        limax.zdb.Logs.logObject(this, "gmName");
        this.gmName = _o_.gmName;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.id);
        _os_.marshal(this.date);
        _os_.marshal(this.receivedThisTime);
        _os_.marshal(this.receivedTotal);
        _os_.marshal(this.isHandled);
        _os_.marshal(this.handleTime);
        _os_.marshal(this.gmName);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.id = _os_.unmarshal_long();
		this.date = _os_.unmarshal_long();
		this.receivedThisTime = _os_.unmarshal_float();
		this.receivedTotal = _os_.unmarshal_float();
		this.isHandled = _os_.unmarshal_int();
		this.handleTime = _os_.unmarshal_long();
		this.gmName = _os_.unmarshal_String();
		return _os_;
	}

	public long getId() { 
		this.verifyStandaloneOrLockHeld("getId", true);
		return this.id;
	}

	public long getDate() { 
		this.verifyStandaloneOrLockHeld("getDate", true);
		return this.date;
	}

	public float getReceivedThisTime() { 
		this.verifyStandaloneOrLockHeld("getReceivedThisTime", true);
		return this.receivedThisTime;
	}

	public float getReceivedTotal() { 
		this.verifyStandaloneOrLockHeld("getReceivedTotal", true);
		return this.receivedTotal;
	}

	public int getIsHandled() { 
		this.verifyStandaloneOrLockHeld("getIsHandled", true);
		return this.isHandled;
	}

	public long getHandleTime() { 
		this.verifyStandaloneOrLockHeld("getHandleTime", true);
		return this.handleTime;
	}

	public String getGmName() { 
		this.verifyStandaloneOrLockHeld("getGmName", true);
		return this.gmName;
	}

	public void setId(long _v_) { 
		this.verifyStandaloneOrLockHeld("setId", false);
		limax.zdb.Logs.logObject(this, "id");
		this.id = _v_;
	}

	public void setDate(long _v_) { 
		this.verifyStandaloneOrLockHeld("setDate", false);
		limax.zdb.Logs.logObject(this, "date");
		this.date = _v_;
	}

	public void setReceivedThisTime(float _v_) { 
		this.verifyStandaloneOrLockHeld("setReceivedThisTime", false);
		limax.zdb.Logs.logObject(this, "receivedThisTime");
		this.receivedThisTime = _v_;
	}

	public void setReceivedTotal(float _v_) { 
		this.verifyStandaloneOrLockHeld("setReceivedTotal", false);
		limax.zdb.Logs.logObject(this, "receivedTotal");
		this.receivedTotal = _v_;
	}

	public void setIsHandled(int _v_) { 
		this.verifyStandaloneOrLockHeld("setIsHandled", false);
		limax.zdb.Logs.logObject(this, "isHandled");
		this.isHandled = _v_;
	}

	public void setHandleTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setHandleTime", false);
		limax.zdb.Logs.logObject(this, "handleTime");
		this.handleTime = _v_;
	}

	public void setGmName(String _v_) { 
		this.verifyStandaloneOrLockHeld("setGmName", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "gmName");
		this.gmName = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ReceiveRewardInfo _o_ = null;
		if ( _o1_ instanceof ReceiveRewardInfo ) _o_ = (ReceiveRewardInfo)_o1_;
		else return false;
		if (this.id != _o_.id) return false;
		if (this.date != _o_.date) return false;
		if (this.receivedThisTime != _o_.receivedThisTime) return false;
		if (this.receivedTotal != _o_.receivedTotal) return false;
		if (this.isHandled != _o_.isHandled) return false;
		if (this.handleTime != _o_.handleTime) return false;
		if (!this.gmName.equals(_o_.gmName)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + (int)(this.id ^ (this.id >>> 32));
		_h_ += _h_ * 31 + (int)(this.date ^ (this.date >>> 32));
		_h_ += _h_ * 31 + Float.floatToIntBits(this.receivedThisTime);
		_h_ += _h_ * 31 + Float.floatToIntBits(this.receivedTotal);
		_h_ += _h_ * 31 + this.isHandled;
		_h_ += _h_ * 31 + (int)(this.handleTime ^ (this.handleTime >>> 32));
		_h_ += _h_ * 31 + this.gmName.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.id).append(",");
		_sb_.append(this.date).append(",");
		_sb_.append(this.receivedThisTime).append(",");
		_sb_.append(this.receivedTotal).append(",");
		_sb_.append(this.isHandled).append(",");
		_sb_.append(this.handleTime).append(",");
		_sb_.append("T").append(this.gmName.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
