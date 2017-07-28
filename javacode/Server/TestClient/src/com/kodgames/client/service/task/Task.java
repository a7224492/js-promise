package com.kodgames.client.service.task;

import com.kodgames.client.common.Role;
import com.kodgames.client.common.Room;

/**
 * Created by liufei on 2017/6/27.
 */
public interface Task {
    // 执行任务 (room参数可能为空)
    void process(Role role, Room room);
}
