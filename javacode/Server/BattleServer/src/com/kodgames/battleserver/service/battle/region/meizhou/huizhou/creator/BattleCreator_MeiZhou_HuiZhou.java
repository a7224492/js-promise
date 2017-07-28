package com.kodgames.battleserver.service.battle.region.meizhou.huizhou.creator;

import java.util.List;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.IBattleCreator;
import com.kodgames.battleserver.service.battle.core.finish.PlayerFinishChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_13YaoHu;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_PingHu;
import com.kodgames.battleserver.service.battle.core.operation.AfterOperationProcessor;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker;
import com.kodgames.battleserver.service.battle.core.operation.OperationCheckerBase;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_AnGang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_BuGang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Gang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Hu;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Pass;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Peng;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_PassBuGang;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_PassHu;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_PassPeng;
import com.kodgames.battleserver.service.battle.core.playcard.PlayCardProcessor;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreCalculator;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.battle.filter.BattleScoreFilter_RemoveGangWhenHuangZhuang;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreSourceFilter_LastOperator;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter_Common;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreFilter;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_AnGang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_BuGang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_Gang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_PinHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_ShiSanYao;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_HunYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_PengPengHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QiangGangHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QingYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_TianHu;
import com.kodgames.battleserver.service.battle.processer.BattleProcesser;
import com.kodgames.battleserver.service.battle.processer.CalculateProcesser;
import com.kodgames.battleserver.service.battle.processer.DealCardProcesser;
import com.kodgames.battleserver.service.battle.processer.InitCardPoolProcesser;
import com.kodgames.battleserver.service.battle.processer.ZhuangInfoProcesser;
import com.kodgames.battleserver.service.battle.region.meizhou.Rules_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.finish.BattleFinishChecker_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.operation.AfterOperationProcessor_GenZhuang_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.battle.BattleScoreCalculator_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.battle.filter.BattleScoreFilter_GenZhuang_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.game.GameScoreCalculator_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.zhuang.ZhuangCalculator_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.hu.HuChecker_Hu7Hua;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.hu.HuChecker_Hu8Hua;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.operation.OperationAutoFilter_Hu8Hua;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.operation.filter.OperationResultFilter_Hu_HuiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.processor.Processor_JiangMa_HuiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.battle.BattleScoreChecker_Hua_HuiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.filter.ScoreTargetFilter_Hu_HuiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.hu.HuMainScoreChecker_Hu7Hua;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.hu.HuMainScoreChecker_Hu8Hua;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.hu.HuSubScoreChecker_DiHu_HuiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.hu.HuSubScoreChecker_Hu7Hua;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.hu.HuSubScoreChecker_Hu8Hua;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.hu.HuSubScoreChecker_QingPeng_HuiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.hu.HuSubScoreChecker_ZaDui;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BattleCreator_MeiZhou_HuiZhou implements IBattleCreator
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
		return Rules_MeiZhou.checkHuiZhouRules(rules);
	}

	private void createZhuangCalculator(JSONObject context, List<Integer> rules)
	{
		// 创建庄家计算器
		context.element(BattleHelper.KEY_ZHUANG_CALCULATOR, CreateContextHelper.createObject(ZhuangCalculator_MeiZhou.class));
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
				supportTypeContext.add(MahjongConstant.CardType.TONG.toString());
				supportTypeContext.add(MahjongConstant.CardType.ZI.toString());
				supportTypeContext.add(MahjongConstant.CardType.HUA.toString());

				subContext.element(InitCardPoolProcesser.KEY_SUPPORT_CARD_TYPES, supportTypeContext);
			}

			result.element(subContext);
		}

		// 发牌
		result.element(CreateContextHelper.createObject(DealCardProcesser.class));

		// 打牌
		result.element(createBattleProcessor(rules));

		if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_2) || rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_4) || rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_6)
			|| rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_8))
		{
			JSONObject subContext = CreateContextHelper.createObject(Processor_JiangMa_HuiZhou.class);

			// 马跟分
			subContext.element(Processor_JiangMa_HuiZhou.KEY_MA_GEN_FEN, rules.contains(Rules_MeiZhou.GAME_PLAY_MA_GEN_FEN));

			// 马跟杠
			subContext.element(Processor_JiangMa_HuiZhou.KEY_MA_GEN_GANG, rules.contains(Rules_MeiZhou.GAME_PLAY_MA_GEN_GANG));

			result.element(subContext);
		}

		// 分数计算
		result.element(createCalculate(rules));

		context.element(MahjongConstant.JSON_PROC, result);
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

				// 花牌附加分
				arrayContext.add(CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_Hua_HuiZhou.class, ScoreCalculateType.TOTAL_ADD, 1));

				context.element(BattleScoreProcessor.KEY_POINT_CALCULATORS, arrayContext);
			}

			// 牌局分数过滤
			{
				JSONArray arrayContext = CreateContextHelper.createArray();

				// 流局不算杠的分数
				arrayContext.add(CreateContextHelper.createObject(BattleScoreFilter_RemoveGangWhenHuangZhuang.class));

				// 流局不计算跟庄分数
				arrayContext.add(CreateContextHelper.createObject(BattleScoreFilter_GenZhuang_MeiZhou.class));

				context.element(BattleScoreProcessor.KEY_SCORE_FILTERS, arrayContext);
			}

			createContext.element(CalculateProcesser.KEY_BATTLE_SCORE_PROCESSOR, context);
		}

		// 房间分数计算器
		{
			JSONObject context = CreateContextHelper.createObject(GameScoreCalculator_MeiZhou.class);
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 设置房间分数统计
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_ZI_MO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_AN_GANG, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_BU_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_FOLLOW_BANKER, false, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE, true, true));

			context.element(GameScoreCalculator_MeiZhou.KEY_GAME_SCORE_FILTERS_MEIZHOU, arrayContext);
			createContext.element(CalculateProcesser.KEY_GAME_SCORE_CALCULATOR, context);

		}

		return createContext;
	}

	/**
	 * 创建战斗相关处理器，比如前置操作处理和后置操作处理
	 * 
	 * @param rules
	 * @return
	 */
	private JSONObject createBattleProcessor(List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleProcesser.class);

		createOperationChecker(result, rules);
		createAfterOperationProcessor(result, rules);
		createPlayerCardProcessor(result, rules);
		createPlayerFinishFilter(result, rules);
		createGangScoreProcessor(result, rules);

		return result;
	}

	private void createPlayerCardProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject();
		context.element(PlayCardProcessor.class.getSimpleName(), result);
	}

	private void createOperationChecker(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject();

		{
			JSONArray proc = CreateContextHelper.createArray();

			// 碰检测
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Peng.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 漏碰了别人打出的牌，那么在自己出牌前不能碰其他玩家打出的同一张牌
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_PassPeng.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 吃杠（明杠）
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Gang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 暗杠
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_AnGang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 补杠（明杠）
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_BuGang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 明杠必须是在抓到的这张牌触发，如果这次没有杠，以后不能杠了
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_PassBuGang.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);

			}

			// 胡牌
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Hu.class);

				{
					// 前置操作必须是补杠，出牌，摸牌
					JSONArray preFilters = CreateContextHelper.createArray();

					// 抢杠胡
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_BU_GANG_A_CARD));

					// 自摸胡
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_DEAL));

					// 点炮胡
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_PLAY_A_CARD));

					processor.element(OperationCheckerBase.KEY_PRE_FILTER, preFilters);
				}

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 添加鸡胡不能吃胡
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_Hu_HuiZhou.class));

					// 漏胡, 自摸不算漏胡
					{
						JSONObject filter = CreateContextHelper.createObject(OperationResultFilter_PassHu.class);
						filter.element(OperationResultFilter_PassHu.KEY_CHECK_PASS_ZI_MO_HU, false);
						filter.element(OperationResultFilter_PassHu.KEY_CHECK_PASS_DIAN_HU, true);
						afterFilters.element(filter);
					}

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// pass检测
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Pass.class);
				proc.element(processor);
			}

			result.element(OperationChecker.KEY_OPERATION_CHECKERS, proc);
		}

		// 8花自动打牌逻辑
		{
			JSONArray proc = CreateContextHelper.createArray();
			JSONObject processor = CreateContextHelper.createObject(OperationAutoFilter_Hu8Hua.class);
			proc.element(processor);
			result.element(OperationChecker.KEY_OPERATION_CHECKER_FILTERS, proc);
		}

		context.element(OperationChecker.class.getSimpleName(), result);
	}

	/**
	 * 配置附加操作处理器
	 */
	private void createAfterOperationProcessor(JSONObject context, List<Integer> rules)
	{
		// 一个玩家只能胡一次
		JSONObject result = CreateContextHelper.createObject();

		// 添加跟庄
		{
			JSONArray afterFilters = CreateContextHelper.createArray();
			{
				JSONObject afterProcess = CreateContextHelper.createObject(AfterOperationProcessor_GenZhuang_MeiZhou.class);

				afterProcess.element(AfterOperationProcessor_GenZhuang_MeiZhou.KEY_CALCULATE_TYPE, ScoreCalculateType.TOTAL_ADD);
				afterProcess.element(AfterOperationProcessor_GenZhuang_MeiZhou.KEY_SCORE_VALUE, -1);
				afterProcess.element(AfterOperationProcessor_GenZhuang_MeiZhou.KEY_PLAY_TYPE, PlayType.OPERATE_PLAY_A_CARD);

				afterFilters.add(afterProcess);
			}

			result.element(AfterOperationProcessor.KEY_PROCESSORS, afterFilters);
		}

		context.element(AfterOperationProcessor.class.getSimpleName(), result);
	}

	/**
	 * 配置玩家结束条件
	 */
	private void createPlayerFinishFilter(JSONObject context, List<Integer> rules)
	{
		// 一个玩家只能胡一次
		JSONObject result = CreateContextHelper.createObject();
		result.element(PlayerFinishChecker.KEY_LIMIT_MULTI_HU, true);
		context.element(PlayerFinishChecker.class.getSimpleName(), result);
	}

	/**
	 * 创建战斗分数计算器
	 */
	private void createBattleScoreCalculator(JSONObject createContext, List<Integer> rules)
	{
		JSONObject context = CreateContextHelper.createObject(BattleScoreCalculator_MeiZhou.class);

		// 设置为0就是不封顶
		context.element(BattleScoreCalculator.KEY_MAX_VALUE, 0);

		JSONArray huEventOrders = CreateContextHelper.createArray();

		{
			// 平胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_PING_HU));

			// 8张花牌胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_MAIN_8_HUA));

			// 7张花牌胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_MAIN_7_HUA));

			// 十三幺
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_SHI_SAN_YAO));

			// 7张花牌胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_SUB_7_HUA));

			// 8张花牌胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_SUB_8_HUA));

			// 补花分
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.DISPLAY_EX_CARD));

			// 碰碰胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_PENG_PENG_HU));

			// 混一色
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_HUN_YI_SE));

			// 清一色
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_QING_YI_SE));

			// 杂对
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_ZA_DUI));

			// 清碰
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_QING_PENG_HUI_ZHOU));

			// 天胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_TIAN_HU));

			// 地胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_DI_HU));

			// 抢杠胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_QIANG_GANG_HU));

			// 奖马
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.DISPLAY_BETTING_HOUSE));

		}

		context.element(BattleScoreCalculator_MeiZhou.KEY_HU_EVENT_ORDER, huEventOrders);

		createContext.element(BattleHelper.KEY_BATTLE_SCORE_CALCULATOR, context);
	}

	/**
	 * 设置牌局结束条件
	 */
	private void createBattleFinishChecker(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleFinishChecker_MeiZhou.class);

		// 如果选择了奖马，要留够奖马的牌，提前流局
		if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_2))
		{
			result.element(BattleFinishChecker_MeiZhou.KEY_STAY_CARD_COUNT, 2);
		}
		else if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_4))
		{
			result.element(BattleFinishChecker_MeiZhou.KEY_STAY_CARD_COUNT, 4);
		}
		else if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_6))
		{
			result.element(BattleFinishChecker_MeiZhou.KEY_STAY_CARD_COUNT, 6);
		}
		else if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_8))
		{
			result.element(BattleFinishChecker_MeiZhou.KEY_STAY_CARD_COUNT, 8);
		}
		else
		{
			result.element(BattleFinishChecker_MeiZhou.KEY_STAY_CARD_COUNT, 0);
		}

		// 只要胡一次就可以结束牌局
		result.element(BattleFinishChecker_MeiZhou.KEY_HU_COUNT, 1);

		context.element(BattleHelper.KEY_BATTLE_FINISH_CHECKER, result);
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
			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Common.class));
			createContext.element(GangScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 明杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_Gang.class, ScoreCalculateType.TOTAL_ADD, 1));

			// 暗杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_AnGang.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 补杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_BuGang.class, ScoreCalculateType.TOTAL_ADD, 1));

			createContext.element(GangScoreProcessor.KEY_POINT_CACULATORS, arrayContext);
		}

		context.element(GangScoreProcessor.class.getSimpleName(), createContext);
	}

	/**
	 * 创建胡Checker
	 */
	private void createHuCheckerProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(HuChecker.class);

		/**
		 * 胡牌牌形检查器
		 */
		{
			JSONArray checkers = CreateContextHelper.createArray();

			// 平胡（鸡胡）
			checkers.element(CreateContextHelper.createObject(HuChecker_PingHu.class));

			// 十三幺
			checkers.element(CreateContextHelper.createObject(HuChecker_13YaoHu.class));

			// 7张花牌胡牌
			checkers.element(CreateContextHelper.createObject(HuChecker_Hu7Hua.class));

			// 8张花牌胡牌
			checkers.element(CreateContextHelper.createObject(HuChecker_Hu8Hua.class));

			createContext.element(HuChecker.KEY_CHECKERS, checkers);
		}

		context.element(BattleHelper.KEY_HU_CHECK_PROCESSOR, createContext);
	}

	/**
	 * 胡牌得分计算器
	 */
	private void createHuCalculateProcessor(JSONObject context, List<Integer> rules)
	{

		JSONObject createContext = CreateContextHelper.createObject(HuScoreProcessor.class);
		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 普通规则
			arrayContext.add(CreateContextHelper.createObject(ScoreSourceFilter_LastOperator.class));

			createContext.element(HuScoreProcessor.KEY_SOURCE_PLAYER_FILTERS, arrayContext);
		}

		{
			// 分数收分目标过滤器
			JSONArray arrayContext = CreateContextHelper.createArray();

			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Hu_HuiZhou.class));
			createContext.element(HuScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		// 支持的分数
		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 基础牌型：
			// 1. 鸡胡：1分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_PinHu.class, ScoreCalculateType.TOTAL_ADD, 1));

			// 2、 十三幺：13分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_ShiSanYao.class,
				ScoreCalculateType.TOTAL_ADD,
				13,
				new int[] {PlayType.HU_HUN_YAO_JIU, PlayType.HU_SUB_7_HUA, PlayType.HU_SUB_8_HUA}));

			// 3、 7张花牌胡牌（鸡胡）：1分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_Hu7Hua.class,
				ScoreCalculateType.TOTAL_ADD,
				1,
				new int[] {PlayType.HU_SUB_7_HUA, PlayType.HU_PENG_PENG_HU, PlayType.HU_QING_YI_SE, PlayType.HU_HUN_YI_SE, PlayType.HU_ZA_DUI, PlayType.HU_QING_PENG_HUI_ZHOU,
					PlayType.HU_QIANG_GANG_HU, PlayType.HU_TIAN_HU, PlayType.HU_DI_HU}));

			// 4、 8张胡牌胡牌（鸡胡）：1分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_Hu8Hua.class,
				ScoreCalculateType.TOTAL_ADD,
				1,
				new int[] {PlayType.HU_SUB_8_HUA, PlayType.HU_PENG_PENG_HU, PlayType.HU_QING_YI_SE, PlayType.HU_HUN_YI_SE, PlayType.HU_ZA_DUI, PlayType.HU_QING_PENG_HUI_ZHOU,
					PlayType.HU_QIANG_GANG_HU, PlayType.HU_TIAN_HU, PlayType.HU_DI_HU}));

			// 7张花牌作为附加牌型（鸡胡）：1分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_Hu7Hua.class, ScoreCalculateType.TOTAL_ADD, 1, new int[] {PlayType.HU_PING_HU}));

			// 8张花牌作为附加牌型（鸡胡）：1分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_Hu8Hua.class, ScoreCalculateType.TOTAL_ADD, 1, new int[] {PlayType.HU_PING_HU}));

			// 碰碰胡 3分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_PengPengHu.class,
				ScoreCalculateType.TOTAL_ADD,
				3,
				new int[] {PlayType.HU_PING_HU, PlayType.HU_SUB_7_HUA, PlayType.HU_SUB_8_HUA}));

			// 清一色 5分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYiSe.class,
				ScoreCalculateType.TOTAL_ADD,
				5,
				new int[] {PlayType.HU_PING_HU, PlayType.HU_SUB_7_HUA, PlayType.HU_SUB_8_HUA}));

			// 混一色 2分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYiSe.class,
				ScoreCalculateType.TOTAL_ADD,
				2,
				new int[] {PlayType.HU_PING_HU, PlayType.HU_SUB_7_HUA, PlayType.HU_SUB_8_HUA}));

			// 杂对5分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZaDui.class,
				ScoreCalculateType.TOTAL_ADD,
				5,
				new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU, PlayType.HU_HUN_YI_SE, PlayType.HU_SUB_7_HUA, PlayType.HU_SUB_8_HUA}));

			// 清碰 8分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingPeng_HuiZhou.class,
				ScoreCalculateType.TOTAL_ADD,
				8,
				new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU, PlayType.HU_QING_YI_SE, PlayType.HU_SUB_7_HUA, PlayType.HU_SUB_8_HUA}));

			// 特殊加分：
			// 抢杠胡 3倍（一个承包）
			arrayContext
				.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QiangGangHu.class, ScoreCalculateType.TOTAL_MULTI, 3));

			// 特殊牌型：
			// 地胡 5分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DiHu_HuiZhou.class,
				ScoreCalculateType.TOTAL_ADD,
				5,
				new int[] {PlayType.HU_PING_HU, PlayType.HU_SUB_7_HUA, PlayType.HU_SUB_8_HUA}));

			// 天胡 10分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_TianHu.class,
				ScoreCalculateType.TOTAL_ADD,
				10,
				new int[] {PlayType.HU_PING_HU, PlayType.HU_SUB_7_HUA, PlayType.HU_SUB_8_HUA}));

			createContext.element(HuScoreProcessor.KEY_SCORE_CHECKERS, arrayContext);
		}

		context.element(BattleHelper.KEY_HU_SCORE_PROCESSOR, createContext);
	}

}
