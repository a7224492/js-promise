package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class PlayerAddCard extends limax.zdb.XBean {
    private java.util.ArrayList<xbean.AddCard> records; 

    PlayerAddCard(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        records = new java.util.ArrayList<xbean.AddCard>();
	}

	public PlayerAddCard() {
		this(null, null);
	}

	public PlayerAddCard(PlayerAddCard _o_) {
		this(_o_, null, null);
	}

	PlayerAddCard(PlayerAddCard _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.PlayerAddCard", true);
        this.records = new java.util.ArrayList<xbean.AddCard>();
        _o_.records.forEach(_v_ -> this.records.add(new AddCard(_v_, this, "records")));
	}

	public void copyFrom(PlayerAddCard _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromPlayerAddCard", true);
		this.verifyStandaloneOrLockHeld("copyToPlayerAddCard", false);
        java.util.List<xbean.AddCard> this_records = limax.zdb.Logs.logList(this, "records", ()->{});
        this_records.clear();
        _o_.records.forEach(_v_ -> this_records.add(new AddCard(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.records.size());
        for (xbean.AddCard _v_ : this.records) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.AddCard _v_ = new AddCard(this, "records");
			_v_.unmarshal(_os_);
			this.records.add(_v_);
		}
		return _os_;
	}

	public java.util.List<xbean.AddCard> getRecords() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "records", this.verifyStandaloneOrLockHeld("getRecords", true)) : this.records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		PlayerAddCard _o_ = null;
		if ( _o1_ instanceof PlayerAddCard ) _o_ = (PlayerAddCard)_o1_;
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
