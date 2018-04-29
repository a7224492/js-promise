package com.mycode.leetcode;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by jiangzhen on 2018/3/19.
 * function:
 */
public class ArrayHelperTest {
    @Test
    public void intList2String() throws Exception {
        List<Integer> dataList = Arrays.asList(1,22,3);
        List<String> strList = Arrays.asList("id", "age", "grade");
        String format = "%d(%s)";
        String s = ArrayHelper.intList2String(dataList, strList, format);
        System.out.println(s);

        dataList = Arrays.asList(1,22,3,2,23,3);
        s = ArrayHelper.intList2String(dataList, strList, format);
        System.out.println(s);
    }

    @org.junit.Test
    public void maxElemList() throws Exception {
        List<Integer> l = Arrays.asList(1,2,3,4,5,6,7);
        List<Integer> result = ArrayHelper.maxElemList(l, 0);
        assertSame(Arrays.asList(7), result);
    }

}