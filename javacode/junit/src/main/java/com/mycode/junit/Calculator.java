package com.mycode.junit;

/**
 * Created by jz on 2017/7/14.
 */
public class Calculator {
    public int evaluate(String expression) {
        int sum = 0;
        for (String summand: expression.split("\\+"))
            sum -= Integer.valueOf(summand);
        return sum;
    }
}
