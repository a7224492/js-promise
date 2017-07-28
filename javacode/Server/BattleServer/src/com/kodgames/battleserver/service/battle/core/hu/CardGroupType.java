package com.kodgames.battleserver.service.battle.core.hu;

import java.util.concurrent.ConcurrentHashMap;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 刻，相同的三张<br>
 * 坎，三张顺序排，例如（1万，2万，3万）
 *
 * 注意声明顺序, 在检测胡的时候要求非序列组在序列组前面
 */
public class CardGroupType
{
	public static final int INVALID = 0;

	/** 单张 */
	public static final int DAN_ZHANG = 1;

	/** 将 */
	public static final int JIANG = 2;

	/** 明刻 */
	public static final int KE = 3;

	/** 暗刻 */
	public static final int AN_KE = 4;

	/** 明杠 */
	public static final int GANG = 5;

	/** 补杠 */
	public static final int BU_GANG = 6;

	/** 暗杠 */
	public static final int AN_GANG = 7;

	/** 明坎 */
	public static final int KAN = 8;

	/** 暗坎 */
	public static final int AN_KAN = 9;

	private static ConcurrentHashMap<Integer, Integer> converts = new ConcurrentHashMap<Integer, Integer>();

	static
	{
		converts.put(PlayType.OPERATE_CHI_A_CARD, CardGroupType.KAN);
		converts.put(PlayType.OPERATE_PENG_A_CARD, CardGroupType.KE);
		converts.put(PlayType.OPERATE_GANG_A_CARD, CardGroupType.GANG);
		converts.put(PlayType.OPERATE_BU_GANG_A_CARD, CardGroupType.BU_GANG);
		converts.put(PlayType.OPERATE_AN_GANG, CardGroupType.AN_GANG);
	}

	public static boolean isMingType(int type)
	{
		return type == KAN || type == KE || type == GANG || type == BU_GANG;
	}

	public static boolean isKe(int type)
	{
		return type == KE || type == AN_KE;
	}

	public static boolean isGang(int type)
	{
		return type == GANG || type == AN_GANG || type == BU_GANG;
	}

	public static boolean isJiang(int type)
	{
		return type == JIANG;
	}

	/**
	 * 判断指定类型是应该是在玩家手牌中
	 */
	public static boolean isInHand(int type)
	{
		return type == JIANG || type == AN_KAN || type == AN_KE || type == DAN_ZHANG;// || type == DUI;
	}

	/**
	 * 判断PlayType是否是CardGroupType
	 */
	public static boolean isCardGroupType(int playType)
	{
		return converts.containsKey(playType);
	}

	/**
	 * 将PlayType转换为CardGroupType
	 */
	public static int fromPlayType(int playType)
	{
		if (Macro.AssetTrue(isCardGroupType(playType) == false))
			return INVALID;

		return converts.get(playType);
	}
}