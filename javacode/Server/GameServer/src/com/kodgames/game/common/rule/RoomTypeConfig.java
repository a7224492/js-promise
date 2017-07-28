package com.kodgames.game.common.rule;

public final class RoomTypeConfig
{
	private int type;
	private int roundCount;
	private int gameCount;
	private int cardCount;
	private int aaCardCount;

	public int getType()
	{
		return this.type;
	}

	public int getRoundCount()
	{
		return this.roundCount;
	}

	public int getGameCount()
	{
		return this.gameCount;
	}

	public int getCardCount()
	{
		return this.cardCount;
	}

	public int getAACardCount()
	{
		return this.aaCardCount;
	}

	public void setType(int value)
	{
		this.type = value;
	}

	public void setRoundCount(int value)
	{
		this.roundCount = value;
	}

	public void setGameCount(int value)
	{
		this.gameCount = value;
	}

	public void setCardCount(int value)
	{
		this.cardCount = value;
	}

	public void setAACardCount(int value)
	{
		this.aaCardCount = value;
	}

}
