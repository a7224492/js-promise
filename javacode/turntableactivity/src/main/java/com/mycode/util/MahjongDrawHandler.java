package com.mycode.util;

import com.mycode.service.IDrawHandler;

/**
 * Created by jiangzhen on 2017/7/25
 */
public class MahjongDrawHandler implements IDrawHandler<Integer, Integer> {
    public void handleDraw(Integer roleId, Integer rewardId) {
        // TODO 减少玩家的抽奖次数
    }
}
