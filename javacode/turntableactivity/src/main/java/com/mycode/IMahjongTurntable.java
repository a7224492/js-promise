package com.mycode;

/**
 * Created by jz on 2017/7/18.
 */
public interface IMahjongTurntable extends ITurnTable<Integer, Integer>{

    /**
     * 玩家是否能够抽奖
     * @param roleId 玩家id
     * @return 是否能够抽奖
     */
    public boolean canDraw(int roleId);
}
