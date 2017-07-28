package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoundRecord extends limax.zdb.XBean {
    private java.util.ArrayList<Byte> bytes; 
    private java.util.ArrayList<Byte> playbackDatas; 

    RoundRecord(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        bytes = new java.util.ArrayList<Byte>();
        playbackDatas = new java.util.ArrayList<Byte>();
	}

	public RoundRecord() {
		this(null, null);
	}

	public RoundRecord(RoundRecord _o_) {
		this(_o_, null, null);
	}

	RoundRecord(RoundRecord _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoundRecord", true);
        this.bytes = new java.util.ArrayList<Byte>();
        this.bytes.addAll(_o_.bytes);
        this.playbackDatas = new java.util.ArrayList<Byte>();
        this.playbackDatas.addAll(_o_.playbackDatas);
	}

	public void copyFrom(RoundRecord _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoundRecord", true);
		this.verifyStandaloneOrLockHeld("copyToRoundRecord", false);
        java.util.List<Byte> this_bytes = limax.zdb.Logs.logList(this, "bytes", ()->{});
        this_bytes.clear();
        this_bytes.addAll(_o_.bytes);
        java.util.List<Byte> this_playbackDatas = limax.zdb.Logs.logList(this, "playbackDatas", ()->{});
        this_playbackDatas.clear();
        this_playbackDatas.addAll(_o_.playbackDatas);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.bytes.size());
        for (Byte _v_ : this.bytes) {
        	_os_.marshal(_v_);
        }
        _os_.marshal_size(this.playbackDatas.size());
        for (Byte _v_ : this.playbackDatas) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			byte _v_ = _os_.unmarshal_byte();
			this.bytes.add(_v_);
		}
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			byte _v_ = _os_.unmarshal_byte();
			this.playbackDatas.add(_v_);
		}
		return _os_;
	}

	public java.util.List<Byte> getBytes() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "bytes", this.verifyStandaloneOrLockHeld("getBytes", true)) : this.bytes;
	}

	public java.util.List<Byte> getPlaybackDatas() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "playbackDatas", this.verifyStandaloneOrLockHeld("getPlaybackDatas", true)) : this.playbackDatas;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoundRecord _o_ = null;
		if ( _o1_ instanceof RoundRecord ) _o_ = (RoundRecord)_o1_;
		else return false;
		if (!this.bytes.equals(_o_.bytes)) return false;
		if (!this.playbackDatas.equals(_o_.playbackDatas)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.bytes.hashCode();
		_h_ += _h_ * 31 + this.playbackDatas.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.bytes).append(",");
		_sb_.append(this.playbackDatas).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
