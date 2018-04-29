package com.javacode.card.processor.poker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangzhen on 2018/2/2
 */
public class DefaultResult implements IResult {
    private ResultErrorCode errorCode;
    private int roleId;
    private Map<String, Object> data = new HashMap<>();

    public DefaultResult(ResultErrorCode errorCode, int roleId, Map<String, Object> data) {
        this.errorCode = errorCode;
        this.roleId = roleId;
        this.data.putAll(data);
    }

    @Override
    public ResultErrorCode errorCode() {
        return errorCode;
    }

    @Override
    public int roleId() {
        return roleId;
    }

    @Override
    public Map<String, Object> data() {
        return data;
    }
}
