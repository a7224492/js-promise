package com.kodgames.battleserver.service.battle.region.yangzhou.processer;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.processer.Processer;
import com.kodgames.battleserver.service.battle.region.yangzhou.Rules_YangZhou;

/**
 * 确定搬子和配子
 * 搬配子玩法：翻开最后一张牌为搬子，搬子加1为配子。若搬子为三条，配子为四条，风牌按照东西南北，中发白单独循环。
 * 
 * @author  王海亮
 * @version  [版本号, 2017年4月19日]
 */
public class Processer_CheckMasterCard_YangZhou extends Processer
{
	@Override
	public void start()
	{
		this.calculateBanZiAndPeiZi();
		this.finish();
	}
	
	/**
	 * 确定搬子和配子
	 * 搬配子玩法：翻开最后一张牌为搬子，搬子加1为配子。若搬子为三条，配子为四条，风牌按照东西南北，中发白单独循环。
	 */
	private void calculateBanZiAndPeiZi() 
	{
		BattleRoom roomInfo = battleHelper.getRoomInfo();
		List<Integer> rules = roomInfo.getGameplays();
		
		//选择了搬配子玩法才需要确定搬子和配子
		if(rules.contains(Rules_YangZhou.GAME_PLAY_BAN_PEI_ZI) || rules.contains(Rules_YangZhou.GAME_PLAY_BAI_BAN_PEI_ZI))
		{
			byte banZi = CardType.ZI.convertToCard(6);
			byte peiZi = CardType.ZI.convertToCard(6);;
			
			// 如果白板不是配子，需生成搬子和配子
			if(!rules.contains(Rules_YangZhou.GAME_PLAY_BAI_BAN_PEI_ZI))
			{
				BattleBean context = roomInfo.getGames().get(roomInfo.getGames().size() - 1);
				int cardSize = context.getCardPool().getCards().size();
				banZi = context.getCardPool().getCards().get(cardSize - 1);
				peiZi = CardType.getNextCard(banZi, true);
			}
			
			// 发送翻出的牌
			Step step = new Step();
			step.setRoleId(context.getZhuang());
			step.setPlayType(PlayType.DISPLAY_DEAL_MASTER_CARD);
			step.getCards().add(banZi);
			step.getCards().add(peiZi);
			controller.addDisplayOperations(step);
			
			context.getCardPool().getMasterCards().add(peiZi);
			
			// 保存鬼牌, 每个人都要记录
			for (int playerId : context.getPlayerIds())
			{
				Step step1 = new Step();
				step1.setRoleId(playerId);
				step1.setPlayType(PlayType.DISPLAY_SHOW_MASTER_CARD);
				step1.getCards().add(banZi);
				step1.getCards().add(peiZi);
				
				context.addCardHeap(step1);
				context.setPlayerMasterCard(playerId, peiZi);
				
				controller.addDisplayOperations(step1);
			}
			
			controller.sendDisplayOperations();
		}
	}
}
