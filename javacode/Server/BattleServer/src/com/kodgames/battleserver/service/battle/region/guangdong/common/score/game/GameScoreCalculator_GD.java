package com.kodgames.battleserver.service.battle.region.guangdong.common.score.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.common.xbean.GameScore;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreCalculator;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreFilter;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;

public class GameScoreCalculator_GD extends GameScoreCalculator
{
	public static final String KEY_GAME_SCORE_FILTERS_GD = "gameScoreFilter_GD";
	public static final String KEY_NEED_ADD_TRPE_MAP = "key_needAddTypeMap";

	private List<GameScoreFilter> gameScoreFilters = new ArrayList<>();

	/**
	 * 这个是用来做添加一些现有玩法不能实现的房间结算功能 key为需要统计的分数类型
	 */
	private Map<GameScoreFilter_Key, GameScoreFilter_GD> needAddTypesMap = new HashMap<GameScoreFilter_Key, GameScoreFilter_GD>();

	public static GameScoreCalculator create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		GameScoreCalculator instance = CreateContextHelper.instantiateClass(context, GameScoreCalculator.class);
		instance.createFromContext(context);
		return instance;
	}

	public void createFromContext(JSONObject context)
		throws Exception
	{
		// 解析参数
		for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_GAME_SCORE_FILTERS_GD))
			gameScoreFilters.add(GameScoreFilter.create(subContext));
		// 配置需要添加的分数类型
		if (context.containsKey(KEY_NEED_ADD_TRPE_MAP))
		{
			for (JSONObject obj : CreateContextParser.getJSONArray(context, KEY_NEED_ADD_TRPE_MAP))
			{
				GameScoreFilter_GD element = new GameScoreFilter_GD();
				element.createFromContext(obj);
				// 添加到Map中
				needAddTypesMap.put(new GameScoreFilter_Key(element.addToType, element.addOperation), element);
			}
		}
	}

	@Override
	public void calculate(PlayerInfo player, PlayerMatchResultPROTO.Builder playerMatchResult)
	{
		// 如果玩家得分为0, 初始化得分
		if (player.getCards().getGameScore().size() == 0)
			gameScoreFilters.forEach((filter) -> {
				GameScore gameScore = new GameScore();
				gameScore.setScoreType(filter.scoreType);
				gameScore.setAddOperation(filter.addOperation);
				gameScore.setCalculateScorePoint(filter.calculateScorePoint);
				player.getCards().getGameScore().add(gameScore);
			});
		// 添加其他统计，最后会删除
		needAddTypesMap.values().forEach(filter -> {
			// 不是统计step中的
			if (!filter.checkStep)
			{
				// 每个计分类型都要统计
				filter.needAddTypes.forEach(type -> {
					GameScore gameScore = new GameScore();
					gameScore.setScoreType(type);
					gameScore.setAddOperation(filter.addOperation);
					gameScore.setCalculateScorePoint(filter.calculateScorePoint);
					player.getCards().getGameScore().add(gameScore);
				});
			}
		});

		// 检查playerMatchResult并添加到玩家对应的gameScore中去
		checkMatchResult(player, playerMatchResult);

		// 检查step中的统计次数
		checkStep(player, player.getCards().getGameScore());

		// 检查分数中的统计
		checkScore(player, player.getCards().getGameScore());

		// 删除额外添加的计分项
		player.getCards().getGameScore().removeIf(gameScore -> {
			// 是否为额外添加的计分项
			for (GameScoreFilter_GD filter : needAddTypesMap.values())
			{
				// 如果不是检查step并且needAddTypes中包含这个类型，就删除
				if (!filter.checkStep && filter.needAddTypes.contains(gameScore.getScoreType()))
				{
					return true;
				}
			}
			// 不删除
			return false;
		});
	}

	/**
	 * 循环所有分数统计到玩家的gameScore中去，累计计算得分
	 * 
	 * @param player
	 * @param playerMatchResult
	 */
	protected void checkMatchResult(PlayerInfo player, PlayerMatchResultPROTO.Builder playerMatchResult)
	{
		// 累计计算得分
		for (ResultEventPROTO.Builder event : playerMatchResult.getEventsBuilderList())
		{
			for (GameScore gameScore : player.getCards().getGameScore())
			{
				// 如果操作不相同, 就不用检测了.
				if (gameScore.getAddOperation() != event.getAddOperation())
					continue;

				// 计算花牌的房间统计（这里是需要统计的玩家才会统计）
				if (gameScore.getScoreType() == PlayType.DISPLAY_EX_CARD)
				{
					gameScore.setTimes(player.getCards().getExCards().size());
					continue;
				}

				// 统计主分数
				if (gameScore.getScoreType() == event.getScore().getType())
				{
					if (gameScore.getCalculateScorePoint())
						gameScore.setTimes(gameScore.getTimes() + (event.getCombinedTimes() + 1) * event.getScore().getPoint());
					else
						gameScore.setTimes(gameScore.getTimes() + (event.getCombinedTimes() + 1));
				}

				// 统计子分数
				for (ResultScorePROTO.Builder eventSubScore : event.getSubScoresBuilderList())
				{
					// 与主分数相同的不再重复统计
					if (eventSubScore.getType() == event.getScore().getType())
						continue;

					// 检查自分数
					if (gameScore.getScoreType() == eventSubScore.getType())
					{
						if (gameScore.getCalculateScorePoint())
							gameScore.setTimes(gameScore.getTimes() + (event.getCombinedTimes() + 1) * eventSubScore.getPoint());
						else
							gameScore.setTimes(gameScore.getTimes() + (event.getCombinedTimes() + 1));
					}
				}
			}
		}
	}

	/**
	 * 检查step中的统计，默认统计的时候统计step中的cards的size
	 * 
	 * @param player
	 * @param gameScores
	 */
	protected void checkStep(PlayerInfo player, List<GameScore> gameScores)
	{
		// 检查Step中的统计
		for (GameScoreFilter_Key key : needAddTypesMap.keySet())
		{
			// 是否检查step
			if (needAddTypesMap.get(key).checkStep)
			{
				// 获取这个filter
				GameScoreFilter_GD filter = needAddTypesMap.get(key);
				// 初始化次数
				int count = 0;
				// 统计step的cards的size()
				for (Step step : player.getCards().getCardHeap())
				{
					if (filter.needAddTypes.contains(step.getPlayType()))
						count += step.getCards().size();
				}

				// 添加到需要添加的类型中
				for (GameScore gameScore : player.getCards().getGameScore())
				{
					if (gameScore.getScoreType() == key.type)
						gameScore.setTimes(gameScore.getTimes() + count);
				}
			}
		}
	}

	/**
	 * 检查分数中的额外加分项，并添加到某一统计项目中
	 * 
	 * @param player
	 * @param gameScores
	 */
	protected void checkScore(PlayerInfo player, List<GameScore> gameScores)
	{
		// 添加额外的计分项
		for (GameScore gameScrore : player.getCards().getGameScore())
		{
			GameScoreFilter_GD filter = needAddTypesMap.get(new GameScoreFilter_Key(gameScrore.getScoreType(), gameScrore.getAddOperation()));
			if (filter == null)
				continue;
			if (!filter.checkStep)
			{
				for (GameScore score : player.getCards().getGameScore())
				{
					if (filter.addOperation == score.getAddOperation() && score.getAddOperation() == gameScrore.getAddOperation())
					{
						// 判断是否加过
						boolean hasAdd = false;
						for (int type : filter.needAddTypes)
						{
							if (type == score.getScoreType() && score.getTimes() > 0)
							{
								gameScrore.setTimes(gameScrore.getTimes() + score.getTimes());
								// 不是统计分数的时候只统计一次
								if (!score.getCalculateScorePoint())
									hasAdd = true;
							}
						}
						// 是否已经加过
						if (hasAdd)
							break;
					}
				}
			}
		}
	}

	/**
	 * 辅助类，用来当needAddTypesMap的key
	 */
	class GameScoreFilter_Key
	{
		// 类型
		public int type;
		// 是否为加分
		public boolean addOperation;

		// 构造函数
		public GameScoreFilter_Key(int type, boolean addOperation)
		{
			this.type = type;
			this.addOperation = addOperation;
		}

		/**
		 * 写hashCode是为了在hashMap中获取，只要两个对象的hash值相等，则在hashMap中的位置也相等
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + (addOperation ? 1231 : 1237);
			result = prime * result + type;
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GameScoreFilter_Key other = (GameScoreFilter_Key)obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (addOperation != other.addOperation)
				return false;
			if (type != other.type)
				return false;
			return true;
		}

		private GameScoreCalculator_GD getOuterType()
		{
			return GameScoreCalculator_GD.this;
		}
	}
}
