package com.kodgames.battleserver.service.battle.region.guangdong.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kodgames.battleserver.service.battle.common.BattleRulesAnnotation;

/**
 * 潮汕的房间规则定义
 * 
 * @author kod
 *
 */
public class Rules_GuangDong
{
	/** 规则掩码 */
	public static final int RULE_MASK = 0xFFFF0000;
	/** 参数掩码 */
	public static final int ARGS_MASK = 0x0000FFFF;

	/******************** 以下为房间规则 *****************/

	@BattleRulesAnnotation(comment = "潮汕规则", isArea = true)
	public static final int GAME_TYPE_CHAO_SHAN = (1 << 16) | 1;
	@BattleRulesAnnotation(comment = "潮汕推倒胡规则", isArea = true)
	public static final int GAME_TYPE_TUI_DAO_HU = (1 << 16) | 2;
	@BattleRulesAnnotation(comment = "鬼牌潮汕规则", isArea = true)
	public static final int GAME_TYPE_GUI_CHAO_SHAN = (1 << 16) | 3;
	@BattleRulesAnnotation(comment = "潮州规则", isArea = true)
	public static final int GAME_TYPE_CHAO_ZHOU = (1 << 16) | 4;
	@BattleRulesAnnotation(comment = "汕尾规则", isArea = true)
	public static final int GAME_TYPE_SHAN_WEI = (1 << 16) | 5;
	@BattleRulesAnnotation(comment = "汕头规则", isArea = true)
	public static final int GAME_TYPE_SHAN_TOU = (1 << 16) | 6;
	@BattleRulesAnnotation(comment = "普宁规则", isArea = true)
	public static final int GAME_TYPE_PU_NING = (1 << 16) | 7;
	@BattleRulesAnnotation(comment = "惠来规则", isArea = true)
	public static final int GAME_TYPE_HUI_LAI = (1 << 16) | 8;
	@BattleRulesAnnotation(comment = "揭西规则", isArea = true)
	public static final int GAME_TYPE_JIE_XI = (1 << 16) | 9;

	@BattleRulesAnnotation(comment = "无万(潮汕:无字)")
	public static final int GAME_PLAY_NO_WAN = (2 << 16) | 1;
	@BattleRulesAnnotation(comment = "无字 (潮汕:无风)")
	public static final int GAME_PLAY_NO_ZI = (2 << 16) | 2;
	@BattleRulesAnnotation(comment = "三人玩法")
	public static final int GAME_PLAY_THREE_PLAYER = (2 << 16) | 3;
	@BattleRulesAnnotation(comment = "四人玩法")
	public static final int GAME_PLAY_FOUR_PLAYER = (2 << 16) | 4;

	@BattleRulesAnnotation(comment = "吃胡")
	public static final int HU_DIAN_HU = (3 << 16) | 1;
	@BattleRulesAnnotation(comment = "自摸(20分以上可以吃胡)")
	public static final int HU_ZI_MO = (3 << 16) | 2;
	@BattleRulesAnnotation(comment = "自摸胡目标听牌牌形10分限制")
	public static final int HU_ZI_MO_TARGET_SCORE_LIMIT = (3 << 16) | 3;
	@BattleRulesAnnotation(comment = "潮汕十倍不计分")
	public static final int HU_SHI_BEI_BU_JI_FEN = (3 << 16) | 4;
	@BattleRulesAnnotation(comment = "汕头自摸")
	public static final int HU_ZI_MO_SHAN_TOU = (3 << 16) | 5;
	@BattleRulesAnnotation(comment = "汕头20分可吃胡")
	public static final int HU_CHI_HU_ZI_MO_LIMIT = (3 << 16) | 6;

	@BattleRulesAnnotation(comment = "小胡")
	public static final int SCORE_XIAO_HU = (4 << 16) | 1;
	@BattleRulesAnnotation(comment = "鸡胡不能吃胡")
	public static final int SCORE_XIAO_HU_MUST_ZI_MO = (4 << 16) | 2;
	@BattleRulesAnnotation(comment = "跟庄")
	public static final int SCORE_GEN_ZHUANG = (4 << 16) | 3;
	@BattleRulesAnnotation(comment = "三番起胡")
	public static final int SAN_FAN_QI_HU = (4 << 16) | 4;
	@BattleRulesAnnotation(comment = "流局算杠")
	public static final int LIU_JU_SUAN_GANG = (4 << 16) | 5;
	@BattleRulesAnnotation(comment = "跟庄1分")
	public static final int SCORE_GEN_ZHUANG_YI_FEN = (4 << 16) | 6;
	@BattleRulesAnnotation(comment = "跟庄2分")
	public static final int SCORE_GEN_ZHUANG_LIANG_FEN = (4 << 16) | 7;
	@BattleRulesAnnotation(comment = "连庄")
	public static final int SCORE_LIAN_ZHUANG = (4 << 16) | 8;
	@BattleRulesAnnotation(comment = "必胡")
	public static final int SCORE_MUST_HU = (4 << 16) | 9;
	@BattleRulesAnnotation(comment = "鸡胡")
	public static final int SCORE_JI_HU = (4 << 16) | 10;
	@BattleRulesAnnotation(comment = "鸡大")
	public static final int SCORE_JI_DA = (4 << 16) | 11;

	@BattleRulesAnnotation(comment = "封顶5倍")
	public static final int SCORE_LIMIT_5 = (5 << 16) | 1;
	@BattleRulesAnnotation(comment = "封顶10倍")
	public static final int SCORE_LIMIT_10 = (5 << 16) | 2;
	@BattleRulesAnnotation(comment = "不设封顶")
	public static final int SCORE_LIMIT_NONE = (5 << 16) | 3;
	@BattleRulesAnnotation(comment = "杠跟底分")
	public static final int GANG_GEN_DI_FEN = (5 << 16) | 4;

	@BattleRulesAnnotation(comment = "鬼牌_无鬼牌")
	public static final int MASTER_CARD_NONE = (6 << 16) | 1;
	@BattleRulesAnnotation(comment = "鬼牌_红中")
	public static final int MASTER_CARD_HONG_ZHONG = (6 << 16) | 2;
	@BattleRulesAnnotation(comment = "鬼牌_白板")
	public static final int MASTER_CARD_BAI_BAN = (6 << 16) | 3;
	@BattleRulesAnnotation(comment = "鬼牌_翻鬼")
	public static final int MASTER_CARD_GENERATE = (6 << 16) | 4;
	@BattleRulesAnnotation(comment = "鬼牌_双鬼")
	public static final int MASTER_CARD_TOW_MASTER = (6 << 16) | 5;
	@BattleRulesAnnotation(comment = "鬼牌_无鬼加倍")
	public static final int MASTER_CARD_NONE_DOUBLE = (6 << 16) | 6;
	@BattleRulesAnnotation(comment = "鬼牌_四鬼胡牌")
	public static final int MASTER_CARD_FORE_HU = (6 << 16) | 7;
	@BattleRulesAnnotation(comment = "鬼牌_双倍")
	public static final int MASTER_CARD_DOUBLE_SCORE = (6 << 16) | 8;

	@BattleRulesAnnotation(comment = "马牌_无马")
	public static final int BETTING_HOUSE_0 = (7 << 16) | 1;
	@BattleRulesAnnotation(comment = "马牌_2马")
	public static final int BETTING_HOUSE_2 = (7 << 16) | 2;
	@BattleRulesAnnotation(comment = "马牌_5马")
	public static final int BETTING_HOUSE_5 = (7 << 16) | 3;
	@BattleRulesAnnotation(comment = "马牌_8马")
	public static final int BETTING_HOUSE_8 = (7 << 16) | 4;
	@BattleRulesAnnotation(comment = "马牌_马跟杠")
	public static final int BETTING_HOUSE_WITH_GANG = (7 << 16) | 5;
	@BattleRulesAnnotation(comment = "马牌_马跟牌")
	public static final int BETTING_HOUSE_MA_GEN_PAI = (7 << 16) | 6;
	@BattleRulesAnnotation(comment = "马牌_1马")
	public static final int BETTING_HOUSE_1 = (7 << 16) | 7;
	@BattleRulesAnnotation(comment = "奖马翻倍")
	public static final int BETTING_HOUSE_DOUBLE = (7 << 16) | 8;
	@BattleRulesAnnotation(comment = "奖马加番")
	public static final int BETTING_HOUSE_ADD_FAN = (7 << 16) | 9;
	@BattleRulesAnnotation(comment = "奖马加番")
	public static final int FAN_2 = (7 << 16) | 10;
	@BattleRulesAnnotation(comment = "奖马加番")
	public static final int FAN_4 = (7 << 16) | 11;
	@BattleRulesAnnotation(comment = "奖马加番")
	public static final int FAN_6 = (7 << 16) | 12;
	@BattleRulesAnnotation(comment = "马牌_3马")
	public static final int BETTING_HOUSE_3 = (7 << 16) | 13;

	@BattleRulesAnnotation(comment = "可以抢杠胡")
	public static final int CAN_QIANG_GANG_HU = (8 << 16) | 1;
	@BattleRulesAnnotation(comment = "抢杠全包")
	public static final int QIANG_GANG_QUAN_BAO = (8 << 16) | 2;

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
	@BattleRulesAnnotation(comment = "抢杠胡3倍")
	public static final int QIANG_GANG_HU_THREE = (9 << 16) | 8;
	@BattleRulesAnnotation(comment = "海底捞月2倍")
	public static final int HAI_DI_LAO_YUE_DOUBLE = (9 << 16) | 9;
	@BattleRulesAnnotation(comment = "杠爆2倍")
	public static final int GANG_BAO_DOUBLE = (9 << 16) | 10;
	@BattleRulesAnnotation(comment = "吃杠杠爆全包")
	public static final int CHI_GANG_GANG_BAO_QUAN_BAO = (9 << 16) | 11;

	// 大的牌型
	@BattleRulesAnnotation(comment = "全牌型")
	public static final int SHAN_TOU_QUAN_PAI_XING = (10 << 16) | 1;
	@BattleRulesAnnotation(comment = "小胡（鸡胡两分，未选牌型4分)")
	public static final int SHAN_TOU_XIAO_HU = (10 << 16) | 2;
	@BattleRulesAnnotation(comment = "大胡（汕头）")
	public static final int SHAN_TOU_DA_HU = (10 << 16) | 3;

	// 汕头的具体牌型选项
	@BattleRulesAnnotation(comment = "碰碰胡4分")
	public static final int SHAN_TOU_PENG_PENG_HU = (11 << 16) | 1;
	@BattleRulesAnnotation(comment = "混一色4分")
	public static final int SHAN_TOU_HUN_YI_SE = (11 << 16) | 2;
	@BattleRulesAnnotation(comment = "清一色4分")
	public static final int SHAN_TOU_QING_YI_SE = (11 << 16) | 3;
	@BattleRulesAnnotation(comment = "一条龙6分")
	public static final int SHAN_TOU_YI_TIAO_LONG = (11 << 16) | 4;
	@BattleRulesAnnotation(comment = "一九10分 ")
	public static final int SHAN_TOU_YAO_JIU = (11 << 16) | 5;
	@BattleRulesAnnotation(comment = "小三元10分")
	public static final int SHAN_TOU_XIAO_SAN_YUAN = (11 << 16) | 6;
	@BattleRulesAnnotation(comment = "小四喜10分")
	public static final int SHAN_TOU_XIAO_SI_XI = (11 << 16) | 7;
	@BattleRulesAnnotation(comment = "字一色10分")
	public static final int SHAN_TOU_ZI_YI_SE = (11 << 16) | 8;
	@BattleRulesAnnotation(comment = "十三幺26分")
	public static final int SHAN_TOU_SHI_SAN_YAO = (11 << 16) | 9;
	@BattleRulesAnnotation(comment = "大三元20分")
	public static final int SHAN_TOU_DA_SAN_YUAN = (11 << 16) | 10;
	@BattleRulesAnnotation(comment = "大四喜20分")
	public static final int SHAN_TOU_DA_SI_XI = (11 << 16) | 11;
	@BattleRulesAnnotation(comment = "十八罗汉36分")
	public static final int SHAN_TOU_SHI_BA_LUO_HAN = (11 << 16) | 12;
	@BattleRulesAnnotation(comment = "七对6、豪七10、双豪20、三豪30")
	public static final int SHAN_TOU_QI_DUI_ALL = (11 << 16) | 13;
	@BattleRulesAnnotation(comment = "天湖40分、地胡20分")
	public static final int SHAN_TOU_TIAN_DI = (11 << 16) | 14;

	@BattleRulesAnnotation(comment = "无买马")
	public static final int NO_BUG_HORSE = (12 << 16) | 1;
	@BattleRulesAnnotation(comment = "买1马")
	public static final int BUY_HORSE_1 = (12 << 16) | 2;
	@BattleRulesAnnotation(comment = "买2马")
	public static final int BUY_HORSE_2 = (12 << 16) | 3;
	@BattleRulesAnnotation(comment = "罚1马")
	public static final int PUNISH_HORSE_1 = (12 << 16) | 4;
	@BattleRulesAnnotation(comment = "罚2马")
	public static final int PUNISH_HORSE_2 = (12 << 16) | 5;

	/** 潮汕房间规则 */
	private static List<Integer> chaoShanRules = new ArrayList<Integer>();

	/** 推倒胡房间规则 */
	private static List<Integer> tuiDaoHuRules = new ArrayList<Integer>();

	/** 鬼牌潮汕房间规则 */
	private static List<Integer> guiPaiChaoShanHuRules = new ArrayList<Integer>();

	/** 潮州房间规则 */
	private static List<Integer> chaoZhouRules = new ArrayList<Integer>();

	/** 汕尾房间规则 */
	private static List<Integer> shanWeiRules = new ArrayList<Integer>();

	/** 汕头房间规则 */
	private static List<Integer> shanTouRules = new ArrayList<Integer>();

	/** 普宁房间规则 */
	private static List<Integer> puNingRules = new ArrayList<Integer>();

	/** 惠来房间规则 */
	private static List<Integer> huiLaiRules = new ArrayList<Integer>();

	/** 揭西房间规则 */
	private static List<Integer> jieXiRules = new ArrayList<Integer>();

	private static Map<Integer, List<Integer>> rulesMap = new HashMap<Integer, List<Integer>>();

	static
	{
		/**
		 * 潮汕规则
		 */
		chaoShanRules.add(GAME_TYPE_CHAO_SHAN);

		// 无风无字和三人
		chaoShanRules.add(GAME_PLAY_NO_WAN);
		chaoShanRules.add(GAME_PLAY_NO_ZI);
		chaoShanRules.add(GAME_PLAY_THREE_PLAYER);
		chaoShanRules.add(GAME_PLAY_FOUR_PLAYER);

		// 吃胡，自摸，10倍不计分
		chaoShanRules.add(HU_DIAN_HU);
		chaoShanRules.add(HU_ZI_MO);
		chaoShanRules.add(HU_ZI_MO_TARGET_SCORE_LIMIT);

		// 小胡，鸡胡不能吃胡，跟庄
		chaoShanRules.add(SCORE_XIAO_HU);
		chaoShanRules.add(SCORE_XIAO_HU_MUST_ZI_MO);
		chaoShanRules.add(SCORE_GEN_ZHUANG);
		chaoShanRules.add(SCORE_LIAN_ZHUANG);
		chaoShanRules.add(LIU_JU_SUAN_GANG);
		chaoShanRules.add(CHI_GANG_GANG_BAO_QUAN_BAO);
		chaoShanRules.add(SCORE_MUST_HU);

		// 封顶5倍，封顶10倍，无封顶
		chaoShanRules.add(SCORE_LIMIT_5);
		chaoShanRules.add(SCORE_LIMIT_10);
		chaoShanRules.add(SCORE_LIMIT_NONE);

		// 奖马相关
		chaoShanRules.add(BETTING_HOUSE_0);
		chaoShanRules.add(BETTING_HOUSE_2);
		chaoShanRules.add(BETTING_HOUSE_5);
		chaoShanRules.add(BETTING_HOUSE_8);
		chaoShanRules.add(BETTING_HOUSE_WITH_GANG);

		// 罚马相关
		chaoShanRules.add(NO_BUG_HORSE);
		chaoShanRules.add(BUY_HORSE_1);
		chaoShanRules.add(BUY_HORSE_2);
		chaoShanRules.add(PUNISH_HORSE_1);
		chaoShanRules.add(PUNISH_HORSE_2);

		/**
		 * 推倒胡的规则
		 */
		tuiDaoHuRules.add(GAME_TYPE_TUI_DAO_HU);

		tuiDaoHuRules.add(GAME_PLAY_NO_ZI);
		tuiDaoHuRules.add(CAN_QIANG_GANG_HU);
		tuiDaoHuRules.add(QIANG_GANG_QUAN_BAO);
		tuiDaoHuRules.add(SCORE_GEN_ZHUANG);
		tuiDaoHuRules.add(LIU_JU_SUAN_GANG);
		tuiDaoHuRules.add(CHI_GANG_GANG_BAO_QUAN_BAO);

		tuiDaoHuRules.add(QI_DUI_DOUBLE);
		tuiDaoHuRules.add(PENG_PENG_HU_DOUBLE);
		tuiDaoHuRules.add(QI_DUI_FOUR_TIMES);
		tuiDaoHuRules.add(QING_YI_SE_FOUR_TIMES);
		tuiDaoHuRules.add(YAO_JIU_SIX_TIMES);
		tuiDaoHuRules.add(QUANG_FENG_EIGHT_TIMES);
		tuiDaoHuRules.add(SHI_SAN_YAO_EIGHT_TIMES);
		tuiDaoHuRules.add(SCORE_LIAN_ZHUANG);

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

		/**
		 * 鬼牌潮汕规则
		 */
		guiPaiChaoShanHuRules.add(GAME_TYPE_GUI_CHAO_SHAN);

		guiPaiChaoShanHuRules.add(GAME_PLAY_NO_WAN);
		guiPaiChaoShanHuRules.add(GAME_PLAY_NO_ZI);

		guiPaiChaoShanHuRules.add(HU_DIAN_HU);
		guiPaiChaoShanHuRules.add(HU_ZI_MO);
		guiPaiChaoShanHuRules.add(HU_ZI_MO_TARGET_SCORE_LIMIT);
		guiPaiChaoShanHuRules.add(SCORE_MUST_HU);

		guiPaiChaoShanHuRules.add(SCORE_XIAO_HU);
		guiPaiChaoShanHuRules.add(SCORE_XIAO_HU_MUST_ZI_MO);
		guiPaiChaoShanHuRules.add(SCORE_GEN_ZHUANG);
		guiPaiChaoShanHuRules.add(SCORE_LIAN_ZHUANG);
		guiPaiChaoShanHuRules.add(LIU_JU_SUAN_GANG);
		guiPaiChaoShanHuRules.add(CHI_GANG_GANG_BAO_QUAN_BAO);

		guiPaiChaoShanHuRules.add(SCORE_LIMIT_5);
		guiPaiChaoShanHuRules.add(SCORE_LIMIT_10);
		guiPaiChaoShanHuRules.add(SCORE_LIMIT_NONE);

		guiPaiChaoShanHuRules.add(MASTER_CARD_NONE);
		guiPaiChaoShanHuRules.add(MASTER_CARD_HONG_ZHONG);
		guiPaiChaoShanHuRules.add(MASTER_CARD_BAI_BAN);
		guiPaiChaoShanHuRules.add(MASTER_CARD_GENERATE);
		guiPaiChaoShanHuRules.add(MASTER_CARD_TOW_MASTER);
		guiPaiChaoShanHuRules.add(MASTER_CARD_NONE_DOUBLE);
		guiPaiChaoShanHuRules.add(MASTER_CARD_FORE_HU);
		guiPaiChaoShanHuRules.add(MASTER_CARD_DOUBLE_SCORE);

		guiPaiChaoShanHuRules.add(BETTING_HOUSE_0);
		guiPaiChaoShanHuRules.add(BETTING_HOUSE_2);
		guiPaiChaoShanHuRules.add(BETTING_HOUSE_5);
		guiPaiChaoShanHuRules.add(BETTING_HOUSE_8);
		guiPaiChaoShanHuRules.add(BETTING_HOUSE_WITH_GANG);

		/**
		 * 潮州规则
		 */
		chaoZhouRules.add(GAME_TYPE_CHAO_ZHOU);

		// 人数
		chaoZhouRules.add(GAME_PLAY_THREE_PLAYER);
		chaoZhouRules.add(GAME_PLAY_FOUR_PLAYER);

		// 无风 无字
		chaoZhouRules.add(GAME_PLAY_NO_ZI);
		chaoZhouRules.add(GAME_PLAY_NO_WAN);

		// 鬼牌
		chaoZhouRules.add(MASTER_CARD_NONE);
		chaoZhouRules.add(MASTER_CARD_BAI_BAN);
		chaoZhouRules.add(MASTER_CARD_HONG_ZHONG);
		chaoZhouRules.add(MASTER_CARD_GENERATE);
		chaoZhouRules.add(MASTER_CARD_NONE_DOUBLE);
		chaoZhouRules.add(MASTER_CARD_FORE_HU);
		chaoZhouRules.add(MASTER_CARD_DOUBLE_SCORE);

		// 买马
		chaoZhouRules.add(NO_BUG_HORSE);
		chaoZhouRules.add(BUY_HORSE_1);
		chaoZhouRules.add(BUY_HORSE_2);
		chaoZhouRules.add(PUNISH_HORSE_1);
		chaoZhouRules.add(PUNISH_HORSE_2);

		// 跟庄 十倍不计分
		chaoZhouRules.add(SCORE_GEN_ZHUANG_YI_FEN);
		chaoZhouRules.add(HU_SHI_BEI_BU_JI_FEN);
		chaoZhouRules.add(SCORE_LIAN_ZHUANG);
		chaoZhouRules.add(LIU_JU_SUAN_GANG);
		chaoZhouRules.add(CHI_GANG_GANG_BAO_QUAN_BAO);
		chaoZhouRules.add(SCORE_MUST_HU);

		// 奖马
		chaoZhouRules.add(BETTING_HOUSE_0);
		chaoZhouRules.add(BETTING_HOUSE_2);
		chaoZhouRules.add(BETTING_HOUSE_5);
		chaoZhouRules.add(BETTING_HOUSE_8);
		chaoZhouRules.add(BETTING_HOUSE_WITH_GANG);

		// 买马
		chaoZhouRules.add(NO_BUG_HORSE);
		chaoZhouRules.add(BUY_HORSE_1);
		chaoZhouRules.add(BUY_HORSE_2);
		chaoZhouRules.add(PUNISH_HORSE_1);
		chaoZhouRules.add(PUNISH_HORSE_2);

		/**
		 * 汕尾规则
		 */
		shanWeiRules.add(GAME_TYPE_SHAN_WEI);

		// 人数
		shanWeiRules.add(GAME_PLAY_THREE_PLAYER);
		shanWeiRules.add(GAME_PLAY_FOUR_PLAYER);

		// 无字
		shanWeiRules.add(GAME_PLAY_NO_WAN);

		shanWeiRules.add(SAN_FAN_QI_HU);
		shanWeiRules.add(SCORE_GEN_ZHUANG);
		shanWeiRules.add(SCORE_LIAN_ZHUANG);
		shanWeiRules.add(LIU_JU_SUAN_GANG);
		shanWeiRules.add(CHI_GANG_GANG_BAO_QUAN_BAO);

		shanWeiRules.add(MASTER_CARD_NONE);
		shanWeiRules.add(MASTER_CARD_HONG_ZHONG);
		shanWeiRules.add(MASTER_CARD_BAI_BAN);
		shanWeiRules.add(MASTER_CARD_GENERATE);

		shanWeiRules.add(BETTING_HOUSE_0);
		shanWeiRules.add(BETTING_HOUSE_2);
		shanWeiRules.add(BETTING_HOUSE_5);
		shanWeiRules.add(BETTING_HOUSE_8);
		shanWeiRules.add(BETTING_HOUSE_DOUBLE);
		shanWeiRules.add(BETTING_HOUSE_ADD_FAN);
		shanWeiRules.add(BETTING_HOUSE_WITH_GANG);
		shanWeiRules.add(FAN_2);
		shanWeiRules.add(FAN_4);
		shanWeiRules.add(FAN_6);

		shanWeiRules.add(NO_BUG_HORSE);
		shanWeiRules.add(BUY_HORSE_1);
		shanWeiRules.add(BUY_HORSE_2);
		shanWeiRules.add(PUNISH_HORSE_1);
		shanWeiRules.add(PUNISH_HORSE_2);

		/**
		 * 汕头房间规则
		 */
		shanTouRules.add(GAME_TYPE_SHAN_TOU);

		// 无风、无字
		shanTouRules.add(GAME_PLAY_NO_WAN);
		shanTouRules.add(GAME_PLAY_NO_ZI);

		// 人数
		shanTouRules.add(GAME_PLAY_THREE_PLAYER);
		shanTouRules.add(GAME_PLAY_FOUR_PLAYER);

		// 吃胡、自摸、吃胡分数限制、鸡胡不能吃胡、十倍不计分
		shanTouRules.add(HU_DIAN_HU);
		shanTouRules.add(HU_ZI_MO_SHAN_TOU);
		shanTouRules.add(HU_CHI_HU_ZI_MO_LIMIT);
		shanTouRules.add(SCORE_XIAO_HU_MUST_ZI_MO);
		shanTouRules.add(HU_ZI_MO_TARGET_SCORE_LIMIT);
		shanTouRules.add(SCORE_LIAN_ZHUANG);
		shanTouRules.add(LIU_JU_SUAN_GANG);

		// 倍数(全牌型、小胡、大胡)
		shanTouRules.add(SHAN_TOU_QUAN_PAI_XING);
		shanTouRules.add(SHAN_TOU_XIAO_HU);
		shanTouRules.add(SHAN_TOU_DA_HU);

		// 特殊牌型分数
		shanTouRules.add(QIANG_GANG_HU_THREE);
		shanTouRules.add(HAI_DI_LAO_YUE_DOUBLE);
		shanTouRules.add(GANG_BAO_DOUBLE);
		shanTouRules.add(CHI_GANG_GANG_BAO_QUAN_BAO);

		// 牌型分数
		shanTouRules.add(SHAN_TOU_PENG_PENG_HU);
		shanTouRules.add(SHAN_TOU_HUN_YI_SE);
		shanTouRules.add(SHAN_TOU_QING_YI_SE);
		shanTouRules.add(SHAN_TOU_YI_TIAO_LONG);
		shanTouRules.add(SHAN_TOU_YAO_JIU);
		shanTouRules.add(SHAN_TOU_XIAO_SAN_YUAN);
		shanTouRules.add(SHAN_TOU_XIAO_SI_XI);
		shanTouRules.add(SHAN_TOU_ZI_YI_SE);
		shanTouRules.add(SHAN_TOU_SHI_SAN_YAO);
		shanTouRules.add(SHAN_TOU_DA_SAN_YUAN);
		shanTouRules.add(SHAN_TOU_DA_SI_XI);
		shanTouRules.add(SHAN_TOU_SHI_BA_LUO_HAN);
		shanTouRules.add(SHAN_TOU_QI_DUI_ALL);
		shanTouRules.add(SHAN_TOU_TIAN_DI);

		// 特殊分数（跟庄、流局算杠）
		shanTouRules.add(SCORE_GEN_ZHUANG);
		shanTouRules.add(LIU_JU_SUAN_GANG);

		// 封顶
		shanTouRules.add(SCORE_LIMIT_NONE);
		shanTouRules.add(SCORE_LIMIT_5);
		shanTouRules.add(SCORE_LIMIT_10);

		// 鬼牌
		shanTouRules.add(MASTER_CARD_NONE);
		shanTouRules.add(MASTER_CARD_BAI_BAN);
		shanTouRules.add(MASTER_CARD_HONG_ZHONG);
		shanTouRules.add(MASTER_CARD_GENERATE);
		shanTouRules.add(MASTER_CARD_NONE_DOUBLE);
		shanTouRules.add(MASTER_CARD_FORE_HU);
		shanTouRules.add(MASTER_CARD_DOUBLE_SCORE);

		// 马牌
		shanTouRules.add(BETTING_HOUSE_0);
		shanTouRules.add(BETTING_HOUSE_2);
		shanTouRules.add(BETTING_HOUSE_5);
		shanTouRules.add(BETTING_HOUSE_8);
		shanTouRules.add(BETTING_HOUSE_WITH_GANG);

		shanTouRules.add(NO_BUG_HORSE);
		shanTouRules.add(BUY_HORSE_1);
		shanTouRules.add(BUY_HORSE_2);
		shanTouRules.add(PUNISH_HORSE_1);
		shanTouRules.add(PUNISH_HORSE_2);

		/**
		 * 普宁房间规则
		 */
		puNingRules.add(GAME_TYPE_PU_NING);

		// 胡牌
		puNingRules.add(HU_DIAN_HU);
		puNingRules.add(HU_ZI_MO);
		puNingRules.add(HU_ZI_MO_TARGET_SCORE_LIMIT);

		// 结算
		puNingRules.add(CHI_GANG_GANG_BAO_QUAN_BAO);
		puNingRules.add(SCORE_GEN_ZHUANG);
		puNingRules.add(SCORE_GEN_ZHUANG_YI_FEN);
		puNingRules.add(SCORE_GEN_ZHUANG_LIANG_FEN);
		puNingRules.add(SCORE_LIAN_ZHUANG);

		// 封顶
		puNingRules.add(SCORE_LIMIT_5);
		puNingRules.add(SCORE_LIMIT_10);
		puNingRules.add(SCORE_LIMIT_NONE);

		// 鬼牌
		puNingRules.add(MASTER_CARD_NONE);
		puNingRules.add(MASTER_CARD_BAI_BAN);
		puNingRules.add(MASTER_CARD_HONG_ZHONG);
		puNingRules.add(MASTER_CARD_GENERATE);
		puNingRules.add(MASTER_CARD_NONE_DOUBLE);
		puNingRules.add(MASTER_CARD_FORE_HU);
		puNingRules.add(MASTER_CARD_DOUBLE_SCORE);

		// 奖马
		puNingRules.add(BETTING_HOUSE_0);
		puNingRules.add(BETTING_HOUSE_2);
		puNingRules.add(BETTING_HOUSE_5);
		puNingRules.add(BETTING_HOUSE_8);
		puNingRules.add(BETTING_HOUSE_WITH_GANG);

		// 买马
		puNingRules.add(NO_BUG_HORSE);
		puNingRules.add(BUY_HORSE_1);
		puNingRules.add(BUY_HORSE_2);
		puNingRules.add(PUNISH_HORSE_1);
		puNingRules.add(PUNISH_HORSE_2);

		/**
		 * 惠来玩法规则
		 */
		{
			huiLaiRules.add(GAME_TYPE_HUI_LAI);

			huiLaiRules.add(HU_DIAN_HU);
			huiLaiRules.add(HU_ZI_MO);
			huiLaiRules.add(HU_ZI_MO_TARGET_SCORE_LIMIT);

			huiLaiRules.add(GANG_GEN_DI_FEN);
			huiLaiRules.add(LIU_JU_SUAN_GANG);
			huiLaiRules.add(SCORE_GEN_ZHUANG);
			huiLaiRules.add(SCORE_LIAN_ZHUANG);

			huiLaiRules.add(SCORE_LIMIT_NONE);
			huiLaiRules.add(SCORE_LIMIT_5);
			huiLaiRules.add(SCORE_LIMIT_10);

			huiLaiRules.add(MASTER_CARD_NONE);
			huiLaiRules.add(MASTER_CARD_BAI_BAN);
			huiLaiRules.add(MASTER_CARD_HONG_ZHONG);
			huiLaiRules.add(MASTER_CARD_GENERATE);
			huiLaiRules.add(MASTER_CARD_NONE_DOUBLE);
			huiLaiRules.add(MASTER_CARD_FORE_HU);
			huiLaiRules.add(MASTER_CARD_DOUBLE_SCORE);

			huiLaiRules.add(BETTING_HOUSE_0);
			huiLaiRules.add(BETTING_HOUSE_2);
			huiLaiRules.add(BETTING_HOUSE_5);
			huiLaiRules.add(BETTING_HOUSE_8);
			huiLaiRules.add(BETTING_HOUSE_WITH_GANG);

			huiLaiRules.add(NO_BUG_HORSE);
			huiLaiRules.add(BUY_HORSE_1);
			huiLaiRules.add(BUY_HORSE_2);
			huiLaiRules.add(PUNISH_HORSE_1);
			huiLaiRules.add(PUNISH_HORSE_2);
		}

		/**
		 * 揭西玩法
		 */
		{
			jieXiRules.add(GAME_TYPE_JIE_XI);

			jieXiRules.add(GAME_PLAY_THREE_PLAYER);
			jieXiRules.add(GAME_PLAY_FOUR_PLAYER);

			jieXiRules.add(LIU_JU_SUAN_GANG);
			jieXiRules.add(CHI_GANG_GANG_BAO_QUAN_BAO);
			jieXiRules.add(SCORE_GEN_ZHUANG_YI_FEN);
			jieXiRules.add(SCORE_LIAN_ZHUANG);
			jieXiRules.add(SCORE_JI_HU);
			jieXiRules.add(SCORE_JI_DA);

			jieXiRules.add(SCORE_LIMIT_5);
			jieXiRules.add(SCORE_LIMIT_10);
			jieXiRules.add(SCORE_LIMIT_NONE);

			jieXiRules.add(MASTER_CARD_NONE);
			jieXiRules.add(MASTER_CARD_HONG_ZHONG);
			jieXiRules.add(MASTER_CARD_BAI_BAN);
			jieXiRules.add(MASTER_CARD_GENERATE);
			jieXiRules.add(MASTER_CARD_NONE_DOUBLE);
			jieXiRules.add(MASTER_CARD_FORE_HU);
			jieXiRules.add(MASTER_CARD_DOUBLE_SCORE);

			jieXiRules.add(BETTING_HOUSE_0);
			jieXiRules.add(BETTING_HOUSE_2);
			jieXiRules.add(BETTING_HOUSE_5);
			jieXiRules.add(BETTING_HOUSE_8);
			jieXiRules.add(BETTING_HOUSE_WITH_GANG);

			jieXiRules.add(NO_BUG_HORSE);
			jieXiRules.add(BUY_HORSE_1);
			jieXiRules.add(BUY_HORSE_2);
			jieXiRules.add(PUNISH_HORSE_1);
			jieXiRules.add(PUNISH_HORSE_2);

		}

		// 添加到map中
		rulesMap.put(GAME_TYPE_CHAO_SHAN, chaoShanRules);
		rulesMap.put(GAME_TYPE_SHAN_TOU, shanTouRules);
		rulesMap.put(GAME_TYPE_SHAN_WEI, shanWeiRules);
		rulesMap.put(GAME_TYPE_CHAO_ZHOU, chaoZhouRules);
		rulesMap.put(GAME_TYPE_GUI_CHAO_SHAN, guiPaiChaoShanHuRules);
		rulesMap.put(GAME_TYPE_TUI_DAO_HU, tuiDaoHuRules);
		rulesMap.put(GAME_TYPE_PU_NING, puNingRules);
		rulesMap.put(GAME_TYPE_HUI_LAI, huiLaiRules);
		rulesMap.put(GAME_TYPE_JIE_XI, jieXiRules);
	}

	/**
	 * 检查规则是否匹配
	 * 
	 * @param rules 客户端传的规则
	 * @param gameType 需要检查的房间规则定义（对应房间的规则）
	 * @return
	 */
	public static boolean checkRules(List<Integer> rules, int gameType)
	{
		// 如果规则为空或者不包含需要创建的房间规则，或者传了这个房间不应该存在的规则
		if (rules == null || rules.isEmpty() || !rules.contains(gameType) || !rulesMap.get(gameType).containsAll(rules))
			return false;

		// 如果规则中含有重复规则，非法
		Set<Integer> rulesSet = new HashSet<Integer>();
		rules.stream().forEach(rule -> rulesSet.add(rule));
		if (rulesSet.size() != rules.size())
			return false;

		return true;
	}

	/**
	 * 获取地区规则
	 * 
	 * @param rules
	 * @return
	 */
	public static int getArea(List<Integer> rules)
	{
		for (int rule : rules)
		{
			if ((rule & (1 << 16)) != 0)
				return rule;
		}

		return 0;
	}
}
