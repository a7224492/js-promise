package com.kodgames.club.service.common;

import com.kodgames.club.testbase.ClubTestBase;
import com.kodgames.corgi.core.service.ServiceContainer;
import limax.zdb.Procedure;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.RoomCost;

/**
 * Created by Ninlgde on 2017/3/23.
 */
public class ClubCommonServiceTest extends ClubTestBase {

    private static Logger logger = LoggerFactory.getLogger(ClubCommonServiceTest.class);

    @Test
    public void getRoomCost() throws Exception {
        logger.info("enter roomCost");
        Procedure.call(()->{
            ClubCommonService service= ServiceContainer.getInstance().getPublicService(ClubCommonService.class);
            RoomCost rc = service.getRoomCost(100212);

            logger.info("roomCost, {}", rc.toString());
            logger.info("roomCost, {}", rc.getCost());
            logger.info("roomCost, {}", rc.getPayType());

            return true;
        });

    }

}