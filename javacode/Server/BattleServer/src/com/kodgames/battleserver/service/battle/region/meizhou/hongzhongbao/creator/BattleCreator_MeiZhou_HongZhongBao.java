package com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.creator;

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
import com.kodgames.battleserver.service.battle.core.operation.*;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_PassPeng;
import com.kodgames.battleserver.service.battle.core.playcard.PlayCardProcessor;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreCalculator;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.battle.filter.BattleScoreFilter_RemoveGangWhenHuangZhuang;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreSourceFilter_LastOperator;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreFilter;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_AnGang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_BuGang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker_Gang;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.*;
import com.kodgames.battleserver.service.battle.processer.*;
import com.kodgames.battleserver.service.battle.region.meizhou.Rules_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.finish.BattleFinishChecker_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.operation.AfterOperationProcessor_GenZhuang_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.operation.OperationChecker_BuGang_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.processor.Processor_JiangMa;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.battle.BattleScoreCalculator_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.battle.filter.BattleScoreFilter_GenZhuang_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.filter.ScoreTargetFilter_Gang;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.filter.ScoreTarrgetFilter_Hu_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.game.GameScoreCalculator_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.hu.HuSubScoreChecker_DiHu_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.hu.HuSubScoreChecker_HaiDiLaoYue_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.hu.HuSubScoreChecker_HunYaoJiu_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.common.score.hu.HuSubScoreChecker_KanKanHu;
import com.kodgames.battleserver.service.battle.region.meizhou.common.zhuang.ZhuangCalculator_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.hu.HuChecker_HongZhongBao;
import com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.operation.filter.OperationResultFilter_Hu;
import com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.operation.filter.OperationResultFilter_ScoreLimit_HongZhongBao;
import com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.processor.Processor_SetMasterHongZhong;
import com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.score.hu.HuMainScoreChecker_HongZhongBao;
import com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.score.hu.HuSubScoreChecker_HongZhongBao;
import com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.score.hu.HuSubScoreChecker_WuHongZhong;
import com.kodgames.corgi.core.constant.BattleConstant.RoomConst;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public class BattleCreator_MeiZhou_HongZhongBao implements IBattleCreator
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
		return Rules_MeiZhou.checkHongZhongBaoRules(rules);
	}

	@Override
	public int getMaxPlayerSize(List<Integer> rules)
	{
		// 二人玩法
		if (rules.contains(Rules_MeiZhou.GAME_PLAY_TWO_PLAYER))
		{
			return RoomConst.MAX_ROOM_MEMBER_COUNT - 2;
		}
		// 选择三人玩法，默认四人玩法
		if (rules.contains(Rules_MeiZhou.GAME_PLAY_SAN_REN))
		{
			return RoomConst.MAX_ROOM_MEMBER_COUNT - 1;
		}

		return RoomConst.MAX_ROOM_MEMBER_COUNT;
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

				if (rules.contains(Rules_MeiZhou.GAME_PLAY_TWO_PLAYER))
				{
					JSONArray supportCards = CreateContextHelper.createArray();

					// 条
					{
						JSONObject supportCard = CreateContextHelper.createObject();
						supportCard.element(InitCardPoolProcesser.KEY_SUPPORT_CARDS_TYPE, MahjongConstant.CardType.TIAO.toString());
						JSONArray cards = CreateContextHelper.createArray();
						cards.element(MahjongConstant.CardType.TIAO.Value());
						cards.element(MahjongConstant.CardType.TIAO.Value() + 8);
						supportCard.element(InitCardPoolProcesser.KEY_SUPPORT_CARDS_VALUES, cards);
						supportCards.element(supportCard);
					}
					// 筒
					{
						JSONObject supportCard = CreateContextHelper.createObject();
						supportCard.element(InitCardPoolProcesser.KEY_SUPPORT_CARDS_TYPE, MahjongConstant.CardType.TONG.toString());
						JSONArray cards = CreateContextHelper.createArray();
						cards.element(MahjongConstant.CardType.TONG.Value());
						cards.element(MahjongConstant.CardType.TONG.Value() + 8);
						supportCard.element(InitCardPoolProcesser.KEY_SUPPORT_CARDS_VALUES, cards);
						supportCards.element(supportCard);
					}

					subContext.element(InitCardPoolProcesser.KEY_SUPPORT_CARDS, supportCards);
				}
				else
				{
					supportTypeContext.add(MahjongConstant.CardType.TIAO.toString());
					supportTypeContext.add(MahjongConstant.CardType.TONG.toString());
				}


				supportTypeContext.add(MahjongConstant.CardType.ZI.toString());

				subContext.element(InitCardPoolProcesser.KEY_SUPPORT_CARD_TYPES, supportTypeContext);
			}

			result.element(subContext);
		}

		// 发牌
		result.element(CreateContextHelper.createObject(DealCardProcesser.class));

		// 添加红中鬼牌
		JSONObject hongZhongContext = CreateContextHelper.createObject(Processor_SetMasterHongZhong.class);
		result.element(hongZhongContext);

		// 打牌
		result.element(createBattleProcessor(rules));

		// 奖马
		if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_2) || rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_5) || rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_8))
		{
			JSONObject subContext = CreateContextHelper.createObject(Processor_JiangMa.class);
			// 人数
			subContext.element(Processor_JiangMa.KEY_PLAYER_COUNT, rules.contains(Rules_MeiZhou.GAME_PLAY_TWO_PLAYER) ? 2 : rules.contains(Rules_MeiZhou.GAME_PLAY_SAN_REN) ? 3 : 4);
			// 马跟杠
			subContext.element(Processor_JiangMa.KEY_MA_GEN_GANG, rules.contains(Rules_MeiZhou.GAME_PLAY_MA_GEN_GANG));

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

			// 牌局分数过滤
			{
				JSONArray arrayContext = CreateContextHelper.createArray();

				// 流局不算杠的分数
				arrayContext.add(CreateContextHelper.createObject(BattleScoreFilter_RemoveGangWhenHuangZhuang.class));

				// 流局不计算跟庄分数
				arrayContext.add(CreateContextHelper.createObject(BattleScoreFilter_GenZhuang_MeiZhou.class));

				// 补杠转成明杠
				// arrayContext.add(CreateContextHelper.createObject(BattleScoreFilter_Gang_MeiZhou.class));

				context.element(BattleScoreProcessor.KEY_SCORE_FILTERS, arrayContext);
			}

			createContext.element(CalculateProcesser.KEY_BATTLE_SCORE_PROCESSOR, context);
		}

		// 房间分数计算器
		{
			JSONObject context = CreateContextHelper.createObject(GameScoreCalculator_MeiZhou.class);
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 设置房间分数统计
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, PlayType.HU_ZI_MO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, PlayType.HU_DIAN_PAO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, PlayType.HU_DIAN_PAO, false, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, PlayType.OPERATE_AN_GANG, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, PlayType.OPERATE_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, PlayType.OPERATE_BU_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, PlayType.DISPLAY_FOLLOW_BANKER, false, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, PlayType.DISPLAY_BETTING_HOUSE, true, true));

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
				if (rules.contains(Rules_MeiZhou.GAME_PLAY_BAO_GANG))
				{
					JSONObject processor = CreateContextHelper.createObject(OperationChecker_BuGang_MeiZhou.class);

					{
						JSONArray afterFilters = CreateContextHelper.createArray();

						processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
					}

					proc.element(processor);

				}
				else if (rules.contains(Rules_MeiZhou.GAME_PLAY_BU_BAO_GANG))
				{
					JSONObject processor = CreateContextHelper.createObject(OperationChecker_BuGang.class);

					{
						JSONArray afterFilters = CreateContextHelper.createArray();

						processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
					}

					// {
					// JSONArray afterFilters = CreateContextHelper.createArray();
					//
					// // 明杠必须是在抓到的这张牌触发，如果这次没有杠，以后不能杠了
					// afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_PassBuGang.class));
					//
					// processor.element(OperationCheckerBase.KEY_AFTER_FILTER, afterFilters);
					// }

					proc.element(processor);
				}

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

					// 点炮胡(不能吃胡)
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_PLAY_A_CARD));

					processor.element(OperationCheckerBase.KEY_PRE_FILTER, preFilters);
				}

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					{
						// 软三和硬三
						JSONObject object = CreateContextHelper.createObject(OperationResultFilter_ScoreLimit_HongZhongBao.class);

						object.element(OperationResultFilter_ScoreLimit_HongZhongBao.KEY_SCORE_LIMIT,
							rules.contains(Rules_MeiZhou.GAME_PLAY_RUAN_SAN) ? 3 : rules.contains(Rules_MeiZhou.GAME_PLAY_YING_SAN) ? 4 : 0);

						afterFilters.element(object);

					}
					// 不能吃胡（地胡可以吃胡）
					afterFilters.element(CreateContextHelper.createObject(OperationResultFilter_Hu.class));

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
			JSONArray afterFilters = CreateContextHelper.createArray();
			{
				// 二人玩法没有跟庄
				if (rules.contains(Rules_MeiZhou.GAME_PLAY_TWO_PLAYER) == false)
				{
					JSONObject afterProcess = CreateContextHelper.createObject(AfterOperationProcessor_GenZhuang_MeiZhou.class);

					afterProcess.element(AfterOperationProcessor_GenZhuang_MeiZhou.KEY_CALCULATE_TYPE, ScoreCalculateType.TOTAL_ADD);
					afterProcess.element(AfterOperationProcessor_GenZhuang_MeiZhou.KEY_SCORE_VALUE, -1);
					afterProcess.element(AfterOperationProcessor_GenZhuang_MeiZhou.KEY_PLAY_TYPE, PlayType.OPERATE_PLAY_A_CARD);

					afterFilters.add(afterProcess);
				}
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

			// 小七对
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_QI_DUI));

			// 十三幺
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_SHI_SAN_YAO));

			// 红中宝
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_MAIN_HONG_ZHONG_BAO));

			// 红中宝
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_SUB_HONG_ZHONG_BAO));

			// 碰碰胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_PENG_PENG_HU));

			// 混一色
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_HUN_YI_SE));

			// 清一色
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_QING_YI_SE));

			// 豪华七小对
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_HAO_HUA_QI_DUI));

			// 坎坎胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_KAN_KAN_HU));

			// 混幺九
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_HUN_YAO_JIU));

			// 全风头
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_ZI_YI_SE));

			// 全幺九
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_QING_YAO_JIU));

			// 天胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_TIAN_HU));

			// 地胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_DI_HU));

			// 十八罗汉
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_SI_GANG));

			// 双豪华
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_SHUANG_HAO_HUA_QI_DUI));

			// 三豪华
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_SAN_HAO_HUA_QI_DUI));

			// 抢杠胡
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_QIANG_GANG_HU));

			// 杠爆
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_GANG_SHANG_HUA));

			// 海底捞月
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_HAI_DI_LAO_YUE));

			// 无红中
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_WU_HONG_ZHONG));

			// 门清
			huEventOrders.add(CreateContextHelper.createObject().element(BattleScoreCalculator_MeiZhou.KEY_HU_PLAY_TYPE, PlayType.HU_MEN_QIAN_QING));

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
		else if (rules.contains(Rules_MeiZhou.GAME_PLAY_BETTING_HOUSE_5))
		{
			result.element(BattleFinishChecker_MeiZhou.KEY_STAY_CARD_COUNT, 5);
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
			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Gang.class));
			createContext.element(GangScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		{
			JSONArray arrayContext = CreateContextHelper.createArray();
			// 明杠分数计算
			// 不包杠或者二人玩法
			if (rules.contains(Rules_MeiZhou.GAME_PLAY_BU_BAO_GANG) || rules.contains(Rules_MeiZhou.GAME_PLAY_TWO_PLAYER))
			{
				arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_Gang.class, ScoreCalculateType.TOTAL_ADD, 1));
			}
			else
			{
				// 包杠并且不是二人玩法
				arrayContext.add(CreateContextHelper.createGangScoreChecker(GangScoreChecker_Gang.class, ScoreCalculateType.TOTAL_ADD, rules.contains(Rules_MeiZhou.GAME_PLAY_SAN_REN) ? 2 : 3));
			}

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

			// 七小对
			checkers.element(CreateContextHelper.createObject(HuChecker_7DuiHu.class));

			// 十三幺
			checkers.element(CreateContextHelper.createObject(HuChecker_13YaoHu.class));

			// 红中宝
			checkers.element(CreateContextHelper.createObject(HuChecker_HongZhongBao.class));

			createContext.element(HuChecker.KEY_CHECKERS, checkers);
		}

		context.element(BattleHelper.KEY_HU_CHECK_PROCESSOR, createContext);
	}

	/**
	 * 胡牌得分计算器
	 */
	private void createHuCalculateProcessor(JSONObject context, List<Integer> rules)
	{
		// 抢杠胡的倍数(3人2倍。2人1倍)
		int qiangGangHuScore = rules.contains(Rules_MeiZhou.GAME_PLAY_SAN_REN) ? 2 : rules.contains(Rules_MeiZhou.GAME_PLAY_TWO_PLAYER) ? 1 : 3;

		qiangGangHuScore *= rules.contains(Rules_MeiZhou.GAME_PLAY_QIANG_GANG_2) ? 2 : 1;

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

			arrayContext.add(CreateContextHelper.createObject(ScoreTarrgetFilter_Hu_MeiZhou.class));
			createContext.element(HuScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		// 支持的分数
		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 基础牌型：
			// 1. 鸡胡：2分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_PinHu.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 2、 十三幺：20分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_ShiSanYao.class, ScoreCalculateType.TOTAL_ADD, 20, new int[] {PlayType.HU_HUN_YAO_JIU}));

			// 3. 七小对：6分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_QiDui.class, ScoreCalculateType.TOTAL_ADD, 6));

			// 4. 红中宝： 20分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_HongZhongBao.class,
				ScoreCalculateType.TOTAL_ADD,
				20,
				new int[] {PlayType.HU_PENG_PENG_HU, PlayType.HU_SUB_HONG_ZHONG_BAO, PlayType.HU_QING_YI_SE, PlayType.HU_HUN_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_KAN_KAN_HU,
					PlayType.HU_QING_YAO_JIU, PlayType.HU_HUN_YAO_JIU, PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_WU_HONG_ZHONG, PlayType.HU_QIANG_GANG_HU, PlayType.HU_GANG_SHANG_HUA,
					PlayType.HU_HAI_DI_LAO_YUE, PlayType.HU_DI_HU, PlayType.HU_TIAN_HU, PlayType.HU_SI_GANG, PlayType.HU_SHUANG_HAO_HUA_QI_DUI, PlayType.HU_SAN_HAO_HUA_QI_DUI}));

			// 附加牌型：
			// 4. 红中宝： 20分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HongZhongBao.class, ScoreCalculateType.TOTAL_ADD, 20, new int[] {PlayType.HU_PING_HU, PlayType.HU_TIAN_HU}));

			// 碰碰胡 4分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_PengPengHu.class, ScoreCalculateType.TOTAL_ADD, 4, new int[] {PlayType.HU_PING_HU}));

			// 清一色 8分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYiSe.class, ScoreCalculateType.TOTAL_ADD, 8, new int[] {PlayType.HU_PING_HU}));

			// 混一色 4分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYiSe.class, ScoreCalculateType.TOTAL_ADD, 4, new int[] {PlayType.HU_PING_HU}));

			// 全风头 20分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZiYiSe.class, ScoreCalculateType.TOTAL_ADD, 20, new int[] {PlayType.HU_PING_HU}));

			// 坎坎胡 20
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_KanKanHu.class, ScoreCalculateType.TOTAL_ADD, 20, new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU}));

			// 全幺九 20分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYaoJiu.class, ScoreCalculateType.TOTAL_ADD, 20));

			// 混幺九 10分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYaoJiu_MeiZhou.class, ScoreCalculateType.TOTAL_ADD, 10));

			// 豪华七小对 12分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HaoHuaQiDui.class, ScoreCalculateType.TOTAL_ADD, 12, new int[] {PlayType.HU_QI_DUI}));

			// 特殊加分：
			// 无红中 1 分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_WuHongZhong.class, ScoreCalculateType.TOTAL_ADD, 1));

			// 门清 2分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_MenQianQing.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 抢杠胡 3倍（一个承包）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QiangGangHu.class, ScoreCalculateType.TOTAL_MULTI, qiangGangHuScore));

			// 杠爆 2倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_GangShangHua.class, ScoreCalculateType.TOTAL_MULTI, 2));

			// 海底捞月 2倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HaiDiLaoYue_MeiZhou.class, ScoreCalculateType.TOTAL_MULTI, 2));

			// 特殊牌型：
			// 地胡 20分(地胡收三家)
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DiHu_MeiZhou.class, ScoreCalculateType.TOTAL_ADD, 20, new int[] {PlayType.HU_PING_HU}));

			// 天胡 20分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_TianHu.class, ScoreCalculateType.TOTAL_ADD, 20, new int[] {PlayType.HU_PING_HU}));

			// 十八罗汉 48分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_SiGang.class,
				ScoreCalculateType.TOTAL_ADD,
				48,
				new int[] {PlayType.HU_MEN_QIAN_QING, PlayType.HU_PENG_PENG_HU, PlayType.HU_PING_HU}));

			// 双豪华 24分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ShuangHaoHuaQiDui.class,
				ScoreCalculateType.TOTAL_ADD,
				24,
				new int[] {PlayType.HU_QI_DUI, PlayType.HU_HAO_HUA_QI_DUI}));

			// 三豪华 36分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_SanHaoHuaQiDui.class,
				ScoreCalculateType.TOTAL_ADD,
				36,
				new int[] {PlayType.HU_QI_DUI, PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_SHUANG_HAO_HUA_QI_DUI}));

			createContext.element(HuScoreProcessor.KEY_SCORE_CHECKERS, arrayContext);
		}

		context.element(BattleHelper.KEY_HU_SCORE_PROCESSOR, createContext);
	}

}
