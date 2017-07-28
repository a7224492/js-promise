package com.kodgames.game;

//import com.kodgames.game.service.security.SecurityGroupConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.util.rsa.RsaConfig;
import com.kodgames.game.common.ServerJson;
import com.kodgames.game.common.rule.RuleManager;
import com.kodgames.game.service.room.RoomIdGenerator;
import com.kodgames.game.service.room.RoomStatusCheckFuncImpl;
import com.kodgames.game.start.GuardService;
//import com.kodgames.game.start.MobileIdTableInit;
import com.kodgames.game.start.NetInitializer;
import com.kodgames.game.start.PurchaseConfig;
import com.kodgames.game.start.ServerConfigInitializer;
import com.kodgames.game.start.TaskInitializer;
import com.kodgames.game.start.WxPromoterConfig;


import limax.xmlconfig.Service;

public class GameServer
{
	private static Logger logger = LoggerFactory.getLogger(GameServer.class);

	public static void main(String[] args)
	{
		Service.addRunAfterEngineStartTask(() -> {
			try
			{
				ServerConfigInitializer.getInstance().init("/game.conf");
				ServerConfigInitializer.getInstance().initProperties("/game.properties");
				RuleManager.getInstance().load(Object.class.getResource("/rules.xml").getPath());
				PurchaseConfig.getInstance().load(Object.class.getResource("/purchase.xml").getPath());
				// 启动GuardService
				GuardService.getInstance().StartService(50000);

				WxPromoterConfig.getInstance().load(Object.class.getResource("/wxpromoter.xml").getPath());
				RsaConfig.getInstance().parse();
				ServerJson.getInstance().parse();
			    
				// 初始化RoomId生成器
				RoomIdGenerator.Init(new RoomStatusCheckFuncImpl());

				NetInitializer.getInstance().init();
				TaskInitializer.getInstance().init();

				// 删除mobile-id表中status != success 的记录
//				MobileIdTableInit.getInstance().walk();
				
			}
			catch (Exception e)
			{
				logger.error("Game server start error : {}", e);
			}
		});

		Service.run(Object.class.getResource("/zdb_config.xml").getPath());
		
	}

}
