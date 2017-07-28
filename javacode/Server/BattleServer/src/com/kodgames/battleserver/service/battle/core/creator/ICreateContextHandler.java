package com.kodgames.battleserver.service.battle.core.creator;

import net.sf.json.JSONObject;

/**
 * 通过上下文数据构造实例
 * 
 * 初始化战斗实例时, 所有需要通过创建数据来构造的类, 都要继承这个接口
 */
public interface ICreateContextHandler
{
	void createFromContext(JSONObject context)
		throws Exception;
}
