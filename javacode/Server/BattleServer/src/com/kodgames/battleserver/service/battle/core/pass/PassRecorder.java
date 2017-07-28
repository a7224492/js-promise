package com.kodgames.battleserver.service.battle.core.pass;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.PassInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;

/**
 * Pass操作的额外处理
 * 
 * 现在的功能:将Pass掉操作的信息保存在getPassInfos中, 在下一次Deal之前不能做相同的操作 TODO : 可能需要重构, 1.可以继续抽象为设置某个操作不能做. 2.通过监听事件取消限定
 */
public class PassRecorder
{
	public void recordPass(BattleBean context, int roleId, List<Step> steps)
	{
		if (steps == null)
			return;

		CardInfo cardInfo = context.getPlayers().get(roleId).getCards();

		// 加入PassInfo
		steps.forEach(step -> {
			// 添加这个限定
			PassInfo passInfo = new PassInfo();
			passInfo.setPlayRound(cardInfo.getPlayRound());
			passInfo.setSourceIndex(context.getRecords().size() - 1);
			passInfo.setPlayType(step.getPlayType());
			passInfo.getCard().addAll(step.getCards());

			cardInfo.getPassInfos().add(passInfo);
		});
	}
}