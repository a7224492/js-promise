package com.kodgames.battleserver.service.battle.region.meizhou.common.processor;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;

public class BettingHorseHelper_MeiZhou
{
	/**
	 * 玩家对应的奖马信息数组
	 */
	private static final byte[][] HOUSE_CARD_TYPE_LIST = {

		// 庄家：1、5、9 东 中
		{CardType.WAN.Value(), (byte)(CardType.WAN.Value() + 4), (byte)(CardType.WAN.Value() + 8), CardType.TIAO.Value(), (byte)(CardType.TIAO.Value() + 4), (byte)(CardType.TIAO.Value() + 8),
			CardType.TONG.Value(), (byte)(CardType.TONG.Value() + 4), (byte)(CardType.TONG.Value() + 8), CardType.ZI.Value(), (byte)(CardType.ZI.Value() + 4)},

		// 庄家的下家（逆时针）：2、6 南、发
		{(byte)(CardType.WAN.Value() + 1), (byte)(CardType.WAN.Value() + 5), (byte)(CardType.TIAO.Value() + 1), (byte)(CardType.TIAO.Value() + 5), (byte)(CardType.TONG.Value() + 1),
			(byte)(CardType.TONG.Value() + 5), (byte)(CardType.ZI.Value() + 1), (byte)(CardType.ZI.Value() + 5)},

		// 再下家：3、7 西、白
		{(byte)(CardType.WAN.Value() + 2), (byte)(CardType.WAN.Value() + 6), (byte)(CardType.TIAO.Value() + 2), (byte)(CardType.TIAO.Value() + 6), (byte)(CardType.TONG.Value() + 2),
			(byte)(CardType.TONG.Value() + 6), (byte)(CardType.ZI.Value() + 2), (byte)(CardType.ZI.Value() + 6)},

		// 再下家：4、8 北
		{(byte)(CardType.WAN.Value() + 3), (byte)(CardType.WAN.Value() + 7), (byte)(CardType.TIAO.Value() + 3), (byte)(CardType.TIAO.Value() + 7), (byte)(CardType.TONG.Value() + 3),
			(byte)(CardType.TONG.Value() + 7), (byte)(CardType.ZI.Value() + 3)}};

	/**
	 * 三人玩家对应的奖马信息数组
	 */
	private static final byte[][] HOUSE_CARD_TYPE_LIST_THREE = {

		// 庄家：1、4、7、东、北、 白
		{CardType.WAN.Value(), (byte)(CardType.WAN.Value() + 3), (byte)(CardType.WAN.Value() + 6), CardType.TIAO.Value(), (byte)(CardType.TIAO.Value() + 3), (byte)(CardType.TIAO.Value() + 6),
			CardType.TONG.Value(), (byte)(CardType.TONG.Value() + 3), (byte)(CardType.TONG.Value() + 6), CardType.ZI.Value(), (byte)(CardType.ZI.Value() + 3), (byte)(CardType.ZI.Value() + 6)},

		// 庄家的下家（逆时针）：2、5、8、南、中
		{(byte)(CardType.WAN.Value() + 1), (byte)(CardType.WAN.Value() + 4), (byte)(CardType.WAN.Value() + 7), (byte)(CardType.TIAO.Value() + 1), (byte)(CardType.TIAO.Value() + 4),
			(byte)(CardType.TIAO.Value() + 7), (byte)(CardType.TONG.Value() + 1), (byte)(CardType.TONG.Value() + 4), (byte)(CardType.TONG.Value() + 7), (byte)(CardType.ZI.Value() + 1),
			(byte)(CardType.ZI.Value() + 4)},

		// 对家：3、6、9、西、發
		{(byte)(CardType.WAN.Value() + 2), (byte)(CardType.WAN.Value() + 5), (byte)(CardType.WAN.Value() + 8), (byte)(CardType.TIAO.Value() + 2), (byte)(CardType.TIAO.Value() + 5),
			(byte)(CardType.TIAO.Value() + 8), (byte)(CardType.TONG.Value() + 2), (byte)(CardType.TONG.Value() + 5), (byte)(CardType.TONG.Value() + 8), (byte)(CardType.ZI.Value() + 2),
			(byte)(CardType.ZI.Value() + 5)}};

	private static final byte[][] HOUSE_CARD_TYPE_LIST_TWO = {
		// 庄家：1.3. 5.7 东 西 中 白
		{CardType.WAN.Value(), (byte)(CardType.WAN.Value() + 2), (byte)(CardType.WAN.Value() + 4), (byte)(CardType.WAN.Value() + 6), CardType.TIAO.Value(), (byte)(CardType.TIAO.Value() + 2),
			(byte)(CardType.TIAO.Value() + 4), (byte)(CardType.TIAO.Value() + 6), CardType.TONG.Value(), (byte)(CardType.TONG.Value() + 2), (byte)(CardType.TONG.Value() + 4),
			(byte)(CardType.TONG.Value() + 6), CardType.ZI.Value(), (byte)(CardType.ZI.Value() + 2), (byte)(CardType.ZI.Value() + 4), (byte)(CardType.ZI.Value() + 6)},
		// 对家：2.4.6.8 .9 南 北 发
		{(byte)(CardType.WAN.Value() + 1), (byte)(CardType.WAN.Value() + 3), (byte)(CardType.WAN.Value() + 5), (byte)(CardType.WAN.Value() + 7), (byte)(CardType.WAN.Value() + 8),
			(byte)(CardType.TIAO.Value() + 1), (byte)(CardType.TIAO.Value() + 3), (byte)(CardType.TIAO.Value() + 5), (byte)(CardType.TIAO.Value() + 7), (byte)(CardType.TIAO.Value() + 8),
			(byte)(CardType.TONG.Value() + 1), (byte)(CardType.TONG.Value() + 3), (byte)(CardType.TONG.Value() + 5), (byte)(CardType.TONG.Value() + 7), (byte)(CardType.TONG.Value() + 8),
			(byte)(CardType.ZI.Value() + 1), (byte)(CardType.ZI.Value() + 3), (byte)(CardType.ZI.Value() + 5)}

	};

	/**
	 * 获取指定玩家应该奖马的牌
	 *
	 * @param context     战斗信息
	 * @param roleId      玩家id
	 * @param firstRoleId 相对的第一个的玩家id
	 */
	public static byte[] getHouseCardTypeList(BattleBean context, int roleId, int firstRoleId, int playerCount)
	{
		if (roleId == firstRoleId)
			return playerCount == 3 ? HOUSE_CARD_TYPE_LIST_THREE[0] : playerCount == 2 ? HOUSE_CARD_TYPE_LIST_TWO[0] : HOUSE_CARD_TYPE_LIST[0];
		else if (roleId == context.getNextRoleId(firstRoleId))
			return playerCount == 3 ? HOUSE_CARD_TYPE_LIST_THREE[1] : playerCount == 2 ? HOUSE_CARD_TYPE_LIST_TWO[1] : HOUSE_CARD_TYPE_LIST[1];
		else if (playerCount == 2)
		{
			Macro.AssetTrue(true, "get horse card faild, has three player,but get four");
			return HOUSE_CARD_TYPE_LIST_TWO[1];
		}
		else if (roleId == context.getNextRoleId(context.getNextRoleId(firstRoleId)))
			return playerCount == 3 ? HOUSE_CARD_TYPE_LIST_THREE[2] : HOUSE_CARD_TYPE_LIST[2];
		else if (playerCount == 3)
		{
			Macro.AssetTrue(true, "get horse card faild, has three player,but get four");
			return HOUSE_CARD_TYPE_LIST[3];
		}
		else
			return HOUSE_CARD_TYPE_LIST[3];
	}
}
