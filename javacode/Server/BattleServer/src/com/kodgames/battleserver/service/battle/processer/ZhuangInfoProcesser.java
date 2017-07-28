package com.kodgames.battleserver.service.battle.processer;

import java.util.Comparator;

import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayerStatus;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.service.ServiceContainer;

public class ZhuangInfoProcesser extends Processer
{
	@Override
	public void start()
	{
		// 下发庄家信息
		sendZhuangInfo();

		finish();
	}

	private void sendZhuangInfo()
	{
		BattleRoom battleRoom = battleHelper.getRoomInfo();

		// 计算当前庄家
		battleHelper.getZhuangCalculator().calculateZhuang(battleRoom);

		// 获取当前庄家
		int bankId = context.getZhuang();

		// 创建玩家数据
		battleRoom.forEachPlayers(info ->{
			if (info.getRoleId() == bankId)
				info.setStatus(info.getStatus() | PlayerStatus.ZHUANGJIA);

			PlayerInfo data = new PlayerInfo();
			data.copyFrom(info);
			context.getPlayers().put(info.getRoleId(), data);
			context.getPlayerIds().add(info.getRoleId());
		});

		// 玩家顺序按照座次排序
		context.getPlayerIds().sort(new Comparator<Integer>()
		{
			public int compare(Integer l, Integer r)
			{
				PlayerInfo lPlayer = context.getPlayers().get(l);
				PlayerInfo rPlayer = context.getPlayers().get(r);
				return lPlayer.getPosition() - rPlayer.getPosition();
			}
		});

		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		roomService.syncRoomPlayerInfoToMembers(battleRoom);
	}
}
