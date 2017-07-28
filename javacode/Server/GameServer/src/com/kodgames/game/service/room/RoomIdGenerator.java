package com.kodgames.game.service.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by 001 on 2017/4/6.
 * RoomId生成器
 *
 * 功能与目标:
 * 1. 保证生成不重复的RoomId
 * 2. 最近释放的RoomId不要立即被重用
 * 3. 如果调用方未正确释放RoomId导致房间号不够用, 程序要能检查并处理
 */
public final class RoomIdGenerator
{
    private static Logger logger = LoggerFactory.getLogger(RoomIdGenerator.class);
    private static Deque<Integer> freeNumbers = null;                   // 未使用的房间号
    private static Set<Integer> usingNumbers = new LinkedHashSet<>();   // 使用中的房号

    private static final int checkUnusedThreshold = 9000;               // 检查未释放房间的阈值 (少于这么多个可用房间时开始检查)
    private static final int checkUnusedCount = 1000;                   // 每次检查未释放房间的数量 (一次不要检查太多, 不让线程等待太久)

    private static final int roomIdRange = 1000000;                     // 房间号最大值 (不含)
    private static final int roomIdMin = 100000;                        // 房间号最小值 (含)

    private static RoomStatusCheckFunc roomCheckFunc = null;

    public static void Init(RoomStatusCheckFunc func)
    {
        roomCheckFunc = func;

        // 生成所有的RoomId, 将其打乱顺序
        int count = roomIdRange - roomIdMin;
        List<Integer> aList = new ArrayList<>(count);
        for (int i = roomIdMin; i < roomIdRange; i++)
            aList.add(i);
        Collections.shuffle(aList);

        // 用乱序的RoomId列表构造双端队列
        freeNumbers = new ArrayDeque<>(aList);

        // 常驻内存的Deque及大数组的构造过程 测试内存和时间开销
        // 内存占用: 4.5M
        // 时间消耗: 0.1s
        // 内存占用测试方法: 使用VisualVM监视进程的堆内存占用, 比较开启和关闭该函数时的内存差
    }

    /**
     * 获取一个RoomId
     * @return
     */
    public synchronized static int Get()
    {
        // 如果可用房间号为空, 检查一下是否有房间号未正确释放
        if (freeNumbers.isEmpty())
        {
            logger.warn("RoomIdGenerator freeNumbers queue is empty");
            // 这里检查的数量比较大, 避免后面还要频繁检查
            CheckUnusedRooms(checkUnusedThreshold * 2);
        }

        // 从freeNumbers的头部取, 并加入使用中队列
        do
        {
            if (freeNumbers.isEmpty())
            {
                logger.error("RoomIdGenerator freeNumbers queue is empty, and can't get unused rooms");
                return 0;
            }

            Integer i = freeNumbers.pollFirst();
            if (!usingNumbers.add(i))
            {
                // 添加到set失败表示该roomId已经在usingNumbers中
                logger.error("roomId {} already in usingNumbers, but get it from freeNumbers again", i);
                continue;
            }
            return i;
        } while (true);
    }

    /**
     * 释放RoomId
     * @param roomId
     */
    public synchronized static void Free(int roomId)
    {
        // 回收房间号时检查剩余可用的房间号
        // 如果数量很少, 则检查是否有房间号未正确释放, 否则回收的房间号很快会被复用
        if (freeNumbers.size() < checkUnusedThreshold)
        {
            CheckUnusedRooms(checkUnusedCount);
        }

        if (!usingNumbers.remove(roomId))
        {
            logger.error("roomId {} not exist in usingNumbers when Free it", roomId);
        }
        else
        {
            freeNumbers.addLast(roomId);
        }
    }

    private static void CheckUnusedRooms(int checkCount)
    {
        // usingNumbers是保证顺序的Set容器, 从前往后检查, 该房间号是否在使用中
        Iterator<Integer> linkedSetItr = usingNumbers.iterator();
        int count = 0;
        List<Integer> unusedRooms = new ArrayList<>();
        while (linkedSetItr.hasNext())
        {
            Integer i = linkedSetItr.next();
            if (!roomCheckFunc.isRoomInUsing(i))
            {
                unusedRooms.add(i);
                logger.error("check roomId {}, found it not in using, but still in usingNumbers list", i);
            }

            if (count++ > checkCount)
                break;
        }

        for (Integer i : unusedRooms)
        {
            usingNumbers.remove(i);
            freeNumbers.addLast(i);
        }
    }
}
