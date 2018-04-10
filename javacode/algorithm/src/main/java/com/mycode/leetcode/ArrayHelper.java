package com.mycode.leetcode;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jiangzhen on 2018/3/17
 */
public class ArrayHelper {

    /**
     * 从一个数组中取出最大的那些元素，构成一个list返回
     * @param l 需要遍历的list
     * @return 最大元素构成的list
     * TODO 未完成
     */
    public static <T extends Comparable> List<Integer> maxElemList(List<T> l, T min) {
        T max = min;
        Map<T, List<Integer>> m = new HashMap<>();

        for (int i = 0; i < l.size(); ++i) {
            T e = l.get(i);

            m.computeIfAbsent(e, k -> new ArrayList<>()).add(i);

            if (max.compareTo(e) < 0) {
                max = e;
            }
        }

        return m.get(max);
    }

    /**
     * 把dataList和strList按照给定的字符串格式合成一个String返回，比如：
     *      dataList: [1,22,3]
     *      strList: ["id", "age", "grade"]
     *      format: %d(%s),%d表示dataList中的一个元素，%s表示strList中的一个元素
     *      合成
     *      String: "[1(id), 22(age), 3(grade)]"
     *
     *      或者
     *      dataList: [1,22,3,2,22,4]
     *      strList: ["id", "age", "grade"]
     *      format: %d(%s),%d表示dataList中的一个元素，%s表示strList中的一个元素
     *      合成
     *      String: "[1(id), 22(age), 3(grade), 2(id), 22(age), 4(grade)]"
     *
     * @param format 格式化字符串,%d表示dataList中的元素，%s指strList中的元素
     * @return 合成之后的字符串，如果不符合合成条件，则返回dataList的字符串
     */
    public static String intList2String(List<Integer> dataList, List<String> strList, String format) {
        if (dataList == null || strList == null || format == null) {
            return "";
        }
        if (dataList.size() % strList.size() != 0) {
            return dataList.toString();
        }

        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < dataList.size(); ++i) {
            int strIndex = i % strList.size();
            result.append(String.format(format, dataList.get(i), strList.get(strIndex)));
            if (i != dataList.size()-1) {
                result.append(",");
            }
        }

        return result.append("]").toString();
    }

    public static void main(String[] args) {
        Lock a = new ReentrantLock();
        Lock b = new ReentrantLock();

        new Thread(()->{
            Counter counter = new Counter(1);

            System.out.println(Thread.currentThread()+": acquire lock a");
            a.lock();
            try {
                System.out.println(Thread.currentThread()+": get lock a");
                Thread.sleep(1000);

                System.out.println(Thread.currentThread()+": acquire lock b");
                b.lock();
                try {
                    System.out.println(Thread.currentThread()+": get lock b");
                } finally {
                    b.unlock();
                    System.out.println(Thread.currentThread()+": release lock b");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                a.unlock();
                System.out.println(Thread.currentThread()+": release lock a");
            }
        }).start();

        System.out.println(Thread.currentThread()+": acquire lock b");
        b.lock();
        try {
            System.out.println(Thread.currentThread()+": get lock b");
            Thread.sleep(1000);

            System.out.println(Thread.currentThread()+": acquire lock a");
            a.lock();
            try {
                System.out.println(Thread.currentThread()+": get lock a");
            } finally {
                a.unlock();
                System.out.println(Thread.currentThread()+": release lock a");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            b.unlock();
            System.out.println(Thread.currentThread()+": release lock b");
        }
    }

    static class Counter {
        private int value;

        Counter(int value) {
            this.value = value;
        }

        public void inc() {
            ++value;
        }
    }
}