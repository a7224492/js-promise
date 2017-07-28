
package cbean;

public class AccountTableKey implements limax.codec.Marshal, Comparable<AccountTableKey> {

	private String accountString; 
	private int appId; 

	public AccountTableKey() {
		accountString = "";
	}

	public AccountTableKey(String _accountString_, int _appId_) {
		this.accountString = _accountString_;
		this.appId = _appId_;
	}

	public String getAccountString() { 
		return this.accountString;
	}

	public int getAppId() { 
		return this.appId;
	}

	@Override
	public limax.codec.OctetsStream marshal(limax.codec.OctetsStream _os_) {
		_os_.marshal(this.accountString);
		_os_.marshal(this.appId);
		return _os_;
	}

	@Override
	public limax.codec.OctetsStream unmarshal(limax.codec.OctetsStream _os_) throws limax.codec.MarshalException {
		this.accountString = _os_.unmarshal_String();
		this.appId = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public int compareTo(AccountTableKey _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = this.accountString.compareTo( _o_.accountString);
		if (0 != _c_) return _c_;
		_c_ = this.appId - _o_.appId;
		if (0 != _c_) return _c_;
		return _c_;
	}

	@Override
	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AccountTableKey) {
			AccountTableKey _o_ = (AccountTableKey)_o1_;
			if (!this.accountString.equals(_o_.accountString)) return false;
			if (this.appId != _o_.appId) return false;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.accountString.hashCode();
		_h_ += _h_ * 31 + this.appId;
		return _h_;
	}

}
