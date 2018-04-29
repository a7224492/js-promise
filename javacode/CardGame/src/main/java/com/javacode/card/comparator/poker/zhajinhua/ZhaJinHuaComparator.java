package com.javacode.card.comparator.poker.zhajinhua;

import com.javacode.card.checker.poker.Checker;
import com.javacode.card.checker.poker.zhajinhua.ZhaJinHuaCheckerManager;
import com.javacode.card.comparator.Comparator;
import com.javacode.card.comparator.ICard;
import com.javacode.card.comparator.ICardGroup;
import com.javacode.card.defines.poker.CardGroupType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangzhen on 2018/1/30
 */
public class ZhaJinHuaComparator extends Comparator
{
	public ZhaJinHuaComparator() {
	}

	@Override
	protected int _compare(ICardGroup src, ICardGroup tar)
	{
		if (src.getCardGroupType() == CardGroupType.UN_KOWN) {
			src.setCardGroupType(ZhaJinHuaCheckerManager.getInstance().check(src.getCardList()).getCardGroupType());
		}

		if (tar.getCardGroupType() == CardGroupType.UN_KOWN) {
			tar.setCardGroupType(ZhaJinHuaCheckerManager.getInstance().check(tar.getCardList()).getCardGroupType());
		}

		int cmpValue = src.getCardGroupType().v()-tar.getCardGroupType().v();
		if (cmpValue > 0 && cmpValue < 6) {
			return 1;
		} else if (cmpValue == 0) {
			List<ICard> srcCardList = src.getCardList();
			List<ICard> tarCardList = tar.getCardList();

			srcCardList.sort(java.util.Comparator.comparing(ICard::getCompareValue));
			tarCardList.sort(java.util.Comparator.comparing(ICard::getCompareValue));

			for (int i = srcCardList.size()-1; i >= 0; ++i) {
				ICard srcCard = srcCardList.get(i);
				ICard tarCard = tarCardList.get(i);

				if (srcCard.getCompareValue() > tarCard.getCompareValue()) {
					return 1;
				} else if (srcCard.getCompareValue() < tarCard.getCompareValue()) {
					return -1;
				}
			}
		} else {
			return -1;
		}

		return -1;
	}
}
