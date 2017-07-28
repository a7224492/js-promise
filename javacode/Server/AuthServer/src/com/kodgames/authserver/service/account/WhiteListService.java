package com.kodgames.authserver.service.account;

import com.kodgames.corgi.core.service.PublicService;
import io.netty.util.internal.ConcurrentSet;
import limax.zdb.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.WhiteList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by liufei on 2017/7/10.
 * 白名单管理
 */
public class WhiteListService extends PublicService {
    private static final long serialVersionUID = 8968530468026717523L;
    private static final Logger logger = LoggerFactory.getLogger(WhiteListService.class);

    private static int WHITE_LIST_KEY = 1;
    private static int BLACK_LIST_KEY = 2;

    // 保存白名单列表, 用于快速搜索 zdb中保存的是list, 搜索效率太低
    private Set<Integer> accounts = new ConcurrentSet<>();

    // 是否开启白名单验证 (服务器启动后默认开启, 需要通过GMT关闭)
    private boolean enabled = true;

    @Override
    public int init() {
        Procedure.call(() -> {
            WhiteList list = table.Account_white_list.update(WHITE_LIST_KEY);
            if (list == null)
                list = table.Account_white_list.insert(WHITE_LIST_KEY);

            for (Integer id : list.getList()) {
                accounts.add(id);
            }

            return true;
        });

        return 0;
    }

    // 检查账号是否在白名单列表中
    public boolean inList(Integer id) {
        return accounts.contains(id);
    }

    // 添加新白名单账号
    public boolean addAccount(Integer id) {
        if (accounts.contains(id)) {
            logger.warn("account {} already in white list", id);
            return false;
        }

        Procedure.call(() -> {
            WhiteList list = table.Account_white_list.update(WHITE_LIST_KEY);
            if (list == null)
                list = table.Account_white_list.insert(WHITE_LIST_KEY);
            list.getList().add(id);
            return true;
        });

        logger.warn("add account {} to white list", id);
        return  accounts.add(id);
    }

    // 删除白名单账号
    public boolean removeAccount(Integer id) {
        if (!accounts.contains(id)) {
            logger.warn("account {} not in white list", id);
            return false;
        }

        Procedure.call(() -> {
            WhiteList list = table.Account_white_list.update(WHITE_LIST_KEY);
            if (list == null)
                list = table.Account_white_list.insert(WHITE_LIST_KEY);
            list.getList().remove(id);
            return true;
        });

        logger.warn("remove account {} from white list", id);
        return accounts.remove(id);
    }

    public List<Integer> getList() {
        List<Integer> list = new ArrayList<>();
        list.addAll(accounts);
        return list;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean value) {
        logger.warn("set white list service to {}, original {}", value, enabled);
        enabled = value;
    }
}
