package com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu;

import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.service.ServiceContainer;

/**
 * 牌型算分：庄家
 * 
 * 庄家，1分。（连庄1次，2分；2次，3分；依次累加，庄家输牌不额外扣分）
 */
public class HuSubScoreChecker_LianZhuang extends HuSubScoreChecker
{

	public HuSubScoreChecker_LianZhuang()
	{
		super(PlayType.HU_ZHUANG_JIA);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		RoomService service = ServiceContainer.getInstance().getPublicService(RoomService.class);
		BattleRoom room = service.getRoomInfo(context.getRoomId());
		Macro.AssetTrue(null == room);
		List<BattleBean> games = room.getGames();

		// 获取从本局往前数的连庄次数
		int count = 0;
		for (int index = games.size() - 1; index >= 0; --index)
		{
			BattleBean game = games.get(index);
			if (roleId != game.getZhuang())
				break;

			++count;
		}

		boolean isZhuangJia = count > 0;
		if (isZhuangJia)
		{
			ScorePoint point = new ScorePoint();
			point.setScoreType(super.getScoreType());
			point.setCalcType(super.getModifierType().getValue());
			point.setScoreValue(super.getModiferScore() * count);
			inoutHuContext.scoreData.getPoints().add(point);
		}

		return isZhuangJia;
	}

}
