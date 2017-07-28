package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ClubRoomInfo extends limax.zdb.XBean {
    private int roomId; 
    private int creator; 
    private java.util.ArrayList<xbean.ClubRoleBaseInfo> player; 
    private int maxPlayer; 
    private java.util.ArrayList<Integer> gameplays; 
    private int clubId; 
    private boolean enableSubCard; 
    private xbean.RoomCost roomCostSnap; 
    private int roundCount; 
    private int battleId; 

    ClubRoomInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        player = new java.util.ArrayList<xbean.ClubRoleBaseInfo>();
        gameplays = new java.util.ArrayList<Integer>();
        roomCostSnap = new RoomCost(this, "roomCostSnap");
	}

	public ClubRoomInfo() {
		this(null, null);
	}

	public ClubRoomInfo(ClubRoomInfo _o_) {
		this(_o_, null, null);
	}

	ClubRoomInfo(ClubRoomInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ClubRoomInfo", true);
        this.roomId = _o_.roomId;
        this.creator = _o_.creator;
        this.player = new java.util.ArrayList<xbean.ClubRoleBaseInfo>();
        _o_.player.forEach(_v_ -> this.player.add(new ClubRoleBaseInfo(_v_, this, "player")));
        this.maxPlayer = _o_.maxPlayer;
        this.gameplays = new java.util.ArrayList<Integer>();
        this.gameplays.addAll(_o_.gameplays);
        this.clubId = _o_.clubId;
        this.enableSubCard = _o_.enableSubCard;
        this.roomCostSnap = new RoomCost(_o_.roomCostSnap, this, "roomCostSnap");
        this.roundCount = _o_.roundCount;
        this.battleId = _o_.battleId;
	}

	public void copyFrom(ClubRoomInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromClubRoomInfo", true);
		this.verifyStandaloneOrLockHeld("copyToClubRoomInfo", false);
        limax.zdb.Logs.logObject(this, "roomId");
        this.roomId = _o_.roomId;
        limax.zdb.Logs.logObject(this, "creator");
        this.creator = _o_.creator;
        java.util.List<xbean.ClubRoleBaseInfo> this_player = limax.zdb.Logs.logList(this, "player", ()->{});
        this_player.clear();
        _o_.player.forEach(_v_ -> this_player.add(new ClubRoleBaseInfo(_v_)));
        limax.zdb.Logs.logObject(this, "maxPlayer");
        this.maxPlayer = _o_.maxPlayer;
        java.util.List<Integer> this_gameplays = limax.zdb.Logs.logList(this, "gameplays", ()->{});
        this_gameplays.clear();
        this_gameplays.addAll(_o_.gameplays);
        limax.zdb.Logs.logObject(this, "clubId");
        this.clubId = _o_.clubId;
        limax.zdb.Logs.logObject(this, "enableSubCard");
        this.enableSubCard = _o_.enableSubCard;
        this.roomCostSnap.copyFrom(_o_.roomCostSnap);
        limax.zdb.Logs.logObject(this, "roundCount");
        this.roundCount = _o_.roundCount;
        limax.zdb.Logs.logObject(this, "battleId");
        this.battleId = _o_.battleId;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.roomId);
        _os_.marshal(this.creator);
        _os_.marshal_size(this.player.size());
        for (xbean.ClubRoleBaseInfo _v_ : this.player) {
        	_os_.marshal(_v_);
        }
        _os_.marshal(this.maxPlayer);
        _os_.marshal_size(this.gameplays.size());
        for (Integer _v_ : this.gameplays) {
        	_os_.marshal(_v_);
        }
        _os_.marshal(this.clubId);
        _os_.marshal(this.enableSubCard);
        _os_.marshal(this.roomCostSnap);
        _os_.marshal(this.roundCount);
        _os_.marshal(this.battleId);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.roomId = _os_.unmarshal_int();
		this.creator = _os_.unmarshal_int();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.ClubRoleBaseInfo _v_ = new ClubRoleBaseInfo(this, "player");
			_v_.unmarshal(_os_);
			this.player.add(_v_);
		}
		this.maxPlayer = _os_.unmarshal_int();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _v_ = _os_.unmarshal_int();
			this.gameplays.add(_v_);
		}
		this.clubId = _os_.unmarshal_int();
		this.enableSubCard = _os_.unmarshal_boolean();
		this.roomCostSnap.unmarshal(_os_);
		this.roundCount = _os_.unmarshal_int();
		this.battleId = _os_.unmarshal_int();
		return _os_;
	}

	public int getRoomId() { 
		this.verifyStandaloneOrLockHeld("getRoomId", true);
		return this.roomId;
	}

	public int getCreator() { 
		this.verifyStandaloneOrLockHeld("getCreator", true);
		return this.creator;
	}

	public java.util.List<xbean.ClubRoleBaseInfo> getPlayer() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "player", this.verifyStandaloneOrLockHeld("getPlayer", true)) : this.player;
	}

	public int getMaxPlayer() { 
		this.verifyStandaloneOrLockHeld("getMaxPlayer", true);
		return this.maxPlayer;
	}

	public java.util.List<Integer> getGameplays() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "gameplays", this.verifyStandaloneOrLockHeld("getGameplays", true)) : this.gameplays;
	}

	public int getClubId() { 
		this.verifyStandaloneOrLockHeld("getClubId", true);
		return this.clubId;
	}

	public boolean getEnableSubCard() { 
		this.verifyStandaloneOrLockHeld("getEnableSubCard", true);
		return this.enableSubCard;
	}

	public xbean.RoomCost getRoomCostSnap() { 
		this.verifyStandaloneOrLockHeld("getRoomCostSnap", true);
		return this.roomCostSnap;
	}

	public int getRoundCount() { 
		this.verifyStandaloneOrLockHeld("getRoundCount", true);
		return this.roundCount;
	}

	public int getBattleId() { 
		this.verifyStandaloneOrLockHeld("getBattleId", true);
		return this.battleId;
	}

	public void setRoomId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoomId", false);
		limax.zdb.Logs.logObject(this, "roomId");
		this.roomId = _v_;
	}

	public void setCreator(int _v_) { 
		this.verifyStandaloneOrLockHeld("setCreator", false);
		limax.zdb.Logs.logObject(this, "creator");
		this.creator = _v_;
	}

	public void setMaxPlayer(int _v_) { 
		this.verifyStandaloneOrLockHeld("setMaxPlayer", false);
		limax.zdb.Logs.logObject(this, "maxPlayer");
		this.maxPlayer = _v_;
	}

	public void setClubId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setClubId", false);
		limax.zdb.Logs.logObject(this, "clubId");
		this.clubId = _v_;
	}

	public void setEnableSubCard(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setEnableSubCard", false);
		limax.zdb.Logs.logObject(this, "enableSubCard");
		this.enableSubCard = _v_;
	}

	public void setRoundCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoundCount", false);
		limax.zdb.Logs.logObject(this, "roundCount");
		this.roundCount = _v_;
	}

	public void setBattleId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setBattleId", false);
		limax.zdb.Logs.logObject(this, "battleId");
		this.battleId = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ClubRoomInfo _o_ = null;
		if ( _o1_ instanceof ClubRoomInfo ) _o_ = (ClubRoomInfo)_o1_;
		else return false;
		if (this.roomId != _o_.roomId) return false;
		if (this.creator != _o_.creator) return false;
		if (!this.player.equals(_o_.player)) return false;
		if (this.maxPlayer != _o_.maxPlayer) return false;
		if (!this.gameplays.equals(_o_.gameplays)) return false;
		if (this.clubId != _o_.clubId) return false;
		if (this.enableSubCard != _o_.enableSubCard) return false;
		if (!this.roomCostSnap.equals(_o_.roomCostSnap)) return false;
		if (this.roundCount != _o_.roundCount) return false;
		if (this.battleId != _o_.battleId) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roomId;
		_h_ += _h_ * 31 + this.creator;
		_h_ += _h_ * 31 + this.player.hashCode();
		_h_ += _h_ * 31 + this.maxPlayer;
		_h_ += _h_ * 31 + this.gameplays.hashCode();
		_h_ += _h_ * 31 + this.clubId;
		_h_ += _h_ * 31 + (this.enableSubCard ? 1231 : 1237);
		_h_ += _h_ * 31 + this.roomCostSnap.hashCode();
		_h_ += _h_ * 31 + this.roundCount;
		_h_ += _h_ * 31 + this.battleId;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roomId).append(",");
		_sb_.append(this.creator).append(",");
		_sb_.append(this.player).append(",");
		_sb_.append(this.maxPlayer).append(",");
		_sb_.append(this.gameplays).append(",");
		_sb_.append(this.clubId).append(",");
		_sb_.append(this.enableSubCard).append(",");
		_sb_.append(this.roomCostSnap).append(",");
		_sb_.append(this.roundCount).append(",");
		_sb_.append(this.battleId).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
