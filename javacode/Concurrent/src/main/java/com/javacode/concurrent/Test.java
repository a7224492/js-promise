package com.javacode.concurrent;

/**
 * Created by jiangzhen on 2018/3/24
 */
public class Test {
    private static int num;
    private static boolean ready;

    public static void main(String[] args) {
        new Thread(()->{
            while (!ready) {
                Thread.yield();
            }
            System.out.println(num);
        }).start();

        num = 43;
        ready = true;
    }
}
