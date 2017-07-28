package com.kodgames.battleserver.service.battle.region.neimeng.common;

import java.util.*;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.BattleRulesAnnotation;

public class Rules_NeiMeng
{
	/**
	 * 规则掩码
	 */
	public static final int RULE_MASK = 0xFFFF0000;
	/**
	 * 参数掩码
	 */
	public static final int ARGS_MASK = 0x0000FFFF;

	/********************
	 * 以下为房间规则
	 *****************/

	@BattleRulesAnnotation(comment = "鄂尔多斯规则", isArea = true)
	public static final int GAME_TYPE_ERDOS = (1 << 16) | 1;
	@BattleRulesAnnotation(comment = "包头规则", isArea = true)
	public static final int GAME_TYPE_BAOTOU = (1 << 16) | 2;
	@BattleRulesAnnotation(comment = "巴彦淖尔规则", isArea = true)
	public static final int GAME_TYPE_BAYANNAOER = (1 << 16) | 3;
	@BattleRulesAnnotation(comment = "呼和浩特规则", isArea = true)
	public static final int GAME_TYPE_HUHEHAOTE = (1 << 16) | 4;
	@BattleRulesAnnotation(comment = "乌兰察布规则", isArea = true)
	public static final int GAME_TYPE_WULANCHABU = (1 << 16) | 5;
	@BattleRulesAnnotation(comment = "乌海规则", isArea = true)
	public static final int GAME_TYPE_WUHAI = (1 << 16) | 6;
	@BattleRulesAnnotation(comment = "赤峰推倒胡", isArea = true)
	public static final int GAME_TYPE_CHIFENG = (1 << 16) | 7;

	/**
	 * 吃碰听规则
	 */
	public static final int CHI_PENG_TING = 2 << 16;
	/**
	 * 可以吃
	 */
	public static final int CAN_CHI = (2 << 16) | 1;
	/**
	 * 可以碰
	 */
	public static final int CAN_PENG = (2 << 16) | 2;
	/**
	 * 可以吃牌上听
	 */
	public static final int CHI_TING = (2 << 16) | 4;
	/**
	 * 可以碰牌上听
	 */
	public static final int PENG_TING = (2 << 16) | 8;
	/**
	 * 暗牌
	 */
	public static final int AN_PAI = (2 << 16) | 16;

	@BattleRulesAnnotation(comment = "不可吃牌，不可碰牌")
	public static final int NO_CHI_NO_PENG = CHI_PENG_TING;
	@BattleRulesAnnotation(comment = "不可吃牌，可以碰牌")
	public static final int NO_CHI_CAN_PENG = CHI_PENG_TING | CAN_PENG;
	@BattleRulesAnnotation(comment = "不可吃牌，碰牌上听")
	public static final int NO_CHI_PENG_TING = CHI_PENG_TING | CAN_PENG | PENG_TING;
	@BattleRulesAnnotation(comment = "可以吃牌，可以碰牌")
	public static final int CAN_CHI_CAN_PENG = CHI_PENG_TING | CAN_CHI | CAN_PENG;
	@BattleRulesAnnotation(comment = "吃牌上听，可以碰牌")
	public static final int CHI_TING_CAN_PENG = CHI_PENG_TING | CAN_CHI | CHI_TING | CAN_PENG;
	@BattleRulesAnnotation(comment = "一口香（吃牌上听，碰牌上听）")
	public static final int CHI_TING_PENG_TING = CHI_PENG_TING | CAN_CHI | CHI_TING | CAN_PENG | PENG_TING;
	@BattleRulesAnnotation(comment = "黑摸")
	public static final int HEI_MO = CHI_PENG_TING | AN_PAI;

	@BattleRulesAnnotation(comment = "蹲拉跑")
	public static final int DUN_LA_PAO = 3 << 16;
	@BattleRulesAnnotation(comment = "蹲拉跑2分")
	public static final int DUN_LA_PAO_2 = (3 << 16) | 2;
	@BattleRulesAnnotation(comment = "蹲拉跑4分")
	public static final int DUN_LA_PAO_4 = (3 << 16) | 4;
	@BattleRulesAnnotation(comment = "蹲拉跑6分")
	public static final int DUN_LA_PAO_6 = (3 << 16) | 6;
	@BattleRulesAnnotation(comment = "蹲拉跑8分")
	public static final int DUN_LA_PAO_8 = (3 << 16) | 8;
	@BattleRulesAnnotation(comment = "蹲拉跑10分")
	public static final int DUN_LA_PAO_10 = (3 << 16) | 10;

	@BattleRulesAnnotation(comment = "无封顶")
	public static final int HU_SCORE_LIMIT = 4 << 16;
	@BattleRulesAnnotation(comment = "胡牌上限10分")
	public static final int HU_SCORE_LIMIT_10 = (4 << 16) | 10;
	@BattleRulesAnnotation(comment = "胡牌上限15分")
	public static final int HU_SCORE_LIMIT_15 = (4 << 16) | 15;
	@BattleRulesAnnotation(comment = "胡牌上限20分")
	public static final int HU_SCORE_LIMIT_20 = (4 << 16) | 20;

	@BattleRulesAnnotation(comment = "一炮多响")
	public static final int YI_PAO_DUO_XIANG = (5 << 16) | 1;
	@BattleRulesAnnotation(comment = "可以点炮")
	public static final int CAN_DIAN_HU = (5 << 16) | 2;
	@BattleRulesAnnotation(comment = "可以听")
	public static final int CAN_TING = (5 << 16) | 3;

	@BattleRulesAnnotation(comment = "不带风")
	public static final int NO_FENG = (6 << 16) | 1;

	@BattleRulesAnnotation(comment = "十三幺")
	public static final int SHI_SAN_YAO = (7 << 16) | 1;

	@BattleRulesAnnotation(comment = "四人局")
	public static final int SI_REN_JU = (8 << 16) | 1;
	@BattleRulesAnnotation(comment = "三人局")
	public static final int SAN_REN_JU = (8 << 16) | 2;

	@BattleRulesAnnotation(comment = "少一门")
	public static final int SHAO_YI_MEN = (9 << 16) | 1;

	@BattleRulesAnnotation(comment = "摊八张")
	public static final int TAN_BA_ZHANG = (10 << 16) | 1;

	@BattleRulesAnnotation(comment = "连庄加分")
	public static final int LIAN_ZHUANG_JIA_FEN = (11 << 16) | 1;

	@BattleRulesAnnotation(comment = "不赔杠")
	public static final int BU_PEI_GANG = (12 << 16) | 1;
	@BattleRulesAnnotation(comment = "赔三家")
	public static final int PEI_SAN_JIA = (12 << 16) | 2;
	@BattleRulesAnnotation(comment = "赔点杠人")
	public static final int PEI_DIAN_GANG_REN = (12 << 16) | 3;

	@BattleRulesAnnotation(comment = "抢杠胡")
	public static final int QIANG_GANG_HU = (13 << 16) | 1;

	@BattleRulesAnnotation(comment = "财神")
	public static final int CAI_SHEN = (14 << 16) | 1;

	/**
	 * 鄂尔多斯规则
	 */
	private static List<Integer> erdosRules = new ArrayList<>();
	/**
	 * 包头规则
	 */
	private static List<Integer> baotouRules = new ArrayList<>();
	/**
	 * 巴彦淖尔规则
	 */
	private static List<Integer> bayannaoerRules = new ArrayList<>();
	/**
	 * 呼和浩特规则
	 */
	private static List<Integer> huhehaoteRules = new ArrayList<>();
	/**
	 * 乌兰察布规则
	 */
	private static List<Integer> wulanchabuRules = new ArrayList<>();
	/**
	 * 乌海规则
	 */
	private static List<Integer> wuhaiRules = new ArrayList<>();
	/**
	 * 赤峰推倒胡规则
	 */
	private static List<Integer> chiFengRules = new ArrayList<>();
	/**
	 * 房间对应的可选规则列表
	 */
	private static Map<Integer, List<Integer>> rulesMap = new HashMap<>();

	static
	{
		// 鄂尔多斯
		erdosRules.add(GAME_TYPE_ERDOS);
		erdosRules.add(NO_CHI_NO_PENG);
		erdosRules.add(NO_CHI_CAN_PENG);
		erdosRules.add(NO_CHI_PENG_TING);
		erdosRules.add(CAN_CHI_CAN_PENG);
		erdosRules.add(CHI_TING_CAN_PENG);
		erdosRules.add(CHI_TING_PENG_TING);
		erdosRules.add(DUN_LA_PAO);
		erdosRules.add(DUN_LA_PAO_2);
		erdosRules.add(DUN_LA_PAO_4);
		erdosRules.add(DUN_LA_PAO_6);
		erdosRules.add(DUN_LA_PAO_8);
		erdosRules.add(DUN_LA_PAO_10);
		erdosRules.add(YI_PAO_DUO_XIANG);
		erdosRules.add(NO_FENG);
		erdosRules.add(SI_REN_JU);
		erdosRules.add(SAN_REN_JU);
		erdosRules.add(SHAO_YI_MEN);

		// 包头
		baotouRules.add(GAME_TYPE_BAOTOU);
		baotouRules.add(NO_CHI_NO_PENG);
		baotouRules.add(NO_CHI_CAN_PENG);
		baotouRules.add(NO_CHI_PENG_TING);
		baotouRules.add(CAN_CHI_CAN_PENG);
		baotouRules.add(CHI_TING_CAN_PENG);
		baotouRules.add(CHI_TING_PENG_TING);
		baotouRules.add(HU_SCORE_LIMIT);
		baotouRules.add(HU_SCORE_LIMIT_10);
		baotouRules.add(HU_SCORE_LIMIT_15);
		baotouRules.add(HU_SCORE_LIMIT_20);
		baotouRules.add(YI_PAO_DUO_XIANG);
		baotouRules.add(NO_FENG);
		baotouRules.add(SI_REN_JU);
		baotouRules.add(SAN_REN_JU);
		baotouRules.add(SHAO_YI_MEN);

		// 巴彦淖尔
		bayannaoerRules.add(GAME_TYPE_BAYANNAOER);
		bayannaoerRules.add(NO_CHI_CAN_PENG);
		bayannaoerRules.add(NO_CHI_PENG_TING);
		bayannaoerRules.add(CHI_TING_CAN_PENG);
		bayannaoerRules.add(CHI_TING_PENG_TING);
		bayannaoerRules.add(YI_PAO_DUO_XIANG);
		bayannaoerRules.add(NO_FENG);
		bayannaoerRules.add(SHI_SAN_YAO);
		bayannaoerRules.add(SI_REN_JU);
		bayannaoerRules.add(SAN_REN_JU);
		bayannaoerRules.add(SHAO_YI_MEN);

		// 呼和浩特
		huhehaoteRules.add(GAME_TYPE_HUHEHAOTE);
		huhehaoteRules.add(NO_CHI_CAN_PENG);
		huhehaoteRules.add(NO_CHI_PENG_TING);
		huhehaoteRules.add(CHI_TING_CAN_PENG);
		huhehaoteRules.add(CHI_TING_PENG_TING);
		huhehaoteRules.add(DUN_LA_PAO);
		huhehaoteRules.add(DUN_LA_PAO_2);
		huhehaoteRules.add(DUN_LA_PAO_4);
		huhehaoteRules.add(DUN_LA_PAO_6);
		huhehaoteRules.add(DUN_LA_PAO_8);
		huhehaoteRules.add(DUN_LA_PAO_10);
		huhehaoteRules.add(YI_PAO_DUO_XIANG);
		huhehaoteRules.add(NO_FENG);
		huhehaoteRules.add(SI_REN_JU);
		huhehaoteRules.add(SAN_REN_JU);
		huhehaoteRules.add(SHAO_YI_MEN);

		// 乌兰察布
		wulanchabuRules.add(GAME_TYPE_WULANCHABU);
		wulanchabuRules.add(NO_CHI_NO_PENG);
		wulanchabuRules.add(NO_CHI_CAN_PENG);
		wulanchabuRules.add(NO_CHI_PENG_TING);
		wulanchabuRules.add(CHI_TING_CAN_PENG);
		wulanchabuRules.add(CHI_TING_PENG_TING);
		wulanchabuRules.add(HEI_MO);
		wulanchabuRules.add(DUN_LA_PAO);
		wulanchabuRules.add(DUN_LA_PAO_2);
		wulanchabuRules.add(DUN_LA_PAO_4);
		wulanchabuRules.add(DUN_LA_PAO_6);
		wulanchabuRules.add(DUN_LA_PAO_8);
		wulanchabuRules.add(DUN_LA_PAO_10);
		wulanchabuRules.add(NO_FENG);
		wulanchabuRules.add(TAN_BA_ZHANG);
		wulanchabuRules.add(LIAN_ZHUANG_JIA_FEN);
		wulanchabuRules.add(BU_PEI_GANG);
		wulanchabuRules.add(PEI_SAN_JIA);
		wulanchabuRules.add(PEI_DIAN_GANG_REN);
		wulanchabuRules.add(SI_REN_JU);
		wulanchabuRules.add(SAN_REN_JU);
		wulanchabuRules.add(SHAO_YI_MEN);

		// 乌海规则
		wuhaiRules.add(GAME_TYPE_WUHAI);
		wuhaiRules.add(SI_REN_JU);
		wuhaiRules.add(SAN_REN_JU);
		wuhaiRules.add(SHAO_YI_MEN);
		wuhaiRules.add(QIANG_GANG_HU);
		wuhaiRules.add(YI_PAO_DUO_XIANG);
		wuhaiRules.add(NO_FENG);
		wuhaiRules.add(CAI_SHEN);

		// 赤峰推倒胡规则
		chiFengRules.add(GAME_TYPE_CHIFENG);
		chiFengRules.add(SAN_REN_JU);
		chiFengRules.add(NO_FENG);
		chiFengRules.add(CAN_DIAN_HU);
		chiFengRules.add(CAN_TING);

		rulesMap.put(GAME_TYPE_BAOTOU, baotouRules);
		rulesMap.put(GAME_TYPE_BAYANNAOER, bayannaoerRules);
		rulesMap.put(GAME_TYPE_ERDOS, erdosRules);
		rulesMap.put(GAME_TYPE_HUHEHAOTE, huhehaoteRules);
		rulesMap.put(GAME_TYPE_WUHAI, wuhaiRules);
		rulesMap.put(GAME_TYPE_WULANCHABU, wulanchabuRules);
		rulesMap.put(GAME_TYPE_CHIFENG, chiFengRules);
	}

	public static boolean checkRules(List<Integer> rules, int gameType)
	{
		List<Integer> gameRules = rulesMap.get(gameType);
		Macro.AssetTrue(gameRules == null, "gameType error, gameType is:" + gameType);

		for (Integer rule : rules)
			if (!gameRules.contains(rule))
				return false;

		Set<Integer> erdosRulesSet = new HashSet<>();
		rules.stream().forEach(rule -> erdosRulesSet.add(rule));
		if (erdosRulesSet.size() != rules.size())
			return false;

		return true;
	}

	/**
	 * 房间规则是否支持吃碰听
	 */
	private static boolean checkChiPengTing(List<Integer> rules, int targetRule)
	{
		if (null == rules)
			return false;

		for (Integer rule : rules)
		{
			if ((rule & RULE_MASK) == CHI_PENG_TING)
				return targetRule == (targetRule & rule);
		}

		return false;
	}

	public static boolean canChi(List<Integer> rules)
	{
		return checkChiPengTing(rules, CAN_CHI);
	}

	public static boolean canChiTing(List<Integer> rules)
	{
		return checkChiPengTing(rules, CHI_TING);
	}

	public static boolean canPeng(List<Integer> rules)
	{
		return checkChiPengTing(rules, CAN_PENG);
	}

	public static boolean canPengTing(List<Integer> rules)
	{
		return checkChiPengTing(rules, PENG_TING);
	}

	public static boolean anPai(List<Integer> rules)
	{
		return checkChiPengTing(rules, AN_PAI);
	}

	/**
	 * 获取指定类别规则的分数（蹲拉跑和分数上限）
	 */
	private static int getRulePoint(List<Integer> rules, int ruleKind)
	{
		for (Integer rule : rules)
		{
			if ((rule & RULE_MASK) != ruleKind)
				continue;

			int point = ARGS_MASK & rule;
			if (point > 0)
				return point;
		}

		return 0;
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

	/**
	 * 获取蹲拉跑分数（0为不支持蹲拉跑）
	 */
	public static int getDunLaPaoPoint(List<Integer> rules)
	{
		return getRulePoint(rules, DUN_LA_PAO);
	}

	/**
	 * 获取胡牌上限分数（0为无上限）
	 */
	public static int getHuScoreLimit(List<Integer> rules)
	{
		return getRulePoint(rules, HU_SCORE_LIMIT);
	}
}
