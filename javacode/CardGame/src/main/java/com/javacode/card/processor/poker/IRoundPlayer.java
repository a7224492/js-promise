package com.javacode.card.processor.poker;

/**
 * Created by jiangzhen on 2018/2/5
 */
public interface IRoundPlayer {
    void setPointInGame(int pointInGame);

    int getPointInGame();

    void setTotalPoint(int totalPoint);

    int getTotalPoint();
}
