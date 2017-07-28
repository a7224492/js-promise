package com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.creator;

import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.IBattleCreator;
import com.kodgames.battleserver.service.battle.core.finish.PlayerFinishChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_13YaoHu;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_7DuiHu;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_PingHu;
import com.kodgames.battleserver.service.battle.core.operation.AfterOperationProcessor;
import com.kodgames.battleserver.service.battle.core.operation.AfterOperationProcessorBase;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker;
import com.kodgames.battleserver.service.battle.core.operation.OperationCheckerBase;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_AnGang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_BuGang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Chi;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Gang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Pass;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Peng;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_Chi;
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
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_PinHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_QiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_ShiSanYao;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_MenQianQing;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QingYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QueYiMen;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_YiTiaoLong;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_ZiMo;
import com.kodgames.battleserver.service.battle.processer.CalculateProcesser;
import com.kodgames.battleserver.service.battle.processer.DealCardProcesser;
import com.kodgames.battleserver.service.battle.processer.InitCardPoolProcesser;
import com.kodgames.battleserver.service.battle.processer.ZhuangInfoProcesser;
import com.kodgames.battleserver.service.battle.region.neimeng.baotou.score.hu.HuSubScoreChecker_KanWuKui;
import com.kodgames.battleserver.service.battle.region.neimeng.baotou.score.hu.HuSubScoreChecker_SiGuiYi;
import com.kodgames.battleserver.service.battle.region.neimeng.common.Rules_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.common.finish.BattleFinishChecker_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.common.operation.AfterOperationProcessor_EnterTing;
import com.kodgames.battleserver.service.battle.region.neimeng.common.operation.AfterOperationProcessor_GangEnterTing;
import com.kodgames.battleserver.service.battle.region.neimeng.common.operation.OperationChecker_Hu_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.common.operation.filter.OperationResultFilter_ChiTing;
import com.kodgames.battleserver.service.battle.region.neimeng.common.operation.filter.OperationResultFilter_Gang;
import com.kodgames.battleserver.service.battle.region.neimeng.common.operation.filter.OperationResultFilter_GangTing;
import com.kodgames.battleserver.service.battle.region.neimeng.common.operation.filter.OperationResultFilter_PassHu_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.common.operation.filter.OperationResultFilter_Peng;
import com.kodgames.battleserver.service.battle.region.neimeng.common.operation.filter.OperationResultFilter_PengTing;
import com.kodgames.battleserver.service.battle.region.neimeng.common.playcard.filter.PlayCardFilter_EnterTing;
import com.kodgames.battleserver.service.battle.region.neimeng.common.playcard.filter.PlayCardFilter_GangEnterTing;
import com.kodgames.battleserver.service.battle.region.neimeng.common.processor.Processor_DunLaPao;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle.BattleScoreCalculator_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle.BattleScoreChecker_Dun;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle.BattleScoreChecker_Hua;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle.BattleScoreChecker_La;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle.BattleScoreChecker_Pao;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu.HuSubScoreChecker_BianZhang;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu.HuSubScoreChecker_DanDiao;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu.HuSubScoreChecker_GouZhang;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu.HuSubScoreChecker_HaoHuaQiDui;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu.HuSubScoreChecker_KanZhang;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu.HuSubScoreChecker_LianZhuang;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu.HuSubScoreChecker_ZhuangJia;
import com.kodgames.battleserver.service.battle.region.neimeng.common.zhuang.ZhuangCalculator_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.controller.DisplayStepProcessor_AnGang;
import com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.controller.DisplayStepProcessor_DealFirst;
import com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.controller.DisplayStepProcessor_PlayACard;
import com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.operation.OperationResultFilter_TanBaZhang;
import com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.processor.BattleProcessor_WuLanChaBu;
import com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.score.battle.BattleScoreChecker_PeiGang_WuLanChaBu;
import com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.score.filter.ScoreTargetFilter_Gang;
import com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.score.hu.HuSubScoreChecker_MenQingZiMo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 玩法配置器
 * 
 * 内蒙：乌兰察布
 */
public class BattleCreator_NeiMeng_WuLanChaBu implements IBattleCreator
{
	@Override
	public JSONObject create(List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleHelper.class.getSimpleName());

		createControllerManager(result, rules);
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
		return Rules_NeiMeng.checkRules(rules, Rules_NeiMeng.GAME_TYPE_WULANCHABU);
	}

	@Override
	public int getMaxPlayerSize(List<Integer> rules)
	{
		if (rules.contains(Rules_NeiMeng.SAN_REN_JU))
			return MahjongConstant.DEFAULT_ROOM_MEMBER_COUNT - 1;

		return MahjongConstant.DEFAULT_ROOM_MEMBER_COUNT;
	}

	/**
	 * 创建控制管理器
	 */
	private void createControllerManager(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(ControllerManager.class);
		JSONArray stepProcessors = CreateContextHelper.createArray();

		// 添加黑摸玩法的操作控制
		if (rules.contains(Rules_NeiMeng.HEI_MO))
		{
			// 黑摸：发牌/复牌
			{
				JSONObject stepProcessorConfig = CreateContextHelper.createObject(DisplayStepProcessor_DealFirst.class);
				stepProcessorConfig.element(ControllerManager.KEY_DISPLAY_STEP_PLAY_TYPE, PlayType.OPERATE_DEAL_FIRST);
				stepProcessors.element(stepProcessorConfig);
			}

			// 黑摸：打牌
			{
				JSONObject stepProcessorConfig = CreateContextHelper.createObject(DisplayStepProcessor_PlayACard.class);
				stepProcessorConfig.element(ControllerManager.KEY_DISPLAY_STEP_PLAY_TYPE, PlayType.OPERATE_PLAY_A_CARD_HIDE);
				stepProcessors.element(stepProcessorConfig);
			}

			// 黑摸：暗杠
			{
				JSONObject stepProcessorConfig = CreateContextHelper.createObject(DisplayStepProcessor_AnGang.class);
				stepProcessorConfig.element(ControllerManager.KEY_DISPLAY_STEP_PLAY_TYPE, PlayType.OPERATE_AN_GANG);
				stepProcessors.element(stepProcessorConfig);
			}
		}

		createContext.element(ControllerManager.KEY_DISPLAY_STEP_PROCESSORS, stepProcessors);
		context.element(BattleHelper.KEY_CONTROLLER_MANAGER, createContext);
	}

	/**
	 * 创建庄家计时器
	 */
	private void createZhuangCalculator(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(ZhuangCalculator_NeiMeng.class);
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

			// 基础牌型：平和，七对，十三幺
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_PinHu.class, ScoreCalculateType.TOTAL_ADD, 1));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_QiDui.class, ScoreCalculateType.TOTAL_ADD, 5));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_ShiSanYao.class, ScoreCalculateType.TOTAL_ADD, 13));

			// 附加牌型：清一色，一条龙，豪华七对，够张
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYiSe.class, ScoreCalculateType.TOTAL_ADD, 5));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_YiTiaoLong.class, ScoreCalculateType.TOTAL_ADD, 5));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HaoHuaQiDui.class, ScoreCalculateType.TOTAL_ADD, 10, new int[] {PlayType.HU_QI_DUI}));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_GouZhang.class, ScoreCalculateType.TOTAL_ADD, 1));

			// 特殊加分项：单口（边张，坎张，单吊），四归一，砍五魁，缺门，门清，连庄，门请自摸
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_BianZhang.class, ScoreCalculateType.TOTAL_ADD, 1, new int[] {PlayType.HU_KAN_ZHANG, PlayType.HU_DAN_DIAO}));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_KanZhang.class, ScoreCalculateType.TOTAL_ADD, 1, new int[] {PlayType.HU_BIAN_ZHANG, PlayType.HU_DAN_DIAO}));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DanDiao.class, ScoreCalculateType.TOTAL_ADD, 1, new int[] {PlayType.HU_BIAN_ZHANG, PlayType.HU_KAN_ZHANG}));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_SiGuiYi.class, ScoreCalculateType.TOTAL_ADD, 1));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_KanWuKui.class, ScoreCalculateType.TOTAL_ADD, 5, new int[] {PlayType.HU_KAN_ZHANG}));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QueYiMen.class, ScoreCalculateType.TOTAL_ADD, 1));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_MenQianQing.class, ScoreCalculateType.TOTAL_ADD, 1));
			if (rules.contains(Rules_NeiMeng.LIAN_ZHUANG_JIA_FEN))
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_LianZhuang.class, ScoreCalculateType.TOTAL_ADD, 1));
			else
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZhuangJia.class, ScoreCalculateType.TOTAL_ADD, 1));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_MenQingZiMo.class, ScoreCalculateType.TOTAL_ADD, 5, new int[] {PlayType.HU_ZI_MO, PlayType.HU_MEN_QIAN_QING}));
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZiMo.class, ScoreCalculateType.TOTAL_ADD, 1));

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

		// 胡牌牌形检查器
		{
			JSONArray checkers = CreateContextHelper.createArray();
			checkers.element(CreateContextHelper.createObject(HuChecker_PingHu.class));
			checkers.element(CreateContextHelper.createObject(HuChecker_7DuiHu.class));
			checkers.element(CreateContextHelper.createObject(HuChecker_13YaoHu.class));
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

		// 无风玩法需要把牌抓完
		result.element(BattleFinishChecker_NeiMeng.KEY_stayCardCount, rules.contains(Rules_NeiMeng.NO_FENG) ? 0 : 15);

		context.element(BattleHelper.KEY_BATTLE_FINISH_CHECKER, result);
	}

	private void createMainProcessor(JSONObject context, List<Integer> rules)
	{
		JSONArray result = CreateContextHelper.createArray();

		// 确定庄家
		result.element(CreateContextHelper.createObject(ZhuangInfoProcesser.class));

		if (Rules_NeiMeng.getDunLaPaoPoint(rules) > 0)
		{
			// 选择蹲拉跑
			result.element(CreateContextHelper.createObject(Processor_DunLaPao.class));
		}

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

		// 分数计算
		result.element(createCalculate(rules));

		context.element(MahjongConstant.JSON_PROC, result);
	}

	private JSONObject createBattleProcessor(List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleProcessor_WuLanChaBu.class);

		createOperationCheck(result, rules);
		createAfterOperationProcessor(result, rules);
		createPlayerCardProcessor(result, rules);
		createPlayerFinishFilter(result, rules);
		createGangScoreProcessor(result, rules);

		return result;
	}

	private void createOperationCheck(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject();

		{
			JSONArray proc = CreateContextHelper.createArray();

			boolean canChiTing = Rules_NeiMeng.canChiTing(rules);
			boolean canPengTing = Rules_NeiMeng.canPengTing(rules);

			// 吃检测
			if (Rules_NeiMeng.canChi(rules))
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Chi.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 过滤吃牌
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_Chi.class));

					// 如果房间支持吃牌上听，只有导致上听的吃才被允许
					if (canChiTing)
						afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_ChiTing.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 碰检测
			if (Rules_NeiMeng.canPeng(rules))
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Peng.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 过滤碰牌
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_Peng.class));

					// 如果房间支持碰牌上听，只有导致上听的碰才被允许
					if (canPengTing)
						afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_PengTing.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 明杠检测
			if (Rules_NeiMeng.canPeng(rules))
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Gang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 过滤杠牌
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_Gang.class));

					// 如果房间支持碰牌上听，只有导致上听的明杠才被允许
					if (canPengTing)
						afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_GangTing.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 补杠
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_BuGang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 过滤杠牌
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_Gang.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 暗杠
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_AnGang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 过滤杠牌
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_Gang.class));

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 胡牌
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Hu_NeiMeng.class);

				{
					JSONArray preFilters = CreateContextHelper.createArray();

					// 前置操作必须是出牌，摸牌
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_DEAL));
					if (!rules.contains(Rules_NeiMeng.HEI_MO))
						preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_PLAY_A_CARD));

					processor.element(OperationCheckerBase.KEY_PRE_FILTER, preFilters);
				}

				// 过胡
				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					JSONObject filter = CreateContextHelper.createObject(OperationResultFilter_PassHu_NeiMeng.class);
					JSONArray tingOperations = CreateContextHelper.createArray();
					if (canChiTing)
						tingOperations.add(PlayType.OPERATE_CHI_A_CARD);
					if (canPengTing)
					{
						tingOperations.add(PlayType.OPERATE_PENG_A_CARD);
						tingOperations.add(PlayType.OPERATE_GANG_A_CARD);
					}

					filter.element(OperationResultFilter_PassHu_NeiMeng.KEY_CHECK_PASS_ZI_MO_HU, true);
					filter.element(OperationResultFilter_PassHu_NeiMeng.KEY_CHECK_PASS_DIAN_HU, true);
					filter.element(OperationResultFilter_PassHu_NeiMeng.KEY_tingOperations, tingOperations);
					afterFilters.element(filter);

					if (rules.contains(Rules_NeiMeng.TAN_BA_ZHANG))
						afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_TanBaZhang.class));

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

			result.element(OperationChecker.KEY_OPERATION_CHECKERS, proc);
		}

		context.element(OperationChecker.class.getSimpleName(), result);
	}

	/**
	 * 配置附加操作处理器
	 */
	private void createAfterOperationProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject();
		JSONArray arrayContext = CreateContextHelper.createArray();

		// 吃牌上听
		if (Rules_NeiMeng.canChiTing(rules))
		{
			// 吃牌后再打牌进入上听状态
			JSONObject chiTing = CreateContextHelper.createObject(AfterOperationProcessor_EnterTing.class);
			chiTing.element(AfterOperationProcessorBase.KEY_PLAY_TYPE, PlayType.OPERATE_PLAY_A_CARD);
			chiTing.element(AfterOperationProcessor_EnterTing.KEY_prePlayType, PlayType.OPERATE_CHI_A_CARD);
			arrayContext.add(chiTing);
		}

		// 碰牌（杠牌）上听
		if (Rules_NeiMeng.canPengTing(rules))
		{
			// 碰牌后再打牌进入上听状态
			JSONObject pengTing = CreateContextHelper.createObject(AfterOperationProcessor_EnterTing.class);
			pengTing.element(AfterOperationProcessorBase.KEY_PLAY_TYPE, PlayType.OPERATE_PLAY_A_CARD);
			pengTing.element(AfterOperationProcessor_EnterTing.KEY_prePlayType, PlayType.OPERATE_PENG_A_CARD);
			arrayContext.add(pengTing);

			// 杠牌后再打牌进入上听状态
			JSONObject gangTing = CreateContextHelper.createObject(AfterOperationProcessor_GangEnterTing.class);
			gangTing.element(AfterOperationProcessorBase.KEY_PLAY_TYPE, PlayType.OPERATE_PLAY_A_CARD);
			arrayContext.add(gangTing);
		}

		createContext.element(AfterOperationProcessor.KEY_PROCESSORS, arrayContext);
		context.element(AfterOperationProcessor.class.getSimpleName(), createContext);
	}

	/**
	 * 打牌处理器
	 */
	private void createPlayerCardProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject();
		JSONArray arrayContext = CreateContextHelper.createArray();

		// 过滤要上听时可以打什么牌
		boolean canChiTing = Rules_NeiMeng.canChiTing(rules);
		boolean canPengTing = Rules_NeiMeng.canPengTing(rules);
		if (canChiTing)
		{
			// 吃牌上听
			arrayContext.add(CreateContextHelper.createObject(PlayCardFilter_EnterTing.class).element(PlayCardFilter_EnterTing.KEY_enterPlayType, PlayType.OPERATE_CHI_A_CARD));
		}
		if (canPengTing)
		{
			// 碰牌上听
			arrayContext.add(CreateContextHelper.createObject(PlayCardFilter_EnterTing.class).element(PlayCardFilter_EnterTing.KEY_enterPlayType, PlayType.OPERATE_PENG_A_CARD));

			// 明杠上听
			arrayContext.add(CreateContextHelper.createObject(PlayCardFilter_GangEnterTing.class));
		}

		createContext.element(PlayCardProcessor.KEY_scoreResultFilters, arrayContext);
		context.element(PlayCardProcessor.class.getSimpleName(), createContext);
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
			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Gang.class));
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

				// 蹲拉跑
				int dunLaPaoPoint = Rules_NeiMeng.getDunLaPaoPoint(rules);
				if (dunLaPaoPoint > 0)
				{
					arrayContext.add(CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_Dun.class, ScoreCalculateType.TOTAL_ADD, dunLaPaoPoint));
					arrayContext.add(CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_La.class, ScoreCalculateType.TOTAL_ADD, dunLaPaoPoint));
					arrayContext.add(CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_Pao.class, ScoreCalculateType.TOTAL_ADD, dunLaPaoPoint));
				}

				if (rules.contains(Rules_NeiMeng.PEI_DIAN_GANG_REN) || rules.contains(Rules_NeiMeng.PEI_SAN_JIA))
					arrayContext.add(CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_PeiGang_WuLanChaBu.class, ScoreCalculateType.TOTAL_ADD, 0));

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
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_DIAN_PAO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_AN_GANG, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_EX_CARD, true, false));

			if (Rules_NeiMeng.getDunLaPaoPoint(rules) > 0)
			{
				arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_DUN, true, false));
				arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_LA, true, false));
				arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_PAO, true, false));
				arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_DUN, false, false));
				arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_LA, false, false));
				arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_PAO, false, false));
			}

			context.element(GameScoreCalculator.KEY_GAME_SCORE_FILTER, arrayContext);
			createContext.element(CalculateProcesser.KEY_GAME_SCORE_CALCULATOR, context);
		}

		return createContext;
	}
}
