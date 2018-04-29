package com.javacode.card;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangzhen on 2018/3/1
 */
public class Main {
    public static void main(String[] args) {
        Map<Byte, String> m = new HashMap<>();
        m.put((byte)2, "jiangzhen");

        String s = m.get(2);
        System.out.println(s);
    }
}