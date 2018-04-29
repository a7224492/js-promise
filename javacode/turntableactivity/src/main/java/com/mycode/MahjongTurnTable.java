package com.mycode;

import java.util.List;

/**
 * Created by jz on 2017/7/13.
 */
public class MahjongTurnTable extends AbstractTurnTable<Integer, Integer> implements IMahjongTurntable{

    public MahjongTurnTable(long startTime, long endTime) {
        super(startTime, endTime);
    }

    public boolean canDrawRewards(Integer integer) {
        return false;
    }

    protected Integer buildReward(int rewardId) {
        return null;
    }

    protected void subRewardLeftCount(int rewardId, long now) {

    }

    protected void broadCastReward() {

    }

    protected void addPlayerCard(Integer integer, int rewardId) {

    }

    protected boolean isCardReward(int rewardId) {
        return false;
    }

    protected void recordPlayerDrawSuccess(Integer integer, int rewardId) {

    }

    public List<Integer> queryRewards(Integer integer) {
        return null;
    }

    protected boolean hasNoRewardLeftCount(int rewardId, long time) {
        return false;
    }

    protected Integer playerDrawFails() {
        return null;
    }

    protected void playerSubItemCount() {

    }

    protected int playerItemCount(Integer integer) {
        return 0;
    }

    public boolean canDraw(int roleId) {
        // 得到玩家当前的抽奖次数
        int itemCount = playerItemCount(roleId);
        if (itemCount > 0) {
            return true;
        } else {
            return false;
        }
    }
}
