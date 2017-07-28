package com.kodgames.game.service.security;

import com.kodgames.corgi.core.constant.DateTimeConstants;
import com.kodgames.corgi.core.service.ServiceContainer;
import limax.xmlconfig.Service;
import limax.zdb.Procedure;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.CombatInfo;
import xbean.RoleInfo;
import xbean.RoleRecord;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by Liufei on 2017/5/6.
 */
public class SecurityServiceTest {
    private static Logger logger = LoggerFactory.getLogger(SecurityServiceTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Service.run(Object.class.getResource("/zdb_config.xml").getPath());
            }
        }).start();
        Thread.sleep(3000);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        logger.error("stop stop stop stop stop stop");
        Service.stop();
    }

    @Before
    public void setUp()
    {
        ResetZDB();
    }

    @Test
    public void getGroup() throws Exception {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<IPSelectConfig isOpen=\"true\" defaultGroupName=\"defaultGroup\">" +
                "    <IPGroup name=\"group1\" isActive=\"true\">" +
                "        <GroupCondition type=\"REGISTER\" compareType=\"LESS\" intMinValue=\"7\" intMaxValue=\"7\" strValue=\"\" />" +
                "        <GroupCondition type=\"REGION\" compareType=\"EQUAL\" intMinValue=\"0\" intMaxValue=\"0\" strValue=\"贵阳\" />" +
                "    </IPGroup>" +
                "    <IPGroup name=\"group2\" isActive=\"true\">" +
                "        <GroupCondition type=\"REGISTER\" compareType=\"LESS\" intMinValue=\"7\" intMaxValue=\"7\" strValue=\"\" />" +
                "        <GroupCondition type=\"REGION\" compareType=\"NE\" intMinValue=\"0\" intMaxValue=\"0\" strValue=\"贵阳\" />" +
                "    </IPGroup>" +
                "    <IPGroup name=\"group3\" isActive=\"true\">" +
                "        <GroupCondition type=\"COMBAT\" compareType=\"GE\" intMinValue=\"100\" intMaxValue=\"100\" strValue=\"\" />" +
                "        <GroupCondition type=\"REGION\" compareType=\"EQUAL\" intMinValue=\"0\" intMaxValue=\"0\" strValue=\"贵阳\" />" +
                "    </IPGroup>" +
                "</IPSelectConfig>";
        SecurityGroupConfig.getInstance().reload(content, null);

        // roleId 1 返回 group2
        int roleId = 1;
        String ip = "202.201.48.16";
        SecurityService service = ServiceContainer.getInstance().getPublicService(SecurityService.class);
        Procedure.call(() -> {
            String group = service.getGroup(roleId, ip);
            assertEquals("group2", group);
            return true;
        });

        // roleId 4 返回 defaultGroup
    }

    @Test
    public void testCombatTimes() throws Exception {
        // 规则 注册时间小于7天 && 7天总局数 >= 1
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<IPSelectConfig isOpen=\"true\" defaultGroupName=\"defaultGroup\">" +
                "    <IPGroup name=\"group1\" isActive=\"true\">" +
                "        <GroupCondition type=\"REGISTER\" compareType=\"LESS\" intMinValue=\"7\" intMaxValue=\"7\" strValue=\"\" />" +
                "        <GroupCondition type=\"COMBAT\" compareType=\"GE\" intMinValue=\"1\" intMaxValue=\"1\" strValue=\"\" />" +
                "    </IPGroup>" +
                "</IPSelectConfig>";
        SecurityGroupConfig.getInstance().reload(content, null);

        int roleId = 1;
        String ip = "202.201.48.16";
        SecurityService service = ServiceContainer.getInstance().getPublicService(SecurityService.class);
        Procedure.call(() -> {
            // roleId 1 返回 defaultGroup
            String group = service.getGroup(roleId, ip);
            assertEquals("defaultGroup", group);

            // 加一局游戏后, 最近7天局数还是为0, 返回 defaultGroup
            service.updateCombatRecords(roleId);

            group = service.getGroup(roleId, ip);
            assertEquals( "defaultGroup", group);
            return true;
        });
    }

    @Test
    public void testCombatTimesUpdate() throws Exception {
        // 规则 注册时间大于7天 && 7天总局数 >= 7
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<IPSelectConfig isOpen=\"true\" defaultGroupName=\"defaultGroup\">" +
                "    <IPGroup name=\"group1\" isActive=\"true\">" +
                "        <GroupCondition type=\"REGISTER\" compareType=\"GE\" intMinValue=\"7\" intMaxValue=\"7\" strValue=\"\" />" +
                "        <GroupCondition type=\"COMBAT\" compareType=\"GREATER\" intMinValue=\"7\" intMaxValue=\"7\" strValue=\"\" />" +
                "    </IPGroup>" +
                "</IPSelectConfig>";
        SecurityGroupConfig.getInstance().reload(content, null);

        int roleId = 3;
        String ip = "202.201.48.16";
        SecurityService service = ServiceContainer.getInstance().getPublicService(SecurityService.class);
        Procedure.call(() -> {
            // roleId 3 返回 defaultGroup (此时有7条记录)
            String group = service.getGroup(roleId, ip);
            assertEquals("defaultGroup", group);

            // 加一局游戏后, 最近7天的局数还是7, 返回 defaultGroup
            service.updateCombatRecords(roleId);

            group = service.getGroup(roleId, ip);
            assertEquals( "defaultGroup", group);

            // 再加一局游戏后, 最近7天局数还是7, 返回 defaultGroup
            service.updateCombatRecords(roleId);

            group = service.getGroup(roleId, ip);
            assertEquals( "defaultGroup", group);

            return true;
        });
    }

    @Test
    public void resetGroups() throws Exception {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
"<IPSelectConfig isOpen=\"true\" defaultGroupName=\"defaultGroup\">" +
"    <IPGroup name=\"group1\" isActive=\"true\">" +
"        <GroupCondition type=\"REGISTER\" compareType=\"LESS\" intMinValue=\"7\" intMaxValue=\"7\" strValue=\"\" />" +
"        <GroupCondition type=\"REGION\" compareType=\"EQUAL\" intMinValue=\"0\" intMaxValue=\"0\" strValue=\"贵阳\" />" +
"    </IPGroup>" +
"    <IPGroup name=\"group2\" isActive=\"true\">" +
"        <GroupCondition type=\"REGISTER\" compareType=\"LESS\" intMinValue=\"7\" intMaxValue=\"7\" strValue=\"\" />" +
"        <GroupCondition type=\"REGION\" compareType=\"NE\" intMinValue=\"0\" intMaxValue=\"0\" strValue=\"贵阳\" />" +
"    </IPGroup>" +
"    <IPGroup name=\"group3\" isActive=\"true\">" +
"        <GroupCondition type=\"COMBAT\" compareType=\"GE\" intMinValue=\"100\" intMaxValue=\"100\" strValue=\"\" />" +
"        <GroupCondition type=\"REGION\" compareType=\"EQUAL\" intMinValue=\"0\" intMaxValue=\"0\" strValue=\"贵阳\" />" +
"    </IPGroup>" +
"</IPSelectConfig>";

        SecurityGroupConfig.getInstance().reload(content, null);
    }

    private void ResetZDB()
    {
        Procedure.call(() -> {
            // roleId: 1, 注册时间1天前
            int roleId = 1;

            RoleInfo roleInfo = table.Role_info.update(roleId);
            if (roleInfo == null)
                roleInfo = table.Role_info.insert(roleId);
            roleInfo.setAccountId(roleId);
            roleInfo.setRoleCreateTime(System.currentTimeMillis() - DateTimeConstants.DAY);

            RoleRecord roleRecord = table.Role_records.update(roleId);
            if (roleRecord == null)
                roleRecord = table.Role_records.insert(roleId);
            roleRecord.setRole_id(roleId);
            roleRecord.setAgencyId(0);
            roleRecord.getCombatTimes().clear();

            // roleId: 2, 注册时间8天前
            roleId = 2;
            roleInfo = table.Role_info.update(roleId);
            if (roleInfo == null)
                roleInfo = table.Role_info.insert(roleId);
            roleInfo.setAccountId(roleId);
            roleInfo.setRoleCreateTime(System.currentTimeMillis() - DateTimeConstants.DAY * 8);

            roleRecord = table.Role_records.update(roleId);
            if (roleRecord == null)
                roleRecord = table.Role_records.insert(roleId);
            roleRecord.setRole_id(roleId);
            roleRecord.setAgencyId(0);
            roleRecord.getCombatTimes().clear();

            // roleId: 3, 注册时间10天前, 有前7天的局数记录
            roleId = 3;
            roleInfo = table.Role_info.update(roleId);
            if (roleInfo == null)
                roleInfo = table.Role_info.insert(roleId);
            roleInfo.setAccountId(roleId);
            roleInfo.setRoleCreateTime(System.currentTimeMillis() - DateTimeConstants.DAY * 10);

            roleRecord = table.Role_records.update(roleId);
            if (roleRecord == null)
                roleRecord = table.Role_records.insert(roleId);
            roleRecord.setRole_id(roleId);
            roleRecord.setAgencyId(0);
            roleRecord.getCombatTimes().clear();

            for (int i = 7; i > 0; i--) {
                CombatInfo info = new CombatInfo();
                info.setTime(System.currentTimeMillis() - DateTimeConstants.DAY * i);
                info.setCount(1);
                roleRecord.getCombatTimes().add(info);
            }

            // roleId: 4, 注册时间7天前
            roleId = 4;
            roleInfo = table.Role_info.update(roleId);
            if (roleInfo == null)
                roleInfo = table.Role_info.insert(roleId);
            roleInfo.setAccountId(roleId);
            roleInfo.setRoleCreateTime(System.currentTimeMillis() - DateTimeConstants.DAY * 7);

            roleRecord = table.Role_records.update(roleId);
            if (roleRecord == null)
                roleRecord = table.Role_records.insert(roleId);
            roleRecord.setRole_id(roleId);
            roleRecord.setAgencyId(0);
            roleRecord.getCombatTimes().clear();

            return true;
        });
    }
}