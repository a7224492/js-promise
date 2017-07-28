package com.kodgames.client;

import com.kodgames.client.start.GameplaysInitializer;
import com.kodgames.client.start.NetInitializer;
import com.kodgames.client.start.ServerConfigInitializer;
import com.kodgames.client.start.TaskInitializer;
import com.kodgames.corgi.core.util.rsa.RsaConfig;
import limax.xmlconfig.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestClient {
    private static Logger logger = LoggerFactory.getLogger(TestClient.class);

    public static void main(String[] args) throws Exception {
        Service.addRunAfterEngineStartTask(() -> {
            try {
                GameplaysInitializer.getInstance().init("gameplays.xml");

                ServerConfigInitializer.getInstance().init("/client.conf");
                NetInitializer.getInstance().init();

                TaskInitializer.getInstance().init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Service.run("zdb_config.xml");
    }
}
