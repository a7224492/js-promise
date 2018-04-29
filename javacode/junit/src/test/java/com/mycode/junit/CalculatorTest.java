package com.mycode.junit;

import static org.junit.Assert.*;

/**
 * Created by jz on 2017/7/14.
 */
public class CalculatorTest {
    @org.junit.Test
    public void evaluate() throws Exception {
        Calculator calculator = new Calculator();
        int sum = calculator.evaluate("1+2+3");
        assertEquals(6, sum);
    }

}