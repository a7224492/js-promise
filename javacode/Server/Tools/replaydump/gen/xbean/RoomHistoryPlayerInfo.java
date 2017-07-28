package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoomHistoryPlayerInfo extends limax.zdb.XBean {
    private int roleId; 
    private int position; 
    private String nickname; 
    private String headImgUrl; 
    private int sex; 
    private int totalPoint; 

    RoomHistoryPlayerInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        nickname = "";
        headImgUrl = "";
	}

	public RoomHistoryPlayerInfo() {
		this(null, null);
	}

	public RoomHistoryPlayerInfo(RoomHistoryPlayerInfo _o_) {
		this(_o_, null, null);
	}

	RoomHistoryPlayerInfo(RoomHistoryPlayerInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoomHistoryPlayerInfo", true);
        this.roleId = _o_.roleId;
        this.position = _o_.position;
        this.nickname = _o_.nickname;
        this.headImgUrl = _o_.headImgUrl;
        this.sex = _o_.sex;
        this.totalPoint = _o_.totalPoint;
	}

	public void copyFrom(RoomHistoryPlayerInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoomHistoryPlayerInfo", true);
		this.verifyStandaloneOrLockHeld("copyToRoomHistoryPlayerInfo", false);
        limax.zdb.Logs.logObject(this, "roleId");
        this.roleId = _o_.roleId;
        limax.zdb.Logs.logObject(this, "position");
        this.position = _o_.position;
        limax.zdb.Logs.logObject(this, "nickname");
        this.nickname = _o_.nickname;
        limax.zdb.Logs.logObject(this, "headImgUrl");
        this.headImgUrl = _o_.headImgUrl;
        limax.zdb.Logs.logObject(this, "sex");
        this.sex = _o_.sex;
        limax.zdb.Logs.logObject(this, "totalPoint");
        this.totalPoint = _o_.totalPoint;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.roleId);
        _os_.marshal(this.position);
        _os_.marshal(this.nickname);
        _os_.marshal(this.headImgUrl);
        _os_.marshal(this.sex);
        _os_.marshal(this.totalPoint);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.roleId = _os_.unmarshal_int();
		this.position = _os_.unmarshal_int();
		this.nickname = _os_.unmarshal_String();
		this.headImgUrl = _os_.unmarshal_String();
		this.sex = _os_.unmarshal_int();
		this.totalPoint = _os_.unmarshal_int();
		return _os_;
	}

	public int getRoleId() { 
		this.verifyStandaloneOrLockHeld("getRoleId", true);
		return this.roleId;
	}

	public int getPosition() { 
		this.verifyStandaloneOrLockHeld("getPosition", true);
		return this.position;
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

	public int getTotalPoint() { 
		this.verifyStandaloneOrLockHeld("getTotalPoint", true);
		return this.totalPoint;
	}

	public void setRoleId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRoleId", false);
		limax.zdb.Logs.logObject(this, "roleId");
		this.roleId = _v_;
	}

	public void setPosition(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPosition", false);
		limax.zdb.Logs.logObject(this, "position");
		this.position = _v_;
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

	public void setTotalPoint(int _v_) { 
		this.verifyStandaloneOrLockHeld("setTotalPoint", false);
		limax.zdb.Logs.logObject(this, "totalPoint");
		this.totalPoint = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoomHistoryPlayerInfo _o_ = null;
		if ( _o1_ instanceof RoomHistoryPlayerInfo ) _o_ = (RoomHistoryPlayerInfo)_o1_;
		else return false;
		if (this.roleId != _o_.roleId) return false;
		if (this.position != _o_.position) return false;
		if (!this.nickname.equals(_o_.nickname)) return false;
		if (!this.headImgUrl.equals(_o_.headImgUrl)) return false;
		if (this.sex != _o_.sex) return false;
		if (this.totalPoint != _o_.totalPoint) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roleId;
		_h_ += _h_ * 31 + this.position;
		_h_ += _h_ * 31 + this.nickname.hashCode();
		_h_ += _h_ * 31 + this.headImgUrl.hashCode();
		_h_ += _h_ * 31 + this.sex;
		_h_ += _h_ * 31 + this.totalPoint;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roleId).append(",");
		_sb_.append(this.position).append(",");
		_sb_.append("T").append(this.nickname.length()).append(",");
		_sb_.append("T").append(this.headImgUrl.length()).append(",");
		_sb_.append(this.sex).append(",");
		_sb_.append(this.totalPoint).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
