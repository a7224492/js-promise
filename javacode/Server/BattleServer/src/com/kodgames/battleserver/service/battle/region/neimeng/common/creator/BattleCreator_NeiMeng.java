package com.kodgames.battleserver.service.battle.region.neimeng.common.creator;

import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.core.creator.IBattleCreator;
import com.kodgames.battleserver.service.battle.region.neimeng.baotou.creator.BattleCreator_NeiMeng_BaoTou;
import com.kodgames.battleserver.service.battle.region.neimeng.bayannaoer.BattleCreator_NeiMeng_BaYanNaoEr;
import com.kodgames.battleserver.service.battle.region.neimeng.common.Rules_NeiMeng;
import com.kodgames.battleserver.service.battle.region.neimeng.erdos.BattleCreator_NeiMeng_Erdos;
import com.kodgames.battleserver.service.battle.region.neimeng.huhehaote.BattleCreator_NeiMeng_HuHeHaoTe;
import com.kodgames.battleserver.service.battle.region.neimeng.wuhai.creator.BattleCreator_NeiMeng_WuHai;
import com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.creator.BattleCreator_NeiMeng_WuLanChaBu;

import net.sf.json.JSONObject;

/**
 * 创建内蒙地区玩法的战斗实例的数据
 */
public class BattleCreator_NeiMeng implements IBattleCreator
{
	/**
	 * 鄂尔多斯的创建类实例
	 */
	private IBattleCreator erdosCreator = new BattleCreator_NeiMeng_Erdos();

	/**
	 * 包头的创建类实例
	 */
	private IBattleCreator baoTouCreator = new BattleCreator_NeiMeng_BaoTou();

	/**
	 * 巴彦淖尔的创建类实例
	 */
	private IBattleCreator baYanNaoErCreator = new BattleCreator_NeiMeng_BaYanNaoEr();

	/**
	 * 呼和浩特的创建类实例
	 */
	private IBattleCreator huHeHaoTeCreator = new BattleCreator_NeiMeng_HuHeHaoTe();

	/**
	 * 乌兰察布的创建类实例
	 */
	private IBattleCreator wuLanChaBuCreator = new BattleCreator_NeiMeng_WuLanChaBu();

	/**
	 * 乌海的创建类实例
	 */
	private IBattleCreator wuHaiCreator = new BattleCreator_NeiMeng_WuHai();

	private IBattleCreator getSubCreator(List<Integer> rules)
	{
		if (rules.contains(Rules_NeiMeng.GAME_TYPE_ERDOS))
			return erdosCreator;

		if (rules.contains(Rules_NeiMeng.GAME_TYPE_BAOTOU))
			return baoTouCreator;

		if (rules.contains(Rules_NeiMeng.GAME_TYPE_BAYANNAOER))
			return baYanNaoErCreator;

		if (rules.contains(Rules_NeiMeng.GAME_TYPE_HUHEHAOTE))
			return huHeHaoTeCreator;

		if (rules.contains(Rules_NeiMeng.GAME_TYPE_WULANCHABU))
			return wuLanChaBuCreator;

		if (rules.contains(Rules_NeiMeng.GAME_TYPE_WUHAI))
			return wuHaiCreator;

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
