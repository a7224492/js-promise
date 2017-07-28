package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class PersistGlobalInfo extends limax.zdb.XBean {
    private int allowLoginChannelKeySeed; 
    private java.util.HashMap<Integer, String> allowLoginChannel; 
    private java.util.HashMap<Integer, xbean.ForbidRole> forbidPlayers; 

    PersistGlobalInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        allowLoginChannel = new java.util.HashMap<Integer, String>();
        forbidPlayers = new java.util.HashMap<Integer, xbean.ForbidRole>();
	}

	public PersistGlobalInfo() {
		this(null, null);
	}

	public PersistGlobalInfo(PersistGlobalInfo _o_) {
		this(_o_, null, null);
	}

	PersistGlobalInfo(PersistGlobalInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.PersistGlobalInfo", true);
        this.allowLoginChannelKeySeed = _o_.allowLoginChannelKeySeed;
        this.allowLoginChannel = new java.util.HashMap<Integer, String>();
        _o_.allowLoginChannel.forEach((_k_, _v_) -> this.allowLoginChannel.put(_k_, _v_));
        this.forbidPlayers = new java.util.HashMap<Integer, xbean.ForbidRole>();
        _o_.forbidPlayers.forEach((_k_, _v_) -> this.forbidPlayers.put(_k_, new ForbidRole(_v_, this, "forbidPlayers")));
	}

	public void copyFrom(PersistGlobalInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromPersistGlobalInfo", true);
		this.verifyStandaloneOrLockHeld("copyToPersistGlobalInfo", false);
        limax.zdb.Logs.logObject(this, "allowLoginChannelKeySeed");
        this.allowLoginChannelKeySeed = _o_.allowLoginChannelKeySeed;
        java.util.Map<Integer, String> this_allowLoginChannel = limax.zdb.Logs.logMap(this, "allowLoginChannel", ()->{});
        this_allowLoginChannel.clear();
        _o_.allowLoginChannel.forEach((_k_, _v_) -> this_allowLoginChannel.put(_k_, _v_));
        java.util.Map<Integer, xbean.ForbidRole> this_forbidPlayers = limax.zdb.Logs.logMap(this, "forbidPlayers", ()->{});
        this_forbidPlayers.clear();
        _o_.forbidPlayers.forEach((_k_, _v_) -> this_forbidPlayers.put(_k_, new ForbidRole(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.allowLoginChannelKeySeed);
        _os_.marshal_size(this.allowLoginChannel.size());
        for (java.util.Map.Entry<Integer, String> _e_ : this.allowLoginChannel.entrySet()) {
        	_os_.marshal(_e_.getKey());
        	_os_.marshal(_e_.getValue());
        }
        _os_.marshal_size(this.forbidPlayers.size());
        for (java.util.Map.Entry<Integer, xbean.ForbidRole> _e_ : this.forbidPlayers.entrySet()) {
        	_os_.marshal(_e_.getKey());
        	_os_.marshal(_e_.getValue());
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.allowLoginChannelKeySeed = _os_.unmarshal_int();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _k_ = _os_.unmarshal_int();
			String _v_ = _os_.unmarshal_String();
			this.allowLoginChannel.put(_k_, _v_);
		}
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _k_ = _os_.unmarshal_int();
			xbean.ForbidRole _v_ = new ForbidRole(this, "forbidPlayers");
			_v_.unmarshal(_os_);
			this.forbidPlayers.put(_k_, _v_);
		}
		return _os_;
	}

	public int getAllowLoginChannelKeySeed() { 
		this.verifyStandaloneOrLockHeld("getAllowLoginChannelKeySeed", true);
		return this.allowLoginChannelKeySeed;
	}

	public java.util.Map<Integer, String> getAllowLoginChannel() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logMap(this, "allowLoginChannel", this.verifyStandaloneOrLockHeld("getAllowLoginChannel", true)) : this.allowLoginChannel;
	}

	public java.util.Map<Integer, xbean.ForbidRole> getForbidPlayers() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logMap(this, "forbidPlayers", this.verifyStandaloneOrLockHeld("getForbidPlayers", true)) : this.forbidPlayers;
	}

	public void setAllowLoginChannelKeySeed(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAllowLoginChannelKeySeed", false);
		limax.zdb.Logs.logObject(this, "allowLoginChannelKeySeed");
		this.allowLoginChannelKeySeed = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		PersistGlobalInfo _o_ = null;
		if ( _o1_ instanceof PersistGlobalInfo ) _o_ = (PersistGlobalInfo)_o1_;
		else return false;
		if (this.allowLoginChannelKeySeed != _o_.allowLoginChannelKeySeed) return false;
		if (!this.allowLoginChannel.equals(_o_.allowLoginChannel)) return false;
		if (!this.forbidPlayers.equals(_o_.forbidPlayers)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.allowLoginChannelKeySeed;
		_h_ += _h_ * 31 + this.allowLoginChannel.hashCode();
		_h_ += _h_ * 31 + this.forbidPlayers.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.allowLoginChannelKeySeed).append(",");
		_sb_.append(this.allowLoginChannel).append(",");
		_sb_.append(this.forbidPlayers).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
