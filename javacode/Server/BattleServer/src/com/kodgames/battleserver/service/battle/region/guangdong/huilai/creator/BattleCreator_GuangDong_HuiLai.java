package com.kodgames.battleserver.service.battle.region.guangdong.huilai.creator;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
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
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_4MasterCard;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_PinHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_QiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker_ShiSanYao;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_GangShangHua;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_HaoHuaQiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_HunYaoJiu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_HunYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_PengPengHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QiangGangHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QingYaoJiu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QingYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_SanHaoHuaQiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_ShuangHaoHuaQiDui;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_TianHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_ZiYiSe;
import com.kodgames.battleserver.service.battle.processer.BattleProcesser;
import com.kodgames.battleserver.service.battle.processer.CalculateProcesser;
import com.kodgames.battleserver.service.battle.processer.DealCardProcesser;
import com.kodgames.battleserver.service.battle.processer.InitCardPoolProcesser;
import com.kodgames.battleserver.service.battle.region.guangdong.chaozhou.score.filter.ScoreTargetFilter_ZiMo_TingScoreLimit_ChaoZhou;
import com.kodgames.battleserver.service.battle.region.guangdong.common.Rules_GuangDong;
import com.kodgames.battleserver.service.battle.region.guangdong.common.operation.AfterOperationProcessor_PlayCard_ChaoShan;
import com.kodgames.battleserver.service.battle.region.guangdong.common.operation.filter.OperationResultFilter_PassHu_ChaoShan;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.DisplayStepProcessor_HideBuyHorseCard;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processer_LianZhuang;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processor_BettingHorse;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processor_CalculateBuyHorse;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processor_CheckMasterCard;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processor_StartBuyHorse;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.ZhuangInfoProcesser_GuangDong;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.battle.BattleScoreCalculator_GuangDong;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.game.GameScoreCalculator_GD;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.game.GameScoreFilter_GD;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu.HuSubScoreChecker_DiHu_ChaoShan;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu.HuSubScoreChecker_HaiDiLaoYue_GuangDong;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu.HuSubScoreChecker_NoMaster;
import com.kodgames.battleserver.service.battle.region.guangdong.common.zhuang.ZhuangCalculator_GD_JiangYang;
import com.kodgames.battleserver.service.battle.region.guangdong.huilai.processer.Processer_GangWithPaiScore;
import com.kodgames.battleserver.service.battle.region.guangdong.huilai.score.battle.BattleScoreCalculator_GuangDong_HuiLai;

/**
 * 潮汕惠来玩法
 */
public class BattleCreator_GuangDong_HuiLai implements IBattleCreator
{
	public JSONObject create(List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleHelper.class.getSimpleName());
		createZhuangCalculator(result, rules);
		createHuCalculateProcessor(result, rules);
		createHuCheckerProcessor(result, rules);
		createBattleScoreCalculator(result, rules);
		createBattleFinishChecker(result, rules);
		createMainProcessor(result, rules);
		createControllerManager(result, rules);
		return result;
	}

	@Override
	public boolean checkRules(List<Integer> rules)
	{
		return Rules_GuangDong.checkRules(rules, Rules_GuangDong.GAME_TYPE_HUI_LAI);
	}

	private void createZhuangCalculator(JSONObject context, List<Integer> rules)
	{
		// 创建庄家计算器
		context.element(BattleHelper.KEY_ZHUANG_CALCULATOR, CreateContextHelper.createObject(ZhuangCalculator_GD_JiangYang.class));
	}

	/**
	 * 创建controllerManager
	 */
	private void createControllerManager(JSONObject context, List<Integer> rules)
	{
		JSONObject obj = CreateContextHelper.createObject(ControllerManager.class);
		{
			JSONArray arrayContext = CreateContextHelper.createArray();
			// 在这里配置需要的处理
			{
				JSONObject hideObj = CreateContextHelper.createObject(DisplayStepProcessor_HideBuyHorseCard.class);
				hideObj.element(ControllerManager.KEY_DISPLAY_STEP_PLAY_TYPE, PlayType.OPERATE_DEAL_FIRST);

				{
					// 配置需要隐藏的type
					JSONArray hideArray = CreateContextHelper.createArray();
					hideArray.element(PlayType.DISPLAY_BUY_HORSE);
					hideArray.element(PlayType.DISPLAY_PUNISH_HORSE);
					hideObj.element(DisplayStepProcessor_HideBuyHorseCard.KEY_HIDE_TYPES, hideArray);
				}

				arrayContext.element(hideObj);
			}

			obj.element(ControllerManager.KEY_DISPLAY_STEP_PROCESSORS, arrayContext);
		}

		context.element(BattleHelper.KEY_CONTROLLER_MANAGER, obj);
	}

	private void createMainProcessor(JSONObject context, List<Integer> rules)
	{
		JSONArray result = CreateContextHelper.createArray();

		// 确定庄家
		result.element(CreateContextHelper.createObject(ZhuangInfoProcesser_GuangDong.class).element(ZhuangInfoProcesser_GuangDong.KEY_NEED_LIANZHUANG,
			rules.contains(Rules_GuangDong.SCORE_LIAN_ZHUANG)));

		// 初始化牌池
		{
			JSONObject subContext = CreateContextHelper.createObject(InitCardPoolProcesser.class);

			// 设置支持什么类型的牌
			{
				JSONArray supportTypeContext = CreateContextHelper.createArray();

				// 无字牌/有字牌
				// if (rules.contains(Rules_GuangDong.GAME_PLAY_NO_WAN) == false)
				supportTypeContext.add(MahjongConstant.CardType.WAN.toString());
				supportTypeContext.add(MahjongConstant.CardType.TIAO.toString());
				supportTypeContext.add(MahjongConstant.CardType.TONG.toString());

				// 带风/不带风
				// if (rules.contains(Rules_GuangDong.GAME_PLAY_NO_ZI) == false)
				supportTypeContext.add(MahjongConstant.CardType.ZI.toString());

				subContext.element(InitCardPoolProcesser.KEY_SUPPORT_CARD_TYPES, supportTypeContext);
			}

			result.element(subContext);
		}

		// 发牌
		result.element(CreateContextHelper.createObject(DealCardProcesser.class));

		// 买马罚马
		if (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2) || rules.contains(Rules_GuangDong.PUNISH_HORSE_1)
			|| rules.contains(Rules_GuangDong.PUNISH_HORSE_2))
		{
			JSONObject object = CreateContextHelper.createObject(Processor_StartBuyHorse.class);
			// 配置翻的马牌数量
			if (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.PUNISH_HORSE_1))
				object.element(Processor_StartBuyHorse.KEY_HORSE_NUM, 1);
			else
				object.element(Processor_StartBuyHorse.KEY_HORSE_NUM, 2);
			// 发送给客户端的类型(买马或者罚马)
			object.element(Processor_StartBuyHorse.KEY_HORSE_TYPE, (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2)) ? PlayType.DISPLAY_BUY_HORSE
				: PlayType.DISPLAY_PUNISH_HORSE);

			// 是否为买马
			result.element(object);
			object.element(Processor_StartBuyHorse.KEY_IS_BUY, rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2));
		}

		// 鬼牌
		if (rules.contains(Rules_GuangDong.MASTER_CARD_HONG_ZHONG) || rules.contains(Rules_GuangDong.MASTER_CARD_BAI_BAN) || rules.contains(Rules_GuangDong.MASTER_CARD_GENERATE))
		{
			JSONObject subContext = CreateContextHelper.createObject(Processor_CheckMasterCard.class);

			// 无鬼牌/红中做鬼/白板做鬼/翻鬼（双鬼）
			if (rules.contains(Rules_GuangDong.MASTER_CARD_HONG_ZHONG))
				subContext.element(Processor_CheckMasterCard.KEY_DEFAULT_MASTER_CARD, MahjongConstant.CardType.ZI.Value() + 4);
			else if (rules.contains(Rules_GuangDong.MASTER_CARD_BAI_BAN))
				subContext.element(Processor_CheckMasterCard.KEY_DEFAULT_MASTER_CARD, MahjongConstant.CardType.ZI.Value() + 6);
			else if (rules.contains(Rules_GuangDong.MASTER_CARD_TOW_MASTER))
				subContext.element(Processor_CheckMasterCard.KEY_GENERATE_MASTER_CARD_COUNT, 2);
			else
				subContext.element(Processor_CheckMasterCard.KEY_GENERATE_MASTER_CARD_COUNT, 1);

			result.element(subContext);
		}

		// 打牌
		result.element(createBattleProcessor(rules));

		// 连庄
		if (rules.contains(Rules_GuangDong.SCORE_LIAN_ZHUANG))
		{
			JSONObject lianZhuangContext = CreateContextHelper.createObject(Processer_LianZhuang.class);
			{
				JSONArray arrayContext = CreateContextHelper.createArray();

				arrayContext.element(PlayType.HU_QIANG_GANG_HU);
				arrayContext.element(PlayType.HU_GANG_SHANG_HUA);
				arrayContext.element(PlayType.HU_HAI_DI_LAO_YUE);
				arrayContext.element(PlayType.DISPLAY_NO_MASTER_CARD);
				arrayContext.element(PlayType.HU_CHI_GANG_GANG_BAO);

				lianZhuangContext.element(Processer_LianZhuang.KEY_FINAL_CALC_TYPES, arrayContext);
			}
			result.element(lianZhuangContext);
		}

		// 买马罚马结算
		if (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2) || rules.contains(Rules_GuangDong.PUNISH_HORSE_1)
			|| rules.contains(Rules_GuangDong.PUNISH_HORSE_2))
		{
			JSONObject object = CreateContextHelper.createObject(Processor_CalculateBuyHorse.class);
			// 是否为买马
			object.element(Processor_CalculateBuyHorse.KEY_IS_BUY, rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2));
			// 三人玩法
			object.element(Processor_CalculateBuyHorse.KEY_IS_THREE_PLAYER, rules.contains(Rules_GuangDong.GAME_PLAY_THREE_PLAYER));

			// 十倍不计分
			object.element(Processor_CalculateBuyHorse.KEY_SCORE_LIMIT_CALCTYPE, ScoreCalculateType.TOTAL_MULTI);
			object.element(Processor_CalculateBuyHorse.KEY_SCORE_LIMIT_VALUE, 10);

			// 需要删除的分数
			{
				JSONArray ingoreTypes = CreateContextHelper.createArray();

				ingoreTypes.element(PlayType.HU_QIANG_GANG_HU);
				ingoreTypes.element(PlayType.HU_CHI_GANG_GANG_BAO);

				object.element(Processor_CalculateBuyHorse.KEY_INGORE_TYPE, ingoreTypes);
			}
			result.element(object);
		}

		if (rules.contains(Rules_GuangDong.GANG_GEN_DI_FEN))
		{
			JSONObject object = CreateContextHelper.createObject(Processer_GangWithPaiScore.class);
			// 设置杠跟底分的计算类型
			object.element(Processer_GangWithPaiScore.KEY_FEN_CALC_TYPE, ScoreCalculateType.TOTAL_MULTI_2ND.getValue());

			// 设置封顶
			if (rules.contains(Rules_GuangDong.SCORE_LIMIT_5))
				object.element(Processer_GangWithPaiScore.KEY_MAX_VALUE, 5);
			else if (rules.contains(Rules_GuangDong.SCORE_LIMIT_10))
				object.element(Processer_GangWithPaiScore.KEY_MAX_VALUE, 10);
			else
				object.element(Processer_GangWithPaiScore.KEY_MAX_VALUE, 0);
			// 设置不需要的分数类型
			{
				JSONArray arrayContext = CreateContextHelper.createArray();
				arrayContext.add(PlayType.DISPLAY_NO_MASTER_CARD);
				arrayContext.add(PlayType.HU_GANG_SHANG_HUA);
				arrayContext.add(PlayType.HU_HAI_DI_LAO_YUE);
				object.element(Processer_GangWithPaiScore.KEY_NOT_WANT_TYPES, arrayContext);
			}

			result.element(object);
		}

		// 奖马
		if (rules.contains(Rules_GuangDong.BETTING_HOUSE_2) || rules.contains(Rules_GuangDong.BETTING_HOUSE_5) || rules.contains(Rules_GuangDong.BETTING_HOUSE_8))
		{
			JSONObject subContext = CreateContextHelper.createObject(Processor_BettingHorse.class);

			// 三人玩法
			subContext.element(Processor_BettingHorse.KEY_IS_THREE_PLAYER, rules.contains(Rules_GuangDong.GAME_PLAY_THREE_PLAYER));

			// 马跟杠
			subContext.element(Processor_BettingHorse.KEY_APPLY_TO_GANG, rules.contains(Rules_GuangDong.BETTING_HOUSE_WITH_GANG));

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
				if (rules.contains(Rules_GuangDong.LIU_JU_SUAN_GANG) == false)
					arrayContext.add(CreateContextHelper.createObject(BattleScoreFilter_RemoveGangWhenHuangZhuang.class));

				context.element(BattleScoreProcessor.KEY_SCORE_FILTERS, arrayContext);
			}

			createContext.element(CalculateProcesser.KEY_BATTLE_SCORE_PROCESSOR, context);
		}

		// 房间分数计算器
		{
			JSONObject context = CreateContextHelper.createObject(GameScoreCalculator_GD.class);
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 设置房间分数统计
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_ZI_MO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_DIAN_PAO, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.HU_DIAN_PAO, false, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_AN_GANG, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.OPERATE_BU_GANG_A_CARD, true, false));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_ALL_HORSE_CARD_COUNT, true, true));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_LIAN_ZHUANG, true, true));
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_FOLLOW_BANKER, true, false));

			{
				JSONArray countArray = CreateContextHelper.createArray();
				// 买马罚马
				if (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2) || rules.contains(Rules_GuangDong.PUNISH_HORSE_1)
					|| rules.contains(Rules_GuangDong.PUNISH_HORSE_2) || rules.contains(Rules_GuangDong.BETTING_HOUSE_2) || rules.contains(Rules_GuangDong.BETTING_HOUSE_5)
					|| rules.contains(Rules_GuangDong.BETTING_HOUSE_8))
				{
					countArray.element(createGameScoreFilter(PlayType.DISPLAY_ALL_HORSE_CARD_COUNT, true, false, true, new int[] {PlayType.DISPLAY_LOSE_HORSE_CARD, PlayType.DISPLAY_WIN_HORSE_CARD,
						PlayType.DISPLAY_BETTING_HOUSE}));
				}
				// 抢杠胡配置到点炮类中
				{
					countArray.element(createGameScoreFilter(PlayType.HU_DIAN_PAO, true, false, false, new int[] {PlayType.HU_QIANG_GANG_HU}));
				}
				// 抢杠胡配置到被动点炮类中
				{
					countArray.element(createGameScoreFilter(PlayType.HU_DIAN_PAO, false, false, false, new int[] {PlayType.HU_QIANG_GANG_HU}));
				}
				// 杠爆，海捞配置到自摸中
				{
					countArray.element(createGameScoreFilter(PlayType.HU_ZI_MO, true, false, false, new int[] {PlayType.HU_GANG_SHANG_HUA, PlayType.HU_HAI_DI_LAO_YUE}));
				}

				context.element(GameScoreCalculator_GD.KEY_NEED_ADD_TRPE_MAP, countArray);
			}

			context.element(GameScoreCalculator_GD.KEY_GAME_SCORE_FILTERS_GD, arrayContext);
			createContext.element(CalculateProcesser.KEY_GAME_SCORE_CALCULATOR, context);
		}

		return createContext;
	}

	/**
	 * 创建潮汕地区的gameScoreFilter
	 */
	private JSONObject createGameScoreFilter(int addToType, boolean addOperation, boolean calculateScorePoint, boolean checkStep, int[] needAddTypes)
	{
		JSONObject gameScoreFilter = CreateContextHelper.createObject();
		JSONArray needAddTypeArray = CreateContextHelper.createArray();

		for (int index = 0; index < needAddTypes.length; ++index)
		{
			needAddTypeArray.element(needAddTypes[index]);
		}

		gameScoreFilter.element(GameScoreFilter_GD.KEY_NEED_ADD_TYPES, needAddTypeArray);
		gameScoreFilter.element(GameScoreFilter_GD.KEY_ADD_TO_TYPE, addToType);
		gameScoreFilter.element(GameScoreFilter_GD.KEY_ADD_TO_TYPE_OPER, addOperation);
		gameScoreFilter.element(GameScoreFilter_GD.KEY_CALCULATE_SCORE_POINT, calculateScorePoint);
		gameScoreFilter.element(GameScoreFilter_GD.KEY_CHECK_STEP, checkStep);
		return gameScoreFilter;
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
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_BU_GANG_A_CARD));
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_DEAL));
					// 选了吃胡前置才有打牌
					if (rules.contains(Rules_GuangDong.HU_DIAN_HU))
						preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_PLAY_A_CARD));

					processor.element(OperationCheckerBase.KEY_PRE_FILTER, preFilters);
				}

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 漏胡, 自摸不算漏胡
					{
						JSONObject filter = CreateContextHelper.createObject(OperationResultFilter_PassHu_ChaoShan.class);
						filter.element(OperationResultFilter_PassHu_ChaoShan.KEY_CHECK_PASS_ZI_MO_HU, true);
						filter.element(OperationResultFilter_PassHu_ChaoShan.KEY_CHECK_PASS_DIAN_HU, true);
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
			if (rules.contains(Rules_GuangDong.SCORE_GEN_ZHUANG))
			{
				JSONArray afterFilters = CreateContextHelper.createArray();
				{
					JSONObject afterProcess = CreateContextHelper.createObject(AfterOperationProcessor_PlayCard_ChaoShan.class);

					afterProcess.element(AfterOperationProcessor_PlayCard_ChaoShan.KEY_CALCULATE_TYPE, ScoreCalculateType.TOTAL_ADD);
					afterProcess.element(AfterOperationProcessor_PlayCard_ChaoShan.KEY_SCORE_VALUE, -1);
					afterProcess.element(AfterOperationProcessor_PlayCard_ChaoShan.KEY_PLAY_TYPE, PlayType.OPERATE_PLAY_A_CARD);
					afterProcess.element(AfterOperationProcessor_PlayCard_ChaoShan.KEY_LIU_JU, false);

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
		JSONObject context = CreateContextHelper.createObject(BattleScoreCalculator_GuangDong_HuiLai.class);

		// 设置封顶
		if (rules.contains(Rules_GuangDong.SCORE_LIMIT_5))
			context.element(BattleScoreCalculator.KEY_MAX_VALUE, 5);
		else if (rules.contains(Rules_GuangDong.SCORE_LIMIT_10))
			context.element(BattleScoreCalculator.KEY_MAX_VALUE, 10);
		else
			context.element(BattleScoreCalculator.KEY_MAX_VALUE, 0);

		// 底分配置
		context.element(BattleScoreCalculator_GuangDong_HuiLai.KEY_BASE_SCORE, 2);

		// 买马罚马不需要删除的类型
		if (rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2) || rules.contains(Rules_GuangDong.PUNISH_HORSE_1)
			|| rules.contains(Rules_GuangDong.PUNISH_HORSE_2))
		{
			// 是否为买马
			boolean isBuy = rules.contains(Rules_GuangDong.BUY_HORSE_1) || rules.contains(Rules_GuangDong.BUY_HORSE_2);
			// 添加需要忽略的类型
			{
				JSONArray arrayContext = CreateContextHelper.createArray();
				arrayContext.add(isBuy ? PlayType.DISPLAY_BUY_HORSE : PlayType.DISPLAY_PUNISH_HORSE);
				arrayContext.add(isBuy ? PlayType.DISPLAY_HU_BUY_HORSE_SCORE : PlayType.DISPLAY_HU_PUNISH_HORSE_SCORE);
				arrayContext.add(isBuy ? PlayType.DISPLAY_BE_HU_BUY_HORSE_SCORE : PlayType.DISPLAY_BE_HU_PUNISH_HORSE_SCORE);
				context.element(BattleScoreCalculator_GuangDong.KEY_INGORE_TYPES, arrayContext);
			}
			// 添加马分的类型
			{
				JSONArray arrayContext = CreateContextHelper.createArray();
				arrayContext.add(isBuy ? PlayType.DISPLAY_BE_HU_BUY_HORSE_SCORE : PlayType.DISPLAY_BE_HU_PUNISH_HORSE_SCORE);
				arrayContext.add(isBuy ? PlayType.DISPLAY_HU_BUY_HORSE_SCORE : PlayType.DISPLAY_HU_PUNISH_HORSE_SCORE);
				context.element(BattleScoreCalculator_GuangDong.KEY_BUY_MA_TYPES, arrayContext);
			}
		}

		// 配置需要显示的特殊胡牌图标
		{
			JSONArray array = CreateContextHelper.createArray();

			array.element(PlayType.HU_HAI_DI_LAO_YUE);
			array.element(PlayType.HU_QIANG_GANG_HU);
			array.element(PlayType.HU_GANG_SHANG_HUA);

			context.element(BattleScoreCalculator_GuangDong.KEY_HUTYPSE, array);
		}
		// 连庄
		if (rules.contains(Rules_GuangDong.SCORE_LIAN_ZHUANG))
		{
			{
				JSONArray arrayContext = CreateContextHelper.createArray();
				arrayContext.add(PlayType.DISPLAY_LIAN_ZHUANG);
				context.element(BattleScoreCalculator_GuangDong.KEY_OTHER_ADD_TYPE, arrayContext);
			}

			{
				JSONArray arrayContext = CreateContextHelper.createArray();
				arrayContext.add(PlayType.HU_GANG_SHANG_HUA);
				arrayContext.add(PlayType.HU_HAI_DI_LAO_YUE);
				arrayContext.add(PlayType.DISPLAY_NO_MASTER_CARD);
				context.element(BattleScoreCalculator_GuangDong.KEY_OTHER_MULTI_TYPE, arrayContext);
			}
		}

		createContext.element(BattleHelper.KEY_BATTLE_SCORE_CALCULATOR, context);
	}

	/**
	 * 设置牌局结束条件
	 */
	private void createBattleFinishChecker(JSONObject context, List<Integer> rules)
	{
		JSONObject result = CreateContextHelper.createObject(BattleFinishChecker_Common.class);

		// 如果选择了奖马，要留够奖马的牌，提前流局
		if (rules.contains(Rules_GuangDong.BETTING_HOUSE_2))
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 2);
		else if (rules.contains(Rules_GuangDong.BETTING_HOUSE_5))
			result.element(BattleFinishChecker_Common.KEY_STAY_CARD_COUNT, 5);
		else if (rules.contains(Rules_GuangDong.BETTING_HOUSE_8))
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
		 * 胡牌牌形检查器
		 */
		{
			JSONArray checkers = CreateContextHelper.createArray();

			// 四鬼胡牌
			if (rules.contains(Rules_GuangDong.MASTER_CARD_FORE_HU))
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

		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_Common.class));

			// 3. 10倍不计分
			if (rules.contains(Rules_GuangDong.HU_ZI_MO_TARGET_SCORE_LIMIT))
			{
				JSONObject limitContext = CreateContextHelper.createObject(ScoreTargetFilter_ZiMo_TingScoreLimit_ChaoZhou.class);
				limitContext.element(ScoreTargetFilter_ZiMo_TingScoreLimit_ChaoZhou.KEY_SCORE_LIMIT, 10);
				arrayContext.add(limitContext);
			}
			createContext.element(HuScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		// 支持的分数
		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 基础牌型A：
			// 1. 平胡（鸡胡）：四坎牌+一对将 +2
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_PinHu.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 2. 大对子/碰碰胡：2倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_PengPengHu.class, ScoreCalculateType.TOTAL_MULTI, 2, new int[] {PlayType.HU_PING_HU}));

			// 3. 七小对：3倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_QiDui.class, ScoreCalculateType.TOTAL_MULTI, 3));

			// 4. 豪华七小对： 5倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HaoHuaQiDui.class, ScoreCalculateType.TOTAL_MULTI, 5, new int[] {PlayType.HU_QI_DUI}));

			// 5. 双豪华七小对：10倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ShuangHaoHuaQiDui.class, ScoreCalculateType.TOTAL_MULTI, 10, new int[] {PlayType.HU_QI_DUI,
				PlayType.HU_HAO_HUA_QI_DUI}));

			// 6. 三豪华七小对：15倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_SanHaoHuaQiDui.class, ScoreCalculateType.TOTAL_MULTI, 15, new int[] {PlayType.HU_QI_DUI,
				PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_SHUANG_HAO_HUA_QI_DUI}));

			// 7、 十三幺：13倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_ShiSanYao.class, ScoreCalculateType.TOTAL_MULTI, 13));

			// 四鬼胡牌
			if (rules.contains(Rules_GuangDong.MASTER_CARD_FORE_HU))
			{
				if (rules.contains(Rules_GuangDong.MASTER_CARD_DOUBLE_SCORE))
					arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_4MasterCard.class, ScoreCalculateType.TOTAL_ADD, 4, new int[] {PlayType.HU_PING_HU,
						PlayType.HU_HUN_YI_SE, PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_QING_YAO_JIU, PlayType.HU_HUN_YAO_JIU, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU,
						PlayType.HU_QI_DUI, PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_SHUANG_HAO_HUA_QI_DUI, PlayType.HU_SAN_HAO_HUA_QI_DUI, PlayType.HU_SHI_SAN_YAO, PlayType.HU_TIAN_HU,
						PlayType.HU_DI_HU}));
				else
					arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_4MasterCard.class, ScoreCalculateType.TOTAL_ADD, 2, new int[] {PlayType.HU_PING_HU,
						PlayType.HU_HUN_YI_SE, PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_QING_YAO_JIU, PlayType.HU_HUN_YAO_JIU, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU,
						PlayType.HU_QI_DUI, PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_SHUANG_HAO_HUA_QI_DUI, PlayType.HU_SAN_HAO_HUA_QI_DUI, PlayType.HU_SHI_SAN_YAO, PlayType.HU_TIAN_HU,
						PlayType.HU_DI_HU}));
			}

			// 叠加牌型B：
			// 1、 混一色：2倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYiSe.class, ScoreCalculateType.TOTAL_MULTI, 2, new int[] {PlayType.HU_PING_HU}));

			// 2、 青一色：4倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYiSe.class, ScoreCalculateType.TOTAL_MULTI, 4, new int[] {PlayType.HU_PING_HU}));

			// 3、 字一色：10倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZiYiSe.class, ScoreCalculateType.TOTAL_MULTI, 10, new int[] {PlayType.HU_PING_HU}));

			// 5、 一九胡：5倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYaoJiu.class, ScoreCalculateType.TOTAL_MULTI, 5, new int[] {PlayType.HU_PING_HU}));

			// 6、 清一九：7倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYaoJiu.class, ScoreCalculateType.TOTAL_MULTI, 7, new int[] {PlayType.HU_PING_HU}));

			// 其他:
			// 1. 天胡：10倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_TianHu.class, ScoreCalculateType.TOTAL_MULTI, 10, new int[] {PlayType.HU_PING_HU, PlayType.HU_HUN_YI_SE,
				PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_QING_YAO_JIU, PlayType.HU_HUN_YAO_JIU, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI,
				PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_SHUANG_HAO_HUA_QI_DUI, PlayType.HU_SAN_HAO_HUA_QI_DUI, PlayType.HU_SHI_SAN_YAO}));

			// 2. 地胡：10倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DiHu_ChaoShan.class, ScoreCalculateType.TOTAL_MULTI, 10, new int[] {PlayType.HU_PING_HU, PlayType.HU_HUN_YI_SE,
				PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_QING_YAO_JIU, PlayType.HU_HUN_YAO_JIU, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI,
				PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_SHUANG_HAO_HUA_QI_DUI, PlayType.HU_SAN_HAO_HUA_QI_DUI, PlayType.HU_SHI_SAN_YAO}));

			// 3. 杠爆/杠上花：杠牌后，抓到牌就自摸所胡牌型*2
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_GangShangHua.class, ScoreCalculateType.TOTAL_MULTI, 2));

			// 4. 抢杠胡：3倍，三人玩法时为2倍
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QiangGangHu.class,
				ScoreCalculateType.TOTAL_MULTI_2ND,
				rules.contains(Rules_GuangDong.GAME_PLAY_THREE_PLAYER) ? 2 : 3));

			// 5. 海底捞月：最后一张牌自摸了所胡牌型*2
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HaiDiLaoYue_GuangDong.class, ScoreCalculateType.TOTAL_MULTI, 2));

			// 无鬼加倍
			if (rules.contains(Rules_GuangDong.MASTER_CARD_NONE_DOUBLE))
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_NoMaster.class, ScoreCalculateType.TOTAL_MULTI, 2));

			createContext.element(HuScoreProcessor.KEY_SCORE_CHECKERS, arrayContext);
		}

		context.element(BattleHelper.KEY_HU_SCORE_PROCESSOR, createContext);
	}
}
