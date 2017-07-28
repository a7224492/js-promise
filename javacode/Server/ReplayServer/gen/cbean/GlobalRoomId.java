
package cbean;

public class GlobalRoomId implements limax.codec.Marshal, Comparable<GlobalRoomId> {

	private long createTime; 
	private int roomId; 

	public GlobalRoomId() {
	}

	public GlobalRoomId(long _createTime_, int _roomId_) {
		this.createTime = _createTime_;
		this.roomId = _roomId_;
	}

	public long getCreateTime() { 
		return this.createTime;
	}

	public int getRoomId() { 
		return this.roomId;
	}

	@Override
	public limax.codec.OctetsStream marshal(limax.codec.OctetsStream _os_) {
		_os_.marshal(this.createTime);
		_os_.marshal(this.roomId);
		return _os_;
	}

	@Override
	public limax.codec.OctetsStream unmarshal(limax.codec.OctetsStream _os_) throws limax.codec.MarshalException {
		this.createTime = _os_.unmarshal_long();
		this.roomId = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public int compareTo(GlobalRoomId _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(this.createTime - _o_.createTime);
		if (0 != _c_) return _c_;
		_c_ = this.roomId - _o_.roomId;
		if (0 != _c_) return _c_;
		return _c_;
	}

	@Override
	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GlobalRoomId) {
			GlobalRoomId _o_ = (GlobalRoomId)_o1_;
			if (this.createTime != _o_.createTime) return false;
			if (this.roomId != _o_.roomId) return false;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + (int)(this.createTime ^ (this.createTime >>> 32));
		_h_ += _h_ * 31 + this.roomId;
		return _h_;
	}

}
