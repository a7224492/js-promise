package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class AccountInfo extends limax.zdb.XBean {
    private int accountId; 
    private String platform; 
    private String channel; 
    private String username; 
    private String refreshToken; 
    private String nickname; 
    private int sex; 
    private String headImgUrl; 
    private long createTime; 
    private long authTime; 
    private long tokenTime; 
    private String province; 
    private String city; 
    private String country; 
    private String unionid; 
    private String deviceId; 

    AccountInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        platform = "";
        channel = "";
        username = "";
        refreshToken = "";
        nickname = "";
        headImgUrl = "";
        province = "";
        city = "";
        country = "";
        unionid = "";
        deviceId = "";
	}

	public AccountInfo() {
		this(null, null);
	}

	public AccountInfo(AccountInfo _o_) {
		this(_o_, null, null);
	}

	AccountInfo(AccountInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.AccountInfo", true);
        this.accountId = _o_.accountId;
        this.platform = _o_.platform;
        this.channel = _o_.channel;
        this.username = _o_.username;
        this.refreshToken = _o_.refreshToken;
        this.nickname = _o_.nickname;
        this.sex = _o_.sex;
        this.headImgUrl = _o_.headImgUrl;
        this.createTime = _o_.createTime;
        this.authTime = _o_.authTime;
        this.tokenTime = _o_.tokenTime;
        this.province = _o_.province;
        this.city = _o_.city;
        this.country = _o_.country;
        this.unionid = _o_.unionid;
        this.deviceId = _o_.deviceId;
	}

	public void copyFrom(AccountInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromAccountInfo", true);
		this.verifyStandaloneOrLockHeld("copyToAccountInfo", false);
        limax.zdb.Logs.logObject(this, "accountId");
        this.accountId = _o_.accountId;
        limax.zdb.Logs.logObject(this, "platform");
        this.platform = _o_.platform;
        limax.zdb.Logs.logObject(this, "channel");
        this.channel = _o_.channel;
        limax.zdb.Logs.logObject(this, "username");
        this.username = _o_.username;
        limax.zdb.Logs.logObject(this, "refreshToken");
        this.refreshToken = _o_.refreshToken;
        limax.zdb.Logs.logObject(this, "nickname");
        this.nickname = _o_.nickname;
        limax.zdb.Logs.logObject(this, "sex");
        this.sex = _o_.sex;
        limax.zdb.Logs.logObject(this, "headImgUrl");
        this.headImgUrl = _o_.headImgUrl;
        limax.zdb.Logs.logObject(this, "createTime");
        this.createTime = _o_.createTime;
        limax.zdb.Logs.logObject(this, "authTime");
        this.authTime = _o_.authTime;
        limax.zdb.Logs.logObject(this, "tokenTime");
        this.tokenTime = _o_.tokenTime;
        limax.zdb.Logs.logObject(this, "province");
        this.province = _o_.province;
        limax.zdb.Logs.logObject(this, "city");
        this.city = _o_.city;
        limax.zdb.Logs.logObject(this, "country");
        this.country = _o_.country;
        limax.zdb.Logs.logObject(this, "unionid");
        this.unionid = _o_.unionid;
        limax.zdb.Logs.logObject(this, "deviceId");
        this.deviceId = _o_.deviceId;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.accountId);
        _os_.marshal(this.platform);
        _os_.marshal(this.channel);
        _os_.marshal(this.username);
        _os_.marshal(this.refreshToken);
        _os_.marshal(this.nickname);
        _os_.marshal(this.sex);
        _os_.marshal(this.headImgUrl);
        _os_.marshal(this.createTime);
        _os_.marshal(this.authTime);
        _os_.marshal(this.tokenTime);
        _os_.marshal(this.province);
        _os_.marshal(this.city);
        _os_.marshal(this.country);
        _os_.marshal(this.unionid);
        _os_.marshal(this.deviceId);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.accountId = _os_.unmarshal_int();
		this.platform = _os_.unmarshal_String();
		this.channel = _os_.unmarshal_String();
		this.username = _os_.unmarshal_String();
		this.refreshToken = _os_.unmarshal_String();
		this.nickname = _os_.unmarshal_String();
		this.sex = _os_.unmarshal_int();
		this.headImgUrl = _os_.unmarshal_String();
		this.createTime = _os_.unmarshal_long();
		this.authTime = _os_.unmarshal_long();
		this.tokenTime = _os_.unmarshal_long();
		this.province = _os_.unmarshal_String();
		this.city = _os_.unmarshal_String();
		this.country = _os_.unmarshal_String();
		this.unionid = _os_.unmarshal_String();
		this.deviceId = _os_.unmarshal_String();
		return _os_;
	}

	public int getAccountId() { 
		this.verifyStandaloneOrLockHeld("getAccountId", true);
		return this.accountId;
	}

	public String getPlatform() { 
		this.verifyStandaloneOrLockHeld("getPlatform", true);
		return this.platform;
	}

	public String getChannel() { 
		this.verifyStandaloneOrLockHeld("getChannel", true);
		return this.channel;
	}

	public String getUsername() { 
		this.verifyStandaloneOrLockHeld("getUsername", true);
		return this.username;
	}

	public String getRefreshToken() { 
		this.verifyStandaloneOrLockHeld("getRefreshToken", true);
		return this.refreshToken;
	}

	public String getNickname() { 
		this.verifyStandaloneOrLockHeld("getNickname", true);
		return this.nickname;
	}

	public int getSex() { 
		this.verifyStandaloneOrLockHeld("getSex", true);
		return this.sex;
	}

	public String getHeadImgUrl() { 
		this.verifyStandaloneOrLockHeld("getHeadImgUrl", true);
		return this.headImgUrl;
	}

	public long getCreateTime() { 
		this.verifyStandaloneOrLockHeld("getCreateTime", true);
		return this.createTime;
	}

	public long getAuthTime() { 
		this.verifyStandaloneOrLockHeld("getAuthTime", true);
		return this.authTime;
	}

	public long getTokenTime() { 
		this.verifyStandaloneOrLockHeld("getTokenTime", true);
		return this.tokenTime;
	}

	public String getProvince() { 
		this.verifyStandaloneOrLockHeld("getProvince", true);
		return this.province;
	}

	public String getCity() { 
		this.verifyStandaloneOrLockHeld("getCity", true);
		return this.city;
	}

	public String getCountry() { 
		this.verifyStandaloneOrLockHeld("getCountry", true);
		return this.country;
	}

	public String getUnionid() { 
		this.verifyStandaloneOrLockHeld("getUnionid", true);
		return this.unionid;
	}

	public String getDeviceId() { 
		this.verifyStandaloneOrLockHeld("getDeviceId", true);
		return this.deviceId;
	}

	public void setAccountId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAccountId", false);
		limax.zdb.Logs.logObject(this, "accountId");
		this.accountId = _v_;
	}

	public void setPlatform(String _v_) { 
		this.verifyStandaloneOrLockHeld("setPlatform", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "platform");
		this.platform = _v_;
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

	public void setRefreshToken(String _v_) { 
		this.verifyStandaloneOrLockHeld("setRefreshToken", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "refreshToken");
		this.refreshToken = _v_;
	}

	public void setNickname(String _v_) { 
		this.verifyStandaloneOrLockHeld("setNickname", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "nickname");
		this.nickname = _v_;
	}

	public void setSex(int _v_) { 
		this.verifyStandaloneOrLockHeld("setSex", false);
		limax.zdb.Logs.logObject(this, "sex");
		this.sex = _v_;
	}

	public void setHeadImgUrl(String _v_) { 
		this.verifyStandaloneOrLockHeld("setHeadImgUrl", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "headImgUrl");
		this.headImgUrl = _v_;
	}

	public void setCreateTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setCreateTime", false);
		limax.zdb.Logs.logObject(this, "createTime");
		this.createTime = _v_;
	}

	public void setAuthTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setAuthTime", false);
		limax.zdb.Logs.logObject(this, "authTime");
		this.authTime = _v_;
	}

	public void setTokenTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setTokenTime", false);
		limax.zdb.Logs.logObject(this, "tokenTime");
		this.tokenTime = _v_;
	}

	public void setProvince(String _v_) { 
		this.verifyStandaloneOrLockHeld("setProvince", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "province");
		this.province = _v_;
	}

	public void setCity(String _v_) { 
		this.verifyStandaloneOrLockHeld("setCity", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "city");
		this.city = _v_;
	}

	public void setCountry(String _v_) { 
		this.verifyStandaloneOrLockHeld("setCountry", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "country");
		this.country = _v_;
	}

	public void setUnionid(String _v_) { 
		this.verifyStandaloneOrLockHeld("setUnionid", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "unionid");
		this.unionid = _v_;
	}

	public void setDeviceId(String _v_) { 
		this.verifyStandaloneOrLockHeld("setDeviceId", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "deviceId");
		this.deviceId = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		AccountInfo _o_ = null;
		if ( _o1_ instanceof AccountInfo ) _o_ = (AccountInfo)_o1_;
		else return false;
		if (this.accountId != _o_.accountId) return false;
		if (!this.platform.equals(_o_.platform)) return false;
		if (!this.channel.equals(_o_.channel)) return false;
		if (!this.username.equals(_o_.username)) return false;
		if (!this.refreshToken.equals(_o_.refreshToken)) return false;
		if (!this.nickname.equals(_o_.nickname)) return false;
		if (this.sex != _o_.sex) return false;
		if (!this.headImgUrl.equals(_o_.headImgUrl)) return false;
		if (this.createTime != _o_.createTime) return false;
		if (this.authTime != _o_.authTime) return false;
		if (this.tokenTime != _o_.tokenTime) return false;
		if (!this.province.equals(_o_.province)) return false;
		if (!this.city.equals(_o_.city)) return false;
		if (!this.country.equals(_o_.country)) return false;
		if (!this.unionid.equals(_o_.unionid)) return false;
		if (!this.deviceId.equals(_o_.deviceId)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.accountId;
		_h_ += _h_ * 31 + this.platform.hashCode();
		_h_ += _h_ * 31 + this.channel.hashCode();
		_h_ += _h_ * 31 + this.username.hashCode();
		_h_ += _h_ * 31 + this.refreshToken.hashCode();
		_h_ += _h_ * 31 + this.nickname.hashCode();
		_h_ += _h_ * 31 + this.sex;
		_h_ += _h_ * 31 + this.headImgUrl.hashCode();
		_h_ += _h_ * 31 + (int)(this.createTime ^ (this.createTime >>> 32));
		_h_ += _h_ * 31 + (int)(this.authTime ^ (this.authTime >>> 32));
		_h_ += _h_ * 31 + (int)(this.tokenTime ^ (this.tokenTime >>> 32));
		_h_ += _h_ * 31 + this.province.hashCode();
		_h_ += _h_ * 31 + this.city.hashCode();
		_h_ += _h_ * 31 + this.country.hashCode();
		_h_ += _h_ * 31 + this.unionid.hashCode();
		_h_ += _h_ * 31 + this.deviceId.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.accountId).append(",");
		_sb_.append("T").append(this.platform.length()).append(",");
		_sb_.append("T").append(this.channel.length()).append(",");
		_sb_.append("T").append(this.username.length()).append(",");
		_sb_.append("T").append(this.refreshToken.length()).append(",");
		_sb_.append("T").append(this.nickname.length()).append(",");
		_sb_.append(this.sex).append(",");
		_sb_.append("T").append(this.headImgUrl.length()).append(",");
		_sb_.append(this.createTime).append(",");
		_sb_.append(this.authTime).append(",");
		_sb_.append(this.tokenTime).append(",");
		_sb_.append("T").append(this.province.length()).append(",");
		_sb_.append("T").append(this.city.length()).append(",");
		_sb_.append("T").append(this.country.length()).append(",");
		_sb_.append("T").append(this.unionid.length()).append(",");
		_sb_.append("T").append(this.deviceId.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
