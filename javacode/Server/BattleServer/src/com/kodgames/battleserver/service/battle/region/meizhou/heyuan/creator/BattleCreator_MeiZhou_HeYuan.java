package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.creator;

import java.util.List;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.IBattleCreator;
import com.kodgames.battleserver.service.battle.core.finish.BattleFinishChecker_Common;
import com.kodgames.battleserver.service.battle.core.finish.PlayerFinishChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_13YaoHu;
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
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_QiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_ShiSanYao;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_HunYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_PengPengHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QingYaoJiu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QingYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_TianHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_ZiYiSe;
import com.kodgames.battleserver.service.battle.processer.CalculateProcesser;
import com.kodgames.battleserver.service.battle.processer.InitCardPoolProcesser;
import com.kodgames.battleserver.service.battle.processer.ZhuangInfoProcesser;
import com.kodgames.battleserver.service.battle.region.meizhou.Rules_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.operation.AfterOperationProcessor_BreakFan;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.operation.AfterOperationProcessor_LengPeng;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.operation.OperationChecker_Fan;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.operation.filter.OperationResultFilter_Hu_PingJi;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.processor.BattleProcesser_HeYuan;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.processor.DealCardProcesser_HeYuan;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.processor.Processor_JiangMa_HeYuan;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.processor.Processor_SetMasterHuaCard;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.battle.BattleScoreCalculator_HeYuan;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.battle.BattleScoreChecker_BuGang_HeYuan;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.game.GameScoreCalculator_HeYuan;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.hu.HuSubScoreChecker_DaFanBei;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.hu.HuSubScoreChecker_DaGe;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.hu.HuSubScoreChecker_HuaDiaoHua;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.hu.HuSubScoreChecker_HunPeng;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.hu.HuSubScoreChecker_HunYaoJiu_HeYuan;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.zhuang.ZhuangCalculator_HeYuan;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BattleCreator_MeiZhou_HeYuan implements IBattleCreator
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
		return Rules_MeiZhou.checkHeYuanRules(rules);
	}

	private void createZhuangCalculator(JSONObject context, List<Integer> rules)
	{
		// 创建庄家计算器
		context.element(BattleHelper.KEY_ZHUANG_CALCULATOR, CreateContextHelper.createObject(ZhuangCalculator_HeYuan.class));
	}

	private void createMainProcessor(JSONObject context, List<Integer> rules)
	{
		JSONArray result = CreateContextHelper.createArray();

		// 确定庄家
		result.element(CreateContextHelper.createObject(ZhuangInfoProcesser.class));

		// 初始化牌池
		{
			JSONObject subContext = CreateContextHelper.createObject(InitCardPoolProcesser.class);

			// 选择4张花牌
			if (rules.contains(Rules_MeiZhou.GAME_PLAY_HUA_4))
			{
				JSONArray supportTypeContext = CreateContextHelper.createArray();

				supportTypeContext.add(MahjongConstant.CardType.WAN.toString());
				supportTypeContext.add(MahjongConstant.CardType.TIAO.toString());
				supportTypeContext.add(MahjongConstant.CardType.TONG.toString());
				supportTypeContext.add(MahjongConstant.CardType.ZI.toString());
				supportTypeContext.add(MahjongConstant.CardType.SEASON.toString());

				subContext.element(InitCardPoolProcesser.KEY_SUPPORT_CARD_TYPES, supportTypeContext);

			}
			else if (rules.contains(Rules_MeiZhou.GAME_PLAY_HUA_8))
			{
				// 选择8张花牌

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
		result.element(CreateContextHelper.createObject(DealCardProcesser_HeYuan.class));

		// 花牌
		JSONObject huaContext = CreateContextHelper.createObject(Processor_SetMasterHuaCard.class);
		if (rules.contains(Rules_MeiZhou.GAME_PLAY_HUA_4) || rules.contains(Rules_MeiZhou.GAME_PLAY_HUA_8))
		{
			// 万能花牌
			if (rules.contains(Rules_MeiZhou.GAME_PLAY_HUA_4))
				huaContext.element(Processor_SetMasterHuaCard.KEY_HAS_FLOWER_CARD, false);
			else if (rules.contains(Rules_MeiZhou.GAME_PLAY_HUA_8))
				huaContext.element(Processor_SetMasterHuaCard.KEY_HAS_FLOWER_CARD, true);

			result.element(huaContext);
		}

		// 打牌
		result.element(createBattleProcessor(rules));

		// 奖马
		if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_2) || rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_4) || rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_6)
			|| rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_8))
		{
			JSONObject subContext = CreateContextHelper.createObject(Processor_JiangMa_HeYuan.class);

			// 马跟翻
			subContext.element(Processor_JiangMa_HeYuan.KEY_MA_GEN_FAN, rules.contains(Rules_MeiZhou.GAME_PLAY_MA_GEN_FAN));

			// 硬马
			subContext.element(Processor_JiangMa_HeYuan.KEY_YING_MA, rules.contains(Rules_MeiZhou.GAME_PLAY_YING_MA));

			// 马跟杠
			subContext.element(Processor_JiangMa_HeYuan.KEY_MA_GEN_GANG, false);

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

				arrayContext.add(CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_BuGang_HeYuan.class, ScoreCalculateType.TOTAL_ADD, 1));

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
			JSONObject context = CreateContextHelper.createObject(GameScoreCalculator_HeYuan.class);
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 设置房间分数统计
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_ZI_MO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_AN_GANG, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_BU_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_DA_FAN_BEI, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_HUA_DIAO_HUA, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE, true, false));

			context.element(GameScoreCalculator_HeYuan.KEY_GAME_SCORE_FILTERS_HE_YUAN, arrayContext);
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
		JSONObject result = CreateContextHelper.createObject(BattleProcesser_HeYuan.class);

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

			// 暗杠
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_AnGang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 河源吃杠(明杠)
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Gang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 河源明杠(补杠)
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_BuGang.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);

			}

			// 翻检测
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Fan.class);

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					if (rules.contains(Rules_MeiZhou.GAME_PLAY_JI_PING))
					{
						// 添加平鸡胡翻牌过滤器
						afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_Hu_PingJi.class));

						processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);

					}
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
					// preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_BU_GANG_A_CARD));

					// 自摸胡
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_DEAL));

					// 点炮胡(不能吃胡)
					// preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_PLAY_A_CARD));

					processor.element(OperationCheckerBase.KEY_PRE_FILTER, preFilters);
				}

				if (rules.contains(Rules_MeiZhou.GAME_PLAY_JI_PING))
				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 添加平鸡胡
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_Hu_PingJi.class));

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
		JSONObject result = CreateContextHelper.createObject();
		JSONArray arrayContext = CreateContextHelper.createArray();

		// 冷碰的附加
		{
			JSONObject lengPeng = CreateContextHelper.createObject(AfterOperationProcessor_LengPeng.class);
			lengPeng.element(AfterOperationProcessor_BreakFan.KEY_PLAY_TYPE, PlayType.OPERATE_PENG_A_CARD);
			arrayContext.add(lengPeng);
		}

		// 翻的附加
		{
			JSONObject breakFan = CreateContextHelper.createObject(AfterOperationProcessor_BreakFan.class);
			breakFan.element(AfterOperationProcessor_BreakFan.KEY_PLAY_TYPE, PlayType.OPERATE_PLAY_A_CARD);
			arrayContext.add(breakFan);
		}

		result.element(AfterOperationProcessor.KEY_PROCESSORS, arrayContext);

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
		JSONObject context = CreateContextHelper.createObject(BattleScoreCalculator_HeYuan.class);

		// 设置为0就是不封顶
		context.element(BattleScoreCalculator.KEY_MAX_VALUE, 0);

		JSONArray huEventOrders = CreateContextHelper.createArray();

		{
			// 平胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_PING_HU));

			// 小七对
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_QI_DUI));

			// 十三幺
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_SHI_SAN_YAO));

			// 碰碰胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_PENG_PENG_HU));

			// 混一色
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_HUN_YI_SE));

			// 清一色
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_QING_YI_SE));

			// 混碰
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_HUN_PENG));

			// 大哥
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_DA_GE));

			// 混幺九
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_HUN_YAO_JIU));

			// 字一色
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_ZI_YI_SE));

			// 全幺九
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_QING_YAO_JIU));

			// 天胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_TIAN_HU));

			// 打翻倍
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_DA_FAN_BEI));

			// 花吊花
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.HU_HUA_DIAO_HUA));

			// 奖马
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_HeYuan.KEY_HU_PLAY_TYPE, PlayType.DISPLAY_BETTING_HOUSE));

		}

		context.element(BattleScoreCalculator_HeYuan.KEY_HU_EVENT_ORDER, huEventOrders);

		createContext.element(BattleHelper.KEY_BATTLE_SCORE_CALCULATOR, context);
	}

	/**
	 * 设置牌局结束条件
	 */
	private void createBattleFinishChecker(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleFinishChecker_Common.class);

		// 如果选择了奖马，要留够奖马的牌，提前流局
		if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_2))
		{
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 2);
		}
		else if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_4))
		{
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 4);
		}
		else if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_6))
		{
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 6);
		}
		else if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_8))
		{
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 8);
		}
		else
		{
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 0);
		}

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
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_Gang.class, ScoreCalculateType.TOTAL_ADD, 1));

			// 暗杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_AnGang.class, ScoreCalculateType.TOTAL_ADD, 1));

			// 补杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_BuGang.class, ScoreCalculateType.TOTAL_ADD, 1));

			// // 冷杠分数计算（测试）
			// arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_LengGang.class,
			// ScoreCalculateType.TOTAL_ADD, 0));

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

			// 七小对
			checkers.element(CreateContextHelper.createObject(HuChecker_7DuiHu.class));

			// 十三幺
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

		{
			// 分数收分目标过滤器
			JSONArray arrayContext = CreateContextHelper.createArray();

			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Common.class));
			createContext.element(HuScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		// 支持的分数
		{
			JSONArray arrayContext = CreateContextHelper.createArray();
			// 百搭鸡胡牌型
			if (rules.contains(Rules_MeiZhou.GAME_PLAY_JI_BAI_DA))
			{
				// 鸡胡：3分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_PinHu.class, ScoreCalculateType.TOTAL_ADD, 3));

				// 七小对：6分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_QiDui.class, ScoreCalculateType.TOTAL_ADD, 6));

				// 十三幺：12分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_ShiSanYao.class, ScoreCalculateType.TOTAL_ADD, 12));

				// 天胡 2倍
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_TianHu.class, ScoreCalculateType.TOTAL_MULTI, 2));

				// 大翻倍 2倍
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DaFanBei.class, ScoreCalculateType.TOTAL_MULTI, 2));

				// 花吊花 4倍
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HuaDiaoHua.class, ScoreCalculateType.TOTAL_MULTI, 4, new int[] {PlayType.HU_DA_FAN_BEI}));

			}
			else if (rules.contains(Rules_MeiZhou.GAME_PLAY_JI_PING))
			{
				// 鸡胡：3分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_PinHu.class, ScoreCalculateType.TOTAL_ADD, 3));

				// 杂色（混一色) 3分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYiSe.class, ScoreCalculateType.TOTAL_ADD, 3, new int[] {PlayType.HU_PING_HU}));

				// 对对胡（碰碰胡）6 分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_PengPengHu.class, ScoreCalculateType.TOTAL_ADD, 6, new int[] {PlayType.HU_PING_HU}));

				// 清一色 9分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYiSe.class, ScoreCalculateType.TOTAL_ADD, 9, new int[] {PlayType.HU_PING_HU}));

				// 混碰 9分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunPeng.class,
					ScoreCalculateType.TOTAL_ADD,
					9,
					new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU, PlayType.HU_HUN_YI_SE}));

				// 大哥 12分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DaGe.class,
					ScoreCalculateType.TOTAL_ADD,
					12,
					new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU, PlayType.HU_QING_YI_SE}));

				// 混幺九 12分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYaoJiu_HeYuan.class,
					ScoreCalculateType.TOTAL_ADD,
					12,
					new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU, PlayType.HU_HUN_YI_SE, PlayType.HU_HUN_PENG}));

				// 全幺九(清幺九) 18分
				arrayContext
					.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYaoJiu.class, ScoreCalculateType.TOTAL_ADD, 18, new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU}));

				// 字一色 18分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZiYiSe.class, ScoreCalculateType.TOTAL_ADD, 18, new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU}));

				// 十三幺 18分
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_ShiSanYao.class, ScoreCalculateType.TOTAL_ADD, 18));

				// 天胡 2倍
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_TianHu.class, ScoreCalculateType.TOTAL_MULTI, 2));

				// 大翻倍 2倍
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DaFanBei.class, ScoreCalculateType.TOTAL_MULTI, 2));

				// 花吊花 4倍
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HuaDiaoHua.class, ScoreCalculateType.TOTAL_MULTI, 4, new int[] {PlayType.HU_DA_FAN_BEI}));

			}

			createContext.element(HuScoreProcessor.KEY_SCORE_CHECKERS, arrayContext);

		}

		context.element(BattleHelper.KEY_HU_SCORE_PROCESSOR, createContext);
	}
}
