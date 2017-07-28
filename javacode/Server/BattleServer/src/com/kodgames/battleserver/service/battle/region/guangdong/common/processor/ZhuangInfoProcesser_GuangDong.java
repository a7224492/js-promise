package com.kodgames.battleserver.service.battle.region.guangdong.common.processor;

import java.util.Comparator;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayerStatus;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.processer.ZhuangInfoProcesser;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.service.ServiceContainer;

/**
 * 潮汕计算并下发庄家
 * 
 * @author 毛建伟
 */
public class ZhuangInfoProcesser_GuangDong extends ZhuangInfoProcesser
{

	public static final String KEY_NEED_LIANZHUANG = "key_needLianZhuang";

	private boolean needLianZhuang = true;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		if (context.containsKey(KEY_NEED_LIANZHUANG))
			needLianZhuang = CreateContextParser.getBoolean(context, KEY_NEED_LIANZHUANG);
	}

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
		// 判断是否连庄z
		int zhuangCount = 0;
		for (int index = battleRoom.getGames().size() - 2; index >= 0; --index)
		{
			if (battleRoom.getGames().get(index).getZhuang() == bankId)
				++zhuangCount;
			else
				break;
		}

		// 创建玩家数据
		final int countZhuang = zhuangCount;
		battleRoom.forEachPlayers(info -> {
			boolean isZhuang = info.getRoleId() == bankId;
			if (isZhuang)
			{
				info.setStatus(info.getStatus() | PlayerStatus.ZHUANGJIA);
			}

			PlayerInfo data = new PlayerInfo();
			data.copyFrom(info);
			if (isZhuang && needLianZhuang && countZhuang > 0)
			{
				// 下发连庄，设置在data中防止玩家的cardHeap中多出现几个连庄信息
				data.getCards().getCardHeap().add(new Step(bankId, PlayType.DISPLAY_LIAN_ZHUANG, (byte)countZhuang));
			}
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
