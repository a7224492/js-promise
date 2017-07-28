package com.kodgames.game.service.room;

import com.kodgames.corgi.core.service.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 001 on 2017/4/6.
 * 用于检查roomId是否在使用中的接口实现
 */
public class RoomStatusCheckFuncImpl implements RoomStatusCheckFunc
{
    private static Logger logger = LoggerFactory.getLogger(RoomStatusCheckFuncImpl.class);
    private RoomService service = null;

    public boolean isRoomInUsing(int roomId)
    {
        if (service == null)
        {
            service = ServiceContainer.getInstance().getPublicService(RoomService.class);
            if (service == null)
            {
                // 异常状态下返回true, 表示这个roomId正在使用中, 不要删除这个roomId
                logger.error("get RoomService failed");
                return true;
            }
        }

        if (service.getRoom(roomId) != null)
            return true;
        return false;
    }
}
