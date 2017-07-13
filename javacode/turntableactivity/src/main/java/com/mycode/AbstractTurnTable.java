package com.mycode;

import com.mycode.activity.DefaultActivity;
import com.mycode.util.TimeUtil;
import io.netty.util.internal.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

abstract class AbstractTurnTable<P, R> extends DefaultActivity implements ITurnTable<P, R> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractTurnTable.class);

    private TurnTableConfig turnTableConfig = new TurnTableConfig();

	public AbstractTurnTable(long startTime, long endTime) {
		super(startTime, endTime);
	}

	public R drawRewards(P p){
        // 得到玩家当前的抽奖次数
        int itemCount = playerItemCount(p);
        if (itemCount <= 0)
        {
            return playerNoEnoughItemCount();
        }

        // 玩家抽奖次数减1
        playerSubItemCount();

        int randomInt = ThreadLocalRandom.current().nextInt(1, 10000);
        logger.debug("randomInt={}", randomInt);

        long now = System.currentTimeMillis();
        int rewardId = -1;
        Map<Integer, TurnTableConfig.RewardRatioRange> map = turnTableConfig.getRewardRationRangeByDay(TimeUtil.zeroTime(now));
        for (Integer id : map.keySet())
        {
            TurnTableConfig.RewardRatioRange range = map.get(id);
            if (range == null)
            {
                logger.info("range is null");
            }
            if (randomInt > range.getLowerBound() && randomInt <= range.getUpperBound())
            {
                rewardId = id;
                break;
            }
        }

        // 产生的随机数没有在奖品区间（应该是不会发生的）
        if (rewardId == -1 || hasNoRewardLeftCount(rewardId, now)) {
            // TODO
            return playerDrawFails();
        }

        // 转盘抽奖回复
        if (turnTableConfig.isReward(rewardId)) {
            recordPlayerDrawSuccess(p, rewardId);
            if (isCardReward(rewardId)) {
                addPlayerCard(p, rewardId);
            }

            broadCastReward();
        }

        subRewardLeftCount(rewardId, now);
        return buildReward(rewardId);
	}

    /**
     * 构造一个奖品bean
     * @param rewardId
     * @return
     */
    protected abstract R buildReward(int rewardId);

    /**
     * 减少奖品库存
     * @param rewardId
     */
    protected abstract void subRewardLeftCount(int rewardId, long now);

    /**
     * 广播玩家抽到的奖品
     */
    protected abstract void broadCastReward();

    /**
     * 给玩家加卡
     * @param p 玩家标识
     * @param rewardId 奖品id
     */
    protected abstract void addPlayerCard(P p, int rewardId);

    /**
     * 判断抽到的是不是奖品
     * @param rewardId
     * @return
     */
    protected abstract boolean isCardReward(int rewardId);

    /**
     * 记录一下玩家抽奖成功
     * @param rewardId
     */
    protected abstract void recordPlayerDrawSuccess(P p, int rewardId);

    /**
     * 查询玩家抽到的奖品
     * @param p
     * @return
     */
    public abstract List<R> queryRewards(P p);

    /**
     * 没有库存了
     * @param rewardId 奖品id
     * @return
     */
    protected abstract boolean hasNoRewardLeftCount(int rewardId, long time);

    /**
     * 玩家抽到了感谢参与
     * @return
     */
    protected abstract R playerDrawFails();

    /**
     * 减少玩家的抽奖次数
     */
    protected abstract void playerSubItemCount();

    /**
     * 获得玩家的抽奖次数
     * @param p 玩家表示
     * @return 玩家的抽奖次数
     */
    protected abstract int playerItemCount(P p);

    /**
     * 玩家没有足够的抽奖次数
     * @return 没有足够的抽奖次数
     */
	protected abstract R playerNoEnoughItemCount();
}
