package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class DeviceIdBindRecordBean extends limax.zdb.XBean {
    private int accountId; 

    DeviceIdBindRecordBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public DeviceIdBindRecordBean() {
		this(null, null);
	}

	public DeviceIdBindRecordBean(DeviceIdBindRecordBean _o_) {
		this(_o_, null, null);
	}

	DeviceIdBindRecordBean(DeviceIdBindRecordBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.DeviceIdBindRecordBean", true);
        this.accountId = _o_.accountId;
	}

	public void copyFrom(DeviceIdBindRecordBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromDeviceIdBindRecordBean", true);
		this.verifyStandaloneOrLockHeld("copyToDeviceIdBindRecordBean", false);
        limax.zdb.Logs.logObject(this, "accountId");
        this.accountId = _o_.accountId;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.accountId);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.accountId = _os_.unmarshal_int();
		return _os_;
	}

	public int getAccountId() { 
		this.verifyStandaloneOrLockHeld("getAccountId", true);
		return this.accountId;
	}

	public void setAccountId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAccountId", false);
		limax.zdb.Logs.logObject(this, "accountId");
		this.accountId = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		DeviceIdBindRecordBean _o_ = null;
		if ( _o1_ instanceof DeviceIdBindRecordBean ) _o_ = (DeviceIdBindRecordBean)_o1_;
		else return false;
		if (this.accountId != _o_.accountId) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.accountId;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.accountId).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
