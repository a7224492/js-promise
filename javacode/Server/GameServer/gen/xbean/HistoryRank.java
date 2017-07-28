package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class HistoryRank extends limax.zdb.XBean {
    private int score; 

    HistoryRank(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public HistoryRank() {
		this(null, null);
	}

	public HistoryRank(HistoryRank _o_) {
		this(_o_, null, null);
	}

	HistoryRank(HistoryRank _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.HistoryRank", true);
        this.score = _o_.score;
	}

	public void copyFrom(HistoryRank _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromHistoryRank", true);
		this.verifyStandaloneOrLockHeld("copyToHistoryRank", false);
        limax.zdb.Logs.logObject(this, "score");
        this.score = _o_.score;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.score);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.score = _os_.unmarshal_int();
		return _os_;
	}

	public int getScore() { 
		this.verifyStandaloneOrLockHeld("getScore", true);
		return this.score;
	}

	public void setScore(int _v_) { 
		this.verifyStandaloneOrLockHeld("setScore", false);
		limax.zdb.Logs.logObject(this, "score");
		this.score = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		HistoryRank _o_ = null;
		if ( _o1_ instanceof HistoryRank ) _o_ = (HistoryRank)_o1_;
		else return false;
		if (this.score != _o_.score) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.score;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.score).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
