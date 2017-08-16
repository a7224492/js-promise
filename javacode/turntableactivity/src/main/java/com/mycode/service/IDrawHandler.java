package com.mycode.service;

/**
 * Created by Administrator on 2017/7/25.
 *
 * 抽奖之后调用的方法
 */
public interface IDrawHandler<P, R> {
    /**
     * 抽奖之后调用的方法
     * @param p 玩家标识
     * @param r 奖品标识
     */
    public void handleDraw(P p, R r);
}
