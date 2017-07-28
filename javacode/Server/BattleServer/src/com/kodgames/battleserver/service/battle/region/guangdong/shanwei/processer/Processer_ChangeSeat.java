package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.processer;

import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.processer.Processer;
import com.kodgames.battleserver.service.room.RoomService;

/**
 * 汕尾修改玩家座位
 * 
 * @author 毛建伟
 */
public class Processer_ChangeSeat extends Processer
{

	@Override
	public void start()
	{
		changeSeat();

		finish();
	}

	/**
	 * 修改玩家座位
	 * 
	 * * 上一局庄家和这一局庄家一样或者第一句不进行修改座位
	 * 
	 * * 庄家修改为初始的最低位置，下家依次加一
	 */
	private void changeSeat()
	{
		// 获取房间
		BattleRoom room = battleHelper.getRoomInfo();
		// 如果这一局的庄和上一局的庄家一样就退出
		battleHelper.getZhuangCalculator().calculateNextZhuang(room);
		if (context.getZhuang() == context.getNextZhuang())
			return;
		// 获取当前庄家
		int playerRoleId = context.getNextZhuang();
		int pos = RoomService.POSITION_CREATOR;

		// 循环玩家设置玩家座位信息
		do
		{
			// 设置座位
			PlayerInfo playerInfo = room.getPlayerById(playerRoleId);
			playerInfo.setPosition(pos);
			++pos;
			// 下一玩家
			playerRoleId = context.getNextRoleId(playerRoleId);
		} while (playerRoleId != context.getNextZhuang());
	}
}
