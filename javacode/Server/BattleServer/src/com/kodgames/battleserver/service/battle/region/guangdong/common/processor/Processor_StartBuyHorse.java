package com.kodgames.battleserver.service.battle.region.guangdong.common.processor;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.processer.Processer;

/**
 * 潮汕买马罚马
 */
public class Processor_StartBuyHorse extends Processer
{
	/**
	 * 配置定义马的数量
	 */
	public static final String KEY_HORSE_NUM = "Key_horseNum";

	/**
	 * 配置是否为买马
	 */
	public static final String KEY_IS_BUY = "Key_isBuy";

	/**
	 * 配置马的type
	 */
	public static final String KEY_HORSE_TYPE = "Key_horseType";

	/**
	 * 马的数量
	 */
	private int horseNum = 0;

	/**
	 * 是否为买马 true为买马，false为罚马
	 */
	private boolean isBuy = false;

	/**
	 * 马的type
	 */
	private int horseType = 0;

	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);
		horseNum = CreateContextParser.getInt(context, KEY_HORSE_NUM);
		isBuy = CreateContextParser.getBoolean(context, KEY_IS_BUY);
		horseType = CreateContextParser.getInt(context, KEY_HORSE_TYPE);
	}

	@Override
	public void start()
	{
		checkAndSend();

		finish();
	}

	/**
	 * 奖马罚马
	 */
	private void checkAndSend()
	{
		// 获取庄家信息
		PlayerInfo playerInfo = context.getPlayerById(context.getZhuang());
		do
		{
			int partHorseNum = horseNum;
			// 翻到的马牌的list
			List<Byte> horseCardList = new ArrayList<Byte>();
			// 开始翻马
			while (partHorseNum-- > 0)
			{
				horseCardList.add(context.getCardPool().getCards().remove(0));
			}
			// 创建一个step
			Step step = new Step(playerInfo.getRoleId(), horseType, horseCardList);
			// 添加到cardHeap中，复牌用
			context.addCardHeap(step);
			// 马牌转换为-1（不让客户端看到）
			for (int i = 0; i < step.getCards().size(); ++i)
			{
				step.getCards().set(i, (byte)-1);
			}
			// 添加到发送列表
			controller.addDisplayOperations(step);
			// 如果是罚马，就退出循环，罚马只有庄家翻马
			if (!isBuy)
				break;
			// 获取下一个玩家
			playerInfo = context.getPlayerById(context.getNextRoleId(playerInfo.getRoleId()));

			// 如果玩家id等于庄家id就退出循环，证明四个人循环了一遍
		} while (playerInfo.getRoleId() != context.getZhuang());

		// 添加买马结束消息
		controller.addDisplayOperations(new Step(context.getZhuang(), PlayType.DISPLAY_HORSE_END));
		// 发送消息
		controller.sendDisplayOperations();
	}

}
