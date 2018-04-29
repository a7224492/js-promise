package com.mycode.service;

import com.mycode.activity.IActivity;

import java.util.List;

/**
 * 大转盘接口
 * @author jz
 *
 *	P:玩家标识
 *	R:奖品标识
 */
public interface ITurnTable<P, R> extends IActivity {
	/**
	 * 查询玩家获得的奖品
	 * @param p
	 * @return
	 */
	public List<R> queryRewards(P p);
	
	/**
	 * 玩家抽奖
	 * @return
	 */
	public R drawRewards(P p);

	/**
	 * 带回调的抽奖方法
	 * @param handler
	 * @return
	 */
	public R drawRewards(P p, IDrawHandler<P, R> handler);
}
