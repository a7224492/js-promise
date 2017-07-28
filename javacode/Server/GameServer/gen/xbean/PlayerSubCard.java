package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class PlayerSubCard extends limax.zdb.XBean {
    private java.util.ArrayList<xbean.SubCard> records; 

    PlayerSubCard(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        records = new java.util.ArrayList<xbean.SubCard>();
	}

	public PlayerSubCard() {
		this(null, null);
	}

	public PlayerSubCard(PlayerSubCard _o_) {
		this(_o_, null, null);
	}

	PlayerSubCard(PlayerSubCard _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.PlayerSubCard", true);
        this.records = new java.util.ArrayList<xbean.SubCard>();
        _o_.records.forEach(_v_ -> this.records.add(new SubCard(_v_, this, "records")));
	}

	public void copyFrom(PlayerSubCard _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromPlayerSubCard", true);
		this.verifyStandaloneOrLockHeld("copyToPlayerSubCard", false);
        java.util.List<xbean.SubCard> this_records = limax.zdb.Logs.logList(this, "records", ()->{});
        this_records.clear();
        _o_.records.forEach(_v_ -> this_records.add(new SubCard(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.records.size());
        for (xbean.SubCard _v_ : this.records) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.SubCard _v_ = new SubCard(this, "records");
			_v_.unmarshal(_os_);
			this.records.add(_v_);
		}
		return _os_;
	}

	public java.util.List<xbean.SubCard> getRecords() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "records", this.verifyStandaloneOrLockHeld("getRecords", true)) : this.records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		PlayerSubCard _o_ = null;
		if ( _o1_ instanceof PlayerSubCard ) _o_ = (PlayerSubCard)_o1_;
		else return false;
		if (!this.records.equals(_o_.records)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.records.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.records).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
