package com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.creator;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.IBattleCreator;
import com.kodgames.battleserver.service.battle.core.finish.BattleFinishChecker_Common;
import com.kodgames.battleserver.service.battle.core.finish.PlayerFinishChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_13YaoHu;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_4MasterCard;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_7DuiHu;
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
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_CardPoolHasEnoughtStayCard;
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
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreCalculator;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreFilter;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_AnGang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_BuGang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_Gang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_4MasterCard;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_PinHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_QiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_ShiSanYao;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_PengPengHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QiangGangHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_ZiYiSe;
import com.kodgames.battleserver.service.battle.processer.BattleProcesser;
import com.kodgames.battleserver.service.battle.processer.CalculateProcesser;
import com.kodgames.battleserver.service.battle.processer.DealCardProcesser;
import com.kodgames.battleserver.service.battle.processer.InitCardPoolProcesser;
import com.kodgames.battleserver.service.battle.processer.ZhuangInfoProcesser;
import com.kodgames.battleserver.service.battle.region.meizhou.Rules_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.operation.AfterOperationProcessor_PlayCard_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.operation.filter.OperationResultFilter_ZiMo_TuiDaoHu;
import com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.processor.Processor_BettingHorse_TuiDaoHu_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.processor.Processor_SetMasterMeiZhouTuiDaoHu;
import com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.score.battle.BattleScoreCalculator_MeiZhou_TuiDaoHu;
import com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.score.battle.BattleScoreChecker_NoMaster;
import com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.score.filter.ScoreTargetFilter_JiangMa_QiangGangHu;
import com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.score.hu.HuSubScoreChecker_QingYiSe_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.score.hu.HuSubScoreChecker_YaoJiu;
import com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.zhuang.ZhuangCalculator_GD;

public class BattleCreator_MeiZhou_TuiDaoHu implements IBattleCreator
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
		return Rules_MeiZhou.checkTuiDaoHuRules(rules);
	}

	private void createZhuangCalculator(JSONObject context, List<Integer> rules)
	{
		// 创建庄家计算器
		context.element(BattleHelper.KEY_ZHUANG_CALCULATOR, CreateContextHelper.createObject(ZhuangCalculator_GD.class));
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
				// 带风/不带风
				if (rules.contains(Rules_MeiZhou.GAME_PLAY_NO_ZI) == false)
					supportTypeContext.add(MahjongConstant.CardType.ZI.toString());

				subContext.element(InitCardPoolProcesser.KEY_SUPPORT_CARD_TYPES, supportTypeContext);
			}

			result.element(subContext);
		}

		// 发牌
		result.element(CreateContextHelper.createObject(DealCardProcesser.class));

		// 鬼牌
		if (rules.contains(Rules_MeiZhou.MASTER_CARD_HONG_ZHONG) || rules.contains(Rules_MeiZhou.MASTER_CARD_BAI_BAN) || rules.contains(Rules_MeiZhou.MASTER_CARD_GENERATE))
		{
			JSONObject subContext = CreateContextHelper.createObject(Processor_SetMasterMeiZhouTuiDaoHu.class);

			// 无鬼牌/红中做鬼/白板做鬼/翻鬼（双鬼）
			if (rules.contains(Rules_MeiZhou.MASTER_CARD_HONG_ZHONG))
				subContext.element(Processor_SetMasterMeiZhouTuiDaoHu.KEY_DEFAULT_MASTER_CARD, MahjongConstant.CardType.ZI.Value() + 4);
			else if (rules.contains(Rules_MeiZhou.MASTER_CARD_BAI_BAN))
				subContext.element(Processor_SetMasterMeiZhouTuiDaoHu.KEY_DEFAULT_MASTER_CARD, MahjongConstant.CardType.ZI.Value() + 6);
			else if (rules.contains(Rules_MeiZhou.MASTER_CARD_TOW_MASTER))
				subContext.element(Processor_SetMasterMeiZhouTuiDaoHu.KEY_GENERATE_MASTER_CARD_COUNT, 2);
			else
				subContext.element(Processor_SetMasterMeiZhouTuiDaoHu.KEY_GENERATE_MASTER_CARD_COUNT, 1);

			result.element(subContext);
		}

		// 打牌
		result.element(createBattleProcessor(rules));

		// 奖马
		if (rules.contains(Rules_MeiZhou.BETTING_HOUSE_2) || rules.contains(Rules_MeiZhou.BETTING_HOUSE_5) || rules.contains(Rules_MeiZhou.BETTING_HOUSE_8))
		{
			JSONObject subContext = CreateContextHelper.createObject(Processor_BettingHorse_TuiDaoHu_MeiZhou.class);

			// 马跟牌
			subContext.element(Processor_BettingHorse_TuiDaoHu_MeiZhou.KEY_MA_GEN_PAI, rules.contains(Rules_MeiZhou.BETTING_HOUSE_MA_GEN_PAI));
			subContext.element(Processor_BettingHorse_TuiDaoHu_MeiZhou.KEY_QIANG_GANG_QUAN_BAO, rules.contains(Rules_MeiZhou.QIANG_GANG_QUAN_BAO));
			

			// 抢杠胡全包配置过滤器
			{
				JSONArray targetFilter = CreateContextHelper.createArray();
				if (rules.contains(Rules_MeiZhou.QIANG_GANG_QUAN_BAO))
					targetFilter.add(CreateContextHelper.createObject(ScoreTargetFilter_JiangMa_QiangGangHu.class));
				subContext.element(Processor_BettingHorse_TuiDaoHu_MeiZhou.KEY_HORSE_SCORE_TARGET_FILTERS, targetFilter);
			}

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

				// 无鬼加倍
				if (rules.contains(Rules_MeiZhou.MASTER_CARD_NONE_DOUBLE))
					arrayContext.add(CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_NoMaster.class, ScoreCalculateType.TOTAL_MULTI, 2));

				context.element(BattleScoreProcessor.KEY_POINT_CALCULATORS, arrayContext);
			}

			// 牌局分数过滤
			{
				JSONArray arrayContext = CreateContextHelper.createArray();

				// 流局不算杠的分数
				arrayContext.add(CreateContextHelper.createObject(BattleScoreFilter_RemoveGangWhenHuangZhuang.class));

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
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_AN_GANG, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_BU_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE, true, true));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_FOLLOW_BANKER, false, false));

			context.element(GameScoreCalculator.KEY_GAME_SCORE_FILTER, arrayContext);
			createContext.element(CalculateProcesser.KEY_GAME_SCORE_CALCULATOR, context);
		}

		return createContext;
	}

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

					// 2、 如果漏碰了别人打出的牌，那么在自己出牌前不能碰其他玩家打出的同一张牌
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_PassPeng.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 明杠检测
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Gang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 抓到牌库最后一张牌时，不让明杠 暗杠、补杠
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_CardPoolHasEnoughtStayCard.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 暗杠
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_AnGang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 抓到牌库最后一张牌时，不让明杠 暗杠、补杠
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_CardPoolHasEnoughtStayCard.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 补杠
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_BuGang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 抓到牌库最后一张牌时，不让明杠 暗杠、补杠
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_CardPoolHasEnoughtStayCard.class));

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
					JSONArray preFilters = CreateContextHelper.createArray();

					// 前置操作必须是补杠，摸牌
					if (rules.contains(Rules_MeiZhou.CAN_QIANG_GANG_HU))
						preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_BU_GANG_A_CARD));

					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_DEAL));

					processor.element(OperationCheckerBase.KEY_PRE_FILTER, preFilters);
				}

				// 推倒胡只能自摸和抢杠胡
				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 配置只能抢杠胡的自摸
					afterFilters.add(CreateContextHelper.createObject(OperationResultFilter_ZiMo_TuiDaoHu.class));

					// 漏胡, 自摸不算漏胡
					{
						JSONObject filter = CreateContextHelper.createObject(OperationResultFilter_PassHu.class);
						filter.element(OperationResultFilter_PassHu.KEY_CHECK_PASS_ZI_MO_HU, true);
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
			if (rules.contains(Rules_MeiZhou.SCORE_GEN_ZHUANG))
			{
				JSONArray afterFilters = CreateContextHelper.createArray();
				{
					JSONObject afterProcess = CreateContextHelper.createObject(AfterOperationProcessor_PlayCard_MeiZhou.class);

					afterProcess.element(AfterOperationProcessor_PlayCard_MeiZhou.KEY_CALCULATE_TYPE, ScoreCalculateType.TOTAL_ADD);
					afterProcess.element(AfterOperationProcessor_PlayCard_MeiZhou.KEY_SCORE_VALUE, -1);
					afterProcess.element(AfterOperationProcessor_PlayCard_MeiZhou.KEY_PLAY_TYPE, PlayType.OPERATE_PLAY_A_CARD);
					afterProcess.element(AfterOperationProcessor_PlayCard_MeiZhou.KEY_LIU_JU, false);

					afterFilters.add(afterProcess);
				}

				result.element(AfterOperationProcessor.KEY_PROCESSORS, afterFilters);
			}
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
		JSONObject context = CreateContextHelper.createObject(BattleScoreCalculator_MeiZhou_TuiDaoHu.class);
		context.element(BattleScoreCalculator.KEY_MAX_VALUE, 0);
		createContext.element(BattleHelper.KEY_BATTLE_SCORE_CALCULATOR, context);
	}

	/**
	 * 设置牌局结束条件
	 */
	private void createBattleFinishChecker(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleFinishChecker_Common.class);

		// 如果选择了奖马，要留够奖马的牌，提前流局
		if (rules.contains(Rules_MeiZhou.BETTING_HOUSE_2))
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 2);
		else if (rules.contains(Rules_MeiZhou.BETTING_HOUSE_5))
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 5);
		else if (rules.contains(Rules_MeiZhou.BETTING_HOUSE_8))
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 8);
		else
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 0);

		// 只要胡一次就可以结束牌局
		result.element(BattleFinishChecker_Common.KEY_HU_COUNT, 1);

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
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_Gang.class, ScoreCalculateType.TOTAL_ADD, 3));

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
		 * 胡牌行为过滤器
		 */
		{
		}

		/**
		 * 胡牌牌形检查器
		 */
		{
			JSONArray checkers = CreateContextHelper.createArray();

			// 四鬼胡牌
			if (rules.contains(Rules_MeiZhou.MASTER_CARD_FORE_HU))
				checkers.element(CreateContextHelper.createObject(HuChecker_4MasterCard.class));
			checkers.element(CreateContextHelper.createObject(HuChecker_PingHu.class));
			checkers.element(CreateContextHelper.createObject(HuChecker_7DuiHu.class));
			checkers.element(CreateContextHelper.createObject(HuChecker_13YaoHu.class));
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

		// 牌局分数收取目标过滤器
		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Common.class));

			createContext.element(HuScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		// 支持的分数
		{

			JSONArray arrayContext = CreateContextHelper.createArray();

			// 平胡
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_PinHu.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 加倍牌型：
			// 大对子双倍
			if (rules.contains(Rules_MeiZhou.PENG_PENG_HU_DOUBLE))
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_PengPengHu.class, ScoreCalculateType.TOTAL_ADD, 4, new int[] {PlayType.HU_PING_HU}));
			else
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_PengPengHu.class, ScoreCalculateType.TOTAL_ADD, 2, new int[] {PlayType.HU_PING_HU}));

			// 七对双倍
			if (rules.contains(Rules_MeiZhou.QI_DUI_DOUBLE))
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_QiDui.class, ScoreCalculateType.TOTAL_ADD, 4, new int[] {PlayType.HU_PING_HU}));
			// 七对四倍
			else if (rules.contains(Rules_MeiZhou.QI_DUI_FOUR_TIMES))
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_QiDui.class, ScoreCalculateType.TOTAL_ADD, 8, new int[] {PlayType.HU_PING_HU}));
			else
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_QiDui.class, ScoreCalculateType.TOTAL_ADD, 2, new int[] {PlayType.HU_PING_HU}));

			// 清一色4倍
			if (rules.contains(Rules_MeiZhou.QING_YI_SE_FOUR_TIMES))
				arrayContext
					.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYiSe_MeiZhou.class, ScoreCalculateType.TOTAL_ADD, 8, new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU}));
			else
				arrayContext
					.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYiSe_MeiZhou.class, ScoreCalculateType.TOTAL_ADD, 2, new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU}));

			// 幺九6倍
			if (rules.contains(Rules_MeiZhou.YAO_JIU_SIX_TIMES))
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_YaoJiu.class,
					ScoreCalculateType.TOTAL_ADD,
					12,
					new int[] {PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI, PlayType.HU_QUAN_DAI_YAO, PlayType.HU_PING_HU}));
			else
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_YaoJiu.class,
					ScoreCalculateType.TOTAL_ADD,
					2,
					new int[] {PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI, PlayType.HU_QUAN_DAI_YAO, PlayType.HU_PING_HU}));

			// 全风8倍
			if (rules.contains(Rules_MeiZhou.QUANG_FENG_EIGHT_TIMES))
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZiYiSe.class,
					ScoreCalculateType.TOTAL_ADD,
					16,
					new int[] {PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI, PlayType.HU_PING_HU, PlayType.HU_YAO_JIU_GDTDH}));
			else
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZiYiSe.class,
					ScoreCalculateType.TOTAL_ADD,
					2,
					new int[] {PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI, PlayType.HU_PING_HU, PlayType.HU_YAO_JIU_GDTDH}));

			// 十三幺8倍
			if (rules.contains(Rules_MeiZhou.SHI_SAN_YAO_EIGHT_TIMES))
				arrayContext
					.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_ShiSanYao.class, ScoreCalculateType.TOTAL_ADD, 16, new int[] {PlayType.HU_PING_HU, PlayType.HU_YAO_JIU_GDTDH}));
			else
				arrayContext
					.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_ShiSanYao.class, ScoreCalculateType.TOTAL_ADD, 2, new int[] {PlayType.HU_PING_HU, PlayType.HU_YAO_JIU_GDTDH}));

			// 四鬼胡牌
			if (rules.contains(Rules_MeiZhou.MASTER_CARD_FORE_HU))
			{
				if (rules.contains(Rules_MeiZhou.MASTER_CARD_DOUBLE_SCORE))
					arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_4MasterCard.class,
						ScoreCalculateType.TOTAL_ADD,
						4,
						new int[] {PlayType.HU_PING_HU, PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_YAO_JIU_GDTDH, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI,
							PlayType.HU_SHI_SAN_YAO}));
				else
					arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_4MasterCard.class,
						ScoreCalculateType.TOTAL_ADD,
						2,
						new int[] {PlayType.HU_PING_HU, PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_YAO_JIU_GDTDH, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI,
							PlayType.HU_SHI_SAN_YAO}));
			}

			// 抢杠胡按自摸，收一家
			if (rules.contains(Rules_MeiZhou.CAN_QIANG_GANG_HU))
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QiangGangHu.class, ScoreCalculateType.TOTAL_MULTI, 3));

			createContext.element(HuScoreProcessor.KEY_SCORE_CHECKERS, arrayContext);
		}

		context.element(BattleHelper.KEY_HU_SCORE_PROCESSOR, createContext);
	}
}
