package com.kodgames.club.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ninlgde on 2017/3/18.
 */
public class LogAllClub {
//
//    private static class TestData {
//        public int roleId;
//        public String nickName;
//        public String clubName;
//
//        public TestData(int id, String name, String cName){
//            this.roleId = id;
//            this.nickName = name;
//            this.clubName = cName;
//        }
//    }
//
//    private static List<TestData> testDataList = new ArrayList<>();
//
//    private static void initData(){
//        testDataList.add(new TestData(1313123, "哈哈哈", "哈哈哈的club"));
//        testDataList.add(new TestData(47289347, "副书记", "副书记的club"));
//        testDataList.add(new TestData(242424, "方式方法", "方式方法的club"));
//        testDataList.add(new TestData(2342424, "人人网若", "人人网若的club"));
//        testDataList.add(new TestData(534535, "我让", "我让的club"));
//        testDataList.add(new TestData(3643535, "沃尔沃若", "沃尔沃若的club"));
//        testDataList.add(new TestData(34524234, "哈哈哈", "哈哈哈的club"));
//        testDataList.add(new TestData(22525234, "完任务若", "完任务若的club"));
//        testDataList.add(new TestData(242424, "根深蒂固", "根深蒂固的club"));
//        testDataList.add(new TestData(5252352, "深啡网", "深啡网的club"));
//        testDataList.add(new TestData(1313123, "哈哈哈", "哈哈哈的club"));
//    }


    private static final Logger logger = LoggerFactory.getLogger(LogAllClub.class);

    public static void test() {
        table.Clubs.get().walk((k, v) -> {
            logger.debug("the clubId={}, clubInfo={}", k, v.toString());
            return true;
        });
    }
}
