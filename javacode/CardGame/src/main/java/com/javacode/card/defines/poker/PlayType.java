package com.javacode.card.defines.poker;

/**
 * Created by jiangzhen on 2018/2/1.
 * function:
 */
public enum PlayType {
    /**
     * 加注
     */
    ADD_SCORE,

    /**
     * 跟注
     */
    FOLLOW_SCORE,

    /**
     * 看牌
     */
    LOOK_CARD,

    /**
     * 查找比牌的玩家
     */
    FIND_PLAYER_COMPARE_CARD,

    /**
     * 比牌
     */
    COMPARE_CARD,

    /**
     * 弃牌
     */
    DISCARD
}
