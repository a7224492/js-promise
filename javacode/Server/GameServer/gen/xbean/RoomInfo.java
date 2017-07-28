package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoomInfo extends limax.zdb.XBean {
    private int battleId; 
    private int status; 
    private int roundCount; 
    private boolean isLca; 
    private int clubId; 
    private int payType; 
    private int cost; 

    RoomInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public RoomInfo() {
		this(null, null);
	}

	public RoomInfo(RoomInfo _o_) {
		this(_o_, null, null);
	}

	RoomInfo(RoomInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoomInfo", true);
        this.battleId = _o_.battleId;
        this.status = _o_.status;
        this.roundCount = _o_.roundCount;
        this.isLca = _o_.isLca;
        this.clubId = _o_.clubId;
        this.payType = _o_.payType;
        this.cost = _o_.cost;
	}

	public void copyFrom(RoomInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoomInfo", true);
		this.verifyStandaloneOrLockHeld("copyToRoomInfo", false);
        limax.zdb.Logs.logObject(this, "battleId");
        this.battleId = _o_.battleId;
        limax.zdb.Logs.logObject(this, "status");
        this.status = _o_.status;
        limax.zdb.Logs.logObject(this, "roundCount");
        this.roundCount = _o_.roundCount;
        limax.zdb.Logs.logObject(this, "isLca");
        this.isLca = _o_.isLca;
        limax.zdb.Logs.logObject(this, "clubId");
        this.clubId = _o_.clubId;
        limax.zdb.Logs.logObject(this, "payType");
        this.payType = _o_.payType;
        limax.zdb.Logs.logObject(this, "cost");
        this.cost = _o_.cost;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.battleId);
        _os_.marshal(this.status);
        _os_.marshal(this.roundCount);
        _os_.marshal(this.isLca);
        _os_.marshal(this.clubId);
        _os_.marshal(this.payType);
        _os_.marshal(this.cost);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.battleId = _os_.unmarshal_int();
		this.status = _os_.unmarshal_int();
		this.roundCount = _os_.unmarshal_int();
		this.isLca = _os_.unmarshal_boolean();
		this.clubId = _os_.unmarshal_int();
		this.payType = _os_.unmarshal_int();
		this.cost = _os_.unmarshal_int();
		return _os_;
	}

	public int getBattleId() { 
		this.verifyStandaloneOrLockHeld("getBattleId", true);
		return this.battleId;
	}

	public int getStatus() { 
		this.verifyStandaloneOrLockHeld("getStatus", true);
		return this.status;
	}

	public int getRoundCount() { 
		this.verifyStandaloneOrLockHeld("getRoundCount", true);
		return this.roundCount;
	}

	public boolean getIsLca() { 
		this.verifyStandaloneOrLockHeld("getIsLca", true);
		return this.isLca;
	}

	public int getClubId() { 
		this.verifyStandaloneOrLockHeld("getClubId", true);
		return this.clubId;
	}

	public int getPayType() { 
		this.verifyStandaloneOrLockHeld("getPayType", true);
		return this.payType;
	}

	public int getCost() { 
		this.verifyStandaloneOrLockHeld("getCost", true);
		return this.cost;
	}

	public void setBattleId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setBattleId", false);
		limax.zdb.Logs.logObject(this, "battleId");
		this.battleId = _v_;
	}

	public void setStatus(int _v_) { 
		this.verifyStandaloneOrLockHeld("setStatus", false);
		limax.zdb.Logs.logObject(this, "status");
		this.status = _v_;
	}

	public void setRoundCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoundCount", false);
		limax.zdb.Logs.logObject(this, "roundCount");
		this.roundCount = _v_;
	}

	public void setIsLca(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setIsLca", false);
		limax.zdb.Logs.logObject(this, "isLca");
		this.isLca = _v_;
	}

	public void setClubId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setClubId", false);
		limax.zdb.Logs.logObject(this, "clubId");
		this.clubId = _v_;
	}

	public void setPayType(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPayType", false);
		limax.zdb.Logs.logObject(this, "payType");
		this.payType = _v_;
	}

	public void setCost(int _v_) { 
		this.verifyStandaloneOrLockHeld("setCost", false);
		limax.zdb.Logs.logObject(this, "cost");
		this.cost = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoomInfo _o_ = null;
		if ( _o1_ instanceof RoomInfo ) _o_ = (RoomInfo)_o1_;
		else return false;
		if (this.battleId != _o_.battleId) return false;
		if (this.status != _o_.status) return false;
		if (this.roundCount != _o_.roundCount) return false;
		if (this.isLca != _o_.isLca) return false;
		if (this.clubId != _o_.clubId) return false;
		if (this.payType != _o_.payType) return false;
		if (this.cost != _o_.cost) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.battleId;
		_h_ += _h_ * 31 + this.status;
		_h_ += _h_ * 31 + this.roundCount;
		_h_ += _h_ * 31 + (this.isLca ? 1231 : 1237);
		_h_ += _h_ * 31 + this.clubId;
		_h_ += _h_ * 31 + this.payType;
		_h_ += _h_ * 31 + this.cost;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.battleId).append(",");
		_sb_.append(this.status).append(",");
		_sb_.append(this.roundCount).append(",");
		_sb_.append(this.isLca).append(",");
		_sb_.append(this.clubId).append(",");
		_sb_.append(this.payType).append(",");
		_sb_.append(this.cost).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
