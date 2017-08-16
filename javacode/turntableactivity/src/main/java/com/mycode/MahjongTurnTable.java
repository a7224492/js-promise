package com.mycode;

import com.mycode.service.IMahjongTurnTable;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/24.
 */
public class MahjongTurnTable extends AbstractTurnTable<Integer, Integer> implements IMahjongTurnTable {
    public MahjongTurnTable(long startTime, long endTime) {
        super(startTime, endTime);
    }

    protected Map<Integer, TurnTableConfig.RewardRatioRange> rewardRatioRange(int rewardId) {
        return null;
    }

    protected Integer buildReward(int rewardId) {
        return null;
    }

    public List<Integer> queryRewards(Integer integer) {
        return null;
    }

    public boolean canDraw(int roleId) {
        return false;
    }

    public boolean isRightRewardId(int rewardId) {
        return false;
    }

    public int freeRewardId() {
        return 0;
    }
}
