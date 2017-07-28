package com.kodgames.club.service.gmtools;

import com.kodgames.club.testbase.ClubTestBase;
import com.kodgames.corgi.core.service.ServiceContainer;
import limax.zdb.Procedure;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ninlgde on 2017/3/23.
 */
public class ClubGmToolsServiceTest extends ClubTestBase {

    private static Logger logger = LoggerFactory.getLogger(ClubGmToolsServiceTest.class);

    private static class TestData {
        public int roleId;
        public String nickName;
        public String clubName;
        public int agentId;

        public TestData(int id, int aid, String name, String cName){
            this.roleId = id;
            this.agentId = aid;
            this.nickName = name;
            this.clubName = cName;
        }
    }

    private static List<TestData> testDataList = new ArrayList<>();

    static
    {
        testDataList.add(new TestData(13213123,  132131,     "哈哈哈", "哈哈哈的club"));
        testDataList.add(new TestData(141431,    141431,    "副书记", "副书记的club"));
        testDataList.add(new TestData(234243,    234244,    "方式方法", "方式方法的club"));
        testDataList.add(new TestData(523525,    523525,    "人人网若", "人人网若的club"));
        testDataList.add(new TestData(1313123,   131312,    "我让", "我让的club"));
        testDataList.add(new TestData(3643535,   364353,    "沃尔沃若", "沃尔沃若的club"));
        testDataList.add(new TestData(34524234,  345242,    "哈哈哈", "哈哈哈的club"));
        testDataList.add(new TestData(22525234,  225252,     "完任务若", "完任务若的club"));
        testDataList.add(new TestData(242424,    242424,    "根深蒂固", "根深蒂固的club"));
        testDataList.add(new TestData(5252352,   525235,    "深啡网", "深啡网的club"));
        testDataList.add(new TestData(1313123,   131312,    "哈哈哈", "哈哈哈的club"));
    }


    @Test
    public void createNewClub() throws Exception {
        List<Thread> tl = new ArrayList<>();
        for (int i = 0; i < testDataList.size(); i++) {
            TestData testData = testDataList.get(i);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Procedure.call(() -> {
                        ClubGmToolsService service = ServiceContainer.getInstance().getPublicService(ClubGmToolsService.class);
                        service.createNewClub(testData.agentId,
                                testData.roleId, testData.nickName, testData.clubName
                        );
                        return true;
                    });
                }
            });
            tl.add(t);
            t.start();
        }

        for (Thread thread : tl) {
            thread.join();
        }


        Procedure.call(()->{
            table.Clubs.get().walk((k,v) -> {
                logger.info("clubId:{}", k);
                Assert.assertEquals((long)k, v.getClubId());
                logger.info("getAgentId:{}", v.getAgentId());
                logger.info("getClubName:{}", v.getClubName());
                logger.info("getCreateTimestamp:{}", v.getCreateTimestamp());
                logger.info("getStatus:{}", v.getStatus());
                logger.info("creatorName:{}", v.getCreator().getName());
                logger.info("creatorId:{}", v.getCreator().getRoleId());
                logger.info("creatorTitle:{}", v.getCreator().getTitle());
                logger.info("memebers:{}", v.getMembers().toString());
                logger.info("toString:{}", v.toString());
                logger.info("-----------------------------------------------------------------");
                return true;
            });
            return true;
        });
    }

    @Test
    public void getClubsFromAgent() throws Throwable {
        // 基于 createNewClub 运行成功
        Map<Integer, List<Integer>> map = new HashMap<>();
        Map<Integer, List<Integer>> mapA = new HashMap<>();
        Procedure.call(()->{
            table.Clubs.get().walk((k,v) -> {
                logger.info("clubId:{}", k);
                Assert.assertEquals((long)k, v.getClubId());
                int agentId = v.getAgentId();
                if (map.get(agentId) == null) {
                    List<Integer> l = new ArrayList<>();
                    l.add(k);
                    map.put(agentId, l);
                } else {
                    map.get(agentId).add(k);
                }
                return true;
            });

            map.keySet().forEach(k -> {
                ClubGmToolsService service = ServiceContainer.getInstance().getPublicService(ClubGmToolsService.class);
                List<Integer> cs = service.getClubsFromAgent(k);

                mapA.put(k, cs);
            });

            // 这里应该失败的，但是被Procedure.call catch住写到trace.log里了
            Assert.assertTrue(false);
            return true;
        });

        logger.info("table.Clubs: {}", map.toString());
        logger.info("table.Club_agent: {}", mapA.toString());

        Assert.assertEquals(map, mapA);
    }

    @Test
    public void getClubsFromManager() throws Exception {
        // 基于 createNewClub 运行成功
        Map<Integer, List<Integer>> map = new HashMap<>();
        Map<Integer, List<Integer>> mapA = new HashMap<>();
        Procedure.call(()->{
            table.Clubs.get().walk((k,v) -> {
                logger.info("clubId:{}", k);
                Assert.assertEquals((long)k, v.getClubId());
                int managerId = v.getManager().getRoleId();
                if (map.get(managerId) == null) {
                    List<Integer> l = new ArrayList<>();
                    l.add(k);
                    map.put(managerId, l);
                } else {
                    map.get(managerId).add(k);
                }
                return true;
            });

            map.keySet().forEach(k -> {
                ClubGmToolsService service = ServiceContainer.getInstance().getPublicService(ClubGmToolsService.class);
                List<Integer> cs = service.getClubsFromManager(k);

                mapA.put(k, cs);
            });

            // 这里应该失败的，但是被Procedure.call catch住写到trace.log里了
            Assert.assertTrue(false);
            return true;
        });

        logger.info("Clubs: {}", map.toString());
        logger.info("Club_manager: {}", mapA.toString());

        Assert.assertEquals(map, mapA);
    }

    @Test
    public void getAllClub() throws Exception {

    }

    @Test
    public void changeClubStatus() throws Exception {

    }

    @Test
    public void addMemberRoomCard() throws Exception {

    }

}