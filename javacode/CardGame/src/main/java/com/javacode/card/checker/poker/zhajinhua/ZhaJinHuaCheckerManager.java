package com.javacode.card.checker.poker.zhajinhua;

import com.javacode.card.bean.CardGroup;
import com.javacode.card.checker.poker.Checker;
import com.javacode.card.comparator.ICard;
import com.javacode.card.comparator.ICardGroup;
import com.javacode.card.defines.poker.CardGroupType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangzhen on 2018/2/2
 */
public class ZhaJinHuaCheckerManager {
    private static ZhaJinHuaCheckerManager instance = null;
    private List<Checker> checkerList = new ArrayList<>();

    public static ZhaJinHuaCheckerManager getInstance() {
        if (instance == null) {
            synchronized (ZhaJinHuaCheckerManager.class) {
                if (instance == null) {
                    instance = new ZhaJinHuaCheckerManager();
                }
            }
        }

        return instance;
    }

    private ZhaJinHuaCheckerManager() {
        checkerList.add(new BaoZiChecker());
        checkerList.add(new TongHuaShunChecker());
        checkerList.add(new TongHuaChecker());
        checkerList.add(new ShunZiChecker());
        checkerList.add(new DuiZiChecker());
        checkerList.add(new SpecialChecker());
    }

    public ICardGroup check(List<ICard> cardList) {
        for (Checker checker : checkerList) {
            ICardGroup cardGroup = checker.check(cardList);
            if (cardGroup != null) {
                return cardGroup;
            }
        }

        return new CardGroup(cardList, CardGroupType.CARD_GROUP_DAN_ZHANG);
    }
}
