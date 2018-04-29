package com.mycode;

import com.mycode.activity.DefaultActivity;
import com.mycode.service.IDrawHandler;
import com.mycode.service.ITurnTable;
import io.netty.util.internal.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

abstract class AbstractTurnTable<P, R> extends DefaultActivity implements ITurnTable<P, R> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractTurnTable.class);

	public AbstractTurnTable(long startTime, long endTime) {
		super(startTime, endTime);
	}

	public R drawRewards(P p) {
        int randomInt = ThreadLocalRandom.current().nextInt(1, 10000);
        logger.debug("randomInt={}", randomInt);

        long now = System.currentTimeMillis();
        int rewardId = -1;
        Map<Integer, TurnTableConfig.RewardRatioRange> map = rewardRatioRange(rewardId);
        for (Map.Entry<Integer, TurnTableConfig.RewardRatioRange> entry : map.entrySet())
        {
            TurnTableConfig.RewardRatioRange range = entry.getValue();
            if (range == null)
            {
                logger.info("range is null");
            }
            if (randomInt > range.getLowerBound() && randomInt <= range.getUpperBound())
            {
                rewardId = entry.getKey();
                break;
            }
        }

        return buildReward(rewardId);

//        // 产生的随机数没有在奖品区间（应该是不会发生的）
//        if (rewardId == -1 || hasNoRewardLeftCount(rewardId, now)) {
//            // TODO
//            return playerDrawFails();
//        }
//
//        // 转盘抽奖回复
//        if (turnTableConfig.isReward(rewardId)) {
//            recordPlayerDrawSuccess(p, rewardId);
//            if (isCardReward(rewardId)) {
//                addPlayerCard(p, rewardId);
//            }
//
//            broadCastReward();
//        }
//
//        subRewardLeftCount(rewardId, now);
//        return buildReward(rewardId);
	}

	public R drawRewards(P p, IDrawHandler<P, R> handler) {
        R r = drawRewards(p);
        handler.handleDraw(p, r);
        return r;
    }

    protected abstract Map<Integer,TurnTableConfig.RewardRatioRange> rewardRatioRange(int rewardId);

    /**
     * 构造一个奖品bean
     * @param rewardId
     * @return
     */
    protected abstract R buildReward(int rewardId);

    /**
     * 查询玩家抽到的奖品
     * @param p
     * @return
     */
    public abstract List<R> queryRewards(P p);
}
