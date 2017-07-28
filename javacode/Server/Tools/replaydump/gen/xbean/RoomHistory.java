package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoomHistory extends limax.zdb.XBean {
    private int roomId; 
    private long createTime; 
    private int roundType; 
    private int roundCount; 
    private int playerMaxCardCount; 
    private java.util.ArrayList<Integer> gameplays; 
    private java.util.HashMap<Integer, xbean.RoomHistoryPlayerInfo> playerInfo; 
    private java.util.ArrayList<xbean.RoundRecord> roundRecord; 
    private boolean enableMutilHu; 

    RoomHistory(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        gameplays = new java.util.ArrayList<Integer>();
        playerInfo = new java.util.HashMap<Integer, xbean.RoomHistoryPlayerInfo>();
        roundRecord = new java.util.ArrayList<xbean.RoundRecord>();
	}

	public RoomHistory() {
		this(null, null);
	}

	public RoomHistory(RoomHistory _o_) {
		this(_o_, null, null);
	}

	RoomHistory(RoomHistory _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoomHistory", true);
        this.roomId = _o_.roomId;
        this.createTime = _o_.createTime;
        this.roundType = _o_.roundType;
        this.roundCount = _o_.roundCount;
        this.playerMaxCardCount = _o_.playerMaxCardCount;
        this.gameplays = new java.util.ArrayList<Integer>();
        this.gameplays.addAll(_o_.gameplays);
        this.playerInfo = new java.util.HashMap<Integer, xbean.RoomHistoryPlayerInfo>();
        _o_.playerInfo.forEach((_k_, _v_) -> this.playerInfo.put(_k_, new RoomHistoryPlayerInfo(_v_, this, "playerInfo")));
        this.roundRecord = new java.util.ArrayList<xbean.RoundRecord>();
        _o_.roundRecord.forEach(_v_ -> this.roundRecord.add(new RoundRecord(_v_, this, "roundRecord")));
        this.enableMutilHu = _o_.enableMutilHu;
	}

	public void copyFrom(RoomHistory _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoomHistory", true);
		this.verifyStandaloneOrLockHeld("copyToRoomHistory", false);
        limax.zdb.Logs.logObject(this, "roomId");
        this.roomId = _o_.roomId;
        limax.zdb.Logs.logObject(this, "createTime");
        this.createTime = _o_.createTime;
        limax.zdb.Logs.logObject(this, "roundType");
        this.roundType = _o_.roundType;
        limax.zdb.Logs.logObject(this, "roundCount");
        this.roundCount = _o_.roundCount;
        limax.zdb.Logs.logObject(this, "playerMaxCardCount");
        this.playerMaxCardCount = _o_.playerMaxCardCount;
        java.util.List<Integer> this_gameplays = limax.zdb.Logs.logList(this, "gameplays", ()->{});
        this_gameplays.clear();
        this_gameplays.addAll(_o_.gameplays);
        java.util.Map<Integer, xbean.RoomHistoryPlayerInfo> this_playerInfo = limax.zdb.Logs.logMap(this, "playerInfo", ()->{});
        this_playerInfo.clear();
        _o_.playerInfo.forEach((_k_, _v_) -> this_playerInfo.put(_k_, new RoomHistoryPlayerInfo(_v_)));
        java.util.List<xbean.RoundRecord> this_roundRecord = limax.zdb.Logs.logList(this, "roundRecord", ()->{});
        this_roundRecord.clear();
        _o_.roundRecord.forEach(_v_ -> this_roundRecord.add(new RoundRecord(_v_)));
        limax.zdb.Logs.logObject(this, "enableMutilHu");
        this.enableMutilHu = _o_.enableMutilHu;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.roomId);
        _os_.marshal(this.createTime);
        _os_.marshal(this.roundType);
        _os_.marshal(this.roundCount);
        _os_.marshal(this.playerMaxCardCount);
        _os_.marshal_size(this.gameplays.size());
        for (Integer _v_ : this.gameplays) {
        	_os_.marshal(_v_);
        }
        _os_.marshal_size(this.playerInfo.size());
        for (java.util.Map.Entry<Integer, xbean.RoomHistoryPlayerInfo> _e_ : this.playerInfo.entrySet()) {
        	_os_.marshal(_e_.getKey());
        	_os_.marshal(_e_.getValue());
        }
        _os_.marshal_size(this.roundRecord.size());
        for (xbean.RoundRecord _v_ : this.roundRecord) {
        	_os_.marshal(_v_);
        }
        _os_.marshal(this.enableMutilHu);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.roomId = _os_.unmarshal_int();
		this.createTime = _os_.unmarshal_long();
		this.roundType = _os_.unmarshal_int();
		this.roundCount = _os_.unmarshal_int();
		this.playerMaxCardCount = _os_.unmarshal_int();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _v_ = _os_.unmarshal_int();
			this.gameplays.add(_v_);
		}
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _k_ = _os_.unmarshal_int();
			xbean.RoomHistoryPlayerInfo _v_ = new RoomHistoryPlayerInfo(this, "playerInfo");
			_v_.unmarshal(_os_);
			this.playerInfo.put(_k_, _v_);
		}
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.RoundRecord _v_ = new RoundRecord(this, "roundRecord");
			_v_.unmarshal(_os_);
			this.roundRecord.add(_v_);
		}
		this.enableMutilHu = _os_.unmarshal_boolean();
		return _os_;
	}

	public int getRoomId() { 
		this.verifyStandaloneOrLockHeld("getRoomId", true);
		return this.roomId;
	}

	public long getCreateTime() { 
		this.verifyStandaloneOrLockHeld("getCreateTime", true);
		return this.createTime;
	}

	public int getRoundType() { 
		this.verifyStandaloneOrLockHeld("getRoundType", true);
		return this.roundType;
	}

	public int getRoundCount() { 
		this.verifyStandaloneOrLockHeld("getRoundCount", true);
		return this.roundCount;
	}

	public int getPlayerMaxCardCount() { 
		this.verifyStandaloneOrLockHeld("getPlayerMaxCardCount", true);
		return this.playerMaxCardCount;
	}

	public java.util.List<Integer> getGameplays() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "gameplays", this.verifyStandaloneOrLockHeld("getGameplays", true)) : this.gameplays;
	}

	public java.util.Map<Integer, xbean.RoomHistoryPlayerInfo> getPlayerInfo() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logMap(this, "playerInfo", this.verifyStandaloneOrLockHeld("getPlayerInfo", true)) : this.playerInfo;
	}

	public java.util.List<xbean.RoundRecord> getRoundRecord() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "roundRecord", this.verifyStandaloneOrLockHeld("getRoundRecord", true)) : this.roundRecord;
	}

	public boolean getEnableMutilHu() { 
		this.verifyStandaloneOrLockHeld("getEnableMutilHu", true);
		return this.enableMutilHu;
	}

	public void setRoomId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoomId", false);
		limax.zdb.Logs.logObject(this, "roomId");
		this.roomId = _v_;
	}

	public void setCreateTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setCreateTime", false);
		limax.zdb.Logs.logObject(this, "createTime");
		this.createTime = _v_;
	}

	public void setRoundType(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoundType", false);
		limax.zdb.Logs.logObject(this, "roundType");
		this.roundType = _v_;
	}

	public void setRoundCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoundCount", false);
		limax.zdb.Logs.logObject(this, "roundCount");
		this.roundCount = _v_;
	}

	public void setPlayerMaxCardCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPlayerMaxCardCount", false);
		limax.zdb.Logs.logObject(this, "playerMaxCardCount");
		this.playerMaxCardCount = _v_;
	}

	public void setEnableMutilHu(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setEnableMutilHu", false);
		limax.zdb.Logs.logObject(this, "enableMutilHu");
		this.enableMutilHu = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoomHistory _o_ = null;
		if ( _o1_ instanceof RoomHistory ) _o_ = (RoomHistory)_o1_;
		else return false;
		if (this.roomId != _o_.roomId) return false;
		if (this.createTime != _o_.createTime) return false;
		if (this.roundType != _o_.roundType) return false;
		if (this.roundCount != _o_.roundCount) return false;
		if (this.playerMaxCardCount != _o_.playerMaxCardCount) return false;
		if (!this.gameplays.equals(_o_.gameplays)) return false;
		if (!this.playerInfo.equals(_o_.playerInfo)) return false;
		if (!this.roundRecord.equals(_o_.roundRecord)) return false;
		if (this.enableMutilHu != _o_.enableMutilHu) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roomId;
		_h_ += _h_ * 31 + (int)(this.createTime ^ (this.createTime >>> 32));
		_h_ += _h_ * 31 + this.roundType;
		_h_ += _h_ * 31 + this.roundCount;
		_h_ += _h_ * 31 + this.playerMaxCardCount;
		_h_ += _h_ * 31 + this.gameplays.hashCode();
		_h_ += _h_ * 31 + this.playerInfo.hashCode();
		_h_ += _h_ * 31 + this.roundRecord.hashCode();
		_h_ += _h_ * 31 + (this.enableMutilHu ? 1231 : 1237);
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roomId).append(",");
		_sb_.append(this.createTime).append(",");
		_sb_.append(this.roundType).append(",");
		_sb_.append(this.roundCount).append(",");
		_sb_.append(this.playerMaxCardCount).append(",");
		_sb_.append(this.gameplays).append(",");
		_sb_.append(this.playerInfo).append(",");
		_sb_.append(this.roundRecord).append(",");
		_sb_.append(this.enableMutilHu).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
