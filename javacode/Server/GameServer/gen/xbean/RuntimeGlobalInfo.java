package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RuntimeGlobalInfo extends limax.zdb.XBean {
    private java.util.HashMap<Integer, xbean.RuntimeBattleInfo> battles; 
    private long serverStartupTime; 

    RuntimeGlobalInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        battles = new java.util.HashMap<Integer, xbean.RuntimeBattleInfo>();
	}

	public RuntimeGlobalInfo() {
		this(null, null);
	}

	public RuntimeGlobalInfo(RuntimeGlobalInfo _o_) {
		this(_o_, null, null);
	}

	RuntimeGlobalInfo(RuntimeGlobalInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RuntimeGlobalInfo", true);
        this.battles = new java.util.HashMap<Integer, xbean.RuntimeBattleInfo>();
        _o_.battles.forEach((_k_, _v_) -> this.battles.put(_k_, new RuntimeBattleInfo(_v_, this, "battles")));
        this.serverStartupTime = _o_.serverStartupTime;
	}

	public void copyFrom(RuntimeGlobalInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRuntimeGlobalInfo", true);
		this.verifyStandaloneOrLockHeld("copyToRuntimeGlobalInfo", false);
        java.util.Map<Integer, xbean.RuntimeBattleInfo> this_battles = limax.zdb.Logs.logMap(this, "battles", ()->{});
        this_battles.clear();
        _o_.battles.forEach((_k_, _v_) -> this_battles.put(_k_, new RuntimeBattleInfo(_v_)));
        limax.zdb.Logs.logObject(this, "serverStartupTime");
        this.serverStartupTime = _o_.serverStartupTime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.battles.size());
        for (java.util.Map.Entry<Integer, xbean.RuntimeBattleInfo> _e_ : this.battles.entrySet()) {
        	_os_.marshal(_e_.getKey());
        	_os_.marshal(_e_.getValue());
        }
        _os_.marshal(this.serverStartupTime);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _k_ = _os_.unmarshal_int();
			xbean.RuntimeBattleInfo _v_ = new RuntimeBattleInfo(this, "battles");
			_v_.unmarshal(_os_);
			this.battles.put(_k_, _v_);
		}
		this.serverStartupTime = _os_.unmarshal_long();
		return _os_;
	}

	public java.util.Map<Integer, xbean.RuntimeBattleInfo> getBattles() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logMap(this, "battles", this.verifyStandaloneOrLockHeld("getBattles", true)) : this.battles;
	}

	public long getServerStartupTime() { 
		this.verifyStandaloneOrLockHeld("getServerStartupTime", true);
		return this.serverStartupTime;
	}

	public void setServerStartupTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setServerStartupTime", false);
		limax.zdb.Logs.logObject(this, "serverStartupTime");
		this.serverStartupTime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RuntimeGlobalInfo _o_ = null;
		if ( _o1_ instanceof RuntimeGlobalInfo ) _o_ = (RuntimeGlobalInfo)_o1_;
		else return false;
		if (!this.battles.equals(_o_.battles)) return false;
		if (this.serverStartupTime != _o_.serverStartupTime) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.battles.hashCode();
		_h_ += _h_ * 31 + (int)(this.serverStartupTime ^ (this.serverStartupTime >>> 32));
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.battles).append(",");
		_sb_.append(this.serverStartupTime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
