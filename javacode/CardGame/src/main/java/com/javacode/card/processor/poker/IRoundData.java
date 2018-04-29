package com.javacode.card.processor.poker;

import com.javacode.card.comparator.ICard;
import com.javacode.card.defines.poker.PlayType;

import java.util.List;

/**
 * Created by jiangzhen on 2018/2/2.
 * function:
 *
 * 表示一局的数据
 */
public interface IRoundData {
    /**
     * 获取某个玩家的手牌
     * @param roleId 玩家id
     * @return 手牌
     */
    List<ICard> handCardOfPlayer(int roleId);

    /**
     * 判断一个玩家是否有playType
     */
    boolean isPlayerHasPlayType(int roleId, PlayType playType);

    /**
     * 给玩家添加一个playtype
     */
    void addPlayerPlayType(int roleId, PlayType playType);

    /**
     * 移除玩家的一个playType
     */
    void removePlayerPlayType(int roleId, PlayType playType);

    /**
     * 当前出牌或者正在选择操作的玩家id
     * @return 玩家id
     */
    int turnRoleId();

    /**
     * 设置当前操作的玩家id
     */
    void setTurnRoleId(int roleId);

    /**
     * 使用当前操作的下一个玩作为turnRoleId
     */
    default void setTurnRoleIdUseNextRoleId()
    {
        setTurnRoleId(nextRoleId(turnRoleId()));
    }

    /**
     * 返回roleId的下一个玩家id
     * @param roleId 当前的玩家id
     * @return 下一个玩家的id
     */
    int nextRoleId(int roleId);

    /**
     * 得到本局的所有玩家id
     */
    List<Integer> getPlayerIds();

    /**
     * 得到本局玩家的数据
     */
    IRoundPlayer getRoundPlayer(int roleId);

    /**
     * 设置本局底分
     */
    void setBottomScore(int bottomScore);

    /**
     * 得到本局底分
     */
    int getBottomScore();

    /**
     * 可以加注的最大数
     */
    int maxAddScore();
}