package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.creator;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.kodgames.battleserver.common.Macro;
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
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_PassBuGang;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_ScoreLimit;
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
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_HunYaoJiu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_HunYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_MenQianQing;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_PengPengHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QiangGangHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QingYaoJiu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_QingYiSe;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_SiGang;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_TianHu;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_ZiMo;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker_ZiYiSe;
import com.kodgames.battleserver.service.battle.processer.BattleProcesser;
import com.kodgames.battleserver.service.battle.processer.CalculateProcesser;
import com.kodgames.battleserver.service.battle.processer.DealCardProcesser;
import com.kodgames.battleserver.service.battle.processer.InitCardPoolProcesser;
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
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu.HuSubScoreChecker_ChiGangGangBao;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu.HuSubScoreChecker_HunYaoJiu_ChaoShan;
import com.kodgames.battleserver.service.battle.region.guangdong.common.zhuang.ZhuangCalculator_GD_JiangYang;
import com.kodgames.battleserver.service.battle.region.guangdong.shantou.score.filter.ScoreTargetFilter_GangBaoQuanBao_ShanTou;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.operation.filter.OperationResultFilter_SanFanQiHu;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.processer.Processer_BettingHorse_ShanWei;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.processer.Processer_ChangeSeat;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.processer.Processer_Check_Zheng;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.battle.BattleScoreCalculator_GuangDong_ShanWei;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.battle.BattleScoreChecker_CheckFengJian;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.battle.BattleScoreChecker_Hua_ShanWei;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_DaSanYuan;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_DaSiXi;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_DiHu_ShanWei;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_DianPao_ShanWei;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_FengKe;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_GangShangHua_ShanWei;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_HaoHuaQiDui_ShanWei;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_JianKe;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_XiaoSanYuan;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_XiaoSiXi;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu.HuSubScoreChecker_YiTiaoLong_ShanWei;
import com.kodgames.battleserver.service.room.PosMapInfo;

public class BattleCreator_GuangDong_ShanWei implements IBattleCreator
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
		return Rules_GuangDong.checkRules(rules, Rules_GuangDong.GAME_TYPE_SHAN_WEI);
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

				if (rules.contains(Rules_GuangDong.GAME_PLAY_NO_WAN) == false)
					supportTypeContext.add(MahjongConstant.CardType.WAN.toString());
				supportTypeContext.add(MahjongConstant.CardType.TIAO.toString());
				supportTypeContext.add(MahjongConstant.CardType.TONG.toString());
				supportTypeContext.add(MahjongConstant.CardType.ZI.toString());
				supportTypeContext.add(MahjongConstant.CardType.HUA.toString());

				subContext.element(InitCardPoolProcesser.KEY_SUPPORT_CARD_TYPES, supportTypeContext);
			}

			result.element(subContext);
		}

		// 发送玩家的正牌（用于客户端在打牌的过程中添加角标）
		result.element(CreateContextHelper.createObject(Processer_Check_Zheng.class));

		// 发牌
		result.element(CreateContextHelper.createObject(DealCardProcesser.class));

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

			// 需要删除的分数
			{
				JSONArray ingoreTypes = CreateContextHelper.createArray();

				ingoreTypes.element(PlayType.HU_QIANG_GANG_HU);
				ingoreTypes.element(PlayType.HU_CHI_GANG_GANG_BAO);

				object.element(Processor_CalculateBuyHorse.KEY_INGORE_TYPE, ingoreTypes);
			}
			result.element(object);
		}

		// 奖马
		if (rules.contains(Rules_GuangDong.BETTING_HOUSE_2) || rules.contains(Rules_GuangDong.BETTING_HOUSE_5) || rules.contains(Rules_GuangDong.BETTING_HOUSE_8))
		{
			JSONObject subContext = CreateContextHelper.createObject(Processer_BettingHorse_ShanWei.class);

			// 三人玩法
			subContext.element(Processor_BettingHorse.KEY_IS_THREE_PLAYER, rules.contains(Rules_GuangDong.GAME_PLAY_THREE_PLAYER));

			// 马跟杠
			subContext.element(Processor_BettingHorse.KEY_APPLY_TO_GANG, rules.contains(Rules_GuangDong.BETTING_HOUSE_WITH_GANG));

			// 奖马加番
			subContext.element(Processer_BettingHorse_ShanWei.KEY_JIANG_MA_JIA_FAN, rules.contains(Rules_GuangDong.BETTING_HOUSE_ADD_FAN));

			if (rules.contains(Rules_GuangDong.FAN_2))
				subContext.element(Processer_BettingHorse_ShanWei.KEY_JIANG_MA_FEN, 2);
			else if (rules.contains(Rules_GuangDong.FAN_4))
				subContext.element(Processer_BettingHorse_ShanWei.KEY_JIANG_MA_FEN, 4);
			else if (rules.contains(Rules_GuangDong.FAN_6))
				subContext.element(Processer_BettingHorse_ShanWei.KEY_JIANG_MA_FEN, 6);

			result.element(subContext);
		}

		// 修改玩家座位
		result.element(CreateContextHelper.createObject(Processer_ChangeSeat.class));

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

				// 风刻的分数（分数写死了，没有写到配置里，因为有两种分数，一种正牌分为2分，副的为1分）
				{
					JSONObject obj = CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_CheckFengJian.class, ScoreCalculateType.TOTAL_ADD, 2);
					obj.element(BattleScoreChecker_CheckFengJian.KEY_NEED_CHECK_TYPE, PlayType.DISPLAY_FENG_JIA_FAN);
					arrayContext.element(obj);
				}

				// 箭刻的分数（分数写死了，没有写到配置里，因为有两种分数，一种正牌分为2分，副的为1分）
				{
					JSONObject obj = CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_CheckFengJian.class, ScoreCalculateType.TOTAL_ADD, 2);
					obj.element(BattleScoreChecker_CheckFengJian.KEY_NEED_CHECK_TYPE, PlayType.DISPLAY_ZI_JIA_FAN);
					arrayContext.element(obj);
				}

				// 花牌的分数（分数写死了，没有写到配置里，因为有两种分数，一种正牌分为2分，副的为1分）
				arrayContext.element(CreateContextHelper.createBattleScoreChecker(BattleScoreChecker_Hua_ShanWei.class, ScoreCalculateType.TOTAL_ADD, 2));

				context.element(BattleScoreProcessor.KEY_POINT_CALCULATORS, arrayContext);
			}

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
			arrayContext.add(CreateContextHelper.createGameScoreFilter(GameScoreFilter.class, MahjongConstant.PlayType.DISPLAY_EX_CARD, true, true));
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

				// 抢杠胡配置到主动点炮类中
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

				proc.element(processor);
			}

			// 暗杠
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_AnGang.class);

				proc.element(processor);
			}

			// 补杠
			{
				JSONObject processor = CreateContextHelper.createObject(OperationChecker_BuGang.class);

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

					// 前置操作必须是补杠，出牌，摸牌
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_BU_GANG_A_CARD));
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_DEAL));
					preFilters.element(CreateContextHelper.createOperationFilter_LastOP(PlayType.OPERATE_PLAY_A_CARD));

					processor.element(OperationCheckerBase.KEY_PRE_FILTER, preFilters);
				}

				{
					JSONArray afterFilters = CreateContextHelper.createArray();

					// 只能点胡 大于等于20积分的牌型（可以是组合起来大于等于20）和抢杠胡
					{
						if (rules.contains(Rules_GuangDong.SAN_FAN_QI_HU))
						{
							JSONObject filter = CreateContextHelper.createObject(OperationResultFilter_SanFanQiHu.class);
							filter.element(OperationResultFilter_ScoreLimit.KEY_VALUE, 3);
							afterFilters.element(filter);
						}
					}

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
	 * 创建战斗分数计算器
	 */
	private void createBattleScoreCalculator(JSONObject createContext, List<Integer> rules)
	{
		JSONObject context = CreateContextHelper.createObject(BattleScoreCalculator_GuangDong_ShanWei.class);

		// 没有封顶
		context.element(BattleScoreCalculator.KEY_MAX_VALUE, 0);
		// 配置需要显示的特殊胡牌图标
		{
			JSONArray array = CreateContextHelper.createArray();

			array.element(PlayType.HU_HAI_DI_LAO_YUE);
			array.element(PlayType.HU_QIANG_GANG_HU);
			array.element(PlayType.HU_GANG_SHANG_HUA);

			context.element(BattleScoreCalculator_GuangDong.KEY_HUTYPSE, array);
		}

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
				context.element(BattleScoreCalculator_GuangDong.KEY_OTHER_MULTI_TYPE, arrayContext);
			}
		}

		createContext.element(BattleHelper.KEY_BATTLE_SCORE_CALCULATOR, context);
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

			// 吃杠杠爆全包
			if (rules.contains(Rules_GuangDong.CHI_GANG_GANG_BAO_QUAN_BAO))
				arrayContext.add(CreateContextHelper.createObject(ScoreTargetFilter_GangBaoQuanBao_ShanTou.class));

			createContext.element(HuScoreProcessor.KEY_SCORE_TARGET_FILTERS, arrayContext);
		}

		// 支持的分数
		{
			JSONArray arrayContext = CreateContextHelper.createArray();

			// 基础牌型A：
			// 1. 平胡（鸡胡）：四坎牌+一对将 +0
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_PinHu.class, ScoreCalculateType.TOTAL_ADD, 0));

			// 2. 大对子/碰碰胡：成牌时有4个三张相同的和1个对 +5
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_PengPengHu.class, ScoreCalculateType.TOTAL_ADD, 5, new int[] {PlayType.HU_PING_HU}));

			// 1、 十三幺：3种序数牌的一、九牌，7种字牌共13张中的12个单张及另外一对作将组成的胡牌 +26
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_ShiSanYao.class, ScoreCalculateType.TOTAL_ADD, 50));

			// 3. 七小对：由7个对子组成胡牌 +10
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuMainScoreChecker_QiDui.class, ScoreCalculateType.TOTAL_ADD, 10, new int[] {PlayType.HU_MEN_QIAN_QING}));

			// 4. 豪华七小对： 7小对中，有4张牌一样的 +15
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HaoHuaQiDui_ShanWei.class, ScoreCalculateType.TOTAL_ADD, 15, new int[] {PlayType.HU_QI_DUI,
				PlayType.HU_MEN_QIAN_QING}));

			// 叠加牌型B：
			// 1、 混一色：字牌及另外的单一花色（筒 条 万）组成 +5
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYiSe.class, ScoreCalculateType.TOTAL_ADD, 5, new int[] {PlayType.HU_PING_HU}));

			// 2、 青一色：由全部14张牌为同一种牌型（筒子、万子、条子） +10
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYiSe.class, ScoreCalculateType.TOTAL_ADD, 10, new int[] {PlayType.HU_PING_HU}));

			// 3、 字一色：全部由字牌组成 +28
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZiYiSe.class, ScoreCalculateType.TOTAL_ADD, 28, new int[] {PlayType.HU_PING_HU}));

			// 4、 十八罗汉：四个杠配合1个对子组成 +36
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_SiGang.class, ScoreCalculateType.TOTAL_ADD, 40, new int[] {PlayType.HU_PENG_PENG_HU, PlayType.HU_PING_HU}));

			// 5、 一九胡：整手牌都有 1，9加字牌组成 +18
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYaoJiu.class, ScoreCalculateType.TOTAL_ADD, 18, new int[] {PlayType.HU_PING_HU}));

			// 6、 清一九：整手牌由纯数字牌并且都是数字1和9组成，不包含字牌 +28
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QingYaoJiu.class, ScoreCalculateType.TOTAL_ADD, 28, new int[] {PlayType.HU_PING_HU}));

			// 7、 混幺九：即一九+混一色，23分由一九和混一色的18+5得来，此处列出只是因为这种牌型有单独的叫法+23
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_HunYaoJiu_ChaoShan.class, ScoreCalculateType.TOTAL_ADD, 23, new int[] {PlayType.HU_PING_HU,
				PlayType.HU_HUN_YAO_JIU, PlayType.HU_HUN_YI_SE}));

			// 8、一条龙，10分。（胡牌牌型中有同花色的1-9各一张的至少九张牌）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_YiTiaoLong_ShanWei.class, ScoreCalculateType.TOTAL_ADD, 10, new int[] {PlayType.HU_PING_HU}));

			// 9、大三元，30分。（胡牌牌型中有箭牌（中发白）的三个刻子或杠）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DaSanYuan.class, ScoreCalculateType.TOTAL_ADD, 30, new int[] {PlayType.HU_PING_HU}));

			// 10、小三元，15分。（胡牌牌型中有箭牌的两个刻子或杠+一个对子，例如123条+555筒+中中中中+发发+白白白）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_XiaoSanYuan.class, ScoreCalculateType.TOTAL_ADD, 15, new int[] {PlayType.HU_PING_HU}));

			// 11、大四喜，40分。（胡牌牌型中有风牌（东南西北）的四个刻子或杠，不再计算碰碰胡得分）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DaSiXi.class, ScoreCalculateType.TOTAL_ADD, 40, new int[] {PlayType.HU_PING_HU, PlayType.HU_PENG_PENG_HU}));

			// 12、小四喜，20分。（胡牌牌型中有风牌的三个刻子或杠+一个对子）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_XiaoSiXi.class, ScoreCalculateType.TOTAL_ADD, 20, new int[] {PlayType.HU_PING_HU}));

			// 其他:
			// 1. 天胡：庄家起手14张就胡牌+50，不管什么牌型
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_TianHu.class, ScoreCalculateType.TOTAL_ADD, 50, new int[] {PlayType.HU_PING_HU, PlayType.HU_HUN_YI_SE,
				PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_QING_YAO_JIU, PlayType.HU_HUN_YAO_JIU, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI,
				PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_SHI_SAN_YAO, PlayType.HU_DA_SAN_YUAN, PlayType.HU_DA_SI_XI, PlayType.HU_XIAO_SAN_YUAN, PlayType.HU_XIAO_SI_XI,
				PlayType.HU_HUN_YAO_JIU_CHAOZHOU, PlayType.HU_MEN_QIAN_QING, PlayType.HU_YI_TIAO_LONG}));

			// 2. 地胡：闲家起手13张，自己摸的第一张牌就胡，或者是在自己摸牌前，就点胡 +50 不管什么牌型
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DiHu_ShanWei.class, ScoreCalculateType.TOTAL_ADD, 50, new int[] {PlayType.HU_PING_HU, PlayType.HU_HUN_YI_SE,
				PlayType.HU_QING_YI_SE, PlayType.HU_ZI_YI_SE, PlayType.HU_QING_YAO_JIU, PlayType.HU_HUN_YAO_JIU, PlayType.HU_SI_GANG, PlayType.HU_PENG_PENG_HU, PlayType.HU_QI_DUI,
				PlayType.HU_HAO_HUA_QI_DUI, PlayType.HU_SHI_SAN_YAO, PlayType.HU_DA_SAN_YUAN, PlayType.HU_DA_SI_XI, PlayType.HU_XIAO_SAN_YUAN, PlayType.HU_XIAO_SI_XI,
				PlayType.HU_HUN_YAO_JIU_CHAOZHOU, PlayType.HU_MEN_QIAN_QING, PlayType.HU_YI_TIAO_LONG}));

			// 4. 风刻分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_FengKe.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 5. 箭刻分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_JianKe.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 6、自摸2分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ZiMo.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 7、门清，2分。（无碰、无吃杠、无明杠，且七小对不算门清）
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_MenQianQing.class, ScoreCalculateType.TOTAL_ADD, 2));

			// 8、点炮1分
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_DianPao_ShanWei.class, ScoreCalculateType.TOTAL_ADD, 1));

			// 3. 杠爆/杠上花：杠牌后，抓到牌就自摸所胡牌型*2
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_GangShangHua_ShanWei.class, ScoreCalculateType.TOTAL_ADD, 1));
			// 吃杠杠爆全包
			if (rules.contains(Rules_GuangDong.CHI_GANG_GANG_BAO_QUAN_BAO))
			{
				arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_ChiGangGangBao.class,
					ScoreCalculateType.TOTAL_MULTI_2ND,
					rules.contains(Rules_GuangDong.GAME_PLAY_THREE_PLAYER) ? 2 : 3));
			}

			// 9、抢杠胡：补杠的时候，这张牌正好放炮所胡牌型*3
			arrayContext.add(CreateContextHelper.createHuScoreChecker(HuSubScoreChecker_QiangGangHu.class,
				ScoreCalculateType.TOTAL_MULTI_2ND,
				rules.contains(Rules_GuangDong.GAME_PLAY_THREE_PLAYER) ? 2 : 3));

			createContext.element(HuScoreProcessor.KEY_SCORE_CHECKERS, arrayContext);
		}

		context.element(BattleHelper.KEY_HU_SCORE_PROCESSOR, createContext);
	}

	/**
	 * 获取玩家座位映射信息
	 */
	@Override
	public PosMapInfo getPlayerPosition(int maxPlayerCount, int posision, List<Integer> rules)
	{
		Macro.AssetFalse(Rules_GuangDong.getArea(rules) == Rules_GuangDong.GAME_TYPE_SHAN_WEI, "玩法错误，不是汕尾玩法");
		if (maxPlayerCount == 2)
		{
			if (posision == 1)
			{
				return new PosMapInfo(1, 3);
			}
			else if (posision == 2)
			{
				return new PosMapInfo(0, 4);
			}
		}

		return null;
	}
}
