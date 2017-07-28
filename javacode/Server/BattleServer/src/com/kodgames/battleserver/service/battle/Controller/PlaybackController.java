package com.kodgames.battleserver.service.battle.Controller;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleConst;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlaybackConfig;
import com.kodgames.battleserver.service.battle.core.BaseController;
import com.kodgames.battleserver.service.battle.core.MahjongHelper;
import com.kodgames.message.proto.battle.BattleProtoBuf.MatchPlaybackPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.MatchPlaybackPlayerPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.MatchPlaybackStepsPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayStepPROTO;

/**
 * 战局回放
 */
public class PlaybackController extends BaseController
{
	MatchPlaybackPROTO.Builder playbackBuilder = MatchPlaybackPROTO.newBuilder();
	private boolean initialize = false;

	public void processStep(List<Step> steps, boolean rejoin)
	{
		// 数据无效、断线重连信息，不进行回放
		if (rejoin || steps == null || steps.size() <= 0)
			return;

		MatchPlaybackStepsPROTO.Builder stepGroup = MatchPlaybackStepsPROTO.newBuilder();
		for (Step step : steps)
		{
			if (step.getPlayType() == PlayType.OPERATE_DEAL_FIRST && initialize == false)
			{
				initData(stepGroup);
			}

			// 判定是否需要回放该类型操作
			if (PlaybackConfig.enableRecord(step.getPlayType()) == false)
				continue;

			// 添加剩余手牌数量
			if(step.getPlayType() == PlayType.OPERATE_DEAL)
			{
				int leftCardSize = context.getCardPool().getCards().size();
				stepGroup.addSteps(PlayStepPROTO.newBuilder().setPlayType(PlayType.DISPLAY_LAST_CARD_COUNT).setRoleId(BattleConst.INVALID_ROLEID).setPointInGame(leftCardSize).build());
			}
			
			// 添加记录
			stepGroup.addSteps(MahjongHelper.step2Proto(step));
		}

		// 如果没有有效记录，那么直接返回
		if (stepGroup.getStepsCount() <= 0)
			return;

		playbackBuilder.addRecords(stepGroup.build());
	}

	/**
	 * 生成回放数据
	 * 
	 * @return
	 */
	public MatchPlaybackPROTO generatePlaybackData()
	{
		return playbackBuilder.build();
	}

	/**
	 * 构造玩家手牌信息
	 */
	private void initData(MatchPlaybackStepsPROTO.Builder stepGroup)
	{
		context.getPlayers().values().forEach(player -> {

			List<Byte> handCards = new ArrayList<>(player.getCards().getHandCards());
			// 删除发牌
			Step dealStep = context.getPlayerLastRecord(player.getRoleId());
			if (dealStep != null && dealStep.getPlayType() == PlayType.OPERATE_DEAL)
				handCards.remove(Byte.valueOf(dealStep.getCards().get(0)));

			// 添加玩家手牌
			MatchPlaybackPlayerPROTO.Builder playerInfoBuilder = MatchPlaybackPlayerPROTO.newBuilder();
			playerInfoBuilder.setRoleId(player.getRoleId());
			playerInfoBuilder.setHandCards(convert2ByteString(handCards));
			playbackBuilder.addPlayerDatas(playerInfoBuilder.build());
			// 额外操作
			for (Step step : player.getCards().getCardHeap())
			{
				stepGroup.addSteps(MahjongHelper.step2Proto(step));
			}
		});

		initialize = true;
	}
}
