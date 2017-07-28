package com.kodgames.battleserver.service.battle.region.meizhou.common.creator;

import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.core.creator.IBattleCreator;
import com.kodgames.battleserver.service.battle.region.meizhou.Rules_MeiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.creator.BattleCreator_MeiZhou_HeYuan;
import com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.creator.BattleCreator_MeiZhou_HongZhongBao;
import com.kodgames.battleserver.service.battle.region.meizhou.huizhou.creator.BattleCreator_MeiZhou_HuiZhou;
import com.kodgames.battleserver.service.battle.region.meizhou.meixianzhuang.creator.BattleCreator_MeiZhou_MeiXianZhuang;
import com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.creator.BattleCreator_MeiZhou_TuiDaoHu;

import net.sf.json.JSONObject;

public class BattleCreator_MeiZhou implements IBattleCreator
{
	/**
	 * 推到胡的创建类实例
	 */
	private BattleCreator_MeiZhou_TuiDaoHu tuiDaoHuCreator;

	/**
	 * 红中宝的创建类实例
	 */
	private BattleCreator_MeiZhou_HongZhongBao hongZhongBaoCreator;

	/**
	 * 梅县庄的创建类实例
	 */
	private BattleCreator_MeiZhou_MeiXianZhuang meiXianZhuangCreator;

	/**
	 * 河源的创建类实例
	 */
	private BattleCreator_MeiZhou_HeYuan heYuanCreator;

	/**
	 * 惠州的创建类实例
	 */
	private BattleCreator_MeiZhou_HuiZhou huiZhouCreator;

	public BattleCreator_MeiZhou()
	{
		tuiDaoHuCreator = new BattleCreator_MeiZhou_TuiDaoHu();
		hongZhongBaoCreator = new BattleCreator_MeiZhou_HongZhongBao();
		meiXianZhuangCreator = new BattleCreator_MeiZhou_MeiXianZhuang();
		heYuanCreator = new BattleCreator_MeiZhou_HeYuan();
		huiZhouCreator = new BattleCreator_MeiZhou_HuiZhou();
	}

	private IBattleCreator getSubCreator(List<Integer> rules)
	{
		if (rules.contains(Rules_MeiZhou.GAME_TYPE_TUI_DAO_HU))
			return tuiDaoHuCreator;

		if (rules.contains(Rules_MeiZhou.GAME_TYPE_MEI_ZHOU))
			return hongZhongBaoCreator;

		if (rules.contains(Rules_MeiZhou.GAME_TYPE_MEI_XIAN_ZHUANG))
			return meiXianZhuangCreator;

		if (rules.contains(Rules_MeiZhou.GAME_TYPE_HE_YUAN))
			return heYuanCreator;

		if (rules.contains(Rules_MeiZhou.GAME_TYPE_HUI_ZHOU))
			return huiZhouCreator;

		Macro.AssetTrue(true, "创建房间错误，没有所选的玩法");
		return null;
	}

	@Override
	public JSONObject create(List<Integer> rules)
	{
		return getSubCreator(rules).create(rules);
	}

	@Override
	public boolean checkRules(List<Integer> rules)
	{
		return getSubCreator(rules).checkRules(rules);
	}

	@Override
	public int getMaxPlayerSize(List<Integer> rules)
	{
		return getSubCreator(rules).getMaxPlayerSize(rules);
	}
}
