package com.kodgames.battleserver.service.battle.region.neimeng.wuhai.creator;

import java.util.List;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.IBattleCreator;
import com.kodgames.battleserver.service.battle.core.finish.PlayerFinishChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_7DuiHu;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_PingHu;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker;
import com.kodgames.battleserver.service.battle.core.operation.OperationCheckerBase;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_AnGang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_BuGang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Gang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Pass;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Peng;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreCalculator;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreSourceFilter_LastOperator;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter_Common;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreCalculator;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreFilter;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_AnGang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_BuGang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_Gang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_PinHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_QiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_MenQianQing;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QiangGangHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QingYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_YiTiaoLong;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_ZiMo;
import com.kodgames.battleserver.service.battle.processer.BattleProcesser;
import com.kodgames.battleserver.service.battle.processer.CalculateProcesser;
import com.kodgames.battleserver.service.battle.processer.DealCardProcesser;
import com.kodgames.battleserver.service.battle.processer.InitCardPoolProcesser;
import com.kodgames.battleserver.service.battle.processer.ZhuangInfoProcesser;
import com.kodgames.battleserver.service.battle.region.neimeng.common.Rules_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.common.finish.BattleFinishChecker_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.common.operation.OperationChecker_Hu_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.common.operation.filter.OperationResultFilter_PassHu_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle.BattleScoreCalculator_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle.BattleScoreChecker_Hua;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu.HuSubScoreChecker_HaoHuaQiDui;
import com.kodgames.battleserver.service.battle.region.neimeng.wuhai.score.battle.BattleScoreChecker_PeiGang_WuHai;
import com.kodgames.battleserver.service.battle.region.neimeng.wuhai.score.filter.BattleScoreFilter_RemoveGangWhenHuangZhuang_WuHai;
import com.kodgames.battleserver.service.battle.region.neimeng.wuhai.score.filter.ScoreTargetFilter_Common_NeiMeng_WuHai;
import com.kodgames.battleserver.service.battle.region.neimeng.wuhai.score.hu.HuSubScoreChecker_ChaoHaoHuaQiDui;
import com.kodgames.battleserver.service.battle.region.neimeng.wuhai.zhuang.ZhuangCalculator_NeiMeng_WuHai;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 玩法配置器
 * 
 * 内蒙：呼和浩特
 */
public class BattleCreator_NeiMeng_WuHai implements IBattleCreator
{
	@Override
	public JSONObject create(List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleHelper.class.getSimpleName());

		createZhuangCalculator(result, rules);
		createHuCalculateProcessor(result, rules);
		createHuCheckerProcessor(result, rules);
		createBattleScoreCalculator(result, rules);
		createBattleFinishChecker(result, rules);
		createMainProcessor(result, rules);

		return result;
	}

	@Override
	public boolean checkRules(List<Integer> rules)
	{
		return Rules_NeiMeng.checkRules(rules, Rules_NeiMeng.GAME_TYPE_WUHAI);
	}

	@Override
	public int getMaxPlayerSize(List<Integer> rules)
	{
		if (rules.contains(Rules_NeiMeng.SAN_REN_JU))
			return MahjongConstant.DEFAULT_ROOM_MEMBER_COUNT - 1;

		return MahjongConstant.DEFAULT_ROOM_MEMBER_COUNT;
	}

	/**
	 * 创建庄家计时器
	 */
	private void createZhuangCalculator(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(ZhuangCalculator_NeiMeng_WuHai.class);
		context.element(BattleHelper.KEY_ZHUANG_CALCULATOR, createContext);
	}

	/**
	 * 胡牌得分计算器
	 */
	private void createHuCalculateProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(HuScoreProcessor.class);

		// 分数来源玩家
		{
			JSONArray arrayContext = CreateContextHelper.createArray();
			arrayContext.add(CreateContextHelper.createObject(ScoreSourceFilter_LastOperator.class));
			createContext.element(HuScoreProcessor.KEY_SOURCE_PLAYER_FILTERS, arrayContext);
		}

		// 分数目标玩家
		{
			JSONArray arrayContext = CreateContextHelper.createArray();
			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Common.class));
			createContext.element(HuScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		// 支持的分数
		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 基础牌型：平和，七对，豪华七对，超豪华七对
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_PinHu.class, ScoreCalculateType.TOTAL_ADD, 1));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_QiDui.class, ScoreCalculateType.TOTAL_ADD, 8));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HaoHuaQiDui.class, ScoreCalculateType.TOTAL_ADD, 18, new int[] {PlayType.HU_QI_DUI}));
			arrayContext
				.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ChaoHaoHuaQiDui.class, ScoreCalculateType.TOTAL_ADD, 36, new int[] {PlayType.HU_QI_DUI, PlayType.HU_HAO_HUA_QI_DUI}));

			// 附加牌型：清一色，一条龙
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYiSe.class, ScoreCalculateType.TOTAL_ADD, 8));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_YiTiaoLong.class, ScoreCalculateType.TOTAL_ADD, 8));

			// 特殊加分项：门清，自摸
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_MenQianQing.class, ScoreCalculateType.TOTAL_ADD, 1));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZiMo.class, ScoreCalculateType.TOTAL_ADD, 1));

			// 抢杠胡
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QiangGangHu.class, ScoreCalculateType.TOTAL_MULTI_2ND, 3));

			createContext.element(HuScoreProcessor.KEY_SCORE_CHECKERS, arrayContext);
		}

		context.element(BattleHelper.KEY_HU_SCORE_PROCESSOR, createContext);
	}

	/**
	 * 创建胡牌检查处理器
	 */
	private void createHuCheckerProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(HuChecker.class);

		/**
		 * 胡牌行为过滤器
		 */
		{
		}

		/**
		 * 胡牌牌形检查器
		 */
		{
			JSONArray checkers = CreateContextHelper.createArray();
			checkers.element(CreateContextHelper.createObject(HuChecker_PingHu.class));
			checkers.element(CreateContextHelper.createObject(HuChecker_7DuiHu.class));
			createContext.element(HuChecker.KEY_CHECKERS, checkers);
		}

		context.element(BattleHelper.KEY_HU_CHECK_PROCESSOR, createContext);
	}

	/**
	 * 创建战斗分数计算器
	 */
	private void createBattleScoreCalculator(JSONObject createContext, List<Integer> rules)
	{
		JSONObject context = CreateContextHelper.createObject(BattleScoreCalculator_NeiMeng.class);
		context.element(BattleScoreCalculator.KEY_MAX_VALUE, 0);
		createContext.element(BattleHelper.KEY_BATTLE_SCORE_CALCULATOR, context);
	}

	/**
	 * 创建牌局结束检查器
	 */
	private void createBattleFinishChecker(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleFinishChecker_NeiMeng.class);

		result.element(BattleFinishChecker_NeiMeng.KEY_stayCardCount, 0);

		context.element(BattleHelper.KEY_BATTLE_FINISH_CHECKER, result);
	}

	private void createMainProcessor(JSONObject context, List<Integer> rules)
	{
		JSONArray result = CreateContextHelper.createArray();

		// 确定庄家
		result.element(CreateContextHelper.createObject(ZhuangInfoProcesser.class));

		// 初始化牌池
		{
			JSONObject subContext = CreateContextHelper.createObject(InitCardPoolProcesser.class);

			// 设置支持什么类型的牌
			{
				JSONArray supportTypeContext = CreateContextHelper.createArray();

				supportTypeContext.add(MahjongConstant.CardType.WAN.toString());
				supportTypeContext.add(MahjongConstant.CardType.TIAO.toString());

				if (!rules.contains(Rules_NeiMeng.SHAO_YI_MEN))
					supportTypeContext.add(MahjongConstant.CardType.TONG.toString());

				if (!rules.contains(Rules_NeiMeng.NO_FENG))
					supportTypeContext.add(MahjongConstant.CardType.FENG.toString());

				if (rules.contains(Rules_NeiMeng.CAI_SHEN))
					supportTypeContext.add(MahjongConstant.CardType.HUA.toString());

				subContext.element(InitCardPoolProcesser.KEY_SUPPORT_CARD_TYPES, supportTypeContext);
			}

			result.element(subContext);
		}

		// 发牌
		result.element(CreateContextHelper.createObject(DealCardProcesser.class));

		// 打牌
		result.element(createBattleProcessor(rules));

		// 分数计算
		result.element(createCalculate(rules));

		context.element(MahjongConstant.JSON_PROC, result);
	}

	private JSONObject createBattleProcessor(List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleProcesser.class);

		createOperationCheck(result, rules);
		createPlayerFinishFilter(result, rules);
		createGangScoreProcessor(result, rules);

		return result;
	}

	private void createOperationCheck(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject();

		{
			JSONArray proc = CreateContextHelper.createArray();
			{
				JSONArray afterFilters = CreateContextHelper.createArray();
				{
					// 碰检测
					{
						JSONObject processor = CreateContextHelper.createObject(OperationChecker_Peng.class);
						processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
						proc.element(processor);
					}

					// 明杠检测
					{
						JSONObject processor = CreateContextHelper.createObject(OperationChecker_Gang.class);
						processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
						proc.element(processor);
					}

					// 补杠
					{
						JSONObject processor = CreateContextHelper.createObject(OperationChecker_BuGang.class);
						processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
						proc.element(processor);
					}

					// 暗杠
					{
						JSONObject processor = CreateContextHelper.createObject(OperationChecker_AnGang.class);
						processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
						proc.element(processor);
					}

					// 胡牌
					{
						JSONObject processor = CreateContextHelper.createObject(OperationChecker_Hu_NeiMeng.class);

						{
							JSONArray preFilters = CreateContextHelper.createArray();

							// 前置操作必须是摸牌，出牌，补杠（抢杠胡）
							preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_DEAL));
							preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_PLAY_A_CARD));
							if (rules.contains(Rules_NeiMeng.QIANG_GANG_HU))
								preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_BU_GANG_A_CARD));

							processor.element(OperationCheckerBase.KEY_PRE_FILTER, preFilters);
						}

						// 过胡
						{
							JSONObject filter = CreateContextHelper.createObject(OperationResultFilter_PassHu_NeiMeng.class);
							JSONArray tingOperations = CreateContextHelper.createArray();

							filter.element(OperationResultFilter_PassHu_NeiMeng.KEY_CHECK_PASS_ZI_MO_HU, true);
							filter.element(OperationResultFilter_PassHu_NeiMeng.KEY_CHECK_PASS_DIAN_HU, true);
							filter.element(OperationResultFilter_PassHu_NeiMeng.KEY_tingOperations, tingOperations);
							afterFilters.element(filter);

							processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
						}

						// 是否可以一炮多响
						{
							JSONArray canYiPaoDuoXiang = CreateContextHelper.createArray();

							canYiPaoDuoXiang.element(rules.contains(Rules_NeiMeng.YI_PAO_DUO_XIANG));

							processor.element(OperationChecker_Hu_NeiMeng.KEY_YI_PAO_DUO_XIANG, canYiPaoDuoXiang);
						}

						proc.element(processor);
					}

					// pass检测
					{
						JSONObject processor = CreateContextHelper.createObject(OperationChecker_Pass.class);
						proc.element(processor);
					}

				}

			}

			result.element(OperationChecker.KEY_OPERATION_CHECKERS, proc);
		}

		context.element(OperationChecker.class.getSimpleName(), result);
	}

	private void createPlayerFinishFilter(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject();
		createContext.element(PlayerFinishChecker.KEY_LIMIT_MULTI_HU, true);
		context.element(PlayerFinishChecker.class.getSimpleName(), createContext);
	}

	/**
	 * 创建杠分数计算器
	 */
	private void createGangScoreProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject();

		{
			// 分数来源过滤器
			JSONArray arrayContext = CreateContextHelper.createArray();
			arrayContext.add(CreateContextHelper.createObject(ScoreSourceFilter_LastOperator.class));
			createContext.element(GangScoreProcessor.KEY_SOURCE_PLAYER_FILTERS, arrayContext);
		}

		{
			// 分数收分目标过滤器
			JSONArray arrayContext = CreateContextHelper.createArray();
			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Common_NeiMeng_WuHai.class));
			createContext.element(GangScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 明杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_Gang.class, ScoreCalculateType.TOTAL_ADD, 1));

			// 补杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_BuGang.class, ScoreCalculateType.TOTAL_ADD, 1));

			// 暗杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_AnGang.class, ScoreCalculateType.TOTAL_ADD, 2));

			createContext.element(GangScoreProcessor.KEY_POINT_CACULATORS, arrayContext);
		}

		context.element(GangScoreProcessor.class.getSimpleName(), createContext);
	}

	private JSONObject createCalculate(List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(CalculateProcesser.class);

		// 牌局相关分数
		{
			JSONObject context = CreateContextHelper.createObject();

			// 牌局分数添加
			{
				JSONArray arrayContext = CreateContextHelper.createArray();

				// 财神（花牌）
				arrayContext.add(CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_Hua.class, ScoreCalculateType.TOTAL_ADD, 1));

				// 赔杠
				arrayContext.add(CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_PeiGang_WuHai.class, ScoreCalculateType.TOTAL_ADD, 0));

				context.element(BattleScoreProcessor.KEY_POINT_CALCULATORS, arrayContext);
			}

			// 牌局分数过滤
			{
				JSONArray arrayContext = CreateContextHelper.createArray();

				// 流局不算杠的分数
				arrayContext.add(CreateContextHelper.createObject(BattleScoreFilter_RemoveGangWhenHuangZhuang_WuHai.class));

				context.element(BattleScoreProcessor.KEY_SCORE_FILTERS, arrayContext);
			}

			createContext.element(CalculateProcesser.KEY_BATTLE_SCORE_PROCESSOR, context);
		}

		// 房间分数计算器
		{
			JSONObject context = CreateContextHelper.createObject(GameScoreCalculator.class);
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 设置房间分数统计
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_ZI_MO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_DIAN_PAO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_AN_GANG, true, false));
			if (rules.contains(Rules_NeiMeng.CAI_SHEN))
				arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_EX_CARD, true, false));

			context.element(GameScoreCalculator.KEY_GAME_SCORE_FILTER, arrayContext);
			createContext.element(CalculateProcesser.KEY_GAME_SCORE_CALCULATOR, context);
		}

		return createContext;
	}

}
