package com.kodgames.battleserver.service.battle.region.yangzhou.creator;

import java.util.List;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.IBattleCreator;
import com.kodgames.battleserver.service.battle.core.finish.PlayerFinishChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_PingHu;
import com.kodgames.battleserver.service.battle.core.operation.AfterOperationProcessor;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker;
import com.kodgames.battleserver.service.battle.core.operation.OperationCheckerBase;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_AnGang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_BuGang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Gang;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Pass;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Peng;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_PassBuGang;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_PassHu;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_PassPeng;
import com.kodgames.battleserver.service.battle.core.playcard.PlayCardProcessor;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
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
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_HunYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_PengPengHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QingYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_ZiYiSe;
import com.kodgames.battleserver.service.battle.processer.BattleProcesser;
import com.kodgames.battleserver.service.battle.processer.DealCardProcesser;
import com.kodgames.battleserver.service.battle.processer.InitCardPoolProcesser;
import com.kodgames.battleserver.service.battle.processer.ZhuangInfoProcesser;
import com.kodgames.battleserver.service.battle.region.yangzhou.Rules_YangZhou;
import com.kodgames.battleserver.service.battle.region.yangzhou.finish.BattleFinishChecker_YangZhou;
import com.kodgames.battleserver.service.battle.region.yangzhou.operation.OperationChecker_Hu_YangZhou;
import com.kodgames.battleserver.service.battle.region.yangzhou.operation.filter.OperationResultFilter_Hu_YangZhou;
import com.kodgames.battleserver.service.battle.region.yangzhou.processer.Processer_CheckMasterCard_YangZhou;
import com.kodgames.battleserver.service.battle.region.yangzhou.processer.CalculateProcesser_YangZhou;
import com.kodgames.battleserver.service.battle.region.yangzhou.processer.YuanZiInfoProcesser_YangZhou;
import com.kodgames.battleserver.service.battle.region.yangzhou.score.battle.BattleScoreCalculator_YangZhou;
import com.kodgames.battleserver.service.battle.region.yangzhou.score.filter.ScoreTargetFilter_Gang_YangZhou;
import com.kodgames.battleserver.service.battle.region.yangzhou.score.game.GameScoreCalculator_YangZhou;
import com.kodgames.battleserver.service.battle.region.yangzhou.score.hu.HuScoreProcessor_YangZhou;
import com.kodgames.battleserver.service.battle.region.yangzhou.score.hu.HuSubScoreChecker_HunDuiDui;
import com.kodgames.battleserver.service.battle.region.yangzhou.score.hu.HuSubScoreChecker_QingDuiDui;
import com.kodgames.battleserver.service.battle.region.yangzhou.zhuang.ZhuangCalculator_YangZhou;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BattleCreator_YangZhou implements IBattleCreator
{	
	@Override
	public boolean checkRules(List<Integer> rules)
	{
		return Rules_YangZhou.checkRules(rules);
	}

	@Override
	public JSONObject create(List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleHelper.class.getSimpleName());
		this.createZhuangCalculator(result, rules);
		this.createHuCheckerProcessor(result, rules);
		this.createBattleFinishChecker(result, rules);
		this.createHuCalculateProcessor(result, rules);
		this.createBattleScoreCalculator(result, rules);
		this.createMainProcessor(result, rules);

		return result;
	}

	/**
	 * 创建庄家计算器
	 */
	private void createZhuangCalculator(JSONObject context, List<Integer> rules)
	{
		context.element(BattleHelper.KEY_ZHUANG_CALCULATOR, CreateContextHelper.createObject(ZhuangCalculator_YangZhou.class));
	}

	/**
	 * 创建胡Checker
	 */
	private void createHuCheckerProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(HuChecker.class);

		// 胡牌牌形检查器
		{
			JSONArray checkers = CreateContextHelper.createArray();

			// 平胡
			checkers.element(CreateContextHelper.createObject(HuChecker_PingHu.class));

			createContext.element(HuChecker.KEY_CHECKERS, checkers);
		}

		context.element(BattleHelper.KEY_HU_CHECK_PROCESSOR, createContext);
	}

	/**
	 * 设置牌局结束条件
	 */
	private void createBattleFinishChecker(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleFinishChecker_YangZhou.class);

		// 没有牌就结束牌局
		result.element(BattleFinishChecker_YangZhou.KEY_STAY_CARD_COUNT, 0);

		// 只要胡一次就可以结束牌局
		result.element(BattleFinishChecker_YangZhou.KEY_HU_COUNT, 1);

		context.element(BattleHelper.KEY_BATTLE_FINISH_CHECKER, result);
	}

	/**
	 * 胡牌得分计算器
	 */
	private void createHuCalculateProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject createContext = CreateContextHelper.createObject(HuScoreProcessor_YangZhou.class);
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

			// 平胡：2分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_PinHu.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 对对胡 4分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_PengPengHu.class, ScoreCalculateType.TOTAL_ADD, 4, new int[] {PlayType.HU_PING_HU}));

			// 混一色 4分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYiSe.class, ScoreCalculateType.TOTAL_ADD, 4, new int[] {PlayType.HU_PING_HU}));
			
			// 清一色 8分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYiSe.class, ScoreCalculateType.TOTAL_ADD, 8, new int[] {PlayType.HU_PING_HU}));

			// 混对对8分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunDuiDui.class, ScoreCalculateType.TOTAL_ADD, 8, new int[] {PlayType.HU_PING_HU, PlayType.HU_HUN_YI_SE, PlayType.HU_PENG_PENG_HU}));
			
			// 清对对12分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingDuiDui.class, ScoreCalculateType.TOTAL_ADD, 12, new int[] {PlayType.HU_PING_HU, PlayType.HU_QING_YI_SE, PlayType.HU_PENG_PENG_HU}));
			
			// 风清 32分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZiYiSe.class, ScoreCalculateType.TOTAL_ADD, 32, new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU}));

			createContext.element(HuScoreProcessor.KEY_SCORE_CHECKERS, arrayContext);
		}

		context.element(BattleHelper.KEY_HU_SCORE_PROCESSOR, createContext);
	}

	/**
	 * 创建战斗分数计算器
	 */
	private void createBattleScoreCalculator(JSONObject createContext, List<Integer> rules)
	{
		JSONObject context = CreateContextHelper.createObject(BattleScoreCalculator_YangZhou.class);

		// 设置为0就是不封顶
		context.element(BattleScoreCalculator_YangZhou.KEY_MAX_VALUE, 0);

		createContext.element(BattleHelper.KEY_BATTLE_SCORE_CALCULATOR, context);
	}
	
	private void createMainProcessor(JSONObject context, List<Integer> rules)
	{
		JSONArray result = CreateContextHelper.createArray();

		// 检测是否有人进园子
		result.element(CreateContextHelper.createObject(YuanZiInfoProcesser_YangZhou.class));

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

				// 三人玩法中默认不含有筒子
				if (!rules.contains(Rules_YangZhou.GAME_PLAY_SAN_REN))
					supportTypeContext.add(MahjongConstant.CardType.TONG.toString());

				supportTypeContext.add(MahjongConstant.CardType.ZI.toString());

				subContext.element(InitCardPoolProcesser.KEY_SUPPORT_CARD_TYPES, supportTypeContext);
			}

			result.element(subContext);
		}

		// 发牌
		result.element(CreateContextHelper.createObject(DealCardProcesser.class));

		// 确定搬子和配子
		result.element(CreateContextHelper.createObject(Processer_CheckMasterCard_YangZhou.class));

		// 打牌
		result.element(this.createBattleProcessor(rules));

		// 分数计算
		result.element(this.createCalculate(rules));

		context.element(MahjongConstant.JSON_PROC, result);
	}

	private JSONObject createCalculate(List<Integer> rules)
	{

		JSONObject createContext = CreateContextHelper.createObject(CalculateProcesser_YangZhou.class);

		// 牌局相关分数
		{
			JSONObject context = CreateContextHelper.createObject();

			// 牌局分数过滤
			{
				JSONArray arrayContext = CreateContextHelper.createArray();
				context.element(BattleScoreProcessor.KEY_SCORE_FILTERS, arrayContext);
			}

			createContext.element(CalculateProcesser_YangZhou.KEY_BATTLE_SCORE_PROCESSOR, context);
		}

		// 房间分数计算器
		{
			JSONObject context = CreateContextHelper.createObject(GameScoreCalculator_YangZhou.class);
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 设置房间分数统计
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_ZI_MO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_DIAN_PAO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_DIAN_PAO, false, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_AN_GANG, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_GANG_A_CARD, true, false));

			context.element(GameScoreCalculator.KEY_GAME_SCORE_FILTER, arrayContext);
			createContext.element(CalculateProcesser_YangZhou.KEY_GAME_SCORE_CALCULATOR, context);
		}

		return createContext;
	}

	/**
	 * 创建战斗相关处理器，比如前置操作处理和后置操作处理
	 */
	private JSONObject createBattleProcessor(List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleProcesser.class);

		this.createOperationChecker(result, rules);
		this.createAfterOperationProcessor(result, rules);
		this.createPlayerCardProcessor(result, rules);
		this.createPlayerFinishFilter(result, rules);
		this.createGangScoreProcessor(result, rules);

		return result;
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
					// 补杠必须是在抓到的这张牌触发，如果这次没有杠，以后不能杠了
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_PassBuGang.class));
					processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
				}

				proc.element(processor);
			}

			// 胡牌
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_Hu_YangZhou.class);

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

					// 不能吃胡进园子的玩家
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_Hu_YangZhou.class));

					// 过胡后，下次摸牌前不能再胡牌
					JSONObject passHuFilter = CreateContextHelper.createObject(OperationResultFilter_PassHu.class);
					passHuFilter.element(OperationResultFilter_PassHu.KEY_CHECK_PASS_ZI_MO_HU, true);
					passHuFilter.element(OperationResultFilter_PassHu.KEY_CHECK_PASS_DIAN_HU, true);
					afterFilters.element(passHuFilter);

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
		context.element(AfterOperationProcessor.class.getSimpleName(), result);
	}

	private void createPlayerCardProcessor(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject();
		context.element(PlayCardProcessor.class.getSimpleName(), result);
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
			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Gang_YangZhou.class));
			createContext.element(GangScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 明杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_Gang.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 暗杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_AnGang.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 补杠分数计算
			arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_BuGang.class, ScoreCalculateType.TOTAL_ADD, 1));

			createContext.element(GangScoreProcessor.KEY_POINT_CACULATORS, arrayContext);
		}

		context.element(GangScoreProcessor.class.getSimpleName(), createContext);
	}
	
	@Override
	public int getMaxPlayerSize(List<Integer> rules)
	{
		if(rules.contains(Rules_YangZhou.GAME_PLAY_SAN_REN))
			return MahjongConstant.DEFAULT_ROOM_MEMBER_COUNT - 1;
		
		return MahjongConstant.DEFAULT_ROOM_MEMBER_COUNT;
	}
}
