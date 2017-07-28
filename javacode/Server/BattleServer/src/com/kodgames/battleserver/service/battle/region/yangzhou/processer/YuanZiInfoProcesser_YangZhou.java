package com.kodgames.battleserver.service.battle.region.yangzhou.processer;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.MahjongHelper;
import com.kodgames.battleserver.service.battle.processer.Processer;
import com.kodgames.battleserver.service.battle.region.yangzhou.Rules_YangZhou;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCPlayStepSYN;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayStepPROTO;

public class YuanZiInfoProcesser_YangZhou extends Processer 
{
	@Override
	public void start()
	{
		this.checkRoleIsInYuanZi();
		this.finish();
	}
	
	/**
	 * 开局检测玩家是否在园子里，如果在的话通知客户端
	 */
	private void checkRoleIsInYuanZi()
	{
		// 获取该房间的园子大小
		BattleRoom roomInfo = battleHelper.getRoomInfo();
		List<Integer> rules = roomInfo.getGameplays();
		final int yuanZi = Rules_YangZhou.getYuanZi(rules);
		final int roomId = roomInfo.getRoomId();
		
		if (yuanZi != 0) 
		{
			roomInfo.forEachPlayers(playerInfo -> {
				int roleId = playerInfo.getRoleId();
				int totalPoint = playerInfo.getTotalPoint();
				if (totalPoint <= -yuanZi)
				{
					int seq = BattleHelper.getInstance().getControllerManager().getProtocolSeq(roomId, roleId);
					BCPlayStepSYN.Builder builder = BCPlayStepSYN.newBuilder();
					builder.setProtocolSeq(seq);
					PlayStepPROTO stepProto = MahjongHelper.step2Proto(new Step(roleId, PlayType.DISPLAY_GENERAL_HEAD_IMAGE_FLAG));
					builder.addSteps(stepProto);

					// 通知玩家进园子
					roomInfo.forEachPlayers(roleInfo -> {
						BattleHelper.getInstance().getControllerManager().sendMessage(roleInfo.getRoleId(), builder.build());
					});

				}
			});
		}
	}
}
