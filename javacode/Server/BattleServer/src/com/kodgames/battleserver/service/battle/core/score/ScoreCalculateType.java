package com.kodgames.battleserver.service.battle.core.score;

public enum ScoreCalculateType
{
	/**
	 * 最后分数加减, 不用计算翻数
	 */
	NONE(0),

	/**
	 * 二的指数次幂
	 */
	TWO_INDEX(1),

	/**
	 * 最后分数加减, 不用计算翻数
	 */
	TOTAL_ADD(2),

	/**
	 * 最后分数乘翻
	 */
	TOTAL_MULTI(3),

	/**
	 * 最后分数乘翻(不受封顶影响)
	 */
	TOTAL_MULTI_2ND(4);

	private int value;

	private ScoreCalculateType(int value)
	{
		this.value = (byte)value;
	}

	public static ScoreCalculateType getType(int value)
	{
		if (TWO_INDEX.getValue() == value)
			return TWO_INDEX;
		else if (TOTAL_ADD.getValue() == value)
			return TOTAL_ADD;
		else if (TOTAL_MULTI.getValue() == value)
			return TOTAL_MULTI;
		else if (TOTAL_MULTI_2ND.getValue() == value)
			return TOTAL_MULTI_2ND;
		else
			return NONE;
	}

	public int getValue()
	{
		return this.value;
	}
}