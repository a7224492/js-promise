package com.kodgames.battleserver.service.battle.region.guangdong.common.creator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.core.creator.IBattleCreator;
import com.kodgames.battleserver.service.battle.region.guangdong.chaoshan.creator.BattleCreator_GuangDong_ChaoShan;
import com.kodgames.battleserver.service.battle.region.guangdong.chaozhou.creator.BattleCreator_GuangDong_ChaoZhou;
import com.kodgames.battleserver.service.battle.region.guangdong.common.Rules_GuangDong;
import com.kodgames.battleserver.service.battle.region.guangdong.guipaichaoshan.creator.BattleCreator_GuangDong_GuiPaiChaoShan;
import com.kodgames.battleserver.service.battle.region.guangdong.huilai.creator.BattleCreator_GuangDong_HuiLai;
import com.kodgames.battleserver.service.battle.region.guangdong.jiexi.creator.BattleCreator_GuangDong_JieXi;
import com.kodgames.battleserver.service.battle.region.guangdong.puning.creator.BattleCreator_GuangDong_PuNing;
import com.kodgames.battleserver.service.battle.region.guangdong.shantou.creator.BattleCreator_GuangDong_ShanTou;
import com.kodgames.battleserver.service.battle.region.guangdong.shanwei.creator.BattleCreator_GuangDong_ShanWei;
import com.kodgames.battleserver.service.battle.region.guangdong.tuidaohu.creator.BattleCreator_GuangDong_TuiDaoHu;
import com.kodgames.battleserver.service.room.PosMapInfo;

/**
 * 创建广东地区玩法的战斗实例的数据
 */
public class BattleCreator_GuangDong implements IBattleCreator
{
	/**
	 * 广东推到胡的创建类实例
	 */
	private BattleCreator_GuangDong_TuiDaoHu tuiDaoHuCreator;

	/**
	 * 潮汕的创建类实例
	 */
	private BattleCreator_GuangDong_ChaoShan chaoShanCreator;

	/**
	 * 鬼牌潮汕的创建类实例
	 */
	private BattleCreator_GuangDong_GuiPaiChaoShan guiPaiChaoShanCreator;

	/**
	 * 潮州的创建类实例
	 */
	private BattleCreator_GuangDong_ChaoZhou chaoZhouCreator;

	/**
	 * 汕尾的创建类实例
	 */
	private BattleCreator_GuangDong_ShanWei shanWeiCreator;

	/**
	 * 汕头的创建类实例
	 */
	private BattleCreator_GuangDong_ShanTou shanTouCreator;

	/**
	 * 普宁的创建实例
	 */
	private BattleCreator_GuangDong_PuNing puNingCreator;

	/**
	 * 惠来的创建实例
	 */
	private BattleCreator_GuangDong_HuiLai huiLaiCreator;

	/**
	 * 揭西的创建实例
	 */
	private BattleCreator_GuangDong_JieXi jieXiCreator;

	/**
	 * 存放地区对应的creator与规则
	 */
	private Map<Integer, IBattleCreator> areaMap = new HashMap<Integer, IBattleCreator>();

	public BattleCreator_GuangDong()
	{
		tuiDaoHuCreator = new BattleCreator_GuangDong_TuiDaoHu();
		chaoShanCreator = new BattleCreator_GuangDong_ChaoShan();
		guiPaiChaoShanCreator = new BattleCreator_GuangDong_GuiPaiChaoShan();
		chaoZhouCreator = new BattleCreator_GuangDong_ChaoZhou();
		shanWeiCreator = new BattleCreator_GuangDong_ShanWei();
		shanTouCreator = new BattleCreator_GuangDong_ShanTou();
		puNingCreator = new BattleCreator_GuangDong_PuNing();
		huiLaiCreator = new BattleCreator_GuangDong_HuiLai();
		jieXiCreator = new BattleCreator_GuangDong_JieXi();

		areaMap.put(Rules_GuangDong.GAME_TYPE_TUI_DAO_HU, tuiDaoHuCreator);
		areaMap.put(Rules_GuangDong.GAME_TYPE_CHAO_SHAN, chaoShanCreator);
		areaMap.put(Rules_GuangDong.GAME_TYPE_GUI_CHAO_SHAN, guiPaiChaoShanCreator);
		areaMap.put(Rules_GuangDong.GAME_TYPE_CHAO_ZHOU, chaoZhouCreator);
		areaMap.put(Rules_GuangDong.GAME_TYPE_SHAN_WEI, shanWeiCreator);
		areaMap.put(Rules_GuangDong.GAME_TYPE_SHAN_TOU, shanTouCreator);
		areaMap.put(Rules_GuangDong.GAME_TYPE_PU_NING, puNingCreator);
		areaMap.put(Rules_GuangDong.GAME_TYPE_HUI_LAI, huiLaiCreator);
		areaMap.put(Rules_GuangDong.GAME_TYPE_JIE_XI, jieXiCreator);
	}

	private IBattleCreator getSubCreator(List<Integer> rules)
	{
		int areaRule = Rules_GuangDong.getArea(rules);
		Macro.AssetTrue(areaRule == 0, "创建房间错误，没有所选的玩法");
		return areaMap.get(areaRule);
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
		// 选择三家拐时，房间默认人数为3
		if (rules.contains(Rules_GuangDong.GAME_PLAY_THREE_PLAYER))
			return MahjongConstant.DEFAULT_ROOM_MEMBER_COUNT - 1;

		return MahjongConstant.DEFAULT_ROOM_MEMBER_COUNT;
	}

	/**
	 * 获取玩家座位映射信息
	 */
	@Override
	public PosMapInfo getPlayerPosition(int maxPlayerCount, int posision, List<Integer> rules)
	{
		return getSubCreator(rules).getPlayerPosition(maxPlayerCount, posision, rules);
	}
}
