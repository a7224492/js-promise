package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class AgentSatusBean extends limax.zdb.XBean {
    private int agentId; 
    private String phoneNumber; 
    private String bindStatus; 

    AgentSatusBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        phoneNumber = "";
        bindStatus = "";
	}

	public AgentSatusBean() {
		this(null, null);
	}

	public AgentSatusBean(AgentSatusBean _o_) {
		this(_o_, null, null);
	}

	AgentSatusBean(AgentSatusBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.AgentSatusBean", true);
        this.agentId = _o_.agentId;
        this.phoneNumber = _o_.phoneNumber;
        this.bindStatus = _o_.bindStatus;
	}

	public void copyFrom(AgentSatusBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromAgentSatusBean", true);
		this.verifyStandaloneOrLockHeld("copyToAgentSatusBean", false);
        limax.zdb.Logs.logObject(this, "agentId");
        this.agentId = _o_.agentId;
        limax.zdb.Logs.logObject(this, "phoneNumber");
        this.phoneNumber = _o_.phoneNumber;
        limax.zdb.Logs.logObject(this, "bindStatus");
        this.bindStatus = _o_.bindStatus;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.agentId);
        _os_.marshal(this.phoneNumber);
        _os_.marshal(this.bindStatus);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.agentId = _os_.unmarshal_int();
		this.phoneNumber = _os_.unmarshal_String();
		this.bindStatus = _os_.unmarshal_String();
		return _os_;
	}

	public int getAgentId() { 
		this.verifyStandaloneOrLockHeld("getAgentId", true);
		return this.agentId;
	}

	public String getPhoneNumber() { 
		this.verifyStandaloneOrLockHeld("getPhoneNumber", true);
		return this.phoneNumber;
	}

	public String getBindStatus() { 
		this.verifyStandaloneOrLockHeld("getBindStatus", true);
		return this.bindStatus;
	}

	public void setAgentId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAgentId", false);
		limax.zdb.Logs.logObject(this, "agentId");
		this.agentId = _v_;
	}

	public void setPhoneNumber(String _v_) { 
		this.verifyStandaloneOrLockHeld("setPhoneNumber", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "phoneNumber");
		this.phoneNumber = _v_;
	}

	public void setBindStatus(String _v_) { 
		this.verifyStandaloneOrLockHeld("setBindStatus", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "bindStatus");
		this.bindStatus = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		AgentSatusBean _o_ = null;
		if ( _o1_ instanceof AgentSatusBean ) _o_ = (AgentSatusBean)_o1_;
		else return false;
		if (this.agentId != _o_.agentId) return false;
		if (!this.phoneNumber.equals(_o_.phoneNumber)) return false;
		if (!this.bindStatus.equals(_o_.bindStatus)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.agentId;
		_h_ += _h_ * 31 + this.phoneNumber.hashCode();
		_h_ += _h_ * 31 + this.bindStatus.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.agentId).append(",");
		_sb_.append("T").append(this.phoneNumber.length()).append(",");
		_sb_.append("T").append(this.bindStatus.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
