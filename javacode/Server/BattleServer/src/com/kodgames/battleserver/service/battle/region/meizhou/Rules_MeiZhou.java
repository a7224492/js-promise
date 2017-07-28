package com.kodgames.battleserver.service.battle.region.meizhou;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kodgames.battleserver.service.battle.common.BattleRulesAnnotation;

public class Rules_MeiZhou
{
	/** 规则掩码 */
	public static final int RULE_MASK = 0xFFFF0000;
	/** 参数掩码 */
	public static final int ARGS_MASK = 0x0000FFFF;

	/******************** 以下为房间规则 *****************/
	@BattleRulesAnnotation(comment = "红中宝规则", isArea = true)
	public static final int GAME_TYPE_MEI_ZHOU = (1 << 16) | 1;
	@BattleRulesAnnotation(comment = "推倒胡规则", isArea = true)
	public static final int GAME_TYPE_TUI_DAO_HU = (1 << 16) | 2;
	@BattleRulesAnnotation(comment = "梅县庄规则", isArea = true)
	public static final int GAME_TYPE_MEI_XIAN_ZHUANG = (1 << 16) | 3;
	@BattleRulesAnnotation(comment = "河源规则", isArea = true)
	public static final int GAME_TYPE_HE_YUAN = (1 << 16) | 4;
	@BattleRulesAnnotation(comment = "惠州规则", isArea = true)
	public static final int GAME_TYPE_HUI_ZHOU = (1 << 16) | 5;

	@BattleRulesAnnotation(comment = "软三")
	public static final int GAME_PLAY_RUAN_SAN = (2 << 16) | 1;
	@BattleRulesAnnotation(comment = "硬三")
	public static final int GAME_PLAY_YING_SAN = (2 << 16) | 2;
	@BattleRulesAnnotation(comment = "叠加吃胡")
	public static final int GAME_PLAY_DIE_JIA_HU = (2 << 16) | 3;
	@BattleRulesAnnotation(comment = "4张花牌")
	public static final int GAME_PLAY_HUA_4 = (2 << 16) | 4;
	@BattleRulesAnnotation(comment = "8张花牌")
	public static final int GAME_PLAY_HUA_8 = (2 << 16) | 5;
	@BattleRulesAnnotation(comment = "百搭鸡胡")
	public static final int GAME_PLAY_JI_BAI_DA = (2 << 16) | 6;
	@BattleRulesAnnotation(comment = "平鸡")
	public static final int GAME_PLAY_JI_PING = (2 << 16) | 7;

	@BattleRulesAnnotation(comment = "无马")
	public static final int GAME_PLAY_BETTING_HOUSE_0 = (3 << 16) | 1;
	@BattleRulesAnnotation(comment = "2马")
	public static final int GAME_PLAY_BETTING_HOUSE_2 = (3 << 16) | 2;
	@BattleRulesAnnotation(comment = "5马")
	public static final int GAME_PLAY_BETTING_HOUSE_5 = (3 << 16) | 3;
	@BattleRulesAnnotation(comment = "8马")
	public static final int GAME_PLAY_BETTING_HOUSE_8 = (3 << 16) | 4;
	@BattleRulesAnnotation(comment = "马跟杠")
	public static final int GAME_PLAY_MA_GEN_GANG = (3 << 16) | 5;
	@BattleRulesAnnotation(comment = "4马")
	public static final int GAME_PLAY_BETTING_HOUSE_4 = (3 << 16) | 6;
	@BattleRulesAnnotation(comment = "6马")
	public static final int GAME_PLAY_BETTING_HOUSE_6 = (3 << 16) | 7;
	@BattleRulesAnnotation(comment = "硬马")
	public static final int GAME_PLAY_YING_MA = (3 << 16) | 8;
	@BattleRulesAnnotation(comment = "马跟翻")
	public static final int GAME_PLAY_MA_GEN_FAN = (3 << 16) | 9;
	@BattleRulesAnnotation(comment = "马跟分")
	public static final int GAME_PLAY_MA_GEN_FEN = (3 << 16) | 10;

	@BattleRulesAnnotation(comment = "包杠")
	public static final int GAME_PLAY_BAO_GANG = (4 << 16) | 1;
	@BattleRulesAnnotation(comment = "不包杠")
	public static final int GAME_PLAY_BU_BAO_GANG = (4 << 16) | 2;

	@BattleRulesAnnotation(comment = "抢杠1倍")
	public static final int GAME_PLAY_QIANG_GANG_1 = (5 << 16) | 1;
	@BattleRulesAnnotation(comment = "抢杠2倍")
	public static final int GAME_PLAY_QIANG_GANG_2 = (5 << 16) | 2;

	@BattleRulesAnnotation(comment = "四人玩法")
	public static final int GAME_PLAY_SI_REN = (6 << 16) | 1;
	@BattleRulesAnnotation(comment = "三人玩法")
	public static final int GAME_PLAY_SAN_REN = (6 << 16) | 2;
	@BattleRulesAnnotation(comment = "二人玩法")
	public static final int GAME_PLAY_TWO_PLAYER = (6 << 16) | 4;

	@BattleRulesAnnotation(comment = "鬼牌_无鬼牌")
	public static final int MASTER_CARD_NONE = (7 << 16) | 1;
	@BattleRulesAnnotation(comment = "鬼牌_红中")
	public static final int MASTER_CARD_HONG_ZHONG = (7 << 16) | 2;
	@BattleRulesAnnotation(comment = "鬼牌_白板")
	public static final int MASTER_CARD_BAI_BAN = (7 << 16) | 3;
	@BattleRulesAnnotation(comment = "鬼牌_翻鬼")
	public static final int MASTER_CARD_GENERATE = (7 << 16) | 4;
	@BattleRulesAnnotation(comment = "鬼牌_双鬼")
	public static final int MASTER_CARD_TOW_MASTER = (7 << 16) | 5;
	@BattleRulesAnnotation(comment = "鬼牌_无鬼加倍")
	public static final int MASTER_CARD_NONE_DOUBLE = (7 << 16) | 6;
	@BattleRulesAnnotation(comment = "鬼牌_四鬼胡牌")
	public static final int MASTER_CARD_FORE_HU = (7 << 16) | 7;
	@BattleRulesAnnotation(comment = "鬼牌_双倍")
	public static final int MASTER_CARD_DOUBLE_SCORE = (7 << 16) | 8;

	@BattleRulesAnnotation(comment = "可以抢杠胡")
	public static final int CAN_QIANG_GANG_HU = (8 << 16) | 1;
	@BattleRulesAnnotation(comment = "抢杠全包")
	public static final int QIANG_GANG_QUAN_BAO = (8 << 16) | 2;
	@BattleRulesAnnotation(comment = "跟庄")
	public static final int SCORE_GEN_ZHUANG = (8 << 16) | 3;
	@BattleRulesAnnotation(comment = "无字 (广东:无风)")
	public static final int GAME_PLAY_NO_ZI = (8 << 16) | 4;

	@BattleRulesAnnotation(comment = "七对2倍")
	public static final int QI_DUI_DOUBLE = (9 << 16) | 1;
	@BattleRulesAnnotation(comment = "七对4倍")
	public static final int QI_DUI_FOUR_TIMES = (9 << 16) | 2;
	@BattleRulesAnnotation(comment = "碰碰胡2倍")
	public static final int PENG_PENG_HU_DOUBLE = (9 << 16) | 3;
	@BattleRulesAnnotation(comment = "清一色4倍")
	public static final int QING_YI_SE_FOUR_TIMES = (9 << 16) | 4;
	@BattleRulesAnnotation(comment = "全风8倍")
	public static final int QUANG_FENG_EIGHT_TIMES = (9 << 16) | 5;
	@BattleRulesAnnotation(comment = "十三幺8倍")
	public static final int SHI_SAN_YAO_EIGHT_TIMES = (9 << 16) | 6;
	@BattleRulesAnnotation(comment = "幺九6倍")
	public static final int YAO_JIU_SIX_TIMES = (9 << 16) | 7;

	@BattleRulesAnnotation(comment = "马牌_无马")
	public static final int BETTING_HOUSE_0 = (10 << 16) | 1;
	@BattleRulesAnnotation(comment = "马牌_2马")
	public static final int BETTING_HOUSE_2 = (10 << 16) | 2;
	@BattleRulesAnnotation(comment = "马牌_5马")
	public static final int BETTING_HOUSE_5 = (10 << 16) | 3;
	@BattleRulesAnnotation(comment = "马牌_8马")
	public static final int BETTING_HOUSE_8 = (10 << 16) | 4;
	@BattleRulesAnnotation(comment = "马牌_马跟牌")
	public static final int BETTING_HOUSE_MA_GEN_PAI = (10 << 16) | 5;
	@BattleRulesAnnotation(comment = "马牌_马跟杠")
	public static final int BETTING_HOUSE_WITH_GANG = (10 << 16) | 6;

	/** 红中宝房间规则 */
	public static List<Integer> meiZhouRules = new ArrayList<>();

	/** 推倒胡房间规则 */
	private static List<Integer> tuiDaoHuRules = new ArrayList<>();

	/** 梅县庄房间规则 */
	private static List<Integer> meiXianZhuangRules = new ArrayList<>();

	/** 河源所有的规则 */
	public static List<Integer> heYuanRules = new ArrayList<>();

	/** 惠州所有的规则 */
	public static List<Integer> huiZhouRules = new ArrayList<>();

	static
	{
		/**
		 * 梅州红中宝规则
		 */
		meiZhouRules.add(GAME_TYPE_MEI_ZHOU);

		meiZhouRules.add(GAME_PLAY_RUAN_SAN);
		meiZhouRules.add(GAME_PLAY_YING_SAN);
		meiZhouRules.add(GAME_PLAY_TWO_PLAYER);

		meiZhouRules.add(GAME_PLAY_BETTING_HOUSE_0);
		meiZhouRules.add(GAME_PLAY_BETTING_HOUSE_2);
		meiZhouRules.add(GAME_PLAY_BETTING_HOUSE_5);
		meiZhouRules.add(GAME_PLAY_BETTING_HOUSE_8);
		meiZhouRules.add(GAME_PLAY_MA_GEN_GANG);

		meiZhouRules.add(GAME_PLAY_BAO_GANG);
		meiZhouRules.add(GAME_PLAY_BU_BAO_GANG);

		meiZhouRules.add(GAME_PLAY_QIANG_GANG_1);
		meiZhouRules.add(GAME_PLAY_QIANG_GANG_2);

		meiZhouRules.add(GAME_PLAY_SI_REN);
		meiZhouRules.add(GAME_PLAY_SAN_REN);

		/**
		 * 推倒胡的规则
		 */
		tuiDaoHuRules.add(GAME_TYPE_TUI_DAO_HU);

		tuiDaoHuRules.add(GAME_PLAY_NO_ZI);
		tuiDaoHuRules.add(CAN_QIANG_GANG_HU);
		tuiDaoHuRules.add(QIANG_GANG_QUAN_BAO);
		tuiDaoHuRules.add(SCORE_GEN_ZHUANG);

		tuiDaoHuRules.add(QI_DUI_DOUBLE);
		tuiDaoHuRules.add(PENG_PENG_HU_DOUBLE);
		tuiDaoHuRules.add(QI_DUI_FOUR_TIMES);
		tuiDaoHuRules.add(QING_YI_SE_FOUR_TIMES);
		tuiDaoHuRules.add(YAO_JIU_SIX_TIMES);
		tuiDaoHuRules.add(QUANG_FENG_EIGHT_TIMES);
		tuiDaoHuRules.add(SHI_SAN_YAO_EIGHT_TIMES);

		tuiDaoHuRules.add(MASTER_CARD_NONE);
		tuiDaoHuRules.add(MASTER_CARD_HONG_ZHONG);
		tuiDaoHuRules.add(MASTER_CARD_BAI_BAN);
		tuiDaoHuRules.add(MASTER_CARD_GENERATE);
		tuiDaoHuRules.add(MASTER_CARD_TOW_MASTER);
		tuiDaoHuRules.add(MASTER_CARD_NONE_DOUBLE);
		tuiDaoHuRules.add(MASTER_CARD_FORE_HU);
		tuiDaoHuRules.add(MASTER_CARD_DOUBLE_SCORE);

		tuiDaoHuRules.add(BETTING_HOUSE_0);
		tuiDaoHuRules.add(BETTING_HOUSE_2);
		tuiDaoHuRules.add(BETTING_HOUSE_5);
		tuiDaoHuRules.add(BETTING_HOUSE_8);
		tuiDaoHuRules.add(BETTING_HOUSE_MA_GEN_PAI);
		tuiDaoHuRules.add(BETTING_HOUSE_WITH_GANG);

		/**
		 * 梅县庄规则
		 */
		meiXianZhuangRules.add(GAME_TYPE_MEI_XIAN_ZHUANG);

		meiXianZhuangRules.add(GAME_PLAY_DIE_JIA_HU);

		meiXianZhuangRules.add(GAME_PLAY_BETTING_HOUSE_0);
		meiXianZhuangRules.add(GAME_PLAY_BETTING_HOUSE_2);
		meiXianZhuangRules.add(GAME_PLAY_BETTING_HOUSE_5);
		meiXianZhuangRules.add(GAME_PLAY_BETTING_HOUSE_8);
		meiXianZhuangRules.add(GAME_PLAY_MA_GEN_GANG);

		meiXianZhuangRules.add(GAME_PLAY_BAO_GANG);
		meiXianZhuangRules.add(GAME_PLAY_BU_BAO_GANG);

		meiXianZhuangRules.add(GAME_PLAY_QIANG_GANG_1);
		meiXianZhuangRules.add(GAME_PLAY_QIANG_GANG_2);

		/**
		 * 河源规则
		 */
		heYuanRules.add(GAME_TYPE_HE_YUAN);

		heYuanRules.add(GAME_PLAY_HUA_4);
		heYuanRules.add(GAME_PLAY_HUA_8);

		heYuanRules.add(GAME_PLAY_JI_BAI_DA);
		heYuanRules.add(GAME_PLAY_JI_PING);

		heYuanRules.add(GAME_PLAY_YING_MA);
		heYuanRules.add(GAME_PLAY_MA_GEN_FAN);

		heYuanRules.add(GAME_PLAY_BETTING_HOUSE_0);
		heYuanRules.add(GAME_PLAY_BETTING_HOUSE_2);
		heYuanRules.add(GAME_PLAY_BETTING_HOUSE_4);
		heYuanRules.add(GAME_PLAY_BETTING_HOUSE_6);
		heYuanRules.add(GAME_PLAY_BETTING_HOUSE_8);

		/**
		 * 惠州规则
		 */
		huiZhouRules.add(GAME_TYPE_HUI_ZHOU);

		huiZhouRules.add(GAME_PLAY_MA_GEN_GANG);
		huiZhouRules.add(GAME_PLAY_MA_GEN_FEN);

		huiZhouRules.add(GAME_PLAY_BETTING_HOUSE_0);
		huiZhouRules.add(GAME_PLAY_BETTING_HOUSE_2);
		huiZhouRules.add(GAME_PLAY_BETTING_HOUSE_4);
		huiZhouRules.add(GAME_PLAY_BETTING_HOUSE_6);
		huiZhouRules.add(GAME_PLAY_BETTING_HOUSE_8);

	}

	/**
	 * 检查梅州红中宝玩法房间是否合法
	 * 
	 * @param rules 创建房间选择的规则
	 * @return
	 */
	public static boolean checkHongZhongBaoRules(List<Integer> rules)
	{
		if (rules == null || rules.isEmpty() || !rules.contains(GAME_TYPE_MEI_ZHOU))
			return false;

		// 规则是否有效
		for (int rule : rules)
			if (meiZhouRules.contains(rule) == false)
				return false;

		// 如果规则中含有重复规则，非法
		Set<Integer> rulesSet = new HashSet<Integer>();
		rules.stream().forEach(rule -> rulesSet.add(rule));
		if (rulesSet.size() != rules.size())
			return false;

		return true;
	}

	/**
	 * 检查推倒胡玩法房间是否合法
	 * 
	 * @param rules 创建房间选择的规则
	 * @return
	 */
	public static boolean checkTuiDaoHuRules(List<Integer> rules)
	{
		if (rules == null || rules.isEmpty() || !rules.contains(GAME_TYPE_TUI_DAO_HU))
			return false;

		// 规则是否有效
		for (int rule : rules)
			if (tuiDaoHuRules.contains(rule) == false)
				return false;

		// 如果规则中含有重复规则，非法
		Set<Integer> rulesSet = new HashSet<Integer>();
		rules.stream().forEach(rule -> rulesSet.add(rule));
		if (rulesSet.size() != rules.size())
			return false;

		return true;
	}

	/**
	 * 检查梅县庄玩法房间是否合法
	 * 
	 * @param rules 创建房间选择的规则
	 * @return
	 */
	public static boolean checkMeiXianZhuangRules(List<Integer> rules)
	{
		if (rules == null || rules.isEmpty() || !rules.contains(GAME_TYPE_MEI_XIAN_ZHUANG))
			return false;

		// 规则是否有效
		for (int rule : rules)
			if (meiXianZhuangRules.contains(rule) == false)
				return false;

		// 如果规则中含有重复规则，非法
		Set<Integer> rulesSet = new HashSet<Integer>();
		rules.stream().forEach(rule -> rulesSet.add(rule));
		if (rulesSet.size() != rules.size())
			return false;

		return true;
	}

	/**
	 * 检查河源玩法房间是否合法
	 * 
	 * @param rules 创建房间选择的规则
	 * @return
	 */
	public static boolean checkHeYuanRules(List<Integer> rules)
	{
		if (rules == null || rules.isEmpty() || !rules.contains(GAME_TYPE_HE_YUAN))
			return false;

		// 规则是否有效
		for (int rule : rules)
			if (heYuanRules.contains(rule) == false)
				return false;

		// 如果规则中含有重复规则，非法
		Set<Integer> rulesSet = new HashSet<Integer>();
		rules.stream().forEach(rule -> rulesSet.add(rule));
		if (rulesSet.size() != rules.size())
			return false;

		return true;
	}

	/**
	 * 检查惠州玩法房间是否合法
	 * 
	 * @param rules 创建房间选择的规则
	 * @return
	 */
	public static boolean checkHuiZhouRules(List<Integer> rules)
	{
		if (rules == null || rules.isEmpty() || !rules.contains(GAME_TYPE_HUI_ZHOU))
			return false;

		// 规则是否有效
		for (int rule : rules)
			if (huiZhouRules.contains(rule) == false)
				return false;

		// 如果规则中含有重复规则，非法
		Set<Integer> rulesSet = new HashSet<Integer>();
		rules.stream().forEach(rule -> rulesSet.add(rule));
		if (rulesSet.size() != rules.size())
			return false;

		return true;
	}
}
