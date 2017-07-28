package com.kodgames.club.testbase;

import com.kodgames.club.service.common.ClubCommonServiceTest;
import com.kodgames.club.start.NetInitializer;
import com.kodgames.club.start.ServerConfigInitializer;
import limax.xmlconfig.Service;
import limax.zdb.Procedure;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ninlgde on 2017/3/23.
 */
public class ClubTestBase {

    private static Logger logger = LoggerFactory.getLogger(ClubTestBase.class);

    @Before
    public void setUp() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Service.addRunAfterEngineStartTask(() -> {
                    try {
                        ServerConfigInitializer.getInstance().init("/club.conf");
                        NetInitializer.getInstance().init();
                    }
                    catch (Exception e) {
                        logger.error("Club server start error : {}", e);
                    }
                });

                Service.run(Object.class.getResource("/zdb_config.xml").getPath());
            }
        }).start();
        Thread.sleep(5000);
    }

    @After
    public void tearDown() throws Exception {
        logger.error("stop stop stop stop stop stop");
        Service.stop();
    }

    public void flushZDB(Object o) {
        Procedure.call(() -> {
            List<Integer> ids = new ArrayList<Integer>();
            table.Clubs.get().walk((key, value) -> {
                ids.add(key);
                return true;
            });

            for(int id : ids)
            {
                if (!table.Clubs.delete(id))
                    System.out.println("delete club failed: " + id);
                else
                    System.out.println("delete club success: " + id);
            }
            return true;
        });
    }

}
