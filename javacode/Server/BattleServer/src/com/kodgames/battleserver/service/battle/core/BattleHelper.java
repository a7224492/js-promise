package com.kodgames.battleserver.service.battle.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.common.ThreadLocolVariable;
import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.finish.BattleFinishChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreCalculator;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.core.zhuang.ZhuangCalculator;
import com.kodgames.battleserver.service.battle.processer.Processer;
import com.kodgames.message.proto.battle.BattleProtoBuf.MatchPlaybackPROTO;

/*
 *  每一个房间拥有一个BattleHelper,处理和战斗相关的操作
 */
public class BattleHelper
{
	public static final String KEY_CONTROLLER_MANAGER = "controllerManager";
	public static final String KEY_ZHUANG_CALCULATOR = "zhuangCalculator";
	public static final String KEY_HU_CHECK_PROCESSOR = "huCheckProcessor";
	public static final String KEY_HU_SCORE_PROCESSOR = "huScoreProcessor";
	public static final String KEY_BATTLE_SCORE_CALCULATOR = "battleScoreCalculator";
	public static final String KEY_BATTLE_FINISH_CHECKER = "battleFinishChecker";

	static Logger logger = LoggerFactory.getLogger(BattleHelper.class);

	private ZhuangCalculator zhuangCalculator;
	private HuChecker huChecker;
	private HuScoreProcessor huScoreProcessor;
	private BattleScoreCalculator battleScoreCalculator;
	private BattleFinishChecker battleFinishChecker;

	private Processer currentProcesser;
	private ControllerManager controllerManager;
	private BattleRoom battleRoom;

	// CardHeap是否隐藏真实牌子发送给其他人
	public static String key_hideCardHeapCards2Other = "hideCardHeapCards2Other";
	private List<Integer> hideCardHeapCards2Other = new ArrayList<>();

	public static BattleHelper getInstance()
	{
		BattleHelper instance = (BattleHelper)ThreadLocolVariable.Get();
		Macro.AssetFalse(instance != null);
		return instance;
	}

	public void setCurrentInstance()
	{
		Macro.AssetFalse(ThreadLocolVariable.Get() == null);
		ThreadLocolVariable.Set(this);
	}

	public void resetCurrentInstance()
	{
		Macro.AssetFalse(ThreadLocolVariable.Get() != null);
		ThreadLocolVariable.Set(null);
	}

	public BattleHelper(BattleRoom battleRoom)
	{
		this.battleRoom = battleRoom;
	}

	public ZhuangCalculator getZhuangCalculator()
	{
		return this.zhuangCalculator;
	}

	public HuChecker getHuCheckProcessor()
	{
		return this.huChecker;
	}

	public HuScoreProcessor getHuScoreProcessor()
	{
		return this.huScoreProcessor;
	}

	public BattleScoreCalculator getBattleScoreCalculator()
	{
		return this.battleScoreCalculator;
	}

	public BattleFinishChecker getBattleFinishChecker()
	{
		return this.battleFinishChecker;
	}

	public ControllerManager getControllerManager()
	{
		return controllerManager;
	}

	public BattleRoom getRoomInfo()
	{
		return battleRoom;
	}

	public BattleBean getBattleBean()
	{
		List<BattleBean> battles = getRoomInfo().getGames();
		if (battles.size() <= 0)
			return null;
		else
			return battles.get(battles.size() - 1);
	}

	public boolean hideCardHeapCards(int playType)
	{
		return hideCardHeapCards2Other.contains(playType);
	}

	public boolean isRunning()
	{
		BattleBean bean = getBattleBean();
		return bean != null && bean.getIsRunning();
	}

	public void startBattle(JSONObject createContext)
	{
		logger.info("BattleHelper : startBattle {} ", createContext.toString());

		BattleBean lastBattle = getBattleBean();
		if (Macro.AssetTrue(lastBattle != null && lastBattle.getIsRunning()))
			return;

		// 新的一局游戏
		BattleBean battleBean = new BattleBean();
		battleBean.setRoomId(battleRoom.getRoomId());
		// 设置开始时间
		battleBean.setStartTime(new Date().getTime());
		// 设置庄家id
		battleBean.setIsRunning(true);
		battleBean.getGameRules().addAll(battleRoom.getGameplays());
		getRoomInfo().getGames().add(battleBean);

		// 加入处理集
		initBattle(createContext);

		// 开始游戏
		currentProcesser.start();
	}

	// 复牌操作
	public void rejoin(int roleId)
	{
		BattleBean battleBean = getBattleBean();
		if (battleBean == null || !battleBean.getIsRunning())
			return;

		if (!battleBean.getPlayerIds().contains(roleId))
			return;

		resetProcesser().rejoin(roleId);
	}

	// 打牌操作
	public boolean processStep(int roleId, int playType, byte[] cards)
	{
		BattleBean battleBean = getBattleBean();
		if (battleBean == null || !battleBean.getIsRunning())
			return false;

		if (!battleBean.getPlayerIds().contains(roleId))
			return false;

		return resetProcesser().processStep(roleId, playType, cards);
	}

	private void initBattle(JSONObject createContext)
	{
		currentProcesser = null;

		// 创建战斗
		try
		{
			controllerManager = ControllerManager.create(CreateContextParser.getJSONObject(createContext, KEY_CONTROLLER_MANAGER));
			
			createProcesser(createContext);
			
			zhuangCalculator = ZhuangCalculator.create(CreateContextParser.getJSONObject(createContext, KEY_ZHUANG_CALCULATOR));
			huChecker = HuChecker.create(CreateContextParser.getJSONObject(createContext, KEY_HU_CHECK_PROCESSOR));
			huScoreProcessor = HuScoreProcessor.create(CreateContextParser.getJSONObject(createContext, KEY_HU_SCORE_PROCESSOR));
			battleScoreCalculator = BattleScoreCalculator.create(CreateContextParser.getJSONObject(createContext, KEY_BATTLE_SCORE_CALCULATOR));
			battleFinishChecker = BattleFinishChecker.create(CreateContextParser.getJSONObject(createContext, KEY_BATTLE_FINISH_CHECKER));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("BattleHelper Init Error, RoomId : {}", battleRoom.getRoomId());
		}
	}

	private void createProcesser(JSONObject createContext)
		throws Exception
	{
		if (createContext == null)
			return;

		// 解析需要隐藏牌值后发送给其他玩家的CardHeap类型
		if (createContext.containsKey(key_hideCardHeapCards2Other))
		{
			int typeCount = CreateContextParser.getArraySize(createContext, key_hideCardHeapCards2Other);
			for (int i = 0; i < typeCount; ++i)
				hideCardHeapCards2Other.add(CreateContextParser.getIntInArray(createContext, key_hideCardHeapCards2Other, i));
		}

		// Processor.
		JSONArray processers = createContext.getJSONArray(MahjongConstant.JSON_PROC);
		for (int i = 0; i < processers.size(); i++)
		{
			JSONObject processer = processers.getJSONObject(i);
			Processer instance = Processer.create(processer);
			instance.createFromContext(processer);
			addProcesser(instance);
		}
	}

	private void addProcesser(Processer processer)
	{
		if (currentProcesser == null)
			currentProcesser = processer;
		else
		{
			Processer lastProcess = currentProcesser;
			while (lastProcess.hasNextProcesser())
				lastProcess = lastProcess.nextProcesser();

			lastProcess.setNextProcesser(processer);
		}
	}

	private Processer resetProcesser()
	{
		while (currentProcesser.isFinished())
			currentProcesser = currentProcesser.nextProcesser();

		return currentProcesser;
	}

	public boolean enableMutilHu()
	{
		return getBattleFinishChecker().enableMutilHu();
	}

	public MatchPlaybackPROTO generatePlaybackData()
	{
		return controllerManager.getPlayback().generatePlaybackData();
	}

	public int getPlayCardCount()
	{
		return getBattleBean().getCardPool().getPlayCardCount();
	}
}
