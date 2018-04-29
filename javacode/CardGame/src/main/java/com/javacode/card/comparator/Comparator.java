package com.javacode.card.comparator;

import com.javacode.card.bean.Card;
import java.util.List;

/**
 * Created by jiangzhen on 2018/1/26
 *
 * 比较两幅牌的大小
 */
public abstract class Comparator
{
	/**
	 * @param src 发出比较的牌
	 * @param tar 被比的牌
	 * @return 1 src>tar 0 src==tar -1 src<tar
	 */
	public int compare(ICardGroup src, ICardGroup tar)
	{
		if (src == null && tar == null)
		{
			return 0;
		}
		else if (src == null)
		{
			return -1;
		}
		else if (tar == null)
		{
			return 1;
		}

		return _compare(src, tar);
	}

	protected abstract int _compare(ICardGroup src, ICardGroup tar);
}