package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class AddPlayersCardRecord extends limax.zdb.XBean {
    private long createTime; 
    private String gmtUsername; 
    private int planCardNum; 
    private int realCardNum; 
    private int playerNum; 
    private int successNum; 
    private int failNum; 

    AddPlayersCardRecord(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        gmtUsername = "";
	}

	public AddPlayersCardRecord() {
		this(null, null);
	}

	public AddPlayersCardRecord(AddPlayersCardRecord _o_) {
		this(_o_, null, null);
	}

	AddPlayersCardRecord(AddPlayersCardRecord _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.AddPlayersCardRecord", true);
        this.createTime = _o_.createTime;
        this.gmtUsername = _o_.gmtUsername;
        this.planCardNum = _o_.planCardNum;
        this.realCardNum = _o_.realCardNum;
        this.playerNum = _o_.playerNum;
        this.successNum = _o_.successNum;
        this.failNum = _o_.failNum;
	}

	public void copyFrom(AddPlayersCardRecord _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromAddPlayersCardRecord", true);
		this.verifyStandaloneOrLockHeld("copyToAddPlayersCardRecord", false);
        limax.zdb.Logs.logObject(this, "createTime");
        this.createTime = _o_.createTime;
        limax.zdb.Logs.logObject(this, "gmtUsername");
        this.gmtUsername = _o_.gmtUsername;
        limax.zdb.Logs.logObject(this, "planCardNum");
        this.planCardNum = _o_.planCardNum;
        limax.zdb.Logs.logObject(this, "realCardNum");
        this.realCardNum = _o_.realCardNum;
        limax.zdb.Logs.logObject(this, "playerNum");
        this.playerNum = _o_.playerNum;
        limax.zdb.Logs.logObject(this, "successNum");
        this.successNum = _o_.successNum;
        limax.zdb.Logs.logObject(this, "failNum");
        this.failNum = _o_.failNum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.createTime);
        _os_.marshal(this.gmtUsername);
        _os_.marshal(this.planCardNum);
        _os_.marshal(this.realCardNum);
        _os_.marshal(this.playerNum);
        _os_.marshal(this.successNum);
        _os_.marshal(this.failNum);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.createTime = _os_.unmarshal_long();
		this.gmtUsername = _os_.unmarshal_String();
		this.planCardNum = _os_.unmarshal_int();
		this.realCardNum = _os_.unmarshal_int();
		this.playerNum = _os_.unmarshal_int();
		this.successNum = _os_.unmarshal_int();
		this.failNum = _os_.unmarshal_int();
		return _os_;
	}

	public long getCreateTime() { 
		this.verifyStandaloneOrLockHeld("getCreateTime", true);
		return this.createTime;
	}

	public String getGmtUsername() { 
		this.verifyStandaloneOrLockHeld("getGmtUsername", true);
		return this.gmtUsername;
	}

	public int getPlanCardNum() { 
		this.verifyStandaloneOrLockHeld("getPlanCardNum", true);
		return this.planCardNum;
	}

	public int getRealCardNum() { 
		this.verifyStandaloneOrLockHeld("getRealCardNum", true);
		return this.realCardNum;
	}

	public int getPlayerNum() { 
		this.verifyStandaloneOrLockHeld("getPlayerNum", true);
		return this.playerNum;
	}

	public int getSuccessNum() { 
		this.verifyStandaloneOrLockHeld("getSuccessNum", true);
		return this.successNum;
	}

	public int getFailNum() { 
		this.verifyStandaloneOrLockHeld("getFailNum", true);
		return this.failNum;
	}

	public void setCreateTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setCreateTime", false);
		limax.zdb.Logs.logObject(this, "createTime");
		this.createTime = _v_;
	}

	public void setGmtUsername(String _v_) { 
		this.verifyStandaloneOrLockHeld("setGmtUsername", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "gmtUsername");
		this.gmtUsername = _v_;
	}

	public void setPlanCardNum(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPlanCardNum", false);
		limax.zdb.Logs.logObject(this, "planCardNum");
		this.planCardNum = _v_;
	}

	public void setRealCardNum(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRealCardNum", false);
		limax.zdb.Logs.logObject(this, "realCardNum");
		this.realCardNum = _v_;
	}

	public void setPlayerNum(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPlayerNum", false);
		limax.zdb.Logs.logObject(this, "playerNum");
		this.playerNum = _v_;
	}

	public void setSuccessNum(int _v_) { 
		this.verifyStandaloneOrLockHeld("setSuccessNum", false);
		limax.zdb.Logs.logObject(this, "successNum");
		this.successNum = _v_;
	}

	public void setFailNum(int _v_) { 
		this.verifyStandaloneOrLockHeld("setFailNum", false);
		limax.zdb.Logs.logObject(this, "failNum");
		this.failNum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		AddPlayersCardRecord _o_ = null;
		if ( _o1_ instanceof AddPlayersCardRecord ) _o_ = (AddPlayersCardRecord)_o1_;
		else return false;
		if (this.createTime != _o_.createTime) return false;
		if (!this.gmtUsername.equals(_o_.gmtUsername)) return false;
		if (this.planCardNum != _o_.planCardNum) return false;
		if (this.realCardNum != _o_.realCardNum) return false;
		if (this.playerNum != _o_.playerNum) return false;
		if (this.successNum != _o_.successNum) return false;
		if (this.failNum != _o_.failNum) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + (int)(this.createTime ^ (this.createTime >>> 32));
		_h_ += _h_ * 31 + this.gmtUsername.hashCode();
		_h_ += _h_ * 31 + this.planCardNum;
		_h_ += _h_ * 31 + this.realCardNum;
		_h_ += _h_ * 31 + this.playerNum;
		_h_ += _h_ * 31 + this.successNum;
		_h_ += _h_ * 31 + this.failNum;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.createTime).append(",");
		_sb_.append("T").append(this.gmtUsername.length()).append(",");
		_sb_.append(this.planCardNum).append(",");
		_sb_.append(this.realCardNum).append(",");
		_sb_.append(this.playerNum).append(",");
		_sb_.append(this.successNum).append(",");
		_sb_.append(this.failNum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
