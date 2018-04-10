package com.javacode.card.processor.poker.zhajinhua;

import com.javacode.card.bean.CardGroup;
import com.javacode.card.comparator.Comparator;
import com.javacode.card.comparator.ICard;
import com.javacode.card.defines.poker.PlayType;
import com.javacode.card.processor.poker.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.javacode.card.defines.poker.PlayType.*;
import static com.javacode.card.defines.poker.PlayTypeParam.ADD_SCORE;
import static com.javacode.card.defines.poker.PlayTypeParam.*;
import static com.javacode.card.defines.poker.PlayTypeParam.FOLLOW_SCORE;
import static com.javacode.card.defines.poker.PlayTypeResult.*;

/**
 * Created by jiangzhen on 2018/2/1
 */
public class Processor {
    /**
     * 每局一次性操作
     */
    private Map<PlayType, PlayType> roundSequence = new ConcurrentHashMap<>();

    /**
     * 每局的数据
     */
    private IRoundData roundData;

    /**
     * 牌组比较器
     */
    private Comparator comparator;

    public Processor(IRoundData roundData, Comparator comparator) {
        this.roundData = roundData;
        this.comparator = comparator;

        roundSequence.put(LOOK_CARD, LOOK_CARD);
        roundSequence.put(DISCARD, DISCARD);
    }

    public IResult process(int roleId, PlayType playType, Map<String, Object> param) {
        if (isDiscard(roleId)) {
            return null;
        }

        if (isLookCard(roleId) && playType == LOOK_CARD) {
            return null;
        }

        if (roundData.turnRoleId() == roleId) {
            return turnProcess(roleId, playType, param);
        } else {
            if (!roundSequence.containsKey(playType)) {
                return null;
            }

            return nonTurnProcess(roleId, playType, param);
        }
    }

    /**
     * 判断一个玩家是否已经弃牌了
     */
    private boolean isDiscard(int roleId) {
        return roundData.isPlayerHasPlayType(roleId, DISCARD);
    }

    private boolean isLookCard(int roleId) {
        return roundData.isPlayerHasPlayType(roleId, LOOK_CARD);
    }

    /**
     * 发出操作的玩家不是当前正在操作的玩家
     */
    private IResult nonTurnProcess(int roleId, PlayType playType, Map<String, Object> param) {
        switch (playType) {
            case LOOK_CARD:
                return lookCard(roleId, param);
            case DISCARD:
                return discard(roleId, param);
            default:
                return null;
        }
    }

    private IResult discard(int roleId, Map<String, Object> param) {
        roundData.addPlayerPlayType(roleId, DISCARD);
        return new DefaultResult(ResultErrorCode.SUCCESS, roleId, param);
    }

    private IResult lookCard(int roleId, Map<String, Object> param) {
        roundData.addPlayerPlayType(roleId, LOOK_CARD);
        return new DefaultResult(ResultErrorCode.SUCCESS, roleId, param);
    }

    private IResult compareCard(int roleId, PlayType playType, Map<String, Object> param) {
        if (!roundData.isPlayerHasPlayType(roleId, FIND_PLAYER_COMPARE_CARD)) {
            return null;
        }

        int tarRoleId = (int)param.get(FIND_PLAYER_ID.name());
        List<ICard> srcHandCard = roundData.handCardOfPlayer(roleId);
        List<ICard> tarHandCard = roundData.handCardOfPlayer(tarRoleId);

        int compareResult = comparator.compare(new CardGroup(srcHandCard), new CardGroup(tarHandCard));
        if (compareResult > 0) {
            param.put(COMPARE_CARD_WIN_ID.name(), roleId);
            param.put(COMPARE_CARD_LOSE_ID.name(), tarRoleId);
        } else {
            param.put(COMPARE_CARD_WIN_ID.name(), tarRoleId);
            param.put(COMPARE_CARD_LOSE_ID.name(), roleId);
        }

        roundData.removePlayerPlayType(roleId, FIND_PLAYER_COMPARE_CARD);
        return new DefaultResult(ResultErrorCode.SUCCESS, roleId, param);
    }

    private IResult findPlayerCompareCard(int roleId, PlayType playType, Map<String, Object> param) {
        roundData.addPlayerPlayType(roleId, playType);

        List<Integer> compareIdList = roundData.getPlayerIds().stream().filter(_roleId -> !isDiscard(_roleId)).collect(Collectors.toList());
        param.put(COMPARE_ROLE_ID_LIST.name(), compareIdList);
        return new DefaultResult(ResultErrorCode.SUCCESS, roleId, param);
    }

    private IResult followScore(int roleId, PlayType playType, Map<String, Object> param) {
        int followScore = (int)param.get(FOLLOW_SCORE.name());
        if (followScore > roundData.getBottomScore() || followScore < 0) {
            return null;
        }

        _followScore(roleId, followScore);
        return new DefaultResult(ResultErrorCode.SUCCESS, roleId, param);
    }

    private void _followScore(int roleId, int followScore) {
        IRoundPlayer player = roundData.getRoundPlayer(roleId);
        player.setPointInGame(player.getPointInGame()-followScore);
        player.setTotalPoint(player.getTotalPoint()-followScore);
    }

    private IResult addScore(int roleId, PlayType playType, Map<String, Object> param) {
        int addScore = (int)param.get(ADD_SCORE.name());
        if (addScore > roundData.maxAddScore() || addScore < 0) {
            return null;
        }

        _followScore(roleId, addScore);

        roundData.setBottomScore(roundData.getBottomScore()+addScore);
        return new DefaultResult(ResultErrorCode.SUCCESS, roleId, param);
    }

    /**
     * 发出操作的玩家是当前正在操作的玩家
     */
    private IResult turnProcess(int roleId, PlayType playType, Map<String, Object> param) {
        IResult result = null;

        if (roundSequence.containsKey(playType)) {
            result = nonTurnProcess(roleId, playType, param);
            if (result == null) {
                return null;
            }
        } else {
            switch (playType) {
                case ADD_SCORE:
                    result = addScore(roleId, playType, param);
                    break;
                case FOLLOW_SCORE:
                    result = followScore(roleId, playType, param);
                    break;
                case FIND_PLAYER_COMPARE_CARD:
                    result = findPlayerCompareCard(roleId, playType, param);
                    break;
                case COMPARE_CARD:
                    result = compareCard(roleId, playType, param);
                    break;
            }
        }

        roundData.setTurnRoleIdUseNextRoleId();
        return result;
    }
}