package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class AddPlayersCardDetail extends limax.zdb.XBean {
    private int cardNum; 
    private int status; 
    private long creaetTime; 

    AddPlayersCardDetail(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public AddPlayersCardDetail() {
		this(null, null);
	}

	public AddPlayersCardDetail(AddPlayersCardDetail _o_) {
		this(_o_, null, null);
	}

	AddPlayersCardDetail(AddPlayersCardDetail _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.AddPlayersCardDetail", true);
        this.cardNum = _o_.cardNum;
        this.status = _o_.status;
        this.creaetTime = _o_.creaetTime;
	}

	public void copyFrom(AddPlayersCardDetail _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromAddPlayersCardDetail", true);
		this.verifyStandaloneOrLockHeld("copyToAddPlayersCardDetail", false);
        limax.zdb.Logs.logObject(this, "cardNum");
        this.cardNum = _o_.cardNum;
        limax.zdb.Logs.logObject(this, "status");
        this.status = _o_.status;
        limax.zdb.Logs.logObject(this, "creaetTime");
        this.creaetTime = _o_.creaetTime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.cardNum);
        _os_.marshal(this.status);
        _os_.marshal(this.creaetTime);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.cardNum = _os_.unmarshal_int();
		this.status = _os_.unmarshal_int();
		this.creaetTime = _os_.unmarshal_long();
		return _os_;
	}

	public int getCardNum() { 
		this.verifyStandaloneOrLockHeld("getCardNum", true);
		return this.cardNum;
	}

	public int getStatus() { 
		this.verifyStandaloneOrLockHeld("getStatus", true);
		return this.status;
	}

	public long getCreaetTime() { 
		this.verifyStandaloneOrLockHeld("getCreaetTime", true);
		return this.creaetTime;
	}

	public void setCardNum(int _v_) { 
		this.verifyStandaloneOrLockHeld("setCardNum", false);
		limax.zdb.Logs.logObject(this, "cardNum");
		this.cardNum = _v_;
	}

	public void setStatus(int _v_) { 
		this.verifyStandaloneOrLockHeld("setStatus", false);
		limax.zdb.Logs.logObject(this, "status");
		this.status = _v_;
	}

	public void setCreaetTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setCreaetTime", false);
		limax.zdb.Logs.logObject(this, "creaetTime");
		this.creaetTime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		AddPlayersCardDetail _o_ = null;
		if ( _o1_ instanceof AddPlayersCardDetail ) _o_ = (AddPlayersCardDetail)_o1_;
		else return false;
		if (this.cardNum != _o_.cardNum) return false;
		if (this.status != _o_.status) return false;
		if (this.creaetTime != _o_.creaetTime) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.cardNum;
		_h_ += _h_ * 31 + this.status;
		_h_ += _h_ * 31 + (int)(this.creaetTime ^ (this.creaetTime >>> 32));
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.cardNum).append(",");
		_sb_.append(this.status).append(",");
		_sb_.append(this.creaetTime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
