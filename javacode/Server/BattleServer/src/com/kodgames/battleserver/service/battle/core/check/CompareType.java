package com.kodgames.battleserver.service.battle.core.check;

public enum CompareType
{
	Smaller(1), SmallerEqual(2), Equal(3), GreaterEqual(4), Greater(5);

	private byte value;

	private CompareType(int value)
	{
		this.value = (byte)value;
	}

	public byte getValue()
	{
		return this.value;
	}

	public boolean compare(int l, int r)
	{
		if (this.equals(Smaller))
			return l < r;
		else if (this.equals(SmallerEqual))
			return l <= r;
		else if (this.equals(Equal))
			return l == r;
		else if (this.equals(GreaterEqual))
			return l >= r;
		else // Greater
			return l > r;
	}
}
