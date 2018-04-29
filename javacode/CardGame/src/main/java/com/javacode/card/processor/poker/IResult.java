package com.javacode.card.processor.poker;

import java.util.Map;

/**
 * Created by jiangzhen on 2018/2/1.
 * function:
 */
public interface IResult {
    /**
     * 错误码
     * @return 结果的错误码
     */
    ResultErrorCode errorCode();

    /**
     * 本次操作的roleId
     */
    int roleId();

    /**
     * 本次操作结果附带的数据
     */
    Map<String, Object> data();
}
