package com.kodgames.battleserver.service.battle.region.neimeng.common;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 内蒙独有的牌类型
 */
public class CardType_DunLaPao
{

	/** 可以蹲 */
	public static final byte CAN_DUN = 0;
	/** 可以拉 */
	public static final byte CAN_LA = 1;
	/** 可以跑 */
	public static final byte CAN_PAO = 2;

	/** 选择蹲 */
	public static final byte DUN = 10;
	/** 选择拉 */
	public static final byte LA = 11;
	/** 选择跑 */
	public static final byte PAO = 12;
	/** 放弃蹲拉跑 */
	public static final byte NO_DUN_LA_PAO = 13;

	private static final Map<Byte, Integer> dunLaPao = new HashMap<>();

	static
	{
		dunLaPao.put(DUN, PlayType.DISPLAY_DUN);
		dunLaPao.put(LA, PlayType.DISPLAY_LA);
		dunLaPao.put(PAO, PlayType.DISPLAY_PAO);
	}

	public static int getDunLaPaoDisplayType(byte card)
	{
		Integer result = dunLaPao.get(card);
		return (null == result) ? PlayType.UNKNOW : result.intValue();
	}

}
