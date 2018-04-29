package com.mycode.service;

/**
 * Created by jiangzhen on 2017/7/24.
 */
public interface IMahjongTurnTable extends ITurnTable<Integer, Integer> {
    /**
     * 是否能抽奖
     *
     * @param roleId 玩家id
     * @return
     */
    public boolean canDraw(int roleId);

    /**
     * 判断奖品id是否是正确的奖品id，比如，id<0 || id>6的话，id就不是正确的id
     * @param rewardId
     * @return
     */
    public boolean isRightRewardId(int rewardId);

    /**
     * 产生一个感谢参与的奖品id
     * @return
     */
    public int freeRewardId();
}