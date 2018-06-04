package com.mycode.dp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by jiangzhen
 */
public class ZeroOnePackageTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testListSet() {
        List<Integer> testList = new ArrayList<>();
        testList.add(3);
        testList.add(6);
        testList.add(8);
        testList.add(10);

        for (int i = 0; i < testList.size(); ++i) {
            testList.add(10);
        }

        System.out.println(testList);
    }

}