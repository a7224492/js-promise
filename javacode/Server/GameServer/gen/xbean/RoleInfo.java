package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoleInfo extends limax.zdb.XBean {
    private int accountId; 
    private String channel; 
    private String username; 
    private String nickname; 
    private String headImgUrl; 
    private int sex; 
    private int points; 
    private int cardCount; 
    private int totalCostCardCount; 
    private int totalGameCount; 
    private long roleCreateTime; 
    private long lastLoginTime; 
    private java.util.ArrayList<cbean.GlobalRoomId> historyRooms; 
    private java.util.ArrayList<Integer> mergeList; 
    private String unionid; 

    RoleInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        channel = "";
        username = "";
        nickname = "";
        headImgUrl = "";
        historyRooms = new java.util.ArrayList<cbean.GlobalRoomId>();
        mergeList = new java.util.ArrayList<Integer>();
        unionid = "";
	}

	public RoleInfo() {
		this(null, null);
	}

	public RoleInfo(RoleInfo _o_) {
		this(_o_, null, null);
	}

	RoleInfo(RoleInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoleInfo", true);
        this.accountId = _o_.accountId;
        this.channel = _o_.channel;
        this.username = _o_.username;
        this.nickname = _o_.nickname;
        this.headImgUrl = _o_.headImgUrl;
        this.sex = _o_.sex;
        this.points = _o_.points;
        this.cardCount = _o_.cardCount;
        this.totalCostCardCount = _o_.totalCostCardCount;
        this.totalGameCount = _o_.totalGameCount;
        this.roleCreateTime = _o_.roleCreateTime;
        this.lastLoginTime = _o_.lastLoginTime;
        this.historyRooms = new java.util.ArrayList<cbean.GlobalRoomId>();
        this.historyRooms.addAll(_o_.historyRooms);
        this.mergeList = new java.util.ArrayList<Integer>();
        this.mergeList.addAll(_o_.mergeList);
        this.unionid = _o_.unionid;
	}

	public void copyFrom(RoleInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoleInfo", true);
		this.verifyStandaloneOrLockHeld("copyToRoleInfo", false);
        limax.zdb.Logs.logObject(this, "accountId");
        this.accountId = _o_.accountId;
        limax.zdb.Logs.logObject(this, "channel");
        this.channel = _o_.channel;
        limax.zdb.Logs.logObject(this, "username");
        this.username = _o_.username;
        limax.zdb.Logs.logObject(this, "nickname");
        this.nickname = _o_.nickname;
        limax.zdb.Logs.logObject(this, "headImgUrl");
        this.headImgUrl = _o_.headImgUrl;
        limax.zdb.Logs.logObject(this, "sex");
        this.sex = _o_.sex;
        limax.zdb.Logs.logObject(this, "points");
        this.points = _o_.points;
        limax.zdb.Logs.logObject(this, "cardCount");
        this.cardCount = _o_.cardCount;
        limax.zdb.Logs.logObject(this, "totalCostCardCount");
        this.totalCostCardCount = _o_.totalCostCardCount;
        limax.zdb.Logs.logObject(this, "totalGameCount");
        this.totalGameCount = _o_.totalGameCount;
        limax.zdb.Logs.logObject(this, "roleCreateTime");
        this.roleCreateTime = _o_.roleCreateTime;
        limax.zdb.Logs.logObject(this, "lastLoginTime");
        this.lastLoginTime = _o_.lastLoginTime;
        java.util.List<cbean.GlobalRoomId> this_historyRooms = limax.zdb.Logs.logList(this, "historyRooms", ()->{});
        this_historyRooms.clear();
        this_historyRooms.addAll(_o_.historyRooms);
        java.util.List<Integer> this_mergeList = limax.zdb.Logs.logList(this, "mergeList", ()->{});
        this_mergeList.clear();
        this_mergeList.addAll(_o_.mergeList);
        limax.zdb.Logs.logObject(this, "unionid");
        this.unionid = _o_.unionid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.accountId);
        _os_.marshal(this.channel);
        _os_.marshal(this.username);
        _os_.marshal(this.nickname);
        _os_.marshal(this.headImgUrl);
        _os_.marshal(this.sex);
        _os_.marshal(this.points);
        _os_.marshal(this.cardCount);
        _os_.marshal(this.totalCostCardCount);
        _os_.marshal(this.totalGameCount);
        _os_.marshal(this.roleCreateTime);
        _os_.marshal(this.lastLoginTime);
        _os_.marshal_size(this.historyRooms.size());
        for (cbean.GlobalRoomId _v_ : this.historyRooms) {
        	_os_.marshal(_v_);
        }
        _os_.marshal_size(this.mergeList.size());
        for (Integer _v_ : this.mergeList) {
        	_os_.marshal(_v_);
        }
        _os_.marshal(this.unionid);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.accountId = _os_.unmarshal_int();
		this.channel = _os_.unmarshal_String();
		this.username = _os_.unmarshal_String();
		this.nickname = _os_.unmarshal_String();
		this.headImgUrl = _os_.unmarshal_String();
		this.sex = _os_.unmarshal_int();
		this.points = _os_.unmarshal_int();
		this.cardCount = _os_.unmarshal_int();
		this.totalCostCardCount = _os_.unmarshal_int();
		this.totalGameCount = _os_.unmarshal_int();
		this.roleCreateTime = _os_.unmarshal_long();
		this.lastLoginTime = _os_.unmarshal_long();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			cbean.GlobalRoomId _v_ = new cbean.GlobalRoomId();
			_v_.unmarshal(_os_);
			this.historyRooms.add(_v_);
		}
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _v_ = _os_.unmarshal_int();
			this.mergeList.add(_v_);
		}
		this.unionid = _os_.unmarshal_String();
		return _os_;
	}

	public int getAccountId() { 
		this.verifyStandaloneOrLockHeld("getAccountId", true);
		return this.accountId;
	}

	public String getChannel() { 
		this.verifyStandaloneOrLockHeld("getChannel", true);
		return this.channel;
	}

	public String getUsername() { 
		this.verifyStandaloneOrLockHeld("getUsername", true);
		return this.username;
	}

	public String getNickname() { 
		this.verifyStandaloneOrLockHeld("getNickname", true);
		return this.nickname;
	}

	public String getHeadImgUrl() { 
		this.verifyStandaloneOrLockHeld("getHeadImgUrl", true);
		return this.headImgUrl;
	}

	public int getSex() { 
		this.verifyStandaloneOrLockHeld("getSex", true);
		return this.sex;
	}

	public int getPoints() { 
		this.verifyStandaloneOrLockHeld("getPoints", true);
		return this.points;
	}

	public int getCardCount() { 
		this.verifyStandaloneOrLockHeld("getCardCount", true);
		return this.cardCount;
	}

	public int getTotalCostCardCount() { 
		this.verifyStandaloneOrLockHeld("getTotalCostCardCount", true);
		return this.totalCostCardCount;
	}

	public int getTotalGameCount() { 
		this.verifyStandaloneOrLockHeld("getTotalGameCount", true);
		return this.totalGameCount;
	}

	public long getRoleCreateTime() { 
		this.verifyStandaloneOrLockHeld("getRoleCreateTime", true);
		return this.roleCreateTime;
	}

	public long getLastLoginTime() { 
		this.verifyStandaloneOrLockHeld("getLastLoginTime", true);
		return this.lastLoginTime;
	}

	public java.util.List<cbean.GlobalRoomId> getHistoryRooms() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "historyRooms", this.verifyStandaloneOrLockHeld("getHistoryRooms", true)) : this.historyRooms;
	}

	public java.util.List<Integer> getMergeList() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "mergeList", this.verifyStandaloneOrLockHeld("getMergeList", true)) : this.mergeList;
	}

	public String getUnionid() { 
		this.verifyStandaloneOrLockHeld("getUnionid", true);
		return this.unionid;
	}

	public void setAccountId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAccountId", false);
		limax.zdb.Logs.logObject(this, "accountId");
		this.accountId = _v_;
	}

	public void setChannel(String _v_) { 
		this.verifyStandaloneOrLockHeld("setChannel", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "channel");
		this.channel = _v_;
	}

	public void setUsername(String _v_) { 
		this.verifyStandaloneOrLockHeld("setUsername", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "username");
		this.username = _v_;
	}

	public void setNickname(String _v_) { 
		this.verifyStandaloneOrLockHeld("setNickname", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "nickname");
		this.nickname = _v_;
	}

	public void setHeadImgUrl(String _v_) { 
		this.verifyStandaloneOrLockHeld("setHeadImgUrl", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "headImgUrl");
		this.headImgUrl = _v_;
	}

	public void setSex(int _v_) { 
		this.verifyStandaloneOrLockHeld("setSex", false);
		limax.zdb.Logs.logObject(this, "sex");
		this.sex = _v_;
	}

	public void setPoints(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPoints", false);
		limax.zdb.Logs.logObject(this, "points");
		this.points = _v_;
	}

	public void setCardCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setCardCount", false);
		limax.zdb.Logs.logObject(this, "cardCount");
		this.cardCount = _v_;
	}

	public void setTotalCostCardCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setTotalCostCardCount", false);
		limax.zdb.Logs.logObject(this, "totalCostCardCount");
		this.totalCostCardCount = _v_;
	}

	public void setTotalGameCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setTotalGameCount", false);
		limax.zdb.Logs.logObject(this, "totalGameCount");
		this.totalGameCount = _v_;
	}

	public void setRoleCreateTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setRoleCreateTime", false);
		limax.zdb.Logs.logObject(this, "roleCreateTime");
		this.roleCreateTime = _v_;
	}

	public void setLastLoginTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setLastLoginTime", false);
		limax.zdb.Logs.logObject(this, "lastLoginTime");
		this.lastLoginTime = _v_;
	}

	public void setUnionid(String _v_) { 
		this.verifyStandaloneOrLockHeld("setUnionid", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "unionid");
		this.unionid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoleInfo _o_ = null;
		if ( _o1_ instanceof RoleInfo ) _o_ = (RoleInfo)_o1_;
		else return false;
		if (this.accountId != _o_.accountId) return false;
		if (!this.channel.equals(_o_.channel)) return false;
		if (!this.username.equals(_o_.username)) return false;
		if (!this.nickname.equals(_o_.nickname)) return false;
		if (!this.headImgUrl.equals(_o_.headImgUrl)) return false;
		if (this.sex != _o_.sex) return false;
		if (this.points != _o_.points) return false;
		if (this.cardCount != _o_.cardCount) return false;
		if (this.totalCostCardCount != _o_.totalCostCardCount) return false;
		if (this.totalGameCount != _o_.totalGameCount) return false;
		if (this.roleCreateTime != _o_.roleCreateTime) return false;
		if (this.lastLoginTime != _o_.lastLoginTime) return false;
		if (!this.historyRooms.equals(_o_.historyRooms)) return false;
		if (!this.mergeList.equals(_o_.mergeList)) return false;
		if (!this.unionid.equals(_o_.unionid)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.accountId;
		_h_ += _h_ * 31 + this.channel.hashCode();
		_h_ += _h_ * 31 + this.username.hashCode();
		_h_ += _h_ * 31 + this.nickname.hashCode();
		_h_ += _h_ * 31 + this.headImgUrl.hashCode();
		_h_ += _h_ * 31 + this.sex;
		_h_ += _h_ * 31 + this.points;
		_h_ += _h_ * 31 + this.cardCount;
		_h_ += _h_ * 31 + this.totalCostCardCount;
		_h_ += _h_ * 31 + this.totalGameCount;
		_h_ += _h_ * 31 + (int)(this.roleCreateTime ^ (this.roleCreateTime >>> 32));
		_h_ += _h_ * 31 + (int)(this.lastLoginTime ^ (this.lastLoginTime >>> 32));
		_h_ += _h_ * 31 + this.historyRooms.hashCode();
		_h_ += _h_ * 31 + this.mergeList.hashCode();
		_h_ += _h_ * 31 + this.unionid.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.accountId).append(",");
		_sb_.append("T").append(this.channel.length()).append(",");
		_sb_.append("T").append(this.username.length()).append(",");
		_sb_.append("T").append(this.nickname.length()).append(",");
		_sb_.append("T").append(this.headImgUrl.length()).append(",");
		_sb_.append(this.sex).append(",");
		_sb_.append(this.points).append(",");
		_sb_.append(this.cardCount).append(",");
		_sb_.append(this.totalCostCardCount).append(",");
		_sb_.append(this.totalGameCount).append(",");
		_sb_.append(this.roleCreateTime).append(",");
		_sb_.append(this.lastLoginTime).append(",");
		_sb_.append(this.historyRooms).append(",");
		_sb_.append(this.mergeList).append(",");
		_sb_.append("T").append(this.unionid.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
