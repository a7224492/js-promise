package com.kodgames.battleserver.service.battle.region.yangzhou;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kodgames.battleserver.service.battle.common.BattleRulesAnnotation;

public class Rules_YangZhou
{
	/** 规则掩码 */
	public static final int RULE_MASK = 0xFFFF0000;
	/** 参数掩码 */
	public static final int ARGS_MASK = 0x0000FFFF;

	/******************** 以下为房间规则 *****************/
	@BattleRulesAnnotation(comment = "扬州规则", isArea = true)
	public static final int GAME_TYPE_YANG_ZHOU = (1 << 16) | 1;

	@BattleRulesAnnotation(comment = "园子默认")
	public static final int GAME_PLAY_YUANZI_DEFAULT = (2 << 16);
	@BattleRulesAnnotation(comment = "不限园子")
	public static final int GAME_PLAY_YUANZI_NO_LIMIT = (2 << 16) | 1;
	@BattleRulesAnnotation(comment = "10分园子")
	public static final int GAME_PLAY_YUANZI_10 = (2 << 16) | 2;
	@BattleRulesAnnotation(comment = "20分园子")
	public static final int GAME_PLAY_YUANZI_20 = (2 << 16) | 3;
	@BattleRulesAnnotation(comment = "30分园子")
	public static final int GAME_PLAY_YUANZI_30 = (2 << 16) | 4;

	@BattleRulesAnnotation(comment = "一炮多响")
	public static final int GAME_PLAY_YI_PAO_DUO_XIANG = (3 << 16) | 1;
	@BattleRulesAnnotation(comment = "无一炮多响")
	public static final int GAME_PLAY_NO_YI_PAO_DUO_XIANG = (3 << 16) | 2;
	
	@BattleRulesAnnotation(comment = "搬配子")
	public static final int GAME_PLAY_BAN_PEI_ZI = (4 << 16) | 1;
	@BattleRulesAnnotation(comment = "白板做配子")
	public static final int GAME_PLAY_BAI_BAN_PEI_ZI = (4 << 16) | 2;

	@BattleRulesAnnotation(comment = "四人玩法")
	public static final int GAME_PLAY_SI_REN = (5 << 16) | 1;
	@BattleRulesAnnotation(comment = "三人玩法")
	public static final int GAME_PLAY_SAN_REN = (5 << 16) | 2;
	
	/** 扬州规则 */
	public static List<Integer> yangZhouRules = new ArrayList<>();

	static
	{
		yangZhouRules.add(GAME_TYPE_YANG_ZHOU);
		yangZhouRules.add(GAME_PLAY_YUANZI_NO_LIMIT);
		yangZhouRules.add(GAME_PLAY_YUANZI_10);
		yangZhouRules.add(GAME_PLAY_YUANZI_20);
		yangZhouRules.add(GAME_PLAY_YUANZI_30);
		yangZhouRules.add(GAME_PLAY_YI_PAO_DUO_XIANG);
		yangZhouRules.add(GAME_PLAY_NO_YI_PAO_DUO_XIANG);
		yangZhouRules.add(GAME_PLAY_BAN_PEI_ZI);
		yangZhouRules.add(GAME_PLAY_BAI_BAN_PEI_ZI);
		
		// 房间人数
		yangZhouRules.add(GAME_PLAY_SI_REN);
		yangZhouRules.add(GAME_PLAY_SAN_REN);
	}

	/**
	 * 检查扬州玩法房间是否合法
	 * @param rules 创建房间选择的规则
	 */
	public static boolean checkRules(List<Integer> rules)
	{
		if (rules == null || rules.isEmpty() || !rules.contains(GAME_TYPE_YANG_ZHOU))
			return false;

		// 规则是否有效
		for (int rule : rules)
		{
			if (yangZhouRules.contains(rule) == false)
				return false;
		}

		// 如果规则中含有重复规则，非法
		Set<Integer> rulesSet = new HashSet<Integer>();
		rules.stream().forEach(rule -> rulesSet.add(rule));
		if (rulesSet.size() != rules.size())
			return false;

		return true;
	}
	
	/**
	 * 根据房间规则获取园子分数
	 */
	public static int getYuanZi(List<Integer> rules)
	{
		int yuanZi = 0;
		
		if(rules.contains(Rules_YangZhou.GAME_PLAY_YUANZI_10))
			yuanZi = 10;
		else if(rules.contains(Rules_YangZhou.GAME_PLAY_YUANZI_20))
			yuanZi = 20;
		else if(rules.contains(Rules_YangZhou.GAME_PLAY_YUANZI_30))
			yuanZi = 30;
			 		
		return yuanZi;
	}
}
