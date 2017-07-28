package com.kodgames.club.test;

import com.kodgames.club.service.gmtools.ClubGmToolsService;
import com.kodgames.corgi.core.service.ServiceContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ninlgde on 2017/3/18.
 */
public class CreateClubTest {


    private static class TestData {
        public int roleId;
        public String nickName;
        public String clubName;
        public int agentId;

        public TestData(int id, String name, String cName){
            this.roleId = id;
            this.agentId = id;
            this.nickName = name;
            this.clubName = cName;
        }
    }

    private static List<TestData> testDataList = new ArrayList<>();

    private static boolean hasInit = false;

    private static void initData(){
        testDataList.add(new TestData(5001241, "哈哈哈", "哈哈哈的club"));
        testDataList.add(new TestData(5001241, "副书记", "副书记的club"));
        testDataList.add(new TestData(5001241, "方式方法", "方式方法的club"));
        testDataList.add(new TestData(5001241, "人人网若", "人人网若的club"));
        testDataList.add(new TestData(5001241, "我让", "我让的club"));
        testDataList.add(new TestData(3643535, "沃尔沃若", "沃尔沃若的club"));
        testDataList.add(new TestData(34524234, "哈哈哈", "哈哈哈的club"));
        testDataList.add(new TestData(22525234, "完任务若", "完任务若的club"));
        testDataList.add(new TestData(242424, "根深蒂固", "根深蒂固的club"));
        testDataList.add(new TestData(5252352, "深啡网", "深啡网的club"));
        testDataList.add(new TestData(1313123, "哈哈哈", "哈哈哈的club"));
    }

    public static void run() {

        if (!hasInit) {
            initData();
            hasInit = true;
        }

        ClubGmToolsService service = ServiceContainer.getInstance().getPublicService(ClubGmToolsService.class);
//        int clubId = table.Manager.select(1313123);
        // 就执行一次
        testDataList.forEach( testData -> service.createNewClub(testData.agentId,
                testData.roleId, testData.nickName, testData.clubName
        ));
//        if (clubId < ClubConstants.ClubDefault.CLUB_ID_START) {
//        }
    }
}
