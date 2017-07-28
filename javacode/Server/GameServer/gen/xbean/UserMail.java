package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class UserMail extends limax.zdb.XBean {
    private java.util.ArrayList<Long> personalMails; 
    private java.util.ArrayList<Long> allUserMails; 
    private long lastCheckTime; 

    UserMail(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        personalMails = new java.util.ArrayList<Long>();
        allUserMails = new java.util.ArrayList<Long>();
	}

	public UserMail() {
		this(null, null);
	}

	public UserMail(UserMail _o_) {
		this(_o_, null, null);
	}

	UserMail(UserMail _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.UserMail", true);
        this.personalMails = new java.util.ArrayList<Long>();
        this.personalMails.addAll(_o_.personalMails);
        this.allUserMails = new java.util.ArrayList<Long>();
        this.allUserMails.addAll(_o_.allUserMails);
        this.lastCheckTime = _o_.lastCheckTime;
	}

	public void copyFrom(UserMail _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromUserMail", true);
		this.verifyStandaloneOrLockHeld("copyToUserMail", false);
        java.util.List<Long> this_personalMails = limax.zdb.Logs.logList(this, "personalMails", ()->{});
        this_personalMails.clear();
        this_personalMails.addAll(_o_.personalMails);
        java.util.List<Long> this_allUserMails = limax.zdb.Logs.logList(this, "allUserMails", ()->{});
        this_allUserMails.clear();
        this_allUserMails.addAll(_o_.allUserMails);
        limax.zdb.Logs.logObject(this, "lastCheckTime");
        this.lastCheckTime = _o_.lastCheckTime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.personalMails.size());
        for (Long _v_ : this.personalMails) {
        	_os_.marshal(_v_);
        }
        _os_.marshal_size(this.allUserMails.size());
        for (Long _v_ : this.allUserMails) {
        	_os_.marshal(_v_);
        }
        _os_.marshal(this.lastCheckTime);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			long _v_ = _os_.unmarshal_long();
			this.personalMails.add(_v_);
		}
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			long _v_ = _os_.unmarshal_long();
			this.allUserMails.add(_v_);
		}
		this.lastCheckTime = _os_.unmarshal_long();
		return _os_;
	}

	public java.util.List<Long> getPersonalMails() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "personalMails", this.verifyStandaloneOrLockHeld("getPersonalMails", true)) : this.personalMails;
	}

	public java.util.List<Long> getAllUserMails() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "allUserMails", this.verifyStandaloneOrLockHeld("getAllUserMails", true)) : this.allUserMails;
	}

	public long getLastCheckTime() { 
		this.verifyStandaloneOrLockHeld("getLastCheckTime", true);
		return this.lastCheckTime;
	}

	public void setLastCheckTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setLastCheckTime", false);
		limax.zdb.Logs.logObject(this, "lastCheckTime");
		this.lastCheckTime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		UserMail _o_ = null;
		if ( _o1_ instanceof UserMail ) _o_ = (UserMail)_o1_;
		else return false;
		if (!this.personalMails.equals(_o_.personalMails)) return false;
		if (!this.allUserMails.equals(_o_.allUserMails)) return false;
		if (this.lastCheckTime != _o_.lastCheckTime) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.personalMails.hashCode();
		_h_ += _h_ * 31 + this.allUserMails.hashCode();
		_h_ += _h_ * 31 + (int)(this.lastCheckTime ^ (this.lastCheckTime >>> 32));
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.personalMails).append(",");
		_sb_.append(this.allUserMails).append(",");
		_sb_.append(this.lastCheckTime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
