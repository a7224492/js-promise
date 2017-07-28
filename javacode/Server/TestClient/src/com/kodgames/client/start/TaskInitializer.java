package com.kodgames.client.start;

import com.kodgames.client.service.task.TaskService;
import com.kodgames.corgi.core.service.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 初始化定时器任务
 *
 * @author jiangzhen
 */
public class TaskInitializer {
    private static Logger logger = LoggerFactory.getLogger(TaskInitializer.class);
    private static TaskInitializer ti = null;

    private TaskInitializer() {
    }

    public static TaskInitializer getInstance() {
        if (ti == null) {
            synchronized (TaskInitializer.class) {
                if (ti == null) {
                    ti = new TaskInitializer();
                }
            }
        }

        return ti;
    }

    public void init() {
        TaskService service = ServiceContainer.getInstance().getPublicService(TaskService.class);
        new Thread(() -> {
            while (true) {
                try {
                    service.update();
                    Thread.sleep(10);
                } catch (Exception e) {
                    logger.error("exception at task update {}", e.getMessage());
                }
            }
        }).start();
    }
}
